package jager.websocket.dbserver.database;

import java.util.List;

import jager.websocket.dbserver.model.Entity;

public interface DatabaseMethods
{
	void execute(String query);
	List<Entity> executeQuery(String query);
}

