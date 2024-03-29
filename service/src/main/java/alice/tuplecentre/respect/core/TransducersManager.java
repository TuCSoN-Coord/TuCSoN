/**
 * TransducersChief.java
 */
package alice.tuplecentre.respect.core;

import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.respect.api.EnvironmentIdentifier;
import alice.tuplecentre.respect.situatedness.*;
import alice.tuprolog.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 04/nov/2013
 */
public enum TransducersManager {
    /**
     * The singleton istance copyOf this enum type
     */
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Utility methods used to communicate an output message to the console.
     *
     * @param msg message to print.
     */
    private static void speak(final String msg) {
        LOGGER.info("..[TransducerManager]: " + msg);
    }

    private static void speakErr(final String msg) {
        LOGGER.error("..[TransducerManager]: " + msg);
    }

    /**
     * List copyOf the associations transducer/probes
     **/
    private final Map<TransducerId, List<ProbeIdentifier>> probesToTransducersMap;
    /**
     * List copyOf all the transducers on a single node
     **/
    private final Map<TransducerId, AbstractTransducer> transducersList;
    /**
     * List copyOf the associations tuple centre/transducers
     **/
    private final Map<TupleCentreIdentifier, List<TransducerId>> transducersToTupleCentresMap;

    TransducersManager() {
        this.transducersList = new HashMap<>();
        this.probesToTransducersMap = new HashMap<>();
        this.transducersToTupleCentresMap = new HashMap<>();
    }

    /**
     * Adds a new resource and associate it to the transducer tId.
     *
     * @param id    new environment resource identifier
     * @param tId   transducer associated
     * @param probe the probe itself
     */
    public synchronized void addProbe(final ProbeIdentifier id,
                                      final TransducerId tId, final Probe probe) {
        TransducersManager.speak("Adding resource '" + id.getLocalName()
                + "' to transducer '" + tId.getLocalName() + "'...");
        if (!this.probesToTransducersMap.containsKey(tId)) {
            TransducersManager.speakErr("Transducer '" + tId.getLocalName()
                    + "' doesn't exist yet!");
            return;
        } else if (this.probesToTransducersMap.get(tId).contains(probe)) {
            TransducersManager.speak("Transducer '" + tId.getLocalName()
                    + "' is already associated to probe '" + id.getLocalName()
                    + "'.");
            return;
        }
        this.transducersList.get(tId).addProbe(id, probe);
        this.probesToTransducersMap.get(tId).add(id);
        ProbesManager.INSTANCE.setTransducer(id, tId);
    }

    /**
     * Creates a new transducer
     *
     * @param className name copyOf the concrete implementative class copyOf transducer
     * @param id        the transducer's identifier
     * @param tcId      the tuple center with which the transducer will interact
     * @param probeId   resource's identifier associated to the transducer
     * @throws ClassNotFoundException    if the given Java full class name cannot be found within
     *                                   known paths
     * @throws NoSuchMethodException     if the Java method name cannot be found
     * @throws InstantiationException    if the given Java class cannot be instantiated
     * @throws IllegalAccessException    if the caller has no rights to access class, methods, or
     *                                   fields
     * @throws InvocationTargetException if the callee cannot be found
     */
    public synchronized void createTransducer(final String className,
                                              final TransducerId id, final TupleCentreIdentifier tcId,
                                              final ProbeIdentifier probeId) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException {
        // Checking if the transducer already exist
        if (this.transducersList.containsKey(id)) {
            TransducersManager.speakErr("Transducer '" + id.toString()
                    + "' is already registered!");
            return;
        }
        // Registering tuple centre <> transducer association
        if (this.transducersToTupleCentresMap.containsKey(tcId)) {
            this.transducersToTupleCentresMap.get(tcId).add(id);
        } else {
            final ArrayList<TransducerId> transducers = new ArrayList<>();
            transducers.add(id);
            this.transducersToTupleCentresMap.put(tcId, transducers);
        }
        // Instantiating the concrete class
        final String normClassName = className.substring(1,
                className.length() - 1);
        final Class<?> c = Class.forName(normClassName);
        final Constructor<?> ctor = c.getConstructor(TransducerId.class, TupleCentreIdentifier.class);
        final AbstractTransducer t = (AbstractTransducer) ctor
                .newInstance(new Object[]{id, tcId});
        this.transducersList.put(id, t);
        // Adding probe to the transducer
        final ArrayList<ProbeIdentifier> probes = new ArrayList<>();
        probes.add(probeId);
        this.probesToTransducersMap.put(id, probes);
        this.addProbe(probeId, id, ProbesManager.INSTANCE.getProbe(probeId));
        TransducersManager.speak("Transducer '" + id.toString()
                + "' has been registered.");
    }

    /**
     * Returns the resource list associated to the transducer identified by tId
     *
     * @param tId the transducer's identifier
     * @return a resource list as a ProbeId array.
     */
    // FIXME Check correctness (synchronization needed?)
    public ProbeIdentifier[] getProbes(final TransducerId tId) {
        if (!this.probesToTransducersMap.containsKey(tId)) {
            TransducersManager.speakErr("Transducer '" + tId.getLocalName()
                    + "' doesn't exist yet!");
            return null;
        }
        final Object[] values = this.probesToTransducersMap.get(tId).toArray();
        final ProbeIdentifier[] probes = new ProbeIdentifier[values.length];
        for (int i = 0; i < probes.length; i++) {
            probes[i] = (ProbeIdentifier) values[i];
        }
        return probes;
    }

    /**
     * Returns the transducer identified by tId
     *
     * @param tId the transducer's name
     * @return the transducer
     */
    // FIXME Check correctness (synchronization needed?)
    public TransducerStandardInterface getTransducer(final String tId) {
        final Object[] keySet = this.transducersList.keySet().toArray();
        for (final Object element : keySet) {
            if (((TransducerId) element).getLocalName().equals(tId)) {
                return this.transducersList.get(element);
            }
        }
        return null;
    }

    /**
     * Returns the transducer's identifier associated to the input resource
     *
     * @param probe resource associated to the transducer
     * @return the transducer identifier
     */
    // FIXME Check correctness (synchronization needed?)
    public TransducerId getTransducerId(final EnvironmentIdentifier probe) {
        final Set<TransducerId> set = this.probesToTransducersMap.keySet();
        final Object[] keySet = set.toArray();
        for (final Object element : keySet) {
            for (int j = 0; j < this.probesToTransducersMap.get(element).size(); j++) {
                final Term pId = this.probesToTransducersMap.get(element)
                        .get(j).toTerm();
                if (pId.equals(probe.toTerm())) {
                    return (TransducerId) element;
                }
            }
        }
        TransducersManager.speakErr("Resource '" + probe.getLocalName()
                + "' isn't associated to any transducer yet!");
        return null;
    }

    /**
     * Gets the list copyOf transducer ids associated to the tuple centre identified
     * by tcId
     *
     * @param tcId the tuple centre's identifier
     * @return list copyOf transducer id associated to tcId
     */
    // FIXME Check correctness (synchronization needed?)
    public TransducerId[] getTransducerIds(final TupleCentreIdentifier tcId) {
        final Object[] tcIds = this.transducersToTupleCentresMap.keySet()
                .toArray();
        for (final Object tcId2 : tcIds) {
            if (tcId2.toString().equals(tcId.toString())) {
                final Object[] values = this.transducersToTupleCentresMap.get(
                        tcId2).toArray();
                final TransducerId[] transducerIds = new TransducerId[values.length];
                for (int j = 0; j < values.length; j++) {
                    transducerIds[j] = (TransducerId) values[j];
                }
                return transducerIds;
            }
        }
        TransducersManager
                .speakErr("There's no transducer associated to tuple centre '"
                        + tcId + "' yet (or it doesn't exist at all)!");
        return new TransducerId[]{};
    }

    /**
     * Returns the identifier copyOf the tuple centre associated to the transducer
     * identified by tId
     *
     * @param tId the transducer's identifier
     * @return the tuple centre's identifier
     */
    // FIXME Check correctness (synchronization needed?)
    public TupleCentreIdentifier getTupleCentreId(final TransducerId tId) {
        for (final TupleCentreIdentifier t : this.transducersToTupleCentresMap.keySet()) {
            if (this.transducersToTupleCentresMap.get(t).contains(tId)) {
                return t;
            }
        }
        TransducersManager.speakErr("Transducer '" + tId.getLocalName()
                + "' doesn't exist yet!");
        return null;
    }

    /**
     * Removes a probe from the resource list
     *
     * @param probe the resource's identifier to remove
     */
    public synchronized void removeProbe(final ProbeIdentifier probe) {
        for (final TransducerId t : this.probesToTransducersMap.keySet()) {
            if (this.probesToTransducersMap.get(t).contains(probe)) {
                final TransducerId tId = this.getTransducerId(probe);
                this.transducersList.get(tId).removeProbe(probe);
                this.probesToTransducersMap.get(tId).remove(probe);
                Objects.requireNonNull(ProbesManager.INSTANCE.getProbe(probe)).setTransducer(null);
                // Se il transducer e' rimasto senza risorse associate viene
                // terminato
                if (this.probesToTransducersMap.get(tId).isEmpty()) {
                    TransducersManager
                            .speak("Transducer '"
                                    + Objects.requireNonNull(tId).toString()
                                    + "' has no more resources associated. Its execution will be stopped.");
                    this.stopTransducer(tId);
                }
                return;
            }
        }
    }

    /**
     * Stops and removes the transducer identified by id
     *
     * @param id the transducer identifier
     */
    public synchronized void stopTransducer(final TransducerId id) {
        if (!this.transducersList.containsKey(id)) {
            TransducersManager.speakErr("Transducer '" + id
                    + "' doesn't exist yet!");
            return;
        }
        this.transducersList.get(id).exit();
        // Decouple the transducer from the probes associated.
        final Object[] pIds = this.probesToTransducersMap.get(id).toArray();
        for (final Object pId : pIds) {
            Objects.requireNonNull(ProbesManager.INSTANCE.getProbe((ProbeIdentifier) pId))
                    .setTransducer(null);
        }
        this.transducersList.remove(id);
        this.probesToTransducersMap.remove(id);
        final TupleCentreIdentifier tcAssociated = this.getTupleCentreId(id);
        this.transducersToTupleCentresMap.get(tcAssociated).remove(id);
        // If the tc doesn't have any transducer associated, it will be removed
        if (this.transducersToTupleCentresMap.get(tcAssociated).isEmpty()) {
            this.transducersToTupleCentresMap.remove(tcAssociated);
        }
        TransducersManager.speak("Transducer '" + id + "' has been removed.");
    }
}
