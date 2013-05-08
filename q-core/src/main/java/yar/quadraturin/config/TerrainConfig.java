package yar.quadraturin.config;

import yar.quadraturin.objects.EntityShell;
import yar.quadraturin.terrain.ITerrain;
import yar.quadraturin.terrain.ITerrainFactory;
import yarangi.spatial.ITileMap;
import yarangi.java.ReflectionUtil;

public class TerrainConfig
{
	private String factoryClass;
	private int cellsize = 10;
	
	public EntityShell<? extends ITileMap<ITerrain>> createTerrain(float sceneWidth, float sceneHeight)
	{
		ITerrainFactory factory = ReflectionUtil.createInstance( factoryClass ); 
		return factory.generateTerrain( sceneWidth, sceneHeight, cellsize );
	}
}
