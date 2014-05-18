package palilemmatizingserver.handler;

import palilemmatizingserver.AppRuntime;
import palilemmatizingserver.handler.helper.RestrictGetter;
import de.general.jettyserver.ClientRequest;
import de.general.jettyserver.ResponseContainer;
import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.morphology.WordclassStemmer;
import de.unitrier.daalft.pali.tools.WordConverter;

public class StemmerHandler extends AbstractHandler {

	@Override
	public ResponseContainer processRequest(AppRuntime ar,
			ClientRequest request, ILogInterface log) throws Exception {
		log.info("Invoking stemmer");
		RestrictGetter rg = new RestrictGetter();
		WordclassStemmer wcs = new WordclassStemmer();
		String word = request.getRequestParameter("word");
		String wc = rg.get("wc", request);
		String json = WordConverter.toJSONStringStemmer(word,wcs.stem(word,wc));
		return ResponseContainer.createTextResponse(0, json);
	}

}
