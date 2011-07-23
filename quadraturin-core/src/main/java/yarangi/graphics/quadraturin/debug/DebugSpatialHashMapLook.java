package yarangi.graphics.quadraturin.debug;

import java.util.Set;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.math.FastMath;
import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialObject;
import yarangi.spatial.SpatialHashMap;

public class DebugSpatialHashMapLook implements Look <SpatialHashMap<ISpatialObject>>
{
	
	private int gridMeshId;

	public void init(GL gl, SpatialHashMap<ISpatialObject> map) 
	{
		gridMeshId = gl.glGenLists(1);
	    gl.glNewList(gridMeshId, GL.GL_COMPILE);
		double halfCellSize = map.getCellSize() / 2.;
		for(int y = -map.getHeight()/2; y < map.getHeight()/2; y += map.getCellSize())
		{
			gl.glBegin(GL.GL_LINE_STRIP);
			gl.glVertex3f((float)-map.getWidth()/2, (float)(y-halfCellSize), 0f);
			gl.glVertex3f((float)map.getWidth()/2, (float) (y-halfCellSize),0f);
			gl.glEnd();
		}
		
		for(int x = -map.getWidth()/2; x < map.getWidth()/2; x += map.getCellSize())
		{
			gl.glBegin(GL.GL_LINE_STRIP);
			gl.glVertex3f((float)(x-halfCellSize),  (float)map.getHeight()/2, 0f);
			gl.glVertex3f((float)(x-halfCellSize),  (float)-map.getHeight()/2,0f);
			gl.glEnd();
		}
		
		gl.glEndList();
	}

	public void render(GL gl, double time, SpatialHashMap<ISpatialObject> map, RenderingContext context) 
	{
		if(context.isForEffect())
			return;

		gl.glColor4f(0.1f, 0.6f, 0.8f, 0.2f);
		gl.glCallList(gridMeshId);
		
		int cellX, cellY;
		double halfCellSize = map.getCellSize() / 2.f;
		for(int y = -map.getHeight()/2; y < map.getHeight()/2; y += map.getCellSize())
		{
			cellY = FastMath.round(y / map.getCellSize());
			for(int x = -map.getWidth()/2; x < map.getWidth()/2; x += map.getCellSize())
			{
				cellX = FastMath.round(x / map.getCellSize());
				boolean isReal = false;
				Set <IAreaChunk> bucket = map.getBucket(cellX, cellY).keySet();
//				if(bucket.size() > 0)
//				System.out.println(bucket.size() + " ::: " + x + " " + y);
				for(IAreaChunk chunk : bucket)
					if(chunk.overlaps(x-halfCellSize, y-halfCellSize, x+halfCellSize, y+halfCellSize))
					{
						isReal = true;
						break;
					}
				if(bucket.size() != 0)
				{
					if(isReal)		
						gl.glColor4f(0.8f, 0.6f, 0.8f, 0.2f);
					else
						gl.glColor4f(0.1f, 0.6f, 0.8f, 0.1f);
					gl.glBegin(GL.GL_QUADS);
					gl.glVertex3f((float)(x-halfCellSize), (float)(y-halfCellSize), 0f);
					gl.glVertex3f((float)(x-halfCellSize), (float)(y+halfCellSize), 0f);
					gl.glVertex3f((float)(x+halfCellSize), (float)(y+halfCellSize), 0f);
					gl.glVertex3f((float)(x+halfCellSize), (float)(y-halfCellSize), 0f);
				
					gl.glEnd();
				}	
			}
		}
	}

	public void destroy(GL gl, SpatialHashMap<ISpatialObject> entity) 
	{
		gl.glDeleteLists(gridMeshId, 1);
	}

	@Override
	public boolean isCastsShadow() { return false; }

}
