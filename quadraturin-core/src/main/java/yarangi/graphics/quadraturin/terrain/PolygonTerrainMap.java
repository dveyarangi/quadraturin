package yarangi.graphics.quadraturin.terrain;

import com.seisw.util.geom.Poly;

import yarangi.spatial.AABB;
import yarangi.spatial.GridMap;
import yarangi.spatial.ISpatialSensor;
import yarangi.spatial.ITileMap;
import yarangi.spatial.Tile;

public class PolygonTerrainMap extends GridMap <Tile <TilePoly>, TilePoly> implements ITileMap <TilePoly>
{

	public PolygonTerrainMap(int cellSize, float width, float height)
	{
		super( cellSize, width, height );
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Tile<TilePoly> createEmptyCell(int i, int j, double x, double y)
	{
		return new Tile <TilePoly> (i, j, x, y, getCellSize(), getCellSize());
	}

	@Override
	protected Tile<TilePoly>[] createMap(int cellSize, int width, int height)
	{
		return (Tile <TilePoly> []) new Tile [width*height];
	}

	@Override
	protected final int indexAtTile(int i, int j)
	{
		return  i + getGridWidth() * j;
	}

	/**
	 * Retrieves and removes points in specified radius
	 * @param area
	 * @return
	 */
	public void apply(double minx, double miny, double maxx, double maxy, boolean substract, Poly poly)
	{
		MaskingSensor sensor = new MaskingSensor (substract, poly);
		
		query(sensor, AABB.createFromEdges( minx, miny, maxx, maxy, 0 ));
	}
	
	
	public class MaskingSensor implements ISpatialSensor <Tile<TilePoly>, TilePoly>
	{
		private Poly poly;
		
		private boolean substract;
		
		
		public MaskingSensor (boolean substract, Poly poly)
		{
			this.poly = poly;
			
			this.substract = substract;
		}

		@Override
		public boolean objectFound(Tile<TilePoly> chunk, TilePoly tilePoly)
		{
//			System.out.println("erroding behavior: erroding tile " + chunk.get() );
			
			boolean wasEmpty = tilePoly.isEmpty();
			boolean wasFull = tilePoly.isFull();
			if(substract) 
			{
				if(!tilePoly.isEmpty())
					tilePoly.substract( poly );
			}
			else 
			{
				if(!tilePoly.isFull())
					tilePoly.add( poly );
			}
			
			if(wasEmpty && tilePoly.isEmpty())
				return false;
			if(wasFull && tilePoly.isFull())
				return false;
			PolygonTerrainMap.this.setModified( chunk );
			return false;
		}

		@Override
		public void clear() { }
		
	}

}
