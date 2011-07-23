package yarangi.graphics.quadraturin;

import java.awt.Dimension;
import java.awt.Point;

import yarangi.math.RangedDouble;
import yarangi.math.Vector2D;

/**
 * 
 */
public class ViewPoint2D implements IViewPoint
{

	//
	private Vector2D center;
	
	private Dimension window;
	
	private RangedDouble scale;
	
	private Dimension world;
//	private Rectangle2D.Double viewWindow;
//	private Rectangle2D.Double virtualWindow;

	
/*	public ViewPoint2D(Dimension window) 
	{
		this(new Vector2D(0,0), window);
	}*/	
	public ViewPoint2D(Vector2D center, Dimension window, RangedDouble scale, Dimension world)
	{
		this.center = center;
		
		this.window = window;

		this.scale = scale;
		
		this.world = world;
//		transpose();
		
	}
	
/*	private void transpose() {
		

		viewWindow = new Rectangle2D.Double(center.x/scale, center.y/scale, 
						window.width/scale, window.height/scale);
		
		virtualWindow = new Rectangle2D.Double(viewWindow.x + viewWindow.width/2,
											   viewWindow.y + viewWindow.height/2, 
											   viewWindow.width/2, viewWindow.height/2);
	}*/
	
	public void setCenter(Vector2D center)
	{
		this.center = center;
	}
	
	public void setScale(double scale)
	{
		this.scale.setDouble(scale);
	}
	public void setScale(RangedDouble scale)
	{
		this.scale = scale;
	}
	public void setWindow(Dimension window)
	{
		this.window = window;
	}

/*	public boolean isInView(Vector2D phyp)
	{
		if(virtualWindow.x - phyp.x/scale > virtualWindow.width)
			return false;
		if(virtualWindow.y - phyp.y/scale > virtualWindow.height)
			return false;
		
		return true;
	}*/
	
	public Point getPoint(java.awt.geom.Point2D.Double p)
	{
		Point point = new Point();
		double h = scale.getDouble();
		point.x = (int)Math.round(p.x*h - center.x()*h - getPortWidth()/2);
		point.y = (int)Math.round(p.y*h - center.y()*h - getPortHeight()/2);
		
		return point;
	}
	
	public Vector2D toStagePoint(java.awt.Point p)
	{
		double h = scale.getDouble();
		return new Vector2D((p.x + center.x()*h - window.width/2)/h, (p.y + center.y()*h - window.height/2)/h);
	}

//	public Rectangle2D.Double getViewWindow() { return viewWindow; }

/*	public int scaleDistance(double dist) {
		return (int)(dist/scale);
	}*/

	public int getPortWidth() { return window.width; }
	public int getPortHeight() { return window.height;}

	public Vector2D getCenter() { return center; }

	public double getScale() { return scale.getDouble(); }

	public double getMinScale() { return scale.getMin(); }
	public double getMaxScale() { return scale.getMax(); }
	
	public void copyFrom(IViewPoint viewPoint)
	{
		if(!(viewPoint instanceof ViewPoint2D))
			throw new IllegalArgumentException("Must be copied from " + this.getClass() + " type.");
		
		ViewPoint2D vp = (ViewPoint2D) viewPoint; 
		this.center = new Vector2D(vp.getCenter().x(), vp.getCenter().y());
		this.window = new Dimension(vp.getPortWidth(), vp.getPortHeight());
		this.scale = new RangedDouble(vp.getMinScale(), vp.getScale(), vp.getMaxScale());
		this.world = new Dimension(vp.world.width, vp.world.height);
	}	

	public String toString()
	{
		return new StringBuilder()
			.append("center: ").append(center).append(" ")
			.append("scale: [").append(scale).append("] ")
			.toString();
		
	}
}
