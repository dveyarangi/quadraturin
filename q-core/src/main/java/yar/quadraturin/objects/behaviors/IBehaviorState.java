package yar.quadraturin.objects.behaviors;

import yarangi.automata.IState;

public interface IBehaviorState <K> extends IState <K>
{
	public static final double CONTINUE = -1;
	public static final double TERMINATE = 0;
	
	
	/**
	 * Performs the behavior for this state.
	 * State is changed if behavior did not use all frame time;
	 * otherwise the same behavior will be executed the next frame.
	 * 
	 * Note: If the state always return negative value it will never be left.
	 * 
	 * @param time
	 * @param entity
	 * @param isVisible
	 * @return amount of time remaining, positive if behavior took less time than provided; negative if it needs more time, or zero
	 */
	public double behave(double time, K entity);
}