package yarangi.graphics.quadraturin.terrain;

import com.seisw.util.geom.Poly;

/**
 * Represents a grid tile filled with polygons.
 * 
 * Implementations {@link MultilayerTilePoly#substract(Poly)} and {@link TilePoly#substract(Poly)}
 * @author dveyarangi
 *
 */
public interface ITilePoly
{

	/**
	 * @return true, if no polygons in this tile
	 */
	boolean isEmpty();

	/**
	 * @return true if tile is fully covered by polygons
	 */
	boolean isFull();

	/**
	 * Substracts specified polygonal mask from the polygons in this tile.
	 * @param poly
	 * @return
	 */
	boolean substract(Poly poly);

	/**
	 * See {@link MultilayerTilePoly#add(Poly)} and {@link TilePoly#add(Poly)}
	 * @param poly
	 * @return
	 */
	boolean add(Poly poly);

	/*
	 * tile boundary coordinates
	 */
	double getMinX();
	double getMinY();
	double getMaxX();
	double getMaxY();

	Poly getPoly();

}
