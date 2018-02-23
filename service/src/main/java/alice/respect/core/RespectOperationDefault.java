/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.respect.core;

import java.util.LinkedList;
import java.util.List;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.logictuple.Var;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.logictuple.exceptions.InvalidVarNameException;
import alice.respect.api.RespectOperation;
import alice.respect.api.RespectSpecification;
import alice.tucson.service.TucsonOperationDefault;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.OperationCompletionListener;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;

/**
 * This class represents a ReSpecT operation.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 */
public class RespectOperationDefault extends AbstractTupleCentreOperation implements
        RespectOperation {

    /**
     *
     */
    public static final int OPTYPE_ENV = 103;
    /**
     *
     */
    public static final int OPTYPE_FROM = 104;
    /**
     *
     */
    public static final int OPTYPE_GET_ENV = 101;
    /**
     *
     */
    public static final int OPTYPE_SET_ENV = 102;
    /**
     *
     */
    public static final int OPTYPE_TIME = 100;
    /**
     *
     */
    public static final int OPTYPE_TO = 105;


    /**
     *
     * @param opType
     *            the type of the operation
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener to notify upon operation completion
     * @return the ReSpecT operation built
     * @throws InvalidLogicTupleException
     *             if the given logic tuple is not a valid logic tuple
     */
    public static RespectOperationDefault make(final int opType,
                                               final LogicTuple t, final OperationCompletionListener l)
            throws InvalidLogicTupleException {
        if (opType == TucsonOperationDefault.getCode()) {
            return RespectOperationDefault.makeGet(new LogicTuple("get"), l);
        }
        if (opType == TucsonOperationDefault.getSCode()) {
            try {
				return RespectOperationDefault.makeGetS(new LogicTuple("spec", new Var(
				        "S")), l);
			} catch (InvalidVarNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        if (opType == TucsonOperationDefault.setCode()) {
            return RespectOperationDefault.makeSet(t, l);
        }
        if (opType == TucsonOperationDefault.setSCode()) {
            //try {
                if ("spec".equals(t.getName())) {
                    return RespectOperationDefault.makeSetS(null);
                }
                return RespectOperationDefault.makeSetS(t, l);
           // } catch (final InvalidLogicTupleOperationException e) {
              //  e.printStackTrace();
          //  }
        }
        if (opType == TucsonOperationDefault.inCode()) {
            return RespectOperationDefault.makeIn(t, l);
        }
        if (opType == TucsonOperationDefault.inAllCode()) {
          //  try {
                if (",".equals(t.getName()) && t.getArity() == 2) {
                    return RespectOperationDefault.makeInAll(
                            new LogicTuple(t.getArg(0)), l);
                }
                return RespectOperationDefault.makeInAll(t, l);
        //    } catch (final InvalidLogicTupleOperationException e) {
          //      e.printStackTrace();
          //  }
        }
        if (opType == TucsonOperationDefault.inpCode()) {
            return RespectOperationDefault.makeInp(t, l);
        }
        if (opType == TucsonOperationDefault.inpSCode()) {
            return RespectOperationDefault.makeInpS(t, l);
        }
        if (opType == TucsonOperationDefault.inSCode()) {
            return RespectOperationDefault.makeInS(t, l);
        }
        if (opType == TucsonOperationDefault.outCode()) {
            return RespectOperationDefault.makeOut(t, l);
        }
        if (opType == TucsonOperationDefault.outAllCode()) {
            return RespectOperationDefault.makeOutAll(t, l);
        }
        if (opType == TucsonOperationDefault.outSCode()) {
            return RespectOperationDefault.makeOutS(t, l);
        }
        if (opType == TucsonOperationDefault.rdCode()) {
            return RespectOperationDefault.makeRd(t, l);
        }
        if (opType == TucsonOperationDefault.rdAllCode()) {
          //  try {
                if (",".equals(t.getName()) && t.getArity() == 2) {
                    return RespectOperationDefault.makeRdAll(
                            new LogicTuple(t.getArg(0)), l);
                }
                return RespectOperationDefault.makeRdAll(t, l);
          //  } catch (final InvalidLogicTupleOperationException e) {
             //   e.printStackTrace();
           // }
        }
        if (opType == TucsonOperationDefault.rdpCode()) {
            return RespectOperationDefault.makeRdp(t, l);
        }
        if (opType == TucsonOperationDefault.rdpSCode()) {
            return RespectOperationDefault.makeRdpS(t, l);
        }
        if (opType == TucsonOperationDefault.rdSCode()) {
            return RespectOperationDefault.makeRdS(t, l);
        }
        if (opType == TucsonOperationDefault.noCode()) {
            return RespectOperationDefault.makeNo(t, l);
        }
        if (opType == TucsonOperationDefault.noAllCode()) {
          //  try {
                if (",".equals(t.getName()) && t.getArity() == 2) {
                    return RespectOperationDefault.makeNoAll(
                            new LogicTuple(t.getArg(0)), l);
                }
                return RespectOperationDefault.makeNoAll(t, l);
          //  } catch (final InvalidLogicTupleOperationException e) {
           //     e.printStackTrace();
          //  }
        }
        if (opType == TucsonOperationDefault.nopCode()) {
            return RespectOperationDefault.makeNop(t, l);
        }
        if (opType == TucsonOperationDefault.noSCode()) {
            return RespectOperationDefault.makeNoS(t, l);
        }
        if (opType == TucsonOperationDefault.nopSCode()) {
            return RespectOperationDefault.makeNopS(t, l);
        }
        if (opType == TucsonOperationDefault.uinCode()) {
            return RespectOperationDefault.makeUin(t, l);
        }
        if (opType == TucsonOperationDefault.urdCode()) {
            return RespectOperationDefault.makeUrd(t, l);
        }
        if (opType == TucsonOperationDefault.unoCode()) {
            return RespectOperationDefault.makeUno(t, l);
        }
        if (opType == TucsonOperationDefault.uinpCode()) {
            return RespectOperationDefault.makeUinp(t, l);
        }
        if (opType == TucsonOperationDefault.urdpCode()) {
            return RespectOperationDefault.makeUrdp(t, l);
        }
        if (opType == TucsonOperationDefault.unopCode()) {
            return RespectOperationDefault.makeUnop(t, l);
        }
        if (opType == TucsonOperationDefault.spawnCode()) {
            return RespectOperationDefault.makeSpawn(t, l);
        }
        if (opType == TucsonOperationDefault.getEnvCode()) {
            return RespectOperationDefault.makeGetEnv(t, l);
        }
        if (opType == TucsonOperationDefault.setEnvCode()) {
            return RespectOperationDefault.makeSetEnv(t, l);
        }
        return null;
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeFrom(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        final RespectOperationDefault temp = new RespectOperationDefault(
                RespectOperationDefault.OPTYPE_FROM, t, l);
        temp.setTupleResult(t);
        return temp;
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeGet(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        final RespectOperationDefault temp = new RespectOperationDefault(
                AbstractTupleCentreOperation.OPTYPE_GET, (Tuple) t, l);
        return temp;
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeGetEnv(final LogicTuple t,
                                                     final OperationCompletionListener l) {
        final RespectOperationDefault temp = new RespectOperationDefault(
                RespectOperationDefault.OPTYPE_GET_ENV, t, l);
        temp.setTupleResult(t);
        return temp;
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeGetS(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        final RespectOperationDefault temp = new RespectOperationDefault(
                AbstractTupleCentreOperation.OPTYPE_GET_S, (Tuple) t, l);
        return temp;
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeIn(final LogicTuple t,
                                                 final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_IN, t,
                l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeInAll(final LogicTuple t,
                                                    final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_IN_ALL,
                t, l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeInp(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_INP, t,
                l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeInpS(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_INP_S,
                t, l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeInS(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_IN_S,
                t, l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeNo(final LogicTuple t,
                                                 final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_NO, t,
                l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeNoAll(final LogicTuple t,
                                                    final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_NO_ALL,
                t, l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeNop(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_NOP, t,
                l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeNopS(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_NOP_S,
                t, l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeNoS(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_NO_S,
                t, l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeOut(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_OUT,
                (Tuple) t, l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeOutAll(final LogicTuple t,
                                                     final OperationCompletionListener l) {
        return new RespectOperationDefault(
                AbstractTupleCentreOperation.OPTYPE_OUT_ALL, (Tuple) t, l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeOutS(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_OUT_S,
                (Tuple) t, l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeRd(final LogicTuple t,
                                                 final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_RD, t,
                l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeRdAll(final LogicTuple t,
                                                    final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_RD_ALL,
                t, l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeRdp(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_RDP, t,
                l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeRdpS(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_RDP_S,
                t, l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeRdS(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_RD_S,
                t, l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeSet(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        if ("[]".equals(t.toString())) {
            return new RespectOperationDefault(
                    AbstractTupleCentreOperation.OPTYPE_SET,
                    new LinkedList<Tuple>(), l);
        }
        final List<Tuple> list = new LinkedList<Tuple>();
        LogicTuple cpy = null;
        try {
            cpy = LogicTuple.parse(t.toString());
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
            return null;
        }
        TupleArgument arg;
        arg = cpy.getArg(0);
        while (arg != null) {
            if (!arg.isList()) {
                final LogicTuple t1 = new LogicTuple(arg);
                list.add(t1);
                arg = cpy.getArg(1);
            } else {
                final LogicTuple t2 = new LogicTuple(arg);
                cpy = t2;
                if (!"[]".equals(cpy.toString())) {
                    arg = cpy.getArg(0);
                } else {
                    arg = null;
                }
            }
        }
        final RespectOperationDefault temp = new RespectOperationDefault(
                AbstractTupleCentreOperation.OPTYPE_SET, list, l);
        return temp;
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeSetEnv(final LogicTuple t,
                                                     final OperationCompletionListener l) {
        final RespectOperationDefault temp = new RespectOperationDefault(
                RespectOperationDefault.OPTYPE_SET_ENV, t, l);
        temp.setTupleResult(t);
        return temp;
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeSetS(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        if ("[]".equals(t.toString())) {
            return new RespectOperationDefault(
                    AbstractTupleCentreOperation.OPTYPE_SET_S,
                    new LinkedList<Tuple>(), l);
        }
        final List<Tuple> list = new LinkedList<Tuple>();
        LogicTuple cpy = null;
        try {
            cpy = LogicTuple.parse(t.toString());
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
            return null;
        }
        TupleArgument arg;
        arg = cpy.getArg(0);
        while (arg != null) {
            if (!arg.isList()) {
                final LogicTuple t1 = new LogicTuple(arg);
                list.add(t1);
                arg = cpy.getArg(1);
            } else {
                final LogicTuple t2 = new LogicTuple(arg);
                cpy = t2;
                if (!"[]".equals(cpy.toString())) {
                    arg = cpy.getArg(0);
                } else {
                    arg = null;
                }
            }
        }
        final RespectOperationDefault temp = new RespectOperationDefault(
                AbstractTupleCentreOperation.OPTYPE_SET_S, list, l);
        return temp;
    }

    /**
     *
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeSetS(final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_SET_S,
                new LogicTuple(), l);
    }

    /**
     *
     * @param spec
     *            the ReSpecT specification argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeSetS(final RespectSpecification spec,
                                                   final OperationCompletionListener l) {
        RespectOperationDefault temp = null;
        try {
            temp = new RespectOperationDefault(
                    AbstractTupleCentreOperation.OPTYPE_SET_S,
                    (Tuple) LogicTuple.parse(spec.toString()), l);
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeSpawn(final LogicTuple t,
                                                    final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_SPAWN,
                (Tuple) t, l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeTime(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(RespectOperationDefault.OPTYPE_TIME, t, l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeTo(final LogicTuple t,
                                                 final OperationCompletionListener l) {
        final RespectOperationDefault temp = new RespectOperationDefault(
                RespectOperationDefault.OPTYPE_TO, t, l);
        temp.setTupleResult(t);
        return temp;
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeUin(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_UIN, t,
                l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeUinp(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_UINP,
                t, l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeUno(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_UNO, t,
                l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeUnop(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_UNOP,
                t, l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeUrd(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_URD, t,
                l);
    }

    /**
     *
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeUrdp(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(AbstractTupleCentreOperation.OPTYPE_URDP,
                t, l);
    }

    /**
     * @param type
     *            the integer type-code of the oepration
     * @param tupleList
     *            the list of tuples argument of the operation
     * @param l
     *            the listener for operation completion
     */
    protected RespectOperationDefault(final int type, final List<Tuple> tupleList,
                                      final OperationCompletionListener l) {
        super(type, tupleList, l);
    }

    /**
     *
     * @param type
     *            the integer type-code of the oepration
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     */
    protected RespectOperationDefault(final int type, final Tuple t,
                                      final OperationCompletionListener l) {
        super(type, t, l);
    }

    /**
     *
     * @param type
     *            the integer type-code of the oepration
     * @param t
     *            the tuple template argument of the operation
     * @param l
     *            the listener for operation completion
     */
    protected RespectOperationDefault(final int type, final TupleTemplate t,
                                      final OperationCompletionListener l) {
        super(type, t, l);
    }

    @Override
    public LogicTuple getLogicTupleArgument() {
        // TODO cannot move all to AbstractTupleCentreOperation because condition changes from TucsonOperationDefault
        // check why
        if (this.isOut() || this.isOutS() || this.isOutAll() || this.isSpawn()) {
            return (LogicTuple) this.getTupleArgument();
        }
        return (LogicTuple) this.getTemplateArgument();
    }


    @Override
    public boolean isEnv() {
        return this.getType() == RespectOperationDefault.OPTYPE_ENV;
    }

    @Override
    public boolean isGetEnv() {
        return this.getType() == RespectOperationDefault.OPTYPE_GET_ENV;
    }

    @Override
    public boolean isSetEnv() {
        return this.getType() == RespectOperationDefault.OPTYPE_SET_ENV;
    }

    @Override
    public boolean isTime() {
        return this.getType() == RespectOperationDefault.OPTYPE_TIME;
    }

    @Override
    public String toString() {
        return this.toTuple().toString();
    }

    /**
     *
     * @return the logic tuple representation of this operation
     */
    public LogicTuple toTuple() {
        LogicTuple t = null;
        Term[] tl = null;
        if (this.isOperationCompleted()) {
            t = this.getLogicTupleResult();
        } else {
            t = this.getLogicTupleArgument();
        }
        String opName;
        if (this.isSpawn()) {
            opName = "spawn";
        } else if (this.isOut()) {
            opName = "out";
        } else if (this.isIn()) {
            opName = "in";
        } else if (this.isRd()) {
            opName = "rd";
        } else if (this.isInp()) {
            opName = "inp";
        } else if (this.isRdp()) {
            opName = "rdp";
        } else if (this.isNo()) {
            opName = "no";
        } else if (this.isNop()) {
            opName = "nop";
        } else if (this.isOutAll()) {
            opName = "out_all";
            LogicTuple[] tupleL = new LogicTuple[] {};
            tupleL = this.getLogicTupleListResult().toArray(tupleL);
            tl = new Term[tupleL.length];
            for (int i = 0; i < tupleL.length; i++) {
                tl[i] = tupleL[i].toTerm();
            }
            LogicTuple lt = null;
            lt = new LogicTuple(opName, new TupleArgument(this
                    .getLogicTupleArgument().toTerm()),
                    new TupleArgument(new Struct(tl)));
            return lt;
        } else if (this.isInAll()) {
            opName = "in_all";
            LogicTuple[] tupleL = new LogicTuple[] {};
            tupleL = this.getLogicTupleListResult().toArray(tupleL);
            tl = new Term[tupleL.length];
            for (int i = 0; i < tupleL.length; i++) {
                tl[i] = tupleL[i].toTerm();
            }
            LogicTuple lt = null;
            lt = new LogicTuple(opName, new TupleArgument(this
                    .getLogicTupleArgument().toTerm()),
                    new TupleArgument(new Struct(tl)));
            return lt;
        } else if (this.isRdAll()) {
            opName = "rd_all";
            LogicTuple[] tupleL = new LogicTuple[] {};
            tupleL = this.getLogicTupleListResult().toArray(tupleL);
            tl = new Term[tupleL.length];
            for (int i = 0; i < tupleL.length; i++) {
                tl[i] = tupleL[i].toTerm();
            }
            LogicTuple lt = null;
            lt = new LogicTuple(opName, new TupleArgument(this
                    .getLogicTupleArgument().toTerm()),
                    new TupleArgument(new Struct(tl)));
            return lt;
        } else if (this.isNoAll()) {
            opName = "no_all";
            LogicTuple[] tupleL = new LogicTuple[] {};
            tupleL = this.getLogicTupleListResult().toArray(tupleL);
            tl = new Term[tupleL.length];
            for (int i = 0; i < tupleL.length; i++) {
                tl[i] = tupleL[i].toTerm();
            }
            LogicTuple lt = null;
            lt = new LogicTuple(opName, new TupleArgument(this
                    .getLogicTupleArgument().toTerm()),
                    new TupleArgument(new Struct(tl)));
            return lt;
        } else if (this.isUrd()) {
            opName = "urd";
        } else if (this.isUin()) {
            opName = "uin";
        } else if (this.isUno()) {
            opName = "uno";
        } else if (this.isUrdp()) {
            opName = "urdp";
        } else if (this.isUinp()) {
            opName = "uinp";
        } else if (this.isUnop()) {
            opName = "unop";
        } else if (this.isGet()) {
            opName = "get";
            LogicTuple[] tupleL = new LogicTuple[] {};
            tupleL = this.getLogicTupleListResult().toArray(tupleL);
            tl = new Term[tupleL.length];
            for (int i = 0; i < tupleL.length; i++) {
                tl[i] = tupleL[i].toTerm();
            }
        } else if (this.isSet()) {
            opName = "set";
            LogicTuple[] tupleL = new LogicTuple[] {};
            tupleL = this.getTupleListArgument().toArray(tupleL);
            tl = new Term[tupleL.length];
            for (int i = 0; i < tupleL.length; i++) {
                tl[i] = tupleL[i].toTerm();
            }
        } else if (this.isOutS()) {
            opName = "out_s";
        } else if (this.isInS()) {
            opName = "in_s";
        } else if (this.isRdS()) {
            opName = "rd_s";
        } else if (this.isInpS()) {
            opName = "inp_s";
        } else if (this.isRdpS()) {
            opName = "rdp_s";
        } else if (this.isNoS()) {
            opName = "no_s";
        } else if (this.isNopS()) {
            opName = "nop_s";
        } else if (this.isGetS()) {
            opName = "get_s";
            LogicTuple[] tupleL = new LogicTuple[] {};
            tupleL = this.getLogicTupleListResult().toArray(tupleL);
            tl = new Term[tupleL.length];
            for (int i = 0; i < tupleL.length; i++) {
                tl[i] = tupleL[i].toTerm();
            }
        } else if (this.isSetS()) {
            opName = "set_s";
            LogicTuple[] tupleL = new LogicTuple[] {};
            tupleL = this.getTupleListArgument().toArray(tupleL);
            tl = new Term[tupleL.length];
            for (int i = 0; i < tupleL.length; i++) {
                tl[i] = tupleL[i].toTerm();
            }
        } else if (this.isGetEnv()) {
            return t;
        } else if (this.isEnv()) {
            opName = "env";
        } else if (this.isSetEnv()) {
            return t;
        } else if (this.isTime()) {
            opName = "time";
        } else {
            opName = "unknownOp";
        }
        return new LogicTuple(opName, new TupleArgument(
                tl != null ? new Struct(tl) : t.toTerm()));
    }
}
