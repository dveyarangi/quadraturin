package yar.quadraturin.actions;

import java.util.HashMap;
import java.util.Map;

import yar.quadraturin.Camera2D;
import yar.quadraturin.Scene;
import yar.quadraturin.debug.Debug;
import yar.quadraturin.objects.IEntity;
import yarangi.spatial.ISpatialFilter;

public class DefaultActionController extends ActionController 
{
	
	Map <String, IAction> actions;
	
	ICameraMan cameraMover;
	
	public DefaultActionController(Scene scene)
	{
		super(scene);
		
		cameraMover = new CameraMover((Camera2D) scene.getCamera());
		actions = DefaultActionFactory.appendNavActions(new HashMap <String, IAction> (), cameraMover, scene);
		assert Debug.appendDebugActions( actions );
	}

	@Override
	public Map<String, IAction> getActions()
	{
		return actions;
	}

	@Override
	public ISpatialFilter<IEntity> getPickingFilter()
	{
		return null;
	}

	@Override
	public ICameraMan getCameraManager()
	{
		return cameraMover;
	}

}