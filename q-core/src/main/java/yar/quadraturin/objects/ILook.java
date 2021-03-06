package yar.quadraturin.objects;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.IVeil;
import yar.quadraturin.Q2DController;

/**
 * Generic graphics interface for {@link IEntity}-s; implements entity view aspect.
 * 
 * Can be shared between different entities (in this case it should not have modifiable state).
 * 
 * @author Dve Yarangi
 */
public interface ILook <An>
{
	
	/**
	 * Initiate graphics (textures, meshes, etc.)
	 * If this look instance  is used for multiple entities, this method will be called ony one time,
	 * with the first entity passed as parameters 
	 * 
	 * @param gl
	 * @param entity
	 * @param context some global rendering properties
	 */
	public void init(IRenderingContext ctx);
	
	/**
	 * Renders this entity.
	 * @param gl
	 * @param entity entity to be rendered
	 * @param time scene time for animation
	 * @param context some global rendering properties
	 */
	public void render(An entity, IRenderingContext ctx);

	/**
	 * Invoked on entity deconstruction, should implement cleanup logic.
	 * @param gl
	 * @param entity
	 * @param context some global rendering properties
	 * 
	 */
	public void destroy(IRenderingContext ctx);

	/**
	 * Defines look visual priority (looks with priority closer to 0 will override looks below.)
	 * Ranges from {@link Q2DController#MIN_DEPTH_PRIORITY} to {@link Q2DController#MAX_DEPTH_PRIORITY}
	 * TODO: requires reworking (see DefaultRenderingContext DEPTH function setup).
	 * @return
	 */
	public float getPriority();
	
	/**
	 * @return true, if this look should be considered opaque for lighting calculations.
	 * TODO: this looks too specific and out of place
	 */
	public boolean isCastsShadow();

	/**
	 * Can be used to set special rendering environment for the look; ignored if null.
	 * @return
	 */
	public IVeil getVeil();

	/**
	 * Determines either the look should be rendered in entity coordinates (translated and rotated)
	 * @return
	 */
	public boolean isOriented();

}
