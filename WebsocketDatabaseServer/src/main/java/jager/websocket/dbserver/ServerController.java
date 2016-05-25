package jager.websocket.dbserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.websocket.Session;

import org.joda.time.DateTime;

import jager.indoornav.wscconvention.Communicator;
import jager.indoornav.wscconvention.model.ClientIdentifier;
import jager.indoornav.wscconvention.model.GetLocations;
import jager.indoornav.wscconvention.model.Information;
import jager.indoornav.wscconvention.model.InformationCodes;
import jager.indoornav.wscconvention.model.Message;
import jager.indoornav.wscconvention.model.NewLocation;
import jager.indoornav.wscconvention.model.SendLocationList;
import jager.indoornav.wscconvention.model.Tasks;
import jager.indoornav.wscconvention.model.UserLogin;
import jager.indoornav.wscconvention.model.UserRegistration;
import jager.websocket.dbserver.database.LocationRepository;
import jager.websocket.dbserver.database.UserRepository;
import jager.websocket.dbserver.model.Entity;
import jager.websocket.dbserver.model.Location;
import jager.websocket.dbserver.model.User;

public class ServerController
{
	private ServerController()
	{
	}

	private static ServerController self;

	public static ServerController getInstance()
	{
		if (self == null)
			self = new ServerController();
		return self;
	}

	private List<Client> clients = Collections.synchronizedList(new ArrayList<>());
	private UserRepository userRepo = UserRepository.getInstance();
	private LocationRepository locRepo = LocationRepository.getInstance();
	private Communicator communicator = Communicator.getInstance();

	public void newClientConnected(Session clientSession)
	{
		Client c = new Client(clientSession);
		clients.add(c);
	}

	public void clientDisconnected(Session clientSession)
	{
		int idx = 0;
		synchronized (clients)
		{
			while (idx < clients.size() && !clients.get(idx).getSession().equals(clientSession))
			{
				idx++;
			}
			Client client = clients.get(idx);
			if (client.isLoggedIn())
				client.logOut();
			clients.remove(idx);
		}
	}

	public Client getClientByID(String clientID)
	{
		synchronized (clients)
		{
			for (Client client : clients)
			{
				if (client.clientEquals(clientID))
					return client;
			}
		}
		return null;
	}

	private Client getClientBySession(Session clientSession)
	{
		synchronized (clients)
		{
			for (Client client : clients)
			{
				if (client.getSession().equals(clientSession))
					return client;
			}
		}
		return null;
	}

	public void processMessage(String json, Session clientSession)
	{
		System.out.println("Message:" + json);
		Client client = getClientBySession(clientSession);
		Message msg = communicator.getMessage(json);
		Tasks task = msg.getTaskObject().getTask();
		switch (task)
		{
		case TASK_IDENTIFY_CLIENT:
			identifyClient((ClientIdentifier) msg.getTaskObject(), client);
			break;
		case TASK_NEWLOC:
			if (client.isLoggedIn())
				processNewLocation((NewLocation) msg.getTaskObject(), json, client);
			break;

		default:
			break;
		}
	}

	private void identifyClient(ClientIdentifier identifier, Client client)
	{
		client.setClientID(UUID.fromString(identifier.getClientID()));
	}

	public Information processRegistration(UserRegistration data)
	{
		if (data.getUsername() == null || data.getUsername().equals("") || data.getPassword() == null
				|| data.getPassword().equals("") || data.getEmail() == null
						|| data.getEmail().equals(""))
			return new Information(InformationCodes.REG_FAIL_MISSINGDATA);
		User existing = userRepo.userExists(data.getUsername());
		if (existing == null)
		{
			User user = new User();
			user.setUsername(data.getUsername());
			user.setPassword(Utility.getPasswordHash(data.getPassword()));
			user.setEmail(data.getEmail());
			user.setFirstname(data.getFirst_name());
			user.setLastname(data.getLast_name());
			userRepo.addUser(user);
			return new Information(InformationCodes.REG_OK);
		} else
			return new Information(InformationCodes.REG_FAIL);
	}

	public Information processLogin(UserLogin data, Client client)
	{
		if (data.getUsername() == null || data.getUsername().equals("") || data.getPassword() == null
				|| data.getPassword().equals(""))
			return new Information(InformationCodes.LOGIN_FAIL_INCORRECTDATA);
		if (!client.isLoggedIn())
		{
			User u = userRepo.userExists(data.getUsername());
			if (u != null)
			{
				if (u.getPassword().equals(Utility.getPasswordHash(data.getPassword())))
				{
					client.logIn(u);
					return new Information(InformationCodes.LOGIN_OK);
				} else
					return new Information(InformationCodes.LOGIN_FAIL_PWD);
			} else
				return new Information(InformationCodes.LOGIN_FAIL_USERNAME);
		} else
			return new Information(InformationCodes.LOGIN_FAIL_ALREADYLOGGED);
	}

	public Information processLogout(Client client)
	{
		if (client.isLoggedIn())
		{
			client.logOut();
			return new Information(InformationCodes.LOGOUT_OK);
		} else
		{
			return new Information(InformationCodes.LOGOUT_FAIL);
		}
	}

	private void processNewLocation(NewLocation data, String rawmessage, Client client)
	{
		User user = client.getUser();
		if (user != null)
		{
			Location loc = new Location();
			loc.setLatitude(data.getLatitude());
			loc.setLongitude(data.getLongitude());
			loc.setTime(new DateTime(data.getTimestamp()));
			loc.setUserid(user.getId());
			locRepo.addNewLocation(loc);
			ReplySender.sendToActiveUsers(clients, client.getSession(), rawmessage);
		}
	}

	private void getLocations(GetLocations getloc, Client client)
	{
		List<Entity> locdata = locRepo.findUserLocations(getloc.getUsername(), getloc.getFrom(), getloc.getTo());
		List<NewLocation> locations = new ArrayList<NewLocation>();
		for (Entity entity : locdata)
		{
			Location loc = (Location) entity;
			NewLocation locmsg = new NewLocation(loc.getLatitude(), loc.getLongitude(), loc.getTime().getMillis(),
					getloc.getUsername());
			locations.add(locmsg);
		}
		SendLocationList sll = new SendLocationList(locations);
		Message m = new Message(sll.getTask(), sll);
		String message = communicator.getJSON(m);
		ReplySender.sendMessage(client.getSession(), message);
	}
}
