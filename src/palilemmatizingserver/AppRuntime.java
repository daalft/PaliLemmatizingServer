package palilemmatizingserver;


import de.general.jettyserver.*;
import de.general.cfg.*;
import de.general.jettyserver.*;
import de.general.log.*;
import de.unitrier.daalft.pali.morphology.MorphologyGenerator;
import palilemmatizingserver.handler.*;
import palilemmatizingserver.handler.conv.*;

/**
 *
 * @author knauth
 */
public class AppRuntime implements IAppRuntime
{

	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////

	PrintLogger pl;
	AppConfiguration cfg;

	IIPAddressFilter ipAddressFilter;

	FormatConverterManager formatConverterManager;
	
	MorphologyGenerator mg;

	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	/**
	 * Constructor.
	 */
	public AppRuntime()
	{
	}

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	@Override
	public ILogInterface getMainLog()
	{
		return pl;
	}

	public AppConfiguration getConfiguration()
	{
		return cfg;
	}

	@Override
	public void initialize(PrintLogger pl) throws Exception
	{
		this.pl = pl;
		cfg = new AppConfiguration(new IniFileConfigurationLoader("cfg.ini"), pl);
		cfg.load();

		// TODO: fill ipAddressFilter-variable

		formatConverterManager = new FormatConverterManager();
		formatConverterManager.register(ConverterWordFormGeneration.class);
		
		mg = new MorphologyGenerator();
	}

	@Override
	public IIPAddressFilter getIPAddressFilter()
	{
		return ipAddressFilter;
	}

	@Override
	public int getWebServerPortFromConfiguration()
	{
		return cfg.getPort();
	}

	public FormatConverterManager getFormatConverterManager()
	{
		return formatConverterManager;
	}

	public MorphologyGenerator getMorphologyGenerator() {
		return mg;
	}

}
