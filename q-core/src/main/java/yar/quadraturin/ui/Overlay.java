package yar.quadraturin.ui;

import yar.quadraturin.GL2RenderingContext;
import yar.quadraturin.IRenderingContext;
import yar.quadraturin.objects.ILayerObject;
import yar.quadraturin.objects.ILook;
import yar.quadraturin.objects.IVisible;
import yarangi.spatial.AABB;

/**
 * Overlay
 * @author dveyarangi
 */
public class Overlay implements ILayerObject, IVisible
{
	/**
	 * Overlay look
	 */
	@SuppressWarnings("rawtypes")
	private ILook look;
	
	private final Panel parent;
	
	/**
	 * Defines either the element is pickable by controller
	 */
	private final boolean isPickable;
	
	
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
	@Override
	@SuppressWarnings("rawtypes")
	public final ILook getLook() { return look; }

	@Override
	public AABB getArea() {
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
	public void render(IRenderingContext ctx)
	{
		if(look.isOriented())
			GL2RenderingContext.useEntityCoordinates(ctx.gl(), getArea(), getLook());
		look.render( this, ctx );
		if(look.isOriented())
			GL2RenderingContext.useWorldCoordinates(ctx.gl());
	}

	public Panel getParent()
	{
		return parent;
	}
	
	

}
