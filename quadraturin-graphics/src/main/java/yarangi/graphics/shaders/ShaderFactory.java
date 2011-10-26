package yarangi.graphics.shaders;

import java.util.Map;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.plugin.IGraphicsPlugin;
import yarangi.graphics.quadraturin.resources.ResourceFactory;

public class ShaderFactory extends ResourceFactory <IShader> implements IGraphicsPlugin
{
	public static final String NAME = "shaders";
	
	public ShaderFactory(Map <String, String> properties)
	{
		super(NAME);
		
		for(String shaderName : properties.keySet())
		{
			registerResource(shaderName, new FileShader(properties.get(shaderName)));
		}
	}
	
	public void init(GL gl, IRenderingContext context)
	{
		for(IShader shaderResource : getHandles().values())
		{
			shaderResource.init(gl);
		}
	}

	@Override
	public void preRender(GL gl, IRenderingContext context) { /* useless here */ }

	@Override
	public void postRender(GL gl, IRenderingContext context) { /* also useless */ }
	
	public IShader getShader(String resourceId)
	{
		return getResource(resourceId);
	}

	@Override
	public String[] getRequiredExtensions()
	{
		return new String [] { "GL_ARB_vertex_shader", "GL_ARB_fragment_shader"};
	}
}
