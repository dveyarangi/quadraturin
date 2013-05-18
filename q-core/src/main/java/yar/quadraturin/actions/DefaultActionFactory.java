package yar.quadraturin.actions;

import java.util.HashMap;
import java.util.Map;

import yar.quadraturin.Camera2D;
import yar.quadraturin.Scene;
import yar.quadraturin.debug.Debug;
import yar.quadraturin.events.UserActionEvent;
import yar.quadraturin.objects.EntityShell;
import yar.quadraturin.objects.IEntity;
import yarangi.spatial.ISpatialFilter;

/**
 * Default user action controller, allows camera strafe and zoom actions.
 * Requires following key mappings in configuration file:
 * scroll-right scroll-left scroll-up scroll-down zoom-in zoom-out
 * 
 * 
 * 
 * @author dveyarangi
 *
 */
public class DefaultActionFactory 
{
	public static ActionController createDefaultController(Scene scene)
	{
		return new DefaultActionController(scene);
	}
	

	
	public static Map <String, IAction> appendNavActions(Map <String, IAction> actions, final ICameraMan mover, Scene scene)
	{
		actions.put("scroll-right", new IAction() {
			@Override
			public void act(UserActionEvent event) { mover.moveRight(); }}
			);
		actions.put("scroll-left", new IAction() {
			@Override
			public void act(UserActionEvent event) { mover.moveLeft(); }}
		);
		actions.put("scroll-up", new IAction() {
			@Override
			public void act(UserActionEvent event) { mover.moveUp(); }}
		);
		actions.put("scroll-down", new IAction() {
			@Override
			public void act(UserActionEvent event) { mover.moveDown(); }}
		);
		actions.put("zoom-in", new IAction() {
			@Override
			public void act(UserActionEvent event) { mover.zoomIn(); }}
		);
		actions.put("zoom-out", new IAction() {
			@Override
			public void act(UserActionEvent event) { mover.zoomOut(); }}
		);

		return actions;
	}
	
	/**
	 * Use this to append default camera actions to custom controller. 
	 * @param scene
	 * @param controller
	 * @return
	 */
	public static Map <String, IAction> appendNavActions(final Scene scene, ActionController controller)
	{
		if(controller.getCameraManager() == null)
		{
			throw new IllegalArgumentException("Action controller must provide camera manager.");
		}
		appendNavActions(controller.getActions(), controller.getCameraManager(), scene);
		
		assert Debug.appendDebugActions( controller.getActions() );
		
		return controller.getActions();
	}
	

}
