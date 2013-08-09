package alice.respect.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import alice.respect.api.EnvId;
import alice.respect.probe.ISimpleProbe;
import alice.respect.probe.ProbeId;
import alice.respect.transducer.Transducer;
import alice.respect.transducer.TransducerId;
import alice.respect.transducer.TransducerStandardInterface;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.api.TupleCentreId;
import alice.tuprolog.Term;

/**
 * 
 * Class used for managing transducers. It can be used for environment
 * configuration and as a provider of information concerning transducer's
 * associations.
 * 
 * @author Steven Maraldi
 * 
 */
public class TransducerManager {
    /** List of the associations transducer/probes **/
    private static HashMap<TransducerId, ArrayList<ProbeId>> resourceList;

    /** The TransducerManager instance **/
    private static TransducerManager tm;

    /** List of all the transducers on a single node **/
    private static HashMap<TransducerId, Transducer> transducerList;

    /** List of the associations tuple centre/transducers **/
    private static HashMap<TupleCentreId, ArrayList<TransducerId>> tupleCentresAssociated;

    /**
     * Gets the static instance of the transducer manager
     * 
     * @return the transducer manager
     */
    public synchronized static TransducerManager getTransducerManager() {
        if (TransducerManager.tm == null) {
            TransducerManager.tm = new TransducerManager();
        }

        return TransducerManager.tm;
    }

    /**
     * Utility methods used to communicate an output message to the console.
     * 
     * @param msg
     *            message to print.
     */
    private static void speak(final String msg) {
        System.out.println("[TransducerManager] " + msg);
    }

    private static void speakErr(final String msg) {
        System.err.println("[TransducerManager] " + msg);
    }

    private TransducerManager() {
        TransducerManager.transducerList =
                new HashMap<TransducerId, Transducer>();
        TransducerManager.resourceList =
                new HashMap<TransducerId, ArrayList<ProbeId>>();
        TransducerManager.tupleCentresAssociated =
                new HashMap<TupleCentreId, ArrayList<TransducerId>>();
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
     */
    public boolean addResource(final ProbeId id, final TransducerId tId,
            final ISimpleProbe probe) {
        TransducerManager.speak("Adding new resource " + id.getLocalName()
                + " to transducer " + tId.getAgentName());
        if (!TransducerManager.resourceList.containsKey(tId)) {
            TransducerManager.speakErr("Transducer " + tId.getAgentName()
                    + " doesn't exist.");
            return false;
        } else if (TransducerManager.resourceList.get(tId).contains(probe)) {
            TransducerManager.speak("Transducer " + tId.getAgentName()
                    + " is already associated to probe " + id.getLocalName());
            return false;
        }
        TransducerManager.transducerList.get(tId).addProbe(id, probe);
        TransducerManager.resourceList.get(tId).add(id);
        ResourceManager.getResourceManager().setTransducer(id, tId);
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
     * 
     * 
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public boolean createTransducer(final String className,
            final TransducerId id, final TupleCentreId tcId,
            final ProbeId probeId) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, SecurityException, IllegalArgumentException,
            InvocationTargetException {
        // Checking if the transducer already exist
        if (TransducerManager.transducerList.containsKey(id)) {
            TransducerManager.speakErr("Transducer " + id.toString()
                    + " is already been registered");
            return false;
        }

        // Registering tuple centre <> transducer association
        if (TransducerManager.tupleCentresAssociated.containsKey(tcId)) {
            TransducerManager.tupleCentresAssociated.get(tcId).add(id);
        } else {
            final ArrayList<TransducerId> transducers =
                    new ArrayList<TransducerId>();
            transducers.add(id);
            TransducerManager.tupleCentresAssociated.put(tcId, transducers);
        }

        // Instantiating the concrete class
        final String normClassName =
                className.substring(1, className.length() - 1);
        final Class<?> c = Class.forName(normClassName);
        final Constructor<?> ctor =
                c.getConstructor(new Class[] { TransducerId.class,
                        TupleCentreId.class, ProbeId.class });
        final Transducer t =
                (Transducer) ctor
                        .newInstance(new Object[] { id, tcId, probeId });
        TransducerManager.transducerList.put(id, t);

        // Adding probe to the transducer
        final ArrayList<ProbeId> probes = new ArrayList<ProbeId>();
        probes.add(probeId);
        TransducerManager.resourceList.put(id, probes);
        this.addResource(probeId, id, ResourceManager.getResourceManager()
                .getResource(probeId));
        TransducerManager.speak("Transducer " + id.toString()
                + " has been registered");
        return true;
    }

    /**
     * Returns the resource list associated to the transducer identified by tId
     * 
     * @param tId
     *            the transducer's identifier
     * @return a resource list as a ProbeId array.
     */
    public ProbeId[] getResources(final TransducerId tId) {
        if (!TransducerManager.resourceList.containsKey(tId)) {
            TransducerManager.speakErr("The transducer " + tId.getAgentName()
                    + " doesn't exist");
            return null;
        }
        final Object[] values =
                TransducerManager.resourceList.get(tId).toArray();
        final ProbeId[] probes = new ProbeId[values.length];
        for (int i = 0; i < probes.length; i++) {
            probes[i] = (ProbeId) values[i];
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
    public TransducerStandardInterface getTransducer(final String tId) {
        final Object[] keySet =
                TransducerManager.transducerList.keySet().toArray();
        for (final Object element : keySet) {
            if (((TransducerId) element).getAgentName().equals(tId)) {
                return TransducerManager.transducerList.get(element);
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
    public TransducerId getTransducerId(final EnvId probe) {
        final Set<TransducerId> set = TransducerManager.resourceList.keySet();
        final Object[] keySet = set.toArray();
        for (final Object element : keySet) {
            for (int j = 0; j < TransducerManager.resourceList.get(element)
                    .size(); j++) {
                final Term pId =
                        TransducerManager.resourceList.get(element).get(j)
                                .toTerm();
                if (pId.equals(probe.toTerm())) {
                    return (TransducerId) element;
                }
            }
        }
        TransducerManager.speakErr("The resource " + probe.getLocalName()
                + " isn't associated to any transducer");
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
    public TransducerId[] getTransducerIds(final TupleCentreId tcId) {
        final Object[] tcIds =
                TransducerManager.tupleCentresAssociated.keySet().toArray();
        for (final Object tcId2 : tcIds) {
            if (((TupleCentreId) tcId2).toString().equals(tcId.toString())) {
                final Object[] values =
                        TransducerManager.tupleCentresAssociated.get(tcId2)
                                .toArray();
                final TransducerId[] transducerIds =
                        new TransducerId[values.length];
                for (int j = 0; j < values.length; j++) {
                    transducerIds[j] = (TransducerId) values[j];
                }
                return transducerIds;
            }
        }
        TransducerManager
                .speakErr("There's no transducer associated to tuple centre "
                        + tcId + " or it doesn't exist at all");
        return null;
    }

    /**
     * Returns the identifier of the tuple centre associated to the transducer
     * identified by tId
     * 
     * @param tId
     *            the transducer's identifier
     * @return the tuple centre's identifier
     */
    public TupleCentreId getTupleCentreId(final TransducerId tId) {
        if (TransducerManager.tupleCentresAssociated.containsValue(tId)) {
            final TupleCentreId[] tcArray =
                    (TupleCentreId[]) TransducerManager.tupleCentresAssociated
                            .keySet().toArray();
            for (final TupleCentreId element : tcArray) {
                if (TransducerManager.tupleCentresAssociated.get(element)
                        .contains(tId)) {
                    return element;
                }
            }
        }
        TransducerManager.speakErr("The transducer " + tId.getAgentName()
                + " doesn't exist");
        return null;
    }

    /**
     * Removes a probe from the resource list
     * 
     * @param probe
     *            the resource's identifier to remove
     * @throws TucsonOperationNotPossibleException
     */
    public boolean removeResource(final ProbeId probe)
            throws TucsonOperationNotPossibleException {
        if (!TransducerManager.resourceList.containsValue(probe)) {
            return false;
        }
        final TransducerId tId = this.getTransducerId(probe);
        TransducerManager.transducerList.get(tId).removeProbe(probe);
        TransducerManager.resourceList.get(tId).remove(probe);
        ResourceManager.getResourceManager().getResource(probe)
                .setTransducer(null);
        // Se il transducer � rimasto senza risorse associate viene terminato
        if (TransducerManager.resourceList.get(tId).isEmpty()) {
            TransducerManager
                    .speak("Transducer "
                            + tId.toString()
                            + " has no more resources associated. Its execution will be stopped");
            this.stopTransducer(tId);
        }
        return true;
    }

    /**
     * Stops and removes the transducer identified by id
     * 
     * @param id
     *            the transducer identifier
     * @throws TucsonOperationNotPossibleException
     */
    public void stopTransducer(final TransducerId id)
            throws TucsonOperationNotPossibleException {
        if (!TransducerManager.transducerList.containsKey(id)) {
            TransducerManager.speakErr("The transducer " + id
                    + " doesn't exist.");
            return;
        }
        TransducerManager.transducerList.get(id).exit();
        // Decouple the transducer from the probes associated.
        final Object[] pIds = TransducerManager.resourceList.get(id).toArray();
        for (final Object pId : pIds) {
            ResourceManager.getResourceManager().getResource((ProbeId) pId)
                    .setTransducer(null);
        }

        TransducerManager.transducerList.remove(id);
        TransducerManager.resourceList.remove(id);
        final TupleCentreId tcAssociated = this.getTupleCentreId(id);
        TransducerManager.tupleCentresAssociated.get(tcAssociated).remove(id);
        // If the tc doesn't have any transducer associated, it will be removed
        if (TransducerManager.tupleCentresAssociated.get(tcAssociated)
                .isEmpty()) {
            TransducerManager.tupleCentresAssociated.remove(tcAssociated);
        }
        TransducerManager.speak("Transducer " + id + " has been removed.");
    }
}