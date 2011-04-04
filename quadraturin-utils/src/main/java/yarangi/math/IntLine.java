package yarangi.math;

public class IntLine 
{
	public int x1, x2, y1, y2;
	
	public IntLine(int x1, int y1, int x2, int y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public boolean equals(Object o)
	{
		if(!(o instanceof IntLine))
			return false;
		IntLine l = (IntLine) o;
		
		return x1 == l.x1 && x2 == l.x2 && y1 == l.y1 && y2 == l.y2;
	}
	
	public int hashCode()
	{
		return new Long(x1).hashCode()+new Long(x2).hashCode()+new Long(y1).hashCode()+new Long(y2).hashCode();
	}
}
