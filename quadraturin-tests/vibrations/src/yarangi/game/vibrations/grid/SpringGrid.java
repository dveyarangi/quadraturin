package yarangi.game.vibrations.grid;

import yarangi.graphics.quadraturin.terrain.ITileMap;
import yarangi.spatial.GridMap;
import yarangi.spatial.Tile;

public class SpringGrid extends GridMap <Tile <Joint>, Joint> implements ITileMap <Joint>
{

	public SpringGrid(int cellSize, float width, float height)
	{
		super( cellSize, width, height );
	}

	@Override
	protected Tile<Joint> createEmptyCell(int idx, double x, double y)
	{
		return new Tile<Joint>(0, 0, x, y);
	}
	
	@Override
	protected Tile<Joint>[] createMap(int cellSize, int width, int height)
	{
		Tile<Joint> [] tiles = new Tile [width*height];
		
		for(int i = 0; i < tiles.length; i ++)
		{
			int x = i % width;
			int y = i / width;
			tiles[i] = createEmptyCell(i, toXCoord( x ), toYCoord( y ));
			tiles[i].put( new Joint(0,0) );
		}
		return tiles;
	}


	@Override
	protected int indexAtTile(int i, int j)
	{
		return i + j * getGridWidth();
	}


}
