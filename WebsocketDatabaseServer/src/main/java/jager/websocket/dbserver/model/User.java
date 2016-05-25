package jager.websocket.dbserver.model;


public class User extends Entity
{
	public User(long id, String username, String password, long lastKnownPositionID)
	{
		this.id = id;
		this.username = username;
		this.password = password;
		this.lastKnownPositionID = lastKnownPositionID;
	}
	
	public User()
	{
		
	}

	private long id;
	private String username;
	private String password;
	private String email;
	private String firstname;
	private String lastname;
	private long lastKnownPositionID;
	

	public long getId()
	{
		return id;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getFirstname()
	{
		return firstname;
	}

	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

	public String getLastname()
	{
		return lastname;
	}

	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}
	
	public long getLastKnownPositionID()
	{
		return lastKnownPositionID;
	}

	public void setLastKnownPositionID(long lastKnownPositionID)
	{
		this.lastKnownPositionID = lastKnownPositionID;
	}

}
