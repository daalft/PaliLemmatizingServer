package palilemmatizingserver.handler;


import palilemmatizingserver.AppRuntime;
import palilemmatizingserver.handler.helper.RestrictGetter;
import de.general.jettyserver.ClientRequest;
import de.general.jettyserver.ResponseContainer;
import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.morphology.MorphologyGenerator;
import de.unitrier.daalft.pali.tools.WordConverter;

public class GeneratorHandler extends AbstractHandler {

	@Override
	public ResponseContainer processRequest(AppRuntime ar,
			ClientRequest request, ILogInterface log) throws Exception {
		// optional arguments: word class, gender for noun, declension for verb
		log.info("Invoking generator");
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
		MorphologyGenerator mg = new MorphologyGenerator();
		String out = WordConverter.toJSONStringGenerator(mg.generate(word, wc, opt));
		ResponseContainer rc = ResponseContainer.createTextResponse(0, out);
		return rc;
	}

}
