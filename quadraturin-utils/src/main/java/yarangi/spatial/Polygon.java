package yarangi.spatial;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import yarangi.math.Vector2D;

public class Polygon implements Area 
{
	private List <Vector2D> points = new ArrayList <Vector2D> ();
	
	
	private Vector2D maxx, maxy, minx, miny;
	
	public Polygon() { }
	
	private Polygon(Polygon polygon)
	{

		for(Vector2D point : polygon.points)
			points.add(point);
		
		maxx = polygon.maxx;
		minx = polygon.minx;
		maxy = polygon.maxy;
		miny = polygon.miny;
	}
	
	public void add(int idx, Vector2D point) 
	{ 
		this.points.add(idx, point);
		updateAABB(point);
	}
	
	
	public void add(Vector2D point) 
	{ 
		this.points.add(point); 
		updateAABB(point);
	}
	
	final private void updateAABB(Vector2D point)
	{
		if(minx == null || minx.x > point.x)
			minx = point;
		if(maxx == null || maxx.x < point.x)
			maxx = point;
		if(miny == null || miny.y > point.y)
			miny = point;
		if(maxy == null || maxy.y < point.y)
			maxy = point;
	}

	
	public void add(int idx, double x, double y) { add(idx, new Vector2D(x, y)); }
	
	public void add(double x, double y) { add(new Vector2D(x, y)); }
	
	public void remove(int idx) { this.points.remove(idx); }
	
	public Vector2D get(int idx) { return this.points.get(idx); }
	
	public Area clone()
	{
		return new Polygon(this);
	}

	@Override
	public Iterator<IAreaChunk> iterator(double cellsize) {
		// TODO Auto-generated method stub
//		return null;
	}
}
