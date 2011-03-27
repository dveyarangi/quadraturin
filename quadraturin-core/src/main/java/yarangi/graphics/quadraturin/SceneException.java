package yarangi.graphics.quadraturin;

public class SceneException extends Exception 
{

	private static final long serialVersionUID = -5473406005946147765L;
	
	public SceneException(Exception e) { super(e); }
	public SceneException(String s) { super(s); }
	public SceneException(String s, Exception e) { super(s, e); }

}
