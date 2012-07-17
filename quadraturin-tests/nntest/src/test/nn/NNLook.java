package test.nn;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.graphics.quadraturin.objects.ILook;

public class NNLook implements ILook<NNEntity>
{

	@Override
	public void init(GL gl, NNEntity entity, IRenderingContext context)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GL gl, double time, NNEntity entity, IRenderingContext context)
	{
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
	public void destroy(GL gl, NNEntity entity, IRenderingContext context)
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

}
