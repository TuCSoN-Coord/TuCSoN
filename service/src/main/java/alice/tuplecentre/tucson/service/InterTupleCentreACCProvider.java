package alice.tuplecentre.tucson.service;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.respect.api.ILinkContext;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class InterTupleCentreACCProvider implements ILinkContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
                                TucsonTupleCentreId.of(this.fromId));
                    } catch (final TucsonInvalidTupleCentreIdException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                    this.helpers.put(this.fromId, this.helper);
                }
            }
            if (this.helper != null) {
                try {
                    this.helper.doOperation(this.toId, this.op);
                } catch (final UnreachableNodeException | TucsonOperationNotPossibleException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    // FIXME How to fix this?
    private static ExecutorService exec;
    private static Map<TupleCentreIdentifier, InterTupleCentreACC> helpList;
    private final TupleCentreIdentifier idTo;

    /**
     * @param id the identifier copyOf the tuple centre target copyOf the linking
     *           invocation
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
