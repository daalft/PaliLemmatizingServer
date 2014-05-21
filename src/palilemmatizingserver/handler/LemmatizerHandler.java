package palilemmatizingserver.handler;

import palilemmatizingserver.AppRuntime;
import palilemmatizingserver.handler.helper.RestrictGetter;
import de.general.jettyserver.ClientRequest;
import de.general.jettyserver.ResponseContainer;
import de.general.json.JObject;
import de.general.json.JValue;
import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.morphology.Lemmatizer;

public class LemmatizerHandler extends AbstractHandler {

	@Override
	public ResponseContainer processRequest(AppRuntime ar,
			ClientRequest request, ILogInterface log) throws Exception {
		log.info("Invoking lemmatizer");
		RestrictGetter rg = new RestrictGetter();
		String word = request.getRequestParameter("word");
		// opt param: word class
		String wc = rg.get("wc", request);
		Lemmatizer l = new Lemmatizer();
		String json = l.lemmatizeWithDictionary(word, wc);
		JObject jsonData = new JObject();
		jsonData.add("success", new JValue(json));
		ResponseContainer rc = ResponseContainer.createJSONResponse(0, jsonData);
		return rc;
		
	}

}
