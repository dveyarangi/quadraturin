package yarangi.graphics.quadraturin.terrain;

import java.util.Set;

import yarangi.ZenUtils;
import yarangi.graphics.colors.Color;
import yarangi.graphics.quadraturin.Q;
import yarangi.math.FastMath;
import yarangi.spatial.GridMap;
import yarangi.spatial.Tile;

/**
 * 
 * @author dveyarangi
 *
 * @param <T> - tile type
 * @param <P> - tile pixel type
 */
public class GridyTerrainMap extends GridMap<Tile <Bitmap>, Bitmap> implements ITileMap <Bitmap>
{
	
	private float pixelsize;
	
	public GridyTerrainMap(float width, float height, int cellsize, int pixelsize)
	{
		super(cellsize, (int)width, (int)height);
		
		this.pixelsize = pixelsize;
		
		Q.structure.debug( "Created grid terrain [" + width + "x" + height + "] with cellsize " + cellsize + ".");
	}

	@Override
	public Tile <Bitmap>[] createMap(int cellSize, int width, int height)
	{
		return (Tile <Bitmap> []) new Tile [width*height]; // ugly, as always	
	}
	
	@Override
	protected Tile<Bitmap> createEmptyCell(int idx, double x, double y)
	{
		return new Tile <Bitmap> (x, y, getCellSize(), getCellSize());
	}

	@Override
	public final int indexAtTile(int x, int y) 
	{
		return  x + getGridWidth() * y;
	}

	@Override
	public Set<Bitmap> keySet()
	{
		return ZenUtils.methodNotSupported( GridyTerrainMap.class );
	}
	
	public Tile<Bitmap> createTileAt(int i, int j) 
	{
		double x = toRealXIndex( i );
		double y = toRealYIndex( j );
		Bitmap bitmap = new Bitmap(toRealXIndex( i ), toRealYIndex( j ), getCellSize(), 1);
		Tile <Bitmap> tile = getTileByIndex( i, j );
		if(tile == null)
		{
			tile = new Tile <Bitmap> (x, y, getCellSize(), getCellSize());
			putAtIndex( i, j, tile );
		}
		tile.put( bitmap );
		
		return tile;
	}
	
	public Tile <Bitmap> setPixel(double x, double y, Color color)
	{
		if(x < getMinX() || x > getMaxX() || y < getMinY() || y > getMaxY())
			return null;
		Tile <Bitmap> tile = getTile( x, y );
		if(tile == null)
			return null;
		
		int dx = FastMath.floor(x - tile.getX());
		int dy = FastMath.floor(y - tile.getY());
		
//		Color c = tile.get().at( dx, dy );
		tile.get().put( color, dx, dy );
		
		return tile;
	}

	/**
	 * Retrieves and removes points in specified radius
	 * @param area
	 * @return
	 */
/*	public void apply(double ox, double oy, boolean substract, byte [] mask)
	{
		MaskingSensor sensor = new MaskingSensor (false, ox, oy, mask);
		
		query(sensor, ox, oy, 8);
	}*/
	
	
/*	public class MaskingSensor implements ISpatialSensor <T>
	{
		double ox, oy;
		byte [] mask;
		boolean substract;
		
		public MaskingSensor (boolean substract, double ox, double oy, byte [] mask)
		{
			this.ox = ox;
			this.oy = oy;
			
			this.mask = mask;
			
			this.substract = substract;
		}

		@Override
		public boolean objectFound(IAreaChunk chunk, T tile)
		{
//			System.out.println(chunk + " : " + tile);
			int pixelsBefore = tile.getPixelCount();
			if(pixelsBefore == 0 && substract)
				return false;
			
			int ioffset = (int)((chunk.getMinX() - ox) / getCellSize());
			int joffset = (int)((chunk.getMinY() - oy) / getCellSize());
			
			if(substract)
				tile.subMask( ioffset, joffset, mask );
			else
				tile.addMask( ioffset, joffset, mask );
			
			if(tile.getPixelCount() != pixelsBefore)
				setModified( (Cell<T>)chunk );
			
			return false;
		}

		@Override
		public void clear() { }
		
	}*/
	
	public float getPixelSize() { return pixelsize; }
}
