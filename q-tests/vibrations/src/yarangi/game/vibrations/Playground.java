package yarangi.game.vibrations;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.QVoices;
import yar.quadraturin.Scene;
import yar.quadraturin.actions.DefaultActionFactory;
import yar.quadraturin.config.EkranConfig;
import yar.quadraturin.config.SceneConfig;

public class Playground extends Scene
{

	public Playground(SceneConfig config, EkranConfig ekran, QVoices voices)
	{
		super( config, ekran, voices );
		setActionController(DefaultActionFactory.createDefaultController( this ));
		
	}


}
