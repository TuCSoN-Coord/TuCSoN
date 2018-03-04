package alice.tucson.service;

import java.util.HashMap;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.respect.api.IEnvironmentContext;
import alice.respect.api.IManagementContext;
import alice.respect.api.IOrdinaryAsynchInterface;
import alice.respect.api.IOrdinarySynchInterface;
import alice.respect.api.ISpecificationAsynchInterface;
import alice.respect.api.ISpecificationSynchInterface;
import alice.respect.api.RespectSpecification;
import alice.respect.api.TupleCentreId;
import alice.respect.api.exceptions.InvalidSpecificationException;
import alice.respect.api.exceptions.InvalidTupleCentreIdException;
import alice.respect.api.exceptions.OperationNotPossibleException;
import alice.respect.core.InternalEvent;
import alice.respect.core.InternalOperation;
import alice.respect.core.RespectOperationDefault;
import alice.respect.core.RespectTC;
import alice.respect.core.RespectTCContainer;
import alice.respect.core.SpecificationSynchInterface;
import alice.respect.core.TransducersManager;
import alice.respect.situatedness.TransducerId;
import alice.respect.situatedness.TransducerStandardInterface;
import alice.tucson.api.TucsonAgentIdDefault;
import alice.tucson.api.TucsonTupleCentreIdDefault;
import alice.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tucson.api.exceptions.TucsonInvalidSpecificationException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.InspectableEventListener;
import alice.tuplecentre.api.ObservableEventListener;
import alice.tuplecentre.api.TupleCentreOperation;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.OperationCompletionListener;
import alice.tuplecentre.core.TupleCentreOpType;

import static alice.tuplecentre.core.TupleCentreOpType.*;

/**
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 * michele.bombardi@studio.unibo.it)
 */
public final class TupleCentreContainer {
    private static int defaultport;

    /**
     * @param id      the identifier of the tuple centre this wrapper refers to
     * @param q       the size of the input queue
     * @param defPort the default listening port
     * @return the Object representing the ReSpecT tuple centre
     * @throws InvalidTupleCentreIdException if the given tuple centre identifier is not a valid TuCSoN
     *                                       tuple centre identifier
     */
    public static RespectTC createTC(final TucsonTupleCentreIdDefault id, final int q,
                                     final int defPort) throws InvalidTupleCentreIdException {
        TupleCentreContainer.defaultport = defPort;
        try {
            final RespectTCContainer rtcc = RespectTCContainer
                    .getRespectTCContainer();
            RespectTCContainer.setDefPort(TupleCentreContainer.defaultport);
            final TupleCentreId tid = new TupleCentreId(id.getLocalName(),
                    id.getNode(), String.valueOf(id.getPort()));
            return rtcc.createRespectTC(tid, q);
        } catch (final InvalidTupleCentreIdException e) {
            throw new InvalidTupleCentreIdException();
        }
    }

    /**
     *
     */
    public static synchronized void destroyTC() {
        /*
         *
         */
    }

    /**
     *
     */
    public static synchronized void disablePersistence() {
        /*
         *
         */
    }

    public static Object doBlockingOperation(final InputEvent ev)
            throws TucsonInvalidLogicTupleException,
            TucsonOperationNotPossibleException {
        IOrdinarySynchInterface context = null;
        final TupleCentreOpType type = ev.getSimpleTCEvent().getType();
        final TucsonTupleCentreIdDefault tid = (TucsonTupleCentreIdDefault) ev.getTarget();
        try {
            context = RespectTCContainer.getRespectTCContainer()
                    .getOrdinarySynchInterface(tid.getInternalTupleCentreId());
            switch (type) {
                case GET:
                    return context.get(ev);
                case SET:
                    return context.set(ev);
                case OUT:
                    context.out(ev); //TODO to harmonize code, make context.out() return something!!!
                    return ev.getSimpleTCEvent().getTupleArgument();
                case IN:
                    return context.in(ev);
                case INP:
                    return context.inp(ev);
                case RD:
                    return context.rd(ev);
                case RDP:
                    return context.rdp(ev);
                case NO:
                    return context.no(ev);
                case NOP:
                    return context.nop(ev);
                case OUT_ALL:
                    return context.outAll(ev);
                case IN_ALL:
                    return context.inAll(ev);
                case RD_ALL:
                    return context.rdAll(ev);
                case NO_ALL:
                    return context.noAll(ev);
                case UIN:
                    return context.uin(ev);
                case URD:
                    return context.urd(ev);
                case UNO:
                    return context.uno(ev);
                case UINP:
                    return context.uinp(ev);
                case URDP:
                    return context.urdp(ev);
                case UNOP:
                    return context.unop(ev);
            }
        } catch (final InvalidLogicTupleException e) {
            throw new TucsonInvalidLogicTupleException();
        } catch (final OperationNotPossibleException e) {
            throw new TucsonOperationNotPossibleException();
        }
        return null;
    }

    public static Object doBlockingSpecOperation(final InputEvent ev)
            throws TucsonOperationNotPossibleException,
            TucsonInvalidSpecificationException { // FIXME still useful?
        final LogicTuple res = null;
        ISpecificationSynchInterface context = null;
        final TupleCentreOpType type = ev.getSimpleTCEvent().getType();
        final TucsonTupleCentreIdDefault tid = (TucsonTupleCentreIdDefault) ev.getTarget();
        final LogicTuple t = (LogicTuple) ev.getTuple();
        try {
            context = RespectTCContainer.getRespectTCContainer()
                    .getSpecificationSynchInterface(
                            tid.getInternalTupleCentreId());
            if (type == SET_S) {
                if ("spec".equals(t.getName())) {
                    return ((SpecificationSynchInterface) context)
                            .setS(new RespectSpecification(t.getArg(0)
                                    .getName()), ev);
                }
                return context.setS(t, ev);
            }
            if (type == GET_S) {
                return context.getS(ev);
            }
        } catch (final OperationNotPossibleException e) {
            throw new TucsonOperationNotPossibleException();
        } catch (final InvalidSpecificationException e) {
            throw new TucsonInvalidSpecificationException();
        }
        return res;
    }

    public static Object doBlockingSpecOperation(final InputEvent ev,
                                                 final LogicTuple t) throws TucsonOperationNotPossibleException,
            TucsonInvalidSpecificationException {
        final LogicTuple res = null;
        ISpecificationSynchInterface context = null;
        final TupleCentreOpType type = ev.getSimpleTCEvent().getType();
        final TucsonTupleCentreIdDefault tid = (TucsonTupleCentreIdDefault) ev.getTarget();
        try {
            context = RespectTCContainer.getRespectTCContainer()
                    .getSpecificationSynchInterface(
                            tid.getInternalTupleCentreId());
            if (type == SET_S) {
                if ("spec".equals(t.getName())) {
                    return ((SpecificationSynchInterface) context)
                            .setS(new RespectSpecification(t.getArg(0)
                                    .getName()), ev);
                }
                return context.setS(t, ev);
            }
            if (type == GET_S) {
                return context.getS(ev);
            }
        } catch (final OperationNotPossibleException e) {
            throw new TucsonOperationNotPossibleException();
        } catch (final InvalidSpecificationException e) {
            throw new TucsonInvalidSpecificationException();
        }
        return res;
    }

    /**
     * @param opType the type code of the ReSpecT operation to be executed
     * @param aid    the identifier of the TuCSoN agent requesting the operation
     * @param tid    the identifier of the tuple centre target of the operation
     * @param t      the tuple argument of the operation
     * @param l      the listener for operation completion
     * @return the Java object representing the tuple centre operation
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be performed for some
     *                                             reason
     * @throws UnreachableNodeException            if the TuCSoN tuple centre target of the notification cannot
     *                                             be reached over the network
     * @throws OperationTimeOutException           if the notification operation expires timeout
     */
    public static TupleCentreOperation doEnvironmentalOperation(
            final TupleCentreOpType opType, final TucsonAgentIdDefault aid,
            final TucsonTupleCentreIdDefault tid, final LogicTuple t,
            final OperationCompletionListener l)
            throws OperationTimeOutException,
            TucsonOperationNotPossibleException, UnreachableNodeException {
        IEnvironmentContext context = null;
        RespectOperationDefault op = null;
        context = RespectTCContainer.getRespectTCContainer()
                .getEnvironmentContext(tid.getInternalTupleCentreId());
        if (opType == GET_ENV) {
            op = RespectOperationDefault.makeGetEnv(t, l);
        } else if (opType == SET_ENV) {
            op = RespectOperationDefault.makeSetEnv(t, l);
        }
        // Preparing the input event for the tuple centre.
        final HashMap<String, String> eventMap = new HashMap<String, String>();
        eventMap.put("id", aid.toString());
        InputEvent event = null;
        final TransducersManager tm = TransducersManager.INSTANCE;
        TransducerStandardInterface transducer = tm.getTransducer(aid
                .getLocalName());
        if (t != null) {
            // It's an event performed by a transducer. In other words, it's an
            // environment event
            event = new InputEvent(transducer.getIdentifier(), op,
                    tid.getInternalTupleCentreId(), context.getCurrentTime(), null,
                    eventMap);
            // Sending the event
            event.setSource(transducer.getIdentifier());
            event.setTarget(tid.getInternalTupleCentreId());
            context.notifyInputEnvEvent(event);
        } else {
            // It's an agent request of environment properties
            event = new InputEvent(aid, op, tid.getInternalTupleCentreId(),
                    context.getCurrentTime(), null, eventMap);
            final InternalEvent internalEv = new InternalEvent(event,
                    InternalOperation.makeGetEnv(t));
            internalEv.setSource(tid.getInternalTupleCentreId()); // Set
            // the source of the event
            final TransducerId[] tIds = tm.getTransducerIds(tid
                    .getInternalTupleCentreId());
            for (final TransducerId tId2 : tIds) {
                internalEv.setTarget(tId2); // Set target resource
                transducer = tm.getTransducer(tId2.getLocalName());
                transducer.notifyOutput(internalEv);
            }
        }
        return op;
    }

    /**
     * @param type the type codeof the ReSpecT operation to be executed
     * @param aid  the identifier of the tuple centre requesting the operation
     * @param tid  the identifier of the tuple centre target of the operation
     * @param t    the tuple argument of the operation
     * @param l    the listener for operation completion
     * @return the Java object representing the tuple centre operation
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be performed for some
     *                                             reason
     * @throws UnreachableNodeException            if the TuCSoN tuple centre target of the notification cannot
     *                                             be reached over the network
     * @throws OperationTimeOutException           if the notification operation expires timeout
     */
    public static TupleCentreOperation doEnvironmentalOperation(
            final TupleCentreOpType type, final TucsonTupleCentreIdDefault aid,
            final TucsonTupleCentreIdDefault tid, final LogicTuple t,
            final OperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        IEnvironmentContext context = null;
        RespectOperationDefault op = null;
        context = RespectTCContainer.getRespectTCContainer()
                .getEnvironmentContext(tid.getInternalTupleCentreId());
        if (type == GET_ENV) {
            op = RespectOperationDefault.makeGetEnv(t, l);
        } else if (type == SET_ENV) {
            op = RespectOperationDefault.makeSetEnv(t, l);
        }
        // Preparing the input event for the tuple centre.
        final HashMap<String, String> eventMap = new HashMap<String, String>();
        eventMap.put("id", aid.toString());
        InputEvent event = new InputEvent(aid, op,
                tid.getInternalTupleCentreId(), context.getCurrentTime(), null,
                eventMap);
        TransducerStandardInterface transducer;
        event = new InputEvent(aid, op, tid.getInternalTupleCentreId(),
                context.getCurrentTime(), null, eventMap);
        final InternalEvent internalEv = new InternalEvent(event,
                InternalOperation.makeGetEnv(t));
        internalEv.setSource(tid.getInternalTupleCentreId()); // Set
        final TransducersManager tm = TransducersManager.INSTANCE;
        // the source of the event
        final TransducerId[] tIds = tm.getTransducerIds(tid
                .getInternalTupleCentreId());
        for (final TransducerId tId2 : tIds) {
            internalEv.setTarget(tId2); // Set target resource
            transducer = tm.getTransducer(tId2.getLocalName());
            transducer.notifyOutput(internalEv);
        }
        return op;
    }


    /**
     * @param type the type code of the operation requested
     * @param tid  the identifier of the tuple centre target of the operation
     * @param obj  the argument of the management operation
     * @return the result of the operation
     */
    public static Object doManagementOperation(final TupleCentreOpType type,
                                               final TucsonTupleCentreIdDefault tid, final Object obj) {
        IManagementContext context = null;
        context = RespectTCContainer.getRespectTCContainer()
                .getManagementContext(tid.getInternalTupleCentreId());
        switch (type) {
            case ABORT:
                return context.abortOperation((Long) obj);
            case SET_S:
                try {
                    context.setSpec(new RespectSpecification(((LogicTuple) obj)
                            .getArg(0).getName()));
                    return true;
                } catch (final InvalidSpecificationException e) {
                    e.printStackTrace();
                    return false;
                } catch (final InvalidOperationException e) {
                    e.printStackTrace();
                    return false;
                }
            case GET_S:
                return new LogicTuple(context.getSpec().toString());
            case GET_TRSET:
                return context.getTRSet((LogicTuple) obj);
            case GET_TSET:
                return context.getTSet((LogicTuple) obj);
            case GET_WSET:
                return context.getWSet((LogicTuple) obj);
            case GO_CMD:
                try {
                    context.goCommand();
                    return true;
                } catch (final OperationNotPossibleException e) {
                    e.printStackTrace();
                    return false;
                }
            case STOP_CMD:
                try {
                    context.stopCommand();
                    return true;
                } catch (final OperationNotPossibleException e) {
                    e.printStackTrace();
                    return false;
                }
            case IS_STEP_MODE:
                return context.isStepModeCommand();
            case STEP_MODE:
                context.stepModeCommand();
                return true;
            case NEXT_STEP:
                try {
                    context.nextStepCommand();
                    return true;
                } catch (final OperationNotPossibleException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return false;
                }
            case ADD_OBS:
                context.addObserver((ObservableEventListener) obj);
                return true;
            case RMV_OBS:
                context.removeObserver((ObservableEventListener) obj);
                return true;
            case HAS_OBS:
                return context.hasObservers();
            case ADD_INSP:
                context.addInspector((InspectableEventListener) obj);
                return true;
            case RMV_INSP:
                context.removeInspector((InspectableEventListener) obj);
                return true;
            case GET_INSPS:
                return context.getInspectors();
            case HAS_INSP:
                return context.hasInspectors();

            // I don't think this is finished... (here before 23-02-18 Siboni refactor)
            case RESET:
                return context.hasInspectors();
        }
        return null;
    }

    public static TupleCentreOperation doNonBlockingOperation(
            final InputEvent ev) throws TucsonInvalidLogicTupleException,
            TucsonOperationNotPossibleException {
        final TupleCentreOperation res = null;
        IOrdinaryAsynchInterface context = null;
        final TupleCentreOpType type = ev.getSimpleTCEvent().getType();
        final TucsonTupleCentreIdDefault tid = (TucsonTupleCentreIdDefault) ev.getTarget();
        try {
            context = RespectTCContainer.getRespectTCContainer()
                    .getOrdinaryAsynchInterface(tid.getInternalTupleCentreId());

            switch (type) {
                case SPAWN:
                    return context.spawn(ev);
                case OUT:
                    return context.out(ev);
                case IN:
                    return context.in(ev);
                case INP:
                    return context.inp(ev);
                case RD:
                    return context.rd(ev);
                case RDP:
                    return context.rdp(ev);
                case NO:
                    return context.no(ev);
                case NOP:
                    return context.nop(ev);
                case GET:
                    return context.get(ev);
                case SET:
                    return context.set(ev);
                case OUT_ALL:
                    return context.outAll(ev);
                case IN_ALL:
                    return context.inAll(ev);
                case RD_ALL:
                    return context.rdAll(ev);
                case NO_ALL:
                    return context.noAll(ev);
                case UIN:
                    return context.uin(ev);
                case UINP:
                    return context.uinp(ev);
                case URD:
                    return context.urd(ev);
                case URDP:
                    return context.urdp(ev);
                case UNO:
                    return context.uno(ev);
                case UNOP:
                    return context.unop(ev);
            }
        } catch (final InvalidLogicTupleException e) {
            throw new TucsonInvalidLogicTupleException();
        } catch (final OperationNotPossibleException e) {
            throw new TucsonOperationNotPossibleException();
        }
        return res;
    }

    public static TupleCentreOperation doNonBlockingSpecOperation(
            final InputEvent ev) throws TucsonInvalidLogicTupleException,
            TucsonOperationNotPossibleException {
        final TupleCentreOperation res = null;
        ISpecificationAsynchInterface context = null;
        final TupleCentreOpType type = ev.getSimpleTCEvent().getType();
        final TucsonTupleCentreIdDefault tid = (TucsonTupleCentreIdDefault) ev.getTarget();
        final LogicTuple t = (LogicTuple) ev.getTuple();
        try {
            context = RespectTCContainer.getRespectTCContainer()
                    .getSpecificationAsynchInterface(
                            tid.getInternalTupleCentreId());
            switch (type) {
                case NO_S:
                    return context.noS(ev);
                case NOP_S:
                    return context.nopS(ev);
                case OUT_S:
                    return context.outS(ev);
                case IN_S:
                    return context.inS(ev);
                case INP_S:
                    return context.inpS(ev);
                case RD_S:
                    return context.rdS(ev);
                case RDP_S:
                    return context.rdpS(ev);
                case GET_S:
                    return context.getS(ev);
                case SET_S:
                    return context.setS(new RespectSpecification(t.toString()), ev);
            }
        } catch (final InvalidLogicTupleException e) {
            throw new TucsonInvalidLogicTupleException();
        } catch (final OperationNotPossibleException e) {
            throw new TucsonOperationNotPossibleException();
        }
        return res;
    }

    /**
     * CALLED BY UNUSED METHOD ATM
     */
    // why are these methods not implemented yet?
    public static synchronized void enablePersistence() {
        /*
         *
         */
    }

    /**
     * UNUSED ATM
     */
    public static void loadPersistentInformation() {
        /*
         *
         */
    }

    /**
     * @param ttcid           the id of the tuple centre to make persistent
     * @param persistencyPath the path where to store persistency information
     */
    public static synchronized void enablePersistency(
            final TucsonTupleCentreIdDefault ttcid, final String persistencyPath) {
        IManagementContext context = null;
        context = RespectTCContainer.getRespectTCContainer()
                .getManagementContext(ttcid.getInternalTupleCentreId());
        context.enablePersistency(persistencyPath, ttcid);
    }

    /**
     * @param ttcid           the id of the tuple centre to make persistent
     * @param persistencyPath the path where to store persistency information
     * @param file            the name of the file to recover
     */
    public static void recoveryPersistent(final TucsonTupleCentreIdDefault ttcid,
                                          final String persistencyPath, final String file) {
        IManagementContext context = null;
        context = RespectTCContainer.getRespectTCContainer()
                .getManagementContext(ttcid.getInternalTupleCentreId());
        context.recoveryPersistent(persistencyPath, file, ttcid);
    }

    private TupleCentreContainer() {
        /*
         *
         */
    }
}
