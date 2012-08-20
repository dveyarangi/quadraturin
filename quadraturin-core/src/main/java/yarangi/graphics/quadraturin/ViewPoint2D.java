package yarangi.graphics.quadraturin;

import java.awt.Dimension;
import java.awt.Point;

import javax.media.opengl.glu.GLU;

import yarangi.math.FastMath;
import yarangi.math.RangedDouble;
import yarangi.math.Vector2D;

/**
 * 
 */
public class ViewPoint2D implements IBeholder
{
	public static GLU glu = new GLU();

	//
	private Vector2D center;

	private RangedDouble scale;
	
	private final Dimension world;
	
	private final int [] viewport;

	private final int [] prevViewPort;
	private final double [] modelview_matrix;

	private final double [] prev_modelview_matrix; 
	private final double [] projection_matrix;
//	private Rectangle2D.Double viewWindow;
//	private Rectangle2D.Double virtualWindow;

	private final double [] prev_projection_matrix;

	
/*	public ViewPoint2D(Dimension window) 
	{
		this(new Vector2D(0,0), window);
	}*/	
	public ViewPoint2D(Vector2D center, Dimension window, RangedDouble scale, Dimension world)
	{
		this.center = center;

		this.scale = scale;
		
		this.world = world;

		this.viewport = new int [4];
		this.modelview_matrix = new double [16];
		this.projection_matrix = new double [16];
		
		this.prevViewPort = new int [4];
		this.prev_modelview_matrix = new double [16];
		this.prev_projection_matrix = new double [16];
}
	
	public ViewPoint2D()
	{
		this.center = Vector2D.ZERO();
		this.scale = new RangedDouble( 0,  0, 0 );
		this.world = new Dimension(0,0);
		this.viewport = new int [4];
		this.modelview_matrix = new double [16];
		this.projection_matrix = new double [16];
		
		this.prevViewPort = new int [4];
		this.prev_modelview_matrix = new double [16];
		this.prev_projection_matrix = new double [16];
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
//	public Rectangle2D.Double getViewWindow() { return viewWindow; }

/*	public int scaleDistance(double dist) {
		return (int)(dist/scale);
	}*/

	public int getPortWidth() { return  FastMath.floor(viewport[2]); }
	public int getPortHeight() { return FastMath.floor(viewport[3]); }

	public Vector2D getCenter() { return center; }

	public double getScale() { return scale.getDouble(); }

	public double getMinScale() { return scale.getMin(); }
	public double getMaxScale() { return scale.getMax(); }
	
	public void copyFrom(IBeholder viewPoint)
	{
		if(!(viewPoint instanceof ViewPoint2D))
			throw new IllegalArgumentException("Must be copied from " + this.getClass() + " type.");
		
		ViewPoint2D vp = (ViewPoint2D) viewPoint; 
		this.center.setxy( vp.getCenter().x(), vp.getCenter().y() );
		this.scale.setMin( vp.getMinScale());
		this.scale.setMax( vp.getMaxScale());
		this.scale.setDouble( vp.getScale());
		
		this.world.width = vp.world.width;
		this.world.height = vp.world.height;
	
		for(int idx = 0; idx < 4; idx ++)
			viewport[idx] = vp.viewport[idx];
	}	

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append("center: ").append(center).append(" ")
			.append("scale: [").append(scale).append("] ")
			.toString();
		
	}

	/**
	 * Updates view point transformation aspects from GL viewpoint, model-view and 
	 * projection matrices.
	 * @param viewport (4 elements)
	 * @param modelview_matrix (16 elements)
	 * @param projection_matrix (16 elements)
	 */
	public void updatePointModel(int[] viewport, double [] modelview_matrix, double [] projection_matrix)
	{
		
		for(int idx = 0; idx < 4; idx ++) {
			prevViewPort[idx] = this.viewport[idx];
			this.viewport[idx] = viewport[idx];
		}
		for(int idx = 0; idx < 16; idx ++) {
			prev_modelview_matrix[idx] = this.modelview_matrix[idx];
			this.modelview_matrix[idx] = modelview_matrix[idx];
		}
		for(int idx = 0; idx < 16; idx ++) {
			prev_projection_matrix[idx] = this.projection_matrix[idx];
			this.projection_matrix[idx] = projection_matrix[idx];
		}
	}
	
	/**
	 * Converts specified point in canvas coordinate into world coordinates vector.
	 * Depends on the last invokation of {@link #setPointModel(int[], double[], double[])}
	 * method.
	 * @param pickPoint
	 * @return
	 */
	public Vector2D toWorldCoordinates(Point pickPoint) 
	{
		if(pickPoint == null)
			return null;
		int realy = 0;// inverting y coordinate
		double wcoord[] = new double[4];// wx, wy, wz;// returned xyz coords

		double x = pickPoint.getX();
		double y = pickPoint.getY();

		
		/* note viewport[3] is height of window in pixels */
		realy = viewport[3] - (int) y;
		glu.gluUnProject(x, realy, 0.0, modelview_matrix, 0, projection_matrix, 0, viewport, 0, wcoord, 0);
		return Vector2D.R(wcoord[0], wcoord[1]);
//		return new Vector2D((wcoord[0]+viewPoint.getCenter().x)*viewPoint.getHeight(), (wcoord[1]+viewPoint.getCenter().y)*viewPoint.getHeight());
	}

	public int [] getViewport() { return viewport; }
	public int[] getPrevViewport() { return prevViewPort; }
	
	public double [] getModelViewMatrix() { return modelview_matrix; }
	public double [] getPrevModelViewMatrix() { return prev_modelview_matrix; }
	
	public double [] getProjectiionMatrix() { return projection_matrix; }
	public double [] getPrevProjectionMatrix() { return prev_projection_matrix; }
}
