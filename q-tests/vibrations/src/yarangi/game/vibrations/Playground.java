package yarangi.game.vibrations;

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
		
	}

	@Override
	public void init()
	{
		setActionController(DefaultActionFactory.createDefaultController( this ));
		
//		grid.getTile( 25, 25 ).getContent().add(-10, -10);
	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub
		
	}

}
