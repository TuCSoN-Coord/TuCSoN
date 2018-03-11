package alice.tuplecentre.tucson.parsing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.service.TucsonCmd;

/**
 *
 * @author ste (mailto: s.mariani@unibo.it)
 *
 */
public class TucsonOpParser {

    private static boolean insideReaction(final String in) {
        final Pattern pattern = Pattern
                .compile("((out|in|rd|no|inp|rdp|nop|get|set)_s)+?");
        final Matcher matcher = pattern.matcher(in);
        return matcher.find();
    }

    private TucsonPrimitiveParser cmdParser;
    private final String defPort;
    private final String input;
    private final String node;
    private TucsonCmd tcmd;
    private TucsonTupleCentreId tid;
    private TupleCentreIdParser tidParser;

    /**
     *
     * @param in
     *            the String representation of the TuCSoN operation to parse
     * @param n
     *            the String representation of the default TuCSoN node of
     *            reference
     * @param port
     *            the String representation of the default TuCSoN listening port
     *            of reference
     */
    public TucsonOpParser(final String in, final String n, final int port) {
        this.input = in;
        this.node = n;
        this.defPort = String.valueOf(port);
        this.tcmd = null;
        this.tid = null;
        this.cmdParser = null;
        this.tidParser = null;
    }

    /**
     *
     * @return the TuCSoN command part of the TuCSoN operation
     */
    public TucsonCmd getCmd() {
        return this.tcmd;
    }

    /**
     *
     * @return the id of the tuple centre involved in the operation
     */
    public TucsonTupleCentreId getTid() {
        return this.tid;
    }

    /**
     *
     * @throws TucsonInvalidTupleCentreIdException
     *             if the id of the tuple centre target of the operation is not
     *             a valid TuCSoN tuple centre id
     */
    public void parse() throws TucsonInvalidTupleCentreIdException {
        String cmd = this.input.trim();
        String tc = "default";
        final int iOp = this.input.indexOf('?');
        final int iBra = this.input.indexOf('(');
        if (iOp != -1
                && !TucsonOpParser.insideReaction(this.input.substring(0, iOp)
                        .trim())) {
            if (iBra != -1 && iBra < iOp) {
                cmd = this.input.substring(iOp + 1, this.input.length()).trim();
                tc = this.input.substring(0, iOp).trim();
            } else if (iBra == -1 || iOp < iBra) {
                cmd = this.input.substring(iOp + 1, this.input.length()).trim();
                tc = this.input.substring(0, iOp).trim();
            }
        }
        this.cmdParser = new TucsonPrimitiveParser(cmd);
        this.tidParser = new TupleCentreIdParser(tc, this.node, this.defPort);
        this.tcmd = this.cmdParser.parse();
        this.tid = this.tidParser.parse();
    }
}
