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
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;
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
            return RespectOperationDefault.makeGet(new LogicTuple("get"), listener);
        }
        if (opType == GET_S) {
            try {
                return RespectOperationDefault.makeGetS(new LogicTuple("spec", new Var(
                        "S")), listener);
            } catch (InvalidVarNameException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (tuple == null) {
            throw new InvalidLogicTupleException();
        }
        if (opType == SET) {
            return RespectOperationDefault.makeSet(tuple, listener);
        }
        if (opType == SET_S) {
            //try {
            if ("spec".equals(tuple.getName())) {
                return RespectOperationDefault.makeSetS(null);
            }
            return RespectOperationDefault.makeSetS(tuple, listener);
            // } catch (final InvalidLogicTupleOperationException e) {
            //  e.printStackTrace();
            //  }
        }
        if (opType == IN) {
            return RespectOperationDefault.makeIn(tuple, listener);
        }
        if (opType == IN_ALL) {
            //  try {
            if (",".equals(tuple.getName()) && tuple.getArity() == 2) {
                return RespectOperationDefault.makeInAll(
                        new LogicTuple(tuple.getArg(0)), listener);
            }
            return RespectOperationDefault.makeInAll(tuple, listener);
            //    } catch (final InvalidLogicTupleOperationException e) {
            //      e.printStackTrace();
            //  }
        }
        if (opType == INP) {
            return RespectOperationDefault.makeInp(tuple, listener);
        }
        if (opType == INP_S) {
            return RespectOperationDefault.makeInpS(tuple, listener);
        }
        if (opType == IN_S) {
            return RespectOperationDefault.makeInS(tuple, listener);
        }
        if (opType == OUT) {
            return RespectOperationDefault.makeOut(tuple, listener);
        }
        if (opType == OUT_ALL) {
            return RespectOperationDefault.makeOutAll(tuple, listener);
        }
        if (opType == OUT_S) {
            return RespectOperationDefault.makeOutS(tuple, listener);
        }
        if (opType == RD) {
            return RespectOperationDefault.makeRd(tuple, listener);
        }
        if (opType == RD_ALL) {
            //  try {
            if (",".equals(tuple.getName()) && tuple.getArity() == 2) {
                return RespectOperationDefault.makeRdAll(
                        new LogicTuple(tuple.getArg(0)), listener);
            }
            return RespectOperationDefault.makeRdAll(tuple, listener);
            //  } catch (final InvalidLogicTupleOperationException e) {
            //   e.printStackTrace();
            // }
        }
        if (opType == RDP) {
            return RespectOperationDefault.makeRdp(tuple, listener);
        }
        if (opType == RDP_S) {
            return RespectOperationDefault.makeRdpS(tuple, listener);
        }
        if (opType == RD_S) {
            return RespectOperationDefault.makeRdS(tuple, listener);
        }
        if (opType == NO) {
            return RespectOperationDefault.makeNo(tuple, listener);
        }
        if (opType == NO_ALL) {
            //  try {
            if (",".equals(tuple.getName()) && tuple.getArity() == 2) {
                return RespectOperationDefault.makeNoAll(
                        new LogicTuple(tuple.getArg(0)), listener);
            }
            return RespectOperationDefault.makeNoAll(tuple, listener);
            //  } catch (final InvalidLogicTupleOperationException e) {
            //     e.printStackTrace();
            //  }
        }
        if (opType == NOP) {
            return RespectOperationDefault.makeNop(tuple, listener);
        }
        if (opType == NO_S) {
            return RespectOperationDefault.makeNoS(tuple, listener);
        }
        if (opType == NOP_S) {
            return RespectOperationDefault.makeNopS(tuple, listener);
        }
        if (opType == UIN) {
            return RespectOperationDefault.makeUin(tuple, listener);
        }
        if (opType == URD) {
            return RespectOperationDefault.makeUrd(tuple, listener);
        }
        if (opType == UNO) {
            return RespectOperationDefault.makeUno(tuple, listener);
        }
        if (opType == UINP) {
            return RespectOperationDefault.makeUinp(tuple, listener);
        }
        if (opType == URDP) {
            return RespectOperationDefault.makeUrdp(tuple, listener);
        }
        if (opType == UNOP) {
            return RespectOperationDefault.makeUnop(tuple, listener);
        }
        if (opType == SPAWN) {
            return RespectOperationDefault.makeSpawn(tuple, listener);
        }
        if (opType == GET_ENV) {
            return RespectOperationDefault.makeGetEnv(tuple, listener);
        }
        if (opType == SET_ENV) {
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
        final RespectOperationDefault temp = new RespectOperationDefault(
                FROM, t, l);
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
        final RespectOperationDefault temp = new RespectOperationDefault(
                GET, (Tuple) t, l);
        return temp;
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeGetEnv(final LogicTuple t,
                                                     final OperationCompletionListener l) {
        final RespectOperationDefault temp = new RespectOperationDefault(
                GET_ENV, t, l);
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
        final RespectOperationDefault temp = new RespectOperationDefault(
                GET_S, (Tuple) t, l);
        return temp;
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeIn(final LogicTuple t,
                                                 final OperationCompletionListener l) {
        return new RespectOperationDefault(IN, t,
                l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeInAll(final LogicTuple t,
                                                    final OperationCompletionListener l) {
        return new RespectOperationDefault(IN_ALL,
                t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeInp(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(INP, t,
                l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeInpS(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(INP_S,
                t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeInS(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(IN_S,
                t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeNo(final LogicTuple t,
                                                 final OperationCompletionListener l) {
        return new RespectOperationDefault(NO, t,
                l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeNoAll(final LogicTuple t,
                                                    final OperationCompletionListener l) {
        return new RespectOperationDefault(NO_ALL,
                t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeNop(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(NOP, t,
                l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeNopS(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(NOP_S,
                t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeNoS(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(NO_S,
                t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeOut(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(OUT,
                (Tuple) t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeOutAll(final LogicTuple t,
                                                     final OperationCompletionListener l) {
        return new RespectOperationDefault(
                OUT_ALL, (Tuple) t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeOutS(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(OUT_S,
                (Tuple) t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeRd(final LogicTuple t,
                                                 final OperationCompletionListener l) {
        return new RespectOperationDefault(RD, t,
                l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeRdAll(final LogicTuple t,
                                                    final OperationCompletionListener l) {
        return new RespectOperationDefault(RD_ALL,
                t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeRdp(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(RDP, t,
                l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeRdpS(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(RDP_S,
                t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeRdS(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(RD_S,
                t, l);
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
        final RespectOperationDefault temp = new RespectOperationDefault(
                SET_ENV, t, l);
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
                SET_S, list, l);
        return temp;
    }

    /**
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeSetS(final OperationCompletionListener l) {
        return new RespectOperationDefault(SET_S,
                new LogicTuple(), l);
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
                    (Tuple) LogicTuple.parse(spec.toString()), l);
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
        return new RespectOperationDefault(SPAWN,
                (Tuple) t, l);
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
        final RespectOperationDefault temp = new RespectOperationDefault(
                TO, t, l);
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
        return new RespectOperationDefault(UIN, t,
                l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeUinp(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(UINP,
                t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeUno(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(UNO, t,
                l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeUnop(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(UNOP,
                t, l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeUrd(final LogicTuple t,
                                                  final OperationCompletionListener l) {
        return new RespectOperationDefault(URD, t,
                l);
    }

    /**
     * @param t the tuple argument of the operation
     * @param l the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperationDefault makeUrdp(final LogicTuple t,
                                                   final OperationCompletionListener l) {
        return new RespectOperationDefault(URDP,
                t, l);
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
    public LogicTuple getLogicTupleArgument() {
        // TODO cannot move all to AbstractTupleCentreOperation because condition changes from TucsonOperationDefault
        // check why
        if (this.getType() == OUT
                || this.getType() == OUT_S
                || this.getType() == OUT_ALL
                || this.getType() == SPAWN) {

            return (LogicTuple) this.getTupleArgument();
        }
        return (LogicTuple) this.getTemplateArgument();
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
        String opName;
        if (this.getType() == SPAWN) {
            opName = "spawn";
        } else if (this.getType() == OUT) {
            opName = "out";
        } else if (this.getType() == IN) {
            opName = "in";
        } else if (this.getType() == RD) {
            opName = "rd";
        } else if (this.getType() == INP) {
            opName = "inp";
        } else if (this.getType() == RDP) {
            opName = "rdp";
        } else if (this.getType() == NO) {
            opName = "no";
        } else if (this.getType() == NOP) {
            opName = "nop";
        } else if (this.getType() == OUT_ALL) {
            opName = "out_all";
            LogicTuple[] tupleL = new LogicTuple[]{};
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
        } else if (this.getType() == IN_ALL) {
            opName = "in_all";
            LogicTuple[] tupleL = new LogicTuple[]{};
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
        } else if (this.getType() == RD_ALL) {
            opName = "rd_all";
            LogicTuple[] tupleL = new LogicTuple[]{};
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
        } else if (this.getType() == NO_ALL) {
            opName = "no_all";
            LogicTuple[] tupleL = new LogicTuple[]{};
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
        } else if (this.getType() == URD) {
            opName = "urd";
        } else if (this.getType() == UIN) {
            opName = "uin";
        } else if (this.getType() == UNO) {
            opName = "uno";
        } else if (this.getType() == URDP) {
            opName = "urdp";
        } else if (this.getType() == UINP) {
            opName = "uinp";
        } else if (this.getType() == UNOP) {
            opName = "unop";
        } else if (this.getType() == GET) {
            opName = "get";
            LogicTuple[] tupleL = new LogicTuple[]{};
            tupleL = this.getLogicTupleListResult().toArray(tupleL);
            tl = new Term[tupleL.length];
            for (int i = 0; i < tupleL.length; i++) {
                tl[i] = tupleL[i].toTerm();
            }
        } else if (this.getType() == SET) {
            opName = "set";
            LogicTuple[] tupleL = new LogicTuple[]{};
            tupleL = this.getTupleListArgument().toArray(tupleL);
            tl = new Term[tupleL.length];
            for (int i = 0; i < tupleL.length; i++) {
                tl[i] = tupleL[i].toTerm();
            }
        } else if (this.getType() == OUT_S) {
            opName = "out_s";
        } else if (this.getType() == IN_S) {
            opName = "in_s";
        } else if (this.getType() == RD_S) {
            opName = "rd_s";
        } else if (this.getType() == INP_S) {
            opName = "inp_s";
        } else if (this.getType() == RDP_S) {
            opName = "rdp_s";
        } else if (this.getType() == NO_S) {
            opName = "no_s";
        } else if (this.getType() == NOP_S) {
            opName = "nop_s";
        } else if (this.getType() == GET_S) {
            opName = "get_s";
            LogicTuple[] tupleL = new LogicTuple[]{};
            tupleL = this.getLogicTupleListResult().toArray(tupleL);
            tl = new Term[tupleL.length];
            for (int i = 0; i < tupleL.length; i++) {
                tl[i] = tupleL[i].toTerm();
            }
        } else if (this.getType() == SET_S) {
            opName = "set_s";
            LogicTuple[] tupleL = new LogicTuple[]{};
            tupleL = this.getTupleListArgument().toArray(tupleL);
            tl = new Term[tupleL.length];
            for (int i = 0; i < tupleL.length; i++) {
                tl[i] = tupleL[i].toTerm();
            }
        } else if (this.getType() == GET_ENV) {
            return t;
        } else if (this.getType() == ENV) {
            opName = "env";
        } else if (this.getType() == SET_ENV) {
            return t;
        } else if (this.getType() == TIME) {
            opName = "time";
        } else {
            opName = "unknownOp";
        }
        return new LogicTuple(opName, new TupleArgument(
                tl != null ? new Struct(tl) : t.toTerm()));
    }
}
