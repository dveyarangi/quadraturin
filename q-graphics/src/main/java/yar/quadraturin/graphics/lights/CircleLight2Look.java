package yar.quadraturin.graphics.lights;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.IVeil;
import yar.quadraturin.graphics.shaders.GLSLShader;
import yar.quadraturin.graphics.textures.TextureUtils;
import yar.quadraturin.objects.IEntity;
import yar.quadraturin.objects.ILook;
import yarangi.math.BitUtils;

/**
 * No FBO and not working.
 * @author dveyarangi
 *
 * @param <K>
 */
public class CircleLight2Look <K extends IEntity> implements ILook <K>
{

	private int lightTexture;
	
	private GLSLShader lightShader;
	
	private final int textureSize;
	private final GLU glu = new GLU();
	IntBuffer viewport = IntBuffer.allocate(4);
	DoubleBuffer model = DoubleBuffer.allocate(16);
	DoubleBuffer proj = DoubleBuffer.allocate(16);
	DoubleBuffer res= DoubleBuffer.allocate(3);
	
	public CircleLight2Look(int size)
	{
		textureSize = BitUtils.po2Ceiling(size);
	}
	
	@Override
	public void init(IRenderingContext ctx) {
		
		GL2 gl = ctx.gl();
		
//		System.out.println(size + " : " + entity.getLightRadius()*2);
		lightTexture = TextureUtils.createEmptyTexture2D(gl, textureSize, textureSize, false);
		
//		
//		System.out.println("here");
//		lightShader = ShaderFactory.getShader("light");
//		if(lightShader == null)
//		{
//			ShaderFactory.registerShaderFile("light", "shaders/shadowmap.glsl");	
//			lightShader = ShaderFactory.getShader("light");
//			lightShader.init(gl);
//		}
	}

	@Override
	public void render(K entity, IRenderingContext ctx) 
	{
/*		if(context.isForEffect())
			return;
		Map <ISpatialObject, Double>  entities = entity.getEntities();
		
		if(entities == null)
			return;
		AABB aabb = entity.getAABB();
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, model); 
		gl.glGetDoublev(GL.GL_PROJECTION_MATRIX, proj); 
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport);
		glu.gluProject(0, 0, 0, model, proj, viewport, res);
//		System.out.println(res.get(0) + "x" + res.get(1));
		gl.glEnable(GL.GL_SCISSOR_TEST);
		gl.glScissor((int)(res.get(0)-textureSize/2), (int)(res.get(1)-textureSize/2), 
				(int)(textureSize), (int)(textureSize));
//		gl.glMatrixMode(GL.GL_MODELVIEW);
//		gl.glPushMatrix();
//		gl.glMatrixMode(GL.GL_PROJECTION);
//		gl.glPushMatrix();
//		gl.glTranslatef(2*viewport.get(2), 2*viewport.get(3), 0);
//		gl.glOrtho(2*viewport.get(2), 3*viewport.get(2), 2*viewport.get(3), 3*viewport.get(3), -1, 1);
//		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
//		gl.glDisable(GL.GL_BLEND);
//		gl.glDisable(GL.GL_BLEND);
//		gl.glDisable(GL.GL_DEPTH_TEST);
//		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glBlendFunc ( GL.GL_SRC_ALPHA, GL. GL_ONE_MINUS_SRC_ALPHA);
		//gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL.GL_ALPHA_TEST);
		gl.glAlphaFunc(GL.GL_ALWAYS , 0);
		for(ISpatialObject caster : entities.keySet())
		{
			Vector2D distance = caster.getAABB().minus(entity.getAABB());
			Vector2D dir = distance.normalize();
			
			Vector2D left = dir.rotate(Angles.PI_div_2);
			Vector2D right = dir.rotate(-Angles.PI_div_2); // new Vector2D( dir.y, -dir.x)
			
			Vector2D casterLeftInner  = left.mul(caster.getAABB().r).plus(distance);
			Vector2D casterRightInner = right.mul(caster.getAABB().r).plus(distance);
			Vector2D casterLeft  = left.mul(caster.getAABB().r).plus(distance);
			Vector2D casterRight = right.mul(caster.getAABB().r).plus(distance);
			
			Vector2D sourceLeft = left.mul(entity.getAABB().r);
			Vector2D sourceRight = right.mul(entity.getAABB().r);
			
			Vector2D fullLeft = casterLeft.minus(sourceLeft).normalize();
			Vector2D fullRight = casterRight.minus(sourceRight).normalize();
			Vector2D softLeft = casterLeft.minus(sourceRight).normalize();
			Vector2D softRight = casterRight.minus(sourceLeft).normalize();
//			gl.glColorMask(true, true, true, true);
			//			gl.glDisable(GL.GL_BLEND);
			gl.glColor4f(1.0f, 0.0f, 0.0f, 0.0f);
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex2f((float)(casterLeft.x),     (float)casterLeft.y);
			gl.glVertex2f((float)(casterLeft.x  + fullLeft.x  * textureSize),  (float)(casterLeft.y  + fullLeft.y * textureSize));
			gl.glVertex2f((float)(casterRight.x + fullRight.x * textureSize), (float)(casterRight.y + fullRight.y  * textureSize));
			gl.glVertex2f((float)(casterRight.x),    (float)casterRight.y);
			gl.glEnd();
			
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex2f((float)(casterLeftInner.x),     (float)casterLeftInner.y);
			gl.glVertex2f((float)(casterLeft.x+fullLeft.x*textureSize),  (float)(casterLeft.y+fullLeft.y*textureSize));
//			gl.glColor4f(0.0f, 1.0f, 0.0f, 0.0f);
			gl.glVertex2f((float)(casterLeft.x+softLeft.x*textureSize),  (float)(casterLeft.y+softLeft.y*textureSize));
			gl.glVertex2f((float)(casterLeft.x),     (float)casterLeft.y);
			gl.glEnd();
			
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex2f((float)(casterRight.x+softRight.x*textureSize), (float)(casterRight.y+softRight.y*textureSize));
			gl.glVertex2f((float)(casterRight.x),    (float)casterRight.y);
			gl.glColor4f(1.0f, 0.0f, 0.0f, 0.0f);
			gl.glVertex2f((float)(casterRightInner.x),    (float)casterRightInner.y);
			gl.glVertex2f((float)(casterRight.x+fullRight.x*textureSize), (float)(casterRight.y+fullRight.y*textureSize));
			gl.glEnd();
			
//			gl.glBegin(GL.GL_LINE_STRIP);
//			gl.glVertex2f((float)(right.x), (float)right.y);
//			gl.glVertex2f((float)(right.x*10), (float)(right.y*10));
//			gl.glEnd();
		}
		gl.glDisable(GL.GL_SCISSOR_TEST);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glBindTexture(GL.GL_TEXTURE_2D, lightTexture);
		gl.glCopyTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA2, 
			    (int)(res.get(0)-textureSize/2), (int)(res.get(1)-textureSize/2), 
				(int)(textureSize), (int)(textureSize), 0);

//		gl.glMatrixMode(GL.GL_MODELVIEW);
//		gl.glPopMatrix();
//		gl.glMatrixMode(GL.GL_PROJECTION);
//		gl.glPopMatrix();
		//		gl.glDisable(GL.GL_DEPTH_TEST);
		
//		gl.glDisable(GL.GL_BLEND);
//		gl.glBindTexture(GL.GL_TEXTURE_2D, lightTexture);
		
//		gl.glDisable(GL.GL_M)
//		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, lightTexture);
		lightShader.begin(gl);
		lightShader.setFloat2Uniform(gl, "light", (float)aabb.x, (float)aabb.y);
		renderTexture(gl, lightTexture, entity.getSensorRadius());
		lightShader.end(gl);
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
//		gl.glEnable(GL.GL_BLEND);
//		gl.glEnable(GL.GL_DEPTH_TEST);*/
	}

	private void renderTexture(GL2 gl, int texture, double r)
	{
//		gl.glDisable(GL.GL_BLEND);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		gl.glDisable(GL2.GL_TEXTURE_GEN_S);
		gl.glDisable(GL2.GL_TEXTURE_GEN_T);
//		gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f(-textureSize/2, -textureSize/2);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f(+textureSize/2, -textureSize/2);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f(+textureSize/2, +textureSize/2);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f(-textureSize/2, +textureSize/2);
/*		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f((float)(0), (float)(0));
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f((float)(100), (float)(0));
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f((float)(100), (float)(100));
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f((float)(0), (float)(100));*/
		gl.glEnd();
//		gl.glEnable(GL.GL_BLEND);
		
	}

	@Override
	public void destroy(IRenderingContext ctx) 
	{
		GL2 gl = ctx.gl();
		
		gl.glDeleteTextures(GL.GL_TEXTURE_2D, new int [] {lightTexture}, 1);
	}

	@Override
	public float getPriority()
	{
		return 0;
	}

	@Override
	public boolean isCastsShadow()
	{
		return false;
	}

	@Override
	public IVeil getVeil() {
		
		return null;
	}

	@Override
	public boolean isOriented()
	{
		// TODO Auto-generated method stub
		return true;
	}
	
}
