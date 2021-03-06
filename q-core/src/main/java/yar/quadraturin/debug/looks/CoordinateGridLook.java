package yar.quadraturin.debug.looks;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.IVeil;
import yar.quadraturin.SceneLayer;
import yar.quadraturin.graphics.colors.Color;
import yar.quadraturin.objects.ILook;
import yarangi.math.FastMath;
import yarangi.math.IVector2D;

/**
 * Displays grid debug overlay (with powers of 2 steps).
 * 
 * The grid automatically adjusts to the zoom level.
 * 
 * TODO: print axis values
 * 
 * 
 * @author dveyarangi
 *
 */
public class CoordinateGridLook implements ILook <SceneLayer>
{
	private final Color color;
	
	public CoordinateGridLook(Color color)
	{
		this.color = color;
	}

	@Override
	public void init(IRenderingContext context) { }

	@Override
	public void render(SceneLayer entity, IRenderingContext context)
	{
		GL2 gl = context.gl();
		gl.glEnable(GL.GL_BLEND);
		
		int halfWidth = entity.getWidth() / 2;
		int halfHeight = entity.getHeight() / 2;
		
		float order;
		for( order = 2048f; order > 0.000001; order /= 2f) {
			if(Math.round(  order / context.getCamera().getScale()) == 0)
				break;
		}
		
		// lower left screen corner in world coordinates
		IVector2D screenMinCoord = context.getCamera().getMinCoord();
		// higher right screen corner in world coordinates
		IVector2D screenMaxCoord = context.getCamera().getMaxCoord();
		
		int steps = 8;
		for(int i = 1; i <= steps; i ++) {
			
			int magnitude = (int)Math.pow(2, i);
			float step = order*magnitude*4;
			
			gl.glColor4f(color.getRed(),color.getGreen(),color.getBlue(), 0.05f);
			
			
			float minx = Math.max( -halfWidth, FastMath.toGrid( (float)screenMinCoord.x(), step ));
			float maxx = Math.min( halfWidth, FastMath.toGrid( (float)screenMaxCoord.x(), step ));
			float miny = Math.max( -halfHeight, FastMath.toGrid( (float)screenMinCoord.y(), step ));
			float maxy = Math.min( halfHeight, FastMath.toGrid( (float)screenMaxCoord.y(), step ));
			
			for(float x = minx; x < maxx; x += step) 
			{
				gl.glBegin( GL.GL_LINE_STRIP );
					gl.glVertex2f( x, miny);
					gl.glVertex2d(x, maxy );
				gl.glEnd();
			}
			for(float y = miny; y < maxy; y += step)
			{
				gl.glBegin( GL.GL_LINE_STRIP );
					gl.glVertex2f( minx, y);
					gl.glVertex2d( maxx, y );
				gl.glEnd();
			}
		}
	}

	@Override
	public void destroy(IRenderingContext context) { }

	@Override
	public float getPriority() { return 0; }

	@Override
	public boolean isCastsShadow() { return false; }

	@Override
	public IVeil getVeil() { return null; }

	@Override
	public boolean isOriented()	{ return false; }

}
