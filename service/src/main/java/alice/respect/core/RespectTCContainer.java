package alice.respect.core;

import java.net.InetAddress;
import java.net.UnknownHostException;

import alice.respect.api.IEnvironmentContext;
import alice.respect.api.ILinkContext;
import alice.respect.api.IManagementContext;
import alice.respect.api.IOrdinaryAsynchInterface;
import alice.respect.api.IOrdinarySynchInterface;
import alice.respect.api.IRemoteLinkProvider;
import alice.respect.api.ISpatialContext;
import alice.respect.api.ISpecificationAsynchInterface;
import alice.respect.api.ISpecificationSynchInterface;
import alice.respect.api.ITCRegistry;
import alice.respect.api.exceptions.InstantiationNotPossibleException;
import alice.tucson.service.RemoteLinkProvider;
import alice.tuplecentre.api.TupleCentreIdentifier;

/**
 * A Container for ReSpecT tuple centres
 *
 * @author Matteo Casadei
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public final class RespectTCContainer {

    private static RespectTCContainer container;
    private static int defaultport;
    private static final int QUEUE_SIZE = 1000;

    /**
     *
     * @return ReSpecT default listening port
     */
    public static int getDefPort() {
        return RespectTCContainer.defaultport;
    }

    /**
     *
     * @return the ReSpecT container used for local tuple centres management
     */
    public static RespectTCContainer getRespectTCContainer() {
        if (RespectTCContainer.container == null) {
            RespectTCContainer.container = new RespectTCContainer();
        }
        return RespectTCContainer.container;
    }

    /**
     *
     * @param port
     *            ReSpecT default listening port
     */
    public static void setDefPort(final int port) {
        RespectTCContainer.defaultport = port;
    }

    private String hostname;
    private String loopback;
    private final ITCRegistry registry;
    private IRemoteLinkProvider stub;

    private RespectTCContainer() {
        this.registry = new RespectLocalRegistry();
        this.stub = null;
        try {
            this.loopback = InetAddress.getLocalHost().getHostAddress()
                    .toString();
            this.hostname = InetAddress.getLocalHost().getHostName().toString();
        } catch (final UnknownHostException e) {
            e.printStackTrace();
            this.loopback = null;
            this.hostname = null;
        }
    }

    /**
     *
     * @param s
     *            the entity responsible of providing linking context to
     *            requestors
     */
    public void addStub(final IRemoteLinkProvider s) {
        if (s == null) {
            this.stub = s;
        }
    }

    /**
     *
     * @param id
     *            the identifier of the tuple centre to create
     * @param q
     *            its maximum queue size
     * @return a reference to the ReSpecT tuple centre created
     */
    public RespectTC createRespectTC(final TupleCentreIdentifier id, final Integer q) {
        final RespectTC rtc = new RespectTC(id, this, q);
        this.registry.addTC(rtc);
        return rtc;
    }

    /**
     *
     * @param id
     *            the identifier of the TuCSoN tuple centre whose environmental
     *            context should be acquired
     * @return the environmental context acquired
     */
    public IEnvironmentContext getEnvironmentContext(final TupleCentreIdentifier id) {
        try {
            return ((RespectTC) this.registry.getTC(id))
                    .getEnvironmentContext();
        } catch (final InstantiationNotPossibleException e) {
            final RespectTC tc = new RespectTC(id, this,
                    RespectTCContainer.QUEUE_SIZE);
            this.registry.addTC(tc);
            return tc.getEnvironmentContext();
        }
    }

    /**
     * Return a LinkContext for remote/local call
     *
     * @param id
     *            the identifier of the tuple centre target (local or remote)
     * @return the linking context toward the given tuple centre
     */
    public ILinkContext getLinkContext(final TupleCentreIdentifier id) {
        if ((this.hostname.equals(id.getNode()) || this.loopback.equals(id
                .getNode())) && id.getPort() == RespectTCContainer.defaultport) {
            try {
                return ((RespectTC) this.registry.getTC(id)).getLinkContext();
            } catch (final InstantiationNotPossibleException e) {
                final RespectTC tc = new RespectTC(id, this,
                        RespectTCContainer.QUEUE_SIZE);
                this.registry.addTC(tc);
                return tc.getLinkContext();
            }
        }
        if (this.stub == null) {
            this.stub = new RemoteLinkProvider();
        }
        return this.stub.getRemoteLinkContext(id);
    }

    /**
     *
     * @param id
     *            the identifier of the tuple centre target (local or remote)
     * @return the management context toward the given tuple centre
     */
    public IManagementContext getManagementContext(final TupleCentreIdentifier id) {
        try {
            return ((RespectTC) this.registry.getTC(id)).getManagementContext();
        } catch (final InstantiationNotPossibleException e) {
            final RespectTC tc = new RespectTC(id, this,
                    RespectTCContainer.QUEUE_SIZE);
            this.registry.addTC(tc);
            return tc.getManagementContext();
        }
    }

    /**
     *
     * @param id
     *            the identifier of the tuple centre target (local or remote)
     * @return the ordinary, asynchronous context toward the given tuple centre
     */
    public IOrdinaryAsynchInterface getOrdinaryAsynchInterface(
            final TupleCentreIdentifier id) {
        try {
            return ((RespectTC) this.registry.getTC(id))
                    .getOrdinaryAsynchInterface();
        } catch (final InstantiationNotPossibleException e) {
            final RespectTC tc = new RespectTC(id, this,
                    RespectTCContainer.QUEUE_SIZE);
            this.registry.addTC(tc);
            return tc.getOrdinaryAsynchInterface();
        }
    }

    /**
     *
     * @param id
     *            the identifier of the tuple centre target (local or remote)
     * @return the ordinary, synchronous context toward the given tuple centre
     */
    public IOrdinarySynchInterface getOrdinarySynchInterface(
            final TupleCentreIdentifier id) {
        try {
            return ((RespectTC) this.registry.getTC(id))
                    .getOrdinarySynchInterface();
        } catch (final InstantiationNotPossibleException e) {
            final RespectTC tc = new RespectTC(id, this,
                    RespectTCContainer.QUEUE_SIZE);
            this.registry.addTC(tc);
            return tc.getOrdinarySynchInterface();
        }
    }

    /**
     *
     * @return the registry of local ReSpecT tuple centres
     */
    public ITCRegistry getRegistry() {
        return this.registry;
    }
    
    /**
     * 
     * @param id
     *            the id of the tuple centre whose Spatial Context should be
     *            retrieved
     * @return the Spatial Context retrieved
     */
    public ISpatialContext getSpatialContext(final TupleCentreIdentifier id) {
        try {
            return ((RespectTC) this.registry.getTC(id)).getSpatialContext();
        } catch (final InstantiationNotPossibleException e) {
            final RespectTC tc = new RespectTC(id, this,
                    RespectTCContainer.QUEUE_SIZE);
            this.registry.addTC(tc);
            return tc.getSpatialContext();
        }
    }

    /**
     *
     * @param id
     *            the identifier of the tuple centre target (local or remote)
     * @return the specification, asynchronous context toward the given tuple
     *         centre
     */
    public ISpecificationAsynchInterface getSpecificationAsynchInterface(
            final TupleCentreIdentifier id) {
        try {
            return ((RespectTC) this.registry.getTC(id))
                    .getSpecificationAsynchInterface();
        } catch (final InstantiationNotPossibleException e) {
            final RespectTC tc = new RespectTC(id, this,
                    RespectTCContainer.QUEUE_SIZE);
            this.registry.addTC(tc);
            return tc.getSpecificationAsynchInterface();
        }
    }

    /**
     *
     * @param id
     *            the identifier of the tuple centre target (local or remote)
     * @return the specification, synchronous context toward the given tuple
     *         centre
     */
    public ISpecificationSynchInterface getSpecificationSynchInterface(
            final TupleCentreIdentifier id) {
        try {
            return ((RespectTC) this.registry.getTC(id))
                    .getSpecificationSynchInterface();
        } catch (final InstantiationNotPossibleException e) {
            final RespectTC tc = new RespectTC(id, this,
                    RespectTCContainer.QUEUE_SIZE);
            this.registry.addTC(tc);
            return tc.getSpecificationSynchInterface();
        }
    }
}
