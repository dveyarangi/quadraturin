package yarangi.math;

public class GenericVector implements IVector
{
	
	private double [] coords;
	
	public GenericVector(int dims)
	{
		this.coords = new double [dims];
		for(int idx = 0; idx < dims; idx ++)
			coords[idx] = 0;
	}
	
	public GenericVector(double ... coords)
	{
		this.coords = coords;
	}

	public double getCoord(int dim) {
		return coords[dim];
	}

	public int getDimensions() {
		return coords.length;
	}

}
