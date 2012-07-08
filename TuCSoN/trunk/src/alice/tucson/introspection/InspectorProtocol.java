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

import java.io.Serializable;

import alice.logictuple.LogicTuple;

public class InspectorProtocol implements Serializable{
	
	private static final long serialVersionUID = -6842026459178793395L;

	/** don't observe */
	public static final byte NO_OBSERVATION = 1;
	
	/** observe only when asked by inspector */
	public static final byte REACTIVE_OBSERVATION = 2;
	
	/** observe continuosly */
	public static final byte PROACTIVE_OBSERVATION = 3;

	/** defining T set observation */
	public byte tsetObservType = NO_OBSERVATION;

	/** filter for tuple observed */
	public LogicTuple tsetFilter = null;
	
	/** filter for query observed */
	public LogicTuple wsetFilter = null;

	/** defining W set observation */
	public byte pendingQueryObservType = NO_OBSERVATION;

	/** defining T set observation */
	public byte reactionsObservType = NO_OBSERVATION;

	/** desired tracing state for the vm */
	public boolean tracing = false;
	
}
