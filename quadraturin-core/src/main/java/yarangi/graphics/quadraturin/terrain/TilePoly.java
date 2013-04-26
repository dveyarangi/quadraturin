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
	
	private final AABB area;
	
	private boolean isFull = false;
	
	private final float minx, miny, maxx, maxy;
	
	public TilePoly(float minx, float miny, float maxx, float maxy)
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
	
	@Override
	public Poly getPoly() 
	{
		return structurePoly;
	}
	
	@Override
	public boolean add(Poly poly) {
		
		if(isFull)
			return false;
		
		Poly temp = poly.intersection( borderPoly );
		if(temp.isEmpty())
			return false;
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
		
		return true;
		//     ^ clip to tile             ^ add current structure
	}
	
	@Override
	public boolean substract(Poly poly) {
		
		if(isEmpty())
			return false;
		
		// crop to tile:
		Poly temp = poly.intersection( borderPoly );
		if(temp.isEmpty())
			return false;
		
		// inverse:
		temp = borderPoly.xor( temp );
		
		if(temp.isEmpty()) {
			return false;
		}
			
		structurePoly = structurePoly.intersection( temp );
		if(structurePoly.isEmpty()) 
			structurePoly = null;
		
		if(isEmpty()) {
			isFull = false;
			return true;
		}
		
		if(isFull)
			isFull = structurePoly.xor( borderPoly ).isEmpty();
	
		return true;
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
	public AABB getArea()
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

	@Override public float getMinX() { return minx; }
	@Override public float getMinY() {	return miny; }

	@Override public float getMaxX() { return maxx; }
	@Override public float getMaxY() {	return maxy; }
	
	@Override
	public boolean isFull()
	{
		return isFull;
	}
	
}
