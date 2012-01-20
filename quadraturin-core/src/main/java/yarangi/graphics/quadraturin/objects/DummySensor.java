package yarangi.graphics.quadraturin.objects;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import yarangi.spatial.IAreaChunk;

/**
 * Sensor aspect of {@link Entity}
 * 
 * @author dveyarangi
 *
 */
public class DummySensor implements ISensor <IEntity>
{
	private List <IEntity> entities = new LinkedList <IEntity> ();
	private double radius;
	public DummySensor() { }
	
	public DummySensor(int size)
	{
		radius = size;
	}

	public List <IEntity> getEntities() { return entities; }

	public double getSensorRadiusSquare() { return 0; }
	
	public double getRadius() { return radius; }
 	
	@Override
	public boolean objectFound(IAreaChunk chunk, IEntity object) 
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

	@Override
	public boolean isSenseTerrain()
	{
		return false;
	}
}
