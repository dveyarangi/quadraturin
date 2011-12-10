package yarangi.graphics.quadraturin.objects.behaviors;

import yarangi.automata.FSM;
import yarangi.automata.ICondition;
import yarangi.graphics.quadraturin.objects.Behavior;

/**
 * Behavior that aggregates state machine. The states are to implement {@link #IBehaviorState} amd are linked 
 * to machine using {@link #link(IBehaviorState, ICondition, IBehaviorState)} link... err, method.
 * @author dveyarangi
 *
 * @param <K>
 */
public class FSMBehavior <K> implements Behavior <K> 
{
	/**
	 * Finite state machine.
	 */
	private FSM <K, IBehaviorState<K>> fsm;
	
	/** 
	 * Current state.
	 */
	private IBehaviorState <K> currState;
	
	public FSMBehavior(IBehaviorState <K> initState)
	{
		fsm = new FSM<K, IBehaviorState<K>>(initState);
		
		currState = initState;
	}
	
	/**
	 * Attaches state transition condition to state with specified id
	 * @param stateId
	 * @param condition
	 */
    public void link(int stateId, IBehaviorCondition <K> condition)
    {
    	fsm.link(stateId, condition);
    }
	/**
	 * Attaches state transition condition to state with specified id
	 * @param stateId
	 * @param condition
	 */
    public void link(IBehaviorState <K> state, IBehaviorCondition <K> condition)
    {
    	fsm.link(state.getId(), condition);
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public boolean behave(double time, K entity, boolean isVisible) 
	{
		double remainingTime = time;
		// changing state if behavior did not use all frame time:
		// otherwise the same 
		while((remainingTime = currState.behave(remainingTime, entity)) > 0)
			currState = fsm.nextState(entity);
		
		if(remainingTime == 0)
			currState = fsm.nextState(entity);
			
		return true; // TODO: should make true entity change indication 
	}


}
