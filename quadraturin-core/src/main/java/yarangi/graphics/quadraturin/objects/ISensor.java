package yarangi.graphics.quadraturin.objects;

import java.util.Set;

import yarangi.spatial.ISpatialSensor;

public interface ISensor <K extends IVeilEntity> extends ISpatialSensor <K>
{
	/**
	 * Sensed entities.
	 * @return
	 */
	public abstract Set<K> getEntities();

	public abstract double getSensorRadiusSquare();

	public abstract double getRadius();

	public abstract void clearSensor();
	
	public boolean isSensingNeeded(double time);
}