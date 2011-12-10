package yarangi.graphics.quadraturin.terrain;

import yarangi.graphics.quadraturin.objects.Entity;

public class TerrainEntity extends Entity
{
	private ITileMap terrain;
	public TerrainEntity(ITileMap terrain)
	{
		this.terrain = terrain;
	}
	
	public ITileMap getTerrain() { return terrain; }
}
