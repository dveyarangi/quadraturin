package yarangi.spatial;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@link SpatialHashMap} query iterator.
 * TODO: can return repeating elements.
 */
final class HashMapIterator <T extends ISpatialObject> implements Iterator <T> 
{
	/**
	 * Border of spatial rectangle.
	 */
	protected double minx, miny, maxx, maxy;
	
	/**
	 * Border of queried cell indices
	 */
	protected int xmin, ymin, xmax, ymax;

	
	protected SpatialHashMap<T> map;
	/**
	 * Current cell index.
	 */
	protected int xIdx, yIdx;
	
	/**
	 * Iterator over current cell.
	 */
	protected Iterator <IAreaChunk> bucketIterator;
	
	/**
	 * Next object.
	 */
	protected T next;
	
	/**
	 * Bounding box of the next object.
	 */
	protected IAreaChunk chunk;
	
	public HashMapIterator(double minx, double miny, double maxx, double maxy, SpatialHashMap<T> map) 
	{
		
		this.map = map;
		this.minx = minx; this.maxx = maxx;
		this.miny = miny; this.maxy = maxy;
		
		xmin = (int) (minx/map.getCellSize()); if(xmin < -map.getWidth()/2)  xmin = -map.getWidth()/2;
		ymin = (int) (miny/map.getCellSize()); if(ymin < -map.getHeight()/2) ymin = -map.getHeight()/2;
		xmax = (int) (maxx/map.getCellSize()); if(xmax >  map.getWidth()/2)  xmax =  map.getWidth()/2;
		ymax = (int) (maxy/map.getCellSize()); if(ymax >  map.getHeight()/2) ymax =  map.getHeight()/2;
		
		xIdx = xmin; yIdx = ymin;
		
		// pre-selecting next object:
		bucketIterator = map.getBucket(xIdx,yIdx).keySet().iterator();
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
//		AABB aabb = null;
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
				bucketIterator = map.getBucket(xIdx,yIdx).keySet().iterator();
			}
			
			while(bucketIterator.hasNext())
			{
				chunk = bucketIterator.next();
				// searching for fitting object: 
				if(chunk.overlaps(minx, miny, maxx, maxy))
					return map.getBucket(xIdx,yIdx).get(chunk);
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
