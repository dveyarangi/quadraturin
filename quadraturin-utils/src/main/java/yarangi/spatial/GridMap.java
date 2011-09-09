package yarangi.spatial;

import yarangi.math.FastMath;

/**
 * Encapsulates the functionality shared between various grid structures.
 * 
 * Note: cannot be used in multi-threaded environment, due to passId optimization (and lack of any type of synchronization).
 *
 * @param <K>
 */
public abstract class GridMap <K, O>
{
	/**
	 * data array. It is references using {@link #at(int, int)} method.
	 */
	protected K [] map;
	
	/**
	 * dimensions of area this grid represents
	 */
	private float width, height;

	/**
	 * size of single grid cell
	 */
	private int cellSize; 
	
	/** 
	 * 1/cellSize, to speed up some calculations
	 */
	private float invCellsize;
	
	/** 
	 * cellSize/2
	 */
	private float halfCellSize;
	
	private int gridWidth, gridHeight;
	/**
	 * hash cells amounts 
	 */
	private int halfGridWidth, halfGridHeight;
	
	/**
	 * Used by query methods to mark tested objects and avoid result duplication;
	 * thusly permits only single threaded usage. 
	 */
	private int passId;
	
	private float minX, minY, maxX, maxY;
	
	/**
	 * 
	 * @param size amount of buckets
	 * @param cellSize bucket spatial size
	 * @param width covered area width
	 * @param height covered area height
	 */
	public GridMap(int cellSize, float width, float height)
	{
		
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.invCellsize = 1f / this.cellSize;
		this.gridWidth = FastMath.round(width/cellSize);
		this.gridHeight = FastMath.round(height/cellSize);
		this.halfGridWidth = gridWidth/2;
		this.halfGridHeight = gridHeight/2;
		
		this.halfCellSize = cellSize/2f;
		

		minX = -width/2f;
		maxX = width/2f;
		minY = -height/2f;
		maxY = height/2f;
		
		map = createMap( cellSize, gridWidth, gridHeight );

	}

	protected abstract K createEmptyCell(double x, double y);
	protected abstract K [] createMap(int cellSize, int width, int height);

	/**
	 * @return width of the area, covered by this map
	 */
	public final float getHeight() { return height; }

	/**
	 * @return height of the area, covered by this map
	 */
	public final float getWidth() { return width; }

	
	public float getMaxX() { return maxX; }
	public float getMinX() { return minX; }
	public float getMaxY() { return maxY; }
	public float getMinY() { return minY; }
	
	/**
	 * @return size (height and width) of a single cell
	 */
	public final int getCellSize() { return cellSize; }
	public final int getGridWidth() { return gridWidth; }
	public final int getGridHeight() { return gridHeight; }
	
	/**
	 * Retrieves content of the bucket that holds the contents of (x,y) cell.
	 * @param x
	 * @param y
	 * @return
	 */
	public final K getCell(double x, double y)
	{
		int idx = at(x, y);
		if(idx >= 0 && idx < map.length)
			return map[at(x, y)];
		return null;
	}
	
	protected final K getCell(int x, int y)
	{
		int idx = at(x, y);
		if(idx >= 0 && idx < map.length)
			return map[at(x, y)];
		return null;
	}
	
	public final void put(double x, double y, K cell)
	{
		// TODO: dissolve, if hitting not in the cell center?
//		System.out.println(x + " : " + y + " : " + at(x,y));
		map[at( x, y )] = cell;
	}
	
	protected final void put(int x, int y, K cell)
	{
		// TODO: dissolve, if hitting not in the cell center?
//		System.out.println(x + " : " + y + " : " + at(x,y));
		map[at( x, y )] = cell;
	}
	
	protected final K get(float x, float y)
	{
		return map[at(x,y)];
	}

	/**
	 * Calculates grid index for point.
	 * TODO: closure? %)
	 * @param x cell x cell coordinate (can range from -halfWidth to halfWidth)
	 * @param y cell y cell coordinate (can range from -halfHeight to halfHeight)
	 * @return
	 */
	protected abstract int at(int x, int y);
	
	protected int at(double x, double y)
	{
		return at(toGridXIndex(x), toGridYIndex(y));
	}

	/**
	 * Converts a "real world" x dimension value to cell dimensional index.
	 * @param value
	 * @return
	 */
	public final int toGridXIndex(double value)
	{
		return FastMath.round(value * invCellsize) + halfGridWidth;
	}
	
	public final double toRealXIndex(int i)
	{
		return ((double)(i - halfGridWidth)) * cellSize;
	}
	/**
	 * Converts a "real world" y dimension value to cell dimensional index.
	 * @param value
	 * @return
	 */
	public final int toGridYIndex(double value)
	{
		return FastMath.round(value * invCellsize) + halfGridHeight;
	}
	public final double toRealYIndex(int i)
	{
		return ((double)(i - halfGridHeight)) * cellSize;
	}
	
	public final boolean isInvalidIndex(int x, int y)
	{
		return (x < -halfGridWidth || x > halfGridWidth || y < -halfGridHeight || y > halfGridHeight); 
	}
	
	protected abstract boolean queryCell(K cell, ISpatialSensor<O> sensor, int queryId);
	protected abstract boolean addToCell(K cell, O object);
	protected abstract boolean removeFromCell(K cell, O object);
	
	protected final int getNextPassId() { return ++passId; }
	

	/**
	 * {@inheritDoc}
	 */
	public void addObject(Area area, O object) 
	{
		if(object == null)
			throw new NullPointerException();
		addingConsumer.setObject( object );
		area.iterate( cellSize, addingConsumer );
	}

	/**
	 * {@inheritDoc}
	 */
	public O removeObject(Area area, O object) 
	{
		removingConsumer.setObject( object );
		
		area.iterate( cellSize, removingConsumer );
		
		return object;
	}

	/**
	 * {@inheritDoc}
	 * TODO: not efficient for large polygons:
	 */
	protected void updateObject(Area old, Area area, O object) 
	{
		removeObject(old, object);
		addObject(area, object);
	}


	/**
	 * {@inheritDoc}
	 * TODO: slow
	 */
	public ISpatialSensor <O> query(ISpatialSensor <O> sensor, Area area)
	{
		if(area == null)
			throw new IllegalArgumentException("Area cannot be null.");

		queryingConsumer.setSensor( sensor );
		queryingConsumer.setQueryId(getNextPassId());
		area.iterate( cellSize, queryingConsumer );

		return sensor;
	}
	
	/**
	 * {@inheritDoc}
	 * TODO: actually queries a rectangle with sqrt(radiusSquare) span
	 */
	public final ISpatialSensor <O> query(ISpatialSensor <O> sensor, double x, double y, double radiusSquare)
	{
		// TODO: spiral iteration, remove this root calculation:
		double radius = Math.sqrt(radiusSquare);
		int minx = Math.max(toGridXIndex(x-radius), 0);
		int miny = Math.max(toGridYIndex(y-radius), 0);
		int maxx = Math.min(toGridXIndex(x+radius), gridWidth);
		int maxy = Math.min(toGridYIndex(y+radius), gridHeight);
		int passId = getNextPassId();
		K cell;
		for(int tx = minx; tx <= maxx; tx ++)
			for(int ty = miny; ty <= maxy; ty ++)
			{
				cell = getCell(tx, ty);
				
				double distanceSquare = FastMath.powOf2(x - tx*cellSize) + FastMath.powOf2(y - ty*cellSize);
				if(radiusSquare < distanceSquare)
					continue;
				
				if(cell != null)
					if(queryCell(cell, sensor, passId))
						return sensor;
			}
		
		return sensor;
	}

	public final ISpatialSensor <O> query(ISpatialSensor <O> sensor, double ox, double oy, double dx, double dy)
	{
		int currGridx = toGridXIndex(ox);
		int currGridy = toGridYIndex(oy);
		double tMaxX, tMaxY;
		double tDeltaX, tDeltaY;
		int stepX, stepY;
		if(dx > 0)
		{
			tMaxX = ((FastMath.toGrid( ox, cellSize ) + halfCellSize) - ox) / dx;
			tDeltaX = cellSize / dx;
			stepX = 1;
		}					
		else
		if(dx < 0)
		{
			tMaxX = ((FastMath.toGrid( ox, cellSize ) - halfCellSize) - ox) / dx;
			tDeltaX = -cellSize / dx;
			stepX = -1;
		}
		else { tMaxX = Double.MAX_VALUE; tDeltaX = 0; stepX = 0;}
		
		if(dy > 0)
		{
			tMaxY = ((FastMath.toGrid( oy, cellSize ) + halfCellSize) - oy) / dy;
			tDeltaY = cellSize / dy;
			stepY = 1;
		}
		else
		if(dy < 0)
		{
			tMaxY = ((FastMath.toGrid( oy, cellSize ) - halfCellSize) - oy) / dy;
			tDeltaY = -cellSize / dy;
			stepY = -1;
		}
		else { tMaxY = Double.MAX_VALUE; tDeltaY = 0; stepY = 0;}
		
		int passId = getNextPassId();

//		System.out.println(currGridx + " : " + currGridy + " : " + tMaxX + " : " + tMaxY);
		K cell;
		while(tMaxX <= 1 || tMaxY <= 1)
		{
//			System.out.println("   * " + tMaxX + " : " + tMaxY);
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
			cell = getCell(currGridx, currGridy);
			if(cell != null)
			{
				if(queryCell(cell, sensor, passId))
					return sensor;
			}
		}		
		
		return sensor;
	}

	private interface IObjectConsumer <T> extends IChunkConsumer
	{
		public void setObject(T object);
	}
	
	private IObjectConsumer <O> addingConsumer = new IObjectConsumer <O>()
	{
		private O object;
		
		public void setObject(O object)
		{
			this.object = object;
		}
		
		@Override
		public boolean consume(IAreaChunk chunk)
		{
			K cell = getCell( chunk.getX(), chunk.getY());
			if(cell == null)
			{
				cell = createEmptyCell(chunk.getX(), chunk.getY());
				put( chunk.getX(), chunk.getY(), cell );
			}
			
			return addToCell( cell, object );
		}
	};
	
	private IObjectConsumer <O> removingConsumer = new IObjectConsumer <O>()
	{
		private O object;
		
		public void setObject(O object)
		{
			this.object = object;
		}
		
		@Override
		public boolean consume(IAreaChunk chunk)
		{
			K cell = getCell( chunk.getX(), chunk.getY());
			if(cell == null)
				return false;
			
			return removeFromCell( cell, object );
		}
	};
	
	private interface IQueryingConsumer <T> extends IChunkConsumer
	{
		public void setSensor(ISpatialSensor <T> sensor);

		public void setQueryId(int nextPassId);
	}
	
	private IQueryingConsumer <O> queryingConsumer = new IQueryingConsumer <O>()
	{
		private ISpatialSensor <O> processor;
		private int passId;
		
		public void setSensor(ISpatialSensor <O> processor)
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
			K cell = getCell(chunk.getX(), chunk.getY());
			if(cell != null)
			{
//				System.out.println(chunk.getX() + " : " + chunk.getY());
				return queryCell(cell, processor, passId);
			}
			return false;
		}
	};

}
