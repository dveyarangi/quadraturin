package yarangi.spatial;


/**
 *  
 * 
 */
public class CellChunk implements IAreaChunk
{

	private double x, y;
	
	private double minx, maxx, miny, maxy;
	
	private Area area;
	
	public CellChunk(Area area, double x, double y, double cellsize)
	{
		this.area = area;
		this.x = x;
		this.y = y;
		
		double halfsize = cellsize/2;
		
		this.minx = Math.max(x - halfsize, area.getRefPoint().x()-area.getMaxRadius());
		this.maxx = Math.min(x + halfsize, area.getRefPoint().x()+area.getMaxRadius());
		this.miny = Math.max(y - halfsize, area.getRefPoint().y()-area.getMaxRadius());
		this.maxy = Math.min(y + halfsize, area.getRefPoint().y()+area.getMaxRadius());
	}
	
	@Override final public Area getArea() { return area; }

	@Override final public double getX() { return x; }
	@Override final public double getY() { return y; }

	@Override final public double getMinX() { return minx; }
	@Override final public double getMinY() { return miny; }
	@Override final public double getMaxX() { return maxx; }
	@Override final public double getMaxY() { return maxy; }

/*		@Override
	final public boolean overlaps(IAreaChunk chunk) 
	{
		if(chunk instanceof AABB)
		{
			AABB aabb = (AABB) chunk;
			return overlaps(aabb.x - aabb.r, aabb.x + aabb.r, aabb.y - aabb.r, aabb.y + aabb.r);
		}
		if(chunk instanceof Polygon)
		{
			Polygon polygon = (Polygon) polygon;
			return overlaps(polygon);
		}
		
		throw new IllegalArgumentException("Type of area chunk " + chunk + " is not supported.");
	}
	
	private boolean overlaps(Polygon polygon) {
		throw new IllegalArgumentException("Type of area chunk " + chunk + " is not supported.");

	}*/

	final public boolean overlaps(double xmin, double ymin, double xmax, double ymax)
	{
		return ( (xmax >= minx && xmax <= maxx) ||
			     (xmin >= minx && xmin <= maxx) ||
			     (xmin >= minx && xmax <= maxx) ||
			     (xmin <= minx && xmax >= maxx)    
			  ) && ( 
			     (ymax >= miny && ymax <= maxy) ||
			     (ymin >= miny && ymin <= maxy) ||
			     (ymin >= miny && ymax <= maxy) ||
			     (ymin <= miny && ymax >= maxy)    
			   );
	}

	public boolean equals(Object o)
	{
		if(!(o instanceof CellChunk))
			return false;
		
		CellChunk chunk = (CellChunk) o;
		return this.getArea().equals(chunk.getArea());
	}
	
	public int hashCode() { return this.getArea().hashCode(); }
}

