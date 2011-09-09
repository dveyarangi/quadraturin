package yarangi.graphics.quadraturin.objects;

import java.util.HashSet;
import java.util.Set;

import yarangi.spatial.IAreaChunk;

/**
 * Sensor aspect of {@link Entity}
 * 
 * @author dveyarangi
 *
 */
public class DummySensor implements ISensor <IEntity>
{
	private Set <IEntity> entities = new HashSet <IEntity> ();
	
	public DummySensor() { }
	
	public Set <IEntity> getEntities() { return entities; }

	public double getSensorRadiusSquare() { return 0; }
	
	public double getRadius() { return 0; }
 	
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
		// TODO Auto-generated method stub
		return false;
	}
}
