package yarangi.graphics.quadraturin.objects;

import yarangi.physics.Body;
import yarangi.spatial.AABB;

public class EntityFactory 
{
	
	public static <K> ILayerObject createEntity(ILook <K> look, IBehavior <K> behavior, AABB area, Body body, ISensor sensor)
	{
		Entity entity = new Entity();
		entity.setLook(look);
		entity.setBehavior(behavior);
		entity.setArea(area);
		entity.setBody(body);
		entity.setEntitySensor(sensor);
		return entity;
	}

}
