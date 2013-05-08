package yar.quadraturin.simulations;

import yarangi.spatial.AABB;

public interface ICollidable
{
	public boolean overlaps(AABB aabb);

}
