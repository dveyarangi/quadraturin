package yarangi.graphics.quadraturin.config;

import yarangi.graphics.quadraturin.objects.Behavior;
import yarangi.graphics.quadraturin.objects.EntityShell;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.quadraturin.terrain.ITerrainMap;
import yarangi.java.ReflectionUtil;

public class TerrainConfig
{
	private String terrainClass;
	private String lookClass;
	private String behaviorClass;
	private int cellsize = 10;
	
	public EntityShell<ITerrainMap> createTerrain(float sceneWidth, float sceneHeight)
	{
		ITerrainMap map = ReflectionUtil.createInstance( terrainClass, sceneWidth, sceneHeight, cellsize );
		Look <ITerrainMap> look = ReflectionUtil.createInstance(lookClass);
		Behavior <ITerrainMap> behavior = ReflectionUtil.createInstance(behaviorClass);
		
		return new EntityShell(map, behavior, look);
	}
}
