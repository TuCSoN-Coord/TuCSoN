/*
 * Copyright (C) 2001-2002  aliCE team at deis.unibo.it
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
package alice.tucson.api.util;

import alice.tuprolog.*;
import alice.logictuple.LogicTuple;

import java.io.*;
import java.util.*;


/**
 * This class provides static services to serialise logic tuples
 * to and from DataStream, without using Java Object Serialisation
 *
 */
public class TupleSerialiser {

    private static final byte INT_TYPE      = 0x01;
    private static final byte FLOAT_TYPE    = 0x02;
    private static final byte LONG_TYPE     = 0x03;
    private static final byte DOUBLE_TYPE   = 0x04;
    private static final byte STRUCT_TYPE   = 0x05;
    private static final byte VAR_TYPE      = 0x06;
    private static final byte NULL_TYPE      = 0x07;


    /**
     *  Writes a logic tuple on a data output stream
     *
     * @param dout is the target data output stream
     * @param t is the logic tuple written on the stream
     * @throws s java.io.IOException
     */
    public static void writeTuple(DataOutputStream dout, LogicTuple t) throws IOException{
        writeTerm(dout,t.toTerm());
    }

    /**
     *  Reads a logic tuple from a data input stream
     *
     * @param din is the source data input stream
     * @return the logict tuple read
     * @throws alice.logictuple.InvalidLogicTupleException if no valid logic tuples are available
     */
    public static LogicTuple readTuple(DataInputStream din) throws alice.logictuple.InvalidLogicTupleException {
        try {
            return new LogicTuple(readTerm(din));
        } catch (Exception ex){
            throw new alice.logictuple.InvalidLogicTupleException();
        }
    }

    /**
     *  Writes a term on a data output stream
     *
     * @param dout is the target data output stream
     * @param t is the term written on the stream
     * @throws java.io.IOException
     */
    public static void writeTerm(DataOutputStream dout, Term t) throws IOException{
        LinkedList vars = new LinkedList();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout_app = new DataOutputStream(bout);
        writeTerm(dout_app,t,vars);
        // first the var list is written
        dout.writeInt(vars.size());
        Iterator it = vars.listIterator();
        while (it.hasNext()){
            Var v = (Var)it.next();
            dout.writeUTF(v.getName());
        }
        dout.write(bout.toByteArray());
    }

    /**
     *  Reads a prolog term from a data input stream
     *
     * @param din is the source data input stream
     * @return the prolog read
     */
    public static Term readTerm(DataInputStream din) throws InvalidTermException {
        try {
            int nvars = din.readInt();
            Var[] vars = new Var[nvars];
            for (int i=0; i<nvars; i++){
                String name = din.readUTF();
                vars[i]=new Var(name);
            }
            byte type=din.readByte();
            return readTerm(din,type,vars);
        } catch (Exception ex){
            throw new InvalidTermException();
        }
    }

    private static Term readTerm(DataInputStream din, byte type, Var[] vars) throws IOException {
        Term t=null;
        switch (type){
            case INT_TYPE:
                t = new alice.tuprolog.Int(din.readInt());
                break;
            case LONG_TYPE:
                t = new alice.tuprolog.Long(din.readLong());
                break;
            case FLOAT_TYPE:
                t = new alice.tuprolog.Float(din.readFloat());
                break;
            case DOUBLE_TYPE:
                t = new alice.tuprolog.Double(din.readDouble());
                break;
            case VAR_TYPE:
                int pos = din.readInt();
                if (pos==-1){
                    t = new Var();
                } else {
                    t = vars[pos];
                }
                break;
            case STRUCT_TYPE:
                String name = din.readUTF();
                int nargs = din.readInt();
                Term args[] = new Term[nargs];
                for (int i=0; i<nargs; i++){
                    byte ttype= din.readByte();
                    args[i]=readTerm(din,ttype,vars);
                }
                t = new alice.tuprolog.Struct(name,args);
                break;

            case NULL_TYPE:
                t = new alice.tuprolog.NullTerm();
                break;
        }
        return t;
    }

    private static void writeTerm(DataOutputStream dout, Term x, LinkedList vars)  throws IOException {
        if (x.isNumber()){
            alice.tuprolog.Number xn = (alice.tuprolog.Number)x;
            if (xn.isTypeInt()){
                dout.writeByte(INT_TYPE);
                dout.writeInt(xn.intValue());
            } else if (xn.isTypeLong()){
                dout.writeByte(LONG_TYPE);
                dout.writeLong(xn.longValue());
            } else if (xn.isTypeFloat()){
                dout.writeByte(FLOAT_TYPE);
                dout.writeFloat(xn.floatValue());
            } else {
                dout.writeByte(DOUBLE_TYPE);
                dout.writeDouble(xn.doubleValue());
            }
        } else if (x.isVar()){
            Var xv=(Var)x;
            dout.writeByte(VAR_TYPE);
            //
            if (!xv.isAnonymous()){
                int pos = findInList(xv,vars);
                if (pos==-1){
                    vars.addLast(xv);
                    dout.writeInt(vars.size()-1);
                } else {
                    dout.writeInt(pos);
                }
            } else {
                // anonymous variables --> -1 as pos
                dout.writeInt(-1);
            }
        } else if (x.isStruct()){
            Struct xs = (Struct)x;
            dout.writeByte(STRUCT_TYPE);
            // name
            dout.writeUTF(xs.getName());
            // args
            int n = xs.getArity();
            dout.writeInt(n);
            for (int i=0; i<n; i++){
                Term t = xs.getTerm(i);
                writeTerm(dout,t,vars);
            }
        } else {
            dout.writeByte(NULL_TYPE);
        }
    }


    private static int findInList(Var v, LinkedList l){
        Iterator it=l.listIterator();
        int pos = 0;
        while (it.hasNext()){
            Var e = (Var)it.next();
            if (e.getName().equals(v.getName())){
                return pos;
            } else {
                pos++;
            }
        }
        return -1;
    }

    /*
    public static void main(String[] args) throws Exception{

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);

        Term t=Term.parse("p(X,13,ciccio(X),_)");
        TupleSerialiser.writeTerm(dout,t);

        new FileOutputStream(new File("tptest1")).write(bout.toByteArray());

        DataInputStream din = new DataInputStream(new FileInputStream("tptest1"));
        Term t2 = TupleSerialiser.readTerm(din);
        System.out.println(t2);
        //new ObjectOutputStream(new FileOutputStream(new File("tptest2"))).writeObject(t);
    }*/
}