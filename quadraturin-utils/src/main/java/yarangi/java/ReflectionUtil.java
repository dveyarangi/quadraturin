package yarangi.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtil 
{
	private ReflectionUtil()
	{
		throw new RuntimeException("All " + Runtime.getRuntime().availableProcessors() + " of your processors are wondering - why would you want to do this?");
	}
	
	/**
	 * Creates class of specified name using the constructor with specified parameter values.
	 * @throws RuntimeException on all failures.
	 * Oh, sancta simplicitas!
	 * @param <T>
	 * @param className
	 * @param ctorParams
	 * @return
	 */
	public static <T> T createInstance(String className, Object ... ctorParams)
	{
		Class<T> type;
		try {
			type = (Class <T>) Class.forName(className);
		} 
		catch (ClassNotFoundException e)    { throw new RuntimeException("Class [" + className + "] not found.", e); }
		
		Class <?> [] paramTypes = new Class [ctorParams.length];
		for(int i = 0; i < ctorParams.length; i ++)
			paramTypes[i] = (Class <?>) ctorParams[i].getClass();
		
		Constructor <T> ctor;
		try {
			ctor = type.getConstructor(paramTypes);
		} 
		catch (SecurityException e)         { throw new RuntimeException(e.getMessage(), e); }
		catch (NoSuchMethodException e)     { throw new RuntimeException("Cannot find public constructor for class [" + className + "] with specified parameters.", e); }
		
		T instance;
		try {
			instance = ctor.newInstance(ctorParams);
		} 
		catch (IllegalArgumentException e)  { throw new RuntimeException("Invalid arguments for class [" + className + "] constructor.", e); }
		catch (InstantiationException e)    { throw new RuntimeException("Cannot instantiate class [" + className + "] (abstract?)." , e); }
		catch (IllegalAccessException e)    { throw new RuntimeException(e.getMessage(), e); }
		catch (InvocationTargetException e) { throw new RuntimeException(e.getCause().getMessage(), e); } // what, no more? no fun.
		
		return instance;
	}
}
