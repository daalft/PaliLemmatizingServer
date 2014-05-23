package palilemmatizingserver.handler;


import palilemmatizingserver.AppRuntime;
import de.general.jettyserver.ClientRequest;
import de.general.jettyserver.ResponseContainer;
import de.general.json.JObject;
import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.phonology.SandhiMerge;
import de.unitrier.daalft.pali.tools.WordConverter;


public class SandhiMergeHandler extends AbstractHandler
{

	@Override
	public ResponseContainer processRequest(AppRuntime ar,
			ClientRequest request, ILogInterface log) throws Exception {
		log.info("Invoking sandhi merger");
		String w1 = request.getRequestParameter("word");
		String w2 = request.getRequestParameter("word2");
		if (w1 == null || w2 == null || w1.isEmpty() || w2.isEmpty()) {
			return createError("word and word2 must not be empty!");
		}
		SandhiMerge sm = new SandhiMerge();
		String json = "";
		// only the conversion can throw an error here...
		try {
			json = WordConverter.toJSONStringMerger(w1,w2,sm.merge(w1,w2));
		} catch (Error e) {
			return createError(e.getMessage());
		}
		JObject jsonData = new JObject();
		JObject pjson = WordConverter.toJObject(json);
		jsonData.add("success", pjson);
		ResponseContainer rc = ResponseContainer.createJSONResponse(jsonData);
		return rc;
	}

}
