package alice.tucson.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import alice.respect.api.ILinkContext;
import alice.respect.api.TupleCentreId;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public class InterTupleCentreACCProvider implements ILinkContext {

    class Executor extends Thread {

        private final TupleCentreId fromId;
        private InterTupleCentreACC helper;
        private final Map<TupleCentreId, InterTupleCentreACC> helpers;
        private final AbstractTupleCentreOperation op;
        private final alice.tuplecentre.api.TupleCentreId toId;

        public Executor(final alice.tuplecentre.api.TupleCentreId to,
                final TupleCentreId from, final AbstractTupleCentreOperation o,
                final Map<TupleCentreId, InterTupleCentreACC> helps) {
            super();
            this.toId = to;
            this.fromId = from;
            this.op = o;
            this.helpers = helps;
        }

        @Override
        public void run() {
            if (this.helpers != null) {
                this.helper = this.helpers.get(this.fromId);
                if (this.helper == null) {
                    try {
                        this.helper = new InterTupleCentreACCProxy(
                                new TucsonTupleCentreId(this.fromId));
                    } catch (final TucsonInvalidTupleCentreIdException e) {
                        e.printStackTrace();
                    }
                    if (this.helpers != null) {
                        this.helpers.put(this.fromId, this.helper);
                    }
                }
            }
            if (this.helper != null) {
                try {
                    this.helper.doOperation(this.toId, this.op);
                } catch (final TucsonInvalidTupleCentreIdException e) {
                    e.printStackTrace();
                } catch (final TucsonOperationNotPossibleException e) {
                    e.printStackTrace();
                } catch (final UnreachableNodeException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // FIXME How to fix this?
    private static ExecutorService exec;
    private static Map<TupleCentreId, InterTupleCentreACC> helpList;
    private final alice.tuplecentre.api.TupleCentreId idTo;

    /**
     *
     * @param id
     *            the identifier of the tuple centre target of the linking
     *            invocation
     */
    public InterTupleCentreACCProvider(
            final alice.tuplecentre.api.TupleCentreId id) {
        this.idTo = id;
        synchronized (this) {
            if (InterTupleCentreACCProvider.helpList == null) {
                InterTupleCentreACCProvider.helpList = new HashMap<TupleCentreId, InterTupleCentreACC>();
            }
        }
        synchronized (this) {
            if (InterTupleCentreACCProvider.exec == null) {
                InterTupleCentreACCProvider.exec = Executors
                        .newCachedThreadPool();
            }
        }
    }

    @Override
    public synchronized void doOperation(final TupleCentreId id,
            final AbstractTupleCentreOperation op) {
        // id e' il tuplecentre source
        final Executor ex = new Executor(this.idTo, id, op,
                InterTupleCentreACCProvider.helpList);
        InterTupleCentreACCProvider.exec.execute(ex);
    }
}
