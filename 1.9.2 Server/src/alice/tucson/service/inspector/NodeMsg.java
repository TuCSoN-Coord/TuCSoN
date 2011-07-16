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
package alice.tucson.service.inspector;

import alice.tucson.api.*;

import java.io.*;

/**
 * base class for message requesting a service from a user or inspector to a
 * tuple centre node
 */
public class NodeMsg implements Serializable
{
	public TucsonAgentId aid;
	public String action;

	protected NodeMsg()
	{
	}

	public NodeMsg(TucsonAgentId aid)
	{
		this.aid = aid;
	}

	public NodeMsg(TucsonAgentId aid, String act)
	{
		this.aid = aid;
		action = act;
	}
}
