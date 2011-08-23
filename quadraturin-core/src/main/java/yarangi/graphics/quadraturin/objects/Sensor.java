package yarangi.graphics.quadraturin.objects;

import java.util.HashSet;
import java.util.Set;

import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialFilter;

/**
 * Sensor aspect of {@link WorldEntity}
 * 
 * @author dveyarangi
 *
 */
public class Sensor implements ISensor <IWorldEntity>
{
	private Set <IWorldEntity> entities;
	
	private double radius;
	private double radiusSquare;
	
	private ISpatialFilter <IWorldEntity>filter;

	private double lastSensingTime;

	private double interval;

	public Sensor(double radius, double interval, ISpatialFilter <IWorldEntity>filter )
	{
		this.radiusSquare = radius * radius;
		this.radius = radius;
		this.filter = filter;
		this.interval = interval;
		clearSensor();
	}
	
	@Override
	public Set <IWorldEntity> getEntities()
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
	
	public ISpatialFilter <IWorldEntity> getFilter() { return filter; }
 
	@Override
	public void clearSensor() { this.entities = new HashSet <IWorldEntity> (); }
	
	@Override
	public boolean objectFound(IAreaChunk chunk, IWorldEntity object) 
	{
		if(filter == null || filter.accept(object))
			entities.add(object);
		
		return false;
	}

	@Override
	public boolean isSensingNeeded(double time)
	{
		boolean needed = time - lastSensingTime > interval;
		if(needed)
			lastSensingTime = time;
			
		return needed;
	}
	
	
}
