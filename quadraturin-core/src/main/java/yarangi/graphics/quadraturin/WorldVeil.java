package yarangi.graphics.quadraturin;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.graphics.quadraturin.simulations.IPhysicsEngine;
import yarangi.spatial.SpatialHashMap;

public class WorldVeil extends SceneVeil 
{

	private IPhysicsEngine engine;
	
	public WorldVeil(int width, int height, IPhysicsEngine engine) 
	{ 
//		super(new SpatialHashMap<ISpatialObject>(100, 10, width, height));
		super(width, height, new SpatialHashMap	<SceneEntity>(width*height/100, 10, width, height));
		
		System.out.println("Allocated " + width*height/100 + " cells.");
			
		this.engine = engine;
	}

	public final IPhysicsEngine getPhysicsEngine() 
	{
		return engine; 
	}
	
	/** 
	 * Initializes engine's view point for this scene.
	 * @return
	 */
//	protected void initViewPoint(IViewPoint viewPoint);

	
	/**
	 * Any rendering preprocessing should be made here.
	 * @param gl
	 */
	public void preDisplay(GL gl) {}
	
	/**
	 * Any rendering postprocessing should be made here.
	 * @param gl
	 */
	public void postDisplay(GL gl) {}

}
