package yarangi.graphics.quadraturin.samples.terrain;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IViewPoint;
import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.Scene;
import yarangi.graphics.quadraturin.UIVeil;
import yarangi.graphics.quadraturin.ViewPoint2D;
import yarangi.graphics.quadraturin.WorldVeil;
import yarangi.graphics.quadraturin.actions.DefaultActionFactory;
import yarangi.graphics.quadraturin.actions.IAction;
import yarangi.graphics.quadraturin.debug.SceneDebugOverlay;
import yarangi.graphics.quadraturin.objects.Look;
import yarangi.graphics.quadraturin.simulations.ICollider;
import yarangi.graphics.quadraturin.simulations.IPhysicsEngine;
import yarangi.math.RangedDouble;
import yarangi.math.Vector2D;
import yarangi.spatial.ISpatialObject;
import yarangi.spatial.SpatialIndexer;

public class GraphScene extends Scene
{
	
	private double [][] tx, ty;
	
	private double minx, miny;
	private double maxx, maxy;
	
	private double []nx, ny;
	
	
	private SceneDebugOverlay spatialOverlay;

	public GraphScene()
	{
		super("Terrain test",  new WorldVeil(1000, 1000) {
			
			@Override public void preDisplay(GL gl) { }
			
			@Override public void postDisplay(GL gl) { }
			
			@Override protected void initViewPoint(IViewPoint viewPoint) 
			{ 
				ViewPoint2D vp = (ViewPoint2D) viewPoint;
				
				vp.setCenter(new Vector2D(0,0));
				vp.setScale(new RangedDouble(0.1, 1, 2));
			}

			@Override public IPhysicsEngine createPhysicsEngine() { return null; }
		}, 
		new UIVeil(1000, 1000) {}, 
		1000, 1000, 1);
		
		SpatialIndexer <ISpatialObject> indexer = this.getWorldVeil().getEntityIndex() ;
		spatialOverlay = new SceneDebugOverlay(indexer);
	}
	
	
	@Override
	public void display(GL gl, double time, RenderingContext context) 
	{
		spatialOverlay.display(gl, time, context);
	}

	@Override
	public Map<String, IAction> getActionsMap() {
		// TODO Auto-generated method stub
		return DefaultActionFactory.fillNavigationActions(new HashMap <String, IAction> (), this.getViewPoint());
	}
}
