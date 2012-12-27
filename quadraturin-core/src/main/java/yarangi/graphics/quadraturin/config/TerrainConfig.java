package yarangi.graphics.quadraturin.config;

import yarangi.graphics.quadraturin.objects.EntityShell;
import yarangi.graphics.quadraturin.terrain.ITerrain;
import yarangi.graphics.quadraturin.terrain.ITerrainFactory;
import yarangi.spatial.ITileMap;
import yarangi.java.ReflectionUtil;

public class TerrainConfig
{
	private String factoryClass;
	private final int cellsize = 10;
	
	public EntityShell<? extends ITileMap<ITerrain>> createTerrain(float sceneWidth, float sceneHeight)
	{
		ITerrainFactory factory = ReflectionUtil.createInstance( factoryClass ); 
		return factory.generateTerrain( sceneWidth, sceneHeight, cellsize );
	}
}
