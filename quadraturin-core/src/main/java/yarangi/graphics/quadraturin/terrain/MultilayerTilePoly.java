package yarangi.graphics.quadraturin.terrain;

import yarangi.physics.Body;
import yarangi.spatial.AABB;
import yarangi.spatial.Area;

import com.seisw.util.geom.Poly;
import com.seisw.util.geom.PolyDefault;

public class MultilayerTilePoly implements ITerrain, ITilePoly
{
	/**
	 * Specified the borders of this tile
	 */
	private final Poly borderPoly;
	
	private final double minx, miny, maxx, maxy;
	
	/**
	 * Specifies real structure of this tile:
	 */
	private final Poly [] structurePolys;
	
	private final AABB area;
	
	private boolean isFull = false;
	
	public MultilayerTilePoly(double minx, double miny, double maxx, double maxy, int layersNum, boolean fill)
	{
		this.minx = minx;
		this.maxx = maxx;
		this.miny = miny;
		this.maxy = maxy;
		
		// used to clip larger than tile polygons
		borderPoly = new PolyDefault();
		borderPoly.add( minx, miny );
		borderPoly.add( minx, maxy );
		borderPoly.add( maxx, maxy );
		borderPoly.add( maxx, miny );
		
		structurePolys = new Poly [layersNum];
		
		if(fill) {
			for(int i = 0; i < layersNum; i ++) {
				structurePolys[i] = new PolyDefault();
				structurePolys[i].add( minx, miny );
				structurePolys[i].add( minx, maxy );
				structurePolys[i].add( maxx, maxy );
				structurePolys[i].add( maxx, miny );
			}
		}
		area = AABB.createFromEdges( minx, miny, maxx, maxy, 0 );
	}
	
	public Poly [] getPoly() 
	{
		return structurePolys;
	}
	
	@Override
	public boolean add(Poly poly) {
		
		try {
		if(isFull())
			return false;
		
		Poly temp = poly.intersection( borderPoly );
		if(temp.isEmpty())
			return false;
		
		Poly carry = temp;
		int idx;
		for(idx = 0; idx < structurePolys.length; idx ++) {
			if(structurePolys[idx] == null) {
				structurePolys[idx] = temp;
				break;
			}
				
			temp = structurePolys[idx].intersection( temp );
			structurePolys[idx] = structurePolys[idx].union( carry );
			carry = temp;
		}
		
		if(!isFull && idx == structurePolys.length-1)
			isFull = structurePolys[idx].xor( borderPoly ).isEmpty();
		
		return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		//     ^ clip to tile             ^ add current structure
	}
	
	@Override
	public boolean substract(Poly poly) {
		
		try {
		if(isEmpty())
			return false;
		
		Poly temp = poly.intersection( borderPoly );
		if(temp.isEmpty())
			return false;
		
		temp = borderPoly.xor( temp );
		
//		if(temp.isEmpty()) {
//			return;
//		}
		
		for(int idx = 0; idx < structurePolys.length; idx ++) {
			if(structurePolys[idx] == null) {
				break;
			}
			
			structurePolys[idx] = structurePolys[idx].intersection( temp );
			if(structurePolys[idx].isEmpty())
				structurePolys[idx] = null;
		}
		
		if(isFull)
			isFull = structurePolys[0].xor( borderPoly ).isEmpty();
	
		return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean isEmpty()
	{
		return structurePolys[0] == null;
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
	public boolean isAlive()
	{
		
		return !isEmpty();
	}

/*	@Override
	public void markDead()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isIndexed()
	{
		// TODO Auto-generated method stub
		return false;
	}*/


	@Override
	public boolean isFull()
	{
		return isFull;
	}

	@Override
	public final double getMaxX() { return maxx; }
	@Override
	public final double getMaxY() { return maxy; }
	@Override
	public final double getMinX() { return minx; }
	@Override
	public final double getMinY() { return miny; }
	
}
