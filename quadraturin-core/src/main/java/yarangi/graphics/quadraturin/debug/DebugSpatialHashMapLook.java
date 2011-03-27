package yarangi.graphics.quadraturin.debug;

import java.util.Set;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.interaction.spatial.AABB;
import yarangi.graphics.quadraturin.interaction.spatial.SpatialHashMap;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.quadraturin.objects.SceneEntity;

public class DebugSpatialHashMapLook implements Look <SpatialHashMap<SceneEntity>>
{


	public void render(GL gl, double time, SpatialHashMap<SceneEntity> map, RenderingContext context) 
	{
		if(context.isForEffect())
			return;

		gl.glColor4f(0.1f, 0.6f, 0.8f, 0.4f);
		for(int y = -map.getHeight()/2; y < map.getHeight()/2; y += map.getCellSize())
		{
			gl.glBegin(GL.GL_LINE_STRIP);
			gl.glVertex3f((float)-map.getWidth()/2, (float)y, 0f);
			gl.glVertex3f((float)map.getWidth()/2, (float) y,0f);
			gl.glEnd();
		}
		
		for(int x = -map.getWidth()/2; x < map.getWidth()/2; x += map.getCellSize())
		{
			gl.glBegin(GL.GL_LINE_STRIP);
			gl.glVertex3f((float)x,  (float)map.getHeight()/2, 0f);
			gl.glVertex3f((float)x,  (float)-map.getHeight()/2,0f);
			gl.glEnd();
		}
		
		int cellX, cellY;
		float halfCellSize = map.getCellSize() / 2.f;
		for(int y = -map.getHeight()/2; y < map.getHeight()/2; y += map.getCellSize())
		{
			cellY = y / map.getCellSize();
			for(int x = -map.getWidth()/2; x < map.getWidth()/2; x += map.getCellSize())
			{
				cellX = x / map.getCellSize();
				boolean isReal = false;
				Set <AABB> bucket = map.getBucket(cellX, cellY).keySet();
//				if(bucket.size() > 0)
//				System.out.println(bucket.size() + " ::: " + x + " " + y);
				for(AABB aabb : bucket)
					if(aabb.overlaps(x-halfCellSize, y-halfCellSize, x+halfCellSize, y+halfCellSize))
					{
						isReal = true;
						break;
					}
				if(bucket.size() != 0)
				{
					if(isReal)		
						gl.glColor4f(0.8f, 0.6f, 0.8f, 0.3f);
					else
						gl.glColor4f(0.1f, 0.6f, 0.8f, 0.1f);
					gl.glBegin(GL.GL_QUAD_STRIP);
					gl.glVertex3f(x-halfCellSize, y-halfCellSize, 0f);
					gl.glVertex3f(x-halfCellSize, y+halfCellSize, 0f);
					gl.glVertex3f(x+halfCellSize, y+halfCellSize, 0f);
					gl.glVertex3f(x+halfCellSize, y-halfCellSize, 0f);
				
					gl.glEnd();
				}	
			}
		}
	}

	public void init(GL gl, SpatialHashMap<SceneEntity> entity) {
		// TODO Auto-generated method stub
		
	}

	public void destroy(GL gl, SpatialHashMap<SceneEntity> entity) {
		// TODO Auto-generated method stub
		
	}

}
