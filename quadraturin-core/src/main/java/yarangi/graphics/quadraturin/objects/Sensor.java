package yarangi.graphics.quadraturin.objects;

import java.util.LinkedList;
import java.util.List;

import yarangi.graphics.quadraturin.WorldLayer;
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
public class Sensor implements ISensor <IBeing>
{
	/** 
	 * List of sensed entities 
	 */
	private List <IBeing> entities;
	
	/**
	 * Sensing radius
	 */
	private final double radius;
	
	private final ISpatialFilter <IBeing> filter;
	
	private final boolean senseTerrain;

	private double lastSensingTime;

	private final double interval;

	public Sensor(double radius, double interval, boolean senseTerrain )
	{
		this(radius, interval, null, senseTerrain);
	}

	public Sensor(double radius, double interval, ISpatialFilter <IBeing>filter, boolean senseTerrain )
	{
		this.radius = radius;
		this.filter = filter;
		this.interval = interval;
		this.senseTerrain = senseTerrain;
		clear();
	}
	
	@Override
	public List <IBeing> getEntities()
	{
		return entities;
	}

	@Override
	public double getRadius() { return radius; }
	
	public ISpatialFilter <IBeing> getFilter() { return filter; }
 
	@Override
	public void clear() { this.entities = new LinkedList <IBeing> (); }
	
	@Override
	public boolean objectFound(IBeing object) 
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
