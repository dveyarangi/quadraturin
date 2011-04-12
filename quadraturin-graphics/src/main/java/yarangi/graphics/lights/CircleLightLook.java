package yarangi.graphics.lights;

import java.nio.IntBuffer;
import java.util.Set;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.effects.TextureUtils;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.utils.colors.Color;
import yarangi.graphics.utils.shaders.IShader;
import yarangi.graphics.utils.shaders.ShaderFactory;
import yarangi.math.Angles;
import yarangi.math.BitUtils;
import yarangi.math.Vector2D;
import yarangi.spatial.ISpatialObject;

/**
 * TODO: add capabilities detection (FBO, fragment shaders)
 * @author dveyarangi
 *
 * @param <K>
 */
public class CircleLightLook <K extends CircleLightEntity> implements Look <K>
{
	
	private IShader shadowShader;
	private IShader lightShader;
	
	private int textureSize;
	private IntBuffer viewport = IntBuffer.allocate(4);
	
	private int fbo;
	
	private int lightTexture;

	
	public void init(GL gl, K entity) {
		
		textureSize = BitUtils.po2Ceiling((int)(entity.getLightRadius()*2));
		lightTexture = TextureUtils.createEmptyTexture2D(gl, textureSize, textureSize, false);
		int depthBuffer = TextureUtils.createFBODepthBuffer(gl);
		fbo = TextureUtils.createFBO(gl, lightTexture, depthBuffer);
		
		// TODO: move shader initialization elsewhere:
		lightShader = ShaderFactory.getShader("light");
		if(lightShader == null)
		{
			ShaderFactory.registerShaderFile("light", "shaders/light.glsl");	
			lightShader = ShaderFactory.getShader("light");
			lightShader.init(gl);
		}
		shadowShader = ShaderFactory.getShader("shadow");
		if(shadowShader == null)
		{
			ShaderFactory.registerShaderFile("shadow", "shaders/shadowmap.glsl");	
			shadowShader = ShaderFactory.getShader("shadow");
			shadowShader.init(gl);
		}
	}

	public void render(GL gl, double time, K entity, RenderingContext context) 
	{
		if(context.isForEffect())
			return;
		
		Set <ISpatialObject> entities = entity.getEntities();
		
		if(entities == null)
			return;

		// saving modes:
		gl.glPushAttrib(GL.GL_VIEWPORT_BIT | GL.GL_ENABLE_BIT);	
		
		// retrieving viewport:
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport);
		
		// transforming the FBO plane to fit the light source location and scale:
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glScaled(((double)viewport.get(2)/(double)textureSize),( (double)viewport.get(3)/(double)textureSize), 0);
		gl.glViewport(0, 0, textureSize, textureSize);
		
		// binding FBO:
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fbo);
		
		// clearing frame buffer:
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glClearColor(0,0,0,0);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		// shadow blending setting:
		gl.glBlendEquation( GL.GL_MAX );
		gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_COLOR);

		// drawing red polygones for full shadows and penumbra:
		for(ISpatialObject caster : entities)
		{
			if(caster == entity)
				continue;
			Vector2D distance = caster.getAABB().minus(entity.getAABB());
			Vector2D dir = distance.normalize();
			
			Vector2D left = dir.rotate(Angles.PI_div_2);
			Vector2D right = dir.rotate(-Angles.PI_div_2); // new Vector2D( dir.y, -dir.x)

			Vector2D casterLeft  = left.mul(caster.getAABB().r).plus(distance);
			Vector2D casterRight = right.mul(caster.getAABB().r).plus(distance);
			
			Vector2D sourceLeft = left.mul(entity.getAABB().r);
			Vector2D sourceRight = right.mul(entity.getAABB().r);
			
			Vector2D fullLeft = casterLeft.minus(sourceLeft).normalize();
			Vector2D fullRight = casterRight.minus(sourceRight).normalize();
			
			// penumbra:
			Vector2D softLeft = casterLeft.minus(sourceRight).normalize();
			Vector2D softRight = casterRight.minus(sourceLeft).normalize();
			
			Vector2D casterLeftOutter  = left.mul(caster.getAABB().r*10).plus(distance);
			Vector2D casterRightOutter = right.mul(caster.getAABB().r*10).plus(distance);
			// drawing full shadow
			gl.glColor4f(1.0f, 0.0f, 0.0f, 0.0f);
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex2f((float)(casterLeft.x),     (float)casterLeft.y);
			gl.glVertex2f((float)(casterLeft.x  + fullLeft.x  * textureSize),  (float)(casterLeft.y + fullLeft.y * textureSize));
			gl.glVertex2f((float)(casterRight.x + fullRight.x * textureSize), (float)(casterRight.y + fullRight.y * textureSize));
			gl.glVertex2f((float)(casterRight.x),    (float)casterRight.y);
			gl.glEnd();
			
			// drawing penumbra. the shader calculates penumbra gradient based on pixel angle:
			shadowShader.begin(gl);
			gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(0, 1);gl.glVertex2f((float)(casterLeftOutter.x),     (float)casterLeftOutter.y);
			gl.glTexCoord2f(1, 1);gl.glVertex2f((float)(casterLeft.x+softLeft.x*textureSize),  (float)(casterLeft.y+softLeft.y*textureSize));
			gl.glTexCoord2f(1, 0);gl.glVertex2f((float)(casterLeft.x+fullLeft.x*textureSize),  (float)(casterLeft.y+fullLeft.y*textureSize));
			gl.glTexCoord2f(0, 0);gl.glVertex2f((float)(casterLeft.x),     (float)casterLeft.y);
			gl.glEnd();
			
			gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(0, 1);gl.glVertex2f((float)(casterRightOutter.x),    (float)casterRightOutter.y);
			gl.glTexCoord2f(1, 1);gl.glVertex2f((float)(casterRight.x+softRight.x*textureSize), (float)(casterRight.y+softRight.y*textureSize));
			gl.glTexCoord2f(1, 0);gl.glVertex2f((float)(casterRight.x+fullRight.x*textureSize), (float)(casterRight.y+fullRight.y*textureSize));
			gl.glTexCoord2f(0, 0);gl.glVertex2f((float)(casterRight.x),    (float)casterRight.y);
			gl.glEnd();
			shadowShader.end(gl);
			
		}

		// exiting framebuffer:
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);

		// restoring transformations and 
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPopMatrix();
		gl.glPopAttrib();
		
		// setting blending mode for lights:
		gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
		gl.glBlendEquationSeparate(GL.GL_FUNC_ADD, GL.GL_FUNC_ADD);
		
		// drawing shadow map:
		gl.glBindTexture(GL.GL_TEXTURE_2D, lightTexture);
		lightShader.begin(gl);
		Color color = entity.getColor();
		lightShader.setFloat4Uniform(gl, "color", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
		renderTexture(gl);
		lightShader.end(gl);
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		gl.glPopAttrib();		
	}

	private void renderTexture(GL gl)
	{
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		gl.glDisable(GL.GL_TEXTURE_GEN_S);
		gl.glDisable(GL.GL_TEXTURE_GEN_T);
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f((float)(-textureSize/2), (float)(-textureSize/2));
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f((float)(+textureSize/2), (float)(-textureSize/2));
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f((float)(+textureSize/2), (float)(+textureSize/2));
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f((float)(-textureSize/2), (float)(+textureSize/2));
		gl.glEnd();
		
	}

	public void destroy(GL gl, K entity) 
	{
		gl.glDeleteTextures(GL.GL_TEXTURE_2D, new int [] {lightTexture}, 1);
	}
	
}