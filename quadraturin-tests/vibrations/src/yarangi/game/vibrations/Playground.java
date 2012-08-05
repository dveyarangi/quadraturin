package yarangi.game.vibrations;

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

}
