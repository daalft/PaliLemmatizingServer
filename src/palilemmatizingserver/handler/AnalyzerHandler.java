package palilemmatizingserver.handler;


import palilemmatizingserver.AppRuntime;
import de.general.jettyserver.*;
import de.general.json.JObject;
import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.morphology.MorphologyAnalyzer;
import de.unitrier.daalft.pali.tools.WordConverter;


public class AnalyzerHandler extends AbstractHandler
{

	@Override
	public ResponseContainer processRequest(AppRuntime ar,
			ClientRequest request, ILogInterface log) throws Exception
	{
		log.info("Invoking analyzer");

		// ----------------------------------------------------------------
		// process parameters

		String word = request.getRequestParameter("word");
		String wc = getStrPropertyFromParamJSON(request, "restrict", "wc");
		
		// ----------------------------------------------------------------
		// analyze
		
		String json;
		MorphologyAnalyzer ma = ar.getMorphologyAnalyzer();
		try {
			json = ma.analyzeWithDictionary(log, word, wc);
		} catch (Exception e) {
			return createError(e);
		}
		
		// ----------------------------------------------------------------
		// create response
		
		return createSuccessResponse(WordConverter.toJObject(json));
	}

}
