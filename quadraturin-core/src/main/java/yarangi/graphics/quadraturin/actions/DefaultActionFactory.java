package yarangi.graphics.quadraturin.actions;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.ViewPoint2D;
import yarangi.graphics.quadraturin.actions.IAction;
import yarangi.graphics.quadraturin.actions.IActionController;
import yarangi.graphics.quadraturin.config.InputConfig;
import yarangi.graphics.quadraturin.config.QuadConfigFactory;
import yarangi.graphics.quadraturin.events.CursorEvent;
import yarangi.graphics.quadraturin.events.UserActionEvent;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.spatial.ISpatialFilter;

public class DefaultActionFactory 
{
	public static IActionController createDefaultController(Scene scene)
	{
		return new DefaultActionController(scene);
	}
	
	static class DefaultActionController implements IActionController 
	{
		
		Map <String, IAction> actions;
		
		public DefaultActionController(Scene scene)
		{
			actions = appendNavActions(new HashMap <String, IAction> (), scene);

		}

		@Override
		public void onCursorMotion(CursorEvent event)
		{
			// TODO Auto-generated method stub
			
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
		public void display(GL gl, double time, RenderingContext context)
		{
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
	public static Map <String, IAction> appendNavActions(Map <String, IAction> actions, Scene scene)
	{
		final ViewPoint2D vp = (ViewPoint2D)scene.getViewPoint();
		InputConfig config = QuadConfigFactory.getConfig().getInputConfig();
		
		final double scrollStep = config.getScrollStep();
		final double scaleStep = config.getScaleStep();
		final double initialFrameLength = scene.getFrameLength();
		
		actions.put("scroll-right", new IAction() {
			public void act(UserActionEvent event) { vp.getCenter().add(-scrollStep/vp.getScale(), 0); }}
			);
		actions.put("scroll-left", new IAction() {
			public void act(UserActionEvent event) { vp.getCenter().add(scrollStep/vp.getScale(), 0); }}
		);
		actions.put("scroll-up", new IAction() {
			public void act(UserActionEvent event) { vp.getCenter().add(0, -scrollStep/vp.getScale()); }}
		);
		actions.put("scroll-down", new IAction() {
			public void act(UserActionEvent event) { vp.getCenter().add(0, scrollStep/vp.getScale()); }}
		);
		actions.put("zoom-in", new IAction() {
			public void act(UserActionEvent event) {
				double s = vp.getScale() * scaleStep;
				vp.setScale(s > vp.getMinScale() ? s : vp.getMinScale()); 
			}}
		);
		actions.put("zoom-out", new IAction() {
			public void act(UserActionEvent event) {
				double s = vp.getScale() / scaleStep;
				vp.setScale(s < vp.getMaxScale() ? s : vp.getMaxScale());
			 }}
		);

		return actions;
	}
	
	public static Map <String, IAction> appendNavActions(final Scene scene, IActionController controller)
	{
		return appendNavActions(controller.getActions(), scene);
	}
	

}
