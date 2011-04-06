package yarangi.graphics.quadraturin.actions;

import java.util.Map;

import yarangi.graphics.quadraturin.IViewPoint;
import yarangi.graphics.quadraturin.ViewPoint2D;
import yarangi.graphics.quadraturin.config.InputConfig;
import yarangi.graphics.quadraturin.config.QuadConfigFactory;
import yarangi.graphics.quadraturin.events.UserActionEvent;

public class DefaultActionFactory 
{
	public static Map <String, Action> createNavigatationActions(Map <String, Action> actions, IViewPoint viewPoint)
	{
		final ViewPoint2D vp = (ViewPoint2D) viewPoint;
		InputConfig config = QuadConfigFactory.getInputConfig();
		
		final double scrollStep = config.getScrollStep();
		final double scaleStep = config.getScaleStep();
		
		
		
		actions.put("scroll-right", new Action() {
				public void act(UserActionEvent event) { vp.getCenter().x += scrollStep/vp.getHeight(); }}
			);
		actions.put("scroll-left", new Action() {
			public void act(UserActionEvent event) { vp.getCenter().x -= scrollStep/vp.getHeight(); }}
		);
		actions.put("scroll-up", new Action() {
			public void act(UserActionEvent event) { vp.getCenter().y -= scrollStep/vp.getHeight(); }}
		);
		actions.put("scroll-down", new Action() {
			public void act(UserActionEvent event) { vp.getCenter().y += scrollStep/vp.getHeight(); }}
		);
		actions.put("zoom-in", new Action() {
			public void act(UserActionEvent event) { vp.setHeight(vp.getHeight() * scaleStep); }}
		);
		actions.put("zoom-out", new Action() {
			public void act(UserActionEvent event) { vp.setHeight(vp.getHeight() / scaleStep); }}
		);

		return actions;
	}
}
