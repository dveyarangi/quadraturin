package yarangi.graphics.grid;

import java.awt.Point;
import java.util.Collection;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.Q;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.textures.FBO;
import yarangi.graphics.textures.TextureUtils;
import yarangi.math.BitUtils;
import yarangi.spatial.IGrid;
import yarangi.spatial.IGridListener;
import yarangi.spatial.Tile;

public abstract class TileGridLook <O, G extends IGrid <Tile<O>>> implements Look <G>, IGridListener<Tile<O>>
{
	private FBO fbo;
	
	private int gridTextureWidth, gridTextureHeight;
	
	private Collection <Tile<O>> pendingTiles;
	
	private boolean debug = false;
	
	@Override
	public void init(GL gl, G grid, IRenderingContext context)
	{
//		Q.rendering.debug( "Initializing tiled grid renderer for [" + grid + "]...");
		// rounding texture size to power of 2:
		Point dim = getFBODimensions( grid );
		int tx = BitUtils.po2Ceiling( dim.x );
		int ty = BitUtils.po2Ceiling( dim.y );
		// TODO: divide to 1024x1024 textures
		int texture = TextureUtils.createEmptyTexture2D( gl, tx, ty, false );
//		Q.rendering.debug( "Created terrain FBO texture - size:[" + tx + "x" + ty + "], texture id:" + texture);
		
		gl.glBindTexture( GL.GL_TEXTURE_2D, texture );
		gl.glTexParameteri( GL.GL_TEXTURE_2D,  GL.GL_TEXTURE_MIN_FILTER,  GL.GL_NEAREST);
		gl.glTexParameteri( GL.GL_TEXTURE_2D,  GL.GL_TEXTURE_MAG_FILTER,  GL.GL_NEAREST);		
		gl.glBindTexture( GL.GL_TEXTURE_2D, 0 );
		
		fbo = FBO.createFBO( gl, texture, TextureUtils.ILLEGAL_ID );
//		Q.rendering.debug( "Created terrain FBO - id:" + fbo.getFboId());
		
		grid.setModificationListener( this );
		
		updateFrameBuffer( gl, grid );
	}
	
	protected abstract Point getFBODimensions(G grid);

	@Override
	public void render(GL gl, double time, G grid, IRenderingContext context)
	{
		// redrawing changed tiles to frame buffer
		updateFrameBuffer( gl, grid );
		
		float minx = grid.getMinX();
		float maxx = grid.getMaxX();
		float miny = grid.getMinY();
		float maxy = grid.getMaxY();
		
		// rendering frame buffer texture:
		fbo.bindTexture(gl);
			gl.glBegin(GL.GL_QUADS);
				gl.glTexCoord2f(0,0); gl.glVertex2f( minx, miny );
				gl.glTexCoord2f(0,1); gl.glVertex2f( minx, maxy );
				gl.glTexCoord2f(1,1); gl.glVertex2f( maxx, maxy );
				gl.glTexCoord2f(1,0); gl.glVertex2f( maxx, miny );
			gl.glEnd();
		fbo.unbindTexture(gl);
		
		// rendering debug overlay:
		if(debug) {
			gl.glColor4f(0,0.5f,0, 0.5f);
			// border
			gl.glBegin(GL.GL_LINE_STRIP);
			 gl.glVertex2f( minx, miny );
			 gl.glVertex2f( minx, maxy );
			 gl.glVertex2f( maxx, maxy );
			 gl.glVertex2f( maxx, miny );
			 gl.glVertex2f( minx, miny );
			gl.glEnd();
			// x axis
			gl.glColor4f(0,0.5f,0, 0.5f);
			gl.glBegin(GL.GL_LINE_STRIP);
			gl.glVertex2f( 0, miny );
		 	gl.glVertex2f( 0, maxy );
		 	gl.glEnd();
			// y axis
			gl.glBegin(GL.GL_LINE_STRIP);
			gl.glVertex2f( minx, 0);
		 	gl.glVertex2f( maxx, 0);
		 	gl.glEnd();
			
		 	// tiles
			gl.glColor4f(0,0.5f,0, 0.2f);
			for(float x = minx; x <= maxx; x += grid.getCellSize())
			{
				gl.glBegin(GL.GL_LINE_STRIP);
				gl.glVertex2f( x, miny );
			 	gl.glVertex2f( x, maxy );
			 	gl.glEnd();
			}
			for(float y = minx; y <= maxx; y += grid.getCellSize())
			{
				gl.glBegin(GL.GL_LINE_STRIP);
				gl.glVertex2f( minx, y);
			 	gl.glVertex2f( maxx, y);
			 	gl.glEnd();
			}
		}
	}

	@Override
	public void destroy(GL gl, G grid, IRenderingContext context)
	{
		
		grid.setModificationListener( null );
		
		fbo.destroy( gl );
	}

	@Override
	public boolean isCastsShadow()
	{
		return false;
	}
	
	@Override
	public float getPriority() { return -1f; }

	
	@Override
	public void cellsModified(Collection<Tile<O>> tiles)
	{
		pendingTiles = tiles;
	}

	private void beginFrameBufferSpace(GL gl, G grid)
	{
		// transforming the rendering plane to fit the terrain grid:
		gl.glPushAttrib(GL.GL_VIEWPORT_BIT | GL.GL_ENABLE_BIT);	
		gl.glMatrixMode(GL.GL_MODELVIEW); gl.glPushMatrix();  gl.glLoadIdentity();
		gl.glMatrixMode(GL.GL_PROJECTION); gl.glPushMatrix(); gl.glLoadIdentity();
		gl.glViewport(0,0,gridTextureWidth, gridTextureHeight);
		gl.glOrtho(grid.getMinX(), grid.getMaxX(), 
				   grid.getMinY(), grid.getMaxY(), -1, 1);

		fbo.bind(gl);
	}
	
	private void endFrameBufferSpace(GL gl)
	{
		fbo.unbind(gl);
		
		gl.glMatrixMode(GL.GL_PROJECTION); gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW); gl.glPopMatrix(); 
		gl.glPopAttrib();	
	}
	
	private void updateFrameBuffer(GL gl, G grid)
	{
		if(pendingTiles != null && pendingTiles.size() > 0)
		{
			// redrawing changed tiles:
			
			beginFrameBufferSpace( gl, grid );
			for(Tile<O> tile : pendingTiles)
				renderTile(gl, tile, grid);
			endFrameBufferSpace( gl );
			
			pendingTiles = null;
		}
	}
	
	protected abstract void renderTile(GL gl, Tile<O> tile, G grid);
	
	public void setDebugOverlay(boolean debug)
	{
		this.debug = debug;
	}
}
