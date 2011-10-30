package yarangi.graphics.shaders;

import java.io.IOException;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.resources.TextFileResource;

/**
 * File-based GLSL shader loader 
 * @author FimaR
 */
public class FileShader extends GLSLShader
{
	
	public static final String VERTEX_DELIMETER = "[vertex_shader]";
	public static final String FRAGMENT_DELIMETER = "[fragment_shader]";
	
	private TextFileResource resource;
	
	
	public FileShader(String filename)
	{
		resource = new TextFileResource(filename);
	}

	public void init(GL gl) 
	{
		// reading program file:
		String shaderProgram;
		try {
			shaderProgram = resource.asString();
		} 
		catch (IOException e) {
			log.error("Failed to load shader file [" + resource.getFilename() + "]", e);
			throw new IllegalArgumentException(e);
		}
		
		// splitting shader programs:
		int vertexProgramOffset = shaderProgram.lastIndexOf(VERTEX_DELIMETER);
		int fragmentProgramOffset = shaderProgram.lastIndexOf(FRAGMENT_DELIMETER);
		
		String vertexShaderStr = vertexProgramOffset == -1 ? null :
					shaderProgram.substring(vertexProgramOffset+VERTEX_DELIMETER.length(),
					fragmentProgramOffset == -1 ? shaderProgram.length() : fragmentProgramOffset);
		
		String fragmentShaderStr = fragmentProgramOffset == -1 ? null : 
			shaderProgram.substring(fragmentProgramOffset+FRAGMENT_DELIMETER.length());
		
		log.debug("Compiling and linking shader program [" + resource.getFilename() + "].");
		compileAndLink(gl, vertexShaderStr, fragmentShaderStr);
	}

}

