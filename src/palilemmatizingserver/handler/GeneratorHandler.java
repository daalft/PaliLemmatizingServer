package palilemmatizingserver.handler;


import java.util.*;

import palilemmatizingserver.AppRuntime;
import de.general.jettyserver.*;
import de.general.json.JObject;
import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.morphology.element.*;


public class GeneratorHandler extends AbstractHandler
{

	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	@Override
	public ResponseContainer processRequest(AppRuntime ar,
			ClientRequest request, ILogInterface log) throws Exception
	{
		// optional arguments: word class, gender for noun, declension for verb
		log.info("Invoking generator");

		// ----------------------------------------------------------------
		// process parameters

		String word = request.getRequestParameter("word");
		//String json = request.getRequestParameter("restrict");
		
		String wc = getStrPropertyFromParamJSON(request, "restrict", "wc");
		
		String opt = "";
		if (wc != null) {
			if (wc.equals("noun")) {
				opt = getStrPropertyFromParamJSON(request, "restrict", "gender");
			} else
			if (wc.equals("verb")) {
				opt = getStrPropertyFromParamJSON(request, "restrict", "declension");
			}
		}

		// ----------------------------------------------------------------
		// generate words
		
		List<ConstructedWord> constructedWords = ar.getMorphologyGenerator().generate(log, word, wc, opt);
				
		if ((constructedWords == null) || (constructedWords.size() == 0)) {
			return createError("No word forms generated!");
		}

		// ----------------------------------------------------------------
		// return response
		
		return ResponseContainer.createJSONResponse((JObject)(ar.getFormatConverterManager().convert("generatedwordforms", "json", constructedWords)));
	}

}
