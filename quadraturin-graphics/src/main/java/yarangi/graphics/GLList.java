package yarangi.graphics;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

public class GLList
{
	
	public static GLList create(GL2 gl)
	{
		return new GLList(gl.glGenLists( 1 ));
	}
	
	private final int id;
	
	private GLList(int id)
	{
		this.id = id;
	}
	
	
	public final int getId()
	{
		return id;
	}
	
	public final void start(GL2 gl)
	{
		gl.glNewList( id, GL2.GL_COMPILE );
	}
	
	public final void call(GL2 gl)
	{
		gl.glCallList( id );
	}
	
	public final void end(GL2 gl)
	{
		gl.glEndList();
	}
	
	public final void delete(GL2 gl)
	{
		gl.glDeleteLists( id, 1 );
	}

}
