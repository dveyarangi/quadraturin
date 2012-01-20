package yarangi.graphics.quadraturin.terrain;

import javax.media.opengl.GL;

import yarangi.graphics.colors.Color;
import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.objects.Behavior;
import yarangi.graphics.quadraturin.objects.IEntity;
import yarangi.graphics.quadraturin.objects.ISensor;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.quadraturin.simulations.Body;
import yarangi.graphics.quadraturin.simulations.IPhysicalObject;
import yarangi.spatial.Area;
import yarangi.spatial.PointArea;

public class Bitmap implements IPhysicalObject, IEntity
{
	
	private byte [] pixels;
	
	private int pixelCount;
	
	private PointArea area;
	
	private int size;
	
	private int textureId = -1;

	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param pixels array of pixel color components (r,g,b,a); 
	 */
	public Bitmap(double cx, double cy, int size)
	{
		
		area = new PointArea(cx, cy);
		
		this.pixels = new byte[size*size*4];
		
		this.size = size;
		
		pixelCount = 0;
		for(int i = 0; i < pixels.length; i += 4)
			pixels[i] = 0;
			/*if(pixels[i] != 0 || pixels[i+1] != 0 || pixels[i+2] != 0 || pixels[i+3] != 0)
				pixelCount ++;*/
			
	}
	

	private final int offset(int x, int y) 
	{
		return 4 * (x + size * y);
	}
	
	
	protected final boolean hasColor(int offset)
	{
		return pixels[offset] != 0 || pixels[offset+1] != 0 || pixels[offset+2] != 0 || pixels[offset+3] != 0;
	}
	
	public Color at(int x, int y)
	{
		int offset = offset(x, y);
		return new Color(pixels[offset], pixels[offset+1], pixels[offset+2], pixels[offset+3]);
	}
	
	public void put(Color p, int x, int y)
	{
		int offset = offset(x, y);
			
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
		int offset = offset(x, y);
		
		if(hasColor(offset))
			pixelCount --;
		
		pixels[offset] = pixels[offset+1] = pixels[offset+2] = pixels[offset+3] = 0;
	}
	

	public boolean subMask(int ioffset, int joffset, int maskWidth, byte [] mask)
	{
		int minI = Math.max( -ioffset, 0);
		int maxI = Math.min( maskWidth - ioffset, size);
		int minJ = Math.max( -joffset, 0);
		int maxJ = Math.min( maskWidth - joffset, size);
		int offset;
		boolean changed = false;
		for(int i = minI; i < maxI; i ++)
			for(int j = minJ; j < maxJ; j ++)
			{
				offset = offset(i, j);
//				if(offset < 0 || offset >= pixels.length)
//					continue;
				int maskOffset = 4*((i+ioffset) + maskWidth*(j+joffset));
				if(maskOffset < 0 || maskOffset >= mask.length)
					continue;			
				if(mask[maskOffset] != 0 || mask[maskOffset+1] != 0 && mask[maskOffset+2] != 0 || mask[maskOffset+3] != 0 )
					continue;

	//			pixels[offset] += mask[maskOffset];
	//			if(pixels[offset] > 255)
	//				pixels[offset] = (byte)255;				
				if(hasColor(offset))
					pixelCount --;
				setPixel(offset, maskOffset, mask);
				
				if(hasColor(offset))
					pixelCount ++;
				
				changed = true;
			}
		return changed;
	}	
	public boolean addMask(int ioffset, int joffset, int maskWidth, byte [] mask)
	{
		int minI = Math.max( -ioffset, 0);
		int maxI = Math.min( maskWidth - ioffset, size);
		int minJ = Math.max( -joffset, 0);
		int maxJ = Math.min( maskWidth - joffset, size);
		int offset;
		boolean changed = false;
//		boolean hadColor;
		for(int i = minI; i < maxI; i ++)
			for(int j = minJ; j < maxJ; j ++)
			{
				offset = offset(i, j);
//				if(offset < 0 || offset >= pixels.length)
//					continue;
				int maskOffset = 4*((i+ioffset) + maskWidth*(j+joffset));
				if(maskOffset < 0 || maskOffset >= mask.length)
					continue;			
				if(mask[maskOffset] == 0 &&  mask[maskOffset+1] == 0 && mask[maskOffset+2] == 0 && mask[maskOffset+3] == 0 )
					continue;

	//			pixels[offset] += mask[maskOffset];
	//			if(pixels[offset] > 255)
	//				pixels[offset] = (byte)255;				
				if(hasColor(offset))
					pixelCount --;
				setPixel(offset, maskOffset, mask);
				if(hasColor(offset))
					pixelCount ++;
				
				
				changed = true;
			}
		return changed;
	}
	
	final private void addPixel(int bitmapOffset, int maskOffset, byte [] mask) 
	{
/*		if(pixels[bitmapOffset]+mask[maskOffset] > 255)
			pixels[bitmapOffset] = (byte)255;
		else
			pixels[bitmapOffset] += mask[maskOffset];
		
		if(pixels[bitmapOffset+1] + mask[maskOffset+1] > 255)
			pixels[bitmapOffset+1] = (byte)255;
		else
			pixels[bitmapOffset+1] += mask[maskOffset+1];
		
		if(pixels[bitmapOffset+2] + mask[maskOffset+2] > 255)
			pixels[bitmapOffset+2] = (byte)255;
		else
			pixels[bitmapOffset+2] += mask[maskOffset+2];
		
		if(pixels[bitmapOffset+3] + mask[maskOffset+3] > 255)
			pixels[bitmapOffset+3] = (byte)255;
		else
			pixels[bitmapOffset+3] += mask[maskOffset+3];*/

	}
	
	final private void subPixel(int bitmapOffset, int maskOffset, byte [] mask) 
	{
		byte temp = (byte)(pixels[bitmapOffset] - mask[maskOffset]);
		if(pixels[bitmapOffset] <= mask[maskOffset])
			pixels[bitmapOffset] = (byte)0;
		else
			pixels[bitmapOffset] -= mask[maskOffset];
		
		if(pixels[bitmapOffset+1] <= mask[maskOffset+1])
			pixels[bitmapOffset+1] = (byte)0;
		else
			pixels[bitmapOffset+1] -= mask[maskOffset+1];
		
		if(pixels[bitmapOffset+2] <= mask[maskOffset+2])
			pixels[bitmapOffset+2] = (byte)0;
		else
			pixels[bitmapOffset+2] -= mask[maskOffset+2];
		
		if(pixels[bitmapOffset+3] <= mask[maskOffset+3])
			pixels[bitmapOffset+3] = (byte)0;
		else
			pixels[bitmapOffset+3] -= mask[maskOffset+3];
	}
	
	final private void setPixel(int bitmapOffset, int maskOffset, byte [] mask) 
	{
		pixels[bitmapOffset] = mask[maskOffset];
		pixels[bitmapOffset+1] = mask[maskOffset+1];
		pixels[bitmapOffset+2] = mask[maskOffset+2];
		pixels[bitmapOffset+3] = mask[maskOffset+3];
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

//	public double getPixelSize() { return pixelsize; }
	@Override
	public Area getArea()
	{
		return area;
	}

	@Override
	public Body getBody()
	{
		return null;
	}

	@Override
	public boolean isAlive()
	{
		return pixelCount != 0;
	}

	public int getSize()
	{
		return size;
	}

	///////////////////////////////////////////////
	// TODO: fix this mess. Create a separate interface for 

	@Override
	public Look getLook()
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void markDead()
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isIndexed()
	{
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void init(GL gl, IRenderingContext context)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public void render(GL gl, double time, IRenderingContext context)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public void destroy(GL gl, IRenderingContext context)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setBehavior(Behavior<?> behavior)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setBody(Body body)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setSensor(ISensor<?> sensor)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public Behavior<?> getBehavior()
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ISensor getSensor()
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean behave(double time, boolean b)
	{
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public int getGroupId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
