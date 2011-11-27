package yarangi.graphics.quadraturin.ui;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.objects.ILayerObject;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.spatial.Area;

/**
 * TODO: this class is not yet implemented
 * @author dveyarangi
 */
public class Overlay implements ILayerObject
{
	/**
	 * Overlay look
	 */
	private Look <?> look;
	
	private Panel parent;
	
	
	public Overlay(Panel parent) 
	{
		this.parent = parent;
	}
	
	/** 
	 * Sets overlay's look.
	 * @param look
	 */
	public void setLook(Look <?> look) { this.look = look; }
	
	/**
	 * How the object looks.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public final Look getLook() { return look; }

	@Override
	public Area getArea()
	{
		return parent.getAABB();
	}

	@Override
	public boolean isAlive()
	{
		return false;
	}

	@Override
	public void markDead()
	{
	}

	@Override
	public void render(GL gl, double time, IRenderingContext context)
	{
		this.getLook().render( gl, time, this, context );
	}

	@Override
	public void init(GL gl, IRenderingContext context)
	{
		this.getLook().init( gl, this, context );
	}

	@Override
	public void destroy(GL gl, IRenderingContext context)
	{
		this.getLook().destroy( gl, this, context );
	}

}
