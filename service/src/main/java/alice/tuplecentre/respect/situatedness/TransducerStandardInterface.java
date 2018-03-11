package alice.tuplecentre.respect.situatedness;

import alice.tuplecentre.api.TupleCentreId;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.respect.core.InternalEvent;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * Interface for a generic transducer. This interface is implemented by the
 * AbstractTransducer class, hence TuCSoN programmers should only care of
 * extending such abstract class with their own actual transducers
 * implementation. In particular, this alleviates the burden of implementing
 * 'notifyEnvEvent' and 'notifyOutput' methods, which are the same for all
 * transducers.
 *
 * @author Steven Maraldi
 *
 */
public interface TransducerStandardInterface {

    /**
     *
     * @return the identifier of the transducer
     */
    TransducerId getIdentifier();

    /**
     *
     * @return the list of Probes for which this tranduces is responsible for
     */
    AbstractProbeId[] getProbes();

    /**
     *
     * @return the identifier of the TuCSoN tuple centre this transducer works
     *         with
     */
    TupleCentreId getTCId();

    /**
     *
     * @param key
     *            the <code>key</code> of the environmental property change to
     *            be notified
     * @param value
     *            the <code>value</code> of the environmental property change to
     *            be notified
     * @param mode
     *            if the notification regards a 'sensing' operation or an
     *            'action' operation ('getEnv' and 'setEnv' primitives
     *            respectively)
     * @throws TucsonOperationNotPossibleException
     *             if the requested operation cannot be performed for some
     *             reason
     * @throws UnreachableNodeException
     *             if the TuCSoN tuple centre target of the notification cannot
     *             be reached over the network
     * @throws OperationTimeOutException
     *             if the notification operation expires timeout
     */
    void notifyEnvEvent(String key, int value, int mode)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     *
     * @param ev
     *            the ReSpecT events to be notified
     * @return wether the events has been succesfully notified
     * @throws TucsonOperationNotPossibleException
     *             if the requested operation cannot be performed for some
     *             reason
     * @throws UnreachableNodeException
     *             if the TuCSoN tuple centre target of the notification cannot
     *             be reached over the network
     * @throws OperationTimeOutException
     *             if the notification operation expires timeout
     */
    boolean notifyOutput(InternalEvent ev)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;
}
