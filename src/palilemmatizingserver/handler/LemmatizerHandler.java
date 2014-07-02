package palilemmatizingserver.handler;


import palilemmatizingserver.AppRuntime;
import de.general.jettyserver.*;
import de.general.json.JObject;
import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.morphology.Lemmatizer;
import de.unitrier.daalft.pali.tools.WordConverter;


public class LemmatizerHandler extends AbstractHandler
{

	@Override
	public ResponseContainer processRequest(AppRuntime ar,
			ClientRequest request, ILogInterface log) throws Exception {
		log.info("Invoking lemmatizer");

		// ----------------------------------------------------------------
		// process parameters

		String word = request.getRequestParameter("word");
		// opt param: word class
		String pos = getStrPropertyFromParamJSON(request, "restrict", "pos");

		// ----------------------------------------------------------------
		// lemmatize
		
		Lemmatizer l = ar.getLemmatizer();
		String json = "";
		try {
			json = l.lemmatizeWithDictionary(log, word, pos);
		} catch (Exception e) {
			return createError(e);
		}
		
		// ----------------------------------------------------------------
		// create response
		
		return createSuccessResponse(WordConverter.toJObject(json));
	}

}
