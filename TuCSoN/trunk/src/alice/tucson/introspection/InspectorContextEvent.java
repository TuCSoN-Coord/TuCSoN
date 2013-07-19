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

import java.util.List;

import alice.logictuple.LogicTuple;
import alice.tuplecentre.core.TriggeredReaction;

/**
 * This class defines the events that are generated by an InspectorContext.
 * 
 * @see alice.tucson.introspection.InspectorContext
 * 
 * @author ste (mailto: s.mariani@unibo.it) on 03/lug/2013
 * 
 */
public class InspectorContextEvent implements java.io.Serializable {

    private static final long serialVersionUID = -5586328696570013265L;

    /** observer time */
    private long localTime;

    /** observed a reaction failure */
    private TriggeredReaction reactionFailed = null;

    /** observed a reaction ok */
    private TriggeredReaction reactionOk = null;

    /** tuple observed or to set */
    private List<LogicTuple> tuples = null;

    /** virtual machine time */
    private long vmTime;

    /** events observed */
    private List<WSetEvent> wnEvents = null;

    /**
     * @return the localTime
     */
    public long getLocalTime() {
        return this.localTime;
    }

    /**
     * @return the reactionFailed
     */
    public TriggeredReaction getReactionFailed() {
        return this.reactionFailed;
    }

    /**
     * @return the reactionOk
     */
    public TriggeredReaction getReactionOk() {
        return this.reactionOk;
    }

    /**
     * @return the tuples
     */
    public List<LogicTuple> getTuples() {
        return this.tuples;
    }

    /**
     * @return the vmTime
     */
    public long getVmTime() {
        return this.vmTime;
    }

    /**
     * @return the wnEvents
     */
    public List<WSetEvent> getWnEvents() {
        return this.wnEvents;
    }

    /**
     * @param lt
     *            the localTime to set
     */
    public void setLocalTime(final long lt) {
        this.localTime = lt;
    }

    /**
     * @param rf
     *            the reactionFailed to set
     */
    public void setReactionFailed(final TriggeredReaction rf) {
        this.reactionFailed = rf;
    }

    /**
     * @param ro
     *            the reactionOk to set
     */
    public void setReactionOk(final TriggeredReaction ro) {
        this.reactionOk = ro;
    }

    /**
     * @param t
     *            the tuples to set
     */
    public void setTuples(final List<LogicTuple> t) {
        this.tuples = t;
    }

    /**
     * @param vmt
     *            the vmTime to set
     */
    public void setVmTime(final long vmt) {
        this.vmTime = vmt;
    }

    /**
     * @param wne
     *            the wnEvents to set
     */
    public void setWnEvents(final List<WSetEvent> wne) {
        this.wnEvents = wne;
    }

}
