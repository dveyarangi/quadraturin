package yarangi.graphics.quadraturin.objects;

import java.util.LinkedList;
import java.util.List;

import yarangi.graphics.quadraturin.WorldLayer;
import yarangi.spatial.ISpatialFilter;

/**
 * Sensor aspect of {@link Entity}.
 * Accumulates sensed entities into list; Override {@link #objectFound} if streamlining possible (to prevent double pass over sensed objects)
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
public class Sensor <O> implements ISensor <O>
{
	/** 
	 * List of sensed entities 
	 */
	private List <O> entities;
	
	/**
	 * Sensing radius
	 */
	private final double radius;
	
	/**
	 * Filters out irrelevant entities
	 */
	private final ISpatialFilter <O> filter;


	private double lastSensingTime;

	private final double interval;

	public Sensor(double radius, double interval)
	{
		this(radius, interval, null);
	}

	public Sensor(double radius, double interval, ISpatialFilter <O> filter )
	{
		this.radius = radius;
		this.filter = filter;
		this.interval = interval;
		clear();
	}
	
	@Override
	public List <O> getEntities()
	{
		return entities;
	}

	@Override
	public double getRadius() { return radius; }
	
	public ISpatialFilter <O> getFilter() { return filter; }
 
	@Override
	public void clear() { this.entities = new LinkedList <O> (); }
	
	@Override
	public boolean objectFound(O object) 
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
