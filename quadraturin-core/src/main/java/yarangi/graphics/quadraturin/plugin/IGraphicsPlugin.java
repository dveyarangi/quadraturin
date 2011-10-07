package yarangi.graphics.quadraturin.plugin;

import javax.media.opengl.GL;

public interface IGraphicsPlugin {

	/**
	 * Initializes plugin
	 * @param gl
	 */
	public void init(GL gl);

	/**
	 * @return Array of GL extensions required by this plugin.
	 * @see GL.glGetString(GL.GL_EXTENSIONS)
	 */
	public String [] getRequiredExtensions();
}
