package yarangi.graphics.textures;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.IRenderingContext;
import yarangi.graphics.quadraturin.plugin.IGraphicsPlugin;
import yarangi.resources.ResourceFactory;

import com.sun.opengl.util.texture.Texture;

public final class TextureManager extends ResourceFactory <TextureHandle> implements IGraphicsPlugin
{

	LinkedHashMap<String, TextureHandle> loadedTextures = new LALOCacheMap<String, TextureHandle>();
	
	public TextureManager(String factoryName, Map <String, String> textureFiles)
	{
		super( factoryName );
		
		// registering textures:
		for(String textureFilename : textureFiles.keySet())
		{
			registerResource(textureFilename, new TextureHandle(textureFiles.get(textureFilename)));
		}
	}
	
	/**
	 * Loads and retrieves specified texture
	 * @param name
	 * @return
	 */
	public Texture getTexture(String name, GL gl)
	{
		TextureHandle texture = loadedTextures.get( name );
		if(texture != null) 
			return texture.getTexture();
			
		texture = getResource( name );
		if(texture == null)
			throw new IllegalArgumentException("Texture [" + name + "] is not registered.");
		
		texture.load(gl);
		
		loadedTextures.put( name, texture );
		
		return texture.getTexture();
	}

	@Override
	final public void init(GL gl, IRenderingContext context)
	{
		// TODO: validate textures?
		// TODO: go over registered Looks and test files?
	}

	@Override
	public void resize(GL gl, IRenderingContext context)
	{
		// TODO Texture bundles...
		
	}

	@Override
	public void preRender(GL gl, IRenderingContext context)
	{
	}

	@Override
	public void postRender(GL gl, IRenderingContext context)
	{
	}

	@Override
	public void destroy(GL gl)
	{
		for(TextureHandle texture : loadedTextures.values())
			texture.unload(gl);
	}

	@Override
	public String[] getRequiredExtensions()
	{
		return null;
	}

	
	public class LALOCacheMap <K, V> extends LinkedHashMap <K, V>
	{
		public V get(Object key) 
		{
			V val = super.get(key);
			if(val == null)
				return null;
			
			super.put( (K)key, val );
			
			return val;
		}
	}

}
