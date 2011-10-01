package yarangi.spatial;

import java.util.LinkedList;
import java.util.List;

import yarangi.math.FastMath;

/**
 * Encapsulates the functionality shared between various grid structures.
 * 
 * Note: cannot be used in multi-threaded environment, due to passId optimization (and lack of any type of synchronization).
 *
 * @param <K>
 */
public abstract class GridMap <K, O> implements IGrid
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
	 * edges of represented area
	 */
	private float minX, minY, maxX, maxY;

	/**
	 * size of single grid cell
	 */
	private int cellSize; 
	
	/** 
	 * 1/cellSize, to speed up some calculations
	 */
	private double invCellsize;
	
	/** 
	 * cellSize/2
	 */
	private float halfCellSize;
	
	/**
	 * grid dimensions 
	 */
	private int gridWidth, gridHeight;
	
	/**
	 * grid dimensions 
	 */
	private int halfGridWidth, halfGridHeight;
	
	/**
	 * Used by query methods to mark tested objects and avoid result duplication;
	 * thusly permits only single threaded usage. 
	 */
	private int passId;
	
	protected List <K> modifiedCells = new LinkedList <K> ();
	
	protected List <IGridListener <K>> listeners = new LinkedList <IGridListener <K>> ();
	
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

	/**
	 * Creates an empty grid cell.
	 * @param x - cell reference x
	 * @param y - cell reference y
	 * @return
	 */
	protected abstract K createEmptyCell(int idx, double x, double y);
	
	/**
	 * Create a grid array. Array indexes must be consistent with
	 * {@link #indexAtCell(int, int)} implementation.
	 * @param cellSize
	 * @param width
	 * @param height
	 * @return
	 */
	protected abstract K [] createMap(int cellSize, int width, int height);
	
	/**
	 * Calculates {@link GridMap#map} index for cell index.
	 * @param i cell x cell coordinate (from 0 to gridWidth-1)
	 * @param j cell y cell coordinate (from 0 to gridHeight-1)
	 * @return
	 */
	protected abstract int indexAtCell(int i, int j);
	
	/**
	 * Converts model coordinates to grid array index.
	 * @param x
	 * @param y
	 * @return
	 */
	protected int indexAtCoord(double x, double y)
	{
		return indexAtCell(toGridXIndex(x), toGridYIndex(y));
	}

	/**
	 * Converts a model x coordinate to cell dimensional index.
	 * @param value
	 * @return
	 */
	public final int toGridXIndex(double value)
	{
		return FastMath.round(value * invCellsize) + halfGridWidth;
	}
	
	/**
	 * Converts a model x coordinate to cell dimensional index.
	 * @param value
	 * @return
	 */
	public final int toLowerGridXIndex(double value)
	{
		return FastMath.floor(value * invCellsize) + halfGridWidth;
	}
	/**
	 * Converts a model x coordinate to cell dimensional index.
	 * @param value
	 * @return
	 */
	public final int toHigherGridXIndex(double value)
	{
		return FastMath.ceil(value * invCellsize) + halfGridWidth;
	}
	
	/**
	 * Converts x grid index to model coordinate
	 * @param i
	 * @return
	 */
	public final double toRealXIndex(int i)
	{
		return ((double)(i-halfGridWidth)) * cellSize;
	}
	/**
	 * Converts a model y coordinate to cell dimensional index.
	 * @param value
	 * @return
	 */
	public final int toGridYIndex(double value)
	{
		return FastMath.round(value * invCellsize) + halfGridHeight;
	}
	/**
	 * Converts a model x coordinate to cell dimensional index.
	 * @param value
	 * @return
	 */
	public final int toLowerGridYIndex(double value)
	{
		return FastMath.floor(value * invCellsize) + halfGridHeight;
	}
	/**
	 * Converts a model x coordinate to cell dimensional index.
	 * @param value
	 * @return
	 */
	public final int toHigherGridYIndex(double value)
	{
		return FastMath.ceil(value * invCellsize) + halfGridHeight;
	}
	
	/**
	 * Converts y grid index to model coordinate
	 * @param i
	 * @return
	 */
	public final double toRealYIndex(int i)
	{
		return ((double)(i - halfGridHeight)) * cellSize;
	}
	
	/**
	 * Query specified cell and feed the results to the sensor.
	 * 
	 * @param cell
	 * @param sensor
	 * @param queryId may be used to mark objects during query exxecution.
	 * @return
	 */
	protected abstract boolean queryCell(K cell, ISpatialSensor<O> sensor, int queryId);
	
	/**
	 * Implements cell update logic
	 * @param cell
	 * @param object
	 * @return
	 */
	protected abstract boolean addToCell(K cell, O object);
	
	/**
	 * Implements cell update logic
	 * @param cell
	 * @param object
	 * @return
	 */
	protected abstract boolean removeFromCell(K cell, O object);
	
	/**
	 * Creates an id for query procedure. 
	 * @return
	 */
	protected final int createNextQueryId() { return ++passId; }
	
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
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public final K getCell(double x, double y)
	{
		int idx = indexAtCoord(x,y);
		if(idx < 0 || idx >= map.length)
			return null;
		return map[idx];
	}
	
	/**
	 * Retrieve contents of cell at specified grid index.
	 * @param x
	 * @param y
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public final K getCell(int x, int y)
	{
		int idx = indexAtCell(x, y);
		if(idx < 0 || idx >= map.length)
			return null;
		return map[idx];
	}
	
	public final void put(double x, double y, O tile)
	{
		// TODO: dissolve, if hitting not in the cell center?
//		System.out.println(x + " : " + y + " : " + at(x,y));
		int idx = indexAtCoord(x,y);
		K cell = map[idx];
		if(cell == null)
		{
			cell = createEmptyCell( idx, FastMath.toGrid( x, cellSize ), FastMath.toGrid( y, cellSize ) );
			map[idx] = cell;
		}
		if(addToCell( cell, tile ))
			setModified(cell);
				
	}
	
	public final void remove(double x, double y, O tile)
	{
		// TODO: dissolve, if hitting not in the cell center?
//		System.out.println(x + " : " + y + " : " + at(x,y));
		K cell = map[indexAtCoord(x,y)];
		if(cell == null)
			return;
		
		if(removeFromCell( cell, tile ))
			setModified(cell);
				
	}
	
	protected final void put(int x, int y, K cell)
	{
		// TODO: dissolve, if hitting not in the cell center?
//		System.out.println(x + " : " + y + " : " + at(x,y));
		map[indexAtCell( x, y )] = cell;
	}

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
	 * Adds a cell to modified cells queue.
	 * @param cell
	 */
	protected void setModified(K cell)
	{
		modifiedCells.add( cell );
	}
	
	/**
	 * Retrieves a list of modified cells.
	 * @return
	 */
	protected List <K> getModifiedCells()
	{
		return modifiedCells;
	}
	
	public void addListener(IGridListener <K> l) { this.listeners.add( l ); }
	public void removeListener(IGridListener <K> l) { this.listeners.add( l ); }
	
	/**
	 * Fires cell modification event to listeners and clears modified cells queue.
	 */
	public void fireGridModified()
	{
		for(IGridListener <K> l : listeners)
		{
			l.cellsModified( modifiedCells );
		}
		
		// resetting modified cells queue:
		modifiedCells = new LinkedList <K> ();
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
		queryingConsumer.setQueryId(createNextQueryId());
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
		int minx = Math.max(toLowerGridXIndex(x-radius-cellSize), 0);
		int miny = Math.max(toLowerGridYIndex(y-radius-cellSize), 0);
		int maxx = Math.min(toHigherGridXIndex(x+radius), gridWidth);
		int maxy = Math.min(toHigherGridYIndex(y+radius), gridHeight);
		int passId = createNextQueryId();
		K cell;
		for(int tx = minx; tx <= maxx; tx ++)
			for(int ty = miny; ty <= maxy; ty ++)
			{
				cell = getCell(tx, ty);
				
//				double distanceSquare = FastMath.powOf2(x - tx*cellSize) + FastMath.powOf2(y - ty*cellSize);
//				if(radiusSquare < distanceSquare)
//					continue;
				
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
		
		int passId = createNextQueryId();

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
			put( chunk.getX(), chunk.getY(), object );
			
			return false;
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
			remove(chunk.getX(), chunk.getY(), object);
			
			return false;
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
