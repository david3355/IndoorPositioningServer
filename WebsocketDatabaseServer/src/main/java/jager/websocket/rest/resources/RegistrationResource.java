package jager.websocket.rest.resources;

import org.restlet.resource.Post;

import jager.indoornav.wscconvention.Communicator;
import jager.indoornav.wscconvention.model.Information;
import jager.indoornav.wscconvention.model.Message;
import jager.indoornav.wscconvention.model.Tasks;
import jager.indoornav.wscconvention.model.UserRegistration;
import jager.indoornav.wscconvention.rest.RestApiParameters;

public class RegistrationResource extends CommonServerResource
{
	@Post
	public String represent() throws Exception
	{
		String username = getQueryValue(RestApiParameters.KEY_USERNAME);
		String pwd = getQueryValue(RestApiParameters.KEY_PASSWORD);
		String email = getQueryValue(RestApiParameters.KEY_EMAIL);
		String fname = getQueryValue(RestApiParameters.KEY_FNAME);
		String lname = getQueryValue(RestApiParameters.KEY_LNAME);
		System.out.println("REST registration call");
		UserRegistration data = new UserRegistration(username, pwd, email, fname, lname);
		Information info = serverController.processRegistration(data);
		Message response = new Message(Tasks.TASK_INFO, info);
		return Communicator.getInstance().getJSON(response);
	}
}
