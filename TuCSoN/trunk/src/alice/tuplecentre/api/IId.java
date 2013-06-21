package alice.tuplecentre.api;

/**
 * Represents an operation emitter's interface: it could be both an agent of a
 * tuple centre
 * 
 * @author teo
 * 
 */
public interface IId {

    public boolean isAgent();

    public boolean isEnv();

    public boolean isTC();

}
