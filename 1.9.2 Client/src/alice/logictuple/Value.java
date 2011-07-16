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
 * Class representing a concrete tuple argument value (integer, real, string, structure).
 * 
 * @see TupleArgument
 * @see Var
 *
 * @author aricci
 * @version 1.0
 */
public class Value extends TupleArgument {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Constructs a simple string tuple argument */
    public Value(String f) {
        value=new alice.tuprolog.Struct(f);
    }

    /** Constructs a simple integer tuple argument */
    public Value(int v){
        value=new alice.tuprolog.Int(v);
    }

    /** Constructs a simple long tuple argument */
    public Value(long v){
        value=new alice.tuprolog.Long(v);
    }

    /** Constructs a simple float tuple argument */
    public Value(float v){
        value=new alice.tuprolog.Float(v);
    }

    /** Constructs a simple double tuple argument */
    public Value(double v){
        value=new alice.tuprolog.Double(v);
    }

    /**
     * Constructs a structured (compound) argument, made of a string
     * as a name (functor) and one argument
     *
     * @param f the name of the structure
     * @param at0 the argument of the structure
     */
    public Value(String f,TupleArgument at0) {
        value=new alice.tuprolog.Struct(f,at0.toTerm());
    }

    /**
     * Constructs a structured (compound) argument, made of a string
     * as a name (functor) and two arguments
     *
     * @param f the name of the structure
     * @param at0 the first argument of the structure
     * @param at1 the second argument of the structure
     */
    public Value(String f,TupleArgument at0,TupleArgument at1) {
        value=new alice.tuprolog.Struct(f,at0.toTerm(),at1.toTerm());
    }

    /**
     * Constructs a structured (compound) argument, made of a string
     * as a name (functor) and three arguments
     *
     * @param f the name of the structure
     * @param at0 the first argument of the structure
     * @param at1 the second argument of the structure
     * @param at2 the third argument of the structure
     */
    public Value(String f,TupleArgument at0,TupleArgument at1,TupleArgument at2) {
        value=new alice.tuprolog.Struct(f,at0.toTerm(),at1.toTerm(),at2.toTerm());
    }

    /**
     * Constructs a structured (compound) argument, made of a string
     * as a name (functor) and four arguments
     *
     * @param f the name of the structure
     * @param at0 the first argument of the structure
     * @param at1 the second argument of the structure
     * @param at2 the third argument of the structure
     * @param at3 the fourth argument of the structure
     */
    public Value(String f,TupleArgument at0,TupleArgument at1,TupleArgument at2,TupleArgument at3) {
        value=new alice.tuprolog.Struct(f,at0.toTerm(),at1.toTerm(),at2.toTerm(),at3.toTerm());
    }

    /**
     * Constructs a structured (compound) argument, made of a string
     * as a name (functor) and five arguments
     *
     * @param f the name of the structure
     * @param at0 the first argument of the structure
     * @param at1 the second argument of the structure
     * @param at2 the third argument of the structure
     * @param at3 the fourth argument of the structure
     * @param at4 the fifth argument of the structure
     */
    public Value(String f,TupleArgument at0,TupleArgument at1,TupleArgument at2,TupleArgument at3,TupleArgument at4) {
        value=new alice.tuprolog.Struct(f,at0.toTerm(),at1.toTerm(),at2.toTerm(),at3.toTerm(),at4.toTerm());
    }

    /**
     * Constructs a structured (compound) argument, made of a string
     * as a name (functor) and six arguments
     *
     * @param f the name of the structure
     * @param at0 the first argument of the structure
     * @param at1 the second argument of the structure
     * @param at2 the third argument of the structure
     * @param at3 the fourth argument of the structure
     * @param at4 the fifth argument of the structure
     * @param at5 the sixth argument of the structure
     */
    public Value(String f,TupleArgument at0,TupleArgument at1,TupleArgument at2,TupleArgument at3,TupleArgument at4,TupleArgument at5) {
        value=new alice.tuprolog.Struct(f,at0.toTerm(),at1.toTerm(),at2.toTerm(),at3.toTerm(),at4.toTerm(),at5.toTerm());
    }

    /**
     * Constructs a structured (compound) argument, made of a string
     * as a name (functor) and seven arguments
     *
     * @param f the name of the structure
     * @param at0 the first argument of the structure
     * @param at1 the second argument of the structure
     * @param at2 the third argument of the structure
     * @param at3 the fourth argument of the structure
     * @param at4 the fifth argument of the structure
     * @param at5 the sixth argument of the structure
     * @param at6 the seventh argument of the structure
     */
    public Value(String f,TupleArgument at0,TupleArgument at1,TupleArgument at2,TupleArgument at3,TupleArgument at4,TupleArgument at5,TupleArgument at6) {
        value=new alice.tuprolog.Struct(f,at0.toTerm(),at1.toTerm(),at2.toTerm(),at3.toTerm(),at4.toTerm(),at5.toTerm(),at6.toTerm());
    }

    /**
     * Constructs a structured (compound) argument, made of a string
     * as a name (functor) and list of arguments
     *
     * @param f the name of the structure
     * @param argList the list of the arguments
     */
    public Value(String f,TupleArgument[] argList) {
        alice.tuprolog.Term[] list=new alice.tuprolog.Term[argList.length];
        for (int i=0; i<list.length; i++){
            list[i]=argList[i].toTerm();
        }
        value=new alice.tuprolog.Struct(f,list);
    }

    /**
     * Constructs a structured (compound) argument as a logic list
     *
     * @param f the name of the structure
     * @param argList the list of the arguments
     */
    public Value(TupleArgument[] argList) {
        alice.tuprolog.Term[] list=new alice.tuprolog.Term[argList.length];
        for (int i=0; i<list.length; i++){
            list[i]=argList[i].toTerm();
        }
        value=new alice.tuprolog.Struct(list);
    }

    protected Value() {
        value=null;
    }

}
