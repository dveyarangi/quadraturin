package yarangi.graphics.quadraturin.config;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import yarangi.graphics.quadraturin.config.EkranConfig;
import yarangi.graphics.quadraturin.config.InputConfig;
import yarangi.graphics.quadraturin.config.QuadraturinConfig;
import yarangi.graphics.quadraturin.config.StageConfig;

/**
 * Quadraturin engine configuration entry point.
 * 
 * TODO: reload, save/ load.
 */
public class XMLQuadConfig implements IQuadConfig
{
	/**
	 * Default name of configuration file.
	 */
	public static final String DEFAULT_CONFIG_FILENAME = "quadraturin-config.xml";
	
	/**
	 * Name of JAXB context of the configuration schema.
	 */
	public static final String CONFIG_PACKAGE = "yarangi.graphics.quadraturin.config";
	
	/**
	 * Name of custom configuration file property.
	 */
	public static final String CONFIG_PROPERTY = "yarangi.graphics.quadraturin.config";
	
	/**
	 * Name of actual configuration file name.
	 */
	public static final String CONFIG_FILENAME;
	static {
		String customConfigFilename = System.getProperty(CONFIG_PROPERTY);
		CONFIG_FILENAME = ( customConfigFilename == null ? DEFAULT_CONFIG_FILENAME : customConfigFilename );
		
		instance = new XMLQuadConfig(CONFIG_FILENAME);
	}
	
	private Logger log = Logger.getLogger("q-config");
	
	private static XMLQuadConfig instance;
	
	private StageConfig stageConfig;
	
	private InputConfig inputConfig;
	
	private EkranConfig ekranConfig;
	
	
	
	@SuppressWarnings("unchecked")
	protected XMLQuadConfig(String configFilename)
	{
		log.debug("Loading [" + configFilename + "] configuration file...");
		QuadraturinConfig config = null;
		try {
			JAXBContext ctx = JAXBContext.newInstance(CONFIG_PACKAGE);
			
			Unmarshaller unmarshaller = ctx.createUnmarshaller();
			
			config = ((JAXBElement<QuadraturinConfig>)
					(unmarshaller.unmarshal(new File(configFilename)))).getValue();
			
		} 
		catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		
		log.debug("Using configuration [" + config.getName() + "].");
		
		this.stageConfig = config.getStage();
		this.ekranConfig = config.getEkran();
		this.inputConfig = config.getInput();
	}
	
	public StageConfig getStageConfig() { return stageConfig; }
	public InputConfig getInputConfig() { return inputConfig; }
	public EkranConfig getEkranConfig() { return ekranConfig; }

	public static IQuadConfig getInstance()
	{
		return instance;
	}
	
	
}
