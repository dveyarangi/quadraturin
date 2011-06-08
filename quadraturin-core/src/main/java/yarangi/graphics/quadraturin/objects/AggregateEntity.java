package yarangi.graphics.quadraturin.objects;

import java.util.List;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.SceneVeil;

public abstract class AggregateEntity extends SceneEntity
{
	protected SceneVeil sceneVeil;

	private List <SceneEntity> children;
	
	protected AggregateEntity(SceneVeil sceneVeil) {
		super();
		
		this.sceneVeil = sceneVeil;
	}
	
	public SceneVeil getSceneVeil()
	{
		return sceneVeil;
	}
	
	protected void addChild(SceneEntity entity)
	{
		children.add(entity);
		sceneVeil.addEntity(entity);
	}
	
	protected void removeChild(SceneEntity entity)
	{
		children.remove(entity);
		sceneVeil.removeEntity(entity);
	}
	
	public void display(GL gl, double time, RenderingContext context) { }
	
	public boolean behave(double time, boolean isVisible)
	{
		return behave(time);
	}
	
	public abstract boolean behave(double time); 

	@Override
	public boolean isPickable() { return false; }
}
