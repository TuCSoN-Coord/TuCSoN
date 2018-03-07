/*
 * Tuple Centre media - Copyright (C) 2001-2002 aliCE team at deis.unibo.it This
 * library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with this library; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package alice.tuplecentre.core;

import java.util.HashMap;
import java.util.Map;

import alice.tuplecentre.api.EmitterIdentifier;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.respect.api.geolocation.Position;

/**
 * Represents events of the tuple centre virtual machine
 * <p>
 * An event is always related to the operation executed by some agent.
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
     * place in where this event occurs
     */
    private final Position place;
    /**
     * the current tuple centre (VM) where this event is managed
     **/
    private TupleCentreIdentifier reactingTC;
    /**
     * the operation (primitive + tuple) associated with this event
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
     * time at which this event occurs
     */
    private final long time;

    /**
     * @param s  the identifier of the source of the event
     * @param op the operation which caused the event
     * @param tc the identifier of the tuple centre target of the event
     * @param t  the time at which the event was generated
     * @param p  the position (wichever sort of) where the event was generated
     */
    public AbstractEvent(final EmitterIdentifier s, final AbstractTupleCentreOperation op,
                         final TupleCentreIdentifier tc, final long t, final Position p) {
        this.source = s;
        this.simpleTCEvent = op;
        this.target = tc;
        this.reactingTC = tc;
        this.time = t;
        this.evProp = new HashMap<String, String>();
        this.place = p;
    }

    /**
     * @param s    the identifier of the source of the event
     * @param op   the operation which caused the event
     * @param tc   the identifier of the tuple centre target of the event
     * @param t    the time at which the event was generated
     * @param prop some properties relatde to the event
     * @param p    the position (wichever sort of) where the event was generated
     */
    public AbstractEvent(final EmitterIdentifier s, final AbstractTupleCentreOperation op,
                         final TupleCentreIdentifier tc, final long t, final Position p, final Map<String, String> prop) {
        this(s, op, tc, t, p);
        this.evProp.putAll(prop);
    }

    /**
     * @param key the String representation of the key of the property to
     *            retrieve
     * @return the String representation of the value of the property retrieved
     */
    public String getEventProp(final String key) {
        return this.evProp.get(key);
    }

    /**
     * @return the place in where this event occurred
     */
    public Position getPosition() {
        return this.place;
    }

    /**
     * @return the identifier of the tuple centre currently reacting to the
     * event
     */
    public TupleCentreIdentifier getReactingTC() {
        return this.reactingTC;
    }

    /**
     * @return the operation which caused the event
     */
    public AbstractTupleCentreOperation getSimpleTCEvent() {
        return this.simpleTCEvent;
    }

    /**
     * Gets the executor of the operation which caused directly or indirectly
     * this event.
     *
     * @return the id of the executor
     */
    public EmitterIdentifier getSource() {
        return this.source;
    }

    /**
     * @return the identifier of the target of the event
     */
    public EmitterIdentifier getTarget() {
        return this.target;
    }

    /**
     * @return the time at which this event occurred
     */
    public long getTime() {
        return this.time;
    }

    /**
     * @return the tuple argument of the operation which caused the event
     */
    public Tuple getTuple() {
        return this.simpleTCEvent.getTupleArgument();
    }

    /**
     * Tests if it is an input event
     *
     * @return true if it is an input event
     */
    public abstract boolean isInput();

    /**
     * Tests if it is an internal event
     *
     * @return true if it is an internal event
     */
    public abstract boolean isInternal();

    /**
     * Tests if it is an output event
     *
     * @return true if it is an output event
     */
    public abstract boolean isOutput();

    /**
     * @param tc the identifier of the tuple centre currently reacting to the
     *           event
     */
    public void setReactingTC(final TupleCentreIdentifier tc) {
        this.reactingTC = tc;
    }

    /**
     * @param op the operation which caused the event
     */
    public void setSimpleTCEvent(final AbstractTupleCentreOperation op) {
        this.simpleTCEvent = op;
    }

    /**
     * @param s the identifier of the source of the event
     */
    public void setSource(final EmitterIdentifier s) {
        this.source = s;
    }

    /**
     * @param t the identifier of the target of the event
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
