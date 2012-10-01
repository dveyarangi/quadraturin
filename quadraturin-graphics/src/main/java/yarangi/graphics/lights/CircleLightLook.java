package yarangi.graphics.lights;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yarangi.graphics.colors.Color;
import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.ILook;
import yarangi.graphics.shaders.GLSLShader;
import yarangi.graphics.shaders.ShaderFactory;
import yarangi.graphics.textures.FBO;
import yarangi.math.BitUtils;

/**
 * Generates a rough approximation of 2D lighting and shadows texture. 
 * 
 * TODO: generalize penumbrae shader to generate correct light distribution (should supply it a single large shadow polygon
 * and light source coords and size. 
 * 
 * TODO: convert to {@link IVeil} plugin?
 * 
 * @author dveyarangi
 *
 * @param <K>
 */
public class CircleLightLook <K extends IEntity> implements ILook <K>
{
	
	/**
	 * shader to fill shadow penumbra gradient
	 */
	private GLSLShader penumbraShader;
	
	/**
	 * shader for circular light source "floating" above rendered surface 
	 */
	private GLSLShader lightShader;
	
	/**
	 * resulting texture resolution
	 */
	private int textureSize;
	
	/**
	 * Viewport buffer
	 */
//	private final IntBuffer viewport = IntBuffer.allocate(4);
	
	/**
	 * Rendered frame texture
	 */
	private FBO fbo;
	
	/**
	 * Light source color
	 */
	private Color color;
	

	public CircleLightLook(int size)
	{
		this(size, new Color(1,1,1,1));
	}
	
	public CircleLightLook(int size, Color color)
	{
		this.color = color;

//		if(entity.getEntitySensor() != null)
//			size = (int)(entity.getEntitySensor().getRadius()*2.);
//		else
//			size = (int)(entity.getTerrainSensor().getRadius()*2.);
		textureSize = BitUtils.po2Ceiling(size);

	}
	
	@Override
	public void init(GL gl, IRenderingContext context) {
		
		// rounding texture size to power of 2:
		// TODO: enable non-square textures


		// create rendering buffer
		fbo = FBO.createFBO(gl, textureSize, textureSize, true);
		
		// preparing shaders:
		ShaderFactory factory = context.getPlugin(ShaderFactory.NAME);
		
		lightShader = factory.getShader("light");
		penumbraShader = factory.getShader("penumbra");
		
		//veil = context.getPlugin( BlurVeil.NAME );
	}

	@Override
	public void render(GL gl1, K entity, IRenderingContext context) 
	{
		GL2 gl = gl1.getGL2();
		// saving OpenGL rendering modes:
		gl.glPushAttrib(GL2.GL_VIEWPORT_BIT | GL2.GL_ENABLE_BIT);	
		
		int [] viewport = context.getCamera().getViewport();
		
		// transforming the FBO plane to fit the light source location and scale:
		gl.glMatrixMode(GL2.GL_MODELVIEW); gl.glPushMatrix();  gl.glLoadIdentity();
		gl.glMatrixMode(GL2.GL_PROJECTION); gl.glPushMatrix(); gl.glLoadIdentity();
		
		gl.glScalef((viewport[2]/textureSize),( viewport[3]/textureSize), 0);
		gl.glViewport(0, 0, textureSize, textureSize);
		gl.glOrtho(-viewport[2]/2, viewport[2]/2, -viewport[3]/2, viewport[3]/2, -1, 1);
		
		gl.glDisable(GL.GL_DEPTH_TEST); // always override pixels
		
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		// binding FBO:
		fbo.bind(gl);

		// clearing frame buffer:
		gl.glClearColor(0,0,0,1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		// shadow blending setting:
		gl.glBlendEquation( GL2.GL_MAX ); // maximal intensity of two shadows
		gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_COLOR); // only red component is of interest 

		///////////////////////////////////////////////////////////////
		// drawing shadow polygons for shadow casters in range:
/*		if(entity.getEntitySensor() != null)
		{
			List <IEntity> entities = entity.getEntitySensor().getEntities();
			List <Vector2D> shadowEdge;
			Vector2D sourceLoc = entity.getArea().getAnchor();
			// drawing red polygons for full shadows and penumbra:
			for(IEntity caster : entities)
			{
				// TODO: rewrite as light veil
				// weave is ^^^^^^^^^^^^^^^^^^^^^^^^^
				// use shared screen-size frame buffer for all lights.
				// no entity sensing required
				// iterate over lightsources instead? 
//				if(!caster.getLook().isCastsShadow())
//					continue;
				
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
				// tear veil
				
			}
		}*/
		
		
		fbo.unbind(gl);

		// done with shadows
		/////////////////////////////////////////////////////////
		// now filling with lights
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// restoring to original view
		gl.glMatrixMode(GL2.GL_PROJECTION); gl.glPopMatrix();
		gl.glMatrixMode(GL2.GL_MODELVIEW); gl.glPopMatrix();
		
		gl.glPopAttrib();

		// storing OpenGL rendering modes:
		
		gl.glPushAttrib( GL.GL_COLOR_BUFFER_BIT | GL2.GL_ENABLE_BIT);
		
		gl.glEnable( GL.GL_BLEND );
		gl.glDisable( GL.GL_DEPTH_TEST ); // once again, all pixels are rendered
		gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
		gl.glBlendEquation(GL.GL_FUNC_ADD); // "saturated" blend mode
		
		////////////////////////////////////////////////////////////////////
		// rendering shadows texture and applying lighting shader to it
		// TODO: shader too costly, replace with prepared texture:
		fbo.bindTexture(gl);
			lightShader.begin(gl);
				lightShader.setFloat4Uniform(gl, "color", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
				lightShader.setFloat1Uniform(gl, "height", 0f);
//				lightShader.setFloat1Uniform(gl, "size", 0.01f);
				lightShader.setFloat1Uniform(gl, "cutoff",10f);
				
				renderTexture(gl, entity);
			lightShader.end(gl);
//			lightShader.printDebugInfo( gl );
		fbo.unbindTexture(gl);

		
//		gl.glBlendEquation( GL.GL_FUNC_ADD );
		gl.glPopAttrib(); // recover OpenGL modes:
		context.setDefaultBlendMode( gl );
/**/
/*		gl.glBegin(GL.GL_QUADS);
	//		System.out.println(textureSize/2);
			gl.glVertex3f(-textureSize/2, -textureSize/2, 0f);
			gl.glVertex3f(+textureSize/2, -textureSize/2,  0f);
			gl.glVertex3f(+textureSize/2, +textureSize/2,  0f);
			gl.glVertex3f(-textureSize/2, +textureSize/2,  0f);
		gl.glEnd();*/
	}

	private void renderTexture(GL2 gl, IEntity entity)
	{
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		gl.glDisable(GL2.GL_TEXTURE_GEN_S);
		gl.glDisable(GL2.GL_TEXTURE_GEN_T);
		gl.glBegin(GL2.GL_QUADS);
//		System.out.println(textureSize/2);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-textureSize/2, -textureSize/2, 0f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(+textureSize/2, -textureSize/2,  0f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(+textureSize/2, +textureSize/2,  0f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-textureSize/2, +textureSize/2,  0f);
		gl.glEnd();
	}

	@Override
	public void destroy(GL gl, IRenderingContext context) 
	{
		fbo.destroy(gl);
	}


	@Override
	public boolean isCastsShadow() {
		return false;
	}
	
	public Color getColor() { return color ; }
	public void setColor(Color color) { this.color = color; }

	@Override
	public float getPriority()
	{
		return 0f;
	}
	@Override
	public IVeil getVeil() { return null; }

	@Override
	public boolean isOriented()
	{
		return true;
	}
}
