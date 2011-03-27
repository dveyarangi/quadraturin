package yarangi.graphics.quadraturin.effects;


public class EffectFactory 
{
	
	private static EffectFactory factory;
	
	
	
//	private int textureSize;
	
//	private HashMap <SceneEntity, FloatBuffer> backgroundTextures = new HashMap <SceneEntity, FloatBuffer> ();
	
	protected EffectFactory(int textureSize)
	{
//		this.textureSize = textureSize;
	}
	
	public static void initFactory(int textureSize)
	{
		if(factory == null)
			factory = new EffectFactory(textureSize);
		else
			throw new IllegalStateException("Factory is already initialized.");
	}
	
}
