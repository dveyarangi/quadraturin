package yarangi.graphics.quadraturin.terrain;

import yarangi.spatial.Area;
import yarangi.spatial.IAreaChunk;

public class Cell <P> implements IAreaChunk
{
	private P properties;
	
	private int passId;
	
	private double ox, oy, cellsize;
	
	public Cell(double ox, double oy, double cellsize, P props)
	{
		this.ox = ox;
		this.oy = oy;
		this.cellsize = cellsize;
		this.properties = props;
	}
	
	public P getProperties() { return properties; }
	public void setProperties(P props) { properties = props; }
	
	public void setPassId(int passId) 
	{
		this.passId = passId;
	}
	
	public int getPassId()
	{
		return passId;
	}

	@Override
	public Area getArea() { return null; }

	@Override public double getX() { return ox; }
	@Override public double getY() { return oy; }
	@Override
	public boolean overlaps(double xmin, double ymin, double xmax, double ymax)
	{
		return false;
	}

	@Override public double getMinX() { return ox; }
	@Override public double getMinY() { return oy; }
	@Override public double getMaxX() { return ox+cellsize; }
	@Override public double getMaxY() { return oy+cellsize; }

}
