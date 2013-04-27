package yarangi.graphics.grid;

import java.awt.Point;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yarangi.graphics.GLList;
import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.IVeil;
import yarangi.graphics.quadraturin.Q;
import yarangi.graphics.textures.FBO;
import yarangi.graphics.textures.TextureUtils;
import yarangi.spatial.IGrid;
import yarangi.spatial.Tile;

/**
 * This is a generic renderer for bigger grids
 * Renders into a dedicated frame buffer.
 * Inheriting classes must define {@link Tile} rendering procedure.
 * Only tiles that where explicitly marked "modified" by grid implementation
 * will be redrawn at rendering turn.
 * 
 * @author dveyarangi
 *
 * @param <O>
 * @param <G>
 */
public abstract class TileGridLook <O, G extends IGrid <Tile<O>>> extends GridLook <O, G>
{
	private FBO fbo;
	
	private double gridWidth, gridHeight;
	
	private boolean debug = false;
	
	private Point dimensions;
	
	private GLList debugMesh;
	
	private IVeil veil;

	public TileGridLook(G grid, boolean depthtest, boolean blend)
	{
		super( grid, depthtest, blend);
	}
	
	@Override
	public void init(IRenderingContext ctx)
	{
		super.init( ctx );
		
		GL2 gl = ctx.gl();

		Q.rendering.debug( "Initializing tiled grid renderer for [" + grid + "]...");
		// rounding texture size to power of 2:
 		this.dimensions = getFBODimensions( ctx, grid );
//		this.dimensions = new Point(BitUtils.po2Ceiling(  (int)(grid.getMaxX()-grid.getMinX()) ), BitUtils.po2Ceiling( (int)(grid.getMaxY()-grid.getMinY()) ));
//		this.dimensions = new Point( 512, 512);
		// TODO: divide to 1024x1024 textures
		int texture = TextureUtils.createEmptyTexture2D( gl, dimensions.x, dimensions.y, false );
		Q.rendering.debug( "Created terrain FBO texture - size:[" + dimensions.x + "x" + dimensions.y + "], texture id:" + texture);
		
		gl.glBindTexture( GL.GL_TEXTURE_2D, texture );
//		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		gl.glTexParameteri( GL.GL_TEXTURE_2D,  GL.GL_TEXTURE_MIN_FILTER,  GL.GL_NEAREST);
		gl.glTexParameteri( GL.GL_TEXTURE_2D,  GL.GL_TEXTURE_MAG_FILTER,  GL.GL_NEAREST);		
		gl.glBindTexture( GL.GL_TEXTURE_2D, 0 );
		
		gridWidth = grid.getMaxX() - grid.getMinX();
		gridHeight = grid.getMaxY() - grid.getMinY();
		
		fbo = FBO.createFBO( gl, texture, TextureUtils.ILLEGAL_ID );
		Q.rendering.debug( "Created terrain FBO - id:" + fbo.getFboId());
		
		updateFrameBuffer( gl.getGL2(), ctx, grid );
		
		assert initDebug(gl, grid);
	}
	

	/** 
	 * Defines dimensions terrain render buffer (should be rounded to n^2) 
	 * @param grid
	 * @return
	 */
	protected abstract Point getFBODimensions(IRenderingContext context, G grid);

	@Override
	public void renderGrid(GL2 gl, G grid, IRenderingContext context)
	{
		// redrawing changed tiles to frame buffer
		synchronized(pendingTiles) {
			updateFrameBuffer( gl, context, grid );
		}
// 
		float minx = grid.getMinX();
		float maxx = grid.getMaxX();
		float miny = grid.getMinY();
		float maxy = grid.getMaxY();
		
		// rendering frame buffer texture:
//		fbo.unbind( gl );
//		gl.glDisable( GL.GL_DEPTH_TEST );
		if(veil != null)
			veil.weave( gl, context );
		fbo.bindTexture(gl);
			gl.glBegin(GL2.GL_QUADS);
			gl.glColor4f( 0, 1, 0, 1 );
				gl.glTexCoord2f(0,0); gl.glVertex2f( minx, miny );
				gl.glTexCoord2f(0,1); gl.glVertex2f( minx, maxy );
				gl.glTexCoord2f(1,1); gl.glVertex2f( maxx, maxy );
				gl.glTexCoord2f(1,0); gl.glVertex2f( maxx, miny );
			gl.glEnd();
		fbo.unbindTexture(gl);
		if(veil != null)
			veil.tear( gl );
		
		assert renderDebug(gl);
		
	}


	@Override
	public void destroy(IRenderingContext ctx)
	{
		GL2 gl = ctx.gl();

		super.destroy( ctx );

		fbo.destroy( gl );
	}
	
	/**
	 * Override this to set rendering priority (higher is better)
	 */
	@Override
	public float getPriority() { return -0f; }

	protected void beginFrameBufferSpace(GL2 gl, G grid)
	{
		// transforming the rendering plane to fit the terrain grid:
		gl.glPushAttrib(GL2.GL_VIEWPORT_BIT);	
		gl.glMatrixMode(GL2.GL_MODELVIEW); gl.glPushMatrix();  gl.glLoadIdentity();
		gl.glMatrixMode(GL2.GL_PROJECTION); gl.glPushMatrix(); gl.glLoadIdentity();
		gl.glViewport(0,0,dimensions.x, dimensions.y);
		gl.glOrtho(grid.getMinX(), grid.getMaxX(), grid.getMinY(), grid.getMaxY(), -1, 1);

		fbo.bind(gl);
	}
	
	protected void endFrameBufferSpace(GL2 gl)
	{
		fbo.unbind(gl);
		
		gl.glMatrixMode(GL2.GL_PROJECTION); gl.glPopMatrix();
		gl.glMatrixMode(GL2.GL_MODELVIEW); gl.glPopMatrix(); 
		gl.glPopAttrib();	
	}
	
	/**
	 * 
	 * @param gl
	 * @param grid
	 */
	private void updateFrameBuffer(GL2 gl, IRenderingContext context, G grid)
	{
		if(pendingTiles != null && pendingTiles.size() > 0)
		{

			beginFrameBufferSpace( gl, grid );
			// TODO: listen to viewpont changes and draw only the relevant tiles.
			// This will allow to make sense of scale parameter 
			int scale = 1;
			for(Tile<O> tile : pendingTiles)
				renderTile(gl, context, tile, grid, scale);
			endFrameBufferSpace( gl );
		}
	}
	
	/**
	 * Renders a single tile of terrain texture
	 * 
	 * @param gl
	 * @param tile
	 * @param grid
	 * @param scale
	 */
	protected abstract void renderTile(GL2 gl, IRenderingContext context, Tile<O> tile, G grid, int scale);
	
	public void setDebugOverlay(boolean debug)
	{
		this.debug = debug;
	}
	
	private boolean initDebug(GL gl1, G grid)
	{
		GL2 gl = gl1.getGL2();
		float minx = grid.getMinX();
		float maxx = grid.getMaxX();
		float miny = grid.getMinY();
		float maxy = grid.getMaxY();
		// rendering debug overlay:
		
		debugMesh = GLList.create(gl);
		debugMesh.start( gl );
		
		gl.glColor4f(0,0.5f,0, 0.5f);
		gl.glEnable( GL.GL_BLEND );
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
		for(float x = minx; x < maxx; x += grid.getCellSize())
			for(float y = miny; y < maxy; y += grid.getCellSize())
			{
				if(!grid.isEmptyAtCoord( x, y ))
				{
					gl.glBegin(GL.GL_LINE_STRIP);
					gl.glVertex2f( x, y+grid.getCellSize() );
					gl.glVertex2f( x+grid.getCellSize(), y );
					gl.glEnd();
					gl.glBegin(GL.GL_LINE_STRIP);
					gl.glVertex2f( x, y );
					gl.glVertex2f( x+grid.getCellSize(), y+grid.getCellSize() );
					gl.glEnd();
				}
			}	
		
		 
		gl.glColor4f(0,0.5f,0, 0.2f);
		for(float x = minx; x <= maxx; x += grid.getCellSize())
		{
			gl.glBegin(GL.GL_LINE_STRIP);
			gl.glVertex2f( x, miny );
		 	gl.glVertex2f( x, maxy );
		 	gl.glEnd();
		}
		for(float y = miny; y <= maxy; y += grid.getCellSize())
		{
			gl.glBegin(GL.GL_LINE_STRIP);
			gl.glVertex2f( minx, y);
		 	gl.glVertex2f( maxx, y);
		 	gl.glEnd();
		}
		
		debugMesh.end( gl );
		
		return true;

	}
	

	private boolean renderDebug(GL2 gl)
	{
		if(debug)
			debugMesh.call( gl );
		return true;
	}

}
