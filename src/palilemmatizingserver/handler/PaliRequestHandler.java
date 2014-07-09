package palilemmatizingserver.handler;


import palilemmatizingserver.AppRuntime;
import de.general.jettyserver.*;
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
public class PaliRequestHandler extends AbstractHandler
{

	private HandlerStrategyManager strategy;

	public PaliRequestHandler()
	{
		strategy = new HandlerStrategyManager();
	}

	@Override
	public ResponseContainer processRequest(AppRuntime appRuntime,
			ClientRequest requestWrapper, ILogInterface logger) throws Exception
	{
		try {
			// verify basic arguments

			verifyParamStrPali(requestWrapper, "word", logger);

			// delegate processing to a dedicated handler instance

			AbstractHandler selectedHandler = strategy.getHandler(requestWrapper.getRequestPath(), logger);
			return selectedHandler.processRequest(appRuntime, requestWrapper, logger);

		} catch (Exception e) {
			// some unexpected error occurred: return error

			return createError(e);
		}
	}

}
