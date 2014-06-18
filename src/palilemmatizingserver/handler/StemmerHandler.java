package palilemmatizingserver.handler;

import palilemmatizingserver.AppRuntime;
import de.general.jettyserver.ClientRequest;
import de.general.jettyserver.ResponseContainer;
import de.general.json.JObject;
import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.morphology.WordclassStemmer;
import de.unitrier.daalft.pali.tools.WordConverter;

public class StemmerHandler extends AbstractHandler {

	@Override
	public ResponseContainer processRequest(AppRuntime ar,
			ClientRequest request, ILogInterface log) throws Exception {
		log.info("Invoking stemmer");

		// ----------------------------------------------------------------
		// process parameters

		String word = request.getRequestParameter("word");
		String wc = getStrPropertyFromParamJSON(request, "restrict", "wc");

		// ----------------------------------------------------------------
		// stem

		WordclassStemmer wcs = ar.getWordclassStemmer();
		String json;
		try {
			json = WordConverter.toJSONStringStemmer(word, wcs.stem(word, wc));
		} catch (Exception e) {
			return createError(e);
		}
		
		// ----------------------------------------------------------------
		// create response
		
		return createSuccessResponse(WordConverter.toJObject(json));
	}

}
