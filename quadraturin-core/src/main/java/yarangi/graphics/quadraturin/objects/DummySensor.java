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
	
	public DummySensor() { }
	
	public Set <IWorldEntity> getEntities() { return entities; }

	public double getSensorRadiusSquare() { return 0; }
	
	public double getRadius() { return 0; }
 	
	@Override
	public boolean objectFound(IAreaChunk chunk, IWorldEntity object) 
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
