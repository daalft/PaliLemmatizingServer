package palilemmatizingserver.handler.helper;

import de.general.jettyserver.ClientRequest;
import de.general.json.JObject;
import de.general.json.JToken;
import de.unitrier.daalft.pali.tools.WordConverter;

/**
 * This class accesses a requests restrict information if present
 * and returns valid information (even empty or null if not accessible)
 * @author Work
 *
 */
public class RestrictGetter {

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
	public String get(String property, ClientRequest request) throws Exception {
		if (request.getRequestParameter("restrict") == null)
			return "";
		JObject jo = WordConverter.toJObject(request.getRequestParameter("restrict"));
		JToken val = jo.getValue(property);
		if (val == null) 
			return "";
		return val.toString();
	}
}
