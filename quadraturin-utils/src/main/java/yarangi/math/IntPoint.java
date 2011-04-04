package yarangi.math;

public class IntPoint
{
	public int x;
	public int y;
	
	public IntPoint(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public String toString()
	{
		return new StringBuffer()
			.append("[").append(x).append(",").append(y).append("]")
			.toString();
	}
	
	public boolean equals(Object object)
	{
		if(!(object instanceof IntPoint))
			return false;
		
		IntPoint point = (IntPoint) object;
		
		return point.x == x && point.y == y;
	}
}
