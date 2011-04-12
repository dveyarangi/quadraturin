package yarangi.graphics.utils.shaders;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.resources.TextFileResource;

public class FileShader extends TextFileResource implements IShader
{
	
	public static final String VERTEX_DELIMETER = "[vertex_shader]";
	public static final String FRAGMENT_DELIMETER = "[fragment_shader]";
	
	public static final int ILLEGAL_ID = -1;
	
	/**
	 * GL Shader object id.
	 */
	private int vertexShaderId = ILLEGAL_ID;
	
	/**
	 * GL Shader object id.
	 */
	private int fragmentShaderId = ILLEGAL_ID;	
	/**
	 * GL shader program id.
	 */
	private int programId = ILLEGAL_ID;
	
	private static Logger log = Logger.getLogger(FileShader.class);
	
	public FileShader(String filename)
	{
		super(filename);
	}

	public void init(GL gl) 
	{
		// reading program file:
		String shaderProgram;
		try {
			shaderProgram = asString();
		} 
		catch (IOException e) {
			log.error("Failed to load shader file", e);
			throw new IllegalArgumentException(e);
		}
		
		// splitting shader programs:
		int vertexProgramOffset = shaderProgram.lastIndexOf(VERTEX_DELIMETER);
		int fragmentProgramOffset = shaderProgram.lastIndexOf(FRAGMENT_DELIMETER);
		
		// compiling vertex shader:
		if(vertexProgramOffset != -1)
		{
			vertexShaderId = compileShader(gl, GL.GL_VERTEX_SHADER,
					shaderProgram.substring(vertexProgramOffset+VERTEX_DELIMETER.length(),
					fragmentProgramOffset == -1 ? shaderProgram.length() : fragmentProgramOffset));
/*			System.out.println(shaderProgram.substring(vertexProgramOffset+VERTEX_DELIMETER.length(),
					fragmentProgramOffset == -1 ? shaderProgram.length() : fragmentProgramOffset));*/ 
		}
		// compiling fragment shader:
		if(fragmentProgramOffset != -1)
		{
			fragmentShaderId = compileShader(gl, GL.GL_FRAGMENT_SHADER, 
				shaderProgram.substring(fragmentProgramOffset+FRAGMENT_DELIMETER.length()));
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
	protected int compileShader(GL gl, int type, String program)
	{
		int shaderId = gl.glCreateShader(type);

		gl.glShaderSource(shaderId, 1, new String [] {program}, (int[])null, 0);
		gl.glCompileShader(shaderId);
		printShaderInfoLog(gl, shaderId);
		
		return shaderId;
	}
	
	/**
	 * Creates and links program.
	 * @param gl
	 * @param vertexShaderId
	 * @param fragmentShaderId
	 * @return
	 */
	protected int linkProgram(GL gl, int vertexShaderId, int fragmentShaderId)
	{
		programId = gl.glCreateProgram();
		
		if(vertexShaderId != ILLEGAL_ID)
			gl.glAttachShader(programId, vertexShaderId);
		
		if(fragmentShaderId != ILLEGAL_ID)
			gl.glAttachShader(programId, fragmentShaderId);
		
		gl.glLinkProgram(programId);
		gl.glValidateProgram(programId);
		printProgramInfoLog(gl);

		return programId;
	}
	
	public void setFloat1Uniform( GL gl, String name, float a)
	{
		if(!isInitialized())
			throw new IllegalStateException("Shader is not initialized.");
		
		int uniformId = gl.glGetUniformLocation(programId, name);
//		System.out.println(uniformId);
		gl.glUniform1f(uniformId, a);
	}
	
	public void setFloat2Uniform( GL gl, String name, float a, float b)
	{
		if(!isInitialized())
			throw new IllegalStateException("Shader is not initialized.");
		int uniformId = gl.glGetUniformLocation(programId, name);
		
		gl.glUniform2f(uniformId, a, b);
	}
	
	public void setFloat3Uniform( GL gl, String name, float a, float b, float c)
	{
		if(!isInitialized())
			throw new IllegalStateException("Shader is not initialized.");
		int uniformId = gl.glGetUniformLocation(programId, name);
		
		gl.glUniform3f(uniformId, a, b, c);
	}	

	public void setFloat4Uniform( GL gl, String name, float a, float b, float c, float d)
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
	public void begin(GL gl) 
	{
		gl.glUseProgram(programId);
	}
	
	public void end(GL gl)
	{
		gl.glUseProgram(0);
	}

	protected boolean isInitialized()
	{
		return programId != -1;
	}

	protected int getShaderId(int type)
	{
		return type == GL.GL_FRAGMENT_SHADER ? fragmentShaderId : vertexShaderId;
	}
	
	protected void printProgramInfoLog(GL gl)
	{
	    IntBuffer infologLength = IntBuffer.allocate(1);

		gl.glGetProgramiv(programId, GL.GL_INFO_LOG_LENGTH, infologLength);

		int logLength = infologLength.get(0);
	    if (logLength > 0)
	    {
	    	IntBuffer infoLog = IntBuffer.allocate(1);
	    	ByteBuffer buffer = ByteBuffer.allocate(logLength);
	        gl.glGetProgramInfoLog(programId, logLength, infoLog, buffer);
			log.info("Shader program message: " + new String(buffer.array()));
	    }
	}
	
	protected void printShaderInfoLog(GL gl, int shaderId)
	{
	    IntBuffer infologLength = IntBuffer.allocate(1);

		gl.glGetShaderiv(shaderId, GL.GL_INFO_LOG_LENGTH, infologLength);

		int logLength = infologLength.get(0);
	    if (logLength > 0)
	    {
	    	IntBuffer infoLog = IntBuffer.allocate(1);
	    	ByteBuffer buffer = ByteBuffer.allocate(logLength);
	        gl.glGetShaderInfoLog(shaderId, logLength, infoLog, buffer);
			log.info("Shader message: " + new String(buffer.array()));
	    }
	}

}

