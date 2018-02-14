package alice.tucson.service;

import java.util.HashMap;
import java.util.Map;
import alice.respect.api.ILinkContext;
import alice.respect.api.IRemoteLinkProvider;
import alice.tuplecentre.api.TupleCentreId;

/**
 *
 * @author Alessandro Ricci
 *
 */
public class RemoteLinkProvider implements IRemoteLinkProvider {

    // FIXME How to fix this?
    private static Map<String, InterTupleCentreACCProvider> remoteRegistry = new HashMap<String, InterTupleCentreACCProvider>();

    @Override
    public ILinkContext getRemoteLinkContext(final TupleCentreId id) {
        // id e' il tuplecentre target (nome completo xche' toString?)
        InterTupleCentreACCProvider helper = RemoteLinkProvider.remoteRegistry
                .get(id.toString());
        if (helper == null) {
            helper = new InterTupleCentreACCProvider(id);
            RemoteLinkProvider.remoteRegistry.put(id.toString(), helper);
        }
        return helper;
    }
}
