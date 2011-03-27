package yarangi.graphics.quadraturin;

import yarangi.graphics.quadraturin.interaction.spatial.SpatialHashMap;
import yarangi.graphics.quadraturin.objects.SceneEntity;

public abstract class UIVeil extends SceneVeil 
{
	public UIVeil(int width, int height)
	{
		super(new SpatialHashMap<SceneEntity>(100, 10, width, height));
	}

}