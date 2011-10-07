package yarangi.graphics.shaders;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.resources.GLResource;

/**
 * TODO: more methods!
 * 
 * @author dveyarangi
 */
public interface IShader extends GLResource
{
	
	public void setFloat1Uniform(GL gl, String name, float a);
	public void setFloat2Uniform(GL gl, String name, float a, float b);
	public void setFloat3Uniform(GL gl, String name, float a, float b, float c);
	public void setFloat4Uniform(GL gl, String name, float a, float b, float c, float d);

	/**
	 * Starts shader program for following GL procedures 
	 * @param gl
	 */
	public void begin(GL gl);

	/**
	 * Stops shader program
	 * @param gl
	 */
	public void end(GL gl);

}
