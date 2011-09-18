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
	
    public void link(IBehaviorState <K> sourceState, IBehaviorCondition <K> condition)
    {
    	fsm.link(sourceState, condition);
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public boolean behave(double time, K entity, boolean isVisible) 
	{
		while(currState.behave(time, entity, isVisible)) {/* loopy */}
		
		currState = fsm.nextState(entity);
		return true;
	}


}
