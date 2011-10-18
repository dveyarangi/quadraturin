package yarangi.numbers;

import java.util.Random;

public class RandomUtil 
{

	private static Random random = new Random();
	
	public static int getRandomInt(int n)
	{ 
		return random.nextInt(n) ;
	}

	public static double getRandomDouble(double d) {
		return d*random.nextDouble();
	}
	
	public static float getRandomGaussian(float center, float scale)
	{
		return center + scale * (float)random.nextGaussian();
	} 
	public static double getRandomGaussian(double center, double scale)
	{
		return center + scale * random.nextGaussian();
	} 
	
	public static boolean oneOf(int num)
	{
		return getRandomInt(num) == 0;
	}
}
