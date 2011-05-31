package yarangi.graphics.quadraturin;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.simulations.ICollisionManager;
import yarangi.graphics.quadraturin.simulations.IPhysicalObject;
import yarangi.graphics.quadraturin.simulations.IPhysicsEngine;
import yarangi.spatial.ISpatialObject;
import yarangi.spatial.SpatialHashMap;

public abstract class WorldVeil extends SceneVeil 
{

	private IPhysicsEngine engine;
	
	public WorldVeil(int width, int height) 
	{
//		super(new SpatialHashMap<ISpatialObject>(100, 10, width, height));
		super(width, height, new SpatialHashMap<ISpatialObject>(50000, 10, width, height));
			
		this.engine = createPhysicsEngine();
	}

	/**
	 * Creates a {@link ICollisionManager} object, that provide interactive object 
	 * filters and implement the narrow phase of collision processing.
	 * @return
	 */
	public abstract IPhysicsEngine createPhysicsEngine();

	public IPhysicsEngine getPhysicsEngine() 
	{
		return engine; 
	}
	
	/** 
	 * Initializes engine's view point for this scene.
	 * @return
	 */
	protected abstract void initViewPoint(IViewPoint viewPoint);

	
	/**
	 * Any rendering preprocessing should be made here.
	 * @param gl
	 */
	public abstract void preDisplay(GL gl);
	
	/**
	 * Any rendering postprocessing should be made here.
	 * @param gl
	 */
	public abstract void postDisplay(GL gl);

}
