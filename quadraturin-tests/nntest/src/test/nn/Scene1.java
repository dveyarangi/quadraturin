package test.nn;

import yarangi.graphics.quadraturin.QVoices;
import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.actions.DefaultActionFactory;
import yarangi.graphics.quadraturin.config.EkranConfig;
import yarangi.graphics.quadraturin.config.SceneConfig;

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
