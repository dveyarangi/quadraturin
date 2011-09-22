package yarangi.graphics.quadraturin.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

public class QuadJsonConfig implements IQuadConfig
{
	public static final String CONFIG_FILENAME = "quadraturin-config.json";
	
	public static QuadJsonConfig load()
	{
		Gson gson = new Gson();
		
		String fileContents = null;
		try {
			InputStream stream = QuadJsonConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILENAME);
			if(stream == null)
				throw new RuntimeException("Cannot find " + CONFIG_FILENAME + " file.");
			fileContents = IOUtils.toString(stream);
		} 
		catch (FileNotFoundException e) { throw new RuntimeException("Cannot find " + CONFIG_FILENAME + " file."); } 
		catch (IOException e) { throw new RuntimeException("Cannot read " + CONFIG_FILENAME + " file."); }
		
		QuadJsonConfig config = gson.fromJson(fileContents, QuadJsonConfig.class);
//		System.out.println(config.getInputConfig().getScrollStep());
		
//		for(InputBinding ab : config.getInputConfig().getBindings())
//			System.out.println(ab);
		
		return config;
	}
	
	private EkranConfig ekran;
	private StageConfig stage;
	private InputConfig input;

	@Override
	public EkranConfig getEkranConfig() { return ekran; }

	@Override
	public StageConfig getStageConfig() { return stage; }

	@Override
	public InputConfig getInputConfig() { return input; }
}
