package yarangi.graphics.quadraturin.debug;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.quadraturin.objects.Overlay;
import yarangi.spatial.Point;
import yarangi.spatial.SpatialHashMap;
import yarangi.spatial.SpatialIndexer;

public class SceneDebugOverlay extends Overlay
{

	@SuppressWarnings("rawtypes")
	private Look spatialOverlay;
	
	SpatialIndexer <IEntity> indexer;
	
	public SceneDebugOverlay(SpatialIndexer <IEntity> indexer)
	{
		super();
		
			if(indexer instanceof SpatialHashMap)
				spatialOverlay = new DebugSpatialHashMapLook();
//			if(indexer instanceof QuadTree)
//			 	spatialOverlay = new DebugQuadTreeLook();	
		
		this.indexer = indexer;

		setLook(spatialOverlay);
		setArea(new Point(0,0));
	}
	
	@SuppressWarnings("unchecked")
	public void init(GL gl, IRenderingContext context)
	{
//		super.init(gl);
		
		spatialOverlay.init(gl, indexer, context);
	}
	
	public void render(GL gl, double time, IRenderingContext context)
	{
		spatialOverlay.render( gl, time, indexer, context );
	}


	public void destroy(GL gl, IRenderingContext context)
	{
		spatialOverlay.destroy( gl, indexer, context );
	}

	public SpatialIndexer <IEntity> getIndexer()
	{
		return indexer;
	}
}
