package alice.tuplecentre.respect.core;

import alice.logictuple.LogicTuple;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;
import alice.tuplecentre.respect.api.IRespectTC;

/**
 * A new abstract class for the context
 *
 * @author Unknown...
 *
 */
public class RootInterface {

    private final IRespectTC core;

    /**
     *
     * @param rCore
     *            the ReSpecT tuple centres manager this interface refers to
     */
    public RootInterface(final IRespectTC rCore) {
        this.core = rCore;
    }

    /**
     *
     * @return the ReSpecT tuple centres manager this interface refers to
     */
    protected IRespectTC getCore() {
        return this.core;
    }

    /**
     *
     * @param template
     *            the tuple template to unify
     * @param tuple
     *            the tuple to unify
     * @return the tuple result of the unification process
     */
    protected LogicTuple unify(final TupleTemplate template, final Tuple tuple) {
        final boolean res = template.propagate(tuple);
        if (res) {
            return (LogicTuple) template;
        }
        return null;
    }
}
