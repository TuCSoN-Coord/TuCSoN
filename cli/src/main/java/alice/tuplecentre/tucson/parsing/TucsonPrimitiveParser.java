package alice.tuplecentre.tucson.parsing;

import alice.tuplecentre.tucson.service.TucsonCmd;

/**
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class TucsonPrimitiveParser {

    private final String input;

    /**
     * @param in the String representation of the TuCSoN primitive to be parsed
     */
    public TucsonPrimitiveParser(final String in) {
        this.input = in;
    }

    /**
     * @return the Object representing the TuCSoN command parsed
     */
    public TucsonCmd parse() {
        String primitive = this.input.trim();
        String arg = "";
        final int iLeftBra = this.input.indexOf("(");
        final int iRightBra = this.input.lastIndexOf(")");
        if (iLeftBra != -1) {
            primitive = this.input.substring(0, iLeftBra).trim();
            if (iRightBra != -1) {
                arg = this.input.substring(iLeftBra + 1, iRightBra).trim();
                if (("get".equals(primitive) || "get_s".equals(primitive))
                        && !"".equals(arg)) {
                    primitive = "";
                    arg = "";
                    return null;
                }
            } else {
                primitive = "";
                return null;
            }
        } else {
            if (!"quit".equals(primitive) && !"exit".equals(primitive)
                    && !"get".equals(primitive) && !"get_s".equals(primitive)
                    && !"help".equals(primitive) && !"man".equals(primitive)
                    && !"syntax".equals(primitive) && !"o/".equals(primitive)
                    && !"\\o".equals(primitive) && !"hi".equals(primitive)) {
                primitive = "";
                return null;
            }
        }
        return new TucsonCmd(primitive, arg);
    }
}
