package palilemmatizingserver.handler;


import palilemmatizingserver.AppRuntime;
import palilemmatizingserver.handler.helper.RestrictGetter;
import de.general.jettyserver.*;
import de.general.json.JObject;
import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.morphology.MorphologyGenerator;
import de.unitrier.daalft.pali.tools.WordConverter;


public class GeneratorHandler extends AbstractHandler
{

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
		String json = "";
		try{
			json = WordConverter.toJSONStringGenerator(mg.generate(word, wc, opt));
		} catch (Exception e) {
			return createError(e.getMessage());
		}
		
		JObject jsonData = new JObject();
		JObject pjson = WordConverter.toJObject(json);
		jsonData.add("success", pjson);
		ResponseContainer rc = ResponseContainer.createJSONResponse(jsonData);
		return rc;
	}

}
