package yarangi.graphics.quadraturin.terrain;

import yarangi.spatial.Area;
import yarangi.spatial.IAreaChunk;

/**
 * 
 * Acts as a container for single grid tile; 
 * Provides binding to world coordinates of this tile
 * @author dveyarangi
 *
 * @param <T>
 */
public class Cell <T> implements IAreaChunk
{
	/**
	 * Cell contents
	 */
	private T tile;
	
	private int passId;
	
	private double ox, oy, cellsize;
	
	public Cell(double ox, double oy, double cellsize, T props)
	{
		this.ox = ox;
		this.oy = oy;
		this.cellsize = cellsize;
		this.tile = props;
	}
	
	public T getProperties() { return tile; }
	public void setProperties(T props) { tile = props; }
	
	public void setPassId(int passId) 
	{
		this.passId = passId;
	}
	
	public int getPassId()
	{
		return passId;
	}

	@Override public Area getArea() { return null; }

	@Override public double getX() { return ox; }
	@Override public double getY() { return oy; }
	@Override public boolean overlaps(double xmin, double ymin, double xmax, double ymax)
	{
		return ( (xmax >= ox && xmax <= ox+cellsize) ||
			     (xmin >= ox && xmin <= ox+cellsize) ||
			     (xmin >= ox && xmax <= ox+cellsize) ||
			     (xmin <= ox && xmax >= ox+cellsize)    
			  ) && ( 
			     (ymax >= oy && ymax <= oy+cellsize) ||
			     (ymin >= oy && ymin <= oy+cellsize) ||
			     (ymin >= oy && ymax <= oy+cellsize) ||
			     (ymin <= oy && ymax >= oy+cellsize)    
			   );
	}

	@Override public double getMinX() { return ox; }
	@Override public double getMinY() { return oy; }
	@Override public double getMaxX() { return ox+cellsize; }
	@Override public double getMaxY() { return oy+cellsize; }

}
