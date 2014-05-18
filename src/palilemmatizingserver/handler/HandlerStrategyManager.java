package palilemmatizingserver.handler;

public class HandlerStrategyManager {

	public AbstractHandler getHandler(String path) {
		switch(path) {
		case "/api/json/morphgen":
			return new GeneratorHandler();
		case "/api/json/morphana":
			return new AnalyzerHandler();
		case "/api/json/lemma":
			return new LemmatizerHandler();
		case "/api/json/stem":
			return new StemmerHandler();
		case "/api/json/smerge":
			return new SandhiMergeHandler();
		case "/api/json/ssplit":
			return new SandhiSplitHandler();
		
			default: System.err.println("No suitable handler found for path " + path);
			return new NullHandler();
		}
	}
}
