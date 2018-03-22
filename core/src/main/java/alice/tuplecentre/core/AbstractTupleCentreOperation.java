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

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;

import alice.tuple.Tuple;
import alice.tuple.TupleTemplate;
import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.ITCCycleResult;
import alice.tuplecentre.api.OperationIdentifier;
import alice.tuplecentre.api.TupleCentreOperation;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.TucsonOpId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents an Operation on a tuple centre.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public abstract class AbstractTupleCentreOperation implements TupleCentreOperation {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Shared id counter
     */
    private static long idCounter = 0;

    /**
     * internal identifier of the operation
     */
    private final OperationIdentifier id;

    private final TCCycleResult result;
    private TupleTemplate templateArgument;
    private Tuple tupleArgument;
    private List<Tuple> tupleListArgument;
    private final TupleCentreOpType type;

    private OperationCompletionListener listener;

    private boolean operationCompleted;

    /**
     * Used for possible synchronisation
     */
    protected final Object token; //TODO controllare se è usato bene per la sincronizzazione

    private AbstractTupleCentreOperation(final TupleCentreOpType opType) {
        this.operationCompleted = false;
        this.result = new TCCycleResult();
        this.type = opType;
        this.token = new Object();
        this.id = new TucsonOpId(AbstractTupleCentreOperation.idCounter);
        AbstractTupleCentreOperation.idCounter++;
    }

    /**
     * @param opType    the type code of the operation
     * @param tupleList the list of tuples argument of the operation
     */
    protected AbstractTupleCentreOperation(final TupleCentreOpType opType,
                                           final List<Tuple> tupleList) {
        this(opType);
        this.listener = null;
        this.tupleListArgument = tupleList;
    }

    /**
     * @param opType    the type code of the operation
     * @param tupleList the list of tuples argument of the operation
     * @param l         the listener for operation completion
     */
    protected AbstractTupleCentreOperation(final TupleCentreOpType opType,
                                           final List<Tuple> tupleList, final OperationCompletionListener l) {
        this(opType, tupleList);
        this.listener = l;
    }

    /**
     * @param opType the type code of the operation
     * @param t      the tuple argument of the operation
     */
    protected AbstractTupleCentreOperation(final TupleCentreOpType opType, final Tuple t) {
        this(opType);
        this.listener = null;
        this.tupleArgument = t;
    }

    /**
     * @param opType the type code of the operation
     * @param t      the tuple argument of the operation
     * @param l      the listener for operation completion
     */
    protected AbstractTupleCentreOperation(final TupleCentreOpType opType, final Tuple t,
                                           final OperationCompletionListener l) {
        this(opType, t);
        this.listener = l;
    }

    /**
     * @param opType the type code of the operation
     * @param t      the tuple template argument of the operation
     */
    protected AbstractTupleCentreOperation(final TupleCentreOpType opType, final TupleTemplate t) {
        this(opType);
        this.listener = null;
        this.templateArgument = t;
    }

    /**
     * @param opType the type code of the operation
     * @param t      the tuple template argument of the operation
     * @param l      the listener for operation completion
     */
    protected AbstractTupleCentreOperation(final TupleCentreOpType opType, final TupleTemplate t,
                                           final OperationCompletionListener l) {
        this(opType, t);
        this.listener = l;
    }

    @Override
    public OperationIdentifier getId() {
        return this.id;
    }

    @Override
    public Tuple getPredicate() {
        final StringBuilder pred = new StringBuilder();
        try {
            if (TupleCentreOpType.getProducerPrimitives().contains(this.type)) {
                pred.append(this.getPrimitive().toString()).append('(')
                        .append(this.tupleArgument).append(')');
                return LogicTuples.parse(pred.toString());
            }
            pred.append(this.getPrimitive().toString()).append('(')
                    .append(this.templateArgument).append(')');
            return LogicTuples.parse(pred.toString());
        } catch (final InvalidLogicTupleException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }


    @Override
    public Tuple getPrimitive() {
        if (TupleCentreOpType.getStandardOperationTypes().contains(this.type)) {


            return LogicTuples.newInstance(this.type.name().toLowerCase());
        } else {
            return null;
        }
    }

    @Override
    public TupleTemplate getTemplateArgument() {
        return this.templateArgument;
    }

    @Override
    public Tuple getTupleArgument() {
        return this.tupleArgument;
    }

    @Override
    public List<Tuple> getTupleListArgument() {
        return this.tupleListArgument;
    }

    @Override
    public List<Tuple> getTupleListResult() {
        return this.result.getTupleListResult();
    }

    @Override
    public Tuple getTupleResult() {
        return this.result.getTupleResult();
    }

    @Override
    public TupleCentreOpType getType() {
        return this.type;
    }

    // TODO metodo inessenziale... favorisce i clienti della classe, perchè non devono fare "instanceof" e downcasting
    @Override
    public LogicTuple getLogicTupleResult() {
        return (LogicTuple) this.getTupleResult();
    }

    // TODO metodo inessenziale... favorisce i clienti della classe, perchè non devono fare "instanceof" e downcasting
    @Override
    public List<LogicTuple> getLogicTupleListResult() {
        final List<LogicTuple> toReturn = new LinkedList<>();

        final List<Tuple> tupleList = this.getTupleListResult();
        if (tupleList != null && !tupleList.isEmpty()) {
            for (final Tuple t : tupleList) {
                toReturn.add((LogicTuple) t);
            }
        }
        return toReturn;
    }


    @Override
    public boolean isResultDefined() {
        return this.result.isResultDefined();
    }

    @Override
    public boolean isResultFailure() {
        return this.result.isResultFailure();
    }

    @Override
    public boolean isResultSuccess() {
        return this.result.isResultSuccess();
    }

    @Override
    public void setCompletionListener(final OperationCompletionListener lis) {
        this.listener = lis;
    }

    @Override
    public OperationCompletionListener getCompletionListener() {
        return listener;
    }

    @Override
    public boolean isOperationCompleted() {
        return this.operationCompleted;
    }

    @Override
    public void removeCompletionListener() {
        this.listener = null;
    }

    @Override
    public void setOpResult(final ITCCycleResult.Outcome o) {
        this.result.setOpResult(o);
    }

    @Override
    public void setTupleListResult(final List<? extends Tuple> t) {
        this.result.setTupleListResult(t);
        this.result.setEndTime(System.currentTimeMillis());
    }

    @Override
    public void setTupleResult(final Tuple t) {
        this.result.setTupleResult(t);
        if (this.templateArgument != null) {
            this.templateArgument.propagate(t);
        }
        this.tupleArgument = t;
        this.result.setEndTime(System.currentTimeMillis());
    }

    @Override
    public void waitForOperationCompletion() {
        try {
            synchronized (this.token) {
                while (!this.operationCompleted) {
                    this.token.wait();
                }
            }
        } catch (final InterruptedException e) {
            // do nothing here, ususally happens when shutting down nodes
        }
    }

    @Override
    public LogicTuple getLogicTupleArgument() {
        if (TupleCentreOpType.getProducerPrimitives().contains(this.type)) {
            return (LogicTuple) this.getTupleArgument();
        }
        return (LogicTuple) this.getTemplateArgument();
    }

    @Override
    public void waitForOperationCompletion(final long ms)
            throws OperationTimeOutException {
        synchronized (this.token) {
            if (!this.operationCompleted) {
                try {
                    this.token.wait(ms);
                } catch (final InterruptedException e) {
                    // do nothing here, usually happens when shutting down
                    // nodes
                }
            }
            if (!this.operationCompleted) {
                throw new OperationTimeOutException(ms);
            }
        }
    }

    @Override
    public void notifyCompletion() {
        if (this.listener != null) {
            this.operationCompleted = true;
            this.listener.operationCompleted(this);
        } else {
            synchronized (this.token) {
                this.operationCompleted = true;
                this.token.notifyAll();
            }
        }
    }
}
