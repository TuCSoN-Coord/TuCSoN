/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tucson.introspection;

import java.io.Serializable;
import alice.tuple.logic.LogicTuple;

/**
 *
 * @author Unknown...
 *
 */
public class InspectorProtocol implements Serializable {

    /** don't observe */
    public static final byte NO_OBSERVATION = 1;
    /** observe continuosly */
    public static final byte PROACTIVE_OBSERVATION = 3;
    /** observe only when asked by inspector */
    public static final byte REACTIVE_OBSERVATION = 2;
    /** observe step mode like an agent */
    public static final byte STEPMODE_AGENT_OBSERVATION = 5;
    /** observe step mode like the tuple space */
    public static final byte STEPMODE_TUPLESPACE_OBSERVATION = 4;
    private static final long serialVersionUID = -6842026459178793395L;
    /** defining W set observation */
    private byte pendingQueryObservType = InspectorProtocol.NO_OBSERVATION;
    /** defining T set observation */
    private byte reactionsObservType = InspectorProtocol.NO_OBSERVATION;
    /** defining stepMode observation */
    private byte stepModeObservType = InspectorProtocol.STEPMODE_TUPLESPACE_OBSERVATION;
    /** desired tracing state for the vm */
    private boolean tracing = false;
    /** filter for tuple observed */
    private LogicTuple tsetFilter = null;
    /** defining T set observation */
    private byte tsetObservType = InspectorProtocol.NO_OBSERVATION;
    /** filter for query observed */
    private LogicTuple wsetFilter = null;

    /**
     * @return the pendingQueryObservType
     */
    public byte getPendingQueryObservType() {
        return this.pendingQueryObservType;
    }

    /**
     * @return the reactionsObservType
     */
    public byte getReactionsObservType() {
        return this.reactionsObservType;
    }

    /**
     * @return the sepModeObservType
     */
    public byte getStepModeObservType() {
        return this.stepModeObservType;
    }

    /**
     * @return the tsetFilter
     */
    public LogicTuple getTsetFilter() {
        return this.tsetFilter;
    }

    /**
     * @return the tsetObservType
     */
    public byte getTsetObservType() {
        return this.tsetObservType;
    }

    /**
     * @return the wsetFilter
     */
    public LogicTuple getWsetFilter() {
        return this.wsetFilter;
    }

    /**
     * @return the tracing
     */
    public boolean isTracing() {
        return this.tracing;
    }

    /**
     * @param obstype
     *            the pendingQueryObservType to set
     */
    public void setPendingQueryObservType(final byte obstype) {
        this.pendingQueryObservType = obstype;
    }

    /**
     * @param obstype
     *            the reactionsObservType to set
     */
    public void setReactionsObservType(final byte obstype) {
        this.reactionsObservType = obstype;
    }

    /**
     * @param obstype
     *            the stepModeObservType to set
     */
    public void setStepModeObservType(final byte obstype) {
        this.stepModeObservType = obstype;
    }

    /**
     * @param t
     *            the tracing to set
     */
    public void setTracing(final boolean t) {
        this.tracing = t;
    }

    /**
     * @param filter
     *            the tsetFilter to set
     */
    public void setTsetFilter(final LogicTuple filter) {
        this.tsetFilter = filter;
    }

    /**
     * @param obstype
     *            the tsetObservType to set
     */
    public void setTsetObservType(final byte obstype) {
        this.tsetObservType = obstype;
    }

    /**
     * @param filter
     *            the wsetFilter to set
     */
    public void setWsetFilter(final LogicTuple filter) {
        this.wsetFilter = filter;
    }
}
