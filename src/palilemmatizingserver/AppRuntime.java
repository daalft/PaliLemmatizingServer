package palilemmatizingserver;


import de.general.jettyserver.IIPAddressFilter;
import de.general.cfg.*;
import de.general.jettyserver.*;
import de.general.log.*;



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

}
