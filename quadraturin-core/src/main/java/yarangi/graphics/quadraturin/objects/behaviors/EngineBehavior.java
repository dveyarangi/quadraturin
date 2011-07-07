package yarangi.graphics.quadraturin.objects.behaviors;

import yarangi.graphics.quadraturin.objects.NewtonialSceneEntity;

public class EngineBehavior <K extends NewtonialSceneEntity> implements IBehaviorState<K> 
{
	private double enginePower;
	
	public EngineBehavior(double enginePower) 
	{
		this.enginePower = enginePower;
	}

	@Override
	public boolean behave(double time, K entity, boolean isVisible) 
	{
		
		double a = entity.getArea().getOrientation();
		
		entity.setForce(enginePower*time*Math.cos(a), enginePower*time*Math.sin(a));
		return true;
	}


}
