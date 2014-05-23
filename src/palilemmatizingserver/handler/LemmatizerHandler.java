package palilemmatizingserver.handler;


import palilemmatizingserver.AppRuntime;
import palilemmatizingserver.handler.helper.RestrictGetter;
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
		RestrictGetter rg = new RestrictGetter();
		String word = request.getRequestParameter("word");
		// opt param: word class
		String wc = rg.get("wc", request);
		Lemmatizer l = new Lemmatizer();
		String json = "";
		try {
			json = l.lemmatizeWithDictionary(word, wc);
		} catch (Exception e) {
			return createError(e.getMessage());
		}
		
		JObject jsonData = new JObject();
		JObject pjson = WordConverter.toJObject(json);
		jsonData.add("success", pjson);
		ResponseContainer rc = ResponseContainer.createJSONResponse(jsonData);
		return rc;
		
	}

}
