package yarangi.graphics.quadraturin.simulations;

import yarangi.spatial.AABB;

public interface ICollidable
{
	public boolean overlaps(AABB aabb);

}
