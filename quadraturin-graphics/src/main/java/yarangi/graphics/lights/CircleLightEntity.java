package yarangi.graphics.lights;

import java.util.Map;

import yarangi.graphics.colors.Color;
import yarangi.graphics.quadraturin.objects.ISensorEntity;
import yarangi.graphics.quadraturin.objects.SceneEntity;
import yarangi.spatial.AABB;
import yarangi.spatial.IAreaChunk;
import yarangi.spatial.ISpatialObject;

public abstract class CircleLightEntity extends SceneEntity implements ISensorEntity
{
	
	private Map <IAreaChunk, ISpatialObject> entities; 
	
	private double intensity;
	
	private double radius;
	
	private Color lightColor;

	public CircleLightEntity(AABB aabb, double lightradius, Color color) 
	{
		super(aabb);
			
		this.radius = lightradius;
		
		this.lightColor = color;
	}

	public void setEntities(Map <IAreaChunk, ISpatialObject>  entities)
	{
		this.entities = entities;
	}
	
	public Map <IAreaChunk, ISpatialObject> getEntities() { return entities; }
	
	public Color getColor() { return lightColor; }
	
//	public double getIntensity() { return intensity; }
	
	public double getSensorRadius() { return radius; }

}
