package yar.quadraturin;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import yar.quadraturin.debug.Debug;
import yar.quadraturin.objects.ILook;
import yar.quadraturin.objects.IVisible;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

/**
 * Aggregate of {@link IVisible} entities, providing means to add, remove and render them.
 * @author dveyarangi
 *
 */
public class LookManager
{
	
//	private final List <IVisible> entities = new LinkedList <IVisible> ();
	private final Multimap <ILook<?>, IVisible> looks = LinkedListMultimap.<ILook<?>, IVisible>create();
	
	private final Set <String> lookClasses = new HashSet <String> ();
	
	/**
	 * Queue of entities waiting to be initialized.
	 */
	private final Queue <ILook<?>> bornLooks = new LinkedList<ILook<?>> ();

	/**
	 * Queue of dead entities to be cleaned up.
	 */
	private final Queue <ILook<?>> deadLooks = new LinkedList <ILook<?>> ();
	
	/**
	 * Displays the entirety of entities in this scene for one scene animation frame.
	 * Also handles the decomposition of newly created and oldly dead entities.
	 * @param gl
	 * @param time scene frame time
	 */
	protected void renderEntities(IRenderingContext ctx)
	{
	    
		// injecting new entities
		while(!bornLooks.isEmpty())
		{
			ILook<?> born = bornLooks.poll();
			born.init( ctx );
		}
		
		while(!deadLooks.isEmpty())
		{
			ILook<?> dead = deadLooks.poll();
			dead.destroy( ctx );
		}
	
		IVeil veil;
		
		// TODO: do the viewport clipping already, you lazy me!
//		ISpatialSensor <SceneEntity> clippingSensor = new ClippingSensor(gl, time, context);
//		getEntityIndex().query(clippingSensor, new AABB(0, 0, Math.max(viewPoint.getPortWidth(), viewPoint.getPortHeight()), 0));
		
		for(ILook<?> look : looks.keySet())
		{
			// TODO: sort by veil, then weave and tear once
			veil = look.getVeil();
			
			if(veil != null)
				veil.weave( ctx );
			
			for(IVisible entity : looks.get( look )) 
				entity.render( ctx );
			
			if(veil != null)
				veil.tear( ctx );

			if(Debug.ON)
			for(IVisible entity : looks.get( look ))
				assert Debug.renderEntityOverlay(entity, ctx);

		}

	}

	
	/**
	 * Adds visible entity to rendering queue.
	 * @param entity
	 */
	public void addVisible(IVisible entity)
	{
		addVisible( entity, entity.getLook(), looks );
	}

	
	private void addVisible(IVisible entity, ILook look, Multimap queue) {
		
		lookClasses.add(look.getClass().toString());
		
		Collection <IVisible> entities = queue.get( look );
		
		// newborn visibles will be added next time rendering context becomes available: 
		if(entities.size() == 0)
			bornLooks.add(look);

		queue.put( look, entity );
		
		entities.add( entity );
		
	}
	

	/** 
	 * Removes an overlay from rendering queue
	 * Entities added to scene and implementing IVisible will be automatically removed.
	 *   
	 * @param entity
	 */
	public void removeVisible(IVisible entity)
	{
		Collection <IVisible> entities = looks.get( entity.getLook() );
		if(entities.size() == 1) {
			looks.removeAll( entity.getLook() );
			deadLooks.add( entity.getLook() );
		}
		else
			looks.remove( entity.getLook(), entity );
		
		entities.remove( entity );
		
	}
	

	/**
	 * Hints at entity associated with specified look.
	 * If look is used by multiple entities, any one of them will be returned.
	 * @return null, in case no entity found.
	 */
	@SuppressWarnings("unchecked")
	public <K> K getAssociatedEntity(ILook <K> look)
	{
		Collection <IVisible> associatedEntities = looks.get( look );
		if(associatedEntities == null || associatedEntities.isEmpty()) 
			return null;
		
		return (K)associatedEntities.iterator().next();
	}

}
