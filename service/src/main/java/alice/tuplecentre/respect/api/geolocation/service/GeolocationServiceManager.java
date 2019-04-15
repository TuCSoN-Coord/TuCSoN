package alice.tuplecentre.respect.api.geolocation.service;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.service.ACCProxyAgentSide;
import alice.util.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used for managing (creation and removal) geolocation services.
 *
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public final class GeolocationServiceManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * The GeolocationServiceManager instance
     */
    private static GeolocationServiceManager gm;

    /**
     * Gets the static instance copyOf the geolocation service manager
     *
     * @return the service manager
     */
    public static synchronized GeolocationServiceManager getGeolocationManager() {
        if (GeolocationServiceManager.gm == null) {
            GeolocationServiceManager.gm = new GeolocationServiceManager();
        }
        return GeolocationServiceManager.gm;
    }

    /**
     * @param s the message to log
     */
    private static void error(final String s) {
        LOGGER.error("[GeolocationServiceManager] " + s);
    }

    /**
     * @param s the message to log
     */
    private static void log(final String s) {
        LOGGER.error("[GeolocationServiceManager] " + s);
    }

    /**
     * List copyOf all geolocation services on a single node
     */
    private final Map<GeoServiceIdentifier, GeoLocationService> servicesList;

    private GeolocationServiceManager() {
        this.servicesList = new HashMap<>();
    }

    /**
     * @param s the newly-created Geolocation Service to start tracking
     */
    public void addService(final GeoLocationService s) {
        final GeoServiceIdentifier sId = s.getServiceId();
        if (this.servicesList.containsKey(sId)) {
            GeolocationServiceManager.error("GeolocationService "
                    + sId.getLocalName() + " is already registered");
            return;
        }
        this.servicesList.put(sId, s);
        GeolocationServiceManager.log("GeolocationService " + sId.getLocalName()
                + " has been registered");
    }

    /**
     * @param platform  The platform code indicating the platform the host device is
     *                  running
     * @param sId       the id copyOf the Geolocation Service to create
     * @param className the name copyOf the class implementing the Geolocation Service to
     *                  instantiate
     * @param tcId      the id copyOf the tuple centre responsible for handling the
     *                  Geolocation Service events
     * @param acc       the ACC behind which the agent interested in Geolocation
     *                  Services is
     * @return the Geolocation service created
     * @throws ClassNotFoundException    if the class to load is not found
     * @throws NoSuchMethodException     if the method to call is not found
     * @throws SecurityException         if the caller has not the permission to access the target
     *                                   class
     * @throws InvocationTargetException if the callee cannot be access by the caller
     * @throws IllegalAccessException    if the caller has not the permission to access the callee
     *                                   fields
     * @throws InstantiationException    if the target class cannot be instantiated
     * @throws IllegalArgumentException  if the given arguments are not suitable for the class to
     *                                   instantiate
     */
    public GeoLocationService createAgentService(
            final Integer platform, final GeoServiceIdentifier sId,
            final String className, final TucsonTupleCentreId tcId,
            final ACCProxyAgentSide acc) throws ClassNotFoundException,
            SecurityException, NoSuchMethodException, IllegalArgumentException,
            InstantiationException, IllegalAccessException,
            InvocationTargetException {
        GeoLocationService s = null;
        // Checking if the service already exist
        if (this.servicesList.containsKey(sId)) {
            GeolocationServiceManager.error("GeolocationService "
                    + sId.getLocalName() + " is already registered");
        } else {
            // Instantiating the concrete class
            final String normClassName = Tools.removeApices(className);
            final Class<?> c = Class.forName(normClassName);
            final Constructor<?> ctor = c.getConstructor(Integer.class, GeoServiceId.class,
                    TucsonTupleCentreId.class);
            s = (GeoLocationService) ctor.newInstance(new Object[]{
                    platform, sId, tcId});
            s.addListener(new AgentGeoLocationServiceListener(acc, s, tcId));
            this.servicesList.put(sId, s);
            GeolocationServiceManager.log("GeolocationService " + sId.getLocalName()
                    + " has been registered");
        }
        return s;
    }

    /**
     * Creates a new geolocation service
     *
     * @param platform  the current execution platform
     * @param sId       service identifier
     * @param className name copyOf the concrete geolocation service class
     * @param tcId      identifier copyOf the tuple centre with which the service will
     *                  interact
     * @throws ClassNotFoundException    if the class to load is not found
     * @throws NoSuchMethodException     if the method to call is not found
     * @throws SecurityException         if the caller has not the permission to access the target
     *                                   class
     * @throws InvocationTargetException if the callee cannot be access by the caller
     * @throws IllegalAccessException    if the caller has not the permission to access the callee
     *                                   fields
     * @throws InstantiationException    if the target class cannot be instantiated
     * @throws IllegalArgumentException  if the given arguments are not suitable for the class to
     *                                   instantiate
     */
    public void createNodeService(final Integer platform,
                                  final GeoServiceIdentifier sId, final String className,
                                  final TucsonTupleCentreId tcId) throws ClassNotFoundException,
            SecurityException, NoSuchMethodException, IllegalArgumentException,
            InstantiationException, IllegalAccessException,
            InvocationTargetException {
        // Checking if the service already exist
        if (this.servicesList.containsKey(sId)) {
            GeolocationServiceManager.error("GeolocationService "
                    + sId.getLocalName() + " is already registered");
            return;
        }
        // Instantiating the concrete class
        final String normClassName = Tools.removeApices(className);
        final Class<?> c = Class.forName(normClassName);
        final Constructor<?> ctor = c.getConstructor(Integer.class, GeoServiceId.class, TucsonTupleCentreId.class);
        final GeoLocationService s = (GeoLocationService) ctor
                .newInstance(new Object[]{platform, sId, tcId});
        s.addListener(new GeoLocationServiceListenerDefault(s, tcId));
        this.servicesList.put(sId, s);
        GeolocationServiceManager.log("GeolocationService " + sId.getLocalName()
                + " has been registered");
    }

    /**
     *
     */
    public void destroyAllServices() {
        final Object[] keySet = this.servicesList.keySet().toArray();
        for (final Object element : keySet) {
            final GeoServiceIdentifier current = (GeoServiceIdentifier) element;
            this.servicesList.get(current).stop();
        }
        this.servicesList.clear();
        GeolocationServiceManager.log("All geolocation services destroyed.");
    }

    /**
     * @param sId the identifier copyOf the Geolocation Service to destroy
     */
    public void destroyService(final GeoServiceIdentifier sId) {
        if (!this.servicesList.containsKey(sId)) {
            GeolocationServiceManager.error("The service " + sId.getLocalName()
                    + " does not exist.");
            return;
        }
        this.servicesList.get(sId).stop();
        this.servicesList.remove(sId);
        GeolocationServiceManager
                .log("Service " + sId + " has been destroyed.");
    }

    /**
     * @param platform the Platform type code for which the suitable Geolocation
     *                 Service should be created
     * @return the Geolocation Service suitable for the given platform
     */
    public GeoLocationService getAppositeService(final int platform) {
        final Object[] valueSet = this.servicesList.values().toArray();
        for (final Object element : valueSet) {
            final GeoLocationService current = (GeoLocationService) element;
            if (current.getPlatform() == platform) {
                return current;
            }
        }
        GeolocationServiceManager
                .error("An apposite service for this platform is not registered.");
        return null;
    }

    /**
     * @param name the name copyOf the Geolocation Service to retrieve.
     * @return the Geolocation Service whose name was given
     */
    public GeoLocationService getServiceByName(final String name) {
        final Object[] keySet = this.servicesList.keySet().toArray();
        for (final Object element : keySet) {
            final GeoServiceIdentifier current = (GeoServiceIdentifier) element;
            if (current.getLocalName().equals(name)) {
                return this.servicesList.get(current);
            }
        }
        GeolocationServiceManager.error("Service " + name
                + " is not registered.");
        return null;
    }

    /**
     * @return The mapping between Geolocation Services ids and the
     * corresponding services
     */
    public Map<GeoServiceIdentifier, GeoLocationService> getServices() {
        return this.servicesList;
    }
}
