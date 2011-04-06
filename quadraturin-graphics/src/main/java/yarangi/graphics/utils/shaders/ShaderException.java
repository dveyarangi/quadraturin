package yarangi.graphics.utils.shaders;

public class ShaderException extends Exception 
{

	private static final long serialVersionUID = -5473406005946147765L;
	
	public ShaderException(Exception e) { super(e); }
	public ShaderException(String s) { super(s); }
	public ShaderException(String s, Exception e) { super(s, e); }

}
