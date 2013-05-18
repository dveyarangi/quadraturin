package yar.quadraturin.graphics.shaders;

import java.util.Map;

import javax.media.opengl.GL2;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.plugin.IGraphicsPlugin;
import yarangi.resources.ResourceFactory;

public final class ShaderFactory extends ResourceFactory <GLSLShader> implements IGraphicsPlugin
{
	public static final String NAME = "shaders";
	
	private boolean isInited = false;
	
	public ShaderFactory(Map <String, String> properties)
	{
		super(NAME);
		
		for(String shaderName : properties.keySet())
		{
			registerResource(shaderName, new FileShader(properties.get(shaderName)));
		}
	}
	
	@Override
	public void init(IRenderingContext context)
	{
		GL2 gl = context.gl();
		if(isInited) return;
		
		for(GLSLShader shaderResource : getHandles().values())
		{
			shaderResource.init(gl);
		}
		isInited = true;
	}
	
	@Override
	public void resize(IRenderingContext context)
	{
		// lazy
	}

	@Override
	public void preRender(IRenderingContext context) { /* useless here */ }

	@Override
	public void postRender(IRenderingContext context) { /* also useless */ }
	
	public GLSLShader getShader(String resourceId)
	{
		return getResource(resourceId);
	}

	@Override
	public String[] getRequiredExtensions()
	{
		return new String [] { "GL_ARB_vertex_shader", "GL_ARB_fragment_shader"};
	}

	@Override
	public void destroy(IRenderingContext context)
	{
		if(!isInited)
			throw new IllegalStateException("Trying to destroy not initialized plugin");
		
		GL2 gl = context.gl();
		
		for(GLSLShader shaderResource : getHandles().values())
		{
			shaderResource.destroy(gl);
		}
		
		isInited = false;
	}
}
