package yarangi.graphics.quadraturin.terrain;

import yarangi.graphics.quadraturin.objects.IBeing;
import yarangi.physics.Body;
import yarangi.spatial.AABB;
import yarangi.spatial.Area;

import com.seisw.util.geom.Poly;
import com.seisw.util.geom.PolyDefault;

public class MultilayerTilePoly implements IBeing, ITilePoly
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
	
	public MultilayerTilePoly(double minx, double miny, double maxx, double maxy, int layersNum)
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
		
/*		structurePoly = new PolyDefault();
		structurePoly.add( minx, miny );
		structurePoly.add( minx, maxy );
		structurePoly.add( maxx, maxy );
		structurePoly.add( maxx, miny );*/
		
		area = AABB.createFromEdges( minx, miny, maxx, maxy, 0 );
	}
	
	public Poly [] getPoly() 
	{
		return structurePolys;
	}
	
	@Override
	public void add(Poly poly) {
		
		Poly temp = poly.intersection( borderPoly );
		if(temp.isEmpty())
			return;
		Poly carry = temp;
		for(int idx = 0; idx < structurePolys.length; idx ++) {
			if(structurePolys[idx] == null) {
				structurePolys[idx] = temp;
				break;
			}
				
			temp = structurePolys[idx].intersection( temp );
			structurePolys[idx] = structurePolys[idx].union( carry );
			carry = temp;
		}
		if(!isFull)
			isFull = structurePolys[0].xor( borderPoly ).isEmpty();
		//     ^ clip to tile             ^ add current structure
	}
	
	@Override
	public void substract(Poly poly) {
		
		Poly temp = poly.intersection( borderPoly );
		if(temp.isEmpty())
			return;
		
		temp = borderPoly.xor( temp );
		
//		if(temp.isEmpty()) {
//			return;
//		}
		
		for(int idx = 0; idx < structurePolys.length; idx ++) {
			if(structurePolys[idx] == null) {
				break;
			}
			
			structurePolys[idx] = structurePolys[idx].intersection( temp );
			if(structurePolys[idx].isEmpty() || structurePolys[idx].getNumPoints() < 2)
				structurePolys[idx] = null;
		}
		
		if(isFull)
			isFull = structurePolys[0].xor( borderPoly ).isEmpty();
	
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
