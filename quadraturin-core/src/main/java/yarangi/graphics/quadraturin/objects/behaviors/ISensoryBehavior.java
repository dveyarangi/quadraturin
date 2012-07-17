package yarangi.graphics.quadraturin.objects.behaviors;

import java.util.List;

import yarangi.graphics.quadraturin.objects.IBehavior;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.ISensor;
import yarangi.graphics.quadraturin.objects.Sensor;
import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialFilter;
import yarangi.spatial.ISpatialSensor;

public class ISensoryBehavior <K extends IEntity> extends Sensor implements IBehavior <K>
{


	public ISensoryBehavior(double radius, double interval, ISpatialFilter<IEntity> filter, boolean senseTerrain)
	{
		super( radius, interval, filter, senseTerrain );
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean behave(double time, K entity, boolean isVisible) {return false;}

	@Override
	public void clear() {}

	@Override
	public boolean objectFound(IAreaChunk tile, IEntity object)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<IEntity> getEntities()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getRadius()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isSensingNeeded(double time)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSenseTerrain()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
}
