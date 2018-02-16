package alice.tuplecentre.core;

import java.util.List;
import alice.tuplecentre.api.ITCCycleResult;
import alice.tuplecentre.api.Tuple;

/**
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public class TCCycleResult implements ITCCycleResult {


    private long endTime;
    private Outcome opResult;
    private final long startTime;
    private List<Tuple> tupleListResult;
    private Tuple tupleResult;

    /**
     *
     */
    public TCCycleResult() {
        this.opResult = Outcome.UNDEFINED;
        this.tupleResult = null;
        this.tupleListResult = null;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public long getEndTime() {
        return this.endTime;
    }

    @Override
    public Outcome getOpResult() {
        return this.opResult;
    }

    @Override
    public long getStartTime() {
        return this.startTime;
    }

    @Override
    public List<Tuple> getTupleListResult() {
        return this.tupleListResult;
    }

    @Override
    public Tuple getTupleResult() {
        return this.tupleResult;
    }

    @Override
    public boolean isResultDefined() {
        return this.opResult != Outcome.UNDEFINED;
    }

    @Override
    public boolean isResultFailure() {
        return this.opResult == Outcome.FAILURE;
    }

    @Override
    public boolean isResultSuccess() {
        return this.opResult == Outcome.SUCCESS;
    }

    @Override
    public void setEndTime(final long time) {
        this.endTime = time;
    }

    @Override
    public void setOpResult(final Outcome o) {
        this.opResult = o;
    }

    @Override
    public void setTupleListResult(final List<Tuple> res) {
        this.tupleListResult = res;
    }

    @Override
    public void setTupleResult(final Tuple res) {
        this.tupleResult = res;
    }

}
