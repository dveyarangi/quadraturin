package yarangi.graphics.quadraturin.terrain;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.objects.IBehavior;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.ILook;
import yarangi.graphics.quadraturin.objects.ISensor;
import yarangi.physics.Body;
import yarangi.physics.IPhysicalObject;
import yarangi.spatial.Area;

import com.seisw.util.geom.Poly;
import com.seisw.util.geom.PolyDefault;

public class MultilayerTilePoly implements IEntity, IPhysicalObject, ITilePoly
{
	/**
	 * Specified the borders of this tile
	 */
	private final Poly borderPoly;
	
	/**
	 * Specifies real structure of this tile:
	 */
	private final Poly [] structurePolys;
	
	
	
	private boolean isFull = false;
	
	public MultilayerTilePoly(double minx, double miny, double maxx, double maxy, int layersNum)
	{
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ILook getLook()
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

	@Override
	public void init(GL gl, IRenderingContext context)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GL gl, IRenderingContext context)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy(GL gl, IRenderingContext context)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBehavior(IBehavior<?> behavior)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public IBehavior<?> getBehavior()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISensor getSensor()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean behave(double time, boolean b)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getGroupId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isFull()
	{
		return isFull;
	}
	
}
