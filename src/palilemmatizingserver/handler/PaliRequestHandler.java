package palilemmatizingserver.handler;

import palilemmatizingserver.AppRuntime;
import de.general.jettyserver.ClientRequest;
import de.general.jettyserver.IRequestHandler;
import de.general.jettyserver.ResponseContainer;
import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.ngram.NGramScorer;
/**
 * Handles incoming client requests
 * <p>
 * This class is a delegator that calls the relevant request handler
 * and passes the arguments to the relevant request handler. The class
 * checks whether the input is a valid Pali word before proceeding to
 * avoid NullPointerExceptions. However, if an invalid word passes the test,
 * a NullPointerException will be thrown anyway.
 * @author Work
 *
 */
public class PaliRequestHandler implements IRequestHandler<AppRuntime> {

	@Override
	public ResponseContainer processRequest(AppRuntime appRuntime,
			ClientRequest requestWrapper, ILogInterface logger) throws Exception {
		String word = requestWrapper.getRequestParameter("word");
		if (word == null || word.isEmpty()) {
			// 400 - Client Error - Bad Request
			return ResponseContainer.createTextResponse(400, "No word provided!");
		}
		boolean isPali = NGramScorer.getInstance().testHypothesis(word, "pi");
		// 422 - Client Error - Unprocessable Entity
		if (!isPali) return ResponseContainer.createTextResponse(422, "Not a valid Pali word!");
		return new HandlerStrategyManager().getHandler(requestWrapper.getRequestPath())
				.processRequest(appRuntime, requestWrapper, logger);
	}

}
