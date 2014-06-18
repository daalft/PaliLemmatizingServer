package palilemmatizingserver;


import palilemmatizingserver.handler.PaliRequestHandler;
import de.general.jettyserver.*;
import de.general.log.*;
import de.general.util.*;


/**
 *
 * @author knauth
 */
public class MyServer extends JettyServer<AppRuntime>
{

	public MyServer() throws Exception
	{
		super(AppRuntime.class);
	}

}
