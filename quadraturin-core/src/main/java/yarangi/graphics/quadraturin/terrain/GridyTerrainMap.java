package yarangi.graphics.quadraturin.terrain;

import java.util.Set;

import yarangi.Zen;
import yarangi.graphics.colors.Color;
import yarangi.graphics.quadraturin.Q;
import yarangi.math.FastMath;
import yarangi.spatial.AABB;
import yarangi.spatial.GridMap;
import yarangi.spatial.ISpatialSensor;
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
	
	private int bitmapWidth;
	
	private float toPixelCoef;
	
	private float pixelSize;
	
	public GridyTerrainMap(float width, float height, int cellsize, float pixelsize)
	{
		super(cellsize, (int)width, (int)height);
		
		this.bitmapWidth = (int)(getCellSize()/pixelsize);
		
		this.toPixelCoef = bitmapWidth/cellsize;
//		System.out.println("pixel: " + toPixelCoef);
		
		this.pixelSize = pixelsize;
		
		Q.structure.debug( "Created grid terrain [" + width + "x" + height + "] with cellsize " + cellsize + ".");
	}
	
	public int getBitmapSize()
	{
		return bitmapWidth;
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
		return Zen.notSupported();
	}
	
	public Tile<Bitmap> createTileAt(int i, int j) 
	{
		double x = toXCoord( i );
		double y = toYCoord( j );
		Bitmap bitmap = new Bitmap(toXCoord( i ), toYCoord( j ), bitmapWidth);
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
		
		int dx = (int)(FastMath.floor(x - tile.getX())/pixelSize);
		int dy = (int)(FastMath.floor(y - tile.getY())/pixelSize);
		
//		Color c = tile.get().at( dx, dy );
//		System.out.println("GridyTerrainMap: (" + dx + ":" + dy + "), (" + x + ":" + y + "), (" + tile.getX() + ":" + tile.getY() + ")");
		tile.get().put( color, dx, dy );
		
		return tile;
	}

	/**
	 * Retrieves and removes points in specified radius
	 * @param area
	 * @return
	 */
	public void apply(double ox, double oy, boolean substract, int maskWidth, byte [] mask)
	{
		MaskingSensor sensor = new MaskingSensor (substract, ox, oy, maskWidth, mask);
		float maskRealWidth = maskWidth*pixelSize;
		
		query(sensor, AABB.createFromEdges( ox, oy, ox+maskRealWidth, oy+maskRealWidth, 0 ));
	}
	
	
	public class MaskingSensor implements ISpatialSensor <Tile<Bitmap>, Bitmap>
	{
		double ox, oy;
		byte [] mask;
		boolean substract;
		
		int maskWidth;
		
		public MaskingSensor (boolean substract, double ox, double oy, int maskWidth, byte [] mask)
		{
			this.ox = ox;
			this.oy = oy;
			
			this.mask = mask;
			this.maskWidth = maskWidth;
			
			this.substract = substract;
		}

		@Override
		public boolean objectFound(Tile<Bitmap> chunk, Bitmap bitmap)
		{
//			System.out.println(chunk + " : " + tile);
			int pixelsBefore = bitmap.getPixelCount();
			if(pixelsBefore == 0 && substract)
				return false;
			
			int ioffset = (int)((chunk.getX()-ox)*bitmap.getSize()/getCellSize());
			int joffset = (int)((chunk.getY()-oy)*bitmap.getSize()/getCellSize());
			
			boolean changed = false;
			if(substract)
				changed = bitmap.subMask( ioffset, joffset, maskWidth, mask );
			else
				changed = bitmap.addMask( ioffset, joffset, maskWidth, mask );
			
			if(changed)
				setModified(chunk );
			
			return false;
		}

		@Override
		public void clear() { }
		
	}


	public float getPixelSize() { return pixelSize; }
}
