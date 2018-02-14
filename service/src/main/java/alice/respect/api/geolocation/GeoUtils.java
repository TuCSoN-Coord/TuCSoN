package alice.respect.api.geolocation;

/**
 * 
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 * 
 */
public final class GeoUtils {
    private static final double EARTH_RADIUS = 3958.75;
    private static final float LATLNG_CONVERSION = 111200f;
    private static final double METER_CONVERSION = 1609;

    /**
     * Calculates the distance in meters between who points expressed in
     * latitude and longitude coordinates.
     * 
     * @param lat1
     *            the latitude coordinate of the start point
     * @param lng1
     *            the longitude coordinate of the start point
     * @param lat2
     *            the latitude coordinate of the end point
     * @param lng2
     *            the longitude coordinate of the end point
     * @return the distance between two points in meters
     */
    public static float distanceBetween(final float lat1, final float lng1,
            final float lat2, final float lng2) {
        final double dLat = Math.toRadians(lat2 - lat1);
        final double dLng = Math.toRadians(lng2 - lng1);
        final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
                * Math.sin(dLng / 2);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        final double dist = GeoUtils.EARTH_RADIUS * c;
        return new Float(dist * GeoUtils.METER_CONVERSION).floatValue();
    }

    /**
     * Converts from meters to degrees
     * 
     * @param val
     *            the distance in meters
     * @return the distance in degrees
     */
    public static float toDegrees(final float val) {
        return val / GeoUtils.LATLNG_CONVERSION;
    }

    /**
     * Converts from degrees to meters
     * 
     * @param val
     *            the distance in degrees
     * @return the distance in meters
     */
    public static float toMeters(final float val) {
        return val * GeoUtils.LATLNG_CONVERSION;
    }

    private GeoUtils() {
        /*
         * 
         */
    }
}
