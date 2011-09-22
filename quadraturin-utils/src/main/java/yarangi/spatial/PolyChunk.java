package yarangi.spatial;

/**
 * A piece of polygon that fits single grid cell.
 * 
 * TODO: add some meat 
 */
public class PolyChunk implements IAreaChunk
{
	private double x, y;
	
	private Polygon parent;
	
	public PolyChunk(Polygon parent, double x, double y) {
		this.x = x;
		this.y = y;
		
		this.parent = parent;
		
	}

	@Override public Area getArea() { return parent; }
	public Polygon getParent() { return parent; }

	@Override public double getX() { return x; }
	@Override public double getY() { return y; }
	
	@Override public double getMinX() { return x; }
	@Override public double getMinY() { return y; }
	@Override public double getMaxX() { return x; }
	@Override public double getMaxY() { return y; }

	@Override
	public boolean overlaps(double xmin, double ymin, double xmax,
			double ymax) {
		// TODO: there should be more logic here :)
		return true;
	}

	
}
