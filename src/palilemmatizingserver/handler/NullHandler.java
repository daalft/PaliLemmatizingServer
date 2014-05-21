package palilemmatizingserver.handler;

import palilemmatizingserver.AppRuntime;
import de.general.jettyserver.ClientRequest;
import de.general.jettyserver.ResponseContainer;
import de.general.log.ILogInterface;

public class NullHandler extends AbstractHandler {

	@Override
	public ResponseContainer processRequest(AppRuntime appRuntime,
			ClientRequest request, ILogInterface log) throws Exception {
		// 501 - Server Error - Not implemented
		return this.createError("The requested operation " + request.getRequestPath() + " is not supported!");
	}

}
