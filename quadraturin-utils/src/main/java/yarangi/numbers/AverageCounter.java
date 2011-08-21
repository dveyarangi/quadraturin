package yarangi.numbers;

public class AverageCounter 
{
	private int elements;
	private double sum;
	
	public AverageCounter()
	{
		reset();
	}
	
	public void addValue(double value)
	{
		sum += value;
		elements ++;
	}
	
	public double getAverage() { return sum / elements; }
	public double get1DivAverage() { return elements / sum; }
	
	public void reset() {
		this.elements = 0;
		this.sum = 0;
	}
	
	public int getCounter() { return elements; }

}
