package yarangi.graphics.grid;

import java.util.Collection;
import java.util.LinkedList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.objects.ILook;
import yarangi.spatial.IGrid;
import yarangi.spatial.IGridListener;
import yarangi.spatial.Tile;

/**
 * Abstract grid renderer.
 * 
 * @author dveyarangi
 *
 * @param <O>
 * @param <G>
 */
public abstract class GridLook <O, G extends IGrid <Tile<O>>> implements ILook <G>, IGridListener<Tile<O>>
{
	/**
	 * Tiles that need re-rendering
	 */
	protected Collection <Tile<O>> pendingTiles;

	/**
	 * TODO: rendering parameters
	 */
	protected boolean depthtest, blend;
	
	/**
	 * Allows to toggle rendering.
	 */
	protected boolean isVisible = true;
	
	/**
	 * The Grid
	 */
	protected final G grid;

	public GridLook(G grid, boolean depthtest, boolean blend)
	{
		this.depthtest = depthtest;
		this.blend = blend;
		pendingTiles = new LinkedList <Tile<O>> ();
		
		this.grid = grid;
		
		grid.setModificationListener( this );
	}
	
	public boolean isVisible() { return isVisible; }
	public void setVisible(boolean isVisible) { this.isVisible = isVisible; }
	
	@Override
	public void tilesModified(Tile<O> tile)
	{
		synchronized ( pendingTiles )
		{
			pendingTiles.add( tile );
		}
	}

	@Override
	public void init(IRenderingContext context) {}

	@Override
	public void destroy(IRenderingContext context) {
		grid.setModificationListener( null );
	}

	@Override
	public final void render( G entity, IRenderingContext ctx)
	{
		GL2 gl = ctx.gl();
		gl.glPushAttrib( GL2.GL_ENABLE_BIT );
		
		if(blend) gl.glEnable(GL.GL_BLEND); else gl.glDisable(GL.GL_BLEND);
		if(depthtest) gl.glEnable(GL.GL_DEPTH_TEST); else gl.glDisable(GL.GL_DEPTH_TEST);
		
		if(isVisible)
			renderGrid( gl, entity, ctx );
		
		gl.glPopAttrib();
		
		synchronized ( pendingTiles ) 
		{
			pendingTiles.clear();
		}
	}
	
	public abstract void renderGrid(GL2 gl, G entity, IRenderingContext context);

	@Override
	public boolean isCastsShadow() { return false; }


}
