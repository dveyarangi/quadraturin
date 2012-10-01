package test.nn;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.graphics.quadraturin.objects.ILook;

public class NNLook implements ILook<NNEntity>
{

	@Override
	public void init(GL gl, IRenderingContext context)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GL gl1, NNEntity entity, IRenderingContext context)
	{
		GL2 gl = gl1.getGL2();

		gl.glBegin(GL.GL_LINE_STRIP);
		gl.glColor3f(0,1,0);
		for(int idx = 0; idx < entity.getTrainingSetSize(); idx ++)
		{
			gl.glVertex2f((float)entity.getInputSet()[idx], (float)entity.getOutputSet()[idx]);
		}
		gl.glEnd();
		
		gl.glColor3f(1,1,0);
		gl.glBegin(GL.GL_LINE_STRIP);
		for(int idx = 0; idx < entity.getTrainingSetSize(); idx ++)
		{
			gl.glVertex2f((float)entity.getInputSet()[idx], (float)entity.getCalculatedSet()[idx]);
		}
		gl.glEnd();
	}

	@Override
	public void destroy(GL gl, IRenderingContext context)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getPriority()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isCastsShadow()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IVeil getVeil()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOriented()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
