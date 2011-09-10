package yarangi.graphics.quadraturin.simulations;

import yarangi.spatial.ISpatialObject;


/**
 * Basic interface for entities that can be manipulated by {@link IPhysicsEngine}.
 * 
 * @author Dve Yarangi
 */
public interface IPhysicalObject extends ISpatialObject
{
	
	Body getBody();
	
	boolean isAlive();
}
