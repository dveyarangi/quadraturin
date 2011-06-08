package yarangi.graphics.quadraturin.debug;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.quadraturin.objects.Overlay;
import yarangi.spatial.Area;
import yarangi.spatial.ISpatialObject;
import yarangi.spatial.SpatialHashMap;
import yarangi.spatial.SpatialIndexer;

public class SceneDebugOverlay extends Overlay
{

	@SuppressWarnings("rawtypes")
	private Look spatialOverlay;
	
	SpatialIndexer <ISpatialObject> indexer;
	
	public SceneDebugOverlay(SpatialIndexer <ISpatialObject> indexer)
	{
		super();
			if(indexer instanceof SpatialHashMap)
				spatialOverlay = new DebugSpatialHashMapLook();
//			if(indexer instanceof QuadTree)
//			 	spatialOverlay = new DebugQuadTreeLook();	
		
		this.indexer = indexer;

	}
	
	public void init(GL gl)
	{
		super.init(gl);
		
		spatialOverlay.init(gl, indexer);
	}

	
	@SuppressWarnings("unchecked")
	public void display(GL gl, double time, RenderingContext context) 
	{
		// if we here, it must be debug:
//		SpatialIndexer <SceneEntity> indexer = scene.getWorldVeil().getEntityIndex();
		gl.glColor4f(1f,1f,1f, 0.5f);
		spatialOverlay.render(gl, time,  indexer, context);
		
/*		Area area;
		gl.glColor4f(0.f, 0.f, 1.f, 0.5f);
		Set <ISpatialObject> entities = indexer.keySet();
		for(ISpatialObject entity : entities)
		{
			area = entity.getArea();
			if( area == null )
				continue;
			
			gl.glBegin(GL.GL_LINE_STRIP);
			gl.glVertex3f((float)(area.getRefX()-area.()), (float)(c.y+c.r), 0.f);
			gl.glVertex3f((float)(area.getRefX()+c.r), (float)(c.y+c.r), 0.f);
			gl.glVertex3f((float)(area.getRefX()+c.r), (float)(c.y-c.r), 0.f);
			gl.glVertex3f((float)(area.getRefX()-c.r), (float)(c.y-c.r), 0.f);
			gl.glVertex3f((float)(area.getRefX()-c.r), (float)(c.y+c.r), 0.f);
			gl.glEnd();
		}*/
	}


	@Override
	public boolean isPickable() { return false;	}

	@Override
	public Area getArea() {	return null; }

}
