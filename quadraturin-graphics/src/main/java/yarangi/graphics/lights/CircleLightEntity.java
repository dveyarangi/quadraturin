package yarangi.graphics.lights;

import java.util.Map;

import yarangi.graphics.quadraturin.objects.ISensorEntity;
import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.graphics.utils.colors.Color;
import yarangi.spatial.AABB;
import yarangi.spatial.ISpatialObject;

public abstract class CircleLightEntity extends SceneEntity implements ISensorEntity
{
	
	private Map <ISpatialObject, Double>  entities; 
	
	private double intensity;
	
	private double radius;
	
	private Color lightColor;

	public CircleLightEntity(AABB aabb, double lightradius, Color color) 
	{
		super(aabb);
			
		this.radius = lightradius;
		
		this.lightColor = color;
	}

	public void setEntities(Map <ISpatialObject, Double>  entities)
	{
		this.entities = entities;
	}
	
	public Map <ISpatialObject, Double>  getEntities() { return entities; }
	
	public Color getColor() { return lightColor; }
	
//	public double getIntensity() { return intensity; }
	
	public double getSensorRadius() { return radius; }

}
