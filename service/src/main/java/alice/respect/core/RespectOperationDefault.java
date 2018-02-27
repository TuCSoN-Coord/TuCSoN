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

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTupleDefault;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.TupleArgument;
import alice.tuple.logic.TupleArgumentDefault;
import alice.tuple.logic.VarArgument;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuple.logic.exceptions.InvalidVarNameException;
import alice.respect.api.RespectOperation;
import alice.respect.api.RespectSpecification;
import alice.tuple.Tuple;
import alice.tuple.TupleTemplate;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.OperationCompletionListener;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;

import static alice.tuplecentre.core.TupleCentreOpType.*;

/**
 * This class represents a ReSpecT operation.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public class RespectOperationDefault extends AbstractTupleCentreOperation implements RespectOperation {


    /**
     * Factory Method
     *
     * @param opType   the type of the operation
     * @param tuple    the tuple argument of the operation, except for <code>get</code>, <code>get_s</code> it <b>should be not null</b>
     * @param listener the listener to notify upon operation completion
     * @return the ReSpecT operation built
     * @throws InvalidLogicTupleException if the given logic tuple is not a valid logic tuple
     */
    public static RespectOperationDefault make(final TupleCentreOpType opType,
                                               final LogicTuple tuple, final OperationCompletionListener listener)
            throws InvalidLogicTupleException {
        if (opType == GET) {
            return RespectOperationDefault.makeGet(new LogicTupleDefault("get"), listener);
        }
        if (opType == GET_S) {
            try {
                return RespectOperationDefault.makeGetS(new LogicTupleDefault("spec", new VarArgument(
                        "S")), listener);
            } catch (InvalidVarNameException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // TODO because of public specific makeXXX methods, this control is easily avoided using them
        // TODO could be a good idea to delete them, using this "make" (maybe replacing this checked exception with an IllegalArgument)
        if (tuple == null) {
            throw new InvalidLogicTupleException();
        }

        switch (opType) {
            case SET:
                return RespectOperationDefault.makeSet(tuple, listener);
            case SET_S:
                //try {
                if ("spec".equals(tuple.getName())) {
                    return RespectOperationDefault.makeSetS(null);
                }
                return RespectOperationDefault.makeSetS(tuple, listener);
            // } catch (final InvalidLogicTupleOperationException e) {
            //  e.printStackTrace();
            //  }
            case IN:
                return RespectOperationDefault.makeIn(tuple, listener);
            case IN_ALL:
                //  try {
                if (",".equals(tuple.getName()) && tuple.getArity() == 2) {
                    return RespectOperationDefault.makeInAll(
                            new LogicTupleDefault(tuple.getArg(0)), listener);
                }
                return RespectOperationDefault.makeInAll(tuple, listener);
            //    } catch (final InvalidLogicTupleOperationException e) {
            //      e.printStackTrace();
            //  }
            case INP:
                return RespectOperationDefault.makeInp(tuple, listener);
            case INP_S:
                return RespectOperationDefault.makeInpS(tuple, listener);
            case IN_S:
                return RespectOperationDefault.makeInS(tuple, listener);
            case OUT:
                return RespectOperationDefault.makeOut(tuple, listener);
            case OUT_ALL:
                return RespectOperationDefault.makeOutAll(tuple, listener);
            case OUT_S:
                return RespectOperationDefault.makeOutS(tuple, listener);
            case RD:
                return RespectOperationDefault.makeRd(tuple, listener);
            case RD_ALL:
                //  try {
                if (",".equals(tuple.getName()) && tuple.getArity() == 2) {
                    return RespectOperationDefault.makeRdAll(
                            new LogicTupleDefault(tuple.getArg(0)), listener);
                }
                return RespectOperationDefault.makeRdAll(tuple, listener);
            //  } catch (final InvalidLogicTupleOperationException e) {
            //   e.printStackTrace();
            // }
            case RDP:
                return RespectOperationDefault.makeRdp(tuple, listener);
            case RDP_S:
                return RespectOperationDefault.makeRdpS(tuple, listener);
            case RD_S:
                return RespectOperationDefault.makeRdS(tuple, listener);
            case NO:
                return RespectOperationDefault.makeNo(tuple, listener);
            case NO_ALL:
                //  try {
                if (",".equals(tuple.getName()) && tuple.getArity() == 2) {
                    return RespectOperationDefault.makeNoAll(
                            new LogicTupleDefault(tuple.getArg(0)), listener);
                }
                return RespectOperationDefault.makeNoAll(tuple, listener);
            //  } catch (final InvalidLogicTupleOperationException e) {
            //     e.printStackTrace();
            //  }
            case NOP:
                return RespectOperationDefault.makeNop(tuple, listener);
            case NO_S:
                return RespectOperationDefault.makeNoS(tuple, listener);
            case NOP_S:
                return RespectOperationDefault.makeNopS(tuple, listener);
            case UIN:
                return RespectOperationDefault.makeUin(tuple, listener);
            case URD:
                return RespectOperationDefault.makeUrd(tuple, listener);
            case UNO:
                return RespectOperationDefault.makeUno(tuple, listener);
            case UINP:
                return RespectOperationDefault.makeUinp(tuple, listener);
            case URDP:
                return RespectOperationDefault.makeUrdp(tuple, listener);
            case UNOP:
                return RespectOperationDefault.makeUnop(tuple, listener);
            case SPAWN:
                return RespectOperationDefault.makeSpawn(tuple, listener);
            case GET_ENV:
                return RespectOperationDefault.makeGetEnv(tuple, listener);
            case SET_ENV:
                return RespectOperationDefault.makeSetEnv(tuple, listener);
        }
        return null;
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeFrom(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        final RespectOperationDefault temp = new RespectOperationDefault(FROM, t, l);
        temp.setTupleResult(t);
        return temp;
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeGet(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(GET, (Tuple) t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeGetEnv(final LogicTuple t,
                                                     final OperationCompletionListener l) {
        final RespectOperationDefault temp = new RespectOperationDefault(GET_ENV, t, l);
        temp.setTupleResult(t);
        return temp;
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeGetS(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(GET_S, (Tuple) t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeIn(final LogicTuple t,
                                                 final OperationCompletionListener l) {
        return new RespectOperationDefault(IN, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeInAll(final LogicTuple t,
                                                    final OperationCompletionListener l) {
        return new RespectOperationDefault(IN_ALL, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeInp(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(INP, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeInpS(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(INP_S, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeInS(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(IN_S, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeNo(final LogicTuple t,
                                                 final OperationCompletionListener l) {
        return new RespectOperationDefault(NO, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeNoAll(final LogicTuple t,
                                                    final OperationCompletionListener l) {
        return new RespectOperationDefault(NO_ALL, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeNop(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(NOP, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeNopS(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(NOP_S, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeNoS(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(NO_S, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeOut(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(OUT, (Tuple) t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeOutAll(final LogicTuple t,
                                                     final OperationCompletionListener l) {
        return new RespectOperationDefault(OUT_ALL, (Tuple) t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeOutS(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(OUT_S, (Tuple) t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeRd(final LogicTuple t,
                                                 final OperationCompletionListener l) {
        return new RespectOperationDefault(RD, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeRdAll(final LogicTuple t,
                                                    final OperationCompletionListener l) {
        return new RespectOperationDefault(RD_ALL, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeRdp(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(RDP, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeRdpS(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(RDP_S, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeRdS(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(RD_S, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeSet(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        if ("[]".equals(t.toString())) {
            return new RespectOperationDefault(
                    SET,
                    new LinkedList<Tuple>(), l);
        }
        final List<Tuple> list = new LinkedList<Tuple>();
        LogicTuple cpy = null;
        cpy = LogicTuples.newInstance(t);
        TupleArgument arg;
        arg = cpy.getArg(0);
        while (arg != null) {
            if (!arg.isList()) {
                final LogicTuple t1 = new LogicTupleDefault(arg);
                list.add(t1);
                arg = cpy.getArg(1);
            } else {
                final LogicTuple t2 = new LogicTupleDefault(arg);
                cpy = t2;
                if (!"[]".equals(cpy.toString())) {
                    arg = cpy.getArg(0);
                } else {
                    arg = null;
                }
            }
        }
        final RespectOperationDefault temp = new RespectOperationDefault(
                SET, list, l);
        return temp;
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeSetEnv(final LogicTuple t,
                                                     final OperationCompletionListener l) {
        final RespectOperationDefault temp = new RespectOperationDefault(SET_ENV, t, l);
        temp.setTupleResult(t);
        return temp;
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeSetS(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        if ("[]".equals(t.toString())) {
            return new RespectOperationDefault(
                    SET_S,
                    new LinkedList<Tuple>(), l);
        }
        final List<Tuple> list = new LinkedList<Tuple>();
        LogicTuple cpy = null;
        cpy = LogicTuples.newInstance(t);
        TupleArgument arg;
        arg = cpy.getArg(0);
        while (arg != null) {
            if (!arg.isList()) {
                final LogicTuple t1 = new LogicTupleDefault(arg);
                list.add(t1);
                arg = cpy.getArg(1);
            } else {
                final LogicTuple t2 = new LogicTupleDefault(arg);
                cpy = t2;
                if (!"[]".equals(cpy.toString())) {
                    arg = cpy.getArg(0);
                } else {
                    arg = null;
                }
            }
        }
        final RespectOperationDefault temp = new RespectOperationDefault(
                SET_S, list, l);
        return temp;
    }

    /**
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeSetS(final OperationCompletionListener l) {
        return new RespectOperationDefault(SET_S, new LogicTupleDefault(), l);
    }

    /**
     * @param spec the ReSpecT specification argument of the operation
     * @param l    the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeSetS(final RespectSpecification spec,
                                                   final OperationCompletionListener l) {
        RespectOperationDefault temp = null;
        try {
            temp = new RespectOperationDefault(
                    SET_S,
                    (Tuple) LogicTuples.parse(spec.toString()), l);
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeSpawn(final LogicTuple t,
                                                    final OperationCompletionListener l) {
        return new RespectOperationDefault(SPAWN, (Tuple) t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeTime(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(TIME, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeTo(final LogicTuple t,
                                                 final OperationCompletionListener l) {
        final RespectOperationDefault temp = new RespectOperationDefault(TO, t, l);
        temp.setTupleResult(t);
        return temp;
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeUin(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(UIN, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeUinp(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(UINP, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeUno(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(UNO, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeUnop(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(UNOP, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeUrd(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(URD, t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeUrdp(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(URDP, t, l);
    }

    /**
     * @param type      the type-code of the oepration
     * @param tupleList the list of tuples argument of the operation
     * @param l         the listener for operation completion
     */
    protected RespectOperationDefault(final TupleCentreOpType type, final List<Tuple> tupleList,
                                      final OperationCompletionListener l) {
        super(type, tupleList, l);
    }

    /**
     * @param type the type-code of the oepration
     * @param t    the tuple argument of the operation
     * @param l    the listener for operation completion
     */
    protected RespectOperationDefault(final TupleCentreOpType type, final Tuple t,
                                      final OperationCompletionListener l) {
        super(type, t, l);
    }

    /**
     * @param type the type-code of the oepration
     * @param t    the tuple template argument of the operation
     * @param l    the listener for operation completion
     */
    protected RespectOperationDefault(final TupleCentreOpType type, final TupleTemplate t,
                                      final OperationCompletionListener l) {
        super(type, t, l);
    }

    @Override
    public String toString() {
        return this.toTuple().toString();
    }

    @Override
    public LogicTuple toTuple() {
        LogicTuple t;
        Term[] tl = null;
        if (this.isOperationCompleted()) {
            t = this.getLogicTupleResult();
        } else {
            t = this.getLogicTupleArgument();
        }
        String opName = this.getType().name().toLowerCase();
        switch (this.getType()) {
            case SPAWN:
            case OUT:
            case IN:
            case INP:
            case RD:
            case RDP:
            case NO:
            case NOP:
            case URD:
            case UIN:
            case UNO:
            case URDP:
            case UINP:
            case UNOP:
            case OUT_S:
            case IN_S:
            case RD_S:
            case INP_S:
            case RDP_S:
            case NO_S:
            case NOP_S:
            case ENV:
            case TIME:
                break; //because opName was set at the beginning with the constant name toLowerCase

            case OUT_ALL: {
                LogicTuple[] tupleL = new LogicTuple[]{};
                tupleL = this.getLogicTupleListResult().toArray(tupleL);
                tl = new Term[tupleL.length];
                for (int i = 0; i < tupleL.length; i++) {
                    tl[i] = tupleL[i].toTerm();
                }
                LogicTuple lt = null;
                lt = new LogicTupleDefault(opName, new TupleArgumentDefault(this
                        .getLogicTupleArgument().toTerm()),
                        new TupleArgumentDefault(new Struct(tl)));
                return lt;
            }
            case IN_ALL: {
                LogicTuple[] tupleL = new LogicTuple[]{};
                tupleL = this.getLogicTupleListResult().toArray(tupleL);
                tl = new Term[tupleL.length];
                for (int i = 0; i < tupleL.length; i++) {
                    tl[i] = tupleL[i].toTerm();
                }
                LogicTuple lt = null;
                lt = new LogicTupleDefault(opName, new TupleArgumentDefault(this
                        .getLogicTupleArgument().toTerm()),
                        new TupleArgumentDefault(new Struct(tl)));
                return lt;
            }
            case RD_ALL: {
                LogicTuple[] tupleL = new LogicTuple[]{};
                tupleL = this.getLogicTupleListResult().toArray(tupleL);
                tl = new Term[tupleL.length];
                for (int i = 0; i < tupleL.length; i++) {
                    tl[i] = tupleL[i].toTerm();
                }
                LogicTuple lt = null;
                lt = new LogicTupleDefault(opName, new TupleArgumentDefault(this
                        .getLogicTupleArgument().toTerm()),
                        new TupleArgumentDefault(new Struct(tl)));
                return lt;
            }
            case NO_ALL: {
                LogicTuple[] tupleL = new LogicTuple[]{};
                tupleL = this.getLogicTupleListResult().toArray(tupleL);
                tl = new Term[tupleL.length];
                for (int i = 0; i < tupleL.length; i++) {
                    tl[i] = tupleL[i].toTerm();
                }
                LogicTuple lt = null;
                lt = new LogicTupleDefault(opName, new TupleArgumentDefault(this
                        .getLogicTupleArgument().toTerm()),
                        new TupleArgumentDefault(new Struct(tl)));
                return lt;
            }
            case GET: {
                LogicTuple[] tupleL = new LogicTuple[]{};
                tupleL = this.getLogicTupleListResult().toArray(tupleL);
                tl = new Term[tupleL.length];
                for (int i = 0; i < tupleL.length; i++) {
                    tl[i] = tupleL[i].toTerm();
                }
                break;
            }
            case SET: {
                LogicTuple[] tupleL = new LogicTuple[]{};
                tupleL = this.getTupleListArgument().toArray(tupleL);
                tl = new Term[tupleL.length];
                for (int i = 0; i < tupleL.length; i++) {
                    tl[i] = tupleL[i].toTerm();
                }
                break;
            }
            case GET_S: {
                LogicTuple[] tupleL = new LogicTuple[]{};
                tupleL = this.getLogicTupleListResult().toArray(tupleL);
                tl = new Term[tupleL.length];
                for (int i = 0; i < tupleL.length; i++) {
                    tl[i] = tupleL[i].toTerm();
                }
                break;
            }
            case SET_S: {
                LogicTuple[] tupleL = new LogicTuple[]{};
                tupleL = this.getTupleListArgument().toArray(tupleL);
                tl = new Term[tupleL.length];
                for (int i = 0; i < tupleL.length; i++) {
                    tl[i] = tupleL[i].toTerm();
                }
                break;
            }
            case GET_ENV:
            case SET_ENV:
                return t;
            default:
                opName = "unknownOp";
                break;
        }
        return new LogicTupleDefault(opName, new TupleArgumentDefault(
                tl != null ? new Struct(tl) : t.toTerm()));
    }
}
