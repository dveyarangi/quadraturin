package yarangi.graphics.quadraturin.resources;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class ResourceFactory <R extends IResource>
{
	
	protected HashMap <String, R> handles = new HashMap <String, R> ();
	
	protected Logger log = Logger.getLogger(this.getClass());
	
	public void registerResource(String resourceId, R resource)
	{
		if(resourceId == null)
			throw new IllegalArgumentException("Resource id cannot be null.");
		if(handles.containsKey(resourceId))
			throw new IllegalArgumentException("Resource with id " + resourceId + " is already registered.");
		if(!resource.validate(log))
			throw new IllegalArgumentException("Invalid resource, see resource factory log.");
		
		handles.put(resourceId, resource);
	}
	
	public void releaseResource(String resourceId)
	{
		if(resourceId == null)
			throw new IllegalArgumentException("Resource id cannot be null.");
		if(!handles.containsKey(resourceId))
			throw new IllegalArgumentException("Resource with id " + resourceId + " is not registered.");
		
//		R resource = handles.get(resourceId);
//		resource.release(log);
		handles.remove(resourceId);
	}
	
	public boolean containsResource(String resourceId)
	{
		return handles.containsKey(resourceId);
	}
	
	protected R getResource(String resourceId)
	{
		return handles.get(resourceId);
	}
	
	protected HashMap <String, R> getHandles() { return handles; }
	
/*	public InputStream asStream(int resourceId) throws IOException
	{
		R resource = getResource(resourceId);
		
		return resource.asStream();
	}
	
	public String asString(int resourceId) throws IOException
	{
		return IOUtils.toString(asStream(resourceId));
	}*/
	
/*	public T asObject(int resourceId) throws IOException
	{
		R resource = getResource(resourceId);
		
		return resource.asObject();
	}*/

}
