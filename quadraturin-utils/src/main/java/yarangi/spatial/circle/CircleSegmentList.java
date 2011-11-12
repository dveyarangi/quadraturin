package yarangi.spatial.circle;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CircleSegmentList
{
	private List <AngleInterval> intervals = new LinkedList <AngleInterval> ();
	
	public void add(AngleInterval interval)
	{
		Iterator <AngleInterval> it = intervals.iterator();
		Iterator <AngleInterval> maxit = intervals.iterator();
		List <AngleInterval> merged = new LinkedList <AngleInterval> ();
		while(it.hasNext())
		{
			AngleInterval imin = it.next();
			if(imin.calcPosition( interval.getMin() ) == 0)
			{
				if(imin.calcPosition( interval.getMax() ) == 0)
					return; // new interval included in an existing one
				
				while(maxit.hasNext())
				{
					AngleInterval imax = maxit.next();
					if(imax.calcPosition( interval.getMin() ) == 0)
					{
						
					}
					
				}
				
			}
				
		}
	}
}
