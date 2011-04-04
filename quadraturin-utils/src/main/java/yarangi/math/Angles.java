package yarangi.math;

public class Angles
{
	public static final double PI = Math.PI;
	public static final double PI_2 = Math.PI*2;
	public static final double PI_div_2 = Math.PI/2;
	public static final double PI_div_6 = Math.PI/6;
	public static final double PI_div_10 = Math.PI/10;
	public static final double PI_div_20 = Math.PI/20;
	
	public static final double TO_RAD = PI/180.;
	public static final double TO_DEG = 180./PI;
	
	public static double toRadians(double degrees)
	{
		return degrees*TO_RAD;
	}
	
	public static double toDegrees(double radians)
	{
		return radians*TO_DEG;
	}
	
	/**
	 * Compresses the angle into [-180, 180] range.
	 * @param angle
	 * @return
	 */
	public static double normalize(double angle)
	{
		return angle < -180 ? angle+360 : angle > 180 ? angle-360 : angle;
	}
	
	/** 
	 * Adjusts the oldAngle in the closest direction to targetAngle by step.
	 * @param oldAngle
	 * @param targetAngle
	 * @param step
	 * @return
	 */
	public static double stepTo(double oldAngle, double targetAngle, double step)
	{
		double diff = normalize(oldAngle - targetAngle);
		double absDiff = Math.abs(diff);
		
		return normalize(oldAngle + (diff < 0 ? 1 : -1) * (absDiff < step ? absDiff : step));
	}

	/**
	 * Steps the oldAngle towards the targetAngle, making last steps gradually slowing down,
	 * for "softness" effect.
	 * A bit slower than the {@link #stepTo(double, double, double)} operation.
	 * 
	 * Reaches target after (abs(targetAngle-oldAngle) - step*softness) + log b(1/softness) (step*softness)
	 * @param oldAngle
	 * @param targetAngle
	 * @param step initial step size
	 * @param softness
	 * @return
	 */
	public static double stepSoftly(double oldAngle, double targetAngle, double step, double softness)
	{
		double diff = normalize(oldAngle - targetAngle);
		double absDiff = Math.abs(diff);
		
		double dir = (diff < 0 ? 1 : -1);
		
		if(absDiff <= softness*step)
		{
			double softStep = absDiff / softness;
			if(softStep < 1/(softness*softness))
				return normalize(oldAngle + absDiff);
			return normalize(oldAngle + dir * absDiff / softness);
		}
		
		return normalize(oldAngle + dir * step);
	}
}
