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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import alice.respect.api.IRespectTC;
import alice.respect.api.geolocation.PlatformUtils;
import alice.respect.api.geolocation.Position;
import alice.respect.api.geolocation.service.AbstractGeolocationService;
import alice.respect.api.geolocation.service.GeolocationServiceManager;
import alice.respect.core.RespectVM;
import alice.respect.core.StepMonitor;
import alice.tuplecentre.api.AgentId;
import alice.tuplecentre.api.IId;
import alice.tuplecentre.api.ITupleCentre;
import alice.tuplecentre.api.ITupleCentreManagement;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleCentreId;
import alice.tuplecentre.api.TupleTemplate;
import alice.tuplecentre.api.exceptions.OperationNotPossibleException;
import alice.tuprolog.Term;

/**
 * Defines the core abstract behaviour of a tuple centre virtual machine.
 *
 * The class is abstract because the specific implementation of the reacting
 * behaviour and of the set management is left to the derived classes.
 *
 * This class implements - by means of the state pattern - the behaviour
 * described formally in the article "From Tuple Space to Tuple Centre"
 * (Omicini, Denti) - Science of Computer Programming 2001,
 *
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 */
public abstract class AbstractTupleCentreVMContext implements
        ITupleCentreManagement, ITupleCentre {

    private long bootTime;
    private InputEvent currentEvent;
    private AbstractTupleCentreVMState currentState;
    private Term distanceTollerance;
    private final List<AbstractEvent> inputEnvEvents;
    private final List<AbstractEvent> inputEvents;
    private boolean management;
    private final int maxPendingInputEventNumber;
    private Position place;
    private final IRespectTC respectTC;
    private final RespectVM rvm;
    private final Map<String, AbstractTupleCentreVMState> states;
    private final StepMonitor step;
    private boolean stepMode;
    private final TupleCentreId tid;

    /**
     * Creates a new tuple centre virtual machine core
     *
     * @param vm
     *            is the ReSpecT virtual machine
     * @param id
     *            is the tuple centre identifier
     * @param ieSize
     *            is the size of the input event queue
     * @param rtc
     *            the ReSpecT tuple centre this VM refers to
     */
    public AbstractTupleCentreVMContext(final RespectVM vm,
            final TupleCentreId id, final int ieSize, final IRespectTC rtc) {
        this.rvm = vm;
        this.management = false;
        this.stepMode = false;
        this.step = new StepMonitor();
        this.inputEvents = new LinkedList<AbstractEvent>();
        this.inputEnvEvents = new LinkedList<AbstractEvent>();
        this.tid = id;
        this.maxPendingInputEventNumber = ieSize;
        final AbstractTupleCentreVMState resetState = new ResetState(this);
        final AbstractTupleCentreVMState idleState = new IdleState(this);
        final AbstractTupleCentreVMState listeningState = new ListeningState(
                this);
        final AbstractTupleCentreVMState fetchState = new FetchState(this);
        final AbstractTupleCentreVMState fetchEnvState = new FetchEnvState(this);
        final AbstractTupleCentreVMState reactingState = new ReactingState(this);
        final AbstractTupleCentreVMState speakingState = new SpeakingState(this);
        this.states = new HashMap<String, AbstractTupleCentreVMState>();
        this.states.put("ResetState", resetState);
        this.states.put("IdleState", idleState);
        this.states.put("ListeningState", listeningState);
        this.states.put("FetchState", fetchState);
        this.states.put("FetchEnvState", fetchEnvState);
        this.states.put("ReactingState", reactingState);
        this.states.put("SpeakingState", speakingState);
        final Iterator<AbstractTupleCentreVMState> it = this.states.values()
                .iterator();
        while (it.hasNext()) {
            it.next().resolveLinks();
        }
        this.currentState = resetState;
        this.respectTC = rtc;
    }

    /**
     *
     * @param in
     *            the input envirnomental event to add to the environmental
     *            queue
     */
    public void addEnvInputEvent(final InputEvent in) {
        synchronized (this.inputEnvEvents) {
            this.inputEnvEvents.add(in);
        }
    }

    /**
     *
     * @param in
     *            the input event to add to the input queue
     */
    public void addInputEvent(final InputEvent in) {
        synchronized (this.inputEvents) {
            this.inputEvents.add(in);
        }
    }

    /**
     *
     * @param t
     *            the tuple representing the list of tuples to add
     * @return the list of tuples just added
     */
    public abstract List<Tuple> addListTuple(Tuple t);

    /**
     * Adds a query to the pending query set (W) of the tuple centre
     *
     * @param w
     *            the pending query to be added
     */
    public abstract void addPendingQueryEvent(InputEvent w);

    /**
     * Adds a tuple to the specification tuple set
     *
     * @param t
     *            the tuple to be added
     */
    public abstract void addSpecTuple(Tuple t);

    /**
     * Adds a tuple to the tuple set (T)
     *
     * @param t
     *            the tuple to be addedd
     * @param u
     *            a flag indicating wether a persistency update is due
     */
    public abstract void addTuple(Tuple t, boolean u);

    @Override
    public void doOperation(final IId who, final AbstractTupleCentreOperation op)
            throws OperationNotPossibleException {
        final InputEvent ev = new InputEvent(who, op, this.tid,
                this.getCurrentTime(), this.getPosition());
        synchronized (this.inputEvents) {
            if (this.inputEvents.size() > this.maxPendingInputEventNumber) {
                throw new OperationNotPossibleException(
                        "Max pending input event limit reached");
            }
            this.inputEvents.add(ev);
        }
    }
    
    public void doOperation(final InputEvent ev)
            throws OperationNotPossibleException {
        synchronized (this.inputEvents) {
            if (this.inputEvents.size() > this.maxPendingInputEventNumber) {
                throw new OperationNotPossibleException();
            }
            this.inputEvents.add(ev);
        }
    }

    /**
     * Removes all tuples
     */
    public abstract void emptyTupleSet();

    /**
     * Evaluates a triggered reaction, changing the state of the VM accordingly.
     *
     * @param z
     *            the triggered reaction to be evaluated
     */
    public abstract void evalReaction(TriggeredReaction z);

    /**
     * Executes a virtual machine behaviour cycle
     */
    public void execute() {
        while (!this.currentState.isIdle()) {
            this.currentState.execute();
            this.currentState = this.currentState.getNextState();
            // notify TYPE_NEWSTATE
            if (this.rvm.hasInspectors()) {
                this.rvm.notifyInspectableEvent(new InspectableEvent(this,
                        InspectableEvent.TYPE_NEWSTATE));
            }
            if (this.isStepMode()) {
                try {
                    this.step.awaitEvent();
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            /*
             * old if (this.management && this.stop) { if (!this.doStep) {
             * break; } this.doStep = false; }
             */
        }
    }

    /**
     *
     */
    public void fetchPendingEnvEvent() {
        if (this.pendingEnvEvents()) {
            synchronized (this.inputEnvEvents) {
                this.currentEvent = (InputEvent) this.inputEnvEvents.remove(0);
            }
        }
    }

    /**
     * Fetches a new pending input event.
     *
     * The first pending input event is fetched from the queue as current event
     * subject of VM process.
     *
     */
    public void fetchPendingEvent() {
        synchronized (this.inputEvents) {
            this.currentEvent = (InputEvent) this.inputEvents.remove(0);
        }
    }

    /**
     * Collects the time-triggered reactions
     *
     * @param ev
     *            the event triggering reactions
     */
    public abstract void fetchTimedReactions(AbstractEvent ev);

    /**
     * Collects the reactions that are triggered by an event
     *
     * @param ev
     *            the event triggering reactions
     */
    public abstract void fetchTriggeredReactions(AbstractEvent ev);

    /**
     * Gets all the tuples of the tuple centre
     *
     * @return the whole tuple set
     */
    public abstract List<Tuple> getAllTuples();

    /**
     * Gets the boot time of the Tuple Centre VM
     *
     * The time is expressed in millisecond, according to the standard Java
     * measurement of time.
     *
     * @return the time at which the tuple centre VM has been booted
     */
    public long getBootTime() {
        return this.bootTime;
    }

    /**
     * Gets the event currently processed by the virtual machine
     *
     * @return the input event currently under process
     */
    public InputEvent getCurrentEvent() {
        return this.currentEvent;
    }

    /**
     *
     * @return the String representation of the state the tuple centre VM is
     *         currently in
     */
    public String getCurrentState() {
        return this.currentState.getClass().getSimpleName();
    }

    /**
     * Gets current time of the Tuple Centre VM
     *
     * The time is expressed in millisecond, according to the standard Java
     * measurement of time.
     *
     * @return the time at which the tuple centre VM is now
     */
    public long getCurrentTime() {
        return System.currentTimeMillis() - this.bootTime;
    }
    
    /**
     * 
     * @return the tuProlog Term representing the floating point precision
     *         tollerance set for proximity check
     */
    public Term getDistanceTollerance() {
        return this.distanceTollerance;
    }

    /**
     * Gets the identifier of this tuple centre
     *
     * @return the identifier of the tuple centre managed by this tuple centre
     *         VM
     */
    public TupleCentreId getId() {
        return this.tid;
    }

    /**
     * Gets an iterator over the pending query set (W)
     *
     * @return the iterator
     */
    public abstract Iterator<? extends AbstractEvent> getPendingQuerySetIterator();
    
    /**
     * 
     * @return the position of the device hosting the tuple centre VM
     */
    public Position getPosition() {
        return this.place;
    }

    /**
     *
     * @return the ReSpecT tuple centre wrapper
     */
    public IRespectTC getRespectTC() {
        return this.respectTC;
    }

    /**
     *
     * @return the iterator through the tuple set
     */
    public abstract Iterator<? extends Tuple> getSpecTupleSetIterator();

    /**
     * Gets a state of tuple centre virtual machine.
     *
     * @param stateName
     *            name of the state
     * @return the state
     */
    public AbstractTupleCentreVMState getState(final String stateName) {
        return this.states.get(stateName);
    }

    /**
     * Gets an iterator over the set of triggered reactions
     *
     * @return the iterator
     */
    public abstract Iterator<? extends TriggeredReaction> getTriggeredReactionSetIterator();

    /**
     * Gets an iterator over the tuple set (T)
     *
     * @return the iterator
     */
    public abstract Iterator<? extends Tuple> getTupleSetIterator();

    @Override
    public void goCommand() throws OperationNotPossibleException {
        if (!this.management) {
            throw new OperationNotPossibleException();
        }
    }

    /**
     * Gets all the tuples of the tuple centre matching the TupleTemplate t
     *
     * @param t
     *            the tuple template to be used
     * @return the list of matching tuples
     */
    public abstract List<Tuple> inAllTuples(TupleTemplate t);

    @Override
    public boolean isStepMode() {
        return this.stepMode;
    }

    /**
     *
     * @param out
     *            the output event generated due to a linking operation
     */
    public abstract void linkOperation(OutputEvent out);

    @Override
    public void nextStepCommand() throws OperationNotPossibleException {
        if (!this.stepMode) {
            throw new OperationNotPossibleException();
        }
        this.step.signalEvent();
    }

    /**
     *
     * @param e
     *            the Exception to notify
     */
    public void notifyException(final Exception e) {
        e.printStackTrace();
    }

    /**
     *
     * @param ex
     *            the String representation of the Exception to notify
     */
    public void notifyException(final String ex) {
        System.err.println(ex);
    }

    /**
     *
     * @return wether there are environmental events still to process (at least
     *         one)
     */
    public boolean pendingEnvEvents() {
        synchronized (this.inputEnvEvents) {
            return this.inputEnvEvents.size() > 0;
        }
    }

    /**
     * Tests if there are pending input events
     *
     * The method tests in there are input events to be processed (or rather if
     * the input event queue is not empty)
     *
     * @return wether there are input events still to process (at least one)
     */
    public boolean pendingEvents() {
        synchronized (this.inputEvents) {
            return this.inputEvents.size() > 0;
        }
    }

    /**
     * Gets all the tuples of the tuple centre matching the TupleTemplate t
     * without removing them
     *
     * @param t
     *            the tuple template to be used
     * @return the list of tuples result of the operation
     */
    public abstract List<Tuple> readAllTuples(TupleTemplate t);

    /**
     *
     * @param templateArgument
     *            the tuple template to be used
     * @return the tuple representation of the ReSpecT specification
     */
    public abstract Tuple readMatchingSpecTuple(TupleTemplate templateArgument);

    /**
     * Gets (not deterministically) without removing from the tuple set a tuple
     * that matches with the provided tuple template
     *
     * @param t
     *            the tuple template that must be matched by the tuple
     * @return a tuple matching the tuple template
     */
    public abstract Tuple readMatchingTuple(TupleTemplate t);

    /**
     * Gets a tuple from tuple space in a non deterministic way
     *
     * @param t
     *            the tuple template to be used
     * @return the tuple result of the operation
     */
    public abstract Tuple readUniformTuple(TupleTemplate t);

    /**
     *
     * @param templateArgument
     *            the tuple template to be used
     * @return the tuple representation of the ReSpecT specification
     */
    public abstract Tuple removeMatchingSpecTuple(TupleTemplate templateArgument);

    /**
     * Removes (not deterministically) from the tuple set a tuple that matches
     * with the provided tuple template
     *
     * @param t
     *            the tuple template that must be matched by the tuple
     * @param u
     *            a flag indicating wether a persistency update is due
     * @return a tuple matching the tuple template
     */
    public abstract Tuple removeMatchingTuple(TupleTemplate t, boolean u);

    /**
     * Removes the pending queries related to an agent
     *
     * @param id
     *            is the agent identifies
     */
    public abstract void removePendingQueryEventsOf(AgentId id);

    /**
     * Removes a time-triggered reaction, previously fetched
     *
     * @return the time-triggered reaction
     */
    public abstract TriggeredReaction removeTimeTriggeredReaction();

    /**
     * Removes a triggered reaction, previously fetched
     *
     * @return the triggered reaction
     */
    public abstract TriggeredReaction removeTriggeredReaction();

    /**
     * Gets a tuple from tuple space in a non deterministic way
     *
     * @param t
     *            the tuple template to be used
     * @return the tuple result of the operation
     */
    public abstract Tuple removeUniformTuple(TupleTemplate t);

    /**
     * Resets the tuple centre vm core.
     */
    public abstract void reset();

    /**
     *
     * @param tupleList
     *            the list of tuples representing ReSpecT specification argument
     *            of the operation
     */
    public abstract void setAllSpecTuples(List<Tuple> tupleList);

    /**
     * Gets all the tuples of the tuple centre
     *
     * @param tupleList
     *            the list of tuples argument of the operation
     */
    public abstract void setAllTuples(List<Tuple> tupleList);

    @Override
    public void setManagementMode(final boolean activate) {
        this.management = activate;
    }

    /**
     *
     * @param t
     *            the tuple representing the computational activity to launch
     * @param owner
     *            the identifier of the owner of the operation
     * @param targetTC
     *            the identifier of the tuple centre target of the operation
     * @return wether the operation succeeded
     */
    public abstract boolean spawnActivity(Tuple t, IId owner, IId targetTC);

    @Override
    public void stopCommand() throws OperationNotPossibleException {
        if (!this.management) {
            throw new OperationNotPossibleException();
        }
    }

    /**
     *
     * @return wether there are some time-triggered ReSpecT reactions
     */
    public abstract boolean timeTriggeredReaction();

    @Override
    public boolean toggleStepMode() {
        if (this.isStepMode()) {
            this.stepMode = false;
            this.step.signalEvent();
            return false;
        }
        this.stepMode = true;
        return true;
    }

    /**
     *
     * @return wether there are some triggered ReSpecT reactions
     */
    public abstract boolean triggeredReaction();

    /**
     *
     * @param tr
     *            the ReSpecT specification to trigger
     */
    public abstract void updateSpecAfterTimedReaction(TriggeredReaction tr);

    /**
     * Specifies how to notify an output event.
     *
     * @param ev
     *            the output event to notify
     */
    protected void notifyOutputEvent(final OutputEvent ev) {
        ev.getSimpleTCEvent().notifyCompletion();
    }

    /**
     *
     */
    protected void setBootTime() {
        this.bootTime = System.currentTimeMillis();
    }
    
    /**
     * 
     * @param t
     *            the floating point precision to set as a tollerance for
     *            proximity check
     */
    protected void setDistanceTollerance(final float t) {
        this.distanceTollerance = Term.createTerm(String.valueOf(t));
    }

    /**
     * 
     * @param dt
     *            the tuProlog term representing the floating point precision to
     *            set as a tollerance for proximity check
     */
    protected void setDistanceTollerance(final Term dt) {
        this.distanceTollerance = dt;
    }

    
    /**
     * 
     */
    protected void setPosition() {
        this.place = new Position();
        final GeolocationServiceManager geolocationManager = GeolocationServiceManager
                .getGeolocationManager();
        if (geolocationManager.getServices().size() > 0) {
            final int platform = PlatformUtils.getPlatform();
            final AbstractGeolocationService geoService = GeolocationServiceManager
                    .getGeolocationManager().getAppositeService(platform);
            if (geoService != null && !geoService.isRunning()) {
                geoService.start();
            }
        }
    }
}
