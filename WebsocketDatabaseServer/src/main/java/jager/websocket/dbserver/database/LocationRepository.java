package jager.websocket.dbserver.database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import jager.indoornav.wscconvention.model.NewLocation;
import jager.websocket.dbserver.model.Entity;
import jager.websocket.dbserver.model.Location;
import jager.websocket.dbserver.model.User;

public class LocationRepository extends MySqlDatabaseAccess implements Repository<Location, Long>
{
	private LocationRepository()
	{

	}

	private static LocationRepository self;

	public static LocationRepository getInstance()
	{
		if (self == null)
			self = new LocationRepository();
		return self;
	}

	private final static String TABLE = "location";

	private final static String COL_ID = "id";
	private final static String COL_LATITUDE = "latitude";
	private final static String COL_LONGITUDE = "longitude";
	private final static String COL_TIMESTAMP = "time";
	private final static String COL_USERID = "userid";

	public void save(Location loc)
	{
		String q = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (%s, %s, %s, %s)", TABLE, COL_LATITUDE,
				COL_LONGITUDE, COL_TIMESTAMP, COL_USERID, loc.getLatitude(), loc.getLongitude(),
				loc.getTime().getMillis(), loc.getUserid());
		execute(q);
		// TODO: Itt a lekérdezés után tudni kéne a beszúrt elem id-ját és be
		// kéne állítani az objektumnak!!!
	}

	public List<Entity> findAll()
	{
		String q = String.format("SELECT * FROM %s", TABLE);
		List<Entity> resultdata = executeQuery(q);
		return resultdata;
	}

	public void delete(Long id)
	{
		String q = String.format("DELETE FROM %s WHERE %=%", TABLE, COL_ID, id);
		execute(q);
	}
	
	public Location findEntity(Long id)
	{
		String q = String.format("SELECT * FROM %s WHERE %s=%s", TABLE, COL_ID, id);
		List<Entity> resultdata = executeQuery(q);
		if(resultdata.size()>0) return (Location)resultdata.get(0);
		return null;
	}

	public void addNewLocation(Location location)
	{
		save(location);
	}

	public List<Entity> findUserLocations(String username, Long fromTimestamp, Long toTimestamp)
	{
		String timefilter = "";
		if (fromTimestamp != null && toTimestamp != null)
		{
			timefilter = String.format("AND %s>=%s AND %s<=%s", COL_TIMESTAMP, fromTimestamp, COL_TIMESTAMP, toTimestamp);
		} else if (fromTimestamp != null)
		{
			timefilter = String.format("AND %s>=%s", COL_TIMESTAMP, fromTimestamp);
		} else if (toTimestamp != null)
		{
			timefilter = String.format("AND %s<=%s", COL_TIMESTAMP, toTimestamp);
		}
		long userid = UserRepository.getInstance().getUser(username).getId();
		String q = String.format("SELECT * FROM %s WHERE userid=%s %s", TABLE, userid, timefilter);
		List<Entity> resultdata = executeQuery(q);
		return resultdata;
	}

	@Override
	protected List<Entity> transformResultSet(ResultSet resultdata)
	{
		Location loc;
		List<Entity> locations = new ArrayList<Entity>();
		long id, timestamp, userid;
		double latitude, longitude;

		try
		{
			if (!resultdata.first())
				return locations;
			do
			{

				id = Long.parseLong(resultdata.getString(COL_ID));
				timestamp = Long.parseLong(resultdata.getString(COL_TIMESTAMP));
				userid = Long.parseLong(resultdata.getString(COL_USERID));
				latitude = resultdata.getDouble(COL_LATITUDE);
				longitude = resultdata.getDouble(COL_LONGITUDE);
				loc = new Location(id, latitude, longitude, new DateTime(timestamp), userid);
				locations.add(loc);
			} while (resultdata.next());
		} catch (Exception e)
		{
		}
		return locations;
	}

	

}
