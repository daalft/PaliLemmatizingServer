package palilemmatizingserver;


import palilemmatizingserver.handler.PaliRequestHandler;
import de.general.jettyserver.*;
import de.general.log.*;
import de.general.util.*;


/**
 * This class contains the main startup code for the server.
 */
public class PaliLemmatizingServer
{

    public static void main(String[] args) throws Exception
    {
		// construct the server; while doing this the AppRuntime-object will be created and initialized automatically.
		MyServer server = new MyServer();

		// add request handlers
		server.add(new PaliRequestHandler());
		
		// start the server
		server.start();
    }

}
