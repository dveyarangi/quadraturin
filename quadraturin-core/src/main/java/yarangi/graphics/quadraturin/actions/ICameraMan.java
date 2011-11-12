package yarangi.graphics.quadraturin.actions;

import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.objects.Behavior;

public interface ICameraMan extends Behavior <Scene>
{
	public abstract boolean behave(double time, Scene scene, boolean isVisible);

	public abstract void moveRight();

	public abstract void moveLeft();

	public abstract void moveUp();

	public abstract void moveDown();

	public abstract void zoomIn();

	public abstract void zoomOut();

}