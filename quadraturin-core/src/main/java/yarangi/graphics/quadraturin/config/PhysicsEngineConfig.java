package yarangi.graphics.quadraturin.config;

import yarangi.graphics.quadraturin.Q;
import yarangi.graphics.quadraturin.simulations.ICollider;
import yarangi.graphics.quadraturin.simulations.IPhysicsEngine;
import yarangi.java.ReflectionUtil;
import yarangi.spatial.ISpatialSetIndex;
import yarangi.spatial.ITileMap;

public class PhysicsEngineConfig
{
    protected String engineClass;
    protected String colliderClass;

    
	public IPhysicsEngine createEngine(ISpatialSetIndex index, ITileMap terrain) 
	{
		if(engineClass == null)
			return null;
		
		ICollider manager = null;
		if(colliderClass != null)
			manager = ReflectionUtil.createInstance(colliderClass, 
					new Object[] {index, terrain},
					new Class <?> [] {ISpatialSetIndex.class, ITileMap.class}
		);

		IPhysicsEngine engine = ReflectionUtil.createInstance(engineClass, 
				new Object[] {manager}, new Class <?> [] {ICollider.class});
		Q.config.debug("Created physics engine " + engine);
		return engine;
	}

}
