package jager.websocket.rest.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import jager.indoornav.wscconvention.model.LocationResult;
import jager.indoornav.wscconvention.model.NewLocation;
import jager.websocket.dbserver.database.LocationRepository;
import jager.websocket.dbserver.model.Entity;
import jager.websocket.dbserver.model.Location;

public class LocationResource extends CommonServerResource
{
	@Get("json")
	public LocationResult represent() throws IOException {
		String	username = getQueryValue("username");
		if(username== null || username.equals(""))throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
		Long from, to;
		String f = getQueryValue("from");
		if(f== null || f.equals("")) from = null;
		else from = Long.parseLong(f);
		String t = getQueryValue("to");
		if(t == null || t.equals("")) to = null;
		else to = Long.parseLong(t);
		List<Entity> locations = LocationRepository.getInstance().findUserLocations(username, from, to);
		List<NewLocation> returndata = new ArrayList<NewLocation>();
		for (Entity newLocation : locations)
		{
			Location loc = (Location) newLocation;
			returndata.add(new NewLocation(loc.getLatitude(), loc.getLongitude(), loc.getTime().getMillis(), username));
		}
		LocationResult res = new LocationResult(returndata);
		return res;
	} 
}
