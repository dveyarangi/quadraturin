package yarangi.graphics.quadraturin.objects;

import yarangi.spatial.Area;

public class Overlay implements IVeilEntity
{
	/**
	 * Overlay look
	 */
	private Look <?> look;
	
	
	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPassId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPassId(int id)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setArea(Area area)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAlive()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void markDead()
	{
		// TODO Auto-generated method stub
		
	}

}
