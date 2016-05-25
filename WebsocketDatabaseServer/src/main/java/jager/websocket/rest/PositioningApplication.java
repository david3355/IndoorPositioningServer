package jager.websocket.rest;

import org.restlet.*;
import org.restlet.routing.Router;

import jager.websocket.rest.resources.LocationResource;
import jager.websocket.rest.resources.LoginResource;
import jager.websocket.rest.resources.LogoutResource;
import jager.websocket.rest.resources.RegistrationResource;

public class PositioningApplication extends Application
{
	/*
	 * Example queries:
	 * 
	 * Get movie by ID:
	 * http://localhost:2551/rottentomatoes/movie/star_wars_episode_vii_the_force_awakens
	 * 
	 * Search for movies (and optionally sort):
	 * http://localhost:2551/rottentomatoes/search?searchTerm=inception&sortby=tomatometer&order=desc
	 * http://localhost:2551/rottentomatoes/search?searchTerm=star%20wars&sortby=tomatometer
	 * http://localhost:2551/rottentomatoes/search?searchTerm=star%20wars&sortby=releaseyear
	 * http://localhost:2551/rottentomatoes/search?searchTerm=inception
	 * 
	 */
	
	@Override
	public Restlet createInboundRoot() {
		Router	router = new Router(getContext());
		router.setDefaultMatchingQuery(true);
		router.attach("/locations/{userid}", LocationResource.class); // ez még nincs kidolgozva, lehet hogy törölni kéne
		router.attach("/getlocations?{query}", LocationResource.class);
		router.attach("/login?{query}", LoginResource.class);
		router.attach("/logout?{query}", LogoutResource.class);
		router.attach("/registration?{query}", RegistrationResource.class);
		return router;
	}

	
}
