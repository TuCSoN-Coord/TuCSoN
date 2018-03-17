/**
 * ResourcesChief.java
 */
package alice.tuplecentre.respect.core;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import alice.tuplecentre.respect.situatedness.AbstractProbeId;
import alice.tuplecentre.respect.situatedness.ISimpleProbe;
import alice.tuplecentre.respect.situatedness.ProbeIdentifier;
import alice.tuplecentre.respect.situatedness.TransducerId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 04/nov/2013
 */
public enum ProbesManager {

    /**
     * the singleton instance of this probes manager
     */
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    /**
     * Utility method used to communicate an output message to the console.
     *
     * @param msg message to print
     */
    private static void speak(final String msg) {
        LOGGER.info("..[ResourceManager]: " + msg);
    }

    private static void speakErr(final String msg) {
        LOGGER.error("..[ResourceManager]: " + msg);
    }

    /**
     * List of all probes on a single node
     **/
    private final Map<ProbeIdentifier, ISimpleProbe> probesList;

    ProbesManager() {
        this.probesList = new HashMap<>();
    }

    /**
     * Creates a resource
     *
     * @param className the concrete implementative class of the resource
     * @param id        the identifier of the resource
     * @return wether the Resource has been succesfully created.
     * @throws ClassNotFoundException    if the given Java full class name cannot be found within
     *                                   known paths
     * @throws NoSuchMethodException     if the Java method name cannot be found
     * @throws InstantiationException    if the given Java class cannot be instantiated
     * @throws IllegalAccessException    if the caller has no rights to access class, methods, or
     *                                   fields
     * @throws InvocationTargetException if the callee cannot be found
     */
    public synchronized void createProbe(final String className,
                                         final ProbeIdentifier id) throws ClassNotFoundException,
            NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        if (this.probesList.containsKey(id)) {
            ProbesManager.speakErr("Probe '" + id.getLocalName()
                    + "' already exists!");
            return;
        }
        final String normClassName = className.substring(1,
                className.length() - 1);
        final Class<?> c = Class.forName(normClassName);
        final Constructor<?> ctor = c
                .getConstructor(AbstractProbeId.class);
        final ISimpleProbe probe = (ISimpleProbe) ctor
                .newInstance(new Object[]{id});
        this.probesList.put(id, probe);
        ProbesManager.speak("Resource '" + id.getLocalName()
                + "' has been registered.");
    }

    /**
     * Gets the resource by its identifier
     *
     * @param id the resource's identifier
     * @return an interface toward the resource whose identifier has been given
     */
    // FIXME Check correctness (synchronization needed?)
    public ISimpleProbe getProbe(final ProbeIdentifier id) {
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
     * @param name resource's local name
     * @return an interface toward the resource whose logical name has been
     * given
     */
    // FIXME Check correctness (synchronization needed?)
    public ISimpleProbe getProbeByName(final String name) {
        final Object[] keySet = this.probesList.keySet().toArray();
        for (final Object element : keySet) {
            if (((ProbeIdentifier) element).getLocalName().equals(name)) {
                return this.probesList.get(element);
            }
        }
        ProbesManager.speakErr("'Resource " + name + "' isn't registered yet!");
        return null;
    }

    /**
     * Removes a resource from the list
     *
     * @param id the identifier of the resource to remove
     * @return wether the resource has been successfully removed
     */
    public synchronized void removeProbe(final ProbeIdentifier id) {
        ProbesManager.speak("Removing probe '" + id.getLocalName() + "'...");
        if (!this.probesList.containsKey(id)) {
            ProbesManager.speakErr("Resource '" + id.getLocalName()
                    + "' doesn't exist!");
            return;
        }
        final TransducersManager tm = TransducersManager.INSTANCE;
        tm.removeProbe(id);
        this.probesList.remove(id);
    }

    /**
     * Sets the transducer which the probe will communicate with.
     *
     * @param pId the probe's identifier
     * @param tId the transducer's identifier
     */
    public void setTransducer(final ProbeIdentifier pId, final TransducerId tId) {
        Objects.requireNonNull(this.getProbe(pId)).setTransducer(tId);
        if (tId != null) {
            ProbesManager.speak("...transducer '" + tId.getLocalName()
                    + "' set to probe '" + pId.getLocalName() + "'.");
        }
    }
}
