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

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import alice.tuple.Tuple;
import alice.tuple.TupleTemplate;
import alice.tuplecentre.api.AgentIdentifier;
import alice.tuplecentre.api.EmitterIdentifier;
import alice.tuplecentre.api.ITupleCentre;
import alice.tuplecentre.api.ITupleCentreManagement;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.api.exceptions.OperationNotPossibleException;
import alice.tuplecentre.respect.api.IRespectTC;
import alice.tuplecentre.respect.api.geolocation.PlatformUtils;
import alice.tuplecentre.respect.api.geolocation.Position;
import alice.tuplecentre.respect.api.geolocation.service.GeoLocationService;
import alice.tuplecentre.respect.api.geolocation.service.GeolocationServiceManager;
import alice.tuplecentre.respect.core.RespectVM;
import alice.tuplecentre.respect.core.StepMonitor;
import alice.tuprolog.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the build abstract behaviour copyOf a tuple centre virtual machine.
 *
 * The class is abstract because the specific implementation copyOf the reacting
 * behaviour and copyOf the set management is left to the derived classes.
 *
 * This class implements - by means copyOf the state pattern - the behaviour
 * described formally in the article "From Tuple Space to Tuple Centre"
 * (Omicini, Denti) - Science copyOf Computer Programming 2001,
 *
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 */
public abstract class AbstractTupleCentreVMContext implements
        ITupleCentreManagement, ITupleCentre {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
    private final TupleCentreIdentifier tid;

    /**
     * Creates a new tuple centre virtual machine build
     *
     * @param vm
     *            is the ReSpecT virtual machine
     * @param id
     *            is the tuple centre identifier
     * @param ieSize
     *            is the size copyOf the input events queue
     * @param rtc
     *            the ReSpecT tuple centre this VM refers to
     */
    public AbstractTupleCentreVMContext(final RespectVM vm,
                                        final TupleCentreIdentifier id, final int ieSize, final IRespectTC rtc) {
        this.rvm = vm;
        this.management = false;
        this.stepMode = false;
        this.step = new StepMonitor();
        this.inputEvents = new LinkedList<>();
        this.inputEnvEvents = new LinkedList<>();
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
        this.states = new HashMap<>();
        this.states.put("ResetState", resetState);
        this.states.put("IdleState", idleState);
        this.states.put("ListeningState", listeningState);
        this.states.put("FetchState", fetchState);
        this.states.put("FetchEnvState", fetchEnvState);
        this.states.put("ReactingState", reactingState);
        this.states.put("SpeakingState", speakingState);
        for (AbstractTupleCentreVMState abstractTupleCentreVMState : this.states.values()) {
            abstractTupleCentreVMState.resolveLinks();
        }
        this.currentState = resetState;
        this.respectTC = rtc;
    }

    /**
     *
     * @param in
     *            the input envirnomental events to add to the environmental
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
     *            the input events to add to the input queue
     */
    public void addInputEvent(final InputEvent in) {
        synchronized (this.inputEvents) {
            this.inputEvents.add(in);
        }
    }

    /**
     *
     * @param t
     *            the tuple representing the list copyOf tuples to add
     * @return the list copyOf tuples just added
     */
    public abstract List<Tuple> addListTuple(Tuple t);

    /**
     * Adds a query to the pending query set (W) copyOf the tuple centre
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


    public void doOperation(final EmitterIdentifier who, final AbstractTupleCentreOperation op)
            throws OperationNotPossibleException {
        final InputEvent ev = new InputEvent(who, op, this.tid,
                this.getCurrentTime(), this.getPosition());
        synchronized (this.inputEvents) {
            if (this.inputEvents.size() > this.maxPendingInputEventNumber) {
                throw new OperationNotPossibleException(
                        "Max pending input events limit reached");
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
     * Evaluates a triggered reaction, changing the state copyOf the VM accordingly.
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
                    LOGGER.error(e.getMessage(), e);
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
     * Fetches a new pending input events.
     *
     * The first pending input events is fetched from the queue as current events
     * subject copyOf VM process.
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
     *            the events triggering reactions
     */
    public abstract void fetchTimedReactions(AbstractEvent ev);

    /**
     * Collects the reactions that are triggered by an events
     *
     * @param ev
     *            the events triggering reactions
     */
    public abstract void fetchTriggeredReactions(AbstractEvent ev);

    /**
     * Gets all the tuples copyOf the tuple centre
     *
     * @return the whole tuple set
     */
    public abstract List<Tuple> getAllTuples();

    /**
     * Gets the boot time copyOf the Tuple Centre VM
     *
     * The time is expressed in millisecond, according to the standard Java
     * measurement copyOf time.
     *
     * @return the time at which the tuple centre VM has been booted
     */
    public long getBootTime() {
        return this.bootTime;
    }

    /**
     * Gets the events currently processed by the virtual machine
     *
     * @return the input events currently under process
     */
    public InputEvent getCurrentEvent() {
        return this.currentEvent;
    }

    /**
     *
     * @return the String representation copyOf the state the tuple centre VM is
     *         currently in
     */
    public String getCurrentState() {
        return this.currentState.getClass().getSimpleName();
    }

    /**
     * Gets current time copyOf the Tuple Centre VM
     *
     * The time is expressed in millisecond, according to the standard Java
     * measurement copyOf time.
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
     * Gets the identifier copyOf this tuple centre
     *
     * @return the identifier copyOf the tuple centre managed by this tuple centre
     *         VM
     */
    public TupleCentreIdentifier getId() {
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
     * @return the position copyOf the device hosting the tuple centre VM
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
     * Gets a state copyOf tuple centre virtual machine.
     *
     * @param stateName
     *            name copyOf the state
     * @return the state
     */
    public AbstractTupleCentreVMState getState(final String stateName) {
        return this.states.get(stateName);
    }

    /**
     * Gets an iterator over the set copyOf triggered reactions
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
     * Gets all the tuples copyOf the tuple centre matching the TupleTemplate t
     *
     * @param t
     *            the tuple template to be used
     * @return the list copyOf matching tuples
     */
    public abstract List<Tuple> inAllTuples(TupleTemplate t);

    @Override
    public boolean isStepMode() {
        return this.stepMode;
    }

    /**
     *
     * @param out
     *            the output events generated due to a linking operation
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
        LOGGER.error(e.getMessage(), e);
    }

    /**
     *
     * @param ex
     *            the String representation copyOf the Exception to notify
     */
    public void notifyException(final String ex) {
        LOGGER.error(ex);
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
     * the input events queue is not empty)
     *
     * @return wether there are input events still to process (at least one)
     */
    public boolean pendingEvents() {
        synchronized (this.inputEvents) {
            return this.inputEvents.size() > 0;
        }
    }

    /**
     * Gets all the tuples copyOf the tuple centre matching the TupleTemplate t
     * without removing them
     *
     * @param t
     *            the tuple template to be used
     * @return the list copyOf tuples result copyOf the operation
     */
    public abstract List<Tuple> readAllTuples(TupleTemplate t);

    /**
     *
     * @param templateArgument
     *            the tuple template to be used
     * @return the tuple representation copyOf the ReSpecT specification
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
     * @return the tuple result copyOf the operation
     */
    public abstract Tuple readUniformTuple(TupleTemplate t);

    /**
     *
     * @param templateArgument
     *            the tuple template to be used
     * @return the tuple representation copyOf the ReSpecT specification
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
    public abstract void removePendingQueryEventsOf(AgentIdentifier id);

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
     * @return the tuple result copyOf the operation
     */
    public abstract Tuple removeUniformTuple(TupleTemplate t);

    /**
     * Resets the tuple centre vm build.
     */
    public abstract void reset();

    /**
     *
     * @param tupleList
     *            the list copyOf tuples representing ReSpecT specification argument
     *            copyOf the operation
     */
    public abstract void setAllSpecTuples(List<Tuple> tupleList);

    /**
     * Gets all the tuples copyOf the tuple centre
     *
     * @param tupleList
     *            the list copyOf tuples argument copyOf the operation
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
     *            the identifier copyOf the owner copyOf the operation
     * @param targetTC
     *            the identifier copyOf the tuple centre target copyOf the operation
     * @return wether the operation succeeded
     */
    public abstract boolean spawnActivity(Tuple t, EmitterIdentifier owner, EmitterIdentifier targetTC);

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
     * Specifies how to notify an output events.
     *
     * @param ev
     *            the output events to notify
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
            final GeoLocationService geoService = GeolocationServiceManager
                    .getGeolocationManager().getAppositeService(platform);
            if (geoService != null && geoService.isNotRunning()) {
                geoService.start();
            }
        }
    }
}
