package yarangi.spatial.circle;

import java.util.HashSet;
import java.util.Set;

import yarangi.math.Angles;

public class CircleSegmentTree {

	/**
	 * List for angle intervals that include pi/2 point.
	 */
	private Set <AngleInterval> criticalIntervals = new HashSet<AngleInterval> ();
	
	private CircleSegmentNode centerNode;
	
	public CircleSegmentTree()
	{
		centerNode = new CircleSegmentNode(Angles.PI_div_2, -Angles.PI_div_2);
	}
	
	public void add(AngleInterval interval)
	{
		if(interval.getMin() > interval.getMax())
			criticalIntervals.add(interval);
		else
			centerNode.add(interval);
	}
	
	public void remove(AngleInterval interval)
	{
		if(interval.getMin() > interval.getMax())
			criticalIntervals.remove(interval);
		else
			centerNode.remove(interval);
	}
	
	public AngleInterval getAny(double angle)
	{
		for(AngleInterval interval : criticalIntervals)
			if((angle <= Angles.PI_div_2 && angle >= interval.getMin())
			 ||(angle >= -Angles.PI_div_2 && angle <= interval.getMax()))
				return interval;
				
		return centerNode.getAny(angle);
	}
}
