package yarangi.game.vibrations;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.QVoices;
import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.actions.DefaultActionFactory;
import yarangi.graphics.quadraturin.config.EkranConfig;
import yarangi.graphics.quadraturin.config.SceneConfig;

public class Playground extends Scene
{

	public Playground(SceneConfig config, EkranConfig ekran, QVoices voices)
	{
		super( config, ekran, voices );
		
		setActionController(DefaultActionFactory.createDefaultController( this ));
		
//		grid.getTile( 25, 25 ).getContent().add(-10, -10);
	}
	@Override
	public void preDisplay(GL gl, boolean useNames)
	{
		super.preDisplay(gl, false);
	      gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	}

}
