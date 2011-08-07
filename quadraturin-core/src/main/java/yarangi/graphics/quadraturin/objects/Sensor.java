package yarangi.graphics.quadraturin.objects;

import java.util.HashSet;
import java.util.Set;

import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialFilter;

/**
 * Sensor aspect of {@link SceneEntity}
 * 
 * @author dveyarangi
 *
 */
public class Sensor implements ISensor
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
	
	@Override
	public Set <SceneEntity> getEntities()
	{
		return entities;
	}

	@Override
	public double getSensorRadiusSquare()
	{
		return radiusSquare;
	}
	
	@Override
	public double getRadius() { return radius; }
	
	public ISpatialFilter <SceneEntity> getFilter() { return filter; }
 
	@Override
	public void clearSensor() { this.entities = new HashSet <SceneEntity> (); }
	
	@Override
	public boolean objectFound(IAreaChunk chunk, SceneEntity object) 
	{
		if(filter == null || filter.accept(object))
			entities.add(object);
		
		return false;
	}
}
