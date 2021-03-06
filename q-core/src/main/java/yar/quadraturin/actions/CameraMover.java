package yar.quadraturin.actions;

import yar.quadraturin.Camera2D;
import yar.quadraturin.Scene;
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
	 * @see yar.quadraturin.actions.ICameraMan#behave(double, yar.quadraturin.Scene, boolean)
	 */
	@Override
	public boolean behave(double time, Scene scene, boolean isVisible)
	{

		Vector2D center = viewPoint.getCenter();
		center.setx( (19*center.x() + target.x())/20 );
		center.sety( (19*center.y() + target.y())/20 );

		return false;
	}

	/* (non-Javadoc)
	 * @see yar.quadraturin.actions.ICameraMan#moveRight(yar.quadraturin.ViewPoint2D)
	 */
	@Override
	public void moveRight()
	{
		// System.out.println(viewPoint.getCenter() + " : " + target);
		target.setx( viewPoint.getCenter().x() - scrollStep*viewPoint.getScale() );
	}

	/* (non-Javadoc)
	 * @see yar.quadraturin.actions.ICameraMan#moveLeft(yar.quadraturin.ViewPoint2D)
	 */
	@Override
	public void moveLeft()
	{
		target.setx( viewPoint.getCenter().x() + scrollStep*viewPoint.getScale() );
	}
	/* (non-Javadoc)
	 * @see yar.quadraturin.actions.ICameraMan#moveUp(yar.quadraturin.ViewPoint2D)
	 */
	@Override
	public void moveUp()
	{
		target.sety( viewPoint.getCenter().y() - scrollStep*viewPoint.getScale() );
	}

	/* (non-Javadoc)
	 * @see yar.quadraturin.actions.ICameraMan#moveDown(yar.quadraturin.ViewPoint2D)
	 */
	@Override
	public void moveDown()
	{
		target.sety( viewPoint.getCenter().y() + scrollStep*viewPoint.getScale() );
	}

	/* (non-Javadoc)
	 * @see yar.quadraturin.actions.ICameraMan#zoomIn(yar.quadraturin.ViewPoint2D)
	 */
	@Override
	public void zoomOut()
	{

		double s = viewPoint.getScale() / scaleStep;
		viewPoint.setScale(s < viewPoint.getMaxScale() ? s : viewPoint.getMaxScale());
	}

	/* (non-Javadoc)
	 * @see yar.quadraturin.actions.ICameraMan#zoomOut(yar.quadraturin.ViewPoint2D)
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
		
		double totalMove = Math.hypot(x, y);
		if(totalMove == 0)
			return;
		target.setx( viewPoint.getCenter().x() + 10*(x/totalMove)*viewPoint.getScale() );
		target.sety( viewPoint.getCenter().y() + 10*(y/totalMove)*viewPoint.getScale() );
	}
}
