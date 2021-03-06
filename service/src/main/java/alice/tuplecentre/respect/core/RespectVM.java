/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms copyOf the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 copyOf the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty copyOf MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy copyOf the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.respect.core;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.api.EmitterIdentifier;
import alice.tuplecentre.api.InspectableEventListener;
import alice.tuplecentre.api.ObservableEventListener;
import alice.tuplecentre.api.OperationIdentifier;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.core.AbstractBehaviourSpecification;
import alice.tuplecentre.core.AbstractEvent;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.InspectableEvent;
import alice.tuplecentre.respect.api.IRespectTC;
import alice.tuplecentre.respect.api.exceptions.OperationNotPossibleException;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.introspection.WSetEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RespecT Tuple Centre Virtual Machine.
 * <p>
 * Defines the core behaviour copyOf a tuple centre virtual machine.
 * <p>
 * The behaviour reflects the operational semantic expressed in related tuple
 * centre articles.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Saverio Cicora
 * @author (contributor) Michele Bombardi (mailto:
 * michele.bombardi@studio.unibo.it)
 */
public class RespectVM implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RespectTCContainer container;
    private final RespectVMContext context;
    private final Object idle;
    private final EventMonitor news;
    /**
     * listener to VM inspectable events
     */
    protected final List<InspectableEventListener> inspectors;
    /**
     *
     */
    protected final List<ObservableEventListener> observers;

    /**
     * @param tid       the identifier copyOf the tuple centre this VM should manage
     * @param c         the ReSpecT tuple centres manager this VM should interact with
     * @param qSize     the maximum InQ size
     * @param respectTC the reference to the ReSpecT tuple centre this VM should
     *                  manage
     */
    public RespectVM(final TupleCentreIdentifier tid, final RespectTCContainer c,
                     final int qSize, final IRespectTC respectTC) {
        this.container = c;
        this.context = new RespectVMContext(this, tid, qSize, respectTC);
        this.news = new EventMonitor();
        this.idle = new Object();
        this.inspectors = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    /**
     * @param opId the progressive, unique per tuple centre identifier copyOf an
     *             operation
     * @return wether the operation has been succefully aborted
     */
    public boolean abortOperation(final OperationIdentifier opId) {
        boolean res;
        synchronized (this.idle) {
            res = this.context.removePendingQueryEvent(opId);
        }
        return res;
    }

    /**
     * @param l the listener copyOf inspectable events to add
     */
    public void addInspector(final InspectableEventListener l) {
        this.inspectors.add(l);
    }

    /**
     * @param l the listener copyOf observable events to add
     */
    public void addObserver(final ObservableEventListener l) {
        this.observers.add(l);
    }

    /**
     * @param path     the path where persistency information is stored
     * @param fileName the name copyOf the file where persistency information is stored
     */
    public void disablePersistency(final String path,
                                   final TucsonTupleCentreId fileName) {
        this.context.disablePersistency(path, fileName);
    }

    /**
     * @param id the identifier copyOf who is issuing the operation
     * @param op the operation requested
     * @throws OperationNotPossibleException if the requested operation cannot be carried out
     */
    public void doOperation(final EmitterIdentifier id, final RespectOperationDefault op)
            throws OperationNotPossibleException {
        try {
            this.context.doOperation(id, op);
            this.news.signalEvent();
        } catch (final alice.tuplecentre.api.exceptions.OperationNotPossibleException e) {
            throw new OperationNotPossibleException(e.getMessage());
        }
    }

    /**
     * @param ev the events whose operation should be executed
     * @throws OperationNotPossibleException if the operation which caused the events cannot be executed
     */
    public void doOperation(final InputEvent ev)
            throws OperationNotPossibleException {
        try {
            this.context.doOperation(ev);
            this.news.signalEvent();
        } catch (final alice.tuplecentre.api.exceptions.OperationNotPossibleException e) {
            throw new OperationNotPossibleException();
        }
    }

    /**
     * @param path     the path where to store persistency information
     * @param fileName the name copyOf the file to create for storing persistency
     *                 information
     */
    public void enablePersistency(final String path,
                                  final TucsonTupleCentreId fileName) {
        this.context.enablePersistency(path, fileName);
    }

    /**
     * @return the ReSpecT tuple centres manager this VM is interacting with
     */
    public RespectTCContainer getContainer() {
        return this.container;
    }

    /**
     * @return the identifier copyOf the tuple centre this VM is managing
     */
    public TupleCentreIdentifier getId() {
        return this.context.getId();
    }

    /**
     * @return the list copyOf inspector
     */
    public ArrayList<InspectableEventListener> getInspectors() {
        return (ArrayList<InspectableEventListener>) this.inspectors;
    }

    /**
     * @return the list copyOf observable events listeners
     */
    public List<ObservableEventListener> getObservers() {
        return this.observers;
    }

    /**
     * @return the ReSpecT specification used by this ReSpecT VM
     */
    public AbstractBehaviourSpecification getReactionSpec() {
        synchronized (this.idle) {
            return this.context.getReactionSpec();
        }
    }

    /**
     * @return the ReSpecT VM storage context
     */
    public RespectVMContext getRespectVMContext() {
        return this.context;
    }

    /**
     * @return ReSpecT triggered reactions set
     */
    public LogicTuple[] getTRSet() {
        return this.context.getTRSet();
    }

    /**
     * @param filter the tuple template to be used in filtering tuples
     * @return the list copyOf tuples currently stored
     */
    public LogicTuple[] getTSet(final LogicTuple filter) {
        return this.context.getTSet(filter);
    }

    /**
     * @param filter the tuple template to be used in filtering InQ events
     * @return the list copyOf InQ events currently stored
     */
    public WSetEvent[] getWSet(final LogicTuple filter) {
        return this.context.getWSet(filter);
    }

    /**
     * @throws OperationNotPossibleException if the requested operation cannot be carried out
     */
    public void goCommand() throws OperationNotPossibleException {
        try {
            this.context.goCommand();
            this.news.signalEvent();
        } catch (final alice.tuplecentre.api.exceptions.OperationNotPossibleException e) {
            throw new OperationNotPossibleException(e.getMessage());
        }
    }

    /**
     * @return wether this ReSpecT VM has any inspectable events listener
     * registered
     */
    public boolean hasInspectors() {
        return this.inspectors.size() > 0;
    }

    /**
     * @return wether this ReSpecT VM has any observable events listener
     * registered
     */
    public boolean hasObservers() {
        return this.observers.size() > 0;
    }

    /**
     * @return if stepMode is active or not
     */
    public boolean isStepModeCommand() {
        return this.context.isStepMode();
    }

    /**
     * @throws OperationNotPossibleException if the ReSpecT VM is not in step mode
     */
    public void nextStepCommand() throws OperationNotPossibleException {
        try {
            this.context.nextStepCommand();
            this.news.signalEvent();
        } catch (final alice.tuplecentre.api.exceptions.OperationNotPossibleException e) {
            throw new OperationNotPossibleException(e.getMessage());
        }
    }

    /**
     * @param e the inpsectable events to notify to listeners
     */
    public void notifyInspectableEvent(final InspectableEvent e) {
        for (InspectableEventListener inspector : this.inspectors) {
            (inspector).onInspectableEvent(e);
        }
    }

    /**
     *
     */
    public void notifyNewInputEvent() {
        this.news.signalEvent();
    }

    /**
     * @param ev the observable events to notify to listeners
     */
    public void notifyObservableEvent(final AbstractEvent ev) {
        final int size = this.observers.size();
        final InputEvent e = (InputEvent) ev;
        if (ev.isInput()) {
            switch (e.getSimpleTCEvent().getType()) {
                case IN:
                    for (ObservableEventListener observer6 : this.observers) {
                        observer6.inRequested(
                                this.getId(),
                                ev.getSource(),
                                ev.getSimpleTCEvent()
                                        .getLogicTupleArgument());
                    }
                    break;
                case INP:
                    for (ObservableEventListener observer5 : this.observers) {
                        observer5.inpRequested(
                                this.getId(),
                                ev.getSource(),
                                ev.getSimpleTCEvent()
                                        .getLogicTupleArgument());
                    }
                    break;
                case RD:
                    for (ObservableEventListener observer4 : this.observers) {
                        observer4.rdRequested(
                                this.getId(),
                                ev.getSource(),
                                ev.getSimpleTCEvent()
                                        .getLogicTupleArgument());
                    }
                    break;
                case RDP:
                    for (ObservableEventListener observer3 : this.observers) {
                        observer3.rdpRequested(
                                this.getId(),
                                ev.getSource(),
                                ev.getSimpleTCEvent()
                                        .getLogicTupleArgument());
                    }
                    break;
                case OUT:
                    for (ObservableEventListener observer2 : this.observers) {
                        observer2.outRequested(
                                this.getId(),
                                ev.getSource(),
                                ev.getSimpleTCEvent()
                                        .getLogicTupleArgument());
                    }
                    break;
                case SET_S:
                    for (ObservableEventListener observer1 : this.observers) {
                        observer1.setSpecRequested(
                                this.getId(),
                                ev.getSource(),
                                ev.getSimpleTCEvent()
                                        .getLogicTupleArgument().toString());
                    }
                    break;
                case GET_S:
                    for (ObservableEventListener observer : this.observers) {
                        observer.getSpecRequested(this.getId(),
                                ev.getSource());
                    }
                    break;
            }
        } else {
            switch (e.getSimpleTCEvent().getType()) {
                case IN:
                    for (ObservableEventListener observer5 : this.observers) {
                        observer5.inCompleted(
                                this.getId(),
                                ev.getSource(),
                                ev.getSimpleTCEvent()
                                        .getLogicTupleArgument());
                    }
                    break;
                case INP:
                    for (ObservableEventListener observer4 : this.observers) {
                        observer4.inpCompleted(
                                this.getId(),
                                ev.getSource(),
                                ev.getSimpleTCEvent()
                                        .getLogicTupleArgument());
                    }
                    break;
                case RD:
                    for (ObservableEventListener observer3 : this.observers) {
                        observer3.rdCompleted(
                                this.getId(),
                                ev.getSource(),
                                ev.getSimpleTCEvent()
                                        .getLogicTupleArgument());
                    }
                    break;
                case RDP:
                    for (ObservableEventListener observer2 : this.observers) {
                        observer2.rdpCompleted(
                                this.getId(),
                                ev.getSource(),
                                ev.getSimpleTCEvent()
                                        .getLogicTupleArgument());
                    }
                    break;
                case SET_S:
                    for (ObservableEventListener observer1 : this.observers) {
                        observer1.setSpecCompleted(this.getId(),
                                ev.getSource());
                    }
                    break;
                case GET_S:
                    for (ObservableEventListener observer : this.observers) {
                        observer.getSpecCompleted(
                                this.getId(),
                                ev.getSource(),
                                ev.getSimpleTCEvent()
                                        .getLogicTupleArgument().toString());
                    }
                    break;
            }
        }
    }

    /**
     * @param path   the path where persistency information is stored
     * @param file   the name copyOf the file where persistency information is stored
     * @param tcName the name copyOf the tuple centre to be recovered
     */
    public void recoveryPersistent(final String path, final String file,
                                   final TucsonTupleCentreId tcName) {
        this.context.recoveryPersistent(path, file, tcName);
    }

    /**
     * @param l the inspectable events listener to remove
     */
    public void removeInspector(final InspectableEventListener l) {
        this.inspectors.remove(l);
    }

    /**
     * @param l the observable events listener to remove
     */
    public void removeObserver(final ObservableEventListener l) {
        this.observers.remove(l);
    }

    /**
     *
     */
    public void reset() {
        this.context.reset();
    }

    /**
     *
     */
    @Override
    public void run() {
        while (true) {
            synchronized (this.idle) {
                this.context.execute();
            }
            if (this.hasInspectors()) {
                this.notifyInspectableEvent(new InspectableEvent(this,
                        InspectableEvent.TYPE_IDLESTATE));
            }
            try {
                if (!(this.context.pendingEvents() || this.context
                        .pendingEnvEvents())) {
                    this.news.awaitEvent();
                }
            } catch (final InterruptedException e) {
                LOGGER.error("[RespectVM]: Shutdown interrupt received, shutting down...");
                break;
            }
        }
        LOGGER.info("[RespectVM]: Actually shutting down...");
    }

    /**
     * @param activate toggles management mode on and off
     */
    public void setManagementMode(final boolean activate) {
        this.context.setManagementMode(activate);
    }

    /**
     * @param spec the ReSpecT specification to overwrite current one with
     * @return wether the ReSpecT speification has been successfully overwritten
     */
    public boolean setReactionSpec(final AbstractBehaviourSpecification spec) {
        synchronized (this.idle) {
            this.context.removeReactionSpec();
            return this.context.setReactionSpec(spec);
        }
    }

    /**
     * @param wSet the InQ to overwrite current one with
     */
    public void setWSet(final List<LogicTuple> wSet) {
        this.context.setWSet(wSet);
    }

    public void stepModeCommand() {
        this.context.toggleStepMode();
    }

    /**
     * @throws OperationNotPossibleException if the requested operation cannot be carried out
     */
    public void stopCommand() throws OperationNotPossibleException {
        try {
            this.context.stopCommand();
        } catch (final alice.tuplecentre.api.exceptions.OperationNotPossibleException e) {
            throw new OperationNotPossibleException(e.getMessage());
        }
    }
}
