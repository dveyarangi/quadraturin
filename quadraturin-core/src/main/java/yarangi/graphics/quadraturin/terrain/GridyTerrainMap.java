package yarangi.graphics.quadraturin.terrain;

import java.util.Set;

import yarangi.ZenUtils;
import yarangi.graphics.colors.Color;
import yarangi.graphics.quadraturin.QServices;
import yarangi.spatial.GridMap;
import yarangi.spatial.IAreaChunk;
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
		
		QServices.structure.debug( "Created grid terrain [" + width + "x" + height + "] with cellsize " + cellsize + ".");
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
	public void consume(double ox, double oy, double radius)
	{
		ConsumingSensor sensor = new ConsumingSensor (false, ox, oy, radius*radius);
		
		query(sensor, ox, oy, radius);
		
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
	
	
	/**
	 * Retrieves and removes points in specified radius
	 * @param area
	 * @return
	 */
	public void produce(double ox, double oy, double radius)
	{
		ConsumingSensor sensor = new ConsumingSensor (true, ox, oy, radius*radius);
		
		query(sensor, ox, oy, radius);
		
	}
	
	public class ConsumingSensor implements ISpatialSensor <T>
	{
		double ox, oy, radiusSquare;
		boolean draw = false;
		
		public ConsumingSensor (boolean draw, double ox, double oy, double radiusSquare)
		{
			this.ox = ox;
			this.oy = oy;
			this.radiusSquare = radiusSquare;
			this.draw = draw;
		}
		/**
		 * @param chunk - current cell
		 */
		@Override
		public boolean objectFound(IAreaChunk chunk, T tile)
		{
//			System.out.println(chunk + " : " + tile);
			int pixelsBefore = tile.getPixelCount();
			if(pixelsBefore == 0 && !draw)
				return false;
			for(int i = 0; i < tile.getSize(); i ++)
				for(int j = 0; j < tile.getSize(); j ++)
				{
					double dx = chunk.getMinX() + i * pixelsize - ox;
					double dy = chunk.getMinY() + j * pixelsize - oy;
					if((dx*dx) + (dy*dy) > radiusSquare)
						continue;

					if(draw)
						((Tile)tile).put((new Color(0,1,1,1)), i, j );
					else
						tile.remove( i, j );
				}
			
			if(tile.getPixelCount() != pixelsBefore)
				setModified( (Cell<T>)chunk );
			
			return false;
		}

		@Override
		public void clear() { }
		
	}
	
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
}
