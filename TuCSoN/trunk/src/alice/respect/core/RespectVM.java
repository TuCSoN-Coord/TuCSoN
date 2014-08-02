/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.respect.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import alice.logictuple.LogicTuple;
import alice.respect.api.IRespectTC;
import alice.respect.api.TupleCentreId;
import alice.respect.api.exceptions.OperationNotPossibleException;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.introspection.WSetEvent;
import alice.tuplecentre.api.IId;
import alice.tuplecentre.api.InspectableEventListener;
import alice.tuplecentre.api.ObservableEventListener;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.core.AbstractBehaviourSpecification;
import alice.tuplecentre.core.AbstractEvent;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.InspectableEvent;

/**
 * RespecT Tuple Centre Virtual Machine.
 * 
 * Defines the core behaviour of a tuple centre virtual machine.
 * 
 * The behaviour reflects the operational semantic expressed in related tuple
 * centre articles.
 * 
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Saverio Cicora
 */
public class RespectVM implements Runnable {
    private final RespectTCContainer container;
    private final RespectVMContext context;
    private final Object idle;
    private final EventMonitor news;
    private final EventMonitor step;
    private boolean stepMode;
    /** listener to VM inspectable events */
    protected List<InspectableEventListener> inspectors;
    /**
     * 
     */
    protected List<ObservableEventListener> observers;

    /**
     * 
     * @param tid
     *            the identifier of the tuple centre this VM should manage
     * @param c
     *            the ReSpecT tuple centres manager this VM should interact with
     * @param qSize
     *            the maximum InQ size
     * @param respectTC
     *            the reference to the ReSpecT tuple centre this VM should
     *            manage
     */
    public RespectVM(final TupleCentreId tid, final RespectTCContainer c,
            final int qSize, final IRespectTC respectTC) {
        this.container = c;
        this.context = new RespectVMContext(this, tid, qSize, respectTC);
        this.news = new EventMonitor();
        this.idle = new Object();
        this.inspectors = new ArrayList<InspectableEventListener>();
        this.observers = new ArrayList<ObservableEventListener>();
        this.stepMode = false;
        this.step = new EventMonitor();
    }

    /**
     * 
     * @param opId
     *            the progressive, unique per tuple centre identifier of an
     *            operation
     * @return wether the operation has been succefully aborted
     */
    public boolean abortOperation(final long opId) {
        boolean res;
        synchronized (this.idle) {
            res = this.context.removePendingQueryEvent(opId);
        }
        return res;
    }

    /**
     * 
     * @param l
     *            the listener of inspectable events to add
     */
    public void addInspector(final InspectableEventListener l) {
        this.inspectors.add(l);
    }

    /**
     * 
     * @param l
     *            the listener of observable events to add
     */
    public void addObserver(final ObservableEventListener l) {
        this.observers.add(l);
    }

    /**
     * @param path
     *            the path where persistency information is stored
     * @param fileName
     *            the name of the file where persistency information is stored
     * 
     */
    public void disablePersistency(final String path,
            final TucsonTupleCentreId fileName) {
        this.context.disablePersistency(path, fileName);
    }

    /**
     * 
     * @param id
     *            the identifier of who is issuing the operation
     * @param op
     *            the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    public void doOperation(final IId id, final RespectOperation op)
            throws OperationNotPossibleException {
        try {
            this.context.doOperation(id, op);
            this.news.signalEvent();
        } catch (final alice.tuplecentre.api.exceptions.OperationNotPossibleException e) {
            throw new OperationNotPossibleException();
        }
    }

    /**
     * @param path
     *            the path where to store persistency information
     * @param fileName
     *            the name of the file to create for storing persistency
     *            information
     * 
     */
    public void enablePersistency(final String path,
            final TucsonTupleCentreId fileName) {
        this.context.enablePersistency(path, fileName);
    }

    /**
     * 
     * @return the ReSpecT tuple centres manager this VM is interacting with
     */
    public RespectTCContainer getContainer() {
        return this.container;
    }

    /**
     * 
     * @return the identifier of the tuple centre this VM is managing
     */
    public TupleCentreId getId() {
        return (TupleCentreId) this.context.getId();
    }

    /**
     * 
     * @return the list of observable events listeners
     */
    public List<ObservableEventListener> getObservers() {
        return this.observers;
    }

    /**
     * 
     * @return the ReSpecT specification used by this ReSpecT VM
     */
    public AbstractBehaviourSpecification getReactionSpec() {
        synchronized (this.idle) {
            return this.context.getReactionSpec();
        }
    }

    /**
     * 
     * @return the ReSpecT VM storage context
     */
    public RespectVMContext getRespectVMContext() {
        return this.context;
    }

    /**
     * 
     * @return ReSpecT triggered reactions set
     */
    public LogicTuple[] getTRSet() {
        return this.context.getTRSet();
    }

    /**
     * 
     * @param filter
     *            the tuple template to be used in filtering tuples
     * @return the list of tuples currently stored
     */
    public LogicTuple[] getTSet(final LogicTuple filter) {
        return this.context.getTSet(filter);
    }

    /**
     * 
     * @param filter
     *            the tuple template to be used in filtering InQ events
     * @return the list of InQ events currently stored
     */
    public WSetEvent[] getWSet(final LogicTuple filter) {
        return this.context.getWSet(filter);
    }

    /**
     * 
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    public void goCommand() throws OperationNotPossibleException {
        try {
            this.context.goCommand();
            this.news.signalEvent();
        } catch (final alice.tuplecentre.api.exceptions.OperationNotPossibleException e) {
            throw new OperationNotPossibleException();
        }
    }

    /**
     * 
     * @return wether this ReSpecT VM has any inspectable events listener
     *         registered
     */
    public boolean hasInspectors() {
        return this.inspectors.size() > 0;
    }

    /**
     * 
     * @return wether this ReSpecT VM has any observable events listener
     *         registered
     */
    public boolean hasObservers() {
        return this.observers.size() > 0;
    }

    public void stepModeCommand(){
    	this.context.setStepMode();
    }

    /**
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    public void nextStepCommand() throws OperationNotPossibleException {
    	//TODO
        /*try {
            this.context.nextStepCommand();
            this.news.signalEvent();
        } catch (final alice.tuplecentre.api.exceptions.OperationNotPossibleException e) {
            throw new OperationNotPossibleException();
        }*/
    }

    /**
     * 
     * @param e
     *            the inpsectable event to notify to listeners
     */
    public void notifyInspectableEvent(final InspectableEvent e) {
        final Iterator<? extends InspectableEventListener> it = this.inspectors
                .iterator();
        while (it.hasNext()) {
            ((InspectableEventListener) it.next()).onInspectableEvent(e);
        }
    }

    /**
     * 
     */
    public void notifyNewInputEvent() {
        this.news.signalEvent();
    }

    /**
     * 
     * @param ev
     *            the observable event to notify to listeners
     */
    public void notifyObservableEvent(final AbstractEvent ev) {
        final int size = this.observers.size();
        final InputEvent e = (InputEvent) ev;
        if (ev.isInput()) {
            if (e.getSimpleTCEvent().isIn()) {
                for (int i = 0; i < size; i++) {
                    this.observers.get(i).inRequested(
                            this.getId(),
                            ev.getSource(),
                            ((RespectOperation) ev.getSimpleTCEvent())
                                    .getLogicTupleArgument());
                }
            } else if (e.getSimpleTCEvent().isInp()) {
                for (int i = 0; i < size; i++) {
                    this.observers.get(i).inpRequested(
                            this.getId(),
                            ev.getSource(),
                            ((RespectOperation) ev.getSimpleTCEvent())
                                    .getLogicTupleArgument());
                }
            } else if (e.getSimpleTCEvent().isRd()) {
                for (int i = 0; i < size; i++) {
                    this.observers.get(i).rdRequested(
                            this.getId(),
                            ev.getSource(),
                            ((RespectOperation) ev.getSimpleTCEvent())
                                    .getLogicTupleArgument());
                }
            } else if (e.getSimpleTCEvent().isRdp()) {
                for (int i = 0; i < size; i++) {
                    this.observers.get(i).rdpRequested(
                            this.getId(),
                            ev.getSource(),
                            ((RespectOperation) ev.getSimpleTCEvent())
                                    .getLogicTupleArgument());
                }
            } else if (e.getSimpleTCEvent().isOut()) {
                for (int i = 0; i < size; i++) {
                    this.observers.get(i).outRequested(
                            this.getId(),
                            ev.getSource(),
                            ((RespectOperation) ev.getSimpleTCEvent())
                                    .getLogicTupleArgument());
                }
            } else if (e.getSimpleTCEvent().isSetS()) {
                for (int i = 0; i < size; i++) {
                    this.observers.get(i).setSpecRequested(
                            this.getId(),
                            ev.getSource(),
                            ((Tuple) ((RespectOperation) ev.getSimpleTCEvent())
                                    .getLogicTupleArgument()).toString());
                }
            } else if (e.getSimpleTCEvent().isGetS()) {
                for (int i = 0; i < size; i++) {
                    this.observers.get(i).getSpecRequested(this.getId(),
                            ev.getSource());
                }
            }
        } else {
            if (e.getSimpleTCEvent().isIn()) {
                for (int i = 0; i < size; i++) {
                    this.observers.get(i).inCompleted(
                            this.getId(),
                            ev.getSource(),
                            ((RespectOperation) ev.getSimpleTCEvent())
                                    .getLogicTupleArgument());
                }
            } else if (e.getSimpleTCEvent().isInp()) {
                for (int i = 0; i < size; i++) {
                    this.observers.get(i).inpCompleted(
                            this.getId(),
                            ev.getSource(),
                            ((RespectOperation) ev.getSimpleTCEvent())
                                    .getLogicTupleArgument());
                }
            } else if (e.getSimpleTCEvent().isRd()) {
                for (int i = 0; i < size; i++) {
                    this.observers.get(i).rdCompleted(
                            this.getId(),
                            ev.getSource(),
                            ((RespectOperation) ev.getSimpleTCEvent())
                                    .getLogicTupleArgument());
                }
            } else if (e.getSimpleTCEvent().isRdp()) {
                for (int i = 0; i < size; i++) {
                    this.observers.get(i).rdpCompleted(
                            this.getId(),
                            ev.getSource(),
                            ((RespectOperation) ev.getSimpleTCEvent())
                                    .getLogicTupleArgument());
                }
            } else if (e.getSimpleTCEvent().isSetS()) {
                for (int i = 0; i < size; i++) {
                    this.observers.get(i).setSpecCompleted(this.getId(),
                            ev.getSource());
                }
            } else if (e.getSimpleTCEvent().isGetS()) {
                for (int i = 0; i < size; i++) {
                    this.observers.get(i).getSpecCompleted(
                            this.getId(),
                            ev.getSource(),
                            ((Tuple) ((RespectOperation) ev.getSimpleTCEvent())
                                    .getLogicTupleArgument()).toString());
                }
            }
        }
    }

    /**
     * @param path
     *            the path where persistency information is stored
     * @param file
     *            the name of the file where persistency information is stored
     * @param tcName
     *            the name of the tuple centre to be recovered
     * 
     */
    public void recoveryPersistent(final String path, final String file,
            final TucsonTupleCentreId tcName) {
        this.context.recoveryPersistent(path, file, tcName);
    }

    /**
     * 
     * @param l
     *            the inspectable events listener to remove
     */
    public void removeInspector(final InspectableEventListener l) {
        this.inspectors.remove(l);
    }

    /**
     * 
     * @param l
     *            the observable events listener to remove
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
        	/*if(this.stepMode){
        		try {
					this.step.awaitEvent();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}*/
            synchronized (this.idle) {
                this.context.execute();
            }
            try {
                if (this.hasInspectors()) {
                    this.notifyInspectableEvent(new InspectableEvent(this,
                            InspectableEvent.TYPE_NEWSTATE));
                }
                if (!(this.context.pendingEvents() || this.context
                        .pendingEnvEvents())) {
                    this.news.awaitEvent();
                }
            } catch (final InterruptedException e) {
                System.out
                        .println("[RespectVM]: Shutdown interrupt received, shutting down...");
                break;
            }
        }
        System.out.println("[RespectVM]: Actually shutting down...");
    }

    /**
     * 
     * @param activate
     *            toggles management mode on and off
     */
    public void setManagementMode(final boolean activate) {
        this.context.setManagementMode(activate);
    }

    /**
     * 
     * @param spec
     *            the ReSpecT specification to overwrite current one with
     * @return wether the ReSpecT speification has been successfully overwritten
     */
    public boolean setReactionSpec(final AbstractBehaviourSpecification spec) {
        synchronized (this.idle) {
            this.context.removeReactionSpec();
            return this.context.setReactionSpec(spec);
        }
    }

    /**
     * 
     * @param wSet
     *            the InQ to overwrite current one with
     */
    public void setWSet(final List<LogicTuple> wSet) {
        this.context.setWSet(wSet);
    }

    /**
     * 
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    public void stopCommand() throws OperationNotPossibleException {
        try {
            this.context.stopCommand();
        } catch (final alice.tuplecentre.api.exceptions.OperationNotPossibleException e) {
            throw new OperationNotPossibleException();
        }
    }
}
