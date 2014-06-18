package palilemmatizingserver.handler;

import palilemmatizingserver.AppRuntime;
import de.general.jettyserver.ClientRequest;
import de.general.jettyserver.ResponseContainer;
import de.general.json.JObject;
import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.phonology.SandhiSplit;
import de.unitrier.daalft.pali.tools.WordConverter;

public class SandhiSplitHandler extends AbstractHandler {

	@Override
	public ResponseContainer processRequest(AppRuntime ar,
			ClientRequest request, ILogInterface log) throws Exception {
		log.info("Invoking sandhi splitter");

		// ----------------------------------------------------------------
		// process parameters

		String word = request.getRequestParameter("word");

		// ----------------------------------------------------------------
		// split

		String json;
		SandhiSplit ss = ar.getDefaultSandhiSplitter();
		// only the conversion can throw an error
		try {
			json = WordConverter.toJSONStringSplitter(word, ss.split(word, 0));
		} catch (Exception e) {
			return createError(e);
		}
		
		// ----------------------------------------------------------------
		// create response
		
		return createSuccessResponse(WordConverter.toJObject(json));
	}

}
