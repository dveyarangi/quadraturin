package yarangi.graphics.quadraturin.resources;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class TextFileResource extends FileResource 
{
	public static final String DEFAULT_ENCODING = "UTF8";
	
	protected String encoding;

	public TextFileResource(String filename) {
		this(filename, DEFAULT_ENCODING);
	}
	
	public TextFileResource(String filename, String encoding) {
		super(filename);
		
		this.encoding = encoding;
	}
	
	public String asString() throws IOException
	{
		return IOUtils.toString(asStream(), encoding);
	}

}
