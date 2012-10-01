package yarangi.graphics.quadraturin.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import yarangi.graphics.quadraturin.Q;

import com.google.gson.Gson;

public class QuadJsonConfig implements IQuadConfig
{
	
	public static final String CONFIG_FILENAME;
	static {
		String configFileProp = System.getProperty( Q.CONFIG_FILE );
		if(configFileProp != null)
			CONFIG_FILENAME = configFileProp;
		else 
			CONFIG_FILENAME = "quadraturin-config.json";
		
		LOG.debug( "Using [" + new File(CONFIG_FILENAME).getAbsolutePath() + "] configuration file." );
	}
	
	
	
	public static QuadJsonConfig load()
	{
		Gson gson = new Gson();
		
		String fileContents = null;
		try {
			InputStream stream = QuadJsonConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILENAME);
			if(stream == null) {
				stream = new FileInputStream( CONFIG_FILENAME);
			}
//				throw new RuntimeException("Cannot find " + CONFIG_FILENAME + " file.");
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
