package palilemmatizingserver.handler;

import palilemmatizingserver.AppRuntime;
import de.general.jettyserver.ClientRequest;
import de.general.jettyserver.ResponseContainer;
import de.general.json.JObject;
import de.general.json.JValue;
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
		String json = WordConverter.toJSONStringSplitter(word, ss.split(word, 0));
		JObject jsonData = new JObject();
		jsonData.add("success", new JValue(json));
		ResponseContainer rc = ResponseContainer.createJSONResponse(0, jsonData);
		return rc;
	}

}
