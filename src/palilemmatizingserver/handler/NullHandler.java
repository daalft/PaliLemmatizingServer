package palilemmatizingserver.handler;

import palilemmatizingserver.AppRuntime;
import palilemmatizingserver.handler.helper.RestrictGetter;
import de.general.jettyserver.ClientRequest;
import de.general.jettyserver.ResponseContainer;
import de.general.log.ILogInterface;

public class NullHandler extends AbstractHandler {

	@Override
	public ResponseContainer processRequest(AppRuntime appRuntime,
			ClientRequest request, ILogInterface log) throws Exception {
		// 501 - Server Error - Not implemented
		RestrictGetter rg = new RestrictGetter();
		rg.get("tusse", request);
		return ResponseContainer.createTextResponse(501, "Unsupported operation!");
	}

}
