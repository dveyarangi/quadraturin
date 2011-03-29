package yarangi.graphics.quadraturin;

import java.awt.Point;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.interaction.ICollisionManager;
import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.math.RangedDouble;
import yarangi.math.Vector2D;

public class DummyScene extends Scene
{
	
	public DummyScene(String sceneName) 
	{
		super(sceneName, 0, 0, 
			new WorldVeil(0,0)
			{
				public void preDisplay(GL gl) { }
				public void postDisplay(GL gl) { }
				public ICollisionManager createCollisionManager() { return null; }
				protected void initViewPoint(IViewPoint viewPoint) 
				{ 
					ViewPoint2D vp = (ViewPoint2D)viewPoint;
					vp.setCenter(new Vector2D(0,0));
					vp.setHeight(new RangedDouble(-1,-1,-1));
				}
				public void init(GL gl) throws SceneException {}
			}, 
			new UIVeil(0,0)
			{
				public void init(GL gl) throws SceneException {}
			}
		);
	}

	public SceneEntity pick(Vector2D worldLocation, Point canvasLocation)
	{
		return null;
	}

	@Override
	public void bindSceneActions(EventManager voices) { }
}
