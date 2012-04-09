package test;

import java.util.Random;

public class Test {

	public static void main(String ... args)
	{
		
		System.out.println(-6 % 4);
		long start, end;
		int size = 10;
//		int j = 0;
		int i;
		
		Random rand = new Random();
		int N=0;
		int pN = 10000000;
		Vector2D target = new Vector2D(0,0);
		
		double x = 1231, y=53454566;
		for(int j = 0; j < 100; j ++)
		{
			
			start = System.nanoTime();
			for(i = N; i < pN; i ++)
			{
//				target = new Vector2D(Math.random(),Math.random());
//				x = Math.random();
//				y = Math.random();
				target._add( 1, 1 );
			}
			end = System.nanoTime();
			System.out.println("time (mod): " +(end-start) + " ns");
			start = System.nanoTime();
			for(i = N; i < pN; i ++)
			{
//				target = new Vector2D();
				target.add( 1, 1 );
			}	
			end = System.nanoTime();
			System.out.println("time (div): " +(end-start) + " ns");
				
			end = System.nanoTime();

			
			
			
//			pN = N;
		}
	}
}
