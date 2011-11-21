package yarangi.intervals;

public class AngleInterval {
	private double min;
	private double max;
	
	/**
	 * If min is higher than max, the interval contains pi/2 angle
	 * @param min
	 * @param max
	 */
	public AngleInterval(double min, double max) 
	{
		this.min = min;
		this.max = max;
	}
	
	public double getMin() { return min; }
	public double getMax() { return max; }
	
	/**
	 * @return negative if the value is left to the interval, positive if right and zero if inside.
	 */
	public double calcPosition(double value)
	{
		if(value < min)
			return value-min;
		if(value > max)
			return value-max;
		
		return 0;
	}
	
	public boolean equals(Object o) 
	{
		if(!(o instanceof AngleInterval))
			return false;
		
		AngleInterval that = (AngleInterval) o;
		
		return this.min == that.min && this.max == that.max;
	}
	
	public int hashCode() {
		// FIXME: bullshit:
		return (int)(max*31+min*17);
	}
	
	public String toString() 
	{
		return new StringBuilder()
			.append("angles [").append(min).append( ":" ).append(max).append( "]" )
			.toString();
	}
}
