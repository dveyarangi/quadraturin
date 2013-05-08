package yar.quadraturin.terrain;

import yar.quadraturin.objects.EntityShell;


public interface ITerrainFactory <G>
{
	public EntityShell <G> generateTerrain(float width, float height, int cellsize);
}
