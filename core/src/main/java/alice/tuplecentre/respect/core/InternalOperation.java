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
package alice.tuplecentre.respect.core;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.TupleArguments;

/**
 * Represents an internal operation of a ReSpecT virtual machine.
 * <p>
 * The possible argument of the operation is represented by a logic tuple
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public final class InternalOperation {

    private static final int OPTYPE_GET_ENV = 77;
    private static final int OPTYPE_GET_R = 5;
    private static final int OPTYPE_GET_S_R = 18;
    private static final int OPTYPE_IN_ALLR = 11;
    private static final int OPTYPE_IN_SR = 15;
    private static final int OPTYPE_INR = 2;
    private static final int OPTYPE_NO_ALLR = 13;
    private static final int OPTYPE_NO_SR = 17;
    private static final int OPTYPE_NOR = 4;
    private static final int OPTYPE_OUT_ALLR = 10;
    private static final int OPTYPE_OUT_SR = 14;
    private static final int OPTYPE_OUTR = 1;
    private static final int OPTYPE_RD_ALLR = 12;
    private static final int OPTYPE_RD_SR = 16;
    private static final int OPTYPE_RDR = 3;
    private static final int OPTYPE_SET_ENV = 78;
    private static final int OPTYPE_SET_R = 6;
    private static final int OPTYPE_SET_S_R = 19;
    private static final int OPTYPE_SPAWN_R = 66;
    private static final int OPTYPE_UINR = 7;
    private static final int OPTYPE_UNOR = 9;
    private static final int OPTYPE_URDR = 8;

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeGetEnv(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_GET_ENV, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeGetR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_GET_R, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeGetSR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_GET_S_R, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeInAllR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_IN_ALLR, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeInR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_INR, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeInSR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_IN_SR, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeNoAllR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_NO_ALLR, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeNoR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_NOR, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeNoSR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_NO_SR, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeOutAllR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_OUT_ALLR, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeOutR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_OUTR, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeOutSR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_OUT_SR, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeRdAllR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_RD_ALLR, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeRdR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_RDR, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeRdSR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_RD_SR, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeSetEnv(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_SET_ENV, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeSetR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_SET_R, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeSetSR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_SET_S_R, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeSpawnR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_SPAWN_R, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeUinR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_UINR, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeUnoR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_UNOR, t);
    }

    /**
     * @param t the tuple argument of this internal operation
     * @return the internal operation built
     */
    public static InternalOperation makeUrdR(final LogicTuple t) {
        return new InternalOperation(InternalOperation.OPTYPE_URDR, t);
    }

    private final LogicTuple arg;
    private LogicTuple result;
    private final int type;

    private InternalOperation(final int t, final LogicTuple a) {
        this.arg = a;
        this.type = t;
    }

    /**
     * @return the tuple argument of this internal operation
     */
    public LogicTuple getArgument() {
        return this.arg;
    }

    /**
     * @return the tuple result of this internal operation
     */
    public LogicTuple getResult() {
        return this.result;
    }

    /**
     * @return <code>true</code> if this is a get_env
     */
    public boolean isGetEnv() {
        return this.type == InternalOperation.OPTYPE_GET_ENV;
    }

    /**
     * @return <code>true</code> if this is a get
     */
    public boolean isGetR() {
        return this.type == InternalOperation.OPTYPE_GET_R;
    }

    /**
     * @return <code>true</code> if this is a get_s
     */
    public boolean isGetSR() {
        return this.type == InternalOperation.OPTYPE_GET_S_R;
    }

    /**
     * @return <code>true</code> if this is a in_all
     */
    public boolean isInAllR() {
        return this.type == InternalOperation.OPTYPE_IN_ALLR;
    }

    /**
     * @return <code>true</code> if this is a in
     */
    public boolean isInR() {
        return this.type == InternalOperation.OPTYPE_INR;
    }

    /**
     * @return <code>true</code> if this is a in_s
     */
    public boolean isInSR() {
        return this.type == InternalOperation.OPTYPE_IN_SR;
    }

    /**
     * @return <code>true</code> if this is a no_all
     */
    public boolean isNoAllR() {
        return this.type == InternalOperation.OPTYPE_NO_ALLR;
    }

    /**
     * @return <code>true</code> if this is a no
     */
    public boolean isNoR() {
        return this.type == InternalOperation.OPTYPE_NOR;
    }

    /**
     * @return <code>true</code> if this is a no_s
     */
    public boolean isNoSR() {
        return this.type == InternalOperation.OPTYPE_NO_SR;
    }

    /**
     * @return <code>true</code> if this is a out_all
     */
    public boolean isOutAllR() {
        return this.type == InternalOperation.OPTYPE_OUT_ALLR;
    }

    /**
     * @return <code>true</code> if this is a out
     */
    public boolean isOutR() {
        return this.type == InternalOperation.OPTYPE_OUTR;
    }

    /**
     * @return <code>true</code> if this is a out_s
     */
    public boolean isOutSR() {
        return this.type == InternalOperation.OPTYPE_OUT_SR;
    }

    /**
     * @return <code>true</code> if this is a rd_all
     */
    public boolean isRdAllR() {
        return this.type == InternalOperation.OPTYPE_RD_ALLR;
    }

    /**
     * @return <code>true</code> if this is a rd
     */
    public boolean isRdR() {
        return this.type == InternalOperation.OPTYPE_RDR;
    }

    /**
     * @return <code>true</code> if this is a rd_s
     */
    public boolean isRdSR() {
        return this.type == InternalOperation.OPTYPE_RD_SR;
    }

    /**
     * @return <code>true</code> if this is a set_env
     */
    public boolean isSetEnv() {
        return this.type == InternalOperation.OPTYPE_SET_ENV;
    }

    /**
     * @return <code>true</code> if this is a set
     */
    public boolean isSetR() {
        return this.type == InternalOperation.OPTYPE_SET_R;
    }

    /**
     * @return <code>true</code> if this is a set_s
     */
    public boolean isSetSR() {
        return this.type == InternalOperation.OPTYPE_SET_S_R;
    }

    /**
     * @return <code>true</code> if this is a spawn
     */
    public boolean isSpawnR() {
        return this.type == InternalOperation.OPTYPE_SPAWN_R;
    }

    /**
     * @return <code>true</code> if this is a uin
     */
    public boolean isUinR() {
        return this.type == InternalOperation.OPTYPE_UINR;
    }

    /**
     * @return <code>true</code> if this is a uno
     */
    public boolean isUnoR() {
        return this.type == InternalOperation.OPTYPE_UNOR;
    }

    /**
     * @return <code>true</code> if this is a urd
     */
    public boolean isUrdR() {
        return this.type == InternalOperation.OPTYPE_URDR;
    }

    /**
     * @param t the tuple result of this internal operation
     */
    public void setResult(final LogicTuple t) {
        this.result = t;
    }

    @Override
    public String toString() {
        return this.toTuple().toString();
    }

    /**
     * @return the tuple representation of this internal operation
     */
    public LogicTuple toTuple() {
        LogicTuple t = null;
        if (this.result != null) {
            t = this.getResult();
        } else {
            t = this.getArgument();
        }
        String opName;
        if (this.isSpawnR()) {
            opName = "spawn";
        } else if (this.isOutR()) {
            opName = "out";
        } else if (this.isInR()) {
            opName = "in";
        } else if (this.isRdR()) {
            opName = "rd";
        } else if (this.isNoR()) {
            opName = "no";
        } else if (this.isOutAllR()) {
            opName = "out_all";
        } else if (this.isInAllR()) {
            opName = "in_all";
        } else if (this.isRdAllR()) {
            opName = "rd_all";
        } else if (this.isNoAllR()) {
            opName = "no_all";
        } else if (this.isUrdR()) {
            opName = "urd";
        } else if (this.isUinR()) {
            opName = "uin";
        } else if (this.isUnoR()) {
            opName = "uno";
        } else if (this.isGetR()) {
            opName = "get";
        } else if (this.isSetR()) {
            opName = "set";
        } else if (this.isOutSR()) {
            opName = "out_s";
        } else if (this.isInSR()) {
            opName = "in_s";
        } else if (this.isRdSR()) {
            opName = "rd_s";
        } else if (this.isNoSR()) {
            opName = "no_s";
        } else if (this.isGetSR()) {
            opName = "get_s";
        } else if (this.isSetSR()) {
            opName = "set_s";
        } else if (this.isGetEnv()) {
            return t;
        } else if (this.isSetEnv()) {
            return t;
        } else {
            opName = "unknownOp";
        }
        return LogicTuples.newInstance(opName, TupleArguments.newInstance(t.toTerm()));
    }
}
