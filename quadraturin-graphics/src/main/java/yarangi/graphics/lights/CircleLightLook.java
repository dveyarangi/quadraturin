package yarangi.graphics.lights;

import java.nio.IntBuffer;
import java.util.List;
import java.util.Set;

import javax.media.opengl.GL;

import yarangi.graphics.colors.Color;
import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.shaders.IShader;
import yarangi.graphics.shaders.ShaderFactory;
import yarangi.graphics.textures.TextureUtils;
import yarangi.graphics.textures.TextureUtils.FBOHandle;
import yarangi.math.BitUtils;
import yarangi.math.Vector2D;

/**
 * Generates a rough approximation of 2D shadows texture. 
 * 
 * TODO: generalize penumbrae shader to generate correct light distribution (should suply it a single large shadow polygon
 * and light source coords and size. 
 * 
 * TODO: add capabilities detection (FBO, fragment shaders)
 * 
 * @author dveyarangi
 *
 * @param <K>
 */
public class CircleLightLook <K extends IEntity> implements Look <K>
{
	
	private IShader penumbraShader;
	private IShader lightShader;
	
	private int textureSize;
	private IntBuffer viewport = IntBuffer.allocate(4);
	
	private FBOHandle fbo;
	
	private int lightTexture;
	
	private Color color;
	
	public CircleLightLook(Color color)
	{
		this.color = color;
	}

	
	public void init(GL gl, K entity, IRenderingContext context) {
		
		// rounding texture size to power of 2:
		textureSize = BitUtils.po2Ceiling((int)(entity.getSensor().getRadius()*2.));
		
		// creating empty texture for lighting:
		lightTexture = TextureUtils.createEmptyTexture2D(gl, textureSize, textureSize, false);
		
		// creating 
//		int depthBuffer = TextureUtils.createFBODepthBuffer(gl);
		fbo = TextureUtils.createFBO(gl, lightTexture, TextureUtils.ILLEGAL_ID);
		
		// preparing shaders:
		ShaderFactory factory = context.getPlugin("shaders");
		
		lightShader = factory.getShader("light");
		penumbraShader = factory.getShader("penumbra");
	}

	public void render(GL gl, double time, K entity, IRenderingContext context) 
	{
		// TODO: store and restore blending mode
		if(context.isForEffect())
			return;
		
		// saving modes:
		gl.glPushAttrib(GL.GL_VIEWPORT_BIT | GL.GL_ENABLE_BIT);	
		
		// retrieving viewport:
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport);
		
		// transforming the FBO plane to fit the light source location and scale:
		gl.glMatrixMode(GL.GL_MODELVIEW); gl.glPushMatrix();  gl.glLoadIdentity();
		
		gl.glMatrixMode(GL.GL_PROJECTION); gl.glPushMatrix(); gl.glLoadIdentity();
		
		gl.glScaled(((double)viewport.get(2)/(double)textureSize),( (double)viewport.get(3)/(double)textureSize), 0);
		gl.glViewport(0, 0, textureSize, textureSize);
		gl.glOrtho(-viewport.get(2)/2, viewport.get(2)/2, -viewport.get(3)/2, viewport.get(3)/2, -1, 1);
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		// binding FBO:
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fbo.getFboId());
		
		gl.glDisable(GL.GL_DEPTH_TEST);
		
		// clearing frame buffer:
		gl.glClearColor(0,0,0,0);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT/* | GL.GL_DEPTH_BUFFER_BIT*/);
		
		// shadow blending setting:
		gl.glBlendEquation( GL.GL_MAX );
		gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_COLOR);

		if(entity.getSensor() != null)
		{
			Set <IEntity> entities = entity.getSensor().getEntities();
			List <Vector2D> shadowEdge;
			Vector2D sourceLoc = entity.getArea().getRefPoint();
		// drawing red polygones for full shadows and penumbra:
			for(IEntity caster : entities)
			{
				if(!caster.getLook().isCastsShadow())
					continue;
				
				shadowEdge = caster.getArea().getDarkEdge( sourceLoc );
				if(shadowEdge.size() < 2)
					continue;
				
				// TODO: raycasting?
				Vector2D casterLeft  = shadowEdge.get(0);
				Vector2D casterRight = shadowEdge.get(shadowEdge.size()-1);
				
				Vector2D sourceLeftLeft = casterLeft.left().normalize().multiply(entity.getArea().getMaxRadius());
				Vector2D sourceLeftRight = casterLeft.right().normalize().multiply(entity.getArea().getMaxRadius());
				Vector2D sourceRightLeft = casterRight.left().normalize().multiply(entity.getArea().getMaxRadius());
				Vector2D sourceRightRight = casterRight.right().normalize().multiply(entity.getArea().getMaxRadius());
	//			Vector2D casterLeft  = left.mul(caster.getAABB().r).plus(distance);
	//			Vector2D casterRight = right.mul(caster.getAABB().r).plus(distance);
				
	//			Vector2D sourceLeft = left.mul(entity.getAABB().r);
	//			Vector2D sourceRight = right.mul(entity.getAABB().r);
				
				Vector2D fullLeft = casterLeft.normal();
				Vector2D fullRight = casterRight.normal();
				
				// penumbra:
				Vector2D softLeft = casterLeft.minus(sourceLeftRight).normalize();
				Vector2D softRight = casterRight.minus(sourceRightLeft).normalize();
				
				// TODO: calc real shadow extent:
				Vector2D casterLeftOutter  = casterLeft.plus(sourceLeftLeft.mul(10*10));
				Vector2D casterRightOutter = casterRight.plus(sourceRightRight.mul(10*10));
	//			Vector2D casterLeftOutter  = left.mul(caster.getAABB().r*10).plus(distance);
	//			Vector2D casterRightOutter = right.mul(caster.getAABB().r*10).plus(distance);
				
				// drawing full shadow
				gl.glColor4f(1.0f, 0.0f, 0.0f, 0.0f);
				gl.glBegin(GL.GL_QUADS);
				gl.glVertex2f((float)(casterLeft.x()),     (float)casterLeft.y());
				gl.glVertex2f((float)(casterLeft.x()  + fullLeft.x()  * textureSize),  (float)(casterLeft.y() + fullLeft.y() * textureSize));
				gl.glVertex2f((float)(casterRight.x() + fullRight.x() * textureSize), (float)(casterRight.y() + fullRight.y() * textureSize));
				gl.glVertex2f((float)(casterRight.x()),    (float)casterRight.y());
				gl.glEnd();
				
				// drawing penumbra. the shader calculates penumbra gradient based on pixel angle:
				// TODO: to costly, replace with prepared texture:

				penumbraShader.begin(gl);
				gl.glBegin(GL.GL_QUADS);
				gl.glTexCoord2f(0, 1);gl.glVertex2f((float)(casterLeftOutter.x()),     (float)casterLeftOutter.y());
				gl.glTexCoord2f(1, 1);gl.glVertex2f((float)(casterLeft.x()+softLeft.x()*textureSize),  (float)(casterLeft.y()+softLeft.y()*textureSize));
				gl.glTexCoord2f(1, 0);gl.glVertex2f((float)(casterLeft.x()+fullLeft.x()*textureSize),  (float)(casterLeft.y()+fullLeft.y()*textureSize));
				gl.glTexCoord2f(0, 0);gl.glVertex2f((float)(casterLeft.x()),     (float)casterLeft.y());
				gl.glEnd();
				
				gl.glBegin(GL.GL_QUADS);
				gl.glTexCoord2f(0, 1);gl.glVertex2f((float)(casterRightOutter.x()),    (float)casterRightOutter.y());
				gl.glTexCoord2f(1, 1);gl.glVertex2f((float)(casterRight.x()+softRight.x()*textureSize), (float)(casterRight.y()+softRight.y()*textureSize));
				gl.glTexCoord2f(1, 0);gl.glVertex2f((float)(casterRight.x()+fullRight.x()*textureSize), (float)(casterRight.y()+fullRight.y()*textureSize));
				gl.glTexCoord2f(0, 0);gl.glVertex2f((float)(casterRight.x()),    (float)casterRight.y());
				gl.glEnd();
				penumbraShader.end(gl);
				
			}
		}
		
		// exiting framebuffer:
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// restoring to original view
		gl.glMatrixMode(GL.GL_PROJECTION); gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW); gl.glPopMatrix();
		gl.glPopAttrib();
		
		// setting blending mode for lights:
		
//		gl.glBlendEquationSeparate(GL.GL_FUNC_ADD, GL.GL_MAX);
//		gl.glBlendFuncSeparate(GL.GL_SRC_COLOR, GL.GL_DST_COLOR, GL.GL_ZERO, GL.GL_ONE);
//		gl.glBlendFuncSeparate(GL.GL_ONE, GL.GL_ONE, GL.GL_ONE, GL.GL_ONE);		
		gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
		gl.glBlendEquation(GL.GL_FUNC_ADD);
//		System.out.println(textureSize);
		// drawing shadow map:
		
		// drawing light:
		// TODO: shader too costly, replace with prepared texture:
		gl.glBindTexture(GL.GL_TEXTURE_2D, lightTexture);
		lightShader.begin(gl);
		lightShader.setFloat4Uniform(gl, "color", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
		lightShader.setFloat1Uniform(gl, "height", 2f);
		lightShader.setFloat1Uniform(gl, "size", 0.01f);
		lightShader.setFloat1Uniform(gl, "cutoff",5f);
		renderTexture(gl, entity);
		lightShader.end(gl);
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		gl.glBlendEquation( GL.GL_FUNC_ADD );
//		gl.glBlendEquationSeparate(GL.GL_FUNC_ADD, GL.GL_FUNC_ADD);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
/*		gl.glColor3f(1, 1, 1);
		gl.glBegin(GL.GL_LINE_STRIP);
		 gl.glVertex2f((float)(-textureSize/2), (float)(-textureSize/2));
		gl.glVertex2f((float)(+textureSize/2), (float)(-textureSize/2));
		gl.glVertex2f((float)(+textureSize/2), (float)(+textureSize/2));
		gl.glVertex2f((float)(-textureSize/2), (float)(+textureSize/2));
		gl.glVertex2f((float)(-textureSize/2), (float)(-textureSize/2));
		gl.glEnd();*/
		
		// shadow blending setting:


	}

	private void renderTexture(GL gl, IEntity entity)
	{
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		gl.glDisable(GL.GL_TEXTURE_GEN_S);
		gl.glDisable(GL.GL_TEXTURE_GEN_T);
		gl.glBegin(GL.GL_QUADS);
//		System.out.println(textureSize/2);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-textureSize/2, -textureSize/2, -0.1f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(+textureSize/2, -textureSize/2,  -0.1f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(+textureSize/2, +textureSize/2,  -0.1f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-textureSize/2, +textureSize/2,  -0.1f);
		gl.glEnd();
		
	}

	public void destroy(GL gl, K entity, IRenderingContext context) 
	{

		TextureUtils.destroyFBO(gl, fbo);
	}


	@Override
	public boolean isCastsShadow() {
		return false;
	}
	
	public Color getColor() { return color ; }


	@Override
	public float getPriority()
	{
		return 0f;
	}
}
