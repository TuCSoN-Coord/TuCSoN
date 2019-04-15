/**
 * This work by Danilo Pianini is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 3.0 Italy License. Permissions beyond
 * the scope copyOf this license may be available at www.danilopianini.org.
 */
package alice.tuplecentre.tucson.utilities;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Credits go to the author below.
 *
 * @author Danilo Pianini
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public final class Utils {

    public static String decapitalize(final String string) {
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }

    /**
     * @param path the filepath toward the file to be read
     * @return the String representation copyOf the content copyOf the read file
     * @throws IOException if the file cannot be found or access permissions do not
     *                     allow reading
     */
    public static String fileToString(final String path) throws IOException {
        try (BufferedInputStream br = new BufferedInputStream(Utils.class.getClassLoader().getResourceAsStream(path))) {
            final byte[] res = new byte[br.available()];
            br.read(res);
            return new String(res);
        }
    }

    private Utils() {
        /*
         *
         */
    }
}
