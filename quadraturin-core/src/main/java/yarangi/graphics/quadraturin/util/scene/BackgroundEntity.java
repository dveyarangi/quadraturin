package yarangi.graphics.quadraturin.util.scene;

import yarangi.graphics.quadraturin.interaction.spatial.AABB;
import yarangi.graphics.quadraturin.objects.DummyEntity;
import yarangi.graphics.quadraturin.objects.CompositeSceneEntity;

/**
 * Once collision detection is set up, this may be converted to physical entity.
 *  
 * @author  
 */
public class BackgroundEntity extends CompositeSceneEntity 
{

	private static final long serialVersionUID = -734638445941525615L;
	
	private float[] lightPosition = {0.0f, 0.0f, 0.0f, 1.0f};

	
	private double [][] heights;
	
	private double width;
	private double height;
	private double tileSize;
	
	public BackgroundEntity(int width, int height, double tileSize) 
	{
		super(new AABB(0,0,0,0));
		
		this.width = tileSize*width;
		this.height = tileSize*height;
		this.tileSize = tileSize;
		
		heights = new double[width][height];
		for(int x = 0; x < width; x ++)
			for(int y = 0; y < height; y ++)
				heights[x][y] = -1;
		setBehavior(DummyEntity.DUMMY_BEHAVIOR);
		setLook(new BackgroundLook());
	}
	
	public void setLightPosition(float [] lightPosition) 
	{
		this.lightPosition = lightPosition;
	}
	
	public double [][] getHeightsMap() { return heights; }
	public double getWidth() { return width; }
	public double getHeight() { return height; }
	public double getTileSize() { return tileSize; }

	@Override
	public boolean isPickable() { return false; }
	
	public float[] getLightPosition() { return lightPosition; }

}
