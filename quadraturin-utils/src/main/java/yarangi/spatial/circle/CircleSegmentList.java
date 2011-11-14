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
	private AngleInterval closureInterval;
	
	public CircleSegmentList()
	{
	}

	/**
	 * TODO: drunk porcupine could do better
	 * @param newInterval
	 */
	public void add(AngleInterval inew)
	{
		boolean inverse = inew.getMin() > inew.getMax();
		if(inverse && closureInterval == null)
		{
			closureInterval = inew;
		}
		else
		if(intervals.isEmpty())
		{
			intervals.add(inew);
			return;
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

			if(inverse)
			{

				
				if(inext.getMax() >= inew.getMin()) {
					minFound = true;
					if(inext.getMin() < inew.getMin())
						imin = inext;
				}
				boolean replacementZone = (minFound || !maxFound) ;
				
				
				if(inext.getMax() >= inew.getMax() && !maxFound) {
					maxFound = true;
					if(inext.getMin() > inew.getMax())
						removeInterval = false;
					else
						imax = inext;
				}		
				
				
				if(replacementZone && removeInterval) {
					it.remove();
				}
				removeInterval = true;
				
			}
			else
			{
				if(inext.getMax() >= inew.getMin() && !minFound) {
					minFound = true;
					if(inext.getMin() > inew.getMax())
						removeInterval = false; // we skipped over this
					else
						imin = inext;
				}
				
				boolean replacementZone = minFound && !maxFound ;
				
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

			
		}
		boolean mergeWithIntverse = closureInterval != null && 
				(inew.getMin() <= closureInterval.getMax() ||
				 inew.getMax() >= closureInterval.getMin());
		if(inverse || mergeWithIntverse)
			closureInterval = new AngleInterval(imin == null ? Math.max(inew.getMin(), closureInterval.getMin()) : imin.getMin(), 
											    imax == null ? Math.min(inew.getMax(), closureInterval.getMax()) : imax.getMax());
		else
			it.add(new AngleInterval(imin == null ? inew.getMin() : imin.getMin(),
									 imax == null ? inew.getMax() : imax.getMax()));
	}
	
	public static void main(String ... args) 
	{
		CircleSegmentList list = new CircleSegmentList();
		
		list.add(new AngleInterval(1, 2));
		list.add(new AngleInterval(2.5, 3));
		list.add(new AngleInterval(2.4, -0.5));
		
		for(AngleInterval i : list.intervals)
			System.out.println(i);
	}
}
