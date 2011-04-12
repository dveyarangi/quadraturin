package yarangi.graphics.lights;

import java.util.Set;

import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.graphics.utils.colors.Color;
import yarangi.spatial.AABB;
import yarangi.spatial.ISpatialObject;

public abstract class CircleLightEntity extends SceneEntity
{
	
	private Set <ISpatialObject> entities; 
	
	private double intensity;
	
	private double radius;
	
	private Color lightColor;

	public CircleLightEntity(AABB aabb, double intensity, Color color) 
	{
		super(aabb);
		
		this.intensity = intensity;
		
		this.radius = intensity / 0.01;
		
		this.lightColor = color;
	}

	public void setEntities(Set <ISpatialObject> entities)
	{
		this.entities = entities;
	}
	
	public Set <ISpatialObject> getEntities() { return entities; }
	
	public Color getColor() { return lightColor; }
	
//	public double getIntensity() { return intensity; }
	
	public double getLightRadius() { return radius; }
}
