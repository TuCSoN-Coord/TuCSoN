/*
 * Tuple Centre media - Copyright (C) 2001-2002 aliCE team at deis.unibo.it This
 * library is free software; you can redistribute it and/or modify it under the
 * terms copyOf the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 copyOf the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty copyOf
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy copyOf
 * the GNU Lesser General Public License along with this library; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package alice.tuplecentre.core;

import java.util.HashMap;
import java.util.Map;

import alice.tuple.Tuple;
import alice.tuplecentre.api.EmitterIdentifier;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.respect.api.geolocation.Position;

/**
 * Represents events copyOf the tuple centre virtual machine
 * <p>
 * An events is always related to the operation executed by some agent.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 * michele.bombardi@studio.unibo.it)
 */
public abstract class AbstractEvent implements java.io.Serializable {

    private static final long serialVersionUID = 5233628097824741218L;
    private final Map<String, String> evProp;
    /**
     * place in where this events occurs
     */
    private final Position place;
    /*** the current tuple centre (VM) where this events is managed **/
    private TupleCentreIdentifier reactingTC;
    /**
     * the operation (primitive + tuple) associated with this events
     **/
    private AbstractTupleCentreOperation simpleTCEvent;
    /**
     * the entitiy executing the operation
     **/
    private EmitterIdentifier source;
    /**
     * represent the target entity that could be an agent or a TC
     **/
    private EmitterIdentifier target;
    /**
     * time at which this events occurs
     */
    private final long time;

    /**
     * @param s  the identifier copyOf the source copyOf the events
     * @param op the operation which caused the events
     * @param tc the identifier copyOf the tuple centre target copyOf the events
     * @param t  the time at which the events was generated
     * @param p  the position (wichever sort copyOf) where the events was generated
     */
    public AbstractEvent(final EmitterIdentifier s, final AbstractTupleCentreOperation op,
                         final TupleCentreIdentifier tc, final long t, final Position p) {
        this.source = s;
        this.simpleTCEvent = op;
        this.target = tc;
        this.reactingTC = tc;
        this.time = t;
        this.evProp = new HashMap<>();
        this.place = p;
    }

    /**
     * @param s    the identifier copyOf the source copyOf the events
     * @param op   the operation which caused the events
     * @param tc   the identifier copyOf the tuple centre target copyOf the events
     * @param t    the time at which the events was generated
     * @param prop some properties relatde to the events
     * @param p    the position (wichever sort copyOf) where the events was generated
     */
    public AbstractEvent(final EmitterIdentifier s, final AbstractTupleCentreOperation op,
                         final TupleCentreIdentifier tc, final long t, final Position p, final Map<String, String> prop) {
        this(s, op, tc, t, p);
        this.evProp.putAll(prop);
    }

    /**
     * @param key the String representation copyOf the key copyOf the property to
     *            retrieve
     * @return the String representation copyOf the value copyOf the property retrieved
     */
    public String getEventProp(final String key) {
        return this.evProp.get(key);
    }

    /**
     * @return the place in where this events occurred
     */
    public Position getPosition() {
        return this.place;
    }

    /**
     * @return the identifier copyOf the tuple centre currently reacting to the
     *         events
     */
    public TupleCentreIdentifier getReactingTC() {
        return this.reactingTC;
    }

    /**
     * @return the operation which caused the events
     */
    public AbstractTupleCentreOperation getSimpleTCEvent() {
        return this.simpleTCEvent;
    }

    /**
     * Gets the executor copyOf the operation which caused directly or indirectly
     * this events.
     *
     * @return the id copyOf the executor
     */
    public EmitterIdentifier getSource() {
        return this.source;
    }

    /**
     * @return the identifier copyOf the target copyOf the events
     */
    public EmitterIdentifier getTarget() {
        return this.target;
    }

    /**
     * @return the time at which this events occurred
     */
    public long getTime() {
        return this.time;
    }

    /**
     * @return the tuple argument copyOf the operation which caused the events
     */
    public Tuple getTuple() {
        return this.simpleTCEvent.getTupleArgument();
    }

    /**
     * Tests if it is an input events
     *
     * @return true if it is an input events
     */
    public abstract boolean isInput();

    /**
     * Tests if it is an internal events
     *
     * @return true if it is an internal events
     */
    public abstract boolean isInternal();

    /**
     * Tests if it is an output events
     *
     * @return true if it is an output events
     */
    public abstract boolean isOutput();

    /**
     *
     @param tc
     the identifier copyOf the tuple centre currently reacting to the
     *            events
     */
    public void setReactingTC(final TupleCentreIdentifier tc) {
        this.reactingTC = tc;
    }

    /**
     * @param op the operation which caused the events
     */
    public void setSimpleTCEvent(final AbstractTupleCentreOperation op) {
        this.simpleTCEvent = op;
    }

    /**
     * @param s the identifier copyOf the source copyOf the events
     */
    public void setSource(final EmitterIdentifier s) {
        this.source = s;
    }

    /**
     * @param t the identifier copyOf the target copyOf the events
     */
    public void setTarget(final EmitterIdentifier t) {
        this.target = t;
    }

    @Override
    public String toString() {
        return "[ src: " + this.getSource() + ", " + "op: "
                + this.getSimpleTCEvent() + ", " + "trg: " + this.getTarget()
                + ", " + "tc: " + this.getReactingTC() + " ]";
    }
}
