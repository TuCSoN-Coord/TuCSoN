package alice.tucson.service;

import java.util.List;
import alice.logictuple.LogicTuple;
import alice.tuplecentre.api.TupleCentreOpId;

/**
 * Completion of a TuCSoN operation: such event stores the corresponding
 * operation ID, its success state, its result and other useful info.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public class TucsonOpCompletionEvent {

    private final boolean allowed;
    private final TupleCentreOpId opId;
    private final boolean resultSuccess;
    private String spec;
    private final boolean success;
    private LogicTuple tuple;
    private List<LogicTuple> tupleList;

    /**
     *
     * @param id
     *            the identifier of the TuCSoN operation
     * @param a
     *            wether the operation is allowed
     * @param s
     *            wether the operation succeded
     * @param resOk
     *            wether the result operation succeded
     */
    public TucsonOpCompletionEvent(final TupleCentreOpId id, final boolean a,
                                   final boolean s, final boolean resOk) {
        this.opId = id;
        this.allowed = a;
        this.success = s;
        this.resultSuccess = resOk;
    }

    /**
     *
     * @param id
     *            the identifier of the TuCSoN operation
     * @param a
     *            wether the operation is allowed
     * @param s
     *            wether the operation succeded
     * @param resOk
     *            wether the result operation succeded
     * @param tl
     *            the list of tuples result of the oepration
     */
    public TucsonOpCompletionEvent(final TupleCentreOpId id, final boolean a,
                                   final boolean s, final boolean resOk, final List<LogicTuple> tl) {
        this.opId = id;
        this.allowed = a;
        this.success = s;
        this.resultSuccess = resOk;
        this.tupleList = tl;
    }

    /**
     *
     * @param id
     *            the identifier of the TuCSoN operation
     * @param a
     *            wether the operation is allowed
     * @param s
     *            wether the operation succeded
     * @param t
     *            the tuple result of the operation
     *
     * @param resOk
     *            whether the operations already has its result available
     */
    public TucsonOpCompletionEvent(final TupleCentreOpId id, final boolean a,
                                   final boolean s, final boolean resOk, final LogicTuple t) {
        this.opId = id;
        this.allowed = a;
        this.success = s;
        this.resultSuccess = resOk;
        this.tuple = t;
    }

    /**
     *
     * @param id
     *            the identifier of the TuCSoN operation
     * @param a
     *            wether the operation is allowed
     * @param s
     *            wether the operation succeded
     * @param resOk
     *            wether the result operation succeded
     * @param sp
     *            the String representation of the ReSpecT specification used
     */
    public TucsonOpCompletionEvent(final TupleCentreOpId id, final boolean a,
                                   final boolean s, final boolean resOk, final String sp) {
        this.opId = id;
        this.allowed = a;
        this.success = s;
        this.resultSuccess = resOk;
        this.spec = sp;
    }

    /**
     *
     * @return the identifier of the TuCSoN operation
     */
    public TupleCentreOpId getOpId() {
        return this.opId;
    }

    /**
     *
     * @return the ReSpecT specification used in the operation
     */
    public String getSpec() {
        return this.spec;
    }

    /**
     *
     * @return the tuple result of the operation
     */
    public LogicTuple getTuple() {
        return this.tuple;
    }

    /**
     *
     * @return the list of tuples result of the operation
     */
    public List<LogicTuple> getTupleList() {
        return this.tupleList;
    }

    /**
     *
     * @return wether the operation was allowed
     */
    public boolean operationAllowed() {
        return this.allowed;
    }

    /**
     *
     * @return wether the operation succeeded
     */
    public boolean operationSucceeded() {
        return this.success;
    }

    /**
     *
     * @return wether the result operation succeeded
     */
    public boolean resultOperationSucceeded() {
        return this.resultSuccess;
    }
}
