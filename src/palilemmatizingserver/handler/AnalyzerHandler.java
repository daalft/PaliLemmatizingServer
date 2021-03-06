package palilemmatizingserver.handler;


import java.util.ArrayList;
import java.util.List;

import palilemmatizingserver.AppRuntime;
import de.general.jettyserver.*;
import de.general.json.JArray;
import de.general.json.JObject;
import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.morphology.MorphologyAnalyzer;
import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.tools.WordConverter;


public class AnalyzerHandler extends AbstractHandler
{

	@Override
	public ResponseContainer processRequest(AppRuntime ar,
			ClientRequest request, ILogInterface log) throws Exception
			{
		log.info("Invoking analyzer");

		// ----------------------------------------------------------------
		// process parameters

		String word = request.getRequestParameter("word");
		JObject gramGrp = this.getParamJObject(request, "gramGrp");

		if (gramGrp == null) {
			List<JObject> gramGrps = this.getGramGrpFromDictionary(word, "LEMMA", ar, log);
			if (gramGrps == null) {
				log.warn("No grammar node found for " + word);
			}
			//	return ResponseContainer.createJSONResponse(EnumError.PROCESSING, WordConverter.toJObject("{}"));

			// Premature return block
			else if (gramGrps.size() > 1) {
				List<ConstructedWord> prematureResult = new ArrayList<ConstructedWord>();
				for (JObject innerGramGrp : gramGrps) {
					
					List<ConstructedWord> words = ar.getMorphologyAnalyzer().analyzeWithOptions(log, word);
					
					prematureResult.addAll(words);
				}
				JObject ret = (JObject)(ar.getFormatConverterManager().convert("generatedwordforms", "json", prematureResult, null));

				return ResponseContainer.createJSONResponse(ret);
			}
			// End premature return block

		} else {
			gramGrp = WordConverter.toJObject("{gramGrp:" +
					gramGrp.toJSON() + "}");
		}
		// ----------------------------------------------------------------
		// analyze

		String json;
		MorphologyAnalyzer ma = ar.getMorphologyAnalyzer();
		try {
			if (gramGrp == null)
				json = ma.analyzeWithDictionary(log, word, null);
			else
				json = ma.analyzeWithDictionary(log, word, gramGrp);
		} catch (Exception e) {
			return createError(e);
		}

		// ----------------------------------------------------------------
		// create response
		return createSuccessResponse(WordConverter.toJObject(json));
			}

}
