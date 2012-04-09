package test.terrain;

import java.awt.Point;

import javax.media.opengl.GL;

import yarangi.graphics.grid.TileGridLook;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.graphics.quadraturin.Q;
import yarangi.graphics.quadraturin.terrain.Bitmap;
import yarangi.graphics.quadraturin.terrain.GridyTerrainMap;
import yarangi.graphics.textures.TextureUtils;
import yarangi.spatial.Tile;

public class GridyTerrainLook extends TileGridLook<Bitmap, GridyTerrainMap>
{

	@Override
	protected void renderTile(GL gl, Tile<Bitmap> tile, GridyTerrainMap grid, int scale)
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
		gl.glBegin( GL.GL_QUADS );
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
		gl.glBegin( GL.GL_QUADS );
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
	public IVeil getVeil() { return IVeil.ORIENTING; }
	
	@Override
	protected Point getFBODimensions(GridyTerrainMap grid)
	{
		return new Point(
				grid.getGridWidth()  * grid.getBitmapSize(), 
				grid.getGridHeight() * grid.getBitmapSize()

			);
	}
	
}
