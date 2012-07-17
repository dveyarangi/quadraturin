package yarangi.graphics.grid;

import java.util.Collection;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.objects.ILook;
import yarangi.spatial.IGrid;
import yarangi.spatial.IGridListener;
import yarangi.spatial.Tile;

public abstract class GridLook <O, G extends IGrid <Tile<O>>> implements ILook <G>, IGridListener<Tile<O>>
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
	}

	@Override
	public void destroy(GL gl, G entity, IRenderingContext context)
	{
	}

	@Override
	public final void render(GL gl, double time, G entity, IRenderingContext context)
	{
		gl.glPushAttrib( GL.GL_ENABLE_BIT );
		if(blend) gl.glEnable(GL.GL_BLEND); else gl.glDisable(GL.GL_BLEND);
		if(depthtest) gl.glEnable(GL.GL_DEPTH_TEST); else gl.glDisable(GL.GL_DEPTH_TEST);
		
		if(isVisible)
			renderGrid( gl, time, entity, context );
		gl.glPopAttrib();
	}
	
	public abstract void renderGrid(GL gl, double time, G entity, IRenderingContext context);

	@Override
	public boolean isCastsShadow() { return false; }


}
