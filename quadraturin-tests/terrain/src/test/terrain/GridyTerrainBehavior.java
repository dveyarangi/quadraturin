package test.terrain;

import yarangi.graphics.quadraturin.objects.Behavior;
import yarangi.graphics.quadraturin.terrain.GridyTerrainMap;

public class GridyTerrainBehavior implements Behavior<GridyTerrainMap>
{

	@Override
	public boolean behave(double time, GridyTerrainMap grid, boolean isVisible)
	{
		grid.fireGridModified();
		return false;
	}

}
