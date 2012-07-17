package yarangi.graphics.quadraturin.objects;

import yarangi.graphics.quadraturin.objects.behaviors.IBehaviorState;
import yarangi.numbers.RandomUtil;

public abstract class EntityBehavior <E extends IEntity>implements IBehavior <E>, IBehaviorState <E>
{
	
	private int hashcode = RandomUtil.N( Integer.MAX_VALUE ); 

	@Override
	public boolean behave(double time, E entity, boolean isVisible)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() { return hashcode; }

	@Override
	public abstract double behave(double time, E entity);

}
