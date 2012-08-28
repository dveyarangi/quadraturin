package yarangi.graphics.quadraturin.objects;

import java.util.List;

import yarangi.spatial.ISpatialSensor;

/**
 * should be merged with behavior to remove additional loop over sensed entities
 * see ISensoryBehavior
 * @author dveyarangi
 *
 * @param <K>
 */
public interface ISensor <K> extends ISpatialSensor <K>
{
	/**
	 * Sensed entities.
	 * @return
	 */
	public abstract List<K> getEntities();

	public abstract double getRadius();
	
	public boolean isSensingNeeded(double time);
 
}