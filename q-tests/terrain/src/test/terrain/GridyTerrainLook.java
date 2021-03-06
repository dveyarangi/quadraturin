package test.terrain;

import java.awt.Point;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.IVeil;
import yar.quadraturin.graphics.grid.TileGridLook;
import yar.quadraturin.graphics.textures.TextureUtils;
import yar.quadraturin.terrain.Bitmap;
import yar.quadraturin.terrain.GridyTerrainMap;
import yarangi.spatial.Tile;

public class GridyTerrainLook extends TileGridLook<Bitmap, GridyTerrainMap>
{

	public GridyTerrainLook(GridyTerrainMap grid, boolean depthtest, boolean blend)
	{
		super( grid, depthtest, blend );
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void renderTile(GL2 gl, IRenderingContext context, Tile<Bitmap> tile, GridyTerrainMap grid, int scale)
	{
		Bitmap chunk = tile.get();
		if(chunk == null)
			return;
		
		if(chunk.hasTexture())
			TextureUtils.destroyTexture(gl, chunk.getTextureId());
		
		chunk.setTextureId( TextureUtils.createBitmapTexture2D( gl, chunk.getSize(), chunk.getSize(), chunk.getPixels(), false ) );
			
		gl.glDisable( GL.GL_DEPTH_TEST );
		gl.glDisable( GL.GL_BLEND );
		gl.glColor3f( 0,0,0 );
		gl.glBegin( GL2.GL_QUADS );
		gl.glVertex2f( (float)tile.getMinX(), (float)tile.getMinY());
		gl.glVertex2f( (float)tile.getMaxX(), (float)tile.getMinY());
		gl.glVertex2f( (float)tile.getMaxX(), (float)tile.getMaxY());
		gl.glVertex2f( (float)tile.getMinX(), (float)tile.getMaxY());
		gl.glEnd();
		
		
		gl.glBindTexture( GL.GL_TEXTURE_2D, chunk.getTextureId() );
		
		// disabling filtering for tile texture to avoid  
		gl.glTexParameteri( GL.GL_TEXTURE_2D,  GL.GL_TEXTURE_MIN_FILTER,  GL.GL_NEAREST);
		gl.glTexParameteri( GL.GL_TEXTURE_2D,  GL.GL_TEXTURE_MAG_FILTER,  GL.GL_NEAREST);
		
		gl.glColor3f( 1,1,1 );
		gl.glBegin( GL2.GL_QUADS );
//		System.out.println(cell.getMinX() + " : " + cell.getMaxX());
			gl.glTexCoord2f( 0, 0 ); gl.glVertex2f( (float)tile.getMinX(), (float)tile.getMinY());
			gl.glTexCoord2f( 0, 1 ); gl.glVertex2f( (float)tile.getMinX(), (float)tile.getMaxY());
			gl.glTexCoord2f( 1, 1 ); gl.glVertex2f( (float)tile.getMaxX(), (float)tile.getMaxY());
			gl.glTexCoord2f( 1, 0 ); gl.glVertex2f( (float)tile.getMaxX(), (float)tile.getMinY());
		gl.glEnd();
		
		gl.glBindTexture( GL.GL_TEXTURE_2D, 0 );
		gl.glEnable( GL.GL_DEPTH_TEST );
		gl.glEnable( GL.GL_BLEND );
	}
	@Override
	public IVeil getVeil() { return null; }
	
	@Override
	protected Point getFBODimensions(IRenderingContext context, GridyTerrainMap grid)
	{
		return new Point(
				grid.getGridWidth()  * grid.getBitmapSize(), 
				grid.getGridHeight() * grid.getBitmapSize()

			);
	}
	@Override
	public boolean isOriented() { return false; }

	
}
