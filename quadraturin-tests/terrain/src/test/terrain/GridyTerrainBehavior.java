package test.terrain;

import yarangi.graphics.quadraturin.objects.IBehavior;
import yarangi.graphics.quadraturin.terrain.GridyTerrainMap;

public class GridyTerrainBehavior implements IBehavior<GridyTerrainMap>
{

	@Override
	public boolean behave(double time, GridyTerrainMap grid, boolean isVisible)
	{
		return false;
	}

}
