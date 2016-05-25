package jager.websocket.dbserver.database;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionDataReader
{
	private ConnectionDataReader()
	{
		readResourceFile(openResourceFile());
	}

	private static ConnectionDataReader self;

	public static ConnectionDataReader getInstance()
	{
		if (self == null)
			self = new ConnectionDataReader();
		return self;
	}

	private Map<String, String> connData;
	private final String RESOURCE_FILENAME = "application.properties";
	private final String KEY_USERNAME = "username";
	private final String KEY_PASSWORD = "password";
	private final String KEY_CONNSTRING = "connstring";

	private File openResourceFile()
	{
		ClassLoader classLoader = getClass().getClassLoader();
		return new File(classLoader.getResource(RESOURCE_FILENAME).getPath().replace("%20", " "));
	}

	private void readResourceFile(File resource)
	{
		connData = new HashMap<String, String>();
		try
		{
			boolean fileExists = Files.exists(resource.toPath());
			if (!fileExists)
				return;
			List<String> connLines = Files.readAllLines(resource.toPath());
			for (String line : connLines)
			{
				String[] tags = line.split("=");
				connData.put(tags[0], tags[1]);
			}
		} catch (IOException e)
		{
		}
	}

	public String getDatabaseUsername()
	{
		if (connData.containsKey(KEY_USERNAME))
			return connData.get(KEY_USERNAME);
		return "";
	}

	public String getDatabasePassword()
	{
		if (connData.containsKey(KEY_PASSWORD))
			return connData.get(KEY_PASSWORD);
		return "";
	}

	public String getConnectionString()
	{
		if (connData.containsKey(KEY_CONNSTRING))
			return connData.get(KEY_CONNSTRING);
		return "";
	}
}
