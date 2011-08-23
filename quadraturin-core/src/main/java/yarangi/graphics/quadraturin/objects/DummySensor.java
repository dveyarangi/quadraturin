package yarangi.graphics.quadraturin.objects;

import java.util.HashSet;
import java.util.Set;

import yarangi.spatial.IAreaChunk;

/**
 * Sensor aspect of {@link WorldEntity}
 * 
 * @author dveyarangi
 *
 */
public class DummySensor implements ISensor <IWorldEntity>
{
	private Set <IWorldEntity> entities = new HashSet <IWorldEntity> ();
	
	private double radius;
	private double radiusSquare;
	
	public DummySensor(double radius)
	{
		this.radiusSquare = radius * radius;
		this.radius = radius;
		clearSensor();
	}
	
	public Set <IWorldEntity> getEntities()
	{
		return entities;
	}

	public double getSensorRadiusSquare()
	{
		return radiusSquare;
	}
	
	public double getRadius() { return radius; }
 
	public void clearSensor() { this.entities = new HashSet <IWorldEntity> (); }
	
	@Override
	public boolean objectFound(IAreaChunk chunk, IWorldEntity object) 
	{
		return true;
	}

	@Override
	public boolean isSensingNeeded(double time)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
