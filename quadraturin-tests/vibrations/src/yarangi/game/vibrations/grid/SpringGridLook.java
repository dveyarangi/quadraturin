package yarangi.game.vibrations.grid;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.graphics.quadraturin.objects.ILook;
import yarangi.math.Vector2D;
import yarangi.spatial.Tile;

public class SpringGridLook implements ILook <SpringGrid>
{
	public static final int SIZE = 3;
	@Override
	public void init(GL gl, SpringGrid entity, IRenderingContext context)
	{
	}

	@Override
	public void render(GL gl, double time, SpringGrid entity, IRenderingContext context)
	{
		Tile <Joint> tile;
		Joint joint;
		Vector2D loc;
		Vector2D vel;
		double val;
		for(int i = 0; i < entity.getGridWidth(); i ++)
			for(int j = 0; j < entity.getGridWidth(); j ++)
			{
				tile = entity.getTile( i, j );
				loc = tile.get().plus( tile.getX(), tile.getY() );
				vel = Vector2D.R(tile.get().vx(), tile.get().vy()).normalize();
				val = Vector2D.R(tile.get().vx(), tile.get().vy()).abs();
				gl.glBegin( GL.GL_QUADS );
				gl.glColor4d(val, (Math.abs(vel.x()) + Math.abs(vel.y())-1)/0.4, (1.4-(Math.abs(vel.x()) + Math.abs(vel.y())))/0.4,1);
//				 gl.glColor4d(val, val/2, 0, 1);
				gl.glVertex2f( (float)loc.x()-SIZE, (float)loc.y()-SIZE );
				gl.glVertex2f( (float)loc.x()+SIZE, (float)loc.y()-SIZE );
				gl.glVertex2f( (float)loc.x()+SIZE, (float)loc.y()+SIZE );
				gl.glVertex2f( (float)loc.x()-SIZE, (float)loc.y()+SIZE );
				gl.glEnd();
			}
	}

	@Override
	public void destroy(GL gl, SpringGrid entity, IRenderingContext context)
	{
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
	public IVeil getVeil()
	{
		return IVeil.ORIENTING;
	}

}
