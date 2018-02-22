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
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tucson.api.exceptions.TucsonInvalidSpecificationException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.TupleCentreOperation;
import alice.tuplecentre.api.InspectableEventListener;
import alice.tuplecentre.api.ObservableEventListener;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.OperationCompletionListener;

/**
 * 
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 * 
 */
public final class TupleCentreContainer {
    private static int defaultport;

    /**
     * 
     * @param id
     *            the identifier of the tuple centre this wrapper refers to
     * @param q
     *            the size of the input queue
     * @param defPort
     *            the default listening port
     * @return the Object representing the ReSpecT tuple centre
     * @throws InvalidTupleCentreIdException
     *             if the given tuple centre identifier is not a valid TuCSoN
     *             tuple centre identifier
     */
    public static RespectTC createTC(final TucsonTupleCentreId id, final int q,
            final int defPort) throws InvalidTupleCentreIdException {
        TupleCentreContainer.defaultport = defPort;
        try {
            final RespectTCContainer rtcc = RespectTCContainer
                    .getRespectTCContainer();
            RespectTCContainer.setDefPort(TupleCentreContainer.defaultport);
            final TupleCentreId tid = new TupleCentreId(id.getName(),
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
        final int type = ev.getSimpleTCEvent().getType();
        final TucsonTupleCentreId tid = (TucsonTupleCentreId) ev.getTarget();
        try {
            context = RespectTCContainer.getRespectTCContainer()
                    .getOrdinarySynchInterface(tid.getInternalTupleCentreId());
            if (type == TucsonOperationDefault.getCode()) {
                return context.get(ev);
            }
            if (type == TucsonOperationDefault.setCode()) {
                return context.set(ev);
            }
            if (type == TucsonOperationDefault.outCode()) {
                context.out(ev);
                return ev.getSimpleTCEvent().getTupleArgument();
            }
            if (type == TucsonOperationDefault.inCode()) {
                return context.in(ev);
            }
            if (type == TucsonOperationDefault.inpCode()) {
                return context.inp(ev);
            }
            if (type == TucsonOperationDefault.rdCode()) {
                return context.rd(ev);
            }
            if (type == TucsonOperationDefault.rdpCode()) {
                return context.rdp(ev);
            }
            if (type == TucsonOperationDefault.noCode()) {
                return context.no(ev);
            }
            if (type == TucsonOperationDefault.nopCode()) {
                return context.nop(ev);
            }
            if (type == TucsonOperationDefault.outAllCode()) {
                return context.outAll(ev);
            }
            if (type == TucsonOperationDefault.inAllCode()) {
                return context.inAll(ev);
            }
            if (type == TucsonOperationDefault.rdAllCode()) {
                return context.rdAll(ev);
            }
            if (type == TucsonOperationDefault.noAllCode()) {
                return context.noAll(ev);
            }
            if (type == TucsonOperationDefault.uinCode()) {
                return context.uin(ev);
            }
            if (type == TucsonOperationDefault.urdCode()) {
                return context.uin(ev);
            }
            if (type == TucsonOperationDefault.unoCode()) {
                return context.uin(ev);
            }
            if (type == TucsonOperationDefault.uinpCode()) {
                return context.uin(ev);
            }
            if (type == TucsonOperationDefault.urdpCode()) {
                return context.uin(ev);
            }
            if (type == TucsonOperationDefault.unopCode()) {
                return context.uin(ev);
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
        final int type = ev.getSimpleTCEvent().getType();
        final TucsonTupleCentreId tid = (TucsonTupleCentreId) ev.getTarget();
        final LogicTuple t = (LogicTuple) ev.getTuple();
        try {
            context = RespectTCContainer.getRespectTCContainer()
                    .getSpecificationSynchInterface(
                            tid.getInternalTupleCentreId());
            if (type == TucsonOperationDefault.setSCode()) {
                if ("spec".equals(t.getName())) {
                    return ((SpecificationSynchInterface) context)
                            .setS(new RespectSpecification(t.getArg(0)
                                    .getName()), ev);
                }
                return context.setS(t, ev);
            }
            if (type == TucsonOperationDefault.getSCode()) {
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
        final int type = ev.getSimpleTCEvent().getType();
        final TucsonTupleCentreId tid = (TucsonTupleCentreId) ev.getTarget();
        try {
            context = RespectTCContainer.getRespectTCContainer()
                    .getSpecificationSynchInterface(
                            tid.getInternalTupleCentreId());
            if (type == TucsonOperationDefault.setSCode()) {
                if ("spec".equals(t.getName())) {
                    return ((SpecificationSynchInterface) context)
                            .setS(new RespectSpecification(t.getArg(0)
                                    .getName()), ev);
                }
                return context.setS(t, ev);
            }
            if (type == TucsonOperationDefault.getSCode()) {
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
    *
    * @param type
    *            the type codeof the ReSpecT operation to be executed
    * @param aid
    *            the identifier of the TuCSoN agent requesting the operation
    * @param tid
    *            the identifier of the tuple centre target of the operation
    * @param t
    *            the tuple argument of the operation
    * @param l
    *            the listener for operation completion
    * @return the Java object representing the tuple centre operation
    * @throws TucsonOperationNotPossibleException
    *             if the requested operation cannot be performed for some
    *             reason
    * @throws UnreachableNodeException
    *             if the TuCSoN tuple centre target of the notification cannot
    *             be reached over the network
    * @throws OperationTimeOutException
    *             if the notification operation expires timeout
    */
   public static TupleCentreOperation doEnvironmentalOperation(
           final int type, final TucsonAgentId aid,
           final TucsonTupleCentreId tid, final LogicTuple t,
           final OperationCompletionListener l)
           throws OperationTimeOutException,
           TucsonOperationNotPossibleException, UnreachableNodeException {
       IEnvironmentContext context = null;
       RespectOperationDefault op = null;
       context = RespectTCContainer.getRespectTCContainer()
               .getEnvironmentContext(tid.getInternalTupleCentreId());
       if (type == TucsonOperationDefault.getEnvCode()) {
           op = RespectOperationDefault.makeGetEnv(t, l);
       } else if (type == TucsonOperationDefault.setEnvCode()) {
           op = RespectOperationDefault.makeSetEnv(t, l);
       }
       // Preparing the input event for the tuple centre.
       final HashMap<String, String> eventMap = new HashMap<String, String>();
       eventMap.put("id", aid.toString());
       InputEvent event = null;
       final TransducersManager tm = TransducersManager.INSTANCE;
       TransducerStandardInterface transducer = tm.getTransducer(aid
               .getAgentName());
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
               transducer = tm.getTransducer(tId2.getAgentName());
               transducer.notifyOutput(internalEv);
           }
       }
       return op;
   }
    
    /**
    *
    * @param type
    *            the type codeof the ReSpecT operation to be executed
    * @param aid
    *            the identifier of the tuple centre requesting the operation
    * @param tid
    *            the identifier of the tuple centre target of the operation
    * @param t
    *            the tuple argument of the operation
    * @param l
    *            the listener for operation completion
    * @return the Java object representing the tuple centre operation
    * @throws TucsonOperationNotPossibleException
    *             if the requested operation cannot be performed for some
    *             reason
    * @throws UnreachableNodeException
    *             if the TuCSoN tuple centre target of the notification cannot
    *             be reached over the network
    * @throws OperationTimeOutException
    *             if the notification operation expires timeout
    */
   public static TupleCentreOperation doEnvironmentalOperation(
           final int type, final TucsonTupleCentreId aid,
           final TucsonTupleCentreId tid, final LogicTuple t,
           final OperationCompletionListener l)
           throws TucsonOperationNotPossibleException,
           UnreachableNodeException, OperationTimeOutException {
       IEnvironmentContext context = null;
       RespectOperationDefault op = null;
       context = RespectTCContainer.getRespectTCContainer()
               .getEnvironmentContext(tid.getInternalTupleCentreId());
       if (type == TucsonOperationDefault.getEnvCode()) {
           op = RespectOperationDefault.makeGetEnv(t, l);
       } else if (type == TucsonOperationDefault.setEnvCode()) {
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
           transducer = tm.getTransducer(tId2.getAgentName());
           transducer.notifyOutput(internalEv);
       }
       return op;
   }
   
   

    /**
    *
    * @param type
    *            the type code of the operation requested
    * @param tid
    *            the identifier of the tuple centre target of the operation
    * @param obj
    *            the argument of the management operation
    * @return the result of the operation
    */
   public static Object doManagementOperation(final int type,
           final TucsonTupleCentreId tid, final Object obj) {
       IManagementContext context = null;
       context = RespectTCContainer.getRespectTCContainer()
               .getManagementContext(tid.getInternalTupleCentreId());
       if (type == TucsonOperationDefault.abortOpCode()) {
           return context.abortOperation((Long) obj);
       }
       if (type == TucsonOperationDefault.setSCode()) {
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
       }
       if (type == TucsonOperationDefault.getSCode()) {
           return new LogicTuple(context.getSpec().toString());
       }
       if (type == TucsonOperationDefault.getTRSetCode()) {
           return context.getTRSet((LogicTuple) obj);
       }
       if (type == TucsonOperationDefault.getTSetCode()) {
           return context.getTSet((LogicTuple) obj);
       }
       if (type == TucsonOperationDefault.getWSetCode()) {
           return context.getWSet((LogicTuple) obj);
       }
       if (type == TucsonOperationDefault.goCmdCode()) {
           try {
               context.goCommand();
               return true;
           } catch (final OperationNotPossibleException e) {
               e.printStackTrace();
               return false;
           }
       }
       if (type == TucsonOperationDefault.stopCmdCode()) {
           try {
               context.stopCommand();
               return true;
           } catch (final OperationNotPossibleException e) {
               e.printStackTrace();
               return false;
           }
       }
       if (type == TucsonOperationDefault.isStepModeCode()) {
           return context.isStepModeCommand();
       }
       if (type == TucsonOperationDefault.stepModeCode()) {
           context.stepModeCommand();
           return true;
       }
       if (type == TucsonOperationDefault.nextStepCode()) {
           try {
               context.nextStepCommand();
               return true;
           } catch (final OperationNotPossibleException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
               return false;
           }
       }
       /*
        * TODO must be delete... if (type == TucsonOperationDefault.setMngModeCode())
        * { context.setManagementMode((Boolean) obj); return true; }
        */
       if (type == TucsonOperationDefault.addObsCode()) {
           context.addObserver((ObservableEventListener) obj);
           return true;
       }
       if (type == TucsonOperationDefault.rmvObsCode()) {
           context.removeObserver((ObservableEventListener) obj);
           return true;
       }
       if (type == TucsonOperationDefault.hasObsCode()) {
           return context.hasObservers();
       }
       if (type == TucsonOperationDefault.addInspCode()) {
           context.addInspector((InspectableEventListener) obj);
           return true;
       }
       if (type == TucsonOperationDefault.rmvInspCode()) {
           context.removeInspector((InspectableEventListener) obj);
           return true;
       }
       if (type == TucsonOperationDefault.getInspectorsCode()) {
           return context.getInspectors();
       }
       if (type == TucsonOperationDefault.hasInspCode()) {
           return context.hasInspectors();
       }
       // I don't think this is finished...
       if (type == TucsonOperationDefault.reset()) {
           return context.hasInspectors();
       }
       return null;
   }

    public static TupleCentreOperation doNonBlockingOperation(
            final InputEvent ev) throws TucsonInvalidLogicTupleException,
            TucsonOperationNotPossibleException {
        final TupleCentreOperation res = null;
        IOrdinaryAsynchInterface context = null;
        final int type = ev.getSimpleTCEvent().getType();
        final TucsonTupleCentreId tid = (TucsonTupleCentreId) ev.getTarget();
        try {
            context = RespectTCContainer.getRespectTCContainer()
                    .getOrdinaryAsynchInterface(tid.getInternalTupleCentreId());
            
            if (type == TucsonOperationDefault.spawnCode()) {
                return context.spawn(ev);
            }
            if (type == TucsonOperationDefault.outCode()) {
                return context.out(ev);
            }
            if (type == TucsonOperationDefault.inCode()) {
                return context.in(ev);
            }
            if (type == TucsonOperationDefault.inpCode()) {
                return context.inp(ev);
            }
            if (type == TucsonOperationDefault.rdCode()) {
                return context.rd(ev);
            }
            if (type == TucsonOperationDefault.rdpCode()) {
                return context.rdp(ev);
            }
            if (type == TucsonOperationDefault.noCode()) {
                return context.no(ev);
            }
            if (type == TucsonOperationDefault.nopCode()) {
                return context.nop(ev);
            }
            if (type == TucsonOperationDefault.getCode()) {
                return context.get(ev);
            }
            if (type == TucsonOperationDefault.setCode()) {
                return context.set(ev);
            }
            if (type == TucsonOperationDefault.outAllCode()) {
                return context.outAll(ev);
            }
            if (type == TucsonOperationDefault.inAllCode()) {
                return context.inAll(ev);
            }
            if (type == TucsonOperationDefault.rdAllCode()) {
                return context.rdAll(ev);
            }
            if (type == TucsonOperationDefault.noAllCode()) {
                return context.noAll(ev);
            }
            if (type == TucsonOperationDefault.uinCode()) {
                return context.uin(ev);
            }
            if (type == TucsonOperationDefault.uinpCode()) {
                return context.uinp(ev);
            }
            if (type == TucsonOperationDefault.urdCode()) {
                return context.urd(ev);
            }
            if (type == TucsonOperationDefault.urdpCode()) {
                return context.urdp(ev);
            }
            if (type == TucsonOperationDefault.unoCode()) {
                return context.uno(ev);
            }
            if (type == TucsonOperationDefault.unopCode()) {
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
        final int type = ev.getSimpleTCEvent().getType();
        final TucsonTupleCentreId tid = (TucsonTupleCentreId) ev.getTarget();
        final LogicTuple t = (LogicTuple) ev.getTuple();
        try {
            context = RespectTCContainer.getRespectTCContainer()
                    .getSpecificationAsynchInterface(
                            tid.getInternalTupleCentreId());
            if (type == TucsonOperationDefault.noSCode()) {
                return context.noS(ev);
            }
            if (type == TucsonOperationDefault.nopSCode()) {
                return context.nopS(ev);
            }
            if (type == TucsonOperationDefault.outSCode()) {
                return context.outS(ev);
            }
            if (type == TucsonOperationDefault.inSCode()) {
                return context.inS(ev);
            }
            if (type == TucsonOperationDefault.inpSCode()) {
                return context.inpS(ev);
            }
            if (type == TucsonOperationDefault.rdSCode()) {
                return context.rdS(ev);
            }
            if (type == TucsonOperationDefault.rdpSCode()) {
                return context.rdpS(ev);
            }
            if (type == TucsonOperationDefault.getSCode()) {
                return context.getS(ev);
            }
            if (type == TucsonOperationDefault.setSCode()) {
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
     * @param ttcid
     *            the id of the tuple centre to make persistent
     * @param persistencyPath
     *            the path where to store persistency information
     *
     */
    public static synchronized void enablePersistency(
            final TucsonTupleCentreId ttcid, final String persistencyPath) {
        IManagementContext context = null;
        context = RespectTCContainer.getRespectTCContainer()
                .getManagementContext(ttcid.getInternalTupleCentreId());
        context.enablePersistency(persistencyPath, ttcid);
    }

    /**
     * @param ttcid
     *            the id of the tuple centre to make persistent
     * @param persistencyPath
     *            the path where to store persistency information
     * @param file
     *            the name of the file to recover
     *
     */
    public static void recoveryPersistent(final TucsonTupleCentreId ttcid,
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
