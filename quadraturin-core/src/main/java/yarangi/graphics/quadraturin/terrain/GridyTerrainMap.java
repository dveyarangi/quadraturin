package yarangi.graphics.quadraturin.terrain;

import yarangi.graphics.quadraturin.simulations.IPhysicalObject;
import yarangi.spatial.GridMap;
import yarangi.spatial.ISpatialSensor;

public class GridyTerrainMap <K extends IPhysicalObject> extends GridMap<Cell <K>, K> implements ITerrainMap <K>
{
	
	public GridyTerrainMap(float width, float height, int cellsize)
	{
		super(cellsize, (int)width, (int)height);
	}

	@Override
	public Cell<K>[] createMap(int cellSize, int width, int height)
	{
		return (Cell <K> []) new Cell [width*height]; // ugly, as always	
	}

	@Override
	public final int at(int x, int y) 
	{
		return  x + getGridWidth() * y;
	}

	@Override
	protected boolean queryCell(Cell<K> cell, ISpatialSensor<K> sensor, int queryId)
	{
		if(cell.getProperties() == null)
			return false;
		
		return sensor.objectFound( cell, cell.getProperties() );
	}

	@Override
	protected boolean addToCell(Cell<K> cell, K object)
	{
		cell.setProperties( object );
		return true;
	}

	@Override
	protected boolean removeFromCell(Cell<K> cell, K object)
	{
		cell.setProperties( null );
		return true;
	}

	@Override
	protected Cell<K> createEmptyCell()
	{
		return new Cell <K> (null);
	}

}
