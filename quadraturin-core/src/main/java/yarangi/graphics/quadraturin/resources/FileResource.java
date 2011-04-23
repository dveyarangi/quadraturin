package yarangi.graphics.quadraturin.resources;

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
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(filename);
		if(stream == null)
			throw new IOException("Cannot locate file [" + filename + "].");
		return stream;
	}

	public String getFilename() 
	{
		return filename;
	}
}
