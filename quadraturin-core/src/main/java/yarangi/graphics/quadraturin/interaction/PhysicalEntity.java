package yarangi.graphics.quadraturin.interaction;

import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.math.Vector2D;

/**
 * Basic interface for entities that can be manipulated by {@link IPhysicsEngine}.
 * 
 * @author Dve Yarangi
 */
public interface PhysicalEntity
{
	/**
	 * Unique entity id.
	 * @return
	 */
	public int getId();
	
	public Vector2D getVelocity();
	
	public void setImpactWith(SceneEntity e);

}
