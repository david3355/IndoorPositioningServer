package jager.websocket.dbserver.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jager.websocket.dbserver.model.Entity;
import jager.websocket.dbserver.model.Location;
import jager.websocket.dbserver.model.User;

public class UserRepository extends MySqlDatabaseAccess implements Repository<User, Long>
{
	private UserRepository()
	{

	}

	private static UserRepository self;

	public static UserRepository getInstance()
	{
		if (self == null)
			self = new UserRepository();
		return self;
	}

	private final static String TABLE = "user";

	private final static String COL_ID = "id";
	private final static String COL_USERNAME = "username";
	private final static String COL_PASSWORD = "password";
	private final static String COL_LNPOSID = "lnposition_id";
	private final static String COL_EMAIL = "email";
	private final static String COL_FNAME = "fname";
	private final static String COL_LNAME = "lname";


	public void save(User user)
	{
		String q = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES ('%s', '%s', %s, '%s', '%s', '%s')", TABLE, COL_USERNAME,
				COL_PASSWORD, COL_LNPOSID, COL_EMAIL, COL_FNAME, COL_LNAME, user.getUsername(), user.getPassword(), user.getLastKnownPositionID(), user.getEmail(), user.getFirstname(), user.getLastname());
		execute(q);
		// TODO: Itt a lekérdezés után tudni kéne a beszúrt elem id-ját és be kéne állítani az objektumnak!!!
	}

	public List<Entity> findAll()
	{
		String q = String.format("SELECT * FROM %s", TABLE);
		List<Entity> resultdata = executeQuery(q);
		return resultdata;
	}

	public void delete(Long id)
	{
		String q = String.format("DELETE FROM %s WHERE %=%", TABLE, COL_ID, id);
		execute(q);
	}
	
	public User findEntity(Long id)
	{
		String q = String.format("SELECT * FROM %s WHERE %s=%s", TABLE, COL_ID, id);
		List<Entity> resultdata = executeQuery(q);
		if(resultdata.size()>0) return (User)resultdata.get(0);
		return null;
	}

	public User userExists(String username)
	{
		User u = null;
		String q = String.format("SELECT * FROM %s WHERE %s='%s'", TABLE, COL_USERNAME, username);
		List<Entity> resultdata = executeQuery(q);
		if(resultdata.size()>0) u = (User)resultdata.get(0);
		return u;
	}
	
	public User getUser(String username)
	{
		return userExists(username);
	}

	public void addUser(User user)
	{
		save(user);
	}

	@Override
	protected List<Entity> transformResultSet(ResultSet resultdata)
	{
		User u;
		List<Entity> users = new ArrayList<Entity>();
		long id, lnposition_id;
		String username, password;

		try
		{
			if (!resultdata.first())
				return users;
			do
			{
				
				id = Long.parseLong(resultdata.getString(COL_ID));
				username = resultdata.getString(COL_USERNAME);
				password = resultdata.getString(COL_PASSWORD);
				lnposition_id = Long.parseLong(resultdata.getString(COL_LNPOSID));
				u = new User(id, username, password, lnposition_id);
				users.add(u);
			} while (resultdata.next());
		} catch (Exception e)
		{
		}
		return users;
	}

	

}
