package yarangi.graphics.quadraturin.actions;

import java.util.HashMap;
import java.util.Map;

import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.ViewPoint2D;
import yarangi.graphics.quadraturin.events.UserActionEvent;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.spatial.ISpatialFilter;

public class DefaultActionFactory 
{
	public static ActionController createDefaultController(Scene scene)
	{
		return new DefaultActionController(scene);
	}
	
	static class DefaultActionController extends ActionController 
	{
		
		Map <String, IAction> actions;
		
		ICameraMan cameraMover;
		
		public DefaultActionController(Scene scene)
		{
			super(scene);
			
			cameraMover = new CameraMover((ViewPoint2D) scene.getViewPoint());
			actions = appendNavActions(new HashMap <String, IAction> (), cameraMover, scene);
		}

		@Override
		public Map<String, IAction> getActions()
		{
			return actions;
		}

		@Override
		public ISpatialFilter<IEntity> getPickingFilter()
		{
			return null;
		}

		@Override
		public ICameraMan getCameraManager()
		{
			return cameraMover;
		}

		@Override
		public Look <ActionController> getLook() {
			return null;
		}
	}
	
	public static Map <String, IAction> appendNavActions(Map <String, IAction> actions, final ICameraMan mover, Scene scene)
	{
		actions.put("scroll-right", new IAction() {
			public void act(UserActionEvent event) { mover.moveRight(); }}
			);
		actions.put("scroll-left", new IAction() {
			public void act(UserActionEvent event) { mover.moveLeft(); }}
		);
		actions.put("scroll-up", new IAction() {
			public void act(UserActionEvent event) { mover.moveUp(); }}
		);
		actions.put("scroll-down", new IAction() {
			public void act(UserActionEvent event) { mover.moveDown(); }}
		);
		actions.put("zoom-in", new IAction() {
			public void act(UserActionEvent event) { mover.zoomIn(); }}
		);
		actions.put("zoom-out", new IAction() {
			public void act(UserActionEvent event) { mover.zoomOut(); }}
		);

		return actions;
	}
	
	public static Map <String, IAction> appendNavActions(final Scene scene, ActionController controller)
	{
		if(controller.getCameraManager() == null)
		{
			throw new IllegalArgumentException("Action controller must provide camera manager.");
		}
		return appendNavActions(controller.getActions(), controller.getCameraManager(), scene);
	}
	

}
