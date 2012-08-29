package yarangi.graphics.quadraturin.debug.looks;

import javax.media.opengl.GL;

import yarangi.graphics.colors.Color;
import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.graphics.quadraturin.SceneLayer;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.ILook;
import yarangi.math.Angles;

public class CoordinateGridLook implements ILook <SceneLayer>
{
	private final Color color;
	
	public CoordinateGridLook(Color color)
	{
		this.color = color;
	}

	@Override
	public void init(GL gl, SceneLayer entity, IRenderingContext context) { }

	@Override
	public void render(GL gl, SceneLayer entity, IRenderingContext context)
	{
		gl.glEnable(GL.GL_BLEND);
		
		int halfWidth = entity.getWidth() / 2;
		int halfHeight = entity.getHeight() / 2;
		
		float order;
		for( order = 2048f; order > 0.000001; order /= 2f) {
			if(Math.round(  order / context.getViewPoint().getScale()) == 0)
				break;
		}
		
		int steps = 8;
		for(int i = 1; i <= steps; i ++) {
			
			int magnitude = (int)Math.pow(2, i);
			
			gl.glColor4f(color.getRed(),color.getGreen(),color.getBlue(), 0.05f);
			
			for(float x = -halfWidth; x < halfWidth; x += order*magnitude*4) 
			{
				gl.glBegin( GL.GL_LINE_STRIP );
					gl.glVertex2f( x, -halfHeight);
					gl.glVertex2d(x, halfHeight );
				gl.glEnd();
			}
			for(float y = -halfHeight; y < halfHeight; y += order*magnitude*4)
			{
				gl.glBegin( GL.GL_LINE_STRIP );
					gl.glVertex2f( -halfWidth, y);
					gl.glVertex2d( halfWidth, y );
				gl.glEnd();
			}
		}
	}

	@Override
	public void destroy(GL gl, SceneLayer entity, IRenderingContext context) { }

	@Override
	public float getPriority() { return 0; }

	@Override
	public boolean isCastsShadow() { return false; }

	@Override
	public IVeil getVeil() { return null; }

	@Override
	public boolean isOriented()	{ return false; }

}
