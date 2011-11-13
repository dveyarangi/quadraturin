package yarangi.spatial.circle;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class CircleSegmentList
{
	private List <AngleInterval> intervals = new LinkedList <AngleInterval> ();
	
	/**
	 * Special holder for interval around pi/2 (-pi/2)
	 */
	private AngleInterval mergingInterval;
	
	public CircleSegmentList()
	{
	}

	/**
	 * TODO: drunk porcupine could do better
	 * @param newInterval
	 */
	public void add(AngleInterval inew)
	{
		if(intervals.isEmpty())
		{
			intervals.add(inew);
			return;
		}
		ListIterator <AngleInterval> it = intervals.listIterator();
		AngleInterval imin = null;
		AngleInterval imax = null;

		boolean minFound = false;
		boolean maxFound = false;
		AngleInterval inext = null;
		while(it.hasNext())
		{
			if(inext != null) {
				if(inew.getMin() > inew.getMax())
				{
					if(minFound || !maxFound)
						it.remove();
				}
				else
				{
					if(minFound && !maxFound)
						it.remove();
				}
			}
			
			inext = it.next();
			if(inext.getMax() <= inew.getMin()) {
				imin = inext.getMax() < inew.getMin() ? null : inext;
				minFound = true;
			}
			if(inext.getMax() >= inew.getMax()) {
				imin = inext.getMin() > inew.getMax() ? null : inext;
			}
		}
	}
	
	public static void main(String ... args) 
	{
		CircleSegmentList list = new CircleSegmentList();
		
		list.add(new AngleInterval(1.5, 2));
		list.add(new AngleInterval(2, 3));
		list.add(new AngleInterval(4, -3.5));
		list.add(new AngleInterval(5, -.15));
		
		for(AngleInterval i : list.intervals)
			System.out.println(i);
	}
}
