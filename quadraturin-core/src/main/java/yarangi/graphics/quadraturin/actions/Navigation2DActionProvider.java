package yarangi.graphics.quadraturin.actions;

import yarangi.graphics.quadraturin.ViewPoint2D;
import yarangi.graphics.quadraturin.config.ActionBinding;
import yarangi.graphics.quadraturin.config.InputConfig;
import yarangi.graphics.quadraturin.config.QuadConfigFactory;
import yarangi.graphics.quadraturin.events.UserActionEvent;
import yarangi.graphics.quadraturin.events.UserActionListener;

/**
 * Default navigation actions.
 * 
 * @author Dve Yarangi
 */
public class Navigation2DActionProvider extends UIProvider implements UserActionListener
{
	
	public static final String NAV_STRAFE_LEFT = "quadraturin.navigation.scroll-left";
	public static final String NAV_STRAFE_RIGHT = "quadraturin.navigation.scroll-right";
	public static final String NAV_STRAFE_UP = "quadraturin.navigation.scroll-upt";
	public static final String NAV_STRAFE_DOWN = "quadraturin.navigation.scroll-down";
	public static final String NAV_ZOOM_OUT = "quadraturin.navigation.zoom-out";
	public static final String NAV_ZOOM_IN = "quadraturin.navigation.zoom-in";
	
	final double scrollStep;
	final double scaleStep;
	
	public Navigation2DActionProvider(final ViewPoint2D vp)
	{
		super(vp);
		InputConfig config = QuadConfigFactory.getInputConfig();
		
		scrollStep = config.getScrollStep();
		scaleStep = config.getScaleStep();
		
		for(ActionBinding binding : config.getBinding())
		{
			if(NAV_STRAFE_LEFT.equals(binding.getActionId()))
				addAction(binding.getKeyId(), new Action() {
					public void act(UserActionEvent event) { vp.getCenter().x += scrollStep/vp.getHeight(); }}
				);
			else
			if(NAV_STRAFE_RIGHT.equals(binding.getActionId()))
				addAction(binding.getKeyId(), new Action() {
					public void act(UserActionEvent event) { vp.getCenter().x -= scrollStep/vp.getHeight(); }}
				);
			else
			if(NAV_STRAFE_UP.equals(binding.getActionId()))
				addAction(binding.getKeyId(), new Action() {
					public void act(UserActionEvent event) { vp.getCenter().y -= scrollStep/vp.getHeight(); }}
				);
			else
			if(NAV_STRAFE_DOWN.equals(binding.getActionId()))
				addAction(binding.getKeyId(), new Action() {
					public void act(UserActionEvent event) { vp.getCenter().y += scrollStep/vp.getHeight(); }}
				);
			else
			if(NAV_ZOOM_IN.equals(binding.getActionId()))
				addAction(binding.getKeyId(), new Action() {
					public void act(UserActionEvent event) { vp.setHeight(vp.getHeight() * scaleStep); }}
				);
			else
			if(NAV_ZOOM_OUT.equals(binding.getActionId()))
				addAction(binding.getKeyId(), new Action() {
					public void act(UserActionEvent event) { vp.setHeight(vp.getHeight() / scaleStep); }}
				);

		}
	}
}