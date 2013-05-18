package yar.quadraturin.ui.transition;

import java.awt.Dimension;

import yar.quadraturin.Camera2D;
import yar.quadraturin.QVoices;
import yar.quadraturin.Scene;
import yar.quadraturin.Stage;
import yar.quadraturin.UserLayer;
import yar.quadraturin.config.EkranConfig;
import yar.quadraturin.config.SceneConfig;
import yar.quadraturin.ui.Direction;
import yar.quadraturin.ui.ImagePanelLook;
import yar.quadraturin.ui.Overlay;
import yar.quadraturin.ui.Panel;
import yarangi.math.RangedDouble;
import yarangi.math.Vector2D;

public class DummyLoadingScreen extends Scene
{
	
	private static final String DUMMY_Q = "media\\Q2.jpg";

	public DummyLoadingScreen(SceneConfig sceneConfig, EkranConfig ekranConfig, QVoices voices)
	{
		super( sceneConfig, ekranConfig, voices );
		
	}

	@Override
	public void init()
	{
		// scene ui aggregator
		this.uiLayer = new UserLayer(width, height);
		
//		super.init( gl, context );
		this.camera = new Camera2D(
				Vector2D.R(0, 0), 
				null,
				new RangedDouble(1, 1, 1),
				new Dimension(width, height)
				);
		Panel [] panels = getUILayer().getBasePanel().split( new int [] {100}, Direction.HORIZONTAL );
		
		Overlay panel = new Overlay(panels[0], true);
//		panels[0].setInsets( new Insets(5,5,5,5));
		panel.setLook( new ImagePanelLook( DUMMY_Q ) );
		
		this.getUILayer().addEntity( panel );
		

	}
	
	@Override
	public void postInit() {
//		Stage.setNextScene( "playground" );
	}
	
//	@Override
//	public void reshape(GL gl, DefaultRenderingContext context) {}

}
