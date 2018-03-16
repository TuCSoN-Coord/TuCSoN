/*
 * Tuple Centre media - Copyright (C) 2001-2002 aliCE team at deis.unibo.it This
 * library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with this library; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package alice.tuplecentre.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import alice.tuple.Tuple;
import alice.tuplecentre.api.ITCCycleResult;
import alice.tuplecentre.api.exceptions.InvalidCoordinationOperationException;
import alice.tuplecentre.respect.core.RespectVMContext;


/**
 * This is the speaking state of the TCVM
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class SpeakingState extends AbstractTupleCentreVMState {

    private AbstractTupleCentreVMState idleState;
    private boolean noMoreSatisfiablePendingQuery;
    private AbstractTupleCentreVMState reactingState;

    /**
     *
     * @param tcvm
     *            the tuple centre VM this state belongs to
     */
    public SpeakingState(final AbstractTupleCentreVMContext tcvm) {
        super(tcvm);
    }

    @Override
    public void execute() {
        if (super.vm.isStepMode()) {
            this.log();
        }
        final Iterator<?> it = this.vm.getPendingQuerySetIterator();
        InputEvent ev = null;
        OutputEvent outEv;
        this.noMoreSatisfiablePendingQuery = true;
        boolean foundSatisfied = false;
        Tuple tuple;
        List<Tuple> tupleList;
        AbstractTupleCentreOperation op = null;
        while (it.hasNext() && !foundSatisfied) {
            try {
                ev = (InputEvent) it.next();
                op = ev.getSimpleTCEvent();
                if (op.isResultDefined() || ev.isLinking()) {
                    foundSatisfied = true;
                } else {
                    switch (op.getType()) {
                        case SPAWN:
                            tuple = op.getTupleArgument();
                            if (this.vm.spawnActivity(tuple, ev.getSource(),
                                    ev.getTarget())) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                            } else {
                                op.setOpResult(ITCCycleResult.Outcome.FAILURE);
                            }
                            op.setTupleResult(tuple);
                            foundSatisfied = true;
                            break;
                        case OUT:
                            tuple = op.getTupleArgument();
                            this.vm.addTuple(tuple, true);
                            op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                            op.setTupleResult(tuple);
                            foundSatisfied = true;
                            break;
                        case IN:
                            tuple = this.vm.removeMatchingTuple(
                                    op.getTemplateArgument(), true);
                            if (tuple != null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(tuple);
                                foundSatisfied = true;
                            } // we do nothing: in is suspensive hence we cannot
                            // conclude FAILURE yet!
                            break;
                        case RD:
                            tuple = this.vm.readMatchingTuple(op
                                    .getTemplateArgument());
                            if (tuple != null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(tuple);
                                foundSatisfied = true;
                            } // we do nothing: rd is suspensive hence we cannot
                            // conclude FAILURE yet!
                            break;
                        case INP:
                            tuple = this.vm.removeMatchingTuple(
                                    op.getTemplateArgument(), true);
                            if (tuple != null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(tuple);
                            } else {
                                op.setOpResult(ITCCycleResult.Outcome.FAILURE);
                                op.setTupleResult(op.getTemplateArgument());
                            }
                            foundSatisfied = true;
                            break;
                        case RDP:
                            tuple = this.vm.readMatchingTuple(op
                                    .getTemplateArgument());
                            if (tuple != null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(tuple);
                            } else {
                                op.setOpResult(ITCCycleResult.Outcome.FAILURE);
                                op.setTupleResult(op.getTemplateArgument());
                            }
                            foundSatisfied = true;
                            break;
                        case NO:
                            tuple = this.vm.readMatchingTuple(op
                                    .getTemplateArgument());
                            if (tuple == null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(op.getTemplateArgument());
                                foundSatisfied = true;
                            } // we do nothing: no is suspensive hence we cannot
                            // conclude FAILURE yet!
                            break;
                        case NOP:
                            tuple = this.vm.readMatchingTuple(op
                                    .getTemplateArgument());
                            if (tuple == null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(op.getTemplateArgument());
                            } else {
                                op.setOpResult(ITCCycleResult.Outcome.FAILURE);
                                op.setTupleResult(tuple);
                            }
                            foundSatisfied = true;
                            break;
                        case GET:
                            tupleList = this.vm.getAllTuples();
                            op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                            op.setTupleListResult(tupleList);
                            foundSatisfied = true;
                            break;
                        case SET:
                            tupleList = op.getTupleListArgument();
                            this.vm.setAllTuples(tupleList);
                            op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                            op.setTupleListResult(tupleList);
                            foundSatisfied = true;
                            break;
                        case OUT_ALL:
                            tuple = op.getTupleArgument();
                            final List<Tuple> list = this.vm.addListTuple(tuple);
                            op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                            op.setTupleListResult(list);
                            foundSatisfied = true;
                            break;
                        case IN_ALL: {
                            List<Tuple> tuples;
                            tuples = this.vm.inAllTuples(op.getTemplateArgument());
                            op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                            op.setTupleListResult(tuples);
                            foundSatisfied = true;
                            break;
                        }
                        case RD_ALL: {
                            List<Tuple> tuples;
                            tuples = this.vm
                                    .readAllTuples(op.getTemplateArgument());
                            op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                            op.setTupleListResult(tuples);
                            foundSatisfied = true;
                            break;
                        }
                        case NO_ALL: {
                            List<Tuple> tuples;
                            tuples = this.vm
                                    .readAllTuples(op.getTemplateArgument());
                            if (tuples == null || tuples.isEmpty()) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                            } else {
                                op.setOpResult(ITCCycleResult.Outcome.FAILURE);
                            }
                            op.setTupleListResult(tuples);
                            foundSatisfied = true;
                            break;
                        }
                        case URD:
                            tuple = this.vm.readUniformTuple(op
                                    .getTemplateArgument());
                            if (tuple != null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(tuple);
                                foundSatisfied = true;
                            } // we do nothing: urd is suspensive hence we cannot
                            // conclude FAILURE yet!
                            break;
                        case UIN:
                            tuple = this.vm.removeUniformTuple(op
                                    .getTemplateArgument());
                            if (tuple != null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(tuple);
                                foundSatisfied = true;
                            } // we do nothing: uin is suspensive hence we cannot
                            // conclude FAILURE yet!
                            break;
                        case UNO:
                            tuple = this.vm.readUniformTuple(op
                                    .getTemplateArgument());
                            if (tuple == null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(op.getTemplateArgument());
                                foundSatisfied = true;
                            } // we do nothing: urd is suspensive hence we cannot
                            // conclude FAILURE yet!
                            break;
                        case URDP:
                            tuple = this.vm.readUniformTuple(op
                                    .getTemplateArgument());
                            if (tuple != null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(tuple);
                            } else {
                                op.setOpResult(ITCCycleResult.Outcome.FAILURE);
                                op.setTupleResult(op.getTemplateArgument());
                            }
                            foundSatisfied = true;
                            break;
                        case UINP:
                            tuple = this.vm.removeUniformTuple(op
                                    .getTemplateArgument());
                            if (tuple != null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(tuple);
                            } else {
                                op.setOpResult(ITCCycleResult.Outcome.FAILURE);
                                op.setTupleResult(op.getTemplateArgument());
                            }
                            foundSatisfied = true;
                            break;
                        case UNOP:
                            tuple = this.vm.readUniformTuple(op
                                    .getTemplateArgument());
                            if (tuple == null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(op.getTemplateArgument());
                            } else {
                                op.setOpResult(ITCCycleResult.Outcome.FAILURE);
                                op.setTupleResult(tuple);
                            }
                            foundSatisfied = true;
                            break;
                        case OUT_S:
                            tuple = op.getTupleArgument();
                            this.vm.addSpecTuple(tuple);
                            op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                            op.setTupleResult(tuple);
                            foundSatisfied = true;
                            break;
                        case IN_S:
                            tuple = this.vm.removeMatchingSpecTuple(op
                                    .getTemplateArgument());
                            if (tuple != null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(tuple);
                                foundSatisfied = true;
                            } // we do nothing: in_s is suspensive hence we cannot
                            // conclude FAILURE yet!
                            break;
                        case RD_S:
                            tuple = this.vm.readMatchingSpecTuple(op
                                    .getTemplateArgument());
                            if (tuple != null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(tuple);
                                foundSatisfied = true;
                            } // we do nothing: rd_s is suspensive hence we cannot
                            // conclude FAILURE yet!
                            break;
                        case INP_S:
                            tuple = this.vm.removeMatchingSpecTuple(op
                                    .getTemplateArgument());
                            if (tuple != null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(tuple);
                            } else {
                                op.setOpResult(ITCCycleResult.Outcome.FAILURE);
                                op.setTupleResult(op.getTemplateArgument());
                            }
                            foundSatisfied = true;
                            break;
                        case RDP_S:
                            tuple = this.vm.readMatchingSpecTuple(op
                                    .getTemplateArgument());
                            if (tuple != null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(tuple);
                            } else {
                                op.setOpResult(ITCCycleResult.Outcome.FAILURE);
                                op.setTupleResult(op.getTemplateArgument());
                            }
                            foundSatisfied = true;
                            break;
                        case NO_S:
                            tuple = this.vm.readMatchingSpecTuple(op
                                    .getTemplateArgument());
                            if (tuple == null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(op.getTemplateArgument());
                                foundSatisfied = true;
                            } // we do nothing: no_s is suspensive hence we cannot
                            // conclude FAILURE yet!
                            break;
                        case NOP_S:
                            tuple = this.vm.readMatchingSpecTuple(op
                                    .getTemplateArgument());
                            if (tuple == null) {
                                op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                                op.setTupleResult(op.getTemplateArgument());
                            } else {
                                op.setOpResult(ITCCycleResult.Outcome.FAILURE);
                                op.setTupleResult(tuple);
                            }
                            foundSatisfied = true;
                            break;
                        case GET_S:
                            final Iterator<? extends Tuple> rit = this.vm
                                    .getSpecTupleSetIterator();
                            final LinkedList<Tuple> reactionList = new LinkedList<>();
                            while (rit.hasNext()) {
                                reactionList.add(rit.next());
                            }
                            final Iterator<? extends Tuple> pit = ((RespectVMContext) this.vm)
                                    .getPrologPredicatesIterator();
                            while (pit.hasNext()) {
                                reactionList.add(pit.next());
                            }
                            op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                            op.setTupleListResult(reactionList);
                            foundSatisfied = true;
                            break;
                        case SET_S:
                            tupleList = op.getTupleListArgument();
                            this.vm.setAllSpecTuples(tupleList);
                            op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                            op.setTupleListResult(tupleList);
                            foundSatisfied = true;
                            break;
                        case TIME:
                            op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                            op.setTupleResult(op.getTemplateArgument());
                            foundSatisfied = true;
                            outEv = new OutputEvent(ev);
                            this.vm.fetchTimedReactions(outEv);
                            break;
                        case GET_ENV:
                            tuple = op.getTupleArgument();
                            op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                            op.setTupleResult(tuple);
                            foundSatisfied = true;
                            break;
                        case SET_ENV:
                            tuple = op.getTupleArgument();
                            op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                            op.setTupleResult(tuple);
                            foundSatisfied = true;
                            break;
                        default:
                            throw new InvalidCoordinationOperationException(
                                    "The coordination operation requested does not exist. "
                                            + "Operation id: " + op.getId()
                                            + ", Operation type: " + op.getType());
                    }
                    if (((RespectVMContext) this.vm).getRespectVM()
                            .getObservers().size() > 0) {
                        ((RespectVMContext) this.vm).getRespectVM()
                                .notifyObservableEvent(ev);
                    }
                }
            } catch (final InvalidCoordinationOperationException ex) {
                this.vm.notifyException(ex);
                it.remove();
            }
        }
        if (foundSatisfied) {
            outEv = new OutputEvent(ev);
            if (!ev.isLinking()) {
                this.vm.notifyOutputEvent(outEv);
                if (((RespectVMContext) this.vm).getRespectVM().getObservers()
                        .size() > 0) {
                    ((RespectVMContext) this.vm).getRespectVM()
                            .notifyObservableEvent(outEv);
                }
            }
            if (ev.isLinking() && !op.isResultDefined()) {
                outEv.setTarget(ev.getTarget());
                this.vm.linkOperation(outEv);
            }
            this.vm.fetchTriggeredReactions(outEv);
            this.noMoreSatisfiablePendingQuery = false;
            it.remove();
        }
    }

    @Override
    public AbstractTupleCentreVMState getNextState() {
        if (this.vm.triggeredReaction()) {
            return this.reactingState;
        } else if (this.noMoreSatisfiablePendingQuery) {
            return this.idleState;
        } else {
            return this;
        }
    }

    /*
     * (non-Javadoc)
     * @see alice.tuplecentre.build.TupleCentreVMState#isIdle()
     */
    @Override
    public boolean isIdle() {
        return false;
    }

    @Override
    public void resolveLinks() {
        this.reactingState = this.vm.getState("ReactingState");
        this.idleState = this.vm.getState("IdleState");
    }
}
