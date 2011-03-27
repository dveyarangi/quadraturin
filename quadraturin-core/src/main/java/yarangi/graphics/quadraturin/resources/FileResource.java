package yarangi.graphics.quadraturin.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

public abstract class FileResource implements IResource
{
	protected String filename;

	public FileResource(String filename)
	{
		this.filename = filename;
	}
	
	public boolean validate(Logger log) 
	{
		// TODO: validation
/*		if(!(new File(filename).exists()))
		{
			log.error("Failed to locate resource file:" + filename);
			return false;
		}*/
		
		return true;
	}
	
	public InputStream asStream() throws IOException 
	{
		return this.getClass().getClassLoader().getResourceAsStream(filename);
	}

	public String getFilename() 
	{
		return filename;
	}
}
