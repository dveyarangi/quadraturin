package yarangi.graphics.quadraturin.ui;

import yarangi.graphics.quadraturin.objects.ILayerObject;
import yarangi.graphics.quadraturin.objects.ILook;
import yarangi.spatial.AABB;

/**
 * TODO: this class is not yet implemented
 * @author dveyarangi
 */
public class Overlay implements ILayerObject
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

}
