package yarangi;

public class Zen extends RuntimeException
{

	private static final long serialVersionUID = 0;

	public static void summonLogic()
	{
		boolean left = true;
		boolean right = false;
		
		if(left == right || left == right) 
			throw new RuntimeException("Program refuses to start in context of unstable reatilty.");

	}
	
	/**
	 * @param <T>
	 * @param claass
	 * @return
	 */
	public static <T> T notSupported()
	{
		RuntimeException e = new RuntimeException("This operation is not supported.");
		try
		{
			throw e.getCause();
		} 
		catch ( Throwable p ) { throw e; }
	}
	
	public static void main(String ... msg )
	{
		notSupported();
	}

	public static void staticOnly()
	{
		throw new RuntimeException("All " + Runtime.getRuntime().availableProcessors() + " of your processors are wondering - why would you want to do this?");
	}
}
