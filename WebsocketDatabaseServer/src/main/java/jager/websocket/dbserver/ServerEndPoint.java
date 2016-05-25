package jager.websocket.dbserver;

import java.io.IOException;
import javax.websocket.*;
import javax.websocket.server.*;

@ServerEndpoint(value = "/websocketServerEndpoint")
public class ServerEndPoint
{
	private static ServerController serverController = ServerController.getInstance();
	
	@OnOpen
	public void handleOpen(Session userSession)
	{
		System.out.println(String.format("New client connected with session id [%s]", userSession.getId()));
		serverController.newClientConnected(userSession);
	}

	@OnMessage
	public void handleMessage(String message, Session userSession) throws IOException
	{
		serverController.processMessage(message, userSession);
	}

	@OnClose
	public void handleClose(Session userSession, CloseReason reason)
	{
		System.out.println(String.format("Client disconnected [%s]", userSession.getId()));
		serverController.clientDisconnected(userSession);
	}

}