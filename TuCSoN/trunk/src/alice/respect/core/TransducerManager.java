/**
 * TransducersChief.java
 */
package alice.respect.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import alice.respect.api.EnvId;
import alice.respect.situatedness.AbstractProbeId;
import alice.respect.situatedness.AbstractTransducer;
import alice.respect.situatedness.ISimpleProbe;
import alice.respect.situatedness.TransducerId;
import alice.respect.situatedness.TransducerStandardInterface;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.api.TupleCentreId;
import alice.tuprolog.Term;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 04/nov/2013
 * 
 */
public enum TransducerManager {
    INSTANCE;

    /**
     * Utility methods used to communicate an output message to the console.
     * 
     * @param msg
     *            message to print.
     */
    private static void speak(final String msg) {
        System.out.println("..[TransducerManager]: " + msg);
    }

    private static void speakErr(final String msg) {
        System.err.println("[TransducerManager]: " + msg);
    }

    /** List of the associations transducer/probes **/
    private Map<TransducerId, List<AbstractProbeId>> resourceList;

    /** List of all the transducers on a single node **/
    private Map<TransducerId, AbstractTransducer> transducerList;

    /** List of the associations tuple centre/transducers **/
    private Map<TupleCentreId, List<TransducerId>> tupleCentresAssociated;

    private TransducerManager() {
        this.transducerList = new HashMap<TransducerId, AbstractTransducer>();
        this.resourceList = new HashMap<TransducerId, List<AbstractProbeId>>();
        this.tupleCentresAssociated =
                new HashMap<TupleCentreId, List<TransducerId>>();
    }

    /**
     * Adds a new resource and associate it to the transducer tId.
     * 
     * @param id
     *            new environment resource identifier
     * @param tId
     *            transducer associated
     * @param probe
     *            the probe itself
     * @return wether the resource has been successfully added
     */
    public synchronized boolean addResource(final AbstractProbeId id,
            final TransducerId tId, final ISimpleProbe probe) {
        TransducerManager.speak("Adding resource '" + id.getLocalName()
                + "' to transducer '" + tId.getAgentName() + "'...");
        if (!this.resourceList.containsKey(tId)) {
            TransducerManager.speakErr("Transducer '" + tId.getAgentName()
                    + "' doesn't exist yet!");
            return false;
        } else if (this.resourceList.get(tId).contains(probe)) {
            TransducerManager.speak("Transducer '" + tId.getAgentName()
                    + "' is already associated to probe '" + id.getLocalName() + "'.");
            return false;
        }
        this.transducerList.get(tId).addProbe(id, probe);
        this.resourceList.get(tId).add(id);
        ResourceManager.INSTANCE.setTransducer(id, tId);
        return true;
    }

    /**
     * Creates a new transducer
     * 
     * @param className
     *            name of the concrete implementative class of transducer
     * @param id
     *            the transducer's identifier
     * @param tcId
     *            the tuple center with which the transducer will interact
     * @param probeId
     *            resource's identifier associated to the transducer
     * @return wether the transducer has been successfully created
     * 
     * @throws ClassNotFoundException
     *             if the given Java full class name cannot be found within
     *             known paths
     * @throws NoSuchMethodException
     *             if the Java method name cannot be found
     * @throws InstantiationException
     *             if the given Java class cannot be instantiated
     * @throws IllegalAccessException
     *             if the caller has no rights to access class, methods, or
     *             fields
     * @throws InvocationTargetException
     *             if the callee cannot be found
     */
    public synchronized boolean createTransducer(final String className,
            final TransducerId id, final TupleCentreId tcId,
            final AbstractProbeId probeId) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException {
        // Checking if the transducer already exist
        if (this.transducerList.containsKey(id)) {
            TransducerManager.speakErr("Transducer '" + id.toString()
                    + "' is already registered!");
            return false;
        }

        // Registering tuple centre <> transducer association
        if (this.tupleCentresAssociated.containsKey(tcId)) {
            this.tupleCentresAssociated.get(tcId).add(id);
        } else {
            final ArrayList<TransducerId> transducers =
                    new ArrayList<TransducerId>();
            transducers.add(id);
            this.tupleCentresAssociated.put(tcId, transducers);
        }

        // Instantiating the concrete class
        final String normClassName =
                className.substring(1, className.length() - 1);
        final Class<?> c = Class.forName(normClassName);
        final Constructor<?> ctor =
                c.getConstructor(new Class[] { TransducerId.class,
                        TupleCentreId.class });
        final AbstractTransducer t =
                (AbstractTransducer) ctor
                        .newInstance(new Object[] { id, tcId });
        this.transducerList.put(id, t);

        // Adding probe to the transducer
        final ArrayList<AbstractProbeId> probes =
                new ArrayList<AbstractProbeId>();
        probes.add(probeId);
        this.resourceList.put(id, probes);
        this.addResource(probeId, id,
                ResourceManager.INSTANCE.getResource(probeId));
        TransducerManager.speak("Transducer '" + id.toString()
                + "' has been registered.");
        return true;
    }

    /**
     * Returns the resource list associated to the transducer identified by tId
     * 
     * @param tId
     *            the transducer's identifier
     * @return a resource list as a ProbeId array.
     */
    // FIXME Check correctness (synchronization needed?)
    public AbstractProbeId[] getResources(final TransducerId tId) {
        if (!this.resourceList.containsKey(tId)) {
            TransducerManager.speakErr("Transducer '" + tId.getAgentName()
                    + "' doesn't exist yet!");
            return null;
        }
        final Object[] values = this.resourceList.get(tId).toArray();
        final AbstractProbeId[] probes = new AbstractProbeId[values.length];
        for (int i = 0; i < probes.length; i++) {
            probes[i] = (AbstractProbeId) values[i];
        }
        return probes;
    }

    /**
     * Returns the transducer identified by tId
     * 
     * @param tId
     *            the transducer's name
     * @return the transducer
     */
    // FIXME Check correctness (synchronization needed?)
    public TransducerStandardInterface getTransducer(final String tId) {
        final Object[] keySet = this.transducerList.keySet().toArray();
        for (final Object element : keySet) {
            if (((TransducerId) element).getAgentName().equals(tId)) {
                return this.transducerList.get(element);
            }
        }
        return null;
    }

    /**
     * Returns the transducer's identifier associated to the input resource
     * 
     * @param probe
     *            resource associated to the transducer
     * @return the transducer identifier
     */
    // FIXME Check correctness (synchronization needed?)
    public TransducerId getTransducerId(final EnvId probe) {
        final Set<TransducerId> set = this.resourceList.keySet();
        final Object[] keySet = set.toArray();
        for (final Object element : keySet) {
            for (int j = 0; j < this.resourceList.get(element).size(); j++) {
                final Term pId = this.resourceList.get(element).get(j).toTerm();
                if (pId.equals(probe.toTerm())) {
                    return (TransducerId) element;
                }
            }
        }
        TransducerManager.speakErr("Resource '" + probe.getLocalName()
                + "' isn't associated to any transducer yet!");
        return null;
    }

    /**
     * Gets the list of transducer ids associated to the tuple centre identified
     * by tcId
     * 
     * @param tcId
     *            the tuple centre's identifier
     * @return list of transducer id associated to tcId
     */
    // FIXME Check correctness (synchronization needed?)
    public TransducerId[] getTransducerIds(final TupleCentreId tcId) {
        final Object[] tcIds = this.tupleCentresAssociated.keySet().toArray();
        for (final Object tcId2 : tcIds) {
            if (((TupleCentreId) tcId2).toString().equals(tcId.toString())) {
                final Object[] values =
                        this.tupleCentresAssociated.get(tcId2).toArray();
                final TransducerId[] transducerIds =
                        new TransducerId[values.length];
                for (int j = 0; j < values.length; j++) {
                    transducerIds[j] = (TransducerId) values[j];
                }
                return transducerIds;
            }
        }
        TransducerManager
                .speakErr("There's no transducer associated to tuple centre '"
                        + tcId + "' yet (or it doesn't exist at all)!");
        return new TransducerId[] {};
    }

    /**
     * Returns the identifier of the tuple centre associated to the transducer
     * identified by tId
     * 
     * @param tId
     *            the transducer's identifier
     * @return the tuple centre's identifier
     */
    // FIXME Check correctness (synchronization needed?)
    public TupleCentreId getTupleCentreId(final TransducerId tId) {
        for (final TupleCentreId t : this.tupleCentresAssociated.keySet()) {
            if (this.tupleCentresAssociated.get(t).contains(tId)) {
                return t;
            }
        }
        TransducerManager.speakErr("Transducer '" + tId.getAgentName()
                + "' doesn't exist yet!");
        return null;
    }

    /**
     * Removes a probe from the resource list
     * 
     * @param probe
     *            the resource's identifier to remove
     * @return wether the resource has been succesfully removed
     * @throws TucsonOperationNotPossibleException
     *             if the requested operation cannot be succesfully carried out
     */
    public synchronized boolean removeResource(final AbstractProbeId probe)
            throws TucsonOperationNotPossibleException {
        for (final TransducerId t : this.resourceList.keySet()) {
            if (this.resourceList.get(t).contains(probe)) {
                final TransducerId tId = this.getTransducerId(probe);
                this.transducerList.get(tId).removeProbe(probe);
                this.resourceList.get(tId).remove(probe);
                ResourceManager.INSTANCE.getResource(probe).setTransducer(null);
                // Se il transducer e' rimasto senza risorse associate viene
                // terminato
                if (this.resourceList.get(tId).isEmpty()) {
                    TransducerManager
                            .speak("Transducer '"
                                    + tId.toString()
                                    + "' has no more resources associated. Its execution will be stopped.");
                    this.stopTransducer(tId);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Stops and removes the transducer identified by id
     * 
     * @param id
     *            the transducer identifier
     * @throws TucsonOperationNotPossibleException
     *             if the requested operation cannot be successfully performed
     */
    public synchronized void stopTransducer(final TransducerId id)
            throws TucsonOperationNotPossibleException {
        if (!this.transducerList.containsKey(id)) {
            TransducerManager.speakErr("Transducer '" + id
                    + "' doesn't exist yet!");
            return;
        }
        this.transducerList.get(id).exit();
        // Decouple the transducer from the probes associated.
        final Object[] pIds = this.resourceList.get(id).toArray();
        for (final Object pId : pIds) {
            ResourceManager.INSTANCE.getResource((AbstractProbeId) pId)
                    .setTransducer(null);
        }

        this.transducerList.remove(id);
        this.resourceList.remove(id);
        final TupleCentreId tcAssociated = this.getTupleCentreId(id);
        this.tupleCentresAssociated.get(tcAssociated).remove(id);
        // If the tc doesn't have any transducer associated, it will be removed
        if (this.tupleCentresAssociated.get(tcAssociated).isEmpty()) {
            this.tupleCentresAssociated.remove(tcAssociated);
        }
        TransducerManager.speak("Transducer '" + id + "' has been removed.");
    }

}
