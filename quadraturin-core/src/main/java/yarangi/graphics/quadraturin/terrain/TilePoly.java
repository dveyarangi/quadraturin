package yarangi.graphics.quadraturin.terrain;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.objects.Behavior;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.ISensor;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.physics.Body;
import yarangi.physics.IPhysicalObject;
import yarangi.spatial.Area;

import com.seisw.util.geom.Poly;
import com.seisw.util.geom.PolyDefault;

public class TilePoly implements IEntity, IPhysicalObject 
{
	/**
	 * Specified the borders of this tile
	 */
	private Poly borderPoly;
	
	/**
	 * Specifies real structure of this tile:
	 */
	private Poly structurePoly;
	
	public TilePoly(double minx, double miny, double maxx, double maxy)
	{
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
	}
	
	public Poly getPoly() 
	{
		return structurePoly;
	}
	
	public void add(Poly poly) {
		
		Poly temp = poly.intersection( borderPoly );
		if(temp.isEmpty())
			return;
		structurePoly = structurePoly == null ? temp : structurePoly.union( temp );
		
		//     ^ clip to tile             ^ add current structure
	}
	
	public void substract(Poly poly) {
		
		Poly temp = poly.intersection( borderPoly );
		if(temp.isEmpty())
			return;
		
		temp = borderPoly.xor( temp );
		
//		if(temp.isEmpty()) {
//			return;
//		}
		
		structurePoly = structurePoly == null ? null : structurePoly.intersection( temp );
		if(structurePoly == null)
			return;
		if(structurePoly.isEmpty() || structurePoly.getNumPoints() < 2)
			structurePoly = null;
	}

	public boolean isEmpty()
	{
		return structurePoly == null;
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
	public Look getLook()
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
	public void render(GL gl, double time, IRenderingContext context)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy(GL gl, IRenderingContext context)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBehavior(Behavior<?> behavior)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public Behavior<?> getBehavior()
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
	
}
