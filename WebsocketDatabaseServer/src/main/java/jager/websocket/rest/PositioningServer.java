package jager.websocket.rest;

import org.restlet.*;
import org.restlet.data.Protocol;

public class PositioningServer
{
	public PositioningServer(int port)
	{
		this.port = port;
	}
	
	public PositioningServer()
	{
		this(2551);
	}
	
	private int port;
	private Server server;

	public void startComponentServer()
	{
		Component component = new Component();
		component.getDefaultHost().attach("/indoorpositioning", new PositioningApplication());
		server = new Server(Protocol.HTTP, port, component);
		try
		{
			server.start();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void stopComponentServer()
	{
		try
		{
			server.stop();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
