/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it
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
package alice.respect.core;
import java.util.AbstractMap;
import java.util.LinkedHashMap;

import alice.tuprolog.*;

/**
 * Defines the reaction type managed by ReSpect Reactor
 * (logic reactions).
 * 
 * @author aricci
 */
public class LogicReaction implements alice.tuplecentre.core.Reaction, java.io.Serializable{

    private Struct reaction;

    public LogicReaction(){
    }

    public LogicReaction(Struct t){
    	/*Before*/
//        reaction=(Struct)t.getRenamedCopy();
        /*insert by Enrico Rossignoli*/
    	AbstractMap<Var,Var> v = new LinkedHashMap<Var,Var>();
    	reaction=(Struct)t.copyGoal(v, 0);
    	/*end insert*/
    }

    public String toString(){
        return reaction.toString();
    }

    public Struct getStructReaction(){
        return reaction;
    }
    
    public Term getReactionTerm(){
    	return ((Struct)reaction.getArg(0));
    }
      
}

