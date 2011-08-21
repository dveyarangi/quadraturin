package yarangi.graphics.quadraturin.debug;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.objects.IWorldEntity;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.quadraturin.objects.Overlay;
import yarangi.spatial.Area;
import yarangi.spatial.SpatialHashMap;
import yarangi.spatial.SpatialIndexer;

public class SceneDebugOverlay extends Overlay
{

	@SuppressWarnings("rawtypes")
	private Look spatialOverlay;
	
	SpatialIndexer <IWorldEntity> indexer;
	
	public SceneDebugOverlay(SpatialIndexer <IWorldEntity> indexer)
	{
		super();
		
			if(indexer instanceof SpatialHashMap)
				spatialOverlay = new DebugSpatialHashMapLook();
//			if(indexer instanceof QuadTree)
//			 	spatialOverlay = new DebugQuadTreeLook();	
		
		this.indexer = indexer;

		setLook(spatialOverlay);
		setArea(new Area.PointArea(0,0));
	}
	
	@SuppressWarnings("unchecked")
	public void init(GL gl)
	{
//		super.init(gl);
		
		spatialOverlay.init(gl, indexer);
	}


	public void destroy(GL gl)
	{
		spatialOverlay.destroy( gl, indexer );
	}

	public SpatialIndexer <IWorldEntity> getIndexer()
	{
		return indexer;
	}
}
