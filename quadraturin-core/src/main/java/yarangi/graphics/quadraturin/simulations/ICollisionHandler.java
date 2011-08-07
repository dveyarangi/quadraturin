package yarangi.graphics.quadraturin.simulations;

import yarangi.graphics.quadraturin.objects.SceneEntity;


public interface ICollisionHandler <K>
{
	/**
	 * Implements collision logic. 
	 * @param e
	 */
	public void setImpactWith(K source, SceneEntity target);

}
