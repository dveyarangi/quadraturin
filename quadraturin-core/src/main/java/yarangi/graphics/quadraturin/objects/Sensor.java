package yarangi.graphics.quadraturin.objects;

import java.util.LinkedList;
import java.util.List;

import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialFilter;

/**
 * Sensor aspect of {@link Entity}.
 * Accumulates sensed entities into list; Override {@link #objectFound} if streamlining possible.
 * 
 * Filter may be used to reduce sensed entities list size.
 * 
 * Use non-positive sensing interval to sense on each animation turn ({@link WorldLayer#animate(double)}).
 * 
 * Scene invokes {@link #clear} each sensing iteration.
 * 
 * @author dveyarangi
 *
 */
public class Sensor implements ISensor <IEntity>
{
	/** 
	 * List of sensed entities 
	 */
	private List <IEntity> entities;
	
	/**
	 * Sensing radius
	 */
	private double radius;
	
	private ISpatialFilter <IEntity> filter;
	
	private boolean senseTerrain;

	private double lastSensingTime;

	private double interval;

	public Sensor(double radius, double interval, boolean senseTerrain )
	{
		this(radius, interval, null, senseTerrain);
	}

	public Sensor(double radius, double interval, ISpatialFilter <IEntity>filter, boolean senseTerrain )
	{
		this.radius = radius;
		this.filter = filter;
		this.interval = interval;
		this.senseTerrain = senseTerrain;
		clear();
	}
	
	@Override
	public List <IEntity> getEntities()
	{
		return entities;
	}

	@Override
	public double getRadius() { return radius; }
	
	public ISpatialFilter <IEntity> getFilter() { return filter; }
 
	@Override
	public void clear() { this.entities = new LinkedList <IEntity> (); }
	
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
