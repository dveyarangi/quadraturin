package yarangi.graphics.quadraturin.config;

import java.awt.Dimension;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.QuadVoices;
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
	
	protected Logger log = Logger.getLogger("q-config");

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

	public Scene createScene(QuadVoices voices) 
	{
		return ReflectionUtil.createInstance(sceneClass, this, voices);
	}

	public ViewPoint2D createViewpoint() 
	{
		return new ViewPoint2D(
				new Vector2D(viewpoint.getCenterX(), viewpoint.getCenterY()), 
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
