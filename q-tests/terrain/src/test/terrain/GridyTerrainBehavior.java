package test.terrain;

import yar.quadraturin.objects.IBehavior;
import yar.quadraturin.terrain.GridyTerrainMap;

public class GridyTerrainBehavior implements IBehavior<GridyTerrainMap>
{

	@Override
	public boolean behave(double time, GridyTerrainMap grid, boolean isVisible)
	{
		return false;
	}

}
