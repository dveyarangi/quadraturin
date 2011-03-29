package yarangi.graphics.quadraturin;

import java.awt.Point;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.interaction.ICollisionManager;
import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.math.Vector2D;

public class DummyScene extends Scene
{
	
	public DummyScene(String sceneName, EventManager voices) 
	{
		super(sceneName, 0, 0, voices, 
			new WorldVeil(0,0)
			{
				public void preDisplay(GL gl) { }
				public void postDisplay(GL gl) { }
				public ICollisionManager createCollisionManager() { return null; }
				protected void initViewPoint(IViewPoint viewPoint) { }
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
}
