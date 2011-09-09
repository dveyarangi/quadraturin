package yarangi.spatial;

import java.util.NoSuchElementException;

import yarangi.math.FastArrays;
import yarangi.math.FastMath;
import yarangi.spatial.Polygon.PolyChunk;
import yarangi.spatial.Polygon.PolyPoint;

/**
 * Implements Bresenham's scanning for each of the polygon edges.
 * TODO: Also consider DDA, ray casting, winding number, scan converting, 
 */

public class PolyIterator implements IGridIterator <IAreaChunk>
{
	/** vertex of current polygon line */
	PolyPoint currPoint, nextPoint;
	/** location of the next point in grid.  */
	private double currx, curry;
	
	
	private int currGridx, currGridy;
	/** location of the next point in grid.  */
	private int nextGridx, nextGridy;
	
	/** step to next point in grid */
	private double dx, dy;
	
	private int startGridx, startGridy;
	
	/** size of single grid cell */
	private double cellsize;
	
	private int currStartIdx, currEndIdx;
	
	private boolean start, scan;

	private Polygon poly; 
	
	public PolyIterator(Polygon poly, double cellsize)
	{
		
		this.poly = poly;
		// TODO: move to {@link #next()} arguments?
		this.cellsize = cellsize;
		PolyPoint tempPoint = poly.get(0);
		
		currx = tempPoint.x();
		curry = tempPoint.y();
		
		int tempGridx = currGridx = nextGridx = startGridx = (int)FastMath.toGrid(currx, cellsize); 
		int tempGridy = currGridy = nextGridy = startGridy = (int)FastMath.toGrid(curry, cellsize); 
		
		int size = poly.size();
		currStartIdx = 0;
		

		// finding the first polygon point in starting cell
		do {
			currPoint = tempPoint;
			
			currStartIdx = FastArrays.dec(currStartIdx, size);
			
			tempPoint = poly.get(currStartIdx);
			tempGridx = (int)FastMath.toGrid(tempPoint.x(), cellsize);
			tempGridy = (int)FastMath.toGrid(tempPoint.y(), cellsize);
//			System.out.println(" * testing at (" + tempGridx + "," + tempGridy + ")");
		} 
		while(tempGridx == startGridx
		   && tempGridy == startGridy);
//		System.out.println(" * starting loc (" + startGridx + "," + startGridy + ")");
		
		currGridx = (int)FastMath.toGrid(currx, cellsize);
		currGridy = (int)FastMath.toGrid(curry, cellsize);
			
		start = scan = true;
		nextPoint = currPoint;
		
		currEndIdx = currStartIdx+1;
		
		
//		nextIndex(false);
		
	}
	
	
	/**
	 * Advances indexes. Calculates currPoint and nextPoint for scanning.
	 * nextGridx, nextGridy
	 * currStartIdx, currEndIdx
	 * NextPoint
	 */
	private void rollIndexes()
	{
		int size = poly.size();
		currStartIdx = FastArrays.dec(currEndIdx, size);
		currEndIdx = currStartIdx;
	
		int tempGridx = nextGridx, tempGridy = nextGridy; 
		do {
			currPoint = nextPoint;
			currEndIdx = FastArrays.inc(currEndIdx, size);
			nextPoint = poly.get(currEndIdx);
			
			nextGridx = (int)FastMath.toGrid(nextPoint.x(), cellsize);
			nextGridy = (int)FastMath.toGrid(nextPoint.y(), cellsize);
		}
		while(nextGridx == tempGridx
		   && nextGridy == tempGridy);
		
		
		
	}
	
	/**
	 * Calculates deltas for Bresenham's scanning for specified polygon edge.
	 * dx and dy are changed
	 */
	private void calcSegment(PolyPoint currPoint, PolyPoint nextPoint)
	{
		
		// calculating segment slope:
		double sx = nextPoint.x() - currPoint.x();
		double sy = nextPoint.y() - currPoint.y();
		double m = sy / sx;
		
		if(m <= 1 && m >= -1) // x changes faster than y:
		{   
			dx = sx > 0 ? cellsize : -cellsize;
			dy = m * dx;
		}
		else 
		{
			dy = sy > 0 ? cellsize : -cellsize;
			dx = Double.isInfinite(m) ? 0 : dy / m;
		}
	}
	
	@Override
	public boolean hasNext() {
		return (currGridx != startGridx	|| currGridy != startGridy) || start;
	}

	@Override
	public IAreaChunk next() 
	{
		// going to the next grid cell center:
		if(!hasNext())
			throw new NoSuchElementException("No more elements.");
		
		// going to the next grid cell center:
		currGridx = (int)FastMath.toGrid(currx, cellsize);
		currGridy = (int)FastMath.toGrid(curry, cellsize);
		
		PolyChunk chunk;
		
		// in a dense poly, this condition will be met frequently:
		if(currGridx == nextGridx && currGridy == nextGridy) // we reached poly edge end 
		{
			// calculating its indexes range inside current cell:
			rollIndexes();
			chunk = new PolyChunk(currGridx, currGridy, currStartIdx, currEndIdx);
			 // set flag to calculate scanning parameters next segment line:
			scan = true; 
		}
		else // traversing along the current poly edge:  
		{
			if(scan)
			{
				// adjusting starting segment index:
				currStartIdx = FastArrays.dec(currEndIdx, poly.size());
				
				// calculating next line segment deltas:
				calcSegment(currPoint, nextPoint);
				
				
				currx = currPoint.x() + dx;
				curry = currPoint.y() + dy;
				// going to the next grid cell center:
				currGridx = (int)FastMath.toGrid(currx, cellsize);
				currGridy = (int)FastMath.toGrid(curry, cellsize);
				
				scan = false;
				start = false;
			}
			
			chunk = new PolyChunk(currGridx, currGridy, currStartIdx, currEndIdx);
			
			if(currGridx != nextGridx || currGridy != nextGridy)
			{ // we could get to the next polypoint point inside the if(scan)
				currx += dx;
				curry += dy;
			}
		}
		
//		System.out.println(chunk);
		return chunk;
	}
}
