package yarangi.automata;

/**
 * Defines transition to next state based on state entity
 * @author dveyarangi
 *
 * @param <K> state entity type.
 * @param <S> state type
 */
public interface ICondition <K, S extends IState <K>> 
{
	public S nextState(K entity);
}
