package yarangi.graphics.grid;

import java.util.Collection;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.spatial.IGrid;
import yarangi.spatial.IGridListener;
import yarangi.spatial.Tile;

public abstract class GridLook <O, G extends IGrid <Tile<O>>> implements Look <G>, IGridListener<Tile<O>>
{
	
	protected Collection <Tile<O>> pendingTiles;

	protected boolean depthtest, blend;
	protected boolean isVisible = true;

	public GridLook(boolean depthtest, boolean blend)
	{
		this.depthtest = depthtest;
		this.blend = blend;
	}
	
	public boolean isVisible() { return isVisible; }
	public void setVisible(boolean isVisible) { this.isVisible = isVisible; }
	
	@Override
	public void tilesModified(Collection<Tile<O>> tiles)
	{
		pendingTiles = tiles;
	}

	@Override
	public void init(GL gl, G grid, IRenderingContext context)
	{
		gl.glEnable( GL.GL_DEPTH_TEST );
		gl.glEnable( GL.GL_BLEND );
	}

	@Override
	public void destroy(GL gl, G entity, IRenderingContext context)
	{
	}

	@Override
	public final void render(GL gl, double time, G entity, IRenderingContext context)
	{
		if(isVisible)
			renderGrid( gl, time, entity, context );
	}
	
	public abstract void renderGrid(GL gl, double time, G entity, IRenderingContext context);

	@Override
	public boolean isCastsShadow() { return false; }


}
