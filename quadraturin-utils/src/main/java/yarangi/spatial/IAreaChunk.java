package yarangi.spatial;

public interface IAreaChunk 
{
	/**
	 * TODO: redundant
	 * @return
	 */
	public Area getArea();
	public double getX();
	public double getY();
	public boolean overlaps(double xmin, double ymin, double xmax, double ymax);
	public double getMinX();
	public double getMinY();
	public double getMaxX();
	public double getMaxY();
}
