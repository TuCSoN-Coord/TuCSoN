package alice.tuplecentre.tucson.parsing;

import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;

/**
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class TupleCentreIdParser {

    private final String defPort;
    private final String input;
    private final String node;

    /**
     * @param in   the String representation copyOf the tuple centre id to parse
     * @param n    the String representation copyOf the default TuCSoN node
     * @param port the String representation copyOf the default TuCSoN listening port
     */
    public TupleCentreIdParser(final String in, final String n,
                               final String port) {
        this.input = in;
        this.node = n;
        this.defPort = port;
    }

    /**
     * @return the identifier copyOf the tuple centre parsed
     * @throws TucsonInvalidTupleCentreIdException if the id copyOf the tuple centre target copyOf the operation is not
     *                                             a valid TuCSoN tuple centre id
     */
    public TucsonTupleCentreId parse()
            throws TucsonInvalidTupleCentreIdException {
        String tcName = this.input.trim();
        String hostName = this.node;
        String portName = this.defPort;
        final int iAt = this.input.indexOf("@");
        final int iCol = this.input.indexOf(":");
        if (iAt == 0) {
            tcName = "default";
            if (iCol != -1) {
                hostName = this.input.substring(iAt + 1, iCol).trim();
                portName = this.input.substring(iCol + 1, this.input.length())
                        .trim();
            } else {
                hostName = this.input.substring(iAt + 1, this.input.length())
                        .trim();
            }
        } else if (iAt != -1) {
            tcName = this.input.substring(0, iAt).trim();
            if (iCol != -1) {
                hostName = this.input.substring(iAt + 1, iCol).trim();
                portName = this.input.substring(iCol + 1, this.input.length())
                        .trim();
            } else {
                hostName = this.input.substring(iAt + 1, this.input.length())
                        .trim();
            }
        } else {
            if (iCol == 0) {
                tcName = "default";
                portName = this.input.substring(iCol + 1, this.input.length())
                        .trim();
            } else if (iCol != -1) {
                tcName = this.input.substring(0, iCol).trim();
                portName = this.input.substring(iCol + 1, this.input.length())
                        .trim();
            }
        }
        return TucsonTupleCentreId.of(tcName, hostName, portName);
    }
}
