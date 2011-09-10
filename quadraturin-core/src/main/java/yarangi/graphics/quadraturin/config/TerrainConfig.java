package yarangi.graphics.quadraturin.config;

import yarangi.graphics.quadraturin.objects.EntityShell;
import yarangi.graphics.quadraturin.terrain.ITerrainFactory;
import yarangi.graphics.quadraturin.terrain.ITerrainMap;
import yarangi.java.ReflectionUtil;

public class TerrainConfig
{
	private String factoryClass;
	private int cellsize = 10;
	
	public EntityShell<? extends ITerrainMap> createTerrain(float sceneWidth, float sceneHeight)
	{
		ITerrainFactory factory = ReflectionUtil.createInstance( factoryClass ); 
		return factory.generateTerrain( sceneWidth, sceneHeight, cellsize );
	}
}
