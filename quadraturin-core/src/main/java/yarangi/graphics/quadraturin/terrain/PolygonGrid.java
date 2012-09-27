package yarangi.graphics.quadraturin.terrain;

import yarangi.spatial.GridMap;
import yarangi.spatial.ISpatialSensor;
import yarangi.spatial.ITileMap;
import yarangi.spatial.Tile;

import com.seisw.util.geom.Poly;

public class PolygonGrid <P extends ITilePoly> extends GridMap <Tile <P>, P> implements ITileMap <P>
{

	public PolygonGrid(int cellSize, float width, float height)
	{
		super( cellSize, width, height );
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Tile<P> createEmptyCell(int i, int j, double x, double y)
	{
		return new Tile <P> (i, j, x, y, getCellSize(), getCellSize());
	}

	@Override
	protected Tile<P>[] createMap(int cellSize, int width, int height)
	{
		return new Tile [width*height];
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
	public boolean apply(double cx, double cy, double rx, double ry, boolean substract, Poly poly)
	{
		MaskingSensor sensor = new MaskingSensor (substract, poly);

		
		queryAABB(sensor, cx, cy, rx, ry);
		
		return sensor.isModified();
	}
	
	
	public class MaskingSensor implements ISpatialSensor <P>
	{
		private final Poly poly;
		
		private final boolean substract;
		
		private boolean modified = false;
		
		public MaskingSensor (boolean substract, Poly poly)
		{
			this.poly = poly;
			
			this.substract = substract;
		}

		@Override
		public boolean objectFound(P tilePoly)
		{
//			System.out.println("erroding behavior: erroding tile " + chunk.get() );
			
			boolean modified = false;
			if(substract) 
			{
				if(!tilePoly.isEmpty())
					modified = tilePoly.substract( poly );
			}
			else 
			{
				if(!tilePoly.isFull())
					modified = tilePoly.add( poly );
			}
	
			if(modified)
				PolygonGrid.this.setModified( tilePoly.getMinX(), tilePoly.getMinY() );
			
			this.modified |= modified;
			return false;
		}

		@Override
		public void clear() { modified = false; }
		
		public boolean isModified() { return modified; }
		
	}

}
