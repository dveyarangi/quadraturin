package yarangi.spatial;

import java.util.HashMap;
import java.util.Map;

import yarangi.math.FastMath;

/**
 * Straightforward implementation of spatial hash map.
 * 
 * Note: cannot be used in multi-threaded environment, due to passId optimization (and lack of any type of synchronization).
 * TODO: refactor to extend {@link GridMap}
 * @param <T>
 */
public class SpatialHashMap <T extends ISpatialObject> extends SpatialIndexer<T>
{
	/**
	 * buckets array.
	 * TODO: hashmap is slow!!!
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
	 * 1/cellSize, to speed up some calculations
	 */
	private double invCellsize;
	/** 
	 * cellSize/2
	 */
	private double halfCellSize;
	/**
	 * hash cells amounts 
	 */
	private int halfGridWidth, halfGridHeight;
	
	/**
	 * Used by query methods to mark tested objects and avoid result duplication;
	 * thusly permits only single threaded usage. 
	 */
	private int passId;
	
	
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
		this.invCellsize = 1. / this.cellSize;
		this.halfGridWidth = width/2/cellSize;
		this.halfGridHeight = height/2/cellSize;
		this.halfCellSize = cellSize/2.;
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
		return ((x+halfGridWidth)*6184547 + (y+halfGridHeight)* 2221069) % size;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void addObject(Area area, T object) 
	{
		if(object == null)
			throw new NullPointerException();
		addingConsumer.setObject( object );
		area.iterate( cellSize, addingConsumer );
	}

	/**
	 * {@inheritDoc}
	 */
	protected T removeObject(Area area, T object) 
	{
		removingConsumer.setObject( object );
		area.iterate( cellSize, removingConsumer );
		
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
	 * TODO: slow
	 */
	public ISpatialSensor <T> query(ISpatialSensor <T> sensor, Area area)
	{
		if(area == null)
			throw new IllegalArgumentException("Area cannot be null.");
		
		queryingConsumer.setSensor( sensor );
		queryingConsumer.setQueryId(getNextPassId());
		area.iterate( cellSize, queryingConsumer );

		return sensor;
	}
	

	protected final int getNextPassId()
	{
		return ++passId;
	}
	
	public final int toGridIndex(double value)
	{
		return FastMath.round(value * invCellsize);
	}
	
	public final boolean isInvalidIndex(int x, int y)
	{
		return (x < -halfGridWidth || x > halfGridWidth || y < -halfGridHeight || y > halfGridHeight); 
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final ISpatialSensor <T> query(ISpatialSensor <T> sensor, double x, double y, double radiusSquare)
	{
		// TODO: spiral iteration, remove this root calculation:
		double radius = Math.sqrt(radiusSquare);
		int minx = Math.max(toGridIndex(x-radius), -halfGridWidth);
		int miny = Math.max(toGridIndex(y-radius), -halfGridHeight);
		int maxx = Math.min(toGridIndex(x+radius),  halfGridWidth);
		int maxy = Math.min(toGridIndex(y+radius),  halfGridHeight);
		int passId = getNextPassId();
		T object;
		
//		System.out.println("dim: " + minx + " " + maxx + " " + miny + " " + maxy + "area size: " + (maxx-minx)*(maxy-miny));
		// removing the object from all overlapping buckets:
		Map <IAreaChunk, T> cell;
		for(int tx = minx; tx <= maxx; tx ++)
			for(int ty = miny; ty <= maxy; ty ++)
			{
				cell = map[hash(tx, ty)];
				
				double distanceSquare = FastMath.powOf2(x - tx*cellSize) + FastMath.powOf2(y - ty*cellSize);
				if(radiusSquare < distanceSquare)
					continue;
				
//				System.out.println(aabb.r+radius + " : " + Math.sqrt(distanceSquare));
				
				// TODO: make it strictier:
				for(IAreaChunk chunk : cell.keySet())
				{
					object = cell.get(chunk);
					if(object.getArea().getPassId() == passId)
						continue;
					
//					double distanceSquare = FastMath.powOf2(x - chunk.getX()) + FastMath.powOf2(y - chunk.getY());
					
//					System.out.println(aabb.r+radius + " : " + Math.sqrt(distanceSquare));
					
					// TODO: make it strictier:
//					if(radiusSquare >= distanceSquare)
						if(sensor.objectFound(chunk, object/*, distanceSquare*/))
							break;
					
					object.getArea().setPassId( passId );
				}

			}
		
		return sensor;
	}
	
	public final ISpatialSensor <T> query(ISpatialSensor <T> sensor, double ox, double oy, double dx, double dy)
	{
		int currGridx = toGridIndex(ox);
		int currGridy = toGridIndex(oy);
		double tMaxX, tMaxY;
		double tDeltaX, tDeltaY;
		int stepX, stepY;
		if(dx > 0)
		{
			tMaxX = ((currGridx*cellSize + halfCellSize) - ox) / dx;
			tDeltaX = cellSize / dx;
			stepX = 1;
		}					
		else
		if(dx < 0)
		{
			tMaxX = ((currGridx*cellSize - halfCellSize) - ox) / dx;
			tDeltaX = -cellSize / dx;
			stepX = -1;
		}
		else { tMaxX = Double.MAX_VALUE; tDeltaX = 0; stepX = 0;}
		
		if(dy > 0)
		{
			tMaxY = ((currGridy*cellSize + halfCellSize) - oy) / dy;
			tDeltaY = cellSize / dy;
			stepY = 1;
		}
		else
		if(dy < 0)
		{
			tMaxY = ((currGridy*cellSize - halfCellSize) - oy) / dy;
			tDeltaY = -cellSize / dy;
			stepY = -1;
		}
		else { tMaxY = Double.MAX_VALUE; tDeltaY = 0; stepY = 0;}
		
		int passId = getNextPassId();
		T object;
		Map <IAreaChunk, T> cell;
		while(tMaxX <= 1 || tMaxY <= 1)
		{
			if(tMaxX < tMaxY)
			{
				tMaxX += tDeltaX;
				currGridx += stepX;
			}
			else
			{
				tMaxY += tDeltaY;
				currGridy += stepY;
			}
			cell = map[hash(currGridx, currGridy)];
			for(IAreaChunk chunk : cell.keySet())
			{
				object =  cell.get(chunk);
				if(object.getArea().getPassId() == passId)
					continue;
				if(toGridIndex(chunk.getX()) == currGridx && toGridIndex(chunk.getY()) == currGridy)
				if(sensor.objectFound(chunk, cell.get(chunk)))
					break;
				
				object.getArea().setPassId( passId );
			}	
		}		
		
		return sensor;
	}

	private interface IObjectConsumer <T> extends IChunkConsumer
	{
		public void setObject(T object);
	}
	
	private IObjectConsumer <T> addingConsumer = new IObjectConsumer <T>()
	{
		private T object;
		private int x, y;
		
		public void setObject(T object)
		{
			this.object = object;
		}
		@Override
		public boolean consume(IAreaChunk chunk)
		{
			x = toGridIndex(chunk.getX()); 
			y = toGridIndex(chunk.getY());
			
			if(isInvalidIndex(x, y)) 
				return false;
			
			map[hash(x, y)].put(chunk, object);
			return true;
		}
	};
	
	private IObjectConsumer <T> removingConsumer = new IObjectConsumer <T>()
	{
		private T object;
		private int x, y;
		
		public void setObject(T object)
		{
			this.object = object;
		}
		
		@Override
		public boolean consume(IAreaChunk chunk)
		{
			x = toGridIndex(chunk.getX()); 
			y = toGridIndex(chunk.getY());
			
			if(isInvalidIndex(x, y)) 
				return false;
			
			if(map[hash(x, y)].remove(chunk) == null)
			{
//				for(IAreaChunk ch : map[hash(x,y)].keySet())
//					System.out.println(ch.getArea() + " :: " + chunk.getArea());
//				throw new IllegalArgumentException("Bucket (loc:[" + x + "," + y + "]; size:" + map[hash(x, y)].size() + ") does not contain object (obj:" + object + ").");
			}
			return true;
		}
	};
	private interface IQueryingConsumer <T extends ISpatialObject> extends IChunkConsumer
	{
		public void setSensor(ISpatialSensor <T> sensor);

		public void setQueryId(int nextPassId);
	}
	
	private IQueryingConsumer <T> queryingConsumer = new IQueryingConsumer <T>()
	{
		private T object;
		private int x, y;
		private Map <IAreaChunk, T> cell;
		private ISpatialSensor <T> processor;
		private int passId;
		
		public void setSensor(ISpatialSensor <T> processor)
		{
			this.processor = processor;
		}
		
		public void setQueryId(int passId)
		{
			this.passId = passId;
		}

		@Override
		public boolean consume(IAreaChunk chunk)
		{
			x = toGridIndex(chunk.getX()); 
			y = toGridIndex(chunk.getY());
			
			if(isInvalidIndex(x, y)) 
				return false;
			
			cell = map[hash(x, y)];
			for(IAreaChunk c : cell.keySet())
			{
				object = cell.get(c);
				if(object.getArea().getPassId() == passId)
					continue;
				
				if(chunk.overlaps(c.getMinX(), c.getMinY(), c.getMaxX(), c.getMaxY()))
//					System.out.println(cell.get(c));
				{
					object.getArea().setPassId( passId );
					if(processor.objectFound(c, object))
						break;
					
				}
			}
			
			return true;
		}
	};

}
