package yarangi.graphics.quadraturin.actions;

import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.Camera2D;
import yarangi.math.Vector2D;

public class CameraMover implements ICameraMan
{
	
	private final Vector2D target; 
	
	private double targetScale;
	
	private final Vector2D speed = Vector2D.ZERO();
	
	private static final double FORCE = 0.1;
	private static final double MAX_SPEED = 5;
	
	final double scrollStep = 100;
	final double scaleStep = 0.9;
	
	private final Camera2D viewPoint;
	
	public CameraMover(Camera2D viewPoint) 
	{
		this.viewPoint = viewPoint;
		this.targetScale = viewPoint.getScale();
		this.target = Vector2D.COPY(viewPoint.getCenter());
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
		double ds = /*targetScale - */viewPoint.getScale();
		
		double distanceSquare = distance.x()*distance.x() + distance.y()*distance.y();
		if(distanceSquare < 10*viewPoint.getScale())
			return false;
		Vector2D speed = distance.normal().multiply(FORCE * Math.sqrt( distanceSquare) );
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
		// System.out.println(viewPoint.getCenter() + " : " + target);
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

	@Override
	public void moveRelative(double x, double y) 
	{
		target.setx( viewPoint.getCenter().x() + (scrollStep*x/(x+y))*viewPoint.getScale() );
		target.sety( viewPoint.getCenter().y() + (scrollStep*y/(x+y))*viewPoint.getScale() );
	}
}
