package yar.quadraturin.graphics.grid;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.Q;
import yar.quadraturin.graphics.GLList;
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
public abstract class PolyGridLook <O, G extends IGrid <Tile<O>>> extends GridLook <O, G>
{

	
	private boolean debug = false;
	
	private GLList debugMesh;
		
	private int [][] listIds;
	
	private static final int NO_LIST_ID = -1;
	
	public PolyGridLook(G grid, boolean depthtest, boolean blend)
	{
		super( grid, depthtest, blend );

	}
	
	@Override
	public void init(IRenderingContext ctx)
	{
		super.init( ctx );
		GL2 gl = ctx.gl();

		Q.rendering.debug( "Initializing tiled grid renderer for [" + grid + "]...");
		// rounding texture size to power of 2:
		
		updateLists( gl, grid );
		
		this.listIds = new int[grid.getGridWidth()][grid.getGridHeight()];
		for(int idx = 0; idx < grid.getGridWidth(); idx ++)
			for(int jdx = 0; jdx < grid.getGridHeight(); jdx ++)
				listIds[idx][jdx] = NO_LIST_ID;
		
		assert initDebug(gl, grid);
	}

	@Override
	public void renderGrid(GL2 gl, G grid, IRenderingContext ctx)
	{
		// redrawing changed tiles to frame buffer
		updateLists( gl, grid );
// 
		float minx = grid.getMinX();
		float maxx = grid.getMaxX();
		float miny = grid.getMinY();
		float maxy = grid.getMaxY();
		gl.glColor4f(0.5f, 0.5f, 1f, 0.5f);
		gl.glBegin( GL.GL_LINE_STRIP );
			gl.glVertex2f(minx, miny);
			gl.glVertex2f(minx, maxy);
			gl.glVertex2f(maxx, maxy);
			gl.glVertex2f(maxx, miny);
			gl.glVertex2f(minx, miny);
		gl.glEnd();
		
		// rendering frame buffer texture:
//		fbo.unbind( gl );
//		gl.glDisable( GL.GL_DEPTH_TEST );
//		veil.weave( gl, grid, context );
		for(int idx = 0; idx < grid.getGridWidth(); idx ++)
			for(int jdx = 0; jdx < grid.getGridHeight(); jdx ++)
				// TODO: to many of those, needs better approach:
				gl.glCallList( listIds[idx][jdx] );
//		veil.tear( gl );
		
//		gl.glEnable(GL.GL_BLEND);
		
		assert renderDebug(gl);
		
	}


	@Override
	public void destroy( IRenderingContext ctx)
	{
		GL2 gl = ctx.gl();

		// destroying lists:
		for(int idx = 0; idx < grid.getGridWidth(); idx ++)
			for(int jdx = 0; jdx < grid.getGridHeight(); jdx ++)
				if(listIds[idx][jdx] != NO_LIST_ID)
					gl.getGL2().glDeleteLists( listIds[idx][jdx], 1 );
	}
	
	@Override
	public float getPriority() { return -0f; }

	/**
	 * 
	 * @param gl
	 * @param grid
	 */
	private void updateLists(GL gl1, G grid)
	{
		GL2 gl = gl1.getGL2();
		if(pendingTiles != null && pendingTiles.size() > 0)
		{
			// TODO: listen to viewpont changes and draw only the relevant tiles.
			// This will allow to make sense of scale parameter 
			int scale = 1;
			for(Tile<O> tile : pendingTiles)
			{
				int listId = listIds[tile.i()][tile.j()];
				if(listId != NO_LIST_ID) 
					gl.glDeleteLists( listId, 1 );
				
				listId = gl.glGenLists( 1 );
				listIds[tile.i()][tile.j()] = listId;
				
				gl.glNewList( listId, GL2.GL_COMPILE );

					renderTile(gl, tile, grid, scale);
					
				gl.glEndList();
			}

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
	protected abstract void renderTile(GL2 gl, Tile<O> tile, G grid, int scale);
	
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
				if(!grid.isEmptyAtCoord(x, y))
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
		
		// 
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
