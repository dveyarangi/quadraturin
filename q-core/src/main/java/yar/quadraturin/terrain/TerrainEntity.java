package yar.quadraturin.terrain;

import yar.quadraturin.objects.Entity;
import yarangi.spatial.ITileMap;

public class TerrainEntity extends Entity
{
	private ITileMap terrain;
	public TerrainEntity(ITileMap terrain)
	{
		this.terrain = terrain;
	}
	
	public ITileMap getTerrain() { return terrain; }
}
