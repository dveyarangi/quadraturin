package yarangi.graphics.quadraturin.debug;

import java.util.Set;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.interaction.spatial.AABB;
//import yarangi.graphics.quadraturin.interaction.spatial.QuadTree;
import yarangi.graphics.quadraturin.interaction.spatial.SpatialHashMap;
import yarangi.graphics.quadraturin.interaction.spatial.SpatialIndexer;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.quadraturin.objects.Overlay;
import yarangi.graphics.quadraturin.objects.SceneEntity;

public class SceneDebugOverlay extends Overlay
{

	@SuppressWarnings("rawtypes")
	private Look spatialOverlay;
	
	SpatialIndexer <SceneEntity> indexer;
	
	public SceneDebugOverlay(SpatialIndexer <SceneEntity>indexer)
	{
		super(new AABB(0,0,0,0));
			if(indexer instanceof SpatialHashMap)
				spatialOverlay = new DebugSpatialHashMapLook();
//			if(indexer instanceof QuadTree)
//			 	spatialOverlay = new DebugQuadTreeLook();	
		
		this.indexer = indexer;

	}

	
	@SuppressWarnings("unchecked")
	public void display(GL gl, double time, RenderingContext context) 
	{
		// if we here, it must be debug:
//		SpatialIndexer <SceneEntity> indexer = scene.getWorldVeil().getEntityIndex();
		gl.glColor4f(1f,1f,1f, 0.5f);
		spatialOverlay.render(gl, time,  indexer, context);
		
		AABB c;
		gl.glColor4f(0.f, 0.f, 1.f, 0.5f);
		Set <SceneEntity> entities = indexer.getLocations().keySet();
		for(SceneEntity entity : entities)
		{
			c = entity.getAABB();
			if( c == null )
				continue;
			
			gl.glBegin(GL.GL_LINE_STRIP);
			gl.glVertex3f((float)(c.x-c.r), (float)(c.y+c.r), 0.f);
			gl.glVertex3f((float)(c.x+c.r), (float)(c.y+c.r), 0.f);
			gl.glVertex3f((float)(c.x+c.r), (float)(c.y-c.r), 0.f);
			gl.glVertex3f((float)(c.x-c.r), (float)(c.y-c.r), 0.f);
			gl.glVertex3f((float)(c.x-c.r), (float)(c.y+c.r), 0.f);
			gl.glEnd();
		}
	}


	@Override
	public boolean isPickable() { return false;	}

}
