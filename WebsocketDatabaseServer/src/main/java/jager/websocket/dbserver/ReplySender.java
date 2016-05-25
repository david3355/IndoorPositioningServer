package jager.websocket.dbserver;

import java.io.IOException;
import java.util.List;

import javax.websocket.Session;

import org.glassfish.tyrus.core.TyrusSession;

public class ReplySender
{
	public static boolean sendMessage(Session session, String message)
	{
		try
		{
			session.getBasicRemote().sendText(message);
			return true;
		} catch (IOException e)
		{
			return false;
		}
	}
	
	public static void broadcast(Session session, String message)
	{
		((TyrusSession) session).broadcast(message);
	}
	
	public static void sendToActiveUsers(List<Client> clients, Session session, String message)
	{
		for (Client client : clients)
		{
			if(!client.getSession().equals(session) && client.isLoggedIn())
				sendMessage(client.getSession(), message);
		}
	}
}
