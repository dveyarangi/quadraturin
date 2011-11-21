package yarangi.fragments;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import yarangi.intervals.AngleInterval;

public class CircleSegmentList
{
	private List <AngleInterval> intervals = new LinkedList <AngleInterval> ();
	
	/**
	 * Special holder for interval around pi/2 (-pi/2)
	 */
	private AngleInterval closureInterval;
	
	private double closureMax, closureMin;
	
	
	/**
	 * Creates a new cyclic intervals list, where values below closureMin and above
	 * closureMax are merged
	 * @param closureMin
	 * @param closureMax
	 */
	public CircleSegmentList(double closureMin, double closureMax)
	{
		if(closureMin > closureMax)
			throw new IllegalArgumentException("Closure parameters are invalid.");
		
		this.closureMin = closureMin;
		this.closureMax = closureMax;
		
		closureInterval = new AngleInterval(closureMax, closureMin);
	}

	/**
	 * 
	 * 
	 * TODO: drunk porcupine could do better
	 * @param newInterval if inverse, it considered a merging interval
	 */
	public void add(AngleInterval inew)
	{
		boolean inverse = inew.getMin() > inew.getMax();
		if(inverse && closureInterval == null)
		{
			closureInterval = inew;
		}
		
		ListIterator <AngleInterval> it = intervals.listIterator();
		AngleInterval imin = null;
		AngleInterval imax = null;
		AngleInterval modifiedInterval;

		boolean minFound = false;
		boolean maxFound = false;
		boolean removeInterval = true;
		
		AngleInterval inext = null;
		
		while(it.hasNext())
		{
			inext = it.next();

			if(inext.getMax() >= inew.getMin()) {
				minFound = true;
				if(inext.getMin() > inew.getMax())
					removeInterval = false;
				else
					imin = inext;
			}
			
			boolean replacementZone = inverse ? !(minFound ^ maxFound) : minFound ^ maxFound ; // false symmetry is false
			
			if(inext.getMax() >= inew.getMax()) {
				maxFound = true;
				if(inext.getMin() > inew.getMax() && !minFound)
					removeInterval = false; // we skipped over this
				else
					imax = inext;
			}
			
			if(replacementZone && removeInterval) {
				it.remove();
			}
			removeInterval = true;

			
		}
		boolean mergeWithIntverse = closureInterval != null && 
				(inew.getMin() <= closureInterval.getMax() ||
				 inew.getMax() >= closureInterval.getMin());
		
		double cmin, cmax;
		
		if(inverse || mergeWithIntverse)
		{
			cmin = inew.getMin() < closureInterval.getMin() && inew.getMin() > closureInterval.getMax() ? inew.getMin() : closureInterval.getMin();
			cmax = inew.getMax() > closureInterval.getMax() && inew.getMax() < closureInterval.getMin() ? inew.getMax() : closureInterval.getMax();
			if(cmin >= cmax)
				closureInterval = new AngleInterval(cmin, cmax);
		}

		else
			it.add(new AngleInterval(imin == null ? inew.getMin() : Math.min(imin.getMin(), inew.getMin()), 
									 imax == null ? inew.getMax() : Math.max(imax.getMax(), inew.getMax())));
	}
	
	public static void main(String ... args) 
	{
		CircleSegmentList list = new CircleSegmentList(-Math.PI/2, Math.PI/2);
		
		list.add(new AngleInterval(0.5, 1));
		list.add(new AngleInterval(1.1, 1.2));
		list.add(new AngleInterval(1.3, 1.5));
		
		list.add(new AngleInterval(0, 1.6));
		list.add(new AngleInterval( -1, -0.5));
//		list.add(new AngleInterval(2.2, 4.5));
		
		for(AngleInterval i : list.intervals)
			System.out.println(i);
		System.out.println(list.closureInterval);
	}

	public void clear()
	{
		intervals.clear();
		closureInterval = new AngleInterval(closureMax, closureMin);
	}

	public boolean covers(double value)
	{
		if(value >= closureInterval.getMin() || value <= closureInterval.getMax())
			return true;
		
		for(AngleInterval interval : intervals)
			if(value <= interval.getMax() && value >= interval.getMin())
				return true;
		
		return false;
	}

	public List <AngleInterval> getRegularList()
	{
		return intervals;
	}
}
