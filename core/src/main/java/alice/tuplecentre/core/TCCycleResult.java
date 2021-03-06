package alice.tuplecentre.core;

import java.util.ArrayList;
import java.util.List;

import alice.tuple.Tuple;
import alice.tuplecentre.api.ITCCycleResult;

/**
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class TCCycleResult implements ITCCycleResult {


    private long endTime;
    private Outcome opResult;
    private final long startTime;
    private List<? extends Tuple> tupleListResult;
    private Tuple tupleResult;

    /**
     *
     */
    public TCCycleResult() {
        this.opResult = Outcome.UNDEFINED;
        this.tupleResult = null;
        this.tupleListResult = new ArrayList<>();
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
        return new ArrayList<>(this.tupleListResult);
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
    public void setTupleListResult(final List<? extends Tuple> res) {
        this.tupleListResult = res;
    }

    @Override
    public void setTupleResult(final Tuple res) {
        this.tupleResult = res;
    }

}
