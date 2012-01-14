package yarangi.spatial;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import yarangi.Zen;
import yarangi.math.FastArrays;
import yarangi.math.FastMath;
import yarangi.math.Vector2D;

public class PolygonArea implements Area 
{
	private List <PolyPoint> points = new ArrayList <PolyPoint> ();
	
	
	private Vector2D ref;
	
	private PolyPoint maxx, maxy, minx, miny;
	
	private double radius = 1;
	
	private int passId;
	
	public PolygonArea(double x, double y) 
	{ 
		this.ref = Vector2D.R(x, y);
	}
	
	private PolygonArea(PolygonArea polygon)
	{

		for(PolyPoint point : polygon.points)
			points.add(point);
		
		maxx = polygon.maxx;
		minx = polygon.minx;
		maxy = polygon.maxy;
		miny = polygon.miny;
		
		ref = Vector2D.COPY(polygon.ref);
	}
	
	public void add(int idx, double x, double y) 
	{ 
		PolyPoint newPoint = new PolyPoint(x, y);
		this.points.add(idx, newPoint);
		updateAABB(newPoint);
	}
	
	
	public void add(double x, double y) 
	{ 
		PolyPoint newPoint = new PolyPoint(x, y);
		this.points.add(newPoint); 
		updateAABB(newPoint);
	}
	
	final private void updateAABB(PolyPoint point)
	{
		if(minx == null || minx.x() > point.x())
			minx = point;
		if(maxx == null || maxx.x() < point.x())
			maxx = point;
		if(miny == null || miny.y() > point.y())
			miny = point;
		if(maxy == null || maxy.y() < point.y())
			maxy = point;
	}

	
	public void add(int idx, Vector2D v) { add(idx, v.x(), v.y()); }
	
	public void add(Vector2D v) 
	{
		add(v.x(), v.y()); 
	}
	
	// TODO: test idx % points.size() against idx < points.size() ? idx : 0;
	public void remove(int idx) { this.points.remove(idx); }
	
	public PolyPoint get(int idx) { return this.points.get(idx); }
	
	public PolygonArea clone()
	{
		return new PolygonArea(this);
	}


	@Override public Vector2D getRefPoint() { return ref; }

	@Override
	public double getOrientation() { return 0; }

	@Override
	public void setOrientation(double a) { throw new IllegalStateException("This method is not yet implemented"); }
	@Override
	public void fitTo(double radius)
	{
		this.radius = radius;
	}

	@Override
	public double getMaxRadius() { return radius; }
	
	@Override
	public void translate(double dx, double dy) {
		ref.add(dx, dy);
	}
	

	@Override
	public void iterate(int cellsize, IChunkConsumer consumer)
	{
		List <PolyChunk> chunks = generateChunks(cellsize);
		for(PolyChunk chunk : chunks)
			consumer.consume( chunk );
	}
	

	final public List <PolyPoint> getPoints() { return points; }
	

	final public int size() { return points.size(); }
	
	
	private List <PolyChunk> generateChunks(int cellsize)
	{
		List <PolyChunk> list = new LinkedList <PolyChunk> ();
		
		/** vertex of current polygon line */
		PolyPoint currPoint, nextPoint;
		
		/** location of the next point in grid.  */
//		double currx = tempPoint.x();
//		double curry = tempPoint.y();
		
		
		int currGridx, currGridy;
		/** location of the next point in grid.  */
		int nextGridx, nextGridy;
		
		/** step to next point in grid */
		double dx, dy;
		
		int startGridx, startGridy;
		
		int currStartIdx, currEndIdx;
		
		int pointsNum = this.size();
		
		int tempGridx, tempGridy;
		
		currStartIdx = 0;
		
		PolyPoint tempPoint = this.get(0);
		
		tempGridx = currGridx = nextGridx = startGridx = FastMath.toGrid(tempPoint.x(), cellsize); 
		tempGridy = currGridy = nextGridy = startGridy = FastMath.toGrid(tempPoint.y(), cellsize); 
		// searching for first polygon point in this grid cell:
		do {
			currPoint = tempPoint;
			
			currStartIdx = FastArrays.dec(currStartIdx, pointsNum);
			
			tempPoint = points.get(currStartIdx);
			tempGridx = FastMath.toGrid(tempPoint.x(), cellsize);
			tempGridy = FastMath.toGrid(tempPoint.y(), cellsize);
//			System.out.println(" * testing at (" + tempGridx + "," + tempGridy + ")");
		} 
		while(tempGridx == startGridx
		   && tempGridy == startGridy);

		nextPoint = currPoint;
		
		currEndIdx = currStartIdx+1;
		
		// line scan parameters (from "A Fast Voxel Traversal Algorithm for Ray Tracing" article): 
		double tMaxX=1, tMaxY=1;
		double tDeltaX=0, tDeltaY=0;
		double stepX=0 , stepY=0;
		
		do
		{
			
			// in a dense poly, this condition will be met frequently:
			if(tMaxX >= 1 && tMaxY >= 1) // we reached poly line end 
			{
				// calculating its indexes range inside current cell:
				currStartIdx = FastArrays.dec(currEndIdx, size());
				currEndIdx = currStartIdx;
			
				do {
					currPoint = nextPoint;
					currEndIdx = FastArrays.inc(currEndIdx, size());
					nextPoint = PolygonArea.this.get(currEndIdx);
					
					nextGridx = FastMath.toGrid(nextPoint.x(), cellsize);
					nextGridy = FastMath.toGrid(nextPoint.y(), cellsize);
				}
				while(nextGridx == currGridx
				   && nextGridy == currGridy);
				
				// 
				list.add(new PolyChunk(currGridx, currGridy, currStartIdx, currEndIdx));
				
				
				// calculating parameters for line scan :
				
				dx = nextPoint.x() - currPoint.x();
				dy = nextPoint.y() - currPoint.y();
				
				if(dx > 0)
				{
					tMaxX = ((currGridx + cellsize/2.) - currPoint.x()) / dx;
					tDeltaX = cellsize / dx;
					stepX = cellsize;
				}					
				else
				if(dx < 0)
				{
					tMaxX = ((currGridx - cellsize/2.) - currPoint.x()) / dx;
					tDeltaX = -cellsize / dx;
					stepX = -cellsize;
				}
				else { tMaxX = Double.MAX_VALUE;}
				
				if(dy > 0)
				{
					tMaxY = ((currGridy + cellsize/2.) - currPoint.y()) / dy;
					tDeltaY = cellsize / dy;
					stepY = cellsize;
				}
				else
				if(dy < 0)
				{
					tMaxY = ((currGridy - cellsize/2.) - currPoint.y()) / dy;
					tDeltaY = -cellsize / dy;
					stepY = -cellsize;
				}
				else { tMaxY = Double.MAX_VALUE;}
				

			}
			
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
			
			list.add(new PolyChunk(currGridx, currGridy, currStartIdx, currEndIdx));
			
		}
		while((currGridx != startGridx || currGridy != startGridy));
		
		return list;
	}
	
	public class PolyPoint extends Vector2D
	{
		private int nextConvexIdx, prevConvexIdx;
		
		public PolyPoint(double x, double y)
		{
			super(x, y);
		}
		
		public void setPrevConvexIdx(int idx) { this.prevConvexIdx = idx; } 
		public void setNextConvexIdx(int idx) { this.nextConvexIdx = idx; }
		
		public double getNextConvexIdx() { return nextConvexIdx; }
		public double getPrevConvexIdx() { return prevConvexIdx; }
	}
	
	/**
	 * A piece of polygon that fits single grid cell.
	 * 
	 * TODO: add some meat 
	 */
	public class PolyChunk implements IAreaChunk
	{
		private int x, y;
		
		private List <Vector2D> pieces;
		
		private int minIdx;
		private int maxIdx;
		public PolyChunk(int x, int y, int minIdx, int maxIdx) 
		{
			
			this.x = x + FastMath.round(PolygonArea.this.getRefPoint().x());
			this.y = y + FastMath.round(PolygonArea.this.getRefPoint().y());
			
			this.minIdx = minIdx; 
			this.maxIdx = maxIdx;
		}
		
		
		public void add(Vector2D piece) { pieces.add(piece); }

		@Override public Area getArea() { return PolygonArea.this; }

		@Override public double getX() { return x; }
		@Override public double getY() { return y; }
		
		@Override public double getMinX() { return x; }
		@Override public double getMinY() { return y; }
		@Override public double getMaxX() { return x; }
		@Override public double getMaxY() { return y; }

		@Override
		public boolean overlaps(double xmin, double ymin, double xmax,
				double ymax) {
			// TODO: implement overlapping test
			return true;
		}
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}


		@Override
		public boolean equals(Object obj)
		{
			if ( this == obj )
				return true;
			if ( obj == null )
				return false;
			if ( getClass() != obj.getClass() )
				return false;
			PolyChunk other = (PolyChunk) obj;
			if ( !getOuterType().equals( other.getOuterType() ) )
				return false;
			if ( x != other.x )
				return false;
			if ( y != other.y )
				return false;
			return true;
		}
		
		public String toString()
		{
			return "Polychunk at [" + x + "," + y + "], idx[" + minIdx + ":" + maxIdx + "].";
		}


		private PolygonArea getOuterType()
		{
			return PolygonArea.this;
		}
	}

	@Override
	public List<Vector2D> getDarkEdge(Vector2D from)
	{
		List <Vector2D> res = new ArrayList <Vector2D> ();

		Vector2D minPoint = null;
		Vector2D maxPoint = null;
		Vector2D areaCenter = ref.minus(from);
		
		Vector2D prev = points.get( points.size()-1 ).plus( areaCenter );
		Vector2D curr = points.get( 0 ).plus( areaCenter );
		Vector2D next;
		double c1 = prev.crossZComponent( curr ), c2;
		for(int idx = 0; idx < points.size(); idx ++)
		{
			next = points.get(FastArrays.inc( idx, points.size() )).plus( areaCenter );
//			System.out.println(prev + " : " + curr + " : " + next);
			c2 = next.crossZComponent( curr );
//			System.out.println(prev.cross);
//			System.out.println(c1 + " : " + c2);
			if(c1 >= 0 && c2 >= 0)
				minPoint = curr;
			if(c1 <= 0 && c2 <= 0)
				maxPoint = curr;
			
			if(minPoint != null && maxPoint != null)
				break;
			
			prev = curr;
			curr = next;
			c1 = -c2;
		}
		
//		res.add( new Vector2D(minPoint.x(), minPoint.y()) );
//		res.add( new Vector2D(maxPoint.x(), maxPoint.y()) );
		
		if(minPoint != null)
			res.add( Vector2D.COPY( minPoint ) );
		if(maxPoint != null)
			res.add( Vector2D.COPY(maxPoint) );

		return res;
	}


	@Override
	public int getPassId() { return passId; }

	@Override
	public void setPassId(int id) {	this.passId = id; }

	@Override
	public boolean overlaps(AABB area)
	{
		return Zen.notSupported();
	}
}
