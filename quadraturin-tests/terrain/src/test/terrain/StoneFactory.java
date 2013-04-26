package test.terrain;

import yarangi.graphics.colors.Color;
import yarangi.graphics.quadraturin.objects.EntityShell;
import yarangi.graphics.quadraturin.terrain.Bitmap;
import yarangi.graphics.quadraturin.terrain.GridyTerrainMap;
import yarangi.graphics.quadraturin.terrain.ITerrainFactory;
import yarangi.spatial.Tile;


public class StoneFactory implements ITerrainFactory <GridyTerrainMap>
{

	@Override
	public EntityShell<GridyTerrainMap> generateTerrain(float width, float height, int cellsize)
	{
		GridyTerrainMap terrain = new GridyTerrainMap( width, height, cellsize, 1f );

		for (int i = 0; i < terrain.getGridWidth(); i ++)
		{
	    	double tileX = terrain.toXCoord( i );
//			xoffset = xstart + toXCoord * i * cellsize;
		    for(int j = 0; j <  terrain.getGridHeight(); j ++)
		    {
//				yoffset = ystart + toYCoord * j * cellsize;
		    	double tileY = terrain.toYCoord( j );

		    	
		    	Tile<Bitmap> tile = terrain.createTileAt( i, j );
		    	Bitmap bitmap = tile.get();
		    	for(int x = 0; x < bitmap.getSize(); x ++)
			    	for(int y = 0; y < bitmap.getSize(); y ++)
			    	{
			    		if((tileX+x*terrain.getPixelSize())*(tileX+x*terrain.getPixelSize()) +
			    		   (tileY+y*terrain.getPixelSize())*(tileY+y*terrain.getPixelSize()) > 100000) // TODO: variety :)
			    			bitmap.put( new Color(0.1f, 0.1f, 0.1f, 1), x, y );
			    	}
		    	terrain.setModifiedByIndex( i, j );
		    }
		}

		GridyTerrainLook look = new GridyTerrainLook(terrain, true, true);
		look.setDebugOverlay( true );
		return new EntityShell<GridyTerrainMap>( terrain, new GridyTerrainBehavior(), look );
	}

}
