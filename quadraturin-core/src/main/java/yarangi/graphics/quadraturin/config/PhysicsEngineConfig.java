package yarangi.graphics.quadraturin.config;

import yarangi.graphics.quadraturin.simulations.ICollider;
import yarangi.graphics.quadraturin.simulations.IPhysicsEngine;
import yarangi.graphics.quadraturin.terrain.ITerrainMap;
import yarangi.java.ReflectionUtil;
import yarangi.spatial.ISpatialIndex;

public class PhysicsEngineConfig
{
    protected String engineClass;
    protected String colliderClass;

    
	public IPhysicsEngine createEngine(ISpatialIndex index, ITerrainMap terrain) 
	{
		if(engineClass == null)
			return null;
		
		ICollider manager = null;
		if(colliderClass != null)
			manager = ReflectionUtil.createInstance(colliderClass, 
					new Object[] {index, terrain},
					new Class <?> [] {ISpatialIndex.class, ITerrainMap.class}
		);

		IPhysicsEngine engine = ReflectionUtil.createInstance(engineClass, 
				new Object[] {manager}, new Class <?> [] {ICollider.class});
		IQuadConfig.LOG.debug("Created physics engine " + engine);
		return engine;
	}

}
