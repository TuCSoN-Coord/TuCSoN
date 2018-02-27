package alice.tucson.service;

import java.util.ArrayList;
import java.util.List;

import alice.logictuple.LogicTuple;
import alice.tucson.api.TucsonOperation;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;
import alice.tuplecentre.api.exceptions.InvalidTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.OperationCompletionListener;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuples.javatuples.impl.JTuplesEngine;

/**
 * Class implementing a Default TucsonOperation
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @see TucsonOperation
 */
public class TucsonOperationDefault extends AbstractTupleCentreOperation implements TucsonOperation {

    private OperationHandler context;

    /**
     * @param type the type code of the operation
     * @param t    the tuple argument of the operation
     * @param l    the listener for operation completion
     * @param ctx  the ACC requesting the operation
     */
    public TucsonOperationDefault(final TupleCentreOpType type, final Tuple t,
                                  final OperationCompletionListener l, final OperationHandler ctx) {
        super(type, t, l);
        this.context = ctx;
    }

    /**
     * @param type the type code of the operation
     * @param t    the tuple template argument of the operation
     * @param l    the listener for operation completion
     * @param ctx  the ACC requesting the operation
     */
    public TucsonOperationDefault(final TupleCentreOpType type, final TupleTemplate t,
                                  final OperationCompletionListener l, final OperationHandler ctx) {
        super(type, t, l);
        this.context = ctx;
    }


    @Override
    public Tuple getJTupleArgument() {
        return logicTupleToJavaTuple(this.getLogicTupleArgument());
    }

    @Override
    public Tuple getJTupleResult() {
        return logicTupleToJavaTuple(this.getLogicTupleArgument());
    }

    /**
     * Transforms a LogicTuple to a JavaTuple
     *
     * @param lt logicTuple to transform
     * @return the Tuple resulting from transformation
     */
    private Tuple logicTupleToJavaTuple(final LogicTuple lt) {
        try {
            if (JTuplesEngine.isTemplate(lt)) {
                return JTuplesEngine.toJavaTupleTemplate(lt);
            }
            return JTuplesEngine.toJavaTuple(lt);
        } catch (final InvalidTupleException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Tuple> getJTupleListResult() {
        final List<LogicTuple> lts = this.getLogicTupleListResult();
        final List<Tuple> jts = new ArrayList<>(lts.size());
        try {
            for (final LogicTuple t : lts) {
                if (JTuplesEngine.isTemplate(t)) {
                    jts.add(JTuplesEngine.toJavaTupleTemplate(t));
                } else {
                    jts.add(JTuplesEngine.toJavaTuple(t));
                }
            }
        } catch (final InvalidTupleException e) {
            e.printStackTrace();
            return null;
        }
        return jts;
    }

    @Override
    public void waitForOperationCompletion(final long ms)
            throws OperationTimeOutException {
        synchronized (this.token) {
            if (!this.isOperationCompleted()) {
                try {
                    this.token.wait(ms);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!this.isOperationCompleted()) {
                this.context.addOperationExpired(this.getId());
                throw new OperationTimeOutException();
            }
        }
    }
}
