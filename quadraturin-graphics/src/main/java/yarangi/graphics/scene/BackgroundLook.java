package yarangi.graphics.scene;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.RenderingContext;
import yarangi.graphics.quadraturin.objects.Look;

/**
 * TODO: add texture and bump map 
 * @author dveyarangi
 *
 */
public class BackgroundLook implements Look<BackgroundEntity>
{

	
	private float[] lightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};
	private float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 200f};
	private float[] lightDirection = {0.0f, 0.0f, -1.0f};
	private float[] lightCutOff = {0};
	private float[] lightExponent = {50f};
	
	private int meshListId = -1;
	
 	public void render(GL gl, double time, BackgroundEntity be, RenderingContext context) 
	{
		
//		gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, this.lightAmbient, 0);
//      gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, this.lightDiffuse, 0);
//      gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, be.getLightPosition(), 0);
//     gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPOT_DIRECTION, lightDirection, 0);    
 //     gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPOT_CUTOFF, lightCutOff, 0);    
 //     gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPOT_EXPONENT, lightExponent, 0);    
     
 //     gl.glEnable(GL.GL_LIGHT1);
 //     gl.glEnable(GL.GL_LIGHTING);
    	  gl.glCallList(meshListId);
      
 //   lightDirection[0] += 0.0001;
//       be.getLightPosition()[2] += 0.0001;
 //     lightExponent[0] -= 0.1;
//      gl.glDisable(GL.GL_LIGHT1);
//      gl.glDisable(GL.GL_LIGHTING);

	}

	public void init(GL gl, BackgroundEntity entity) 
	{
		double [][] heights = entity.getHeightsMap();
		double halfWidth = entity.getWidth()/2;
		double halfHeight = entity.getHeight()/2;
		double tileSize = entity.getTileSize();
	    	  meshListId=gl.glGenLists(1);
	    	  gl.glNewList(meshListId,GL.GL_COMPILE);	
//				gl.glColor4f((float)RandomUtil.getRandomDouble(0.5), (float)RandomUtil.getRandomDouble(0.5), (float)RandomUtil.getRandomDouble(0.5), 1.0f);
				gl.glBegin(GL.GL_QUADS);
			
				for(int x = 0; x < heights[0].length-1; x ++)
				{
					double realx = tileSize*x;
		
					for(int y = 0; y < heights.length-1; y ++)
					{
						if((x % 2) + (y % 2) == 1)
							gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
						else
							gl.glColor4f(0.2f, 0.2f, 0.2f, 1.0f);
						
						double realy = tileSize * y;
						gl.glVertex3f((float)(realx-halfWidth), (float)(realy-halfHeight), (float)heights[x][y]);
						gl.glVertex3f((float)(realx-halfWidth+tileSize), (float)(realy-halfHeight), (float)heights[x+1][y]);
						gl.glVertex3f((float)(realx-halfWidth+tileSize), (float)(realy-halfHeight+tileSize), (float)heights[x+1][y+1]);
						gl.glVertex3f((float)(realx-halfWidth), (float)(realy-halfHeight+tileSize), (float)heights[x][y+1]);
					}
				}
				gl.glEnd();
			gl.glEndList();
	 }

	public void destroy(GL gl, BackgroundEntity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCastsShadow()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
