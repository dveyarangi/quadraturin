package yarangi.intervals;

import java.util.HashSet;
import java.util.Set;

import yarangi.math.Vector2D;

class UnitNode {
	protected Set <UnitInterval> intervals = new HashSet <UnitInterval> ();
	
	protected Vector2D center;
	
	UnitNode (Vector2D center)
	{
		 this.center = center;
	}
	
	protected Vector2D getCenter() 
	{
		return center; 
	}
	
	protected void add(UnitInterval interval)
	{
		intervals.add(interval);
	}
	protected void remove(UnitInterval interval)
	{
		intervals.remove(interval);
	}
}
