package yarangi.graphics.quadraturin.debug;

import java.util.Set;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.math.FastMath;
import yarangi.spatial.IAreaChunk;
import yarangi.spatial.SpatialHashMap;

public class DebugSpatialHashMapLook implements Look <SpatialHashMap<IEntity>>
{
	
	private int gridMeshId;

	public void init(GL gl, SpatialHashMap<IEntity> map, IRenderingContext context) 
	{
		gridMeshId = gl.glGenLists(1);
	    gl.glNewList(gridMeshId, GL.GL_COMPILE);
		gl.glColor4f(0.1f, 0.6f, 0.8f, 0.1f);
		double halfCellSize = map.getCellSize() / 2.;
		float minx = (float)(-map.getWidth()/2-halfCellSize);
		float maxx = (float)(map.getWidth()/2+halfCellSize);
		float miny = (float)(-map.getHeight()/2-halfCellSize);
		float maxy = (float)(map.getHeight()/2+halfCellSize);
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
		
		gl.glEndList();
	}

	public void render(GL gl, double time, SpatialHashMap<IEntity> map, IRenderingContext context) 
	{
		gl.glEnable( GL.GL_BLEND );
		gl.glCallList(gridMeshId);
		
		int cellX, cellY;
		float cellsize = (float)map.getCellSize();
		float halfCellSize = cellsize / 2.f;
		float minx = -map.getWidth()/2-halfCellSize;
		float maxx = map.getWidth()/2+halfCellSize;
		float miny = -map.getHeight()/2-halfCellSize;
		float maxy = map.getHeight()/2+halfCellSize;
		for(float y = miny; y <= maxy; y += map.getCellSize())
		{
			cellY = FastMath.round(y / map.getCellSize());
			for(float x = minx; x < maxx; x += map.getCellSize())
			{
				cellX = FastMath.round(x / map.getCellSize());
				boolean isReal = false;
				Set <IAreaChunk> bucket = map.getBucket(cellX, cellY).keySet();
//				if(bucket.size() > 0)
//				System.out.println(bucket.size() + " ::: " + x + " " + y);
				for(IAreaChunk chunk : bucket)
				{
//					System.out.println(chunk);
					if(chunk.overlaps(x, y, x+map.getCellSize(), y+map.getCellSize()))
					{
						isReal = true;
						break;
					}
				}
				if(bucket.size() != 0)
				{
					if(isReal)		
						gl.glColor4f(0.8f, 0.6f, 0.8f, 0.2f);
					else
						gl.glColor4f(0.1f, 0.6f, 0.8f, 0.1f);
					gl.glBegin(GL.GL_QUADS);
					gl.glVertex3f(x, y, 0f);
					gl.glVertex3f(x, y+cellsize, 0f);
					gl.glVertex3f(x+cellsize, y+cellsize, 0f);
					gl.glVertex3f(x+cellsize, y, 0f);
				
					gl.glEnd();
				}	
			}
		}
	}

	public void destroy(GL gl, SpatialHashMap<IEntity> map, IRenderingContext context) 
	{
		gl.glDeleteLists(gridMeshId, 1);
	}

	@Override
	public boolean isCastsShadow() { return false; }

	@Override
	public float getPriority() { return 0; }

	@Override
	public IVeil getVeil() { return IVeil.ORIENTING; }

}
