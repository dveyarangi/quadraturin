package yarangi.graphics.quadraturin.actions;

import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.ViewPoint2D;
import yarangi.math.Vector2D;

public class CameraMover implements ICameraMan
{
	
	private Vector2D target; 
	
	private double targetScale;
	
	private Vector2D speed = new Vector2D(0,0);
	
	private static final double FORCE = 0.01;
	private static final double MAX_SPEED = 1;
	
	final double scrollStep = 100;
	final double scaleStep = 0.9;
	
	private ViewPoint2D viewPoint;
	
	public CameraMover(ViewPoint2D viewPoint) 
	{
		this.viewPoint = viewPoint;
		this.targetScale = viewPoint.getScale();
		this.target = new Vector2D(viewPoint.getCenter());
	}
	
	public void setTargetLocation(double x, double y)
	{
		target.setxy( x, y );
	}
	
	public void setTargetScale(double scale)
	{
		this.targetScale = scale;
	}

	/* (non-Javadoc)
	 * @see yarangi.graphics.quadraturin.actions.ICameraMan#behave(double, yarangi.graphics.quadraturin.Scene, boolean)
	 */
	@Override
	public boolean behave(double time, Scene scene, boolean isVisible)
	{
		Vector2D distance = target.minus(viewPoint.getCenter());
		double ds = targetScale - viewPoint.getScale();
		
		double distanceSquare = distance.x()*distance.x() + distance.y()*distance.y();
		Vector2D speed = distance.normal().multiply(FORCE * distanceSquare);
		double fs = FORCE * ds;
		
		double speedScalar = speed.abs();
		if(speedScalar > MAX_SPEED)
		{
			speed.mul(MAX_SPEED/speedScalar);
		}
		
		viewPoint.getCenter().add( speed.mul( time ) );
		return false;
	}

	/* (non-Javadoc)
	 * @see yarangi.graphics.quadraturin.actions.ICameraMan#moveRight(yarangi.graphics.quadraturin.ViewPoint2D)
	 */
	@Override
	public void moveRight()
	{
		System.out.println(viewPoint.getCenter() + " : " + target);
		target.setx( viewPoint.getCenter().x() - scrollStep*viewPoint.getScale() );
	}

	/* (non-Javadoc)
	 * @see yarangi.graphics.quadraturin.actions.ICameraMan#moveLeft(yarangi.graphics.quadraturin.ViewPoint2D)
	 */
	@Override
	public void moveLeft()
	{
		target.setx( viewPoint.getCenter().x() + scrollStep*viewPoint.getScale() );
	}
	/* (non-Javadoc)
	 * @see yarangi.graphics.quadraturin.actions.ICameraMan#moveUp(yarangi.graphics.quadraturin.ViewPoint2D)
	 */
	@Override
	public void moveUp()
	{
		target.sety( viewPoint.getCenter().y() - scrollStep*viewPoint.getScale() );
	}

	/* (non-Javadoc)
	 * @see yarangi.graphics.quadraturin.actions.ICameraMan#moveDown(yarangi.graphics.quadraturin.ViewPoint2D)
	 */
	@Override
	public void moveDown()
	{
		target.sety( viewPoint.getCenter().y() + scrollStep*viewPoint.getScale() );
	}

	/* (non-Javadoc)
	 * @see yarangi.graphics.quadraturin.actions.ICameraMan#zoomIn(yarangi.graphics.quadraturin.ViewPoint2D)
	 */
	@Override
	public void zoomOut()
	{

		double s = viewPoint.getScale() / scaleStep;
		viewPoint.setScale(s < viewPoint.getMaxScale() ? s : viewPoint.getMaxScale());
	}

	/* (non-Javadoc)
	 * @see yarangi.graphics.quadraturin.actions.ICameraMan#zoomOut(yarangi.graphics.quadraturin.ViewPoint2D)
	 */
	@Override
	public void zoomIn()
	{
		double s = viewPoint.getScale() * scaleStep;
		viewPoint.setScale(s > viewPoint.getMinScale() ? s : viewPoint.getMinScale()); 

	}
}