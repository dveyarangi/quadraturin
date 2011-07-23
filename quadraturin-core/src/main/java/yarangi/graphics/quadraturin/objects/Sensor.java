package yarangi.graphics.quadraturin.objects;

import java.util.HashSet;
import java.util.Set;

import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialFilter;
import yarangi.spatial.ISpatialSensor;

/**
 * Sensor aspect of {@link SceneEntity}
 * 
 * @author dveyarangi
 *
 */
public class Sensor implements ISpatialSensor <SceneEntity>
{
	private Set <SceneEntity> entities;
	
	private double radius;
	private double radiusSquare;
	
	private ISpatialFilter <SceneEntity>filter;

	
	public Sensor(double radius, ISpatialFilter <SceneEntity>filter )
	{
		this.radiusSquare = radius * radius;
		this.radius = radius;
		this.filter = filter;
		clearSensor();
	}
	
	public void setEntities(Set <SceneEntity> entities)
	{
		this.entities = entities;
	}
	
	public Set <SceneEntity> getEntities()
	{
		return entities;
	}

	public double getSensorRadiusSquare()
	{
		return radiusSquare;
	}
	
	public double getRadius() { return radius; }
	
	public ISpatialFilter <SceneEntity> getFilter() { return filter; }
 
	public void clearSensor() { this.entities = new HashSet <SceneEntity> (); }
	
	@Override
	public boolean objectFound(IAreaChunk chunk, SceneEntity object) 
	{
		if(filter == null || filter.accept(object))
		{
			entities.add(object);
			return true;
		}
		return false;
	}
}
