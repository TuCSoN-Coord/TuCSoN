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
import alice.respect.api.IRespectOperation;
import alice.respect.api.RespectSpecification;
import alice.tucson.service.TucsonOperation;
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
public class RespectOperation extends AbstractTupleCentreOperation implements
        IRespectOperation {

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
    public static RespectOperation make(final int opType,
            final LogicTuple t, final OperationCompletionListener l)
            throws InvalidLogicTupleException {
        if (opType == TucsonOperation.getCode()) {
            return RespectOperation.makeGet(new LogicTuple("get"), l);
        }
        if (opType == TucsonOperation.getSCode()) {
            try {
				return RespectOperation.makeGetS(new LogicTuple("spec", new Var(
				        "S")), l);
			} catch (InvalidVarNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        if (opType == TucsonOperation.setCode()) {
            return RespectOperation.makeSet(t, l);
        }
        if (opType == TucsonOperation.setSCode()) {
            //try {
                if ("spec".equals(t.getName())) {
                    return RespectOperation.makeSetS(null);
                }
                return RespectOperation.makeSetS(t, l);
           // } catch (final InvalidLogicTupleOperationException e) {
              //  e.printStackTrace();
          //  }
        }
        if (opType == TucsonOperation.inCode()) {
            return RespectOperation.makeIn(t, l);
        }
        if (opType == TucsonOperation.inAllCode()) {
          //  try {
                if (",".equals(t.getName()) && t.getArity() == 2) {
                    return RespectOperation.makeInAll(
                            new LogicTuple(t.getArg(0)), l);
                }
                return RespectOperation.makeInAll(t, l);
        //    } catch (final InvalidLogicTupleOperationException e) {
          //      e.printStackTrace();
          //  }
        }
        if (opType == TucsonOperation.inpCode()) {
            return RespectOperation.makeInp(t, l);
        }
        if (opType == TucsonOperation.inpSCode()) {
            return RespectOperation.makeInpS(t, l);
        }
        if (opType == TucsonOperation.inSCode()) {
            return RespectOperation.makeInS(t, l);
        }
        if (opType == TucsonOperation.outCode()) {
            return RespectOperation.makeOut(t, l);
        }
        if (opType == TucsonOperation.outAllCode()) {
            return RespectOperation.makeOutAll(t, l);
        }
        if (opType == TucsonOperation.outSCode()) {
            return RespectOperation.makeOutS(t, l);
        }
        if (opType == TucsonOperation.rdCode()) {
            return RespectOperation.makeRd(t, l);
        }
        if (opType == TucsonOperation.rdAllCode()) {
          //  try {
                if (",".equals(t.getName()) && t.getArity() == 2) {
                    return RespectOperation.makeRdAll(
                            new LogicTuple(t.getArg(0)), l);
                }
                return RespectOperation.makeRdAll(t, l);
          //  } catch (final InvalidLogicTupleOperationException e) {
             //   e.printStackTrace();
           // }
        }
        if (opType == TucsonOperation.rdpCode()) {
            return RespectOperation.makeRdp(t, l);
        }
        if (opType == TucsonOperation.rdpSCode()) {
            return RespectOperation.makeRdpS(t, l);
        }
        if (opType == TucsonOperation.rdSCode()) {
            return RespectOperation.makeRdS(t, l);
        }
        if (opType == TucsonOperation.noCode()) {
            return RespectOperation.makeNo(t, l);
        }
        if (opType == TucsonOperation.noAllCode()) {
          //  try {
                if (",".equals(t.getName()) && t.getArity() == 2) {
                    return RespectOperation.makeNoAll(
                            new LogicTuple(t.getArg(0)), l);
                }
                return RespectOperation.makeNoAll(t, l);
          //  } catch (final InvalidLogicTupleOperationException e) {
           //     e.printStackTrace();
          //  }
        }
        if (opType == TucsonOperation.nopCode()) {
            return RespectOperation.makeNop(t, l);
        }
        if (opType == TucsonOperation.noSCode()) {
            return RespectOperation.makeNoS(t, l);
        }
        if (opType == TucsonOperation.nopSCode()) {
            return RespectOperation.makeNopS(t, l);
        }
        if (opType == TucsonOperation.uinCode()) {
            return RespectOperation.makeUin(t, l);
        }
        if (opType == TucsonOperation.urdCode()) {
            return RespectOperation.makeUrd(t, l);
        }
        if (opType == TucsonOperation.unoCode()) {
            return RespectOperation.makeUno(t, l);
        }
        if (opType == TucsonOperation.uinpCode()) {
            return RespectOperation.makeUinp(t, l);
        }
        if (opType == TucsonOperation.urdpCode()) {
            return RespectOperation.makeUrdp(t, l);
        }
        if (opType == TucsonOperation.unopCode()) {
            return RespectOperation.makeUnop(t, l);
        }
        if (opType == TucsonOperation.spawnCode()) {
            return RespectOperation.makeSpawn(t, l);
        }
        if (opType == TucsonOperation.getEnvCode()) {
            return RespectOperation.makeGetEnv(t, l);
        }
        if (opType == TucsonOperation.setEnvCode()) {
            return RespectOperation.makeSetEnv(t, l);
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
    public static RespectOperation makeFrom(final LogicTuple t,
            final OperationCompletionListener l) {
        final RespectOperation temp = new RespectOperation(
                RespectOperation.OPTYPE_FROM, t, l);
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
    public static RespectOperation makeGet(final LogicTuple t,
            final OperationCompletionListener l) {
        final RespectOperation temp = new RespectOperation(
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
    public static RespectOperation makeGetEnv(final LogicTuple t,
            final OperationCompletionListener l) {
        final RespectOperation temp = new RespectOperation(
                RespectOperation.OPTYPE_GET_ENV, t, l);
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
    public static RespectOperation makeGetS(final LogicTuple t,
            final OperationCompletionListener l) {
        final RespectOperation temp = new RespectOperation(
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
    public static RespectOperation makeIn(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_IN, t,
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
    public static RespectOperation makeInAll(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_IN_ALL,
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
    public static RespectOperation makeInp(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_INP, t,
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
    public static RespectOperation makeInpS(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_INP_S,
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
    public static RespectOperation makeInS(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_IN_S,
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
    public static RespectOperation makeNo(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_NO, t,
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
    public static RespectOperation makeNoAll(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_NO_ALL,
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
    public static RespectOperation makeNop(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_NOP, t,
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
    public static RespectOperation makeNopS(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_NOP_S,
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
    public static RespectOperation makeNoS(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_NO_S,
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
    public static RespectOperation makeOut(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_OUT,
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
    public static RespectOperation makeOutAll(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(
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
    public static RespectOperation makeOutS(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_OUT_S,
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
    public static RespectOperation makeRd(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_RD, t,
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
    public static RespectOperation makeRdAll(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_RD_ALL,
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
    public static RespectOperation makeRdp(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_RDP, t,
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
    public static RespectOperation makeRdpS(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_RDP_S,
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
    public static RespectOperation makeRdS(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_RD_S,
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
    public static RespectOperation makeSet(final LogicTuple t,
            final OperationCompletionListener l) {
        if ("[]".equals(t.toString())) {
            return new RespectOperation(
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
        final RespectOperation temp = new RespectOperation(
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
    public static RespectOperation makeSetEnv(final LogicTuple t,
            final OperationCompletionListener l) {
        final RespectOperation temp = new RespectOperation(
                RespectOperation.OPTYPE_SET_ENV, t, l);
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
    public static RespectOperation makeSetS(final LogicTuple t,
            final OperationCompletionListener l) {
        if ("[]".equals(t.toString())) {
            return new RespectOperation(
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
        final RespectOperation temp = new RespectOperation(
                AbstractTupleCentreOperation.OPTYPE_SET_S, list, l);
        return temp;
    }

    /**
     *
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperation makeSetS(final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_SET_S,
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
    public static RespectOperation makeSetS(final RespectSpecification spec,
            final OperationCompletionListener l) {
        RespectOperation temp = null;
        try {
            temp = new RespectOperation(
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
    public static RespectOperation makeSpawn(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_SPAWN,
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
    public static RespectOperation makeTime(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(RespectOperation.OPTYPE_TIME, t, l);
    }
    
    /**
     * 
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @return the ReSpecT operation built
     */
    public static RespectOperation makeTo(final LogicTuple t,
            final OperationCompletionListener l) {
        final RespectOperation temp = new RespectOperation(
                RespectOperation.OPTYPE_TO, t, l);
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
    public static RespectOperation makeUin(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_UIN, t,
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
    public static RespectOperation makeUinp(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_UINP,
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
    public static RespectOperation makeUno(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_UNO, t,
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
    public static RespectOperation makeUnop(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_UNOP,
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
    public static RespectOperation makeUrd(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_URD, t,
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
    public static RespectOperation makeUrdp(final LogicTuple t,
            final OperationCompletionListener l) {
        return new RespectOperation(AbstractTupleCentreOperation.OPTYPE_URDP,
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
    protected RespectOperation(final int type, final List<Tuple> tupleList,
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
    protected RespectOperation(final int type, final Tuple t,
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
    protected RespectOperation(final int type, final TupleTemplate t,
            final OperationCompletionListener l) {
        super(type, t, l);
    }

    @Override
    public LogicTuple getLogicTupleArgument() {
        if (this.isOut() || this.isOutS() || this.isOutAll() || this.isSpawn()) {
            return (LogicTuple) this.getTupleArgument();
        }
        return (LogicTuple) this.getTemplateArgument();
    }

    @Override
    public List<LogicTuple> getLogicTupleListResult() {
        final List<? extends Tuple> tl = this.getTupleListResult();
        final List<LogicTuple> tll = new LinkedList<LogicTuple>();
        if (tl == null) {
            return new LinkedList<LogicTuple>();
        }
        for (final Tuple t : tl) {
            tll.add((LogicTuple) t);
        }
        return tll;
    }

    @Override
    public LogicTuple getLogicTupleResult() {
        return (LogicTuple) this.getTupleResult();
    }

    @Override
    public boolean isEnv() {
        return this.getType() == RespectOperation.OPTYPE_ENV;
    }

    @Override
    public boolean isGetEnv() {
        return this.getType() == RespectOperation.OPTYPE_GET_ENV;
    }

    @Override
    public boolean isSetEnv() {
        return this.getType() == RespectOperation.OPTYPE_SET_ENV;
    }

    @Override
    public boolean isTime() {
        return this.getType() == RespectOperation.OPTYPE_TIME;
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
