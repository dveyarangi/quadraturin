package yarangi.graphics.quadraturin.terrain;

import com.seisw.util.geom.Poly;

public interface ITilePoly
{

	boolean isEmpty();

	boolean isFull();

	boolean substract(Poly poly);

	boolean add(Poly poly);
	
	double getMinX();
	double getMinY();

	double getMaxX();
	double getMaxY();

}
