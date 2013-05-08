package yar.quadraturin.actions;

import yar.quadraturin.Scene;
import yar.quadraturin.Scene.IWorker;

public interface ICameraMan extends IWorker
{
	@Override
	public abstract boolean behave(double time, Scene scene, boolean isVisible);

	public abstract void moveRight();

	public abstract void moveLeft();

	public abstract void moveUp();

	public abstract void moveDown();

	public abstract void zoomIn();

	public abstract void zoomOut();
	
	public abstract void moveRelative(double x, double y);
}