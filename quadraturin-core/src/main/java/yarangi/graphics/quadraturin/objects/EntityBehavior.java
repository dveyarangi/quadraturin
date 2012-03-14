package yarangi.graphics.quadraturin.objects;

import yarangi.graphics.quadraturin.objects.behaviors.IBehaviorState;
import yarangi.numbers.RandomUtil;

public abstract class EntityBehavior implements Behavior <IEntity>, IBehaviorState <IEntity>
{
	
	private int id = RandomUtil.getRandomInt( Integer.MAX_VALUE ); 

	@Override
	public boolean behave(double time, IEntity entity, boolean isVisible)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getId() { return id; }

	@Override
	public double behave(double time, IEntity entity)
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
