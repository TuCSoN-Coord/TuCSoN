package alice.tuplecentre.tucson.service;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.*;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.OperationCompletionListener;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.respect.api.*;
import alice.tuplecentre.respect.api.exceptions.InvalidSpecificationException;
import alice.tuplecentre.respect.api.exceptions.InvalidTupleCentreIdException;
import alice.tuplecentre.respect.api.exceptions.OperationNotPossibleException;
import alice.tuplecentre.respect.core.*;
import alice.tuplecentre.respect.situatedness.TransducerId;
import alice.tuplecentre.respect.situatedness.TransducerStandardInterface;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidSpecificationException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Objects;

import static alice.tuplecentre.core.TupleCentreOpType.*;

/**
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 * michele.bombardi@studio.unibo.it)
 */
public final class TupleCentreContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static int defaultport;

    /**
     * @param id      the identifier copyOf the tuple centre this wrapper refers to
     * @param q       the size copyOf the input queue
     * @param defPort the default listening port
     * @return the Object representing the ReSpecT tuple centre
     * @throws InvalidTupleCentreIdException if the given tuple centre identifier is not a valid TuCSoN
     *                                       tuple centre identifier
     */
    public static RespectTC createTC(final TucsonTupleCentreId id, final int q,
                                     final int defPort) throws InvalidTupleCentreIdException {
        TupleCentreContainer.defaultport = defPort;
        try {
            final RespectTCContainer rtcc = RespectTCContainer
                    .getRespectTCContainer();
            RespectTCContainer.setDefPort(TupleCentreContainer.defaultport);
            final TupleCentreIdentifier tid = new TupleCentreId(id.getLocalName(),
                    id.getNode(), String.valueOf(id.getPort()));
            return rtcc.createRespectTC(tid, q);
        } catch (final InvalidTupleCentreIdException e) {
            throw new InvalidTupleCentreIdException();
        }
    }

    public static Object doBlockingOperation(final InputEvent ev)
            throws TucsonInvalidLogicTupleException,
            TucsonOperationNotPossibleException {
        IOrdinarySynchInterface context;
        final TupleCentreOpType type = ev.getSimpleTCEvent().getType();
        final TucsonTupleCentreId tid = (TucsonTupleCentreId) ev.getTarget();
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
        ISpecificationSynchInterface context;
        final TupleCentreOpType type = ev.getSimpleTCEvent().getType();
        final TucsonTupleCentreId tid = (TucsonTupleCentreId) ev.getTarget();
        final LogicTuple t = (LogicTuple) ev.getTuple();
        try {
            context = RespectTCContainer.getRespectTCContainer()
                    .getSpecificationSynchInterface(
                            tid.getInternalTupleCentreId());
            if (type == SET_S) {
                if ("spec".equals(t.getName())) {
                    return context
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
        ISpecificationSynchInterface context;
        final TupleCentreOpType type = ev.getSimpleTCEvent().getType();
        final TucsonTupleCentreId tid = (TucsonTupleCentreId) ev.getTarget();
        try {
            context = RespectTCContainer.getRespectTCContainer()
                    .getSpecificationSynchInterface(
                            tid.getInternalTupleCentreId());
            if (type == SET_S) {
                if ("spec".equals(t.getName())) {
                    return context
                            .setS(new RespectSpecification(t.getArg(0)
                                    .getName()), ev);
                }
                return context.setS(t, ev);
            }
            if (type == GET_S) {
                return context.getS(ev);
            }
        } catch (final OperationNotPossibleException e) {
            throw new TucsonOperationNotPossibleException(e);
        } catch (final InvalidSpecificationException e) {
            throw new TucsonInvalidSpecificationException(e);
        }
        return res;
    }

    /**
     * @param opType the type code copyOf the ReSpecT operation to be executed
     * @param aid    the identifier copyOf the TuCSoN agent requesting the operation
     * @param tid    the identifier copyOf the tuple centre target copyOf the operation
     * @param t      the tuple argument copyOf the operation
     * @param l      the listener for operation completion
     * @return the Java object representing the tuple centre operation
     */
    public static TupleCentreOperation doEnvironmentalOperation(
            final TupleCentreOpType opType, final TucsonAgentId aid,
            final TucsonTupleCentreId tid, final LogicTuple t,
            final OperationCompletionListener l) {
        IEnvironmentContext context;
        RespectOperationDefault op = null;
        context = RespectTCContainer.getRespectTCContainer()
                .getEnvironmentContext(tid.getInternalTupleCentreId());
        if (opType == GET_ENV) {
            op = RespectOperationDefault.makeGetEnv(t, l);
        } else if (opType == SET_ENV) {
            op = RespectOperationDefault.makeSetEnv(t, l);
        }
        // Preparing the input events for the tuple centre.
        final HashMap<String, String> eventMap = new HashMap<>();
        eventMap.put("id", aid.toString());
        InputEvent event;
        final TransducersManager tm = TransducersManager.INSTANCE;
        TransducerStandardInterface transducer = tm.getTransducer(aid
                .getLocalName());
        if (t != null) {
            // It's an events performed by a transducer. In other words, it's an
            // environment events
            event = new InputEvent(Objects.requireNonNull(transducer).getIdentifier(), op,
                    tid.getInternalTupleCentreId(), context.getCurrentTime(), null,
                    eventMap);
            // Sending the events
            event.setSource(transducer.getIdentifier());
            event.setTarget(tid.getInternalTupleCentreId());
            context.notifyInputEnvEvent(event);
        } else {
            // It's an agent request copyOf environment properties
            event = new InputEvent(aid, op, tid.getInternalTupleCentreId(),
                    context.getCurrentTime(), null, eventMap);
            final InternalEvent internalEv = new InternalEvent(event,
                    InternalOperation.makeGetEnv(null));
            internalEv.setSource(tid.getInternalTupleCentreId()); // Set
            // the source copyOf the events
            final TransducerId[] tIds = tm.getTransducerIds(tid
                    .getInternalTupleCentreId());
            for (final TransducerId tId2 : tIds) {
                internalEv.setTarget(tId2); // Set target resource
                transducer = tm.getTransducer(tId2.getLocalName());
                Objects.requireNonNull(transducer).notifyOutput(internalEv);
            }
        }
        return op;
    }

    /**
     * @param type the type codeof the ReSpecT operation to be executed
     * @param aid  the identifier copyOf the tuple centre requesting the operation
     * @param tid  the identifier copyOf the tuple centre target copyOf the operation
     * @param t    the tuple argument copyOf the operation
     * @param l    the listener for operation completion
     * @return the Java object representing the tuple centre operation
     */
    public static TupleCentreOperation doEnvironmentalOperation(
            final TupleCentreOpType type, final TucsonTupleCentreId aid,
            final TucsonTupleCentreId tid, final LogicTuple t,
            final OperationCompletionListener l) {
        IEnvironmentContext context;
        RespectOperationDefault op = null;
        context = RespectTCContainer.getRespectTCContainer()
                .getEnvironmentContext(tid.getInternalTupleCentreId());
        if (type == GET_ENV) {
            op = RespectOperationDefault.makeGetEnv(t, l);
        } else if (type == SET_ENV) {
            op = RespectOperationDefault.makeSetEnv(t, l);
        }
        // Preparing the input events for the tuple centre.
        final HashMap<String, String> eventMap = new HashMap<>();
        eventMap.put("id", aid.toString());
        InputEvent event;
        TransducerStandardInterface transducer;
        event = new InputEvent(aid, op, tid.getInternalTupleCentreId(),
                context.getCurrentTime(), null, eventMap);
        final InternalEvent internalEv = new InternalEvent(event,
                InternalOperation.makeGetEnv(t));
        internalEv.setSource(tid.getInternalTupleCentreId()); // Set
        final TransducersManager tm = TransducersManager.INSTANCE;
        // the source copyOf the events
        final TransducerId[] tIds = tm.getTransducerIds(tid
                .getInternalTupleCentreId());
        for (final TransducerId tId2 : tIds) {
            internalEv.setTarget(tId2); // Set target resource
            transducer = tm.getTransducer(tId2.getLocalName());
            Objects.requireNonNull(transducer).notifyOutput(internalEv);
        }
        return op;
    }


    /**
     * @param type the type code copyOf the operation requested
     * @param tid  the identifier copyOf the tuple centre target copyOf the operation
     * @param obj  the argument copyOf the management operation
     * @return the result copyOf the operation
     */
    public static Object doManagementOperation(final TupleCentreOpType type,
                                               final TucsonTupleCentreId tid, final Object obj) {
        IManagementContext context;
        context = RespectTCContainer.getRespectTCContainer()
                .getManagementContext(tid.getInternalTupleCentreId());
        switch (type) {
            case ABORT:
                return context.abortOperation((OperationIdentifier) obj);
            case SET_S:
                try {
                    context.setSpec(new RespectSpecification(((LogicTuple) obj)
                            .getArg(0).getName()));
                    return true;
                } catch (final InvalidSpecificationException | InvalidOperationException e) {
                    LOGGER.error(e.getMessage(), e);
                    return false;
                }
            case GET_S:
                return LogicTuple.of(context.getSpec().toString());
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
                    LOGGER.error(e.getMessage(), e);
                    return false;
                }
            case STOP_CMD:
                try {
                    context.stopCommand();
                    return true;
                } catch (final OperationNotPossibleException e) {
                    LOGGER.error(e.getMessage(), e);
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
                    LOGGER.error(e.getMessage(), e);
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
        IOrdinaryAsynchInterface context;
        final TupleCentreOpType type = ev.getSimpleTCEvent().getType();
        final TucsonTupleCentreId tid = (TucsonTupleCentreId) ev.getTarget();
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
        ISpecificationAsynchInterface context;
        final TupleCentreOpType type = ev.getSimpleTCEvent().getType();
        final TucsonTupleCentreId tid = (TucsonTupleCentreId) ev.getTarget();
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
     * @param ttcid           the id copyOf the tuple centre to make persistent
     * @param persistencyPath the path where to store persistency information
     */
    public static synchronized void enablePersistency(
            final TucsonTupleCentreId ttcid, final String persistencyPath) {
        IManagementContext context;
        context = RespectTCContainer.getRespectTCContainer()
                .getManagementContext(ttcid.getInternalTupleCentreId());
        context.enablePersistency(persistencyPath, ttcid);
    }

    /**
     * @param ttcid           the id copyOf the tuple centre to make persistent
     * @param persistencyPath the path where to store persistency information
     * @param file            the name copyOf the file to recover
     */
    public static void recoveryPersistent(final TucsonTupleCentreId ttcid,
                                          final String persistencyPath, final String file) {
        IManagementContext context;
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
