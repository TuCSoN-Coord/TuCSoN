/*
 * Logic Tuple Communication Language - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.logictuple;

import alice.logictuple.exceptions.InvalidVarNameException;

/**
 * Class representing tuple argument variables.
 * 
 * @see TupleArgument
 * @see Value
 * 
 * @author Alessandro Ricci
 */
public class Var extends TupleArgument {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Constructs an anonymous variable tuple argument */
    public Var() {
        super();
        this.value = new alice.tuprolog.Var();
    }

    /**
     * Construct a variable tuple argument identified with a name
     * 
     * @param n
     *            the name of the variable, which must start with an upper case
     *            letter or the underscore
     * @throws InvalidVarNameException 
     * 				if the text does not represent a valid Var name
     */
    public Var(final String n) throws InvalidVarNameException {
        super();
        try {
            this.value = new alice.tuprolog.Var(n);
        } catch (final alice.tuprolog.InvalidTermException ex) {
            throw new InvalidVarNameException("Invalid Var name: \"" + n + "\"", ex);
        }
    }
}
