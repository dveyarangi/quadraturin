package yarangi.graphics.quadraturin.terrain;

import yarangi.graphics.quadraturin.Q;
import yarangi.physics.Body;
import yarangi.spatial.AABB;
import yarangi.spatial.Area;

import com.seisw.util.geom.Poly;
import com.seisw.util.geom.PolyDefault;

/**
 * Tile with multiple layers of polygons
 * 
 * @author dveyarangi
 *
 */
public class MultilayerTilePoly implements ITerrain, ITilePoly
{
	/**
	 * Specified the borders of this tile
	 */
	private final Poly borderPoly;
	
	private final float minx, miny, maxx, maxy;
	
	/**
	 * Specifies real structure of this tile:
	 */
	private final Poly [] structurePolys;
	
	private final AABB area;
	
	private boolean isFull = false;
	
	public MultilayerTilePoly(float minx, float miny, float maxx, float maxy, int layersNum, boolean fill)
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
	

	@Override
	public Poly getPoly()
	{
		return structurePolys[0];
	}
	
	public Poly [] getPolys() 
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

		}catch(Exception e) {
			Q.structure.warn( "TODO: Polygon operation failed, fix this damn thing", e ); // TODO
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
			
			
			boolean modified = !structurePolys[0].intersection( temp ).isEmpty();
			if(!modified)
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
			
	//		if(isFull)
	//			isFull = structurePolys[0].xor( borderPoly ).isEmpty();
		
			return true;
		}catch(Exception e) {
			Q.structure.warn( "TODO: Polygon operation failed, fix this damn thing", e );// TODO
			return false;
		}

	}

	@Override
	public boolean isFull()
	{
		return isFull;
	}

	@Override
	public final float getMaxX() { return maxx; }
	@Override
	public final float getMaxY() { return maxy; }
	@Override
	public final float getMinX() { return minx; }
	@Override
	public final float getMinY() { return miny; }

	@Override
	public boolean overlaps(AABB aabb)
	{
		if(isEmpty())
			return false;
		if(isFull())
			return true;
		
		// TODO: make and use structurePolys[0].intersects(AABB) boolean 
		return true;
	}
	

	@Override
	public boolean isEmpty()
	{
		return structurePolys[0] == null;
	}

	@Override
	public AABB getArea()
	{
		return area;
	}

	@Override
	public Body getBody()
	{
		return null;
	}

	@Override
	public boolean isAlive()
	{
		return !isEmpty();
	}

}
