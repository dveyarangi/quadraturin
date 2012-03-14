package yarangi.graphics;

import javax.media.opengl.GL;

public class GLList
{
	
	public static GLList create(GL gl)
	{
		return new GLList(gl.glGenLists( 1 ));
	}
	
	private int id;
	
	private GLList(int id)
	{
		this.id = id;
	}
	
	
	public final int getId()
	{
		return id;
	}
	
	public final void start(GL gl)
	{
		gl.glNewList( id, GL.GL_COMPILE );
	}
	
	public final void call(GL gl)
	{
		gl.glCallList( id );
	}
	
	public final void end(GL gl)
	{
		gl.glEndList();
	}
	
	public final void delete(GL gl)
	{
		gl.glDeleteLists( id, 1 );
	}

}
