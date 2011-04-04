package yarangi.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileLoader 
{
	/**
	 * Loads a short text file into string.
	 * 
	 */
	public static String loadFile(File file) throws IOException
	{
		return loadFile(new FileReader(file));
	}
	
	public static String loadFile(InputStream is) throws IOException
	{
		return loadFile(new InputStreamReader(is));
	}
	
	public static String loadFile(Reader r) throws IOException
	{
		BufferedReader reader = new BufferedReader(r);
		StringBuilder programStr = new StringBuilder();
		String line = null;
		while((line = reader.readLine()) != null)
			programStr.append(line);
		
		reader.close();
		return programStr.toString();
	}

}
