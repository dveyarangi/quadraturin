package yar.quadraturin.objects.behaviors;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import yar.quadraturin.objects.IBehavior;
import yar.quadraturin.objects.IEntity;

public class StagingBehavior implements IBehavior <IEntity>
{
	
	private List <IBehaviorState<IEntity>> stages = new LinkedList <IBehaviorState<IEntity>> ();
	
	private Iterator <IBehaviorState<IEntity>> iterator;
	
	private IBehaviorState<IEntity> currentStage;
	
	public StagingBehavior(IBehaviorState <IEntity> ... parts) {
		for(IBehaviorState <IEntity> stage : parts)
			stages.add( stage );
		
		iterator = stages.iterator();
		currentStage = iterator.next();
	}

	@Override
	public boolean behave(double time, IEntity entity, boolean isVisible)
	{
		
		double remaining = currentStage.behave( time, entity );
		if(remaining == 0) 
		{
			if(iterator.hasNext())
				currentStage = iterator.next();
			else
			{
				entity.markDead();
				return false;
			}
			
			return true;
		}
		
		return false;
	}

}
