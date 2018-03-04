package alice.respect.api;

import alice.tuplecentre.api.TupleCentreIdentifier;

/**
 *
 * @author Alessandro Ricci
 *
 */
public interface IRemoteLinkProvider {

    /**
     *
     * @param id
     *            the target tuple centre to link with
     * @return the linking context toward the target tuple centre
     */
    ILinkContext getRemoteLinkContext(TupleCentreIdentifier id);
}
