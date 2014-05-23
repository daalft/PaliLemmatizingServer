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
		String word = request.getRequestParameter("word");
		SandhiSplit ss = new SandhiSplit();
		String json = "";
		// only the conversion can throw an error
		try {
			json = WordConverter.toJSONStringSplitter(word, ss.split(word, 0));
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
