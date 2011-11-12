package yarangi.graphics.quadraturin.objects;

import java.util.HashSet;
import java.util.Set;

import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialFilter;

/**
 * Sensor aspect of {@link Entity}
 * 
 * @author dveyarangi
 *
 */
public class Sensor implements ISensor <IEntity>
{
	private Set <IEntity> entities;
	
	private double radius;
	
	private ISpatialFilter <IEntity> filter;
	
	private boolean senseTerrain;

	private double lastSensingTime;

	private double interval;

	public Sensor(double radius, double interval, ISpatialFilter <IEntity>filter, boolean senseTerrain )
	{
		this.radius = radius;
		this.filter = filter;
		this.interval = interval;
		clear();
	}
	
	@Override
	public Set <IEntity> getEntities()
	{
		return entities;
	}

	@Override
	public double getRadius() { return radius; }
	
	public ISpatialFilter <IEntity> getFilter() { return filter; }
 
	@Override
	public void clear() { this.entities = new HashSet <IEntity> (); }
	
	@Override
	public boolean objectFound(IAreaChunk chunk, IEntity object) 
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

	@Override
	public boolean isSenseTerrain() { return senseTerrain; }
	
	
}
