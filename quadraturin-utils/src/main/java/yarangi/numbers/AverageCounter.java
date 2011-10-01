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
	
	public double getAverage() { return elements == 0 ? 0 : sum / elements; }
	public double get1DivAverage() { return sum == 0 ? 0 : elements / sum; }
	
	public void reset() {
		this.elements = 0;
		this.sum = 0;
	}
	
	public double getCounter() { return elements; }

	public double getSum() { return sum; }

	public void setSum(double d)
	{
		this.sum = d;
	}

}
