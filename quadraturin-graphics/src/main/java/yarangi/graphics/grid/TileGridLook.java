package yarangi.graphics.grid;

import java.util.Collection;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.quadraturin.terrain.Cell;
import yarangi.graphics.textures.FBO;
import yarangi.graphics.textures.TextureUtils;
import yarangi.math.BitUtils;
import yarangi.spatial.IGrid;
import yarangi.spatial.IGridListener;

public abstract class TileGridLook <T, G extends IGrid <Cell<T>>> implements Look <G>, IGridListener<Cell<T>>
{
	private FBO fbo;
	
	private int gridTextureWidth, gridTextureHeight;
	
	private Collection <Cell<T>> pendingCells;
	
	@Override
	public void init(GL gl, G grid, IRenderingContext context)
	{
		// rounding texture size to power of 2:
		gridTextureWidth  = BitUtils.po2Ceiling((int)(grid.getMaxX()-grid.getMinX()));
		gridTextureHeight = BitUtils.po2Ceiling((int)(grid.getMaxY()-grid.getMinY()));
		int texture = TextureUtils.createEmptyTexture2D( gl, gridTextureWidth, gridTextureHeight, false );
		
		fbo = FBO.createFBO( gl, texture, TextureUtils.ILLEGAL_ID );
		
		grid.setModificationListener( this );
		
		updateFrameBuffer( gl, grid );
		
	}

	@Override
	public void render(GL gl, double time, G grid, IRenderingContext context)
	{
		
		updateFrameBuffer( gl, grid );
		
		float minx = grid.getMinX();
		float maxx = grid.getMaxX();
		float miny = grid.getMinY();
		float maxy = grid.getMaxY();
		
		fbo.bindTexture(gl);
			gl.glBegin(GL.GL_QUADS);
				gl.glTexCoord2f(0,0); gl.glVertex2f( minx, miny );
				gl.glTexCoord2f(0,1); gl.glVertex2f( minx, maxy );
				gl.glTexCoord2f(1,1); gl.glVertex2f( maxx, maxy );
				gl.glTexCoord2f(1,0); gl.glVertex2f( maxx, miny );
			gl.glEnd();
		fbo.unbindTexture(gl);
		
		gl.glColor3f(1,0,0);
		gl.glBegin(GL.GL_LINE_STRIP);
		 gl.glVertex2f( minx, miny );
		 gl.glVertex2f( minx, maxy );
		 gl.glVertex2f( maxx, maxy );
		 gl.glVertex2f( maxx, miny );
		gl.glEnd();
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
	public float getPriority() { return 0.9f; }

	
	@Override
	public void cellsModified(Collection<Cell<T>> cells)
	{
		pendingCells = cells;
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
		if(pendingCells != null && pendingCells.size() > 0)
		{
			// redrawing changed tiles:
			
			beginFrameBufferSpace( gl, grid );
			for(Cell <T> cell : pendingCells)
				renderTile(gl, cell, grid);
			endFrameBufferSpace( gl );
			
			pendingCells = null;
		}
	}
	
	protected abstract void renderTile(GL gl, Cell<T> cell, G grid);
	

}
