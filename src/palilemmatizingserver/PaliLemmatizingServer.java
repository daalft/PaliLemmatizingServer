package palilemmatizingserver;


import palilemmatizingserver.handler.PaliRequestHandler;
import de.general.jettyserver.*;
import de.general.log.*;
import de.general.util.*;


/**
 *
 * @author knauth
 */
public class PaliLemmatizingServer
{

	private static class MyServer extends JettyServer<AppRuntime>
	{
		public MyServer() throws Exception
		{
			super(AppRuntime.class);
		}
	}

    public static void main(String[] args) throws Exception
    {
		MyServer server = new MyServer();
		server.add(new PaliRequestHandler());
		server.start();
    }

}
