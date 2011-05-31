package yarangi.spatial;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Straightforward implementation of spatial hash map.
 *
 * @param <T>
 */
public class SpatialHashMap <T extends ISpatialObject> extends SpatialIndexer<T>
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
	private int cellSize;
	
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
		this.halfWidth = width/2/cellSize;
		this.halfHeight = height/2/cellSize;
		this.cellSize = cellSize;

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
	public int getBucketCount() { return size; }

	/**
	 * Calculates spatial hash value.
	 * TODO: closure? %)
	 * @param x cell x cell coordinate (can range from -halfWidth to halfWidth)
	 * @param y cell y cell coordinate (can range from -halfHeight to halfHeight)
	 * @return
	 */
	protected int hash(int x, int y)
	{
		return ((x+halfWidth)*6184547 + (y+halfHeight)* 2221069) % size;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void addObject(Area area, T object) 
	{
		
		Iterator <IAreaChunk> it = area.iterator(cellSize);
		IAreaChunk chunk;
		int x, y;
		
		// adding the object to all overlapping buckets:
		while(it.hasNext())
		{
			chunk = it.next();
			x = (int)(chunk.getX()/cellSize); y = (int)(chunk.getY()/cellSize);
			
			if(x < -halfWidth || x > halfWidth || y < -halfHeight || y > halfHeight) 
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
		Iterator <IAreaChunk> it = area.iterator(cellSize);
		IAreaChunk chunk;
		int x, y;
		while(it.hasNext())
		{
			chunk = it.next();
			x = (int)(chunk.getX()/cellSize); y = (int)(chunk.getY()/cellSize);
			
			if(x < -halfWidth || x > halfWidth || y < -halfHeight || y > halfHeight) 
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
	 * TODO: result, reported to processor may be same object repeatedly.
	 */
	public ISpatialSensor <T> query(ISpatialSensor <T> processor, Area area)
	{
		
//		System.out.println("dim: " + minx + " " + maxx + " " + miny + " " + maxy + "area size: " + (maxx-minx)*(maxy-miny));
		// removing the object from all overlapping buckets:
		Iterator <IAreaChunk> it = area.iterator(cellSize);
		Map <IAreaChunk, T> cell;
		IAreaChunk chunk;
		int x, y;
		while(it.hasNext())
		{
			chunk = it.next();
			x = (int)(chunk.getX()/cellSize); y = (int)(chunk.getY()/cellSize);
			
			if(x < -halfWidth || x > halfWidth || y < -halfHeight || y > halfHeight) 
				continue;
			
			cell = map[hash(x, y)];
			for(IAreaChunk c : cell.keySet())
			{
				if(chunk.overlaps(c.getMinX(), c.getMinY(), c.getMaxX(), c.getMaxY()))
					processor.objectFound(c, cell.get(c)/*, 
							Math.pow((xmax+xmin)/2 * c.getX(), 2) + Math.pow((ymax+ymin)/2 * c.getY(), 2)*/);
			}
		}

		return processor;
	}
	
	/**
	 * {@inheritDoc}
	 * TODO: result, reported to processor may be same object repeatedly.
	 */
	public ISpatialSensor <T> query(ISpatialSensor <T> processor, double x, double y, double radius)
	{
		
		int minx = Math.max((int)((x-radius)/cellSize), -halfWidth);
		int miny = Math.max((int)((y-radius)/cellSize), -halfHeight);
		int maxx = Math.min((int)((x+radius)/cellSize), halfWidth);
		int maxy = Math.min((int)((y+radius)/cellSize), halfHeight);
		
//		System.out.println("dim: " + minx + " " + maxx + " " + miny + " " + maxy + "area size: " + (maxx-minx)*(maxy-miny));
		// removing the object from all overlapping buckets:
		
		Map <IAreaChunk, T> cell;
		for(int tx = minx; tx <= maxx; tx ++)
			for(int ty = miny; ty <= maxy; ty ++)
			{
				cell = map[hash(tx, ty)];
				for(IAreaChunk chunk : cell.keySet())
				{
					double distanceSquare = Math.pow(x - chunk.getX(), 2) + Math.pow(y - chunk.getY(), 2);
					
//					System.out.println(aabb.r+radius + " : " + Math.sqrt(distanceSquare));
					
					// TODO: make it strictier:
					if(radius >= Math.sqrt(distanceSquare))
						processor.objectFound(chunk, cell.get(chunk)/*, distanceSquare*/);
				}

			}
		
		return processor;
	}

	/**
	 * @return width of the area, covered by this map
	 */
	public int getHeight() { return height; }

	/**
	 * @return height of the area, covered by this map
	 */
	public int getWidth() { return width; }

	/**
	 * @return size (height and width) of a single cell
	 */
	public int getCellSize() { return cellSize; }
	
	/**
	 * Retrieves content of the bucket that holds the contents of (x,y) cell.
	 * Result may contain data from other cells as well.
	 * @param x
	 * @param y
	 * @return
	 */
	public Map <IAreaChunk, T> getBucket(int x, int y)
	{
		return map[hash(x, y)];
	}
	

}
