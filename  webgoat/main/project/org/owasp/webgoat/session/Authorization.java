package org.owasp.webgoat.session;

import java.util.Hashtable;
import java.util.Map;

public class Authorization
{
	Map permissions = new Hashtable();
	
	public Authorization()
	{
	}
	
	public void setPermission(int userId, int functionId)
	{
		permissions.put(new Integer(userId), new Integer(functionId));
	}
	
	public boolean isAllowed(int userId, int functionId)
	{
		return (permissions.get(new Integer(userId)) != null);
	}
}

