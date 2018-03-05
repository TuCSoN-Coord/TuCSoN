/**
 * ResourcesChief.java
 */
package alice.tuplecentre.respect.core;

import alice.tuplecentre.respect.situatedness.AbstractProbeId;
import alice.tuplecentre.respect.situatedness.ISimpleProbe;
import alice.tuplecentre.respect.situatedness.TransducerId;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 04/nov/2013
 *
 */
public enum ProbesManager {
    /** the singleton instance of this probes manager */
    INSTANCE;

    /**
     * Utility method used to communicate an output message to the console.
     *
     * @param msg
     *            message to print
     */
    private static void speak(final String msg) {
        System.out.println("..[ResourceManager]: " + msg);
    }

    private static void speakErr(final String msg) {
        System.err.println("..[ResourceManager]: " + msg);
    }

    /** List of all probes on a single node **/
    private final Map<AbstractProbeId, ISimpleProbe> probesList;

    private ProbesManager() {
        this.probesList = new HashMap<AbstractProbeId, ISimpleProbe>();
    }

    /**
     * Creates a resource
     *
     * @param className
     *            the concrete implementative class of the resource
     * @param id
     *            the identifier of the resource
     * @return wether the Resource has been succesfully created.
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
    public synchronized boolean createProbe(final String className,
            final AbstractProbeId id) throws ClassNotFoundException,
            NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        if (this.probesList.containsKey(id)) {
            ProbesManager.speakErr("Probe '" + id.getLocalName()
                    + "' already exists!");
            return false;
        }
        final String normClassName = className.substring(1,
                className.length() - 1);
        final Class<?> c = Class.forName(normClassName);
        final Constructor<?> ctor = c
                .getConstructor(new Class[] { AbstractProbeId.class });
        final ISimpleProbe probe = (ISimpleProbe) ctor
                .newInstance(new Object[] { id });
        this.probesList.put(id, probe);
        ProbesManager.speak("Resource '" + id.getLocalName()
                + "' has been registered.");
        return true;
    }

    /**
     * Gets the resource by its identifier
     *
     * @param id
     *            the resource's identifier
     * @return an interface toward the resource whose identifier has been given
     */
    // FIXME Check correctness (synchronization needed?)
    public ISimpleProbe getProbe(final AbstractProbeId id) {
        if (this.probesList.containsKey(id)) {
            return this.probesList.get(id);
        }
        ProbesManager.speakErr("Resource '" + id.getLocalName()
                + "' isn't registered yet!");
        return null;
    }

    /**
     * Gets the resource by its local name
     *
     * @param name
     *            resource's local name
     * @return an interface toward the resource whose logical name has been
     *         given
     */
    // FIXME Check correctness (synchronization needed?)
    public ISimpleProbe getProbeByName(final String name) {
        final Object[] keySet = this.probesList.keySet().toArray();
        for (final Object element : keySet) {
            if (((AbstractProbeId) element).getLocalName().equals(name)) {
                return this.probesList.get(element);
            }
        }
        ProbesManager.speakErr("'Resource " + name + "' isn't registered yet!");
        return null;
    }

    /**
     * Removes a resource from the list
     *
     * @param id
     *            the identifier of the resource to remove
     * @return wether the resource has been successfully removed
     */
    public synchronized boolean removeProbe(final AbstractProbeId id) {
        ProbesManager.speak("Removing probe '" + id.getLocalName() + "'...");
        if (!this.probesList.containsKey(id)) {
            ProbesManager.speakErr("Resource '" + id.getLocalName()
                    + "' doesn't exist!");
            return false;
        }
        final TransducersManager tm = TransducersManager.INSTANCE;
        tm.removeProbe(id);
        this.probesList.remove(id);
        return true;
    }

    /**
     * Sets the transducer which the probe will communicate with.
     *
     * @param pId
     *            the probe's identifier
     * @param tId
     *            the transducer's identifier
     */
    public void setTransducer(final AbstractProbeId pId, final TransducerId tId) {
        this.getProbe(pId).setTransducer(tId);
        if (tId != null) {
            ProbesManager.speak("...transducer '" + tId.getAgentName()
                    + "' set to probe '" + pId.getLocalName() + "'.");
        }
    }
}
