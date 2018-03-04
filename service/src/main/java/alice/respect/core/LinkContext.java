package alice.respect.core;

import alice.respect.api.ILinkContext;
import alice.respect.api.exceptions.OperationNotPossibleException;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

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
