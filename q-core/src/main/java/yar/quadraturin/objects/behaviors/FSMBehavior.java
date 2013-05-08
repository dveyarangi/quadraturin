package yar.quadraturin.objects.behaviors;

import yar.quadraturin.objects.IBehavior;
import yarangi.automata.FSM;
import yarangi.automata.ICondition;

/**
 * Behavior that aggregates state machine. The states are to implement {@link IBehaviorState} and are linked 
 * to machine using {@link #link(IBehaviorState, IBehaviorCondition)} link... err, method.
 * 
 * Allows states to take less or more that one frame; this is specified by {@link IBehaviorState#behave(double, Object)} return value.
 * @author dveyarangi
 *
 * @param <K>
 */
public class FSMBehavior <K> implements IBehavior <K> 
{
	/**
	 * Finite state machine.
	 */
	private final FSM <K, IBehaviorState<K>> fsm;
	
	/** 
	 * Current state.
	 */
	private IBehaviorState <K> currState;
	
	/**
	 * Creates FSM behavior with specified initial state.
	 * @param initState
	 */
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

	/**
	 * Retrieves current behavior state
	 * @return
	 */
	public IBehaviorState <K> getCurrentState() 
	{
		return currState;
	}
}
