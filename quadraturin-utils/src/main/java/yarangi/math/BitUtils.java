package yarangi.math;

public strictfp class BitUtils {
	
	public static final double LOG_2_COEF = 1/Math.log(2);
	
	public static int po2Ceiling(int number)
	{
		return (int)Math.pow(2, Math.ceil((Math.log(number)*LOG_2_COEF)));
	}
	
	/**
	 * TODO: incorrect (some floating point precision problems)
	 * @param number
	 * @return
	 */
	public  static int po2Floor(int number)
	{
		return (int)Math.pow(2, Math.floor((Math.log(number)*LOG_2_COEF)));
	}
	
	public static void main(String ... args)
	{
		for(int i = 0; i <= 128; i ++)
			System.out.println(i + " : " + po2Ceiling(i) + " : " + po2Floor(i));
	}
}
