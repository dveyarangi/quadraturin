package yarangi.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import yarangi.Zen;

public class ReflectionUtil 
{
	private ReflectionUtil()
	{
		Zen.staticOnly();
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
		
		Class <?> [] paramTypes = new Class [ctorParams.length];
		for(int i = 0; i < ctorParams.length; i ++)
			paramTypes[i] = getClass(ctorParams[i]);
		
		// TODO: also super class permutations check? :)
		
		return createInstance(className, ctorParams, paramTypes);
	}
	
	public static <T> T createInstance(String className, Object [] ctorParams, Class<?> [] paramTypes)
	{
		T instance;
		
		try {
			Class<T> type = (Class <T>) Class.forName(className);

			Constructor <T> ctor = type.getConstructor(paramTypes);
		
			instance = ctor.newInstance(ctorParams);
		} 
		catch (ClassNotFoundException e)    { throw new RuntimeException("Class [" + className + "] not found.", e); }
		catch (SecurityException e)         { throw new RuntimeException(e.getMessage(), e); }
		catch (NoSuchMethodException e)     { throw new RuntimeException("Cannot find public constructor for class [" + className + "] with parameters [" + Arrays.toString( ctorParams ) + "]", e); }
		catch (IllegalArgumentException e)  { throw new RuntimeException("Invalid arguments for class [" + className + "] constructor.", e); }
		catch (InstantiationException e)    { throw new RuntimeException("Cannot instantiate class [" + className + "] (abstract?)." , e); }
		catch (IllegalAccessException e)    { throw new RuntimeException(e.getMessage(), e); }
		catch (InvocationTargetException e) { throw new RuntimeException(e.getCause().getMessage(), e.getCause()); } // what, no more? no fun.
		
		return instance;
	}
	
	private static Class<?> getClass(Object o)
	{
		Class <?> cl = o.getClass();
		if(cl.equals( Boolean.class ))
			return boolean.class;
		if(cl.equals( Integer.class ))
			return int.class;
		if(cl.equals( Short.class ))
			return short.class;
		if(cl.equals( Long.class ))
			return long.class;
		if(cl.equals( Float.class ))
			return float.class;
		if(cl.equals( Double.class ))
			return double.class;
		if(cl.equals( Byte.class ))
			return byte.class;
		
		return cl;
	}
	
}
