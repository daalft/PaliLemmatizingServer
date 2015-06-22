package palilemmatizingserver;


import java.io.File;

import de.general.jettyserver.*;
import de.general.cfg.*;
import de.general.log.*;
import de.unitrier.daalft.pali.lexicon.CachedDictionaryLookup;
import de.unitrier.daalft.pali.lexicon.LexiconAdapter;
import de.unitrier.daalft.pali.morphology.paradigm.*;
import palilemmatizingserver.handler.*;
import palilemmatizingserver.handler.conv.*;
import tagger.CombinedTagger;
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
	LexiconAdapter lexiconAdapter;
	SandhiSolver sandhiSolver;
	CombinedTagger tagger;
	CachedDictionaryLookup dictionaryLookup;
	
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
		formatConverterManager.register(ConverterSandhiSolver.class);
		// read paragigm configuration

		ParadigmAccessor pa = new ParadigmAccessor();

		// initialize functional classes

		morphologyGenerator = new MorphologyGenerator(pa);
		wordclassStemmer = new WordclassStemmer(pa);
		defaultSandhiSplitter = new SandhiSplit();	// initialize with unlimited splitting depth
		sandhiMerge = new SandhiMerge();
		String domain = cfg.getDictDomain();
		int dPort = cfg.getDictPort();
		String dUser = cfg.getDictUser();
		String dPw = cfg.getDictPw();
		int maxCacheSize = cfg.getMaxCacheSize();
		int maxCacheDurationInSeconds = cfg.getMaxCacheDurationInSeconds();
		
		File fileA = new File(cfg.getSandhiRuleFileA());
		File fileB = new File(cfg.getSandhiRuleFileB());
		sandhiSolver = new SandhiSolver(fileA,fileB, domain, dPort, dUser, dPw, maxCacheSize, maxCacheDurationInSeconds);
		try {
			lexiconAdapter = new LexiconAdapter(cfg.getDictDomain(),
					cfg.getDictPort(), cfg.getDictUser(), cfg.getDictPw());
		} catch (Exception e) {
			throw e;
		}
		tagger = new CombinedTagger(cfg.getTaggerModel());
		lemmatizer = new Lemmatizer(pa);
		morphologyAnalyzer = new MorphologyAnalyzer(pa,lexiconAdapter);
		dictionaryLookup = new CachedDictionaryLookup(cfg.getDictDomain(), cfg.getDictPort(),cfg.getDictUser(), cfg.getDictPw(), maxCacheSize, maxCacheDurationInSeconds);
		
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

	public LexiconAdapter getLexiconAdapter() {
		return lexiconAdapter;
	}
	
	public SandhiSolver getSandhiSolver() {
		return sandhiSolver;
	}
	
	public CombinedTagger getTagger () {
		return tagger;
	}

	public CachedDictionaryLookup getDictionaryLookup() {
		return dictionaryLookup;
	}
}
