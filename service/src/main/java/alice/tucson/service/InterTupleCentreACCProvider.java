package alice.tucson.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import alice.respect.api.ILinkContext;
import alice.tucson.api.TucsonTupleCentreIdDefault;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public class InterTupleCentreACCProvider implements ILinkContext {

    class Executor extends Thread {

        private final TupleCentreIdentifier fromId;
        private InterTupleCentreACC helper;
        private final Map<TupleCentreIdentifier, InterTupleCentreACC> helpers;
        private final AbstractTupleCentreOperation op;
        private final TupleCentreIdentifier toId;

        public Executor(final TupleCentreIdentifier to,
                        final TupleCentreIdentifier from, final AbstractTupleCentreOperation o,
                        final Map<TupleCentreIdentifier, InterTupleCentreACC> helps) {
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
                                new TucsonTupleCentreIdDefault(this.fromId));
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
    private static Map<TupleCentreIdentifier, InterTupleCentreACC> helpList;
    private final TupleCentreIdentifier idTo;

    /**
     *
     * @param id
     *            the identifier of the tuple centre target of the linking
     *            invocation
     */
    public InterTupleCentreACCProvider(
            final TupleCentreIdentifier id) {
        this.idTo = id;
        synchronized (this) {
            if (InterTupleCentreACCProvider.helpList == null) {
                InterTupleCentreACCProvider.helpList = new HashMap<>();
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
    public synchronized void doOperation(final TupleCentreIdentifier id,
            final AbstractTupleCentreOperation op) {
        // id e' il tuplecentre source
        final Executor ex = new Executor(this.idTo, id, op,
                InterTupleCentreACCProvider.helpList);
        InterTupleCentreACCProvider.exec.execute(ex);
    }
}
