package yarangi.numbers;

public class AverageCounter 
{
	private double elements;
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
	public void addValue(double value, double weight)
	{
		sum += value;
		elements += weight;
	}
	
	public double getAverage() { return sum / elements; }
	public double get1DivAverage() { return elements / sum; }
	
	public void reset() {
		this.elements = 0;
		this.sum = 0;
	}
	
	public double getCounter() { return elements; }

}
