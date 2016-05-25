package jager.websocket.dbserver.database;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import jager.websocket.dbserver.model.Entity;

public abstract class MySqlDatabaseAccess implements DatabaseMethods
{
	static
	{
		ConnectionDataReader conndata = ConnectionDataReader.getInstance();
		DBUSER = conndata.getDatabaseUsername();
		DBPWD = conndata.getDatabasePassword();
		CONNSTRING = conndata.getConnectionString();
	}
	
	private static String DBUSER;
	private static String DBPWD;
	private static String CONNSTRING = "jdbc:mysql://localhost/indoorpositioning";

	private Connection open() throws SQLException
	{
		Connection connection = DriverManager.getConnection(CONNSTRING, DBUSER, DBPWD); // kitenni resource-ba
		return connection;
	}

	private void close(Connection connection) throws SQLException
	{
		connection.close();
	}

	public void execute(String query)
	{
		try
		{
			Connection conn = open();
			Statement command = conn.createStatement();
			command.execute(query);		// Autocommit
			close(conn);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public List<Entity> executeQuery(String query)
	{
		List<Entity> entities = null;
		try
		{
			Connection conn = open();
			Statement command = conn.createStatement();
			ResultSet resultdata = command.executeQuery(query);
			entities = transformResultSet(resultdata);
			close(conn);
			return entities;
		} catch (SQLException e)
		{
			return null;
		}
	}
	
	protected abstract List<Entity> transformResultSet(ResultSet resultset);
	

}
