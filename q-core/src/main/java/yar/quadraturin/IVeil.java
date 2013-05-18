package yar.quadraturin;

import yar.quadraturin.objects.ILook;

/**
 * Represents a rendering space, that may be customized for 
 * different {@link ILook} rendering effects.
 * 
 * @author dveyarangi
 */
public interface IVeil
{
	
	/**
	 * Marks start of the veil rendering.
	 * @param context
	 */
	public void weave(IRenderingContext context);
	
	/**
	 * Marks end of the veil rendering.
	 * @param context
	 */
	public void tear(IRenderingContext context);

}
