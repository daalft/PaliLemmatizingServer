package palilemmatizingserver.handler;

import java.util.ArrayList;
import java.util.List;

import palilemmatizingserver.AppRuntime;
import de.general.jettyserver.ClientRequest;
import de.general.jettyserver.ResponseContainer;
import de.general.json.JObject;
import de.general.json.JValue;
import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.tools.WordConverter;

public class CombinedTaggerHandler extends AbstractHandler {

	@Override
	public ResponseContainer processRequest(AppRuntime ar,
			ClientRequest request, ILogInterface log) throws Exception {
		// retrieve sentence object
		String sentenceString = request.getRequestParameter("sentence");
		// convert to JObject
		JObject sentenceObject = WordConverter.toJObject(sentenceString);
		// retrieve tokens
		JObject[] tokens = sentenceObject.getPropertyObjectList("tokens");
		
		List<String> list = new ArrayList<String>();
		// collect tokens
		for (JObject token : tokens) {
			list.add(token.getPropertyStringValueNormalized("token"));
		}
		// tag sentence
		String[] tags = ar.getTagger().tag(list.toArray(new String[0]));
		
		for (int i = 0; i < tags.length; i++) {
			tokens[i].add("pos", new JValue(tags[i]));
		}
		return createSuccessResponse(sentenceObject);
	}

}
