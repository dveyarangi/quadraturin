package yarangi.math;

public class FastMath 
{
	   private static final int    BIG_ENOUGH_INT   = 16 * 1024;
	   private static final double BIG_ENOUGH_FLOOR = BIG_ENOUGH_INT;
	   private static final double BIG_ENOUGH_ROUND = BIG_ENOUGH_INT + 0.5;
	 
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
}
