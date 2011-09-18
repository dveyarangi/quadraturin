package yarangi.automata;

import java.util.HashMap;
import java.util.Map;

/**
 * Finite state machine wrapper class. 
 * Transition between states is defined by {@link ICondition} implementations.
 *  
 * @author dveyarangi
 * 
 * @param <K> data type, handled by states of this machine
 * @param <S> state type
 */
public class FSM <K, S extends IState <K>>
{
	/** 
	 * initial state 
	 */
	private S initState;
	
	/**
	 * current state.
	 */
	private S currState;
	
	/**
	 * state transition mapping
	 */
	private Map <S, ICondition<K, S>> stateMap;
	
	/**
	 * Creates a new state with specified initial state.
	 * @param initState
	 */
	public FSM(S initState)
	{
		currState = this.initState = initState;
		
		stateMap = new HashMap <S, ICondition<K, S>> ();
	}
	
	/**
	 * @return initial state
	 */
	public S getInitState()
	{
		return initState;
	}
	
	/**
	 * Links a {@link IState} to a {@link ICondition}.
	 * @param sourceState
	 * @param condition
	 */
	public void link(S sourceState, ICondition <K, S> condition)
	{
		if(stateMap.containsKey(sourceState))
			throw new IllegalArgumentException("State " + sourceState + " already contains successor with condition " + condition);
		
		stateMap.put(sourceState, condition);
	}
	
	/**
	 * Moves the machine to next state.
	 * @param entity
	 * @return
	 */
	public S nextState(K entity)
	{
		S nextState = getNextState(entity, currState);
//		if(nextState == null)
//			throw new IllegalStateException("State " + currState + " ")
		return nextState;
	}
	/**
	 * Retrieves next state without transiting to it.
	 * @param entity
	 * @param currState
	 * @return
	 */
	private S getNextState(K entity, S currState)
	{
		return stateMap.get(currState).nextState(entity);
	}
	
}
