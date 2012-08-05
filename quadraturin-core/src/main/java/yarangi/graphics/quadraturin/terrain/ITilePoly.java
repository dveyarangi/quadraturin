package yarangi.graphics.quadraturin.terrain;

import com.seisw.util.geom.Poly;

public interface ITilePoly
{

	boolean isEmpty();

	boolean isFull();

	void substract(Poly poly);

	void add(Poly poly);
	
	double getMinX();
	double getMinY();

	double getMaxX();
	double getMaxY();

}
