package palilemmatizingserver.handler;


import java.util.List;

import palilemmatizingserver.AppRuntime;
import de.general.jettyserver.*;
import de.general.json.JObject;
import de.general.log.ILogInterface;
import de.general.transliteration.ITransliterator;
import de.pali.PaliTransliterationMgr;
import de.unitrier.daalft.pali.phonology.SandhiSolver;


public class SandhiSolverHandler extends AbstractHandler
{

	@Override
	public ResponseContainer processRequest(AppRuntime ar,
			ClientRequest request, ILogInterface log) throws Exception
	{
		log.info("Invoking sandhi solver");

		// ----------------------------------------------------------------
		// process parameters

		String word = request.getRequestParameter("word");

		String fromConvention = request.getRequestParameter("from");
		String toConvention = request.getRequestParameter("to");

		ITransliterator tFromTo = PaliTransliterationMgr.Instance.get(fromConvention, toConvention);
		ITransliterator tToFrom = PaliTransliterationMgr.Instance.get(toConvention, fromConvention);

		if ((tFromTo == null) || (tToFrom == null)) {
			throw new Exception("No suitable transliterator could be found!");
		}

		if (!fromConvention.toUpperCase().equals("HKC")) {
			word = tFromTo.transliterate(word);
		}
		// ----------------------------------------------------------------
		// resolve

		SandhiSolver sandhiSolver = ar.getSandhiSolver();

		List<String> result = sandhiSolver.resolveSandhiSingleWord(word);

		result.add(word);

		// ----------------------------------------------------------------

		if (!toConvention.toUpperCase().equals("HKC")) {
			return ResponseContainer.createJSONResponse(
					(JObject)(ar.getFormatConverterManager().convert(
							"sandhisolverforms",
							"json",
							result,
							tToFrom
							))
					);
		}

		// create response
		return ResponseContainer.createJSONResponse((JObject)
				(ar.getFormatConverterManager().convert(
						"sandhisolverforms", 
						"json", 
						result, 
						null))
				);
	}

}
