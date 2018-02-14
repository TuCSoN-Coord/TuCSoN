package alice.tucson.persistency;

import java.util.List;
import alice.respect.core.TupleSet;
import alice.respect.core.tupleset.ITupleSet;

/**
 * @author Lorenzo Pontellini, Vincenzo Scafuto
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public class PersistencyData {

    private List<String> predicates;
    private TupleSet prologPredicates;
    private List<String> specTuples;
    private List<String> tuples;
    private ITupleSet tupleSet;
    private ITupleSet tupleSpecSet;
    private List<String> updates;

    public PersistencyData() {
    }

    public PersistencyData(final ITupleSet tSet, final ITupleSet tSpecSet,
            final TupleSet pPrologPred, final List<String> tUpdates) {
        this.setTupleSet(tSet);
        this.setTupleSpecSet(tSpecSet);
        this.setPrologPredicates(pPrologPred);
        this.setUpdates(tUpdates);
    }

    public PersistencyData(final List<String> ts, final List<String> sts,
            final List<String> pps, final List<String> us) {
        this.setTuples(ts);
        this.setSpecTuples(sts);
        this.setPredicates(pps);
        this.setUpdates(us);
    }

    public List<String> getPredicates() {
        return this.predicates;
    }

    public TupleSet getPrologPredicates() {
        return this.prologPredicates;
    }

    public List<String> getSpecTuples() {
        return this.specTuples;
    }

    public List<String> getTuples() {
        return this.tuples;
    }

    public ITupleSet getTupleSet() {
        return this.tupleSet;
    }

    public ITupleSet getTupleSpecSet() {
        return this.tupleSpecSet;
    }

    public List<String> getUpdates() {
        return this.updates;
    }

    public void setPredicates(final List<String> pPredicates) {
        this.predicates = pPredicates;
    }

    public void setPrologPredicates(final TupleSet pps) {
        this.prologPredicates = pps;
    }

    public void setSpecTuples(final List<String> sts) {
        this.specTuples = sts;
    }

    public void setTuples(final List<String> ts) {
        this.tuples = ts;
    }

    public void setTupleSet(final ITupleSet set) {
        this.tupleSet = set;
    }

    public void setTupleSpecSet(final ITupleSet sSet) {
        this.tupleSpecSet = sSet;
    }

    public void setUpdates(final List<String> us) {
        this.updates = us;
    }
}
