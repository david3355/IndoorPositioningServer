package jager.websocket.dbserver;

import java.util.UUID;

import javax.websocket.Session;

import org.joda.time.DateTime;

import jager.websocket.dbserver.model.User;

public class Client
{
	public Client(Session userSession)
	{
		this.session = userSession;
		connectedAt = DateTime.now();
	}

	private Session session;
	private UUID clientID;
	private DateTime connectedAt;
	private User user;

	private final static String SKEY_USERNAME = "username";

	public UUID getClientID()
	{
		return clientID;
	}

	public void setClientID(UUID clientID)
	{
		this.clientID = clientID;
	}
	
	public Session getSession()
	{
		return session;
	}

	public User getUser()
	{
		return user;
	}

	public DateTime getConnectionDateTime()
	{
		return connectedAt;
	}

	public boolean isLoggedIn()
	{
		String username = getUsername();
		return username != null;
	}

	public void logIn(User user)
	{
		this.user = user;
		session.getUserProperties().put(SKEY_USERNAME, user.getUsername());
	}

	public String getUsername()
	{
		return (String) session.getUserProperties().get(SKEY_USERNAME);
	}

	public void logOut()
	{
		this.user = null;
		session.getUserProperties().clear();
	}
	
	public boolean clientEquals(String clientID)
	{
		return this.clientID!= null && this.clientID.toString().equals(clientID);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		Client c = (Client) obj;
		return c.session.getId().equals(this.session.getId());
	}
}
