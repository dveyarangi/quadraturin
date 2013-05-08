package test.terrain;

import yar.quadraturin.QVoices;
import yar.quadraturin.Scene;
import yar.quadraturin.actions.DefaultActionFactory;
import yar.quadraturin.config.EkranConfig;
import yar.quadraturin.config.SceneConfig;
import yar.quadraturin.graphics.colors.Color;
import yar.quadraturin.graphics.colors.MaskUtil;
import yar.quadraturin.terrain.Bitmap;
import yar.quadraturin.terrain.GridyTerrainMap;
import yar.quadraturin.terrain.ITerrain;
import yarangi.spatial.ITileMap;

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

		ITileMap <ITerrain> terrain = (getWorldLayer().getTerrain());
		
//		terrain.apply( 0, 0, false, 20, MaskUtil.createCircleMask( 10, new Color(1,1,1,1), false ) );
	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub
		
	}
	

}
