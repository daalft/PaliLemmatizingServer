package palilemmatizingserver;


import de.general.jettyserver.*;
import de.general.cfg.*;
import de.general.jettyserver.*;
import de.general.log.*;

import de.unitrier.daalft.pali.morphology.paradigm.*;

import palilemmatizingserver.handler.*;
import palilemmatizingserver.handler.conv.*;

import de.unitrier.daalft.pali.morphology.*;
import de.unitrier.daalft.pali.phonology.*;


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
	
	MorphologyGenerator morphologyGenerator;
	WordclassStemmer wordclassStemmer;
	SandhiSplit defaultSandhiSplitter;
	SandhiMerge sandhiMerge;
	Lemmatizer lemmatizer;
	MorphologyAnalyzer morphologyAnalyzer;

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
		
		// read paragigm configuration

		ParadigmAccessor pa = new ParadigmAccessor();

		// initialize functional classes

		morphologyGenerator = new MorphologyGenerator(pa);
		wordclassStemmer = new WordclassStemmer(pa);
		defaultSandhiSplitter = new SandhiSplit();	// initialize with unlimited splitting depth
		sandhiMerge = new SandhiMerge();
		lemmatizer = new Lemmatizer(pa);
		morphologyAnalyzer = new MorphologyAnalyzer(pa);
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

	////////////////////////////////////////////////////////////////

	public MorphologyAnalyzer getMorphologyAnalyzer()
	{
		return morphologyAnalyzer;
	}

	public FormatConverterManager getFormatConverterManager()
	{
		return formatConverterManager;
	}

	public MorphologyGenerator getMorphologyGenerator()
	{
		return morphologyGenerator;
	}

	public WordclassStemmer getWordclassStemmer()
	{
		return wordclassStemmer;
	}

	public SandhiSplit getDefaultSandhiSplitter()
	{
		return defaultSandhiSplitter;
	}

	public SandhiMerge getSandhiMerger()
	{
		return sandhiMerge;
	}

	public Lemmatizer getLemmatizer()
	{
		return lemmatizer;
	}

}
