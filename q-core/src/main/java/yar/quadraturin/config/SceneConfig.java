package yar.quadraturin.config;

import java.awt.Dimension;

import yar.quadraturin.Camera2D;
import yar.quadraturin.QVoices;
import yar.quadraturin.Scene;
import yarangi.java.ReflectionUtil;
import yarangi.math.RangedDouble;
import yarangi.math.Vector2D;

import com.spinn3r.log5j.Logger;

public class SceneConfig 
{
	protected String name;
	
	protected String sceneClass;
	protected String engineClass;
	protected String colliderClass;
	protected double frameLength = 1;
	protected EventConfig [] events;
	
	protected int width, height;
	protected double timeModifier = 1;
	
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
	
	public double getTimeModifier() { return timeModifier; }

	public Scene createScene(EkranConfig ekranConfig, QVoices voices) 
	{
		return ReflectionUtil.createInstance(sceneClass, this, ekranConfig, voices);
	}

	public Camera2D createViewpoint() 
	{
		return new Camera2D(
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
