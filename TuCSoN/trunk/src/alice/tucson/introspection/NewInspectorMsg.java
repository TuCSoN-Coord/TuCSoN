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
package alice.tucson.introspection;


import alice.tucson.api.TucsonAgentId;

/**
 * message sent by an inspector to a daemon in order to become an (inspector)
 * user of a tuple centre
 */
@SuppressWarnings("serial")
public class NewInspectorMsg extends NodeMsg
{
	public String tcName;
	public InspectorProtocol info;

	public NewInspectorMsg(TucsonAgentId aid, String tcName_, InspectorProtocol info_)
	{
		super(aid, "newInspector");
		tcName = tcName_;
		info = info_;
	}
}
