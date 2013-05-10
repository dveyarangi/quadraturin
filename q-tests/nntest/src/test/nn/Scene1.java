package test.nn;

import yar.quadraturin.IRenderingContext;
import yar.quadraturin.QVoices;
import yar.quadraturin.Scene;
import yar.quadraturin.actions.DefaultActionFactory;
import yar.quadraturin.config.EkranConfig;
import yar.quadraturin.config.SceneConfig;

public class Scene1 extends Scene
{

	public Scene1(SceneConfig sceneConfig, EkranConfig ekranConfig, QVoices voices)
	{
		super( sceneConfig, ekranConfig, voices );
	}

	@Override
	public void init()
	{
		
		// default nav keys
		this.setActionController( DefaultActionFactory.createDefaultController( this ));
		
		addEntity( new NNEntity() );
	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub
		
	}
	

}
