package yarangi.spatial;

import java.util.LinkedList;

import yarangi.ZenUtils;
import yarangi.math.Vector2D;

/**
 * A point area implementation.
 * 
 * @author dveyarangi
 */
public class PointArea implements Area 
{
	private Vector2D ref;
	
	private int passId;
	
	public PointArea(double x, double y)
	{
		ref = new Vector2D(x, y);
	}
	
	/**
	 * Creates new point area at specified location.
	 * @param point
	 */
	public PointArea(Vector2D point)
	{
		this( point.x(), point.y() );
	}
	
	@Override
	public void iterate(int cellsize, IChunkConsumer consumer) 
	{ 
		consumer.consume( new PointChunk(ref) );
	}
	
	@Override
	public double getOrientation() 
	{
		return 0;
	}

	public double getMaxRadius() { return 0; }

	@Override
	public void setOrientation(double a) 
	{
		ZenUtils.methodNotSupported(this.getClass());
	}


	@Override
	public Vector2D getRefPoint() {
		return ref;
	}

	@Override
	public void translate(double dx, double dy) 
	{
		ref.add(dx, dy);
	}
	@Override
	public void fitTo(double radius)
	{
		ZenUtils.methodNotSupported(this.getClass());
	}
	
	public Area clone()
	{
		return new PointArea(ref);
	}
	
	public String toString()
	{
		return new StringBuilder()
			.append( "Point area [").append(ref).append("]")
			.toString();
	}
	
	class PointChunk extends Vector2D implements IAreaChunk
	{

		public PointChunk(Vector2D ref)
		{
			super(ref);
		}

		@Override
		public Area getArea() { return PointArea.this; }

		@Override
		public double getX() { return x(); }

		@Override
		public double getY() { return y(); }

		@Override
		public boolean overlaps(double xmin, double ymin, double xmax, double ymax) {
			return x() >= xmin && x() <= xmax && y() >= ymin && y() <= ymax; 
		}

		@Override
		public double getMinX() { return x(); }

		@Override
		public double getMinY() { return y(); }

		@Override
		public double getMaxX() { return x(); }

		@Override
		public double getMaxY() { return y(); }
		
		public boolean equals(Object o)
		{
			if(!(o instanceof PointChunk))
				return false;
			
			PointChunk chunk = (PointChunk) o;
			return this.getArea().equals(chunk.getArea());
		}
		
		public int hashCode() { return this.getArea().hashCode(); }
		
	}

	@Override
	public LinkedList<Vector2D> getDarkEdge(Vector2D from)
	{
		return new LinkedList <Vector2D> ();
	}


	@Override
	public int getPassId() { return passId; }

	@Override
	public void setPassId(int id) {	this.passId = id; }
}
