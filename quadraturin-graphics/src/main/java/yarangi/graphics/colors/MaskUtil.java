package yarangi.graphics.colors;

public class MaskUtil
{
	public static byte [] createCircleMask(int radius)
	{
		int width = 2*radius;
		byte [] mask = new byte [4 * width * width];
		int offset;
		int radiusSquare = radius*radius;
		
		for(int i = 0; i < width; i ++)
			for(int j = 0; j < width; j ++)
			{
				offset = 4 * ( i + width*j );  
				if(i*i + j*j < radiusSquare)
				{
					mask[offset]   = (byte)255;
					mask[offset+1] = (byte)255;
					mask[offset+2] = (byte)255;
					mask[offset+3] = (byte)255;
				}
				else
				{
					mask[offset]   = 0;
					mask[offset+1] = 0;
					mask[offset+2] = 0;
					mask[offset+3] = 0;
				}
			}
		
		return mask;
	}
}
