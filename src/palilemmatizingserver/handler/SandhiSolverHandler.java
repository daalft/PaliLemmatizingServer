package palilemmatizingserver.handler;


import java.util.ArrayList;
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
		String originalWord = word;
		String fromConvention = request.getRequestParameter("from");
		String toConvention = request.getRequestParameter("to");

		// internally, everything is treated as HKC!
		// because the rule file is in HKC
		ITransliterator tFromTo = PaliTransliterationMgr.Instance.get(fromConvention, "HKC");
		ITransliterator tToFrom = PaliTransliterationMgr.Instance.get("HKC", fromConvention);
		// if we have found no transliterator, 
		// but the input and output are HKC, go on
		if (((tFromTo == null) || (tToFrom == null)) && 
				(!fromConvention.equalsIgnoreCase("HKC") && 
						(!toConvention.equals("HKC")))) {
			throw new Exception("No suitable transliterator could be found!");
		}

		if (!fromConvention.toUpperCase().equals("HKC")) {
			word = tFromTo.transliterate(word);
		}
		// ----------------------------------------------------------------
		// resolve

		SandhiSolver sandhiSolver = ar.getSandhiSolver();
	 
		ArrayList<String> res = sandhiSolver.sandhiSplit(word);
		
		String[] result = new String[1];
		result[0] = originalWord;
		if (!res.get(0).equals(word)) {
			res.add(originalWord);
			result = res.toArray(new String[0]);	
		}

		// ----------------------------------------------------------------

		if (!toConvention.toUpperCase().equals("HKC")) {
			return this.createSuccessResponse((JObject)
					(ar.getFormatConverterManager().convert(
						"sandhisolverforms",
						"json",
						result,
						tToFrom
					))
				);
		}

		// create response
		return this.createSuccessResponse((JObject)
				(ar.getFormatConverterManager().convert(
					"sandhisolverforms",
					"json",
					result,
					null
				))
			);
	}

}
