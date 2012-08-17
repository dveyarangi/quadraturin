package test.terrain;

import yarangi.graphics.colors.Color;
import yarangi.graphics.colors.MaskUtil;
import yarangi.graphics.quadraturin.QVoices;
import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.actions.DefaultActionFactory;
import yarangi.graphics.quadraturin.config.EkranConfig;
import yarangi.graphics.quadraturin.config.SceneConfig;
import yarangi.graphics.quadraturin.terrain.Bitmap;
import yarangi.graphics.quadraturin.terrain.GridyTerrainMap;

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

		GridyTerrainMap terrain = (GridyTerrainMap)	(getWorldLayer().<Bitmap>getTerrain());
		
		terrain.apply( 0, 0, false, 20, MaskUtil.createCircleMask( 10, new Color(1,1,1,1), false ) );
	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub
		
	}
	

}
