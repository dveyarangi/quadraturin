package yarangi.graphics.quadraturin.terrain;

import yarangi.spatial.GridMap;
import yarangi.spatial.ISpatialSensor;
import yarangi.spatial.ITileMap;
import yarangi.spatial.Tile;

import com.seisw.util.geom.Poly;

/** 
 * A grid with tiles containing modifiable polygons. 
 * @author dveyarangi
 *
 * @param <P>
 */
public class PolygonGrid extends GridMap <Tile <ITilePoly>, ITilePoly> implements ITileMap <ITilePoly>
{

	public PolygonGrid(int cellSize, float width, float height)
	{
		super( cellSize, width, height );
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Tile<ITilePoly> createEmptyCell(int i, int j, double x, double y)
	{
		return new Tile <ITilePoly> (i, j, x, y, getCellSize(), getCellSize());
	}

	@Override
	protected Tile<ITilePoly>[] createMap(int cellSize, int width, int height)
	{
		return new Tile [width*height];
	}

	@Override
	protected final int indexAtTile(int i, int j)
	{
		return  i + getGridWidth() * j;
	}

	/**
	 * Adds/substracts polygonal mask from the grid
	 * @param area
	 * @return
	 */
	public boolean apply(double cx, double cy, double rx, double ry, boolean substract, Poly mask)
	{
		MaskingSensor sensor = new MaskingSensor (substract, mask);

		queryAABB(sensor, cx, cy, rx, ry);
		
		return sensor.isModified();
	}
	
	
	public class MaskingSensor implements ISpatialSensor <ITilePoly>
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
		public boolean objectFound(ITilePoly tilePoly)
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
			{
				PolygonGrid.this.setModified( tilePoly.getMinX(), tilePoly.getMinY() );
			}
			
			this.modified |= modified;
			return false;
		}

		@Override
		public void clear() { modified = false; }
		
		public boolean isModified() { return modified; }
		
	}

}
