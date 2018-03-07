package alice.tuplecentre.respect.core;

import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.respect.api.ILinkContext;
import alice.tuplecentre.respect.api.exceptions.OperationNotPossibleException;

/**
 *
 * @author ste (mailto: s.mariani@unibo.it)
 *
 */
public class LinkContext implements ILinkContext {

    private final RespectVM vm;

    /**
     *
     * @param rvm
     *            the ReSpecT VM this context refers to
     */
    public LinkContext(final RespectVM rvm) {
        this.vm = rvm;
    }

    @Override
    public void doOperation(final TupleCentreIdentifier id,
            final AbstractTupleCentreOperation op)
            throws OperationNotPossibleException {
        this.vm.doOperation(id, (RespectOperationDefault) op);
    }
}
