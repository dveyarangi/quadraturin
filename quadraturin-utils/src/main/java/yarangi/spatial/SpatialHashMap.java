package yarangi.spatial;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Straightforward implementation of spatial hash map. Keeps a 
 *
 * @param <T>
 */
public class SpatialHashMap <T extends ISpatialObject> extends SpatialIndexer<T> implements Iterable<T>
{
	/**
	 * buckets array.
	 */
	protected Map <AABB, T> [] map;
	
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
			map[idx] = new HashMap <AABB, T> ();
//		System.out.println(halfWidth + " : " + halfHeight);
	}
	
	public SpatialHashMap(int width, int height, int averageAmount)
	{
		
	}
	
	public int getSize() { return size; }

	/**
	 * Calculates spatial hash value.
	 * TODO: closure? %)
	 * @param x cell x cell coordinate (can range from -halfWidth to halfWidth)
	 * @param y cell y cell coordinate (can range from -halfHeight to halfHeight)
	 * @return
	 */
	protected int hash(int x, int y)
	{
		return ((x+halfWidth)*6184547 + (y+halfHeight)* 2221069)% size;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void addObject(AABB aabb, T object) 
	{
		// cropping to map coverage area:
		int minx = (int)((aabb.x-aabb.r)/cellSize); if(minx < -halfWidth)  minx = -halfWidth;
		int maxx = (int)((aabb.x+aabb.r)/cellSize);	if(maxx >  halfWidth)  maxx =  halfWidth;
		int miny = (int)((aabb.y-aabb.r)/cellSize);	if(miny < -halfHeight) miny = -halfHeight;
		int maxy = (int)((aabb.y+aabb.r)/cellSize);	if(maxy >  halfHeight) maxy =  halfHeight;
		
		// adding the object to all overlapping buckets:
		for(int x = minx; x <= maxx; x ++)
			for(int y = miny; y <= maxy; y ++)
			{
//				System.out.println("bucket: [" + x + ":" + y + "] " + hash(x, y) + " (size: "  + map[hash(x, y)].size() + ")");
				map[hash(x, y)].put(aabb, object);
			}
				
	}

	/**
	 * {@inheritDoc}
	 */
	protected T removeObject(AABB aabb, T object) 
	{
		// cropping to map coverage area:
		int minx = (int)((aabb.x-aabb.r)/cellSize); if(minx < -halfWidth)  minx = -halfWidth;
		int maxx = (int)((aabb.x+aabb.r)/cellSize); if(maxx >  halfWidth)  maxx =  halfWidth;
		int miny = (int)((aabb.y-aabb.r)/cellSize); if(miny < -halfHeight) miny = -halfHeight;
		int maxy = (int)((aabb.y+aabb.r)/cellSize); if(maxy >  halfHeight) maxy =  halfHeight;
		
//		System.out.println("dim: " + minx + " " + maxx + " " + miny + " " + maxy + "area size: " + (maxx-minx)*(maxy-miny));
		// removing the object from all overlapping buckets:
		for(int x = minx; x <= maxx; x ++)
			for(int y = miny; y <= maxy; y ++)
			{
				if(map[hash(x, y)].remove(aabb) == null)
						throw new IllegalArgumentException("Bucket at location [" + x + "," + y + "] does not contain object [" + aabb + "|" + object + "].");
				
//				System.out.println("bucket: [" + x + ":" + y + "] " + hash(x, y) + " (size: "  + map[hash(x, y)].size() + ")");

			}
		return object;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void updateObject(AABB old, AABB aabb, T object) 
	{
		removeObject(old, object);
		addObject(aabb, object);
	}

	/**
	 * {@inheritDoc}
	 * TODO: can return repeating elements.
	 * TODO: {@link #iterator} not tested.
	 */
	public Iterator<T> iterator() 
	{ 
		return iterator(-width/2, -height/2, width/2, height/2); 
	}
	
	/**
	 * {@inheritDoc}
	 * TODO: resulting iterator may return same object repeatedly.
	 */
	public Iterator <T> iterator(double minx, double miny, double maxx, double maxy)
	{

		return new HashMapIterator(minx, miny, maxx, maxy);
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
	public Map <AABB, T> getBucket(int x, int y)
	{
		return map[hash(x, y)];
	}
	
	/**
	 * {@link SpatialHashMap} query iterator.
	 * TODO: can return repeating elements.
	 */
	private class HashMapIterator implements Iterator <T> 
	{
		/**
		 * Border of spatial rectangle.
		 */
		protected double minx, miny, maxx, maxy;
		
		/**
		 * Border of queried cell indices
		 */
		protected int xmin, ymin, xmax, ymax;
		
		/**
		 * Current cell index.
		 */
		protected int xIdx, yIdx;
		
		/**
		 * Iterator over current cell.
		 */
		protected Iterator <AABB> bucketIterator;
		
		/**
		 * Next object.
		 */
		protected T next;
		
		/**
		 * Bounding box of the next object.
		 */
		protected AABB aabb;
		
		public HashMapIterator(double minx, double miny, double maxx, double maxy) 
		{
			this.minx = minx; this.maxx = maxx;
			this.miny = miny; this.maxy = maxy;
			
			xmin = (int) (minx/cellSize); if(xmin < -halfWidth)  xmin = -halfWidth;
			ymin = (int) (miny/cellSize); if(ymin < -halfHeight) ymin = -halfHeight;
			xmax = (int) (maxx/cellSize); if(xmax >  halfWidth)  xmax =  halfWidth;
			ymax = (int) (maxy/cellSize); if(ymax >  halfHeight) ymax =  halfHeight;
			
			xIdx = xmin; yIdx = ymin;
			
			// pre-selecting next object:
			bucketIterator = map[hash(xIdx,yIdx)].keySet().iterator();
			next = _next();
		}
		
		/**
		 * {@inheritDoc}
		 */
		public T next()
		{
			if(next == null)
				throw new NoSuchElementException("No more elements answer the query.");
			
			T temp = next;
			next = _next();
			return temp;
		}

		/**
		 * @return SNext object for this query; null, if none found.
		 */
		protected T _next() 
		{
//			AABB aabb = null;
			while(true)
			{
				
				while(!bucketIterator.hasNext()) // searching for next not empty cell
				{
					// advancing to next cell in the queried area:
					if(++xIdx > xmax)
					{
						xIdx = xmin;
						if(++yIdx > ymax)
							return null;
					}
					
					// selecting new bucket iterator:
					bucketIterator = map[hash(xIdx,yIdx)].keySet().iterator();
				}
				
				while(bucketIterator.hasNext())
				{
					aabb = bucketIterator.next();
					// searching for fitting object: 
					if(aabb.overlaps(minx, miny, maxx, maxy))
						return map[hash(xIdx,yIdx)].get(aabb);
				}
				
			}
			
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean hasNext() { return next != null;	}

		/**
		 * {@inheritDoc}
		 */
		public void remove() { bucketIterator.remove(); }
		
	}

}
