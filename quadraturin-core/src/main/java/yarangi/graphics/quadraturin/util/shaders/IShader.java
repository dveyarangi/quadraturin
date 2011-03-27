package yarangi.graphics.quadraturin.util.shaders;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.resources.GLResource;

public interface IShader extends GLResource
{
	
	public static final String TYPE_VERTEX = "GL_ARB_vertex_shader";
	public static final String TYPE_FRAGMENT = "GL_ARB_fragment_shader";
	
	public void setFloat1Uniform(GL gl, String name, float integer);
	
	public void setFloat2Uniform(GL gl, String name, float x, float y);

	public void begin(GL gl);

	public void end(GL gl);

}
