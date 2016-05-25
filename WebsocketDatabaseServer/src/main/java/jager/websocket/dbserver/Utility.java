package jager.websocket.dbserver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utility
{
	public static String getPasswordHash(String password)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] encoded = md.digest(password.getBytes());
			StringBuffer stb = new StringBuffer();
			for (byte b : encoded)
			{
				stb.append(Integer.toHexString(b & 0xff)).toString();
			}
			return stb.toString();
		} catch (NoSuchAlgorithmException e)
		{
			return null;
		}
	}
}
