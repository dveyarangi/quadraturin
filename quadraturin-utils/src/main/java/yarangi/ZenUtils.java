package yarangi;

public class ZenUtils 
{
	public static void summonLogic()
	{
		boolean left = true;
		boolean right = false;
		
		if(left == right || left == right) 
			throw new IllegalStateException("Program refuses to start in context of unstable reatilty.");

	}
	
	public static void methodNotSupported(Class claass)
	{
		throw new IllegalStateException("This method is not supported for class " + claass);
	}
}
