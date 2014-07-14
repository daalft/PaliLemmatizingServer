package palilemmatizingserver.handler;


import java.util.*;

import palilemmatizingserver.AppRuntime;
import de.general.jettyserver.*;
import de.general.json.JObject;
import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.morphology.element.*;
import de.unitrier.daalft.pali.tools.WordConverter;


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
		JObject gramGrp = getParamJObject(request, "gramGrp");
		
		if (gramGrp == null) {
			List<JObject> gramGrps = this.getGramGrpFromDictionary(word, "LEMMA", ar, log);
			
			// Pre-premature return block
			if (gramGrps == null)
				return ResponseContainer.createJSONResponse((JObject)(ar.getFormatConverterManager().convert("generatedwordforms", "json",ar.getMorphologyGenerator().generate(log, word, null))));
			
			// Premature return block
			if (gramGrps.size() > 1) {
				List<ConstructedWord> prematureResult = new ArrayList<ConstructedWord>();
				for (JObject innerGramGrp : gramGrps) {
					prematureResult.addAll(ar.getMorphologyGenerator().generate(log, word, innerGramGrp));
				}
				return ResponseContainer.createJSONResponse((JObject)(ar.getFormatConverterManager().convert("generatedwordforms", "json", prematureResult)));
			}
			// End premature return block
			
		} else {
			gramGrp = WordConverter.toJObject("{gramGrp:" +
					gramGrp.toJSON() + "}");
		}
		
		String[] restrictWordClasses = getStrArrayPropertyFromParamJSON(request, "restrict", "pos");

		// ----------------------------------------------------------------
		// generate words

		List<ConstructedWord> constructedWords;

		if (restrictWordClasses == null) {
			constructedWords = ar.getMorphologyGenerator().generate(log, word, gramGrp);
		} else {
			constructedWords = new ArrayList<ConstructedWord>();
			for (String wc : restrictWordClasses) {
				String opt = "";
				if (wc != null) {
					if (wc.equals("noun")) {
						opt = getStrPropertyFromParamJSON(request, "restrict", "gender");
					} else
					if (wc.equals("verb")) {
						opt = getStrPropertyFromParamJSON(request, "restrict", "declension");
					}
				}
				constructedWords.addAll(ar.getMorphologyGenerator().generate(log, word, gramGrp));
			}
		}
			
		if ((constructedWords == null) || (constructedWords.size() == 0)) {
			return createError("No word forms generated!");
		}

		// ----------------------------------------------------------------
		// return response
		
		return ResponseContainer.createJSONResponse((JObject)(ar.getFormatConverterManager().convert("generatedwordforms", "json", constructedWords)));
	}

}
