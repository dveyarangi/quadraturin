package yarangi.graphics.quadraturin;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.actions.IAction;
import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.graphics.quadraturin.simulations.IPhysicsEngine;
import yarangi.math.RangedDouble;
import yarangi.math.Vector2D;

public class DummyScene extends Scene
{
	
	public DummyScene(String sceneName) 
	{
		super(sceneName,
			new WorldVeil(0,0)
			{
				public void preDisplay(GL gl) { }
				public void postDisplay(GL gl) { }
				protected void initViewPoint(IViewPoint viewPoint) 
				{ 
					ViewPoint2D vp = (ViewPoint2D)viewPoint;
					vp.setCenter(new Vector2D(0,0));
					vp.setScale(new RangedDouble(1,1,1));
				}
				@Override
				public IPhysicsEngine createPhysicsEngine() { return null; }

			}, 
			new UIVeil(0,0)
			{

			},
			 0, 0, 0
		);
	}

	public SceneEntity pick(Vector2D worldLocation, Point canvasLocation)
	{
		return null;
	}

	@Override
	public Map<String, IAction> getActionsMap() { return new HashMap <String, IAction> (); }
}
