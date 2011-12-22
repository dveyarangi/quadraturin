package yarangi.math;

public class FastMath 
{
	   private static final int    BIG_ENOUGH_INT   = 16 * 1024;
	   private static final double BIG_ENOUGH_FLOOR = BIG_ENOUGH_INT;
	   private static final double BIG_ENOUGH_ROUND = BIG_ENOUGH_INT + 0.5;
	   
	   public static final double ROOT_2 = Math.sqrt(2);
	   public static final double ROOT_3 = Math.sqrt(3);
	 
	   public static int floor(double x) {
	      return (int) (x + BIG_ENOUGH_FLOOR) - BIG_ENOUGH_INT;
	   }
	 
	   public static int round(double x) {
	      return (int) (x + BIG_ENOUGH_ROUND) - BIG_ENOUGH_INT;
	   }
	 
	   public static int ceil(double x) {
	       return BIG_ENOUGH_INT - (int)(BIG_ENOUGH_FLOOR-x); // credit: roquen
	   }
	   
	   public static int toGrid(int val, int cell)
	   {
		   return val - val % cell;
		   // return val/cell * cell; // embrace eternity!
	   }
	   public static double toGrid(double val, double cell)
	   {
		   return round(val/cell)*cell;
	   }
	   public static float toGrid(float val, float cell)
	   {
		   return round(val/cell)*cell;
	   }
	   
	   public static int toGrid(double val, int cell)
	   {
		   return round(val/cell)*cell;
	   }
	   public static int toGrid(float val, int cell)
	   {
		   return round(val/cell)*cell;
	   }

	public static double powOf2(double d)
	{
		return d*d;
	}
}
