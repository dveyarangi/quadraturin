package yar.quadraturin.debug;

import java.util.Set;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.IVeil;
import yar.quadraturin.objects.IEntity;
import yar.quadraturin.objects.ILook;
import yarangi.math.FastMath;
import yarangi.spatial.SpatialHashMap;

/**
 * Renders entity index
 * 
 * @author dveyarangi
 *
 */
public class DebugSpatialHashMapLook implements ILook <SpatialHashMap<IEntity>>
{
	
	private int gridMeshId;

	@Override
	public void init(IRenderingContext context) {}

	@Override
	public void render(SpatialHashMap<IEntity> map, IRenderingContext ctx) 
	{
		GL2 gl = ctx.gl();
		gl.glEnable( GL.GL_BLEND );
		int cellX, cellY;
		float cellsize = (float)map.getCellSize();
		float halfCellSize = cellsize / 2.f;
		float minx = -halfCellSize;
		float maxx = map.getWidth()+halfCellSize;
		float miny = -halfCellSize;
		float maxy = map.getHeight()+halfCellSize;
		gl.glColor4f(0f, 0f, 0.4f, 0.5f);
		for(float y = miny; y <= maxy; y += map.getCellSize())
		{
			gl.glBegin(GL.GL_LINE_STRIP);
			gl.glVertex3f(minx, y, 0f);
			gl.glVertex3f(maxx, y, 0f);
			gl.glEnd();
		}
		
		for(float x = minx; x <= maxx; x += map.getCellSize())
		{
			gl.glBegin(GL.GL_LINE_STRIP);
			gl.glVertex3f(x, miny, 0f);
			gl.glVertex3f(x, maxy, 0f);
			gl.glEnd();
		}

		
		Set <IEntity> bucket = null;
		for(float y = miny; y <= maxy; y += map.getCellSize())
		{
			cellY = FastMath.round(y / map.getCellSize());
			for(float x = minx; x < maxx; x += map.getCellSize())
			{
				cellX = FastMath.round(x / map.getCellSize());
				boolean isReal = false;
				try {
					bucket = map.getBucket(cellX, cellY);
				}
				catch(ArrayIndexOutOfBoundsException e) {
					
				}
//				if(bucket.size() > 0)
				if(bucket != null)
				for(IEntity chunk : bucket)
				{
//					System.out.println(chunk);
//					if(chunk.overlaps(x, y, x+map.getCellSize(), y+map.getCellSize()))
					{
						isReal = true;
						break;
					}
				}
				if(bucket == null) {
					gl.glColor4f(1f, 0f, 0.0f, 0.5f);
					gl.glBegin(GL.GL_LINE_STRIP);
					gl.glVertex3f(x, y, 0f);
					gl.glVertex3f(x+cellsize, y+cellsize, 0f);
					gl.glEnd();
					gl.glBegin(GL.GL_LINE_STRIP);
					gl.glVertex3f(x, y+cellsize, 0f);
					gl.glVertex3f(x+cellsize, y, 0f);
					gl.glEnd();

				}
				else
				if(bucket.size() != 0)
				{
					if(isReal)		
						gl.glColor4f(0.8f, 0.6f, 0.8f, 0.2f);
					else
						gl.glColor4f(0.1f, 0.6f, 0.8f, 0.1f);
					gl.glBegin(GL2.GL_QUADS);
					gl.glVertex3f(x, y, 0f);
					gl.glVertex3f(x, y+cellsize, 0f);
					gl.glVertex3f(x+cellsize, y+cellsize, 0f);
					gl.glVertex3f(x+cellsize, y, 0f);
				
					gl.glEnd();
				}	
			}
		}
	}

	@Override
	public void destroy(IRenderingContext context) {}

	@Override
	public boolean isCastsShadow() { return false; }

	@Override
	public float getPriority() { return 0; }

	@Override
	public IVeil getVeil() { return null; }

	@Override
	public boolean isOriented()
	{
		return false;
	}

}
