package jager.websocket.rest.resources;

import org.restlet.resource.ServerResource;

import jager.websocket.dbserver.ServerController;

public class CommonServerResource extends ServerResource
{
	protected static ServerController serverController = ServerController.getInstance();
}
