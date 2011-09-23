package yarangi.graphics.quadraturin.terrain;

import yarangi.graphics.quadraturin.simulations.Body;
import yarangi.graphics.quadraturin.simulations.IPhysicalObject;
import yarangi.spatial.Area;

public class Tile  implements IPhysicalObject
{
	
	private byte [] pixels;
	
	private int pixelCount;
	
	private double x, y;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param pixels array of pixel color components (r,g,b,a); size = width*height*4
	 */
	public Tile(double x, double y, byte [] pixels)
	{
		
		this.x = x;
		this.y = y;
		
		this.pixels = pixels;
		pixelCount = 0;
		for(int i = 0; i < pixels.length/4; i += 4)
			if(pixels[i] != 0 || pixels[i+1] != 0 || pixels[i+2] != 0 || pixels[i+3] != 0)
				pixelCount ++;
			
	}
	
	public byte [] getPixels()
	{
		return pixels;
	}
	
	public boolean isEmpty()
	{
		return pixelCount == 0;
	}
	
	public int getPixelCount()
	{
		return pixelCount;
	}

	@Override
	public Area getArea()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPassId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPassId(int id)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Body getBody()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAlive()
	{
		return pixelCount == 0;
	}

}
