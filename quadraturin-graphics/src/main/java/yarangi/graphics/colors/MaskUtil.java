package yarangi.graphics.colors;

public class MaskUtil
{
	public static byte [] createCircleMask(float radius, Color color, boolean inverse)
	{
		int width = (int)(2*radius);
		byte [] mask = new byte [4 * width * width];
		int offset;
		float radiusSquare = radius*radius;
		
		for(int i = 0; i < width; i ++)
			for(int j = 0; j < width; j ++)
			{
				offset = 4 * ( i + width*j );  
				if((i-radius)*(i-radius) + (j-radius)*(j-radius) <= radiusSquare)
				{
					if(inverse) {
						mask[offset]   = 0;
						mask[offset+1] = 0;
						mask[offset+2] = 0;
						mask[offset+3] = 0;

					}else {
					mask[offset]   = color.getRedByte();
					mask[offset+1] = color.getGreenByte();
					mask[offset+2] = color.getBlueByte();
					mask[offset+3] = color.getAlphaByte();
					}
				}
				else
				{
					if(inverse) {
						mask[offset]   = color.getRedByte();
						mask[offset+1] = color.getGreenByte();
						mask[offset+2] = color.getBlueByte();
						mask[offset+3] = color.getAlphaByte();

					}else {
						mask[offset]   = 0;
						mask[offset+1] = 0;
						mask[offset+2] = 0;
						mask[offset+3] = 0;
					}
				}
			}
		
		return mask;
	}
	
	public static byte [] createSquareMask(int width, Color color)
	{
		int size = 4 * width * width;
		byte [] mask = new byte [size];
		
		for(int offset = 0; offset < size; offset += 4)
		{
			mask[offset]   = color.getRedByte();
			mask[offset+1] = color.getGreenByte();
			mask[offset+2] = color.getBlueByte();
			mask[offset+3] = color.getAlphaByte();
		}
		
		return mask;
	}
}
