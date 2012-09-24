package yarangi.graphics.quadraturin.terrain;

import yarangi.graphics.quadraturin.objects.EntityShell;


public interface ITerrainFactory <G>
{
	public EntityShell <G> generateTerrain(float width, float height, int cellsize);
}
