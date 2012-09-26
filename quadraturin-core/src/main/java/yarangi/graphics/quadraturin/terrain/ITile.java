package yarangi.graphics.quadraturin.terrain;

import yarangi.spatial.AABB;


public interface ITile
{
	public void overlaps(AABB aabb);
}
