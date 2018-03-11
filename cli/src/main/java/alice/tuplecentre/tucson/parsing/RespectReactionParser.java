package alice.tuplecentre.tucson.parsing;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.util.Tools;

/**
 *
 * @author ste (mailto: s.mariani@unibo.it)
 *
 */
public class RespectReactionParser {

    private static boolean bigFatCondition(final TupleArgument arg) {
        return "request".equals(arg.getName())
                || "response".equals(arg.getName())
                || "success".equals(arg.getName())
                || "failure".equals(arg.getName())
                || "endo".equals(arg.getName()) || "exo".equals(arg.getName())
                || "intra".equals(arg.getName())
                || "inter".equals(arg.getName())
                || "from_agent".equals(arg.getName())
                || "to_agent".equals(arg.getName())
                || "from_tc".equals(arg.getName())
                || "to_tc".equals(arg.getName())
                || "before".equals(arg.getName())
                || "after".equals(arg.getName())
                || "from_agent".equals(arg.getName())
                || "invocation".equals(arg.getName())
                || "inv".equals(arg.getName()) || "req".equals(arg.getName())
                || "pre".equals(arg.getName())
                || "completion".equals(arg.getName())
                || "compl".equals(arg.getName())
                || "resp".equals(arg.getName()) || "post".equals(arg.getName())
                || "between".equals(arg.getName())
                || "operation".equals(arg.getName())
                || "link_out".equals(arg.getName())
                || "link_in".equals(arg.getName())
                || "internal".equals(arg.getName());
    }

    private static void log(final String msg) {
        System.out.println("[RespectReactionParser]: " + msg);
    }

    private boolean flag;
    private int nGuards;
    private int nTimes;
    private String spec;
    private final LogicTuple t;

    /**
     *
     * @param lt
     *            the logic tuple representation of the ReSpecT reaction
     */
    public RespectReactionParser(final LogicTuple lt) {
        this.t = lt;
        this.spec = "'reaction(";
        this.nGuards = 0;
        this.flag = false;
        this.nTimes = 0;
    }

    /**
     *
     * @return the String representation of the ReSpecT reaction given to the
     *         parser
     */
    public String parse() {
        if ("[]".equals(this.t.getName())) {
            return "";
        }
        for (int i = 0; i < this.t.getArity(); i++) {
            this.parse(this.t.getArg(i));
        }
        RespectReactionParser.log("spec = "
                + this.spec.substring(1, this.spec.length() - 1));
        return this.spec.substring(1, this.spec.length() - 1);
    }

    private void parse(final TupleArgument arg) {
        if ("[]".equals(arg.toString())) {
            if (this.nTimes == 1) {
                this.spec = this.spec.substring(0, this.spec.length() - 1)
                        + ")).'";
            } else {
                this.spec = this.spec.substring(0, this.spec.length() - 1)
                        + ".'";
            }
        } else if (",".equals(Tools.removeApices(arg.getName()))) {
            for (int i = 0; i < arg.getArity(); i++) {
                this.parse(arg.getArg(i));
            }
            if (this.flag) {
                this.nTimes++;
                if (this.nTimes == 1) {
                    this.spec = this.spec.substring(0, this.spec.length() - 1);
                    this.spec += ")).";
                }
            }
        } else if (".".equals(Tools.removeApices(arg.getName()))) {
            this.spec += " reaction(";
            this.nGuards = 0;
            this.flag = false;
            this.nTimes = 0;
            for (int i = 0; i < arg.getArity(); i++) {
                this.parse(arg.getArg(i));
            }
        } else {
            if (RespectReactionParser.bigFatCondition(arg)) {
                this.nGuards++;
                if (this.nGuards == 1) {
                    this.flag = false;
                    this.spec += "(" + arg + ",";
                } else {
                    this.spec += arg + ",";
                }
            } else {
                if (this.nGuards > 0) {
                    this.nGuards = 0;
                    this.flag = true;
                    this.spec = this.spec.substring(0, this.spec.length() - 1);
                    this.spec += "),(" + arg + ",";
                } else {
                    this.spec += arg + ",";
                }
            }
        }
    }
}
