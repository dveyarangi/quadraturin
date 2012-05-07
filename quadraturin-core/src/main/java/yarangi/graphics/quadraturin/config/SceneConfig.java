package yarangi.graphics.quadraturin.config;

import java.awt.Dimension;

import com.spinn3r.log5j.Logger;

import yarangi.graphics.quadraturin.QVoices;
import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.ViewPoint2D;
import yarangi.java.ReflectionUtil;
import yarangi.math.RangedDouble;
import yarangi.math.Vector2D;

public class SceneConfig 
{
	protected String name;
	
	protected String sceneClass;
	protected String engineClass;
	protected String colliderClass;
	protected double frameLength;
	protected EventConfig [] events;
	
	protected int width, height;
	
	protected ViewPointConfig viewpoint;
	protected PhysicsEngineConfig engine;
	protected TerrainConfig terrain;
	
	protected Logger log = IQuadConfig.LOG;

	public String getName() {
		return name;
	}

	public double getFrameLength() {
		return frameLength;
	}

	public EventConfig[] getEvents() {
		return events;
	}

	public int getWidth() { return width; }

	public int getHeight() { return height; }

	public Scene createScene(EkranConfig ekranConfig, QVoices voices) 
	{
		return ReflectionUtil.createInstance(sceneClass, this, ekranConfig, voices);
	}

	public ViewPoint2D createViewpoint() 
	{
		return new ViewPoint2D(
				Vector2D.R(viewpoint.getCenterX(), viewpoint.getCenterY()), 
				null,
				new RangedDouble(viewpoint.getMinZoom(), viewpoint.getInitZoom(), viewpoint.getMaxZoom()),
				new Dimension(getWidth(), getHeight())
				);
	}

	public String getSceneClass() { return sceneClass; }

	public PhysicsEngineConfig getEngineConfig()
	{
		return engine;
	}
	
	public TerrainConfig getTerrainConfig() { return terrain; }
}
