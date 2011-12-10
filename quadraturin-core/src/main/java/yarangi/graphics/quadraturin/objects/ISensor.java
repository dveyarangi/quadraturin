package yarangi.graphics.quadraturin.objects;

import java.util.Set;

import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialSensor;

public interface ISensor <K> extends ISpatialSensor <IAreaChunk, K>
{
	/**
	 * Sensed entities.
	 * @return
	 */
	public abstract Set<K> getEntities();

	public abstract double getRadius();
	
	public boolean isSensingNeeded(double time);
	
	public boolean isSenseTerrain();
}