/*
 * Logic Tuple Communication Language - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
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
package alice.logictuple;

/**
 * Defines Variable argument of tuples, identified by a name
 *
 * @see TupleArgument
 * @see Value
 *
 *
 * @version 1.0
 */
public class Var extends TupleArgument {

    /** the 'any' (anonymous, which is _) variable constant */
    static public final Var ANY = new Var();

    /**
     * Construct a variable tuple argument identified with a name
     *
     * @param n the name of the variable, which must start with an upper case letter
     *          or the underscore
     * @exception InvalidVarNameException if it is not a valid name
     */
    public Var(String n) throws InvalidVarNameException {
        try {
            value=new alice.tuprolog.Var(n);
        } catch (alice.tuprolog.InvalidVarNameException ex){
            throw new InvalidVarNameException();
        }
    }

    /** Constructs an anonymous variable tuple argument */
    public Var(){
        value=new alice.tuprolog.Var();
    }
}