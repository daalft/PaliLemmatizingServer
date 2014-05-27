package palilemmatizingserver.handler;


import java.util.*;

import palilemmatizingserver.AppRuntime;
import palilemmatizingserver.handler.helper.RestrictGetter;
import de.general.jettyserver.*;
import de.general.json.JObject;
import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.morphology.element.*;


public class GeneratorHandler extends AbstractHandler
{

	@Override
	public ResponseContainer processRequest(AppRuntime ar,
			ClientRequest request, ILogInterface log) throws Exception {
		// optional arguments: word class, gender for noun, declension for verb
		log.info("Invoking generator");

		// ----

		RestrictGetter rg = new RestrictGetter();
		String word = request.getRequestParameter("word");
		//String json = request.getRequestParameter("restrict");
		
		String wc = rg.get("wc", request);
		
		String opt = "";
		if (wc != null && !wc.isEmpty()) {
			if (wc.equals("noun")) {
				opt = rg.get("gender", request);
			} else if (wc.equals("verb")) {
				opt = rg.get("declension", request);
			}
		}

		// ----

		
		List<ConstructedWord> constructedWords = ar.getMorphologyGenerator().generate(word, wc, opt);
				
		if ((constructedWords == null) || (constructedWords.size() == 0)) {
			return createError("No word forms generated!");
		}

		// ----
		
		return ResponseContainer.createJSONResponse((JObject)(ar.getFormatConverterManager().convert("generatedwordforms", "json", constructedWords)));
	}

}
