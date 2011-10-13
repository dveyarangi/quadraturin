package yarangi.graphics.quadraturin.objects.behaviors;

import yarangi.automata.IState;

public interface IBehaviorState <K> extends IState <K>
{
	/**
	 * Performs the behavior for this state.
	 * State is changed if behavior did not use all frame time;
	 * otherwise the same behavior will be executed the next frame.
	 * 
	 * Note: If the state alwsys return zero value it will never be left.
	 * 
	 * @param time
	 * @param entity
	 * @param isVisible
	 * @return amount of time remaining
	 */
	public double behave(double time, K entity, boolean isVisible);
}