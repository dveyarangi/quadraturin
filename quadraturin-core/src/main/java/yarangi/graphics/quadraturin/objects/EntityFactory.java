package yarangi.graphics.quadraturin.objects;

import yarangi.graphics.quadraturin.simulations.Body;
import yarangi.spatial.Area;

public class EntityFactory 
{
	
	public static <K> IVeilEntity createEntity(Look <K> look, Behavior <K> behavior, Area area, Body body, ISensor sensor)
	{
		Entity entity = new Entity();
		entity.setLook(look);
		entity.setBehavior(behavior);
		entity.setArea(area);
		entity.setBody(body);
		entity.setSensor(sensor);
		return entity;
	}

}
