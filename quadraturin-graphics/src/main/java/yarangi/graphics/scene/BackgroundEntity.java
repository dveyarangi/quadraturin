package yarangi.graphics.scene;

import yarangi.graphics.quadraturin.objects.Dummy;
import yarangi.graphics.quadraturin.objects.SceneEntity;

/**
 * Once collision detection is set up, this may be converted to physical entity.
 *  
 * @author  
 */
public class BackgroundEntity extends SceneEntity 
{

	private static final long serialVersionUID = -734638445941525615L;
	
	private float[] lightPosition = {0.0f, 0.0f, 0.0f, 1.0f};

	
	private double [][] heights;
	
	private double width;
	private double height;
	private double tileSize;
	
	public BackgroundEntity(int width, int height, double tileSize) 
	{
		super();
		
		this.width = tileSize*width;
		this.height = tileSize*height;
		this.tileSize = tileSize;
		
		heights = new double[width][height];
		for(int x = 0; x < width; x ++)
			for(int y = 0; y < height; y ++)
				heights[x][y] = -1;
		setBehavior(Dummy.BEHAVIOR);
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
	
	public float[] getLightPosition() { return lightPosition; }

}
