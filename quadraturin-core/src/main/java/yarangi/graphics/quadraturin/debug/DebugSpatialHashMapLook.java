package yarangi.graphics.quadraturin.debug;

import java.util.Set;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.ILook;
import yarangi.math.FastMath;
import yarangi.spatial.SpatialHashMap;

public class DebugSpatialHashMapLook implements ILook <SpatialHashMap<IEntity>>
{
	
	private int gridMeshId;

	@Override
	public void init(GL gl, IRenderingContext context) {}

	@Override
	public void render(GL gl, SpatialHashMap<IEntity> map, IRenderingContext context) 
	{
		GL2 gl2 = gl.getGL2();
		gl.glEnable( GL.GL_BLEND );
		int cellX, cellY;
		float cellsize = (float)map.getCellSize();
		float halfCellSize = cellsize / 2.f;
		float minx = -halfCellSize;
		float maxx = map.getWidth()+halfCellSize;
		float miny = -halfCellSize;
		float maxy = map.getHeight()+halfCellSize;
		gl2.glColor4f(0f, 0f, 0.4f, 0.5f);
		for(float y = miny; y <= maxy; y += map.getCellSize())
		{
			gl2.glBegin(GL.GL_LINE_STRIP);
			gl2.glVertex3f(minx, y, 0f);
			gl2.glVertex3f(maxx, y, 0f);
			gl2.glEnd();
		}
		
		for(float x = minx; x <= maxx; x += map.getCellSize())
		{
			gl2.glBegin(GL.GL_LINE_STRIP);
			gl2.glVertex3f(x, miny, 0f);
			gl2.glVertex3f(x, maxy, 0f);
			gl2.glEnd();
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
					gl2.glColor4f(1f, 0f, 0.0f, 0.5f);
					gl2.glBegin(GL.GL_LINE_STRIP);
					gl2.glVertex3f(x, y, 0f);
					gl2.glVertex3f(x+cellsize, y+cellsize, 0f);
					gl2.glEnd();
					gl2.glBegin(GL.GL_LINE_STRIP);
					gl2.glVertex3f(x, y+cellsize, 0f);
					gl2.glVertex3f(x+cellsize, y, 0f);
					gl2.glEnd();

				}
				else
				if(bucket.size() != 0)
				{
					if(isReal)		
						gl2.glColor4f(0.8f, 0.6f, 0.8f, 0.2f);
					else
						gl2.glColor4f(0.1f, 0.6f, 0.8f, 0.1f);
					gl2.glBegin(GL2.GL_QUADS);
					gl2.glVertex3f(x, y, 0f);
					gl2.glVertex3f(x, y+cellsize, 0f);
					gl2.glVertex3f(x+cellsize, y+cellsize, 0f);
					gl2.glVertex3f(x+cellsize, y, 0f);
				
					gl2.glEnd();
				}	
			}
		}
	}

	@Override
	public void destroy(GL gl, IRenderingContext context) {}

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
