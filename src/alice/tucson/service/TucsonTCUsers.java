/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.tucson.service;

import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;
import java.util.*;

/**
 * 
 * Tucson Tuple Centre Users Information
 * 
 */
public class TucsonTCUsers
{
	private TucsonTupleCentreId tid;
	private Date creationDate;
	private LinkedList<TucsonAgentId> currentAidUsers;

	public TucsonTCUsers(TucsonTupleCentreId tid)
	{
		this.tid = tid;
		creationDate = new Date();
		currentAidUsers = new LinkedList<TucsonAgentId>();
	}

	public TucsonTupleCentreId getTucsonTupleCentreId()
	{
		return tid;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public void addUser(TucsonAgentId aid)
	{
		synchronized (currentAidUsers)
		{
			if (!currentAidUsers.contains(aid))
			{
				currentAidUsers.add(aid);
			}
		}
	}

	public LinkedList<TucsonAgentId> getUsers()
	{
		synchronized (currentAidUsers)
		{
			return currentAidUsers;
		}
	}

	public void removeUser(TucsonAgentId aid)
	{
		synchronized (currentAidUsers)
		{
			if (currentAidUsers.contains(aid) == true)
			{
				currentAidUsers.remove(aid);
			}
		}
	}
}
