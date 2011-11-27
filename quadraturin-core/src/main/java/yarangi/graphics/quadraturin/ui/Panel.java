package yarangi.graphics.quadraturin.ui;

import java.util.HashMap;
import java.util.Map;

import yarangi.graphics.quadraturin.ViewPort;
import yarangi.spatial.AABB;

public class Panel
{
	private ViewPort viewport;

	private Direction direction;
	
	private Insets insets;
	
	private Panel [] children;
	
	private Map <Panel, Integer> properties = new HashMap <Panel, Integer> ();
	
	private AABB area;
	
	
	public Panel(ViewPort viewport, Insets insets) 
	{
		this.viewport = viewport;
		this.insets = insets;
		
		revalidate(viewport);
	}
	
	private Panel(Insets insets)
	{
		this.insets = insets;
	}
	
	public void revalidate(ViewPort viewport)
	{
		int minx = viewport.getMinX()+insets.getLeft();
		int maxx = viewport.getMaxX()-insets.getRight();
		int miny = viewport.getMinY()+insets.getBottom();
		int maxy = viewport.getMaxY()-insets.getTop();
		
		this.viewport = viewport;
		this.area = new AABB(minx, miny, maxx, maxy, 0);
		
		if(children == null)
			return;
		
		int width = maxx - minx;
		int height = maxy - miny;
		
		int cminx = minx, cminy = miny, cmaxx = maxx, cmaxy = maxy;
		
		for(int idx = 0; idx < children.length; idx ++)
		{
			cmaxx = direction == Direction.HORIZONTAL ? cminx + (width * properties.get( children[idx]))/100 : cmaxx;
			cmaxy = direction == Direction.VERTICAL   ? cminy + (height * properties.get( children[idx]))/100 : cmaxy;
			ViewPort childPort = new ViewPort(cminx, cminy, cmaxx-cminx, cmaxy-cminy);
			
			children[idx].revalidate( childPort );
			
			cminx = direction == Direction.HORIZONTAL ? cmaxx : cminx; 
			cminy = direction == Direction.VERTICAL   ? cmaxy : cminy; 
		}
	}

	public Panel [] split(int [] percents, Insets [] childInsets, Direction direction) 
	{
		if(percents.length != childInsets.length)
			throw new IllegalArgumentException("Number of layouts and split pivots must be the same.");
		
		this.direction = direction;
		
		children = new Panel[percents.length];
		
		for(int idx = 0; idx < percents.length; idx ++)
		{
			Panel child = new Panel( childInsets[idx] );
			
			properties.put( child, percents[idx] );
			
			children[idx] = child;
		}
		
		revalidate( viewport );
		
		return children;
	}
	
	public AABB getAABB() { return area; }
	
	public static final class LayoutProps
	{
		private ViewPort viewport;
		private int percents;
		
		public LayoutProps(ViewPort viewport, int percents)
		{
			this.viewport = viewport;
			this.percents = percents;
		}

		protected ViewPort getViewport() { return viewport; }
		protected int getPercents() { return percents; }
	}

	public ViewPort getViewPort()
	{
		return viewport;
	}


}
