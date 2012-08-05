package yarangi.graphics.quadraturin.terrain;

import yarangi.graphics.quadraturin.objects.IBeing;
import yarangi.physics.Body;
import yarangi.spatial.AABB;
import yarangi.spatial.Area;

import com.seisw.util.geom.Poly;
import com.seisw.util.geom.PolyDefault;

public class TilePoly implements IBeing, ITilePoly 
{
	/**
	 * Specified the borders of this tile
	 */
	private final Poly borderPoly;
	
	/**
	 * Specifies real structure of this tile:
	 */
	private Poly structurePoly;
	
	private final Area area;
	
	private boolean isFull = false;
	
	private final double minx, miny, maxx, maxy;
	
	public TilePoly(double minx, double miny, double maxx, double maxy)
	{
		
		this.minx = minx;
		this.miny = miny;
		this.maxx = maxx;
		this.maxy = maxy;
		
		// used to clip larger than tile polygons
		borderPoly = new PolyDefault();
		borderPoly.add( minx, miny );
		borderPoly.add( minx, maxy );
		borderPoly.add( maxx, maxy );
		borderPoly.add( maxx, miny );
		
/*		structurePoly = new PolyDefault();
		structurePoly.add( minx, miny );
		structurePoly.add( minx, maxy );
		structurePoly.add( maxx, maxy );
		structurePoly.add( maxx, miny );*/
		area = AABB.createFromEdges( minx, miny, maxx, maxy, 0 );
	}
	
	public Poly getPoly() 
	{
		return structurePoly;
	}
	
	@Override
	public void add(Poly poly) {
		
		Poly temp = poly.intersection( borderPoly );
		if(temp.isEmpty())
			return;
		if(structurePoly == null) {
			structurePoly = temp;
		}
		else 
		{
//			temp = structurePoly.intersection( temp );
			structurePoly = structurePoly.union( temp );
		}
		if(!isFull)
			isFull = structurePoly.xor( borderPoly ).isEmpty();
		//     ^ clip to tile             ^ add current structure
	}
	
	@Override
	public void substract(Poly poly) {
		
		Poly temp = poly.intersection( borderPoly );
		if(temp.isEmpty())
			return;
		
		temp = borderPoly.xor( temp );
		
		if(temp.isEmpty()) {
			return;
		}
			
		structurePoly = structurePoly.intersection( temp );
		if(structurePoly.isEmpty() || structurePoly.getNumPoints() < 2)
			structurePoly = null;
		
		if(structurePoly == null)
			return;
		
		if(isFull)
			isFull = structurePoly.xor( borderPoly ).isEmpty();
	
	}

	@Override
	public boolean isEmpty()
	{
		return structurePoly == null;
	}
	@Override
	public boolean isAlive()
	{
		// TODO Auto-generated method stub
		return!isEmpty();
	}

	@Override
	public Area getArea()
	{
		
		return area;
	}

	@Override
	public Body getBody()
	{
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public void markDead()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isIndexed()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override public double getMinX() { return minx; }
	@Override public double getMinY() {	return miny; }

	@Override public double getMaxX() { return maxx; }
	@Override public double getMaxY() {	return maxy; }
	
	@Override
	public boolean isFull()
	{
		return isFull;
	}
	
}
