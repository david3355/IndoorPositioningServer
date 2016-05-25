package jager.websocket.dbserver;

import java.util.ArrayList;
import java.util.function.Predicate;

/*
 * Doesn't work with synchronized collections
 * */
public class ClientList<Client> extends ArrayList<Client>
{
	private static final long serialVersionUID = 1L;
	
	public int indexOfItemIf(Predicate<? super Client> filter)
	{
		for (int i = 0; i < this.size(); i++)
		{
			if(filter.test(this.get(i))) return i;
		}
		return -1;
	}
}
