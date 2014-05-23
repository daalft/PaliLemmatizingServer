package palilemmatizingserver.handler;


import palilemmatizingserver.AppRuntime;
import de.general.jettyserver.*;
import de.general.json.JObject;
import de.general.json.JValue;


public abstract class AbstractHandler implements IRequestHandler<AppRuntime>
{

	ResponseContainer createError(String msg) throws Exception
	{
		JObject jsonData = new JObject();
		JObject errmsg = new JObject();
		jsonData.add("error", errmsg);
		errmsg.add("errMsg", new JValue("Error!"));
		errmsg.add("details", new JValue(msg));
		
		return ResponseContainer.createJSONResponse(EnumError.PROCESSING, jsonData);
	}
}
