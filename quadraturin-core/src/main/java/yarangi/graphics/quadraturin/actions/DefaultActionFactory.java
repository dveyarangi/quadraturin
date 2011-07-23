package yarangi.graphics.quadraturin.actions;

import java.util.Map;

import yarangi.graphics.quadraturin.IViewPoint;
import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.ViewPoint2D;
import yarangi.graphics.quadraturin.config.InputConfig;
import yarangi.graphics.quadraturin.config.QuadConfigFactory;
import yarangi.graphics.quadraturin.events.UserActionEvent;

public class DefaultActionFactory 
{
	public static Map <String, IAction> fillNavigationActions(final Scene scene, Map <String, IAction> actions, IViewPoint viewPoint)
	{
		final ViewPoint2D vp = (ViewPoint2D) viewPoint;
		InputConfig config = QuadConfigFactory.getConfig().getInputConfig();
		
		final double scrollStep = config.getScrollStep();
		final double scaleStep = config.getScaleStep();
		
		
		
		actions.put("scroll-right", new IAction() {
			public void act(UserActionEvent event) { vp.getCenter().x -= scrollStep/vp.getScale(); }}
			);
		actions.put("scroll-left", new IAction() {
			public void act(UserActionEvent event) { vp.getCenter().x += scrollStep/vp.getScale(); }}
		);
		actions.put("scroll-up", new IAction() {
			public void act(UserActionEvent event) { vp.getCenter().y -= scrollStep/vp.getScale(); }}
		);
		actions.put("scroll-down", new IAction() {
			public void act(UserActionEvent event) { vp.getCenter().y += scrollStep/vp.getScale(); }}
		);
		actions.put("zoom-in", new IAction() {
			public void act(UserActionEvent event) {
				double s = vp.getScale() * scaleStep;
				vp.setScale(s > vp.getMinScale() ? s : vp.getMinScale()); 
				scene.setFrameLength(vp.getScale()*2);
			}}
		);
		actions.put("zoom-out", new IAction() {
			public void act(UserActionEvent event) {
				double s = vp.getScale() / scaleStep;
				vp.setScale(s < vp.getMaxScale() ? s : vp.getMaxScale());
				scene.setFrameLength(vp.getScale()*2);
			 }}
		);

		return actions;
	}
}
