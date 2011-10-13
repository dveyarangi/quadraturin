package yarangi.automata;

import gnu.trove.map.hash.TIntObjectHashMap;

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
	private TIntObjectHashMap <ICondition<K, S>> stateMap;
	
	/**
	 * Creates a new state with specified initial state.
	 * @param initState
	 */
	public FSM(S initState)
	{
		currState = this.initState = initState;
		
		stateMap = new TIntObjectHashMap <ICondition<K, S>> ();
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
	 * @param id source state id
	 * @param condition
	 */
	public void link(int id, ICondition <K, S> condition)
	{
		if(stateMap.contains(id))
			throw new IllegalArgumentException("State with id" + id + " already contains successor with condition " + condition);
		
		stateMap.put(id, condition);
	}
	
	/**
	 * Moves the machine to next state.
	 * @param entity
	 * @return
	 */
	public S nextState(K entity)
	{
		S nextState = getNextState(entity, currState);
		this.currState = nextState;
//		if(nextState == null)
//			throw new IllegalStateException("State " + currState + " ")
		return currState;
	}
	/**
	 * Retrieves next state without transiting to it.
	 * @param entity
	 * @param currState
	 * @return
	 */
	private S getNextState(K entity, S currState)
	{
		return stateMap.get(currState.getId()).nextState( entity );
	}
	
}
