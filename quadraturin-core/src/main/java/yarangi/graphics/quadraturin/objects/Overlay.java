package yarangi.graphics.quadraturin.objects;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.spatial.Area;

/**
 * TODO: this class is not yet implemented
 * @author dveyarangi
 */
public class Overlay implements IVeilEntity
{
	/**
	 * Overlay look
	 */
	private Look <?> look;
	
	private Area area;
	
	/** 
	 * Sets overlay's look.
	 * @param look
	 */
	public void setLook(Look <?> look) { this.look = look; }
	
	/**
	 * How the object looks.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public final Look getLook() { return look; }

	@Override
	public Area getArea()
	{
		
		return area;
	}

	@Override
	public int getPassId()
	{
		return 0;
	}

	@Override
	public void setPassId(int id)
	{
	}

	@Override
	public void setArea(Area area)
	{
		this.area = area;
	}

	@Override
	public boolean isAlive()
	{
		return false;
	}

	@Override
	public void markDead()
	{
	}

	@Override
	public void render(GL gl, double time, IRenderingContext context)
	{
		this.getLook().render( gl, time, this, context );
	}

	@Override
	public void init(GL gl, IRenderingContext context)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy(GL gl, IRenderingContext context)
	{
		// TODO Auto-generated method stub
		
	}

}
