package yarangi.graphics.quadraturin.terrain;

import java.util.Set;

import yarangi.ZenUtils;
import yarangi.graphics.quadraturin.Q;
import yarangi.spatial.GridMap;
import yarangi.spatial.ISpatialSensor;

/**
 * 
 * @author dveyarangi
 *
 * @param <T> - tile type
 * @param <P> - tile pixel type
 */
public class GridyTerrainMap <T extends ITile <?>> extends GridMap<Cell <T>, T> implements ITerrainMap <T>
{
	
	private float pixelsize;
	
	public GridyTerrainMap(float width, float height, int cellsize, int pixelsize)
	{
		super(cellsize, (int)width, (int)height);
		
		this.pixelsize = pixelsize;
		
		Q.structure.debug( "Created grid terrain [" + width + "x" + height + "] with cellsize " + cellsize + ".");
	}

	@Override
	public Cell<T>[] createMap(int cellSize, int width, int height)
	{
		return (Cell <T> []) new Cell [width*height]; // ugly, as always	
	}
	
	@Override
	protected Cell<T> createEmptyCell(int idx, double x, double y)
	{
		return new Cell <T> (x, y, getCellSize(), null);
	}

	@Override
	public final int indexAtCell(int x, int y) 
	{
		return  x + getGridWidth() * y;
	}

	@Override
	protected boolean queryCell(Cell<T> cell, ISpatialSensor<T> sensor, int queryId)
	{
		if(cell.getProperties() == null)
			return false;
		
		return sensor.objectFound( cell, cell.getProperties() );
	}

	@Override
	protected boolean addToCell(Cell<T> cell, T object)
	{
		cell.setProperties( object );
		return true;
	}

	@Override
	protected boolean removeFromCell(Cell<T> cell, T object)
	{
		cell.setProperties( null );
		return true;
	}


	@Override
	public Set<T> keySet()
	{
		return ZenUtils.methodNotSupported( GridyTerrainMap.class );
	}

	/**
	 * Retrieves and removes points in specified radius
	 * @param area
	 * @return
	 */
/*	public void apply(double ox, double oy, boolean substract, byte [] mask)
	{
		MaskingSensor sensor = new MaskingSensor (false, ox, oy, mask);
		
		query(sensor, ox, oy, 8);
	}*/
	
	
/*	public class MaskingSensor implements ISpatialSensor <T>
	{
		double ox, oy;
		byte [] mask;
		boolean substract;
		
		public MaskingSensor (boolean substract, double ox, double oy, byte [] mask)
		{
			this.ox = ox;
			this.oy = oy;
			
			this.mask = mask;
			
			this.substract = substract;
		}

		@Override
		public boolean objectFound(IAreaChunk chunk, T tile)
		{
//			System.out.println(chunk + " : " + tile);
			int pixelsBefore = tile.getPixelCount();
			if(pixelsBefore == 0 && substract)
				return false;
			
			int ioffset = (int)((chunk.getMinX() - ox) / getCellSize());
			int joffset = (int)((chunk.getMinY() - oy) / getCellSize());
			
			if(substract)
				tile.subMask( ioffset, joffset, mask );
			else
				tile.addMask( ioffset, joffset, mask );
			
			if(tile.getPixelCount() != pixelsBefore)
				setModified( (Cell<T>)chunk );
			
			return false;
		}

		@Override
		public void clear() { }
		
	}*/
	
	public float getPixelSize() { return pixelsize; }
}
