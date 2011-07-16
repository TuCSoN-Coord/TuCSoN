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

import alice.tuprolog.Term;
import alice.tuprolog.Struct;
import alice.tuprolog.Number;
import java.util.*;

/**
 * Base class for tuple argument classes.
 * 
 * @see LogicTuple
 * @see Value
 * @see Var
 *
 * @author aricci
 * @version 1.0
 */
 public class TupleArgument implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** the internal representation of the argument is a (tu)Prolog term*/
    protected Term value;

    public TupleArgument(){
    }

    /**
     * Contructs a tuple argument copying a tuProlog term
     *
     * @param t the Prolog term whose content is used
     *          to build the argument
     */
    public TupleArgument(Term t){
        value=t;
    }

    /** Tests if  the argument is a variable */
    public boolean isVar() {
        return value.isVar();
    }

    /** Tests if  the argument is a value */
    public boolean isValue(){
        return !value.isVar();
    }

    /** Tests if  the argument is an atom */
    public boolean isAtom() {
        return value.isAtom() && value.isStruct();
    }

    /** Tests if  the argument is a number */
    public boolean isNumber(){
        return value.isNumber();
    }

    /** Tests if  the argument is an integer number */
    public boolean isInteger(){
        return value.isNumber() && ((Number)value).isInteger();
    }

    /** Tests if  the argument is a real number */
    public boolean isReal(){
        return value.isNumber() && ((Number)value).isReal();
    }


    /** Tests if  the argument is an integer */
    public boolean isInt(){
        return value.isNumber() && ((Number)value).isTypeInt();
    }

    /** Tests if  the argument is a float */
    public boolean isFloat(){
        return value.isNumber() && ((Number)value).isTypeFloat();
    }

    /** Tests if  the argument is an long */
    public boolean isLong(){
        return value.isNumber() && ((Number)value).isTypeLong();
    }

    /** Tests if  the argument is a double */
    public boolean isDouble(){
        return value.isNumber() && ((Number)value).isTypeDouble();
    }

    /** Tests if  the argument is a structured argument */
    public boolean isStruct(){
        return value.isCompound();
    }

    /** Tests if  the argument is a logic list */
    public boolean isList(){
        return value.isList();
    }
    
    /** Tests if  the argument is an atomic argument */
    public boolean isAtomic(){
        return value.isAtomic();
    }

    /** Gets the prolog term representation of the argument*/
    public alice.tuprolog.Term toTerm(){
        return value;
    }

    /** Gets the string representation of the argument */
    public String toString(){
        return value.getTerm().toString();
    }

    /**
     * Gets the integer value of this argument
     *
     * @return the integer value
     * @throws  InvalidTupleOperationException if the argument is not a number
     */
    public int intValue() throws InvalidTupleOperationException {
        try {
            return ((Number)value).intValue();
        } catch (Exception ex){
            throw new InvalidTupleOperationException();
        }
    }

    /**
     * Gets the long value of this argument
     *
     * @return the long value
     * @throws  InvalidTupleOperationException if the argument is not a number
     */
    public long longValue() throws InvalidTupleOperationException {
        try {
            return ((Number)value).longValue();
        } catch (Exception ex){
            throw new InvalidTupleOperationException();
        }
    }

    /**
     * Gets the float value of this argument
     *
     * @return the float value
     * @throws InvalidTupleOperationException if the argument is not a number
     */
    public float floatValue() throws InvalidTupleOperationException {
        try {
            return ((Number)value).floatValue();
        } catch (Exception ex){
            throw new InvalidTupleOperationException();
        }
    }

    /**
     * Gets the double value of this argument
     *
     * @return the double value
     * @throws s InvalidTupleOperationException if the argument is not a number
     */
    public double doubleValue() throws InvalidTupleOperationException {
        try {
            return ((Number)value).doubleValue();
        } catch (Exception ex){
            throw new InvalidTupleOperationException();
        }
    }


    /**
     * Converts this argument (which is supposed to be a Prolog list) 
     * into a list of values
     *
     * @return the list (actually a LinkedList)
     * @throws s InvalidTupleOperationException if the argument is not a list
     */
    public List toList() throws InvalidTupleOperationException {
        try {
            LinkedList list = new LinkedList();
            Iterator it = ((Struct)value).listIterator();
            while (it.hasNext()){
                list.add(it.next());
            }
            return list;
        } catch (Exception ex){
            throw new InvalidTupleOperationException();
        }
    }

    /**
     * Converts this argument (which is supposed to be a Prolog list) 
     * into an array of values
     *
     * @return an array of Tuple Arguments 
     * @throws s InvalidTupleOperationException if the argument is not a list
     */
    public TupleArgument[] toArray() throws InvalidTupleOperationException {
        try {
            ArrayList list = new ArrayList();
            Iterator it = ((Struct)value).listIterator();
            while (it.hasNext()){
                list.add(it.next());
            }
            TupleArgument[] vect = new TupleArgument[list.size()];
            for (int i=0; i<vect.length; i++){
                vect[i]=new TupleArgument((Term)list.get(i));
            }
            return vect;
        } catch (Exception ex){
            throw new InvalidTupleOperationException();
        }
    }

    /**
     * Gets the name of this argument, supposed to be a structure
     * (including atoms) or a variable
     *
     * @return the name value
     * @throws InvalidTupleOperationException if the argument is not a structure or a variable
     */
    public String getName() throws InvalidTupleOperationException {
        if (value.isStruct()){
            return ((Struct)value).getName();
        } else if (value.isVar()){
            return ((alice.tuprolog.Var)value).getName();
        } else {
            throw new InvalidTupleOperationException();
        }
    }

    /**
     * Gets an argument of this argument supposed to be a compound
     *
     * @param index the index of the argument
     * @return the argument of the compound
     * @throws  InvalidTupleOperationException if this argument is not a compound or an
     *        out of bounds index error is issued
     */
    public TupleArgument getArg(int index) throws InvalidTupleOperationException {
        try {
            return new TupleArgument(((Struct)value).getTerm(index));
        } catch (Exception ex){
            throw new InvalidTupleOperationException();
        }
    }

    /**
     * Gets an argument of this argument supposed to be a compound
     *
     * @param name of the argument
     * @return the argument of the compound
     */
    public TupleArgument getArg(String name)  {
        Struct s = ((Struct)value).getArg(name);
    	    if (s!=null){
    	    		return new TupleArgument(s);
        } else {
        		return null;
        }
    }
        
    /**
     * Gets the argument linked to a variable inside the tuple argument
     *
     * @param varName is the name of the variable
     * @return the value linked to the variable, in the case tha
     *         the variable exits inside the argument, null otherwise
     */
    public TupleArgument getVarValue(String varName) {
        if (value.isVar()){
            return new TupleArgument(((alice.tuprolog.Var)value).getTerm());
        } else if (!value.isStruct()){
            return null;
        } else {
            Term t=getVarValue(varName,(Struct)value);
            if (t!=null){
                return new TupleArgument(t);
            } else {
                return null;
            }
        }
    }

    private Term getVarValue(String name,Struct st){
        int len = st.getArity();
        for (int i=0; i<len; i++){
        	   Term arg = st.getArg(i);
            if (arg.isVar()){
                alice.tuprolog.Var v=(alice.tuprolog.Var)arg;
                if (v.getName().equals(name)){
                    return v.getTerm();
                }
            } else if (arg.isStruct()){
            		Term t = getVarValue(name,((Struct)arg));
            		if (t!=null){
            			return t;
            		}
            }
        }
        return null;
    }

    /**
     * Gets the number of arguments of this argument supposed to be a structure
     *
     * @return the number of arguments
     * @throws  InvalidTupleOperationException if this argument is not a structured or an
     *        out of bounds index error is issued
     */
    public int getArity() throws InvalidTupleOperationException {
        if (value.isStruct()){
            return ((Struct)value).getArity();
        } else {
            throw new InvalidTupleOperationException();
        }
    }

	/**
	 * Specifies if this tuple argument matches with a
	 * specified tuple argument
	 * 
	 * @param t a tuple argument
	 * @return <code>true</code> if there is matching, <code>false</code> otherwise
	 */
	public boolean match(TupleArgument t){
		try {
			boolean result=value.match(t.value);
			return result;
		} catch (Exception ex){
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * Tries to unify this tuple argument with another one
	 * 
	 * @param t a tuple argument
	 * @return <code>true</code> if the propagation was successfull,
	 * <code>false</code> otherwise
	 */
	public boolean propagate(TupleArgument t){
		try {
			return value.unify(t.value);  //NEW FOR TUPROLOG 2.?? con null,
		} catch (Exception ex){
			return false;
		}
	}

    /**
     * Gets an iterator on the elements of this
     * structure supposed to be a list.
     *
     *  @return null if the structure is not a list
     */
    public java.util.Iterator listIterator(){
        if (value.isList()){
            return ((Struct)value).listIterator();
        } else {
            return null;
        }
    }

    
    /**
     * Static service to get a Tuple Argument from a textual representation
     *
     * @param st the text representing the tuple argument
     * @return the tuple argument interpreted from the text
     * @exception InvalidTupleArgumentException if the text does not represent a valid tuple argument
     */
    public static TupleArgument parse(String st) throws InvalidTupleArgumentException {
        try {
            Term t=alice.tuprolog.Term.parse(st);
            return new TupleArgument(t);
        } catch (Exception ex){
            throw new InvalidTupleArgumentException();
        }
    }
}