package yarangi.graphics.quadraturin.config;

import java.awt.Dimension;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.QuadVoices;
import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.ViewPoint2D;
import yarangi.graphics.quadraturin.simulations.IPhysicsEngine;
import yarangi.java.ReflectionUtil;
import yarangi.math.RangedDouble;
import yarangi.math.Vector2D;

public class SceneConfig 
{
	protected String name;
	
	protected String sceneClass;
	protected String engineClass;
	protected int frameLength;
	protected EventConfig [] events;
	
	protected int width, height;
	
	protected ViewPointConfig viewpoint;
	
	protected Logger log = Logger.getLogger(SceneConfig.class);

	public String getName() {
		return name;
	}

	public int getFrameLength() {
		return frameLength;
	}

	public EventConfig[] getEvents() {
		return events;
	}

	public int getWidth() { return width; }

	public int getHeight() { return height; }

	public Scene createScene(QuadVoices voices) 
	{
		if(sceneClass == null)
			throw new ConfigException("Scene class is not set.");
		return ReflectionUtil.createInstance(sceneClass, this, voices);
	}

	public IPhysicsEngine createEngine() 
	{
		if(engineClass == null)
			return null;
		return ReflectionUtil.createInstance(engineClass);
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
}
