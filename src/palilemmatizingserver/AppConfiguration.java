package palilemmatizingserver;


import java.io.*;
import java.util.*;

import de.general.log.*;
import de.general.util.*;
import de.general.cfg.*;



/**
 * This class is meant to read all configuration values from the configuration file and maintain it within variables
 */
public class AppConfiguration
{

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	ILogInterface defaultStdOutLogger;
	IConfigurationLoader cfgLoader;

	int port;
	String[] allowedIPAddresses;
	String sandhiRuleFileA, sandhiRuleFileB;
	
	int dictPort;
	String dictDomain, dictUser, dictPw;
	
	String taggerModel;
	
	int maxCacheSize, maxCacheDurationInSeconds;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public AppConfiguration(IConfigurationLoader cfgLoader, ILogInterface defaultStdOutLogger)
	{
		this.defaultStdOutLogger = defaultStdOutLogger;
		this.cfgLoader = cfgLoader;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void load() throws Exception
	{
		AbstractConfigurationReader r = cfgLoader.loadConfiguration(defaultStdOutLogger);

		// extract configuration data

		port = r.getPortE("Server", "serverPort");
		allowedIPAddresses = r.getStringList("Server", "allowedIPAddresses");
		sandhiRuleFileA = r.getString("SandhiRules", "filenameA");
		sandhiRuleFileB = r.getString("SandhiRules", "filenameB");
		dictDomain = r.getString("Dictionary", "domain");
		dictPort = r.getInt("Dictionary", "port");
		dictUser = r.getString("Dictionary", "user");
		dictPw = r.getString("Dictionary", "pw");
		taggerModel = r.getString("Tagger", "model");
		maxCacheSize = r.getInt("Cache", "maxSize");
		maxCacheDurationInSeconds = r.getInt("Cache", "maxDurationInSeconds");
		
	}

	public String[] getAllowedIPAddresses()
	{
		return allowedIPAddresses;
	}

	public int getPort()
	{
		return port;
	}

	public String getSandhiRuleFileA() {
		return sandhiRuleFileA;
	}

	public String getSandhiRuleFileB() {
		return sandhiRuleFileB;
	}

	public String getDictDomain() {
		return dictDomain;
	}

	public int getDictPort () {
		return dictPort;
	}
	
	public String getDictUser () {
		return dictUser;
	}
	
	public String getDictPw () {
		return dictPw;
	}

	public String getTaggerModel() {
		return taggerModel;
	}

	public int getMaxCacheSize() {
		return maxCacheSize;
	}
	
	public int getMaxCacheDurationInSeconds() {
		return maxCacheDurationInSeconds;
	}
}
