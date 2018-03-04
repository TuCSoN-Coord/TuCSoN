package alice.respect.api;

import alice.tuplecentre.core.AbstractOperationId;

/**
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class RespectOpId extends AbstractOperationId {

    /**
     * @param i the progressive, unique per tuple centre, operation identifier
     */
    public RespectOpId(final long i) {
        super(i);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof RespectOpId)) {
            return false;
        }
        final RespectOpId other = (RespectOpId) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (this.id ^ this.id >>> 32);
        return result;
    }

}
