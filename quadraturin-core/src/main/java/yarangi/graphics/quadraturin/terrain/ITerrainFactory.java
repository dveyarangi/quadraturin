package yarangi.graphics.quadraturin.terrain;

import yarangi.graphics.quadraturin.objects.EntityShell;

public interface ITerrainFactory
{
	public EntityShell <? extends ITerrainMap> generateTerrain(float width, float height, int cellsize);
}
