package yar.quadraturin.graphics.shaders;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;

import com.spinn3r.log5j.Logger;

/**
 * Handler for GLSL shader.
 * @author FimaR
 *
 */
public abstract class GLSLShader {
	
	public static final int ILLEGAL_ID = -1;
	
	/**
	 * GL Shader object id.
	 */
	protected int vertexShaderId = ILLEGAL_ID;
	
	/**
	 * GL Shader object id.
	 */
	protected int fragmentShaderId = ILLEGAL_ID;	
	
	/**
	 * GL shader program id.
	 */
	protected int programId = ILLEGAL_ID;
	

	protected static Logger log = Logger.getLogger("GLSL");
	
	abstract void init(GL2 gl);

	protected void compileAndLink(GL2ES2 gl, String vertexShaderStr, String fragmentShaderStr) 
	{
		// compiling vertex shader:
		if(vertexShaderStr != null)
		{
			vertexShaderId = compileShader(gl, GL2ES2.GL_VERTEX_SHADER, vertexShaderStr);
/*			System.out.println(shaderProgram.substring(vertexProgramOffset+VERTEX_DELIMETER.length(),
					fragmentProgramOffset == -1 ? shaderProgram.length() : fragmentProgramOffset));*/ 
		}
		// compiling fragment shader:
		if(fragmentShaderStr != null)
		{
			fragmentShaderId = compileShader(gl, GL2ES2.GL_FRAGMENT_SHADER, fragmentShaderStr);
//			System.out.println(shaderProgram.substring(fragmentProgramOffset+FRAGMENT_DELIMETER.length()));
		}
		// creating shader program:
		programId = linkProgram(gl, vertexShaderId, fragmentShaderId);
	}	
	
	/**
	 * Creates and compiles a shader.
	 * 
	 * @param gl
	 * @param type {@link GL#GL_VERTEX_SHADER} or {@link GL#GL_FRAGMENT_SHADER}
	 * @param program shader code
	 * @return
	 */
	protected int compileShader(GL2ES2 gl, int type, String program)
	{
		int shaderId = gl.glCreateShader(type);

		gl.glShaderSource(shaderId, 1, new String [] {program}, (int[])null, 0);
		gl.glCompileShader(shaderId);
		String info = getShaderInfoLog(gl, shaderId);
		if(info != null)
			log.debug("Shader message: " + info);
		return shaderId;
	}
	
	/**
	 * Creates and links program.
	 * @param gl
	 * @param vertexShaderId
	 * @param fragmentShaderId
	 * @return
	 */
	protected int linkProgram(GL2ES2 gl, int vertexShaderId, int fragmentShaderId)
	{
		programId = gl.glCreateProgram();
		
		if(vertexShaderId != ILLEGAL_ID)
			gl.glAttachShader(programId, vertexShaderId);
		
		if(fragmentShaderId != ILLEGAL_ID)
			gl.glAttachShader(programId, fragmentShaderId);
		
		gl.glLinkProgram(programId);
		gl.glValidateProgram(programId);
		
		String info = getShaderProgramInfo(gl);
		if(info != null)
			log.debug("Shader program message: " + info);

		return programId;
	}
	
	public void printDebugInfo(GL2ES2 gl)
	{
		String info = getShaderInfoLog(gl, fragmentShaderId);
		if(info != null)
			log.debug("Shader program message: " + info);

	}
	
	public void setFloat1Uniform( GL2ES2 gl, String name, float a)
	{
		if(!isInitialized())
			throw new IllegalStateException("Shader is not initialized.");
		
		int uniformId = gl.glGetUniformLocation(programId, name);
//		System.out.println(uniformId);
		gl.glUniform1f(uniformId, a);
	}
	
	public void setFloat2Uniform( GL2ES2 gl, String name, float a, float b)
	{
		if(!isInitialized())
			throw new IllegalStateException("Shader is not initialized.");
		int uniformId = gl.glGetUniformLocation(programId, name);
		
		gl.glUniform2f(uniformId, a, b);
	}
	
	public void setFloat3Uniform( GL2ES2 gl, String name, float a, float b, float c)
	{
		if(!isInitialized())
			throw new IllegalStateException("Shader is not initialized.");
		int uniformId = gl.glGetUniformLocation(programId, name);
		
		gl.glUniform3f(uniformId, a, b, c);
	}	

	public void setFloat4Uniform( GL2ES2 gl, String name, float a, float b, float c, float d)
	{
		if(!isInitialized())
			throw new IllegalStateException("Shader is not initialized.");
		int uniformId = gl.glGetUniformLocation(programId, name);
		
		gl.glUniform4f(uniformId, a, b, c, d);
	}	

	/**
	 * Invokes the shader.
	 * @param gl
	 */
	public void begin(GL2ES2 gl) 
	{
		gl.glUseProgram(programId);
	}
	
	public void end(GL2ES2 gl)
	{
		gl.glUseProgram(0);
	}

	protected boolean isInitialized()
	{
		return programId != -1;
	}

	protected int getShaderId(int type)
	{
		return type == GL2ES2.GL_FRAGMENT_SHADER ? fragmentShaderId : vertexShaderId;
	}
	
	protected String getShaderProgramInfo(GL2ES2 gl)
	{
	    IntBuffer infologLength = IntBuffer.allocate(1);

		gl.glGetProgramiv(programId, GL2ES2.GL_INFO_LOG_LENGTH, infologLength);

		int logLength = infologLength.get(0);
	    if (logLength > 0)
	    {
	    	IntBuffer infoLog = IntBuffer.allocate(1);
	    	ByteBuffer buffer = ByteBuffer.allocate(logLength);
	        gl.glGetProgramInfoLog(programId, logLength, infoLog, buffer);
		    return new String(buffer.array());
	    }

	    return null;
	}
	
	protected String getShaderInfoLog(GL2ES2 gl, int shaderId)
	{
	    IntBuffer infologLength = IntBuffer.allocate(1);

		gl.glGetShaderiv(shaderId, GL2ES2.GL_INFO_LOG_LENGTH, infologLength);

		int logLength = infologLength.get(0);
	    if (logLength > 0)
	    {
	    	IntBuffer infoLog = IntBuffer.allocate(1);
	    	ByteBuffer buffer = ByteBuffer.allocate(logLength);
	        gl.glGetShaderInfoLog(shaderId, logLength, infoLog, buffer);
			return new String(buffer.array());
	    }
	    return null;
	}

	void destroy(GL gl)
	{
		
	}
}
