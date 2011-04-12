package yarangi.graphics.utils.shaders;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.resources.GLResource;

public interface IShader extends GLResource
{
	
	public static final String TYPE_VERTEX = "GL_ARB_vertex_shader";
	public static final String TYPE_FRAGMENT = "GL_ARB_fragment_shader";
	
	public void setFloat1Uniform(GL gl, String name, float a);
	public void setFloat2Uniform(GL gl, String name, float a, float b);
	public void setFloat3Uniform(GL gl, String name, float a, float b, float c);
	public void setFloat4Uniform(GL gl, String name, float a, float b, float c, float d);

	public void begin(GL gl);

	public void end(GL gl);

}
