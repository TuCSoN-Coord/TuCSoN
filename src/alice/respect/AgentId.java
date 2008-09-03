/*
 * ReSpecT Framework - aliCE team at deis.unibo.it
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
package alice.respect;
import alice.tuprolog.*;
/**
 * agent identifier for logic tuple centres, described in term
 * of string name.  As logic tuple, it is represented by the
 * name itself.
 */
public class AgentId implements alice.tuplecentre.AgentId, java.io.Serializable {
    
	private static final String NO_NAME = "anonymous"; 
    private String id;

    public AgentId(){
    		id = NO_NAME;
    }

    /**
     * Constructs an agent identifier
     *
     * The Agent identifier must be a ground valid logic term
     *
     * @param sid is the string representation of the identifier
     */
    public AgentId(String sid) {
        id=sid;
    }

    public String toString(){
        return id;
    }
    
    public boolean equals(Object obj){
    	return id.equals(obj.toString());
    }
}

