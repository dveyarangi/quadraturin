package yarangi.graphics.quadraturin.terrain;

import yarangi.spatial.Area;
import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ITile;

public class Cell <P> implements IAreaChunk, ITile
{
	private P properties;
	
	private int passId;
	
	public Cell(P props)
	{
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

	@Override public double getX() { return 0; }
	@Override public double getY() { return 0; }
	@Override
	public boolean overlaps(double xmin, double ymin, double xmax, double ymax)
	{
		return false;
	}

	@Override public double getMinX() { return 0; }
	@Override public double getMinY() { return 0; }
	@Override public double getMaxX() { return 0; }
	@Override public double getMaxY() { return 0; }
}
