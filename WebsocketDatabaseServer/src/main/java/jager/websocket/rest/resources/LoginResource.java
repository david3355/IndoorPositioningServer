package jager.websocket.rest.resources;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import jager.indoornav.wscconvention.Communicator;
import jager.indoornav.wscconvention.model.Information;
import jager.indoornav.wscconvention.model.InformationCodes;
import jager.indoornav.wscconvention.model.Message;
import jager.indoornav.wscconvention.model.Tasks;
import jager.indoornav.wscconvention.model.UserLogin;
import jager.indoornav.wscconvention.rest.RestApiParameters;
import jager.websocket.dbserver.Client;

public class LoginResource extends CommonServerResource
{
	@Post
	public String represent() throws Exception
	{
		String username = getQueryValue(RestApiParameters.KEY_USERNAME);
		String pwd = getQueryValue(RestApiParameters.KEY_PASSWORD);
		String sessionID = getQueryValue(RestApiParameters.KEY_CLIENTID);
		Client client = serverController.getClientByID(sessionID);
		System.out.println("REST login call from sessionid: " + sessionID);
		Information info = null;
		if (client != null)
		{
			System.out.println("Client found: " + client.getSession().getId());
			UserLogin data = new UserLogin(username, pwd);
			info = serverController.processLogin(data, client);
		}
		else info = new Information(InformationCodes.CONNECTION_FAILED);
		Message response = new Message(Tasks.TASK_INFO, info);
		return Communicator.getInstance().getJSON(response);
	}

}
