package yarangi.graphics.quadraturin.ui;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.objects.ILayerObject;
import yarangi.graphics.quadraturin.objects.ILook;
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
	private ILook <?> look;
	
	private Panel parent;
	
	/**
	 * Defines either the element is pickable by controller
	 */
	private boolean isPickable;
	
	
	public Overlay(Panel parent, boolean isPickable) 
	{
		this.parent = parent;
		
		this.isPickable = isPickable;
	}
	
	/** 
	 * Sets overlay's look.
	 * @param look
	 */
	public void setLook(ILook <?> look) { this.look = look; }
	
	/**
	 * How the object looks.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public final ILook getLook() { return look; }

	@Override
	public Area getArea() {
		// parent panel defines the pickable area:
		return parent.getAABB();
	}

	@Override
	public boolean isAlive() { return false; }

	@Override
	public void markDead() { /* UI elements are static */ }
	
	@Override
	public boolean isIndexed() { return isPickable; }

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
