package jager.websocket.dbserver;

import java.io.*;
import java.util.*;
import org.glassfish.tyrus.server.Server;

import jager.websocket.rest.PositioningServer;

public class Application
{

	public static void main(String[] args)
	{
		runServer();
	}

	public static void runServer()
	{
		final int websocketPORT = 8081;
		final int restPORT = 2551;
		Map<String, Object> properties = new HashMap();
		Server server = new Server("localhost", websocketPORT, "/WebSocketServer", properties, ServerEndPoint.class);
		PositioningServer pserver = new PositioningServer(restPORT);	// REST
		try
		{
			System.out.println("Launching websocket server...");
			server.start();
			System.out.println("Websocket server started!");
			System.out.println("Launching RESTful Server...");
			pserver.startComponentServer();
			System.out.println("RESTful Server is running");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Please press a key to stop the servers.");
			reader.readLine();
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		} finally
		{
			server.stop();
			pserver.stopComponentServer();
			System.out.println("Servers stopped...");
		}
	}
	
	

}
