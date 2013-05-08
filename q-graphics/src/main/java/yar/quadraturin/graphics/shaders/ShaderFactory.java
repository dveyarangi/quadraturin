package yar.quadraturin.graphics.shaders;

import java.util.Map;

import javax.media.opengl.GL;
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
	public void init(GL gl1, IRenderingContext context)
	{
		GL2 gl = gl1.getGL2();
		if(isInited) return;
		
		for(GLSLShader shaderResource : getHandles().values())
		{
			shaderResource.init(gl);
		}
		isInited = true;
	}
	
	@Override
	public void resize(GL gl, IRenderingContext context)
	{
		// lazy
	}

	@Override
	public void preRender(GL gl, IRenderingContext context) { /* useless here */ }

	@Override
	public void postRender(GL gl, IRenderingContext context) { /* also useless */ }
	
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
	public void destroy(GL gl)
	{
		if(!isInited)
			throw new IllegalStateException("Trying to destroy not initialized plugin");
		for(GLSLShader shaderResource : getHandles().values())
		{
			shaderResource.destroy(gl);
		}
		
		isInited = false;
	}
}
