package yarangi.graphics.quadraturin.terrain;

import yarangi.graphics.quadraturin.objects.Entity;

public class TerrainEntity extends Entity
{
	private ITerrainMap terrain;
	public TerrainEntity(ITerrainMap terrain)
	{
		this.terrain = terrain;
	}
	
	public ITerrainMap getTerrain() { return terrain; }
}
