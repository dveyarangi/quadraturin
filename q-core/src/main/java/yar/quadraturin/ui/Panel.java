package yar.quadraturin.ui;

import java.util.HashMap;
import java.util.Map;

import yar.quadraturin.ViewPort;
import yarangi.spatial.AABB;

/**
 * Basic UI layout element. Allows vertical or horizontal sub division.
 * Should not be created directly, but splitted from {@link UserLayer#getBasePanel()}
 * 
 * @author dveyarangi
 *
 */
public class Panel
{
	
	/**
	 * Rectangular screen segment, associated with this panel.
	 */
	private ViewPort viewport;

	/**
	 * Panel layout direction.
	 */
	private Direction direction;
	
	/**
	 * Inner offsets from viewport
	 */
	private Insets insets = Insets.ZERO;
	
	/**
	 * Subpanels
	 */
	private Panel [] children;
	
	/**
	 * Mapping of subdivision proportions to revalidate children on viewport changes
	 */
	private Map <Panel, Integer> properties = new HashMap <Panel, Integer> ();
	
	/**
	 * Actual working area of panel (viewport - insets)
	 */
	private AABB area;
	
	
	public Panel(ViewPort viewport) 
	{
		this.viewport = viewport;
		
		revalidate(viewport);
	}
	
	private Panel()
	{
	}
	
	public void setInsets(Insets insets)
	{
		this.insets = insets;
		revalidate(viewport);
	}
	
	/**
	 * Updates panel's viewport and propagates changes to children
	 * @param viewport
	 */
	public void revalidate(ViewPort viewport)
	{
		int minx = viewport.getMinX()+insets.getLeft();
		int maxx = viewport.getMaxX()-insets.getRight();
		int miny = viewport.getMinY()+insets.getBottom();
		int maxy = viewport.getMaxY()-insets.getTop();
		
		this.viewport = viewport;
		this.area = AABB.createFromEdges(minx, miny, maxx, maxy, 0);
		
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

	/**
	 * Creates sub-panels with specified width percentages.
	 * This procedure may be invoked once per panel
	 * @param percents
	 * @param childInsets
	 * @param direction
	 * @return
	 */
	public Panel [] split(int [] percents, Direction direction) 
	{
		if(children != null)
			throw new IllegalStateException("Panel may only be splitted once.");
		this.direction = direction;
		
		children = new Panel[percents.length];
		
		for(int idx = 0; idx < percents.length; idx ++)
		{
			Panel child = new Panel( );
			
			properties.put( child, percents[idx] );
			
			children[idx] = child;
		}
		
		revalidate( viewport );
		
		return children;
	}
	
	/**
	 * @return working area of the panel
	 */
	public AABB getAABB() { return area; }

	/** 
	 * @return viewport of this panel
	 */
	public ViewPort getViewPort()
	{
		return viewport;
	}


}
