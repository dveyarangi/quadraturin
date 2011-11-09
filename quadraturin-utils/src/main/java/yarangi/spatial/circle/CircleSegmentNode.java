package yarangi.spatial.circle;

import java.util.HashSet;
import java.util.Set;

public class CircleSegmentNode {
	
	/**
	 * Center point for intervals in this node.
	 */
	private double center;
	
	/**
	 * List of intervals that contain the center point
	 */
	private Set <AngleInterval> intervals = new HashSet <AngleInterval> ();
	
	/**
	 * Node that contains intervals clockwise of the center point.
	 */
	private CircleSegmentNode cwNode;
	
	/**
	 * Node that contains intervals counter-clockwise of the center point.
	 */
	private CircleSegmentNode nodeCCW;
	
	/**
	 * TODO: carefully make use for faster search
	 */
	private double max, min;
	
	CircleSegmentNode(double min, double max)
	{
		this.min = min;
		this.max = max;
		this.center = (max - min) / 2;
	}
	
	/**
	 * Retrieve any interval that contains specified point.
	 * @param point
	 * @return
	 */
	public AngleInterval getAny(double point)
	{
		for(AngleInterval interval : intervals)
			if(interval.calcPosition(point) == 0)
				return interval;
					
		double position = point - center;
				
		if(position > 0 && nodeCCW != null)
			return nodeCCW.getAny(point);
		else if(position < 0 && cwNode != null)
			return cwNode.getAny(point);
		
		return null;
	}
	
	public void add(AngleInterval interval) 
	{
		double position = interval.calcPosition(center);
		if(position == 0) 
			intervals.add(interval);
		else if(position > 0)
		{
			if(nodeCCW == null)
				nodeCCW = new CircleSegmentNode(center, max);
			
			nodeCCW.add(interval);
		}
		else if(position < 0)
		{
			if(cwNode == null)
				cwNode = new CircleSegmentNode(min, center);
			
			nodeCCW.add(interval);
		}
	}
	
	public void remove(AngleInterval interval)
	{
		double position = interval.calcPosition(center);
		if(position == 0)
			intervals.remove(interval);
		else if(position > 0 && nodeCCW != null)
			nodeCCW.remove(interval);
		else if(position < 0 && cwNode != null)
			cwNode.remove(interval);
	}

}
