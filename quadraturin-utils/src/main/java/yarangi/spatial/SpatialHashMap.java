package yarangi.spatial;

import java.util.HashMap;
import java.util.Map;

import yarangi.math.FastMath;

/**
 * Straightforward implementation of spatial hash map.
 *
 * @param <T>
 */
public class SpatialHashMap <T> extends SpatialIndexer<T>
{
	/**
	 * buckets array.
	 */
	protected Map <IAreaChunk, T> [] map;
	
	/**
	 * number of buckets
	 */
	private int size;
	
	/**
	 * dimensions of area this hashmap represents
	 */
	private int width, height;

	/**
	 * size of single hash cell
	 */
	private double cellSize, invCellsize;
	
	/**
	 * hash cells amounts 
	 */
	private int halfWidth, halfHeight;
	
	/**
	 * 
	 * @param size amount of buckets
	 * @param cellSize bucket spatial size
	 * @param width covered area width
	 * @param height covered area height
	 */
	@SuppressWarnings("unchecked")
	public SpatialHashMap(int size, int cellSize, int width, int height)
	{
		this.size = size;
		
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.invCellsize = 1 / this.cellSize;
		this.halfWidth = width/2/cellSize;
		this.halfHeight = height/2/cellSize;

		map = new Map [size];
		for(int idx = 0; idx < size; idx ++)
			map[idx] = new HashMap <IAreaChunk, T> ();
//		System.out.println(halfWidth + " : " + halfHeight);
	}
	
	/**
	 * TODO: Optimizing constructor
	 * @param width
	 * @param height
	 * @param averageAmount
	 */
	public SpatialHashMap(int width, int height, int averageAmount)
	{
		throw new IllegalStateException("Not implemented yet.");
	}
	
	/**
	 * @return buckets number
	 */
	public final int getBucketCount() { return size; }

	/**
	 * Calculates spatial hash value.
	 * TODO: closure? %)
	 * @param x cell x cell coordinate (can range from -halfWidth to halfWidth)
	 * @param y cell y cell coordinate (can range from -halfHeight to halfHeight)
	 * @return
	 */
	protected final int hash(int x, int y)
	{
		return ((x+halfWidth)*6184547 + (y+halfHeight)* 2221069) % size;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void addObject(Area area, T object) 
	{
		
		IGridIterator <?> it = area.iterator(cellSize);
		IAreaChunk chunk;
		int x, y;
		
		// adding the object to all overlapping buckets:
		while(it.hasNext())
		{
			chunk = it.next();
			x = toGridIndex(chunk.getX()); 
			y = toGridIndex(chunk.getY());
			
			if(isInvalidIndex(x, y)) 
				continue;
			
//			System.out.println(x + ":" + y + " " + object.getArea());
			map[hash(x, y)].put(chunk, object);
		}		

	}

	/**
	 * {@inheritDoc}
	 */
	protected T removeObject(Area area, T object) 
	{
		IGridIterator <?> it = area.iterator(cellSize);
		IAreaChunk chunk;
		int x, y;
		while(it.hasNext())
		{
			chunk = it.next();
			x = toGridIndex(chunk.getX()); 
			y = toGridIndex(chunk.getY());
			
			if(isInvalidIndex(x, y)) 
				continue;
			
//			System.out.println(x + ":" + y);
			if(map[hash(x, y)].remove(chunk) == null)
			{
				throw new IllegalArgumentException("Bucket (loc:[" + x + "," + y + "]; size:" + map[hash(x, y)].size() + ") does not contain object (area:" + area + "; obj:" + object + ").");
			}
		}		
		
		return object;
	}

	/**
	 * {@inheritDoc}
	 * TODO: not efficient for large polygons:
	 */
	protected void updateObject(Area old, Area area, T object) 
	{
		removeObject(old, object);
		addObject(area, object);
	}

	/**
	 * {@inheritDoc}
	 * TODO: result, reported to sensor may be same object repeatedly.
	 */
	public ISpatialSensor <T> query(ISpatialSensor <T> processor, Area area)
	{
		if(area == null)
			throw new IllegalArgumentException("Area cannot be null.");
//		System.out.println("dim: " + minx + " " + maxx + " " + miny + " " + maxy + "area size: " + (maxx-minx)*(maxy-miny));
		// removing the object from all overlapping buckets:
		IGridIterator <?> it = area.iterator(cellSize);
		Map <IAreaChunk, T> cell;
		IAreaChunk chunk;
		int x, y;
		while(it.hasNext())
		{
			chunk = it.next();
			x = toGridIndex(chunk.getX()); 
			y = toGridIndex(chunk.getY());
			
			if(isInvalidIndex(x, y)) 
				continue;
			
			cell = map[hash(x, y)];
			for(IAreaChunk c : cell.keySet())
			{
				if(chunk.overlaps(c.getMinX(), c.getMinY(), c.getMaxX(), c.getMaxY()))
//					System.out.println(cell.get(c));
					if(processor.objectFound(c, cell.get(c)/*, 
							Math.pow((xmax+xmin)/2 * c.getX(), 2) + Math.pow((ymax+ymin)/2 * c.getY(), 2)*/))
						break;
			}
		}

		return processor;
	}
	
	
	public final int toGridIndex(double value)
	{
		return FastMath.round(value * invCellsize);
	}
	
	public final boolean isInvalidIndex(int x, int y)
	{
		return (x < -halfWidth || x > halfWidth || y < -halfHeight || y > halfHeight); 
	}
	
	/**
	 * {@inheritDoc}
	 * TODO: result, reported to sensor may be same object repeatedly.
	 */
	public final ISpatialSensor <T> query(ISpatialSensor <T> sensor, double x, double y, double radiusSquare)
	{
		// TODO: spiral iteration, remove this root calculation:
		double radius = Math.sqrt(radiusSquare);
		int minx = Math.max(toGridIndex(x-radius), -halfWidth);
		int miny = Math.max(toGridIndex(y-radius), -halfHeight);
		int maxx = Math.min(toGridIndex(x+radius),  halfWidth);
		int maxy = Math.min(toGridIndex(y+radius),  halfHeight);
		
//		System.out.println("dim: " + minx + " " + maxx + " " + miny + " " + maxy + "area size: " + (maxx-minx)*(maxy-miny));
		// removing the object from all overlapping buckets:
		Map <IAreaChunk, T> cell;
		for(int tx = minx; tx <= maxx; tx ++)
			for(int ty = miny; ty <= maxy; ty ++)
			{
				cell = map[hash(tx, ty)];
				for(IAreaChunk chunk : cell.keySet())
				{
					double distanceSquare = FastMath.powOf2(x - chunk.getX()) + FastMath.powOf2(y - chunk.getY());
					
//					System.out.println(aabb.r+radius + " : " + Math.sqrt(distanceSquare));
					
					// TODO: make it strictier:
					if(radiusSquare >= distanceSquare)
						if(sensor.objectFound(chunk, cell.get(chunk)/*, distanceSquare*/))
							break;
				}

			}
		
		return sensor;
	}

	/**
	 * @return width of the area, covered by this map
	 */
	public final int getHeight() { return height; }

	/**
	 * @return height of the area, covered by this map
	 */
	public final int getWidth() { return width; }

	/**
	 * @return size (height and width) of a single cell
	 */
	public final double getCellSize() { return cellSize; }
	
	/**
	 * Retrieves content of the bucket that holds the contents of (x,y) cell.
	 * Result may contain data from other cells as well.
	 * @param x
	 * @param y
	 * @return
	 */
	public final Map <IAreaChunk, T> getBucket(int x, int y)
	{
		return map[hash(x, y)];
	}
	

}
