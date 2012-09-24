package yarangi.game.vibrations.grid;

import yarangi.graphics.quadraturin.objects.EntityShell;
import yarangi.graphics.quadraturin.terrain.ITerrainFactory;
import yarangi.spatial.ITileMap;

public class SpringGridFactory implements ITerrainFactory <SpringGrid>
{

	@Override
	public EntityShell<SpringGrid> generateTerrain(float width, float height, int cellsize)
	{
		SpringGrid grid = new SpringGrid( cellsize, width, height );
		return new EntityShell<SpringGrid>(grid, new SpringGridBehavior(), new SpringGridLook());
	}

}
