package yarangi.graphics.quadraturin.resources;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

public interface IResource
{
	public boolean validate(Logger log);
	
	public InputStream asStream() throws IOException;
	
//	public R asObject();
}
