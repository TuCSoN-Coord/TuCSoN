package alice.respect.api.geolocation;

import java.util.Properties;

/**
 * 
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 * 
 */
public final class PlatformUtils {
    /**
     * Gets the current execution platform
     * 
     * @return an int value representing the current execution platform
     */
    public static int getPlatform() {
        final Properties p = System.getProperties();
        String type = p.getProperty("os.name");
        // System.out.println("OS: " + type);
        type = type.toLowerCase();
        String specType = p.getProperty("java.runtime.name");
        // System.out.println("Java Runtime: " + specType);
        specType = specType.toLowerCase();
        if (type.contains("windows")) {
            // System.out.println("Running on Windows");
            return Platforms.WINDOWS;
        } else if (type.contains("linux")) {
            if (specType.contains("android")) {
                // System.out.println("Running on Android");
                return Platforms.ANDROID;
            }
            // System.out.println("Running on Linux");
            return Platforms.LINUX;
        } else {
            // System.out.println("Running on Mac Os");
            return Platforms.MACOS;
        }
    }

    private PlatformUtils() {
        /*
         * 
         */
    }
}
