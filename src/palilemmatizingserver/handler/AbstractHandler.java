package palilemmatizingserver.handler;


import java.util.*;
import java.io.*;

import palilemmatizingserver.AppRuntime;
import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.ngram.*;
import de.general.jettyserver.*;
import de.general.json.*;
import de.general.jettyserver.ClientRequest;
import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.tools.WordConverter;


public abstract class AbstractHandler implements IRequestHandler<AppRuntime>
{

	public String verifyParamStr(ClientRequest requestWrapper, String name) throws Exception
	{
		String word = requestWrapper.getRequestParameter(name);
		if (word == null || word.isEmpty()) {
			throw new Exception("Parameter \"" + name + "\" is missing or empty!");
		}
		return word;
	}

	public String verifyParamStrPali(ClientRequest requestWrapper, String name, ILogInterface log) throws Exception
	{
		String word = verifyParamStr(requestWrapper, name);

		boolean isPali = NGramScorer.getInstance().testHypothesis(word, "pi");
		// 422 - Client Error - Unprocessable Entity
		if (!isPali) log.warn("Word " + word + " failed ngram validation.");

		return word;
	}

	/**
	 * Returns the requested property, <b>iff</b> <ul><li>a <em>restrict</em>
	 * parameter is present</li><li>the <em>restrict</em> parameter contains the
	 * requested property</li></ul>
	 * If either condition fails, the empty string is returned 
	 * @param property property to retrieve
	 * @param request client request
	 * @return property if present
	 * @throws Exception
	 */
	public String getStrPropertyFromParamJSON(ClientRequest requestWrapper, String name, String property) throws Exception
	{
		String sRestrict = requestWrapper.getRequestParameter(name);
		if (sRestrict == null) return null;

		JObject jo = WordConverter.toJObject(sRestrict);
		JToken val = jo.getValue(property);
		if (val == null) return null;

		String sVal = val.toString();
		if (sVal.isEmpty()) return null;
		return sVal;
	}

	public JObject getParamJObject(ClientRequest requestWrapper, String name) throws Exception
	{
		String sRestrict = requestWrapper.getRequestParameter(name);
		if (sRestrict == null) return null;

		JObject jo = WordConverter.toJObject(sRestrict);
		return jo;
	}

	public String[] getStrArrayPropertyFromParamJSON(ClientRequest requestWrapper, String name, String property) throws Exception
	{
		String sRestrict = requestWrapper.getRequestParameter(name);
		if (sRestrict == null) return null;

		JObject jo = WordConverter.toJObject(sRestrict);
		JToken val = jo.getValue(property);
		if (val == null) return null;

		String sVal = val.toString();
		if (sVal.isEmpty()) return null;

		return sVal.split(",");
	}

	public ResponseContainer createError(String msg) throws Exception
	{
		JObject jsonData = new JObject();
		JObject errmsg = new JObject();
		jsonData.add("error", errmsg);
		errmsg.add("errMsg", new JValue("Error!"));
		errmsg.add("details", new JValue(msg));

		return ResponseContainer.createJSONResponse(EnumError.PROCESSING, jsonData);
	}

	public ResponseContainer createError(Exception ee) throws Exception
	{
		JObject jsonData = new JObject();
		JObject errmsg = new JObject();
		jsonData.add("error", errmsg);
		errmsg.add("errMsg", new JValue(ee.getMessage()));

		StringWriter sw = new StringWriter();
		ee.printStackTrace(new PrintWriter(sw));
		String[] stackTraceLines = sw.toString().replaceAll("\\r", "").split("\\n");

		JArray a = new JArray();
		for (String s : stackTraceLines) {
			a.add(new JValue(s.trim()));
		}

		errmsg.add("details", a);

		return ResponseContainer.createJSONResponse(EnumError.PROCESSING, jsonData);
	}

	public ResponseContainer createSuccessResponse(JObject data) throws Exception
	{
		JObject jsonData = new JObject();
		jsonData.add("success", data);
		ResponseContainer rc = ResponseContainer.createJSONResponse(jsonData);
		return rc;
	}

	public List<JObject> getGramGrpFromDictionary (String word, String collection, AppRuntime ar, ILogInterface log) throws Exception {
		JObject gramGrp = null;

		JObject[] entries = ar.getLexiconAdapter().getLemmaEntriesAsJObjectArray(word);
		if (entries == null || entries.length == 0) {
			log.error("Could not find " + word + " in the dictionary!");
			return null;
		}

		else if (entries.length > 1) {

			log.debug("More than one dictionary entry found!");
			JObject innerGramGrp = null;
			List<JObject> innerJObjects = new ArrayList<JObject>();
			for (JObject entry : entries) {
				try {
					innerGramGrp = WordConverter.toJObject("{"+entry.getProperty("gramGrp").toJSON()+"}");

					innerJObjects.add(innerGramGrp);
				} catch (Exception e) {
					
					log.warn("Could not find grammar node");
					log.warn("Falling back to general generation!");
					return null;
				}
			}
			return innerJObjects;
		}
		// End premature return block
		try {
			gramGrp = WordConverter.toJObject("{"+entries[0].getProperty("gramGrp").toJSON()+"}");
		} catch (Exception e) {
			log.warn("Entry does not contain grammar node");
		}
		return Collections.singletonList(gramGrp);
	}
}
