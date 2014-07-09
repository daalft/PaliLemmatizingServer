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
			JObject[] entries = ar.getLexiconAdapter().getLemmaEntriesAsJObjectArray(word);
			if (entries == null || entries.length == 0) {
				log.error("Could not find " + word + " in the dictionary!");
				return ResponseContainer.createJSONResponse(EnumError.PROCESSING, WordConverter.toJObject("{}"));
			}
			// Premature return block
			else if (entries.length > 1) {
				
				log.debug("More than one dictionary entry found!");
				JObject innerGramGrp = null;
				List<ConstructedWord> prematureResult = new ArrayList<ConstructedWord>();
				for (JObject entry : entries) {
					try {
						innerGramGrp = WordConverter.toJObject("{"+entry.getProperty("gramGrp").toJSON()+"}");
						log.debug(""+entry.getProperty("gramGrp"));
						prematureResult.addAll(ar.getMorphologyGenerator().generate(log, word, innerGramGrp));
					} catch (Exception e) {
						log.warn("Could not find grammar node");
					}
				}
				return ResponseContainer.createJSONResponse((JObject)(ar.getFormatConverterManager().convert("generatedwordforms", "json", prematureResult)));
			}
			// End premature return block
			try {
				gramGrp = WordConverter.toJObject("{"+entries[0].getProperty("gramGrp").toJSON()+"}");
			} catch (Exception e) {
				log.warn("Entry does not contain grammar node");
			}
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
