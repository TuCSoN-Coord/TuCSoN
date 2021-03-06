package alice.tuplecentre.respect.api;

import java.io.Serializable;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;

/**
 * @author Unknown...
 */
public class EnvironmentId implements EnvironmentIdentifier, Serializable {

    /**
     * serialVersionUID
     **/
    private static final long serialVersionUID = 1L;
    private final Struct id;
    private final String localName;

    /**
     * @param i the struct representing this environment identifier
     */
    public EnvironmentId(final String i) {
        this.localName = i;
        this.id = new Struct(i);
    }

    @Override
    public String getLocalName() {
        return this.localName;
    }

    @Override
    public boolean isAgent() {
        return false;
    }

    @Override
    public boolean isEnv() {
        return true;
    }

    @Override
    public boolean isGeo() {
        return false;
    }

    @Override
    public boolean isTC() {
        return false;
    }

    @Override
    public String toString() {
        return this.toTerm().toString();
    }

    @Override
    public Term toTerm() {
        if ("@".equals(this.id.getName())) {
            return this.id.getArg(0).getTerm();
        }
        return this.id.getTerm();
    }
}
