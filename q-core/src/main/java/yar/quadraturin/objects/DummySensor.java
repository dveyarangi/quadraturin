package yar.quadraturin.objects;

import java.util.LinkedList;
import java.util.List;

/**
 * Sensor aspect of {@link Entity}
 * 
 * @author dveyarangi
 *
 */
public class DummySensor implements ISensor <IEntity>
{
	private final List <IEntity> entities = new LinkedList <IEntity> ();
	private double radius;
	public DummySensor() { }
	
	public DummySensor(int size)
	{
		radius = size;
	}

	@Override
	public List <IEntity> getEntities() { return entities; }

	public double getSensorRadiusSquare() { return 0; }
	
	@Override
	public double getRadius() { return radius; }
 	
	@Override
	public boolean objectFound(IEntity object) 
	{
		return true;
	}

	@Override
	public boolean isSensingNeeded(double time)
	{
		return false;
	}

	@Override
	public void clear() { }

}
