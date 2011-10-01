package yarangi.graphics.quadraturin.terrain;

import yarangi.graphics.colors.Color;
import yarangi.graphics.quadraturin.simulations.Body;
import yarangi.graphics.quadraturin.simulations.IPhysicalObject;
import yarangi.spatial.Area;

public class Tile implements ITile <Color>, IPhysicalObject
{
	
	private byte [] pixels;
	
	private int pixelCount;
	
	private double cx, cy;
	
	private double tilewidth;
	
	private double pixelsize;
	
	private int pixelNum;
	
	private int textureId = -1;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param pixels array of pixel color components (r,g,b,a); size = width*height*4
	 */
	public Tile(double cx, double cy, int size, double pixelsize)
	{
		
		this.cx = cx;
		this.cy = cy;
		
		this.pixelsize = pixelsize;
		
		this.pixels = new byte[size*size*4];
		
		this.pixelNum = size;
		
		pixelCount = 0;
		for(int i = 0; i < pixels.length/4; i += 4)
			if(pixels[i] != 0 || pixels[i+1] != 0 || pixels[i+2] != 0 || pixels[i+3] != 0)
				pixelCount ++;
			
	}
	
//	protected final int getCoordOffset(double x, double y)
//	{
//		return (int)FastMath.toGrid(x-cx, pixelsize) + (int)(pixelNum * FastMath.toGrid(y-cy, pixelsize));
//	}
	
	protected final boolean hasColor(int offset)
	{
		return pixels[offset] != 0 || pixels[offset+1] != 0 || pixels[offset+2] != 0 || pixels[offset+3] != 0;
	}
	
	@Override
	public Color at(int x, int y)
	{
		int offset = 4 * (x + pixelNum * y);
		return new Color(pixels[offset], pixels[offset+1], pixels[offset+2], pixels[offset+3]);
	}

	@Override
	public void put(Color p, int x, int y)
	{
		int offset = 4 * (x + pixelNum * y);
			
		if(hasColor(offset))
			pixelCount --;
		
		pixels[offset] = p.getRedByte();
		pixels[offset+1] = p.getGreenByte();
		pixels[offset+2] = p.getBlueByte();
		pixels[offset+3] = p.getAlphaByte();
		
		if(hasColor(offset))
			pixelCount ++;
	}
	
	public void remove(int x, int y)
	{
		int offset = 4 * (x + pixelNum * y);
		
		if(hasColor(offset))
			pixelCount --;
		
		pixels[offset] = pixels[offset+1] = pixels[offset+2] = pixels[offset+3] = 0;
	}
	
	public void subMask(int ioffset, int joffset, byte [] mask)
	{
		int minI = Math.max( 0, ioffset );
		int maxI = Math.min( pixelNum, pixelNum + ioffset );
		int minJ = Math.max( 0, joffset );
		int maxJ = Math.min( pixelNum, pixelNum + joffset );
		int offset;
		boolean hadColor;
		for(int i = minI; i < maxI; i ++)
			for(int j = minJ; j < maxJ; j ++)
			{
				offset = 4 * (i + pixelNum * j);
				
				hadColor = hasColor(offset);
				pixels[offset] -= mask[4 * (i - ioffset + pixelNum * (j - joffset))];
				if(pixels[offset] < 0)
					pixels[offset] = 0;
				
				if(hadColor && !hasColor(offset))
					pixelCount --;
			}
	}
	
	public void addMask(int ioffset, int joffset, byte [] mask)
	{
		int minI = Math.max( 0, ioffset );
		int maxI = Math.min( pixelNum, pixelNum + ioffset );
		int minJ = Math.max( 0, joffset );
		int maxJ = Math.min( pixelNum, pixelNum + joffset );
		int offset;
		boolean hadColor;
		for(int i = minI; i < maxI; i ++)
			for(int j = minJ; j < maxJ; j ++)
			{
				offset = 4 * (i + pixelNum * j);
				hadColor = hasColor(offset);
				pixels[offset] += mask[4 * (i - ioffset + pixelNum * (j - joffset))];
				if(pixels[offset] > 255)
					pixels[offset] = (byte)255;
				
				if(!hadColor && hasColor(offset))
					pixelCount ++;
			}
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

	public void setTextureId(int id)
	{
		this.textureId = id;
	}
	
	public int getTextureId() { return textureId; }
	public boolean hasTexture() { return textureId != -1; }

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
		return pixelCount != 0;
	}

	@Override
	public int getSize()
	{
		return pixelNum;
	}

}
