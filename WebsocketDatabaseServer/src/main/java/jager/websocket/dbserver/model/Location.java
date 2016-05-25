package jager.websocket.dbserver.model;

import org.joda.time.DateTime;

public class Location extends Entity
{
	
	public Location(long id, double latitude, double longitude, DateTime time, long userid)
	{
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.time = time;
		this.userid = userid;
	}
	
	public Location()
	{
		
	}

	private long id;
	private double latitude;
	private double longitude;
	private DateTime time;
	private long userid;
	
	public long getId()
	{
		return id;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	public DateTime getTime()
	{
		return time;
	}

	public void setTime(DateTime time)
	{
		this.time = time;
	}

	public long getUserid()
	{
		return userid;
	}

	public void setUserid(long userid)
	{
		this.userid = userid;
	}
}
