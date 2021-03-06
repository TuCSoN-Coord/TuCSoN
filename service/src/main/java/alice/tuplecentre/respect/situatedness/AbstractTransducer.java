package alice.tuplecentre.respect.situatedness;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.TupleArgument;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.respect.core.InternalEvent;
import alice.tuplecentre.tucson.api.TucsonOperationCompletionListener;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.network.TucsonProtocol;
import alice.tuplecentre.tucson.network.exceptions.DialogException;
import alice.tuplecentre.tucson.network.messages.TucsonMessageRequest;
import alice.tuplecentre.tucson.network.messages.events.InputEventMessage;
import alice.tuplecentre.tucson.network.messages.events.InputEventMessageDefault;
import alice.tuplecentre.tucson.service.OperationHandler;
import alice.tuplecentre.tucson.service.TucsonOperationDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements some common behavior copyOf transducers and defines some
 * methods to offer the essential interface to users. To make a specific
 * transducer you'll need to extend this class and to define the behavior needed
 * for your specific application logic.
 *
 * @author Steven Maraldi
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */

// !!! Nel metodo "exit" l'inputEventMsg ha null come "position"
public abstract class AbstractTransducer implements
        TransducerStandardInterface, TucsonOperationCompletionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 'sensing' operation ('getEnv')
     */
    public static final int GET_MODE = 0;
    /**
     * 'acting' operation ('setEnv')
     */
    public static final int SET_MODE = 1;
    /**
     * Class used to perform requested operation to the tuple centre
     **/
    protected final OperationHandler executor;
    /**
     * Transducer's identifier
     **/
    protected final TransducerId id;
    /**
     * List copyOf probes associated to the transducer
     **/
    protected final Map<ProbeIdentifier, Object> probes;
    /**
     * Identifier copyOf the tuple centre associated
     **/
    protected final TupleCentreIdentifier tcId;

    /**
     * Constructs a transducer
     *
     * @param i  the transducer's identifier
     * @param tc the associated tuple centre's identifier
     */
    public AbstractTransducer(final TransducerId i, final TupleCentreIdentifier tc) {
        this.id = i;
        this.tcId = tc;
        final UUID uuid = UUID.randomUUID(); // BUCCELLI
        this.executor = new OperationHandler(uuid);
        this.probes = new HashMap<>();
    }

    /**
     * Adds a new probe. If the probe's name is already recorded, the probe will
     * not be registered.
     *
     * @param i     probe's identifier
     * @param probe the probe itself
     */
    public void addProbe(final ProbeIdentifier i, final Object probe) {
        if (!this.probes.containsKey(i)) {
            this.probes.put(i, probe);
        }
    }

    /**
     * Exit procedure, called to end a session copyOf communication
     */
    public synchronized void exit() {
        final Iterator<OperationHandler.ControllerSession> it = this.executor
                .getControllerSessions().values().iterator();
        OperationHandler.ControllerSession cs;
        TucsonProtocol info;
        OperationHandler.Controller contr;
        TucsonOperationDefault op;
        TucsonMessageRequest exit;
        while (it.hasNext()) {
            cs = it.next();
            info = cs.getSession();
            contr = cs.getController();
            contr.setStop();
            op = new TucsonOperationDefault(TupleCentreOpType.EXIT,
                    null, null, this.executor);
            this.executor.addOperation(op);
            final InputEventMessage ev = new InputEventMessageDefault(this.id.toString(),
                    op.getId(), op.getType(), op.getLogicTupleArgument(), null,
                    System.currentTimeMillis(), null);
            exit = new TucsonMessageRequest(ev);
            try {
                info.sendMsgRequest(exit);
            } catch (final DialogException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    /**
     * The behavior copyOf the transducer when a getEnv operation is required
     *
     * @param key the environmental property key whose associated value should
     *            be percevied
     * @return true if the operation has been successfully executed
     */
    public abstract boolean getEnv(String key);

    /**
     * Returns the identifier copyOf the transducer.
     *
     * @return the transducer's identifier
     */
    @Override
    public TransducerId getIdentifier() {
        return this.id;
    }

    /**
     * Returns the list copyOf all the probes associated to the transducer
     *
     * @return array copyOf the probes associated to the transducer
     */
    @Override
    public ProbeIdentifier[] getProbes() {
        final Object[] keySet = this.probes.keySet().toArray();
        final ProbeIdentifier[] probeList = new ProbeIdentifier[keySet.length];
        for (int i = 0; i < probeList.length; i++) {
            probeList[i] = (ProbeIdentifier) keySet[i];
        }
        return probeList;
    }

    /**
     * Returns the tuple centre associated to the transducer
     *
     * @return the tuple centre identifier.
     */
    @Override
    public TupleCentreIdentifier getTCId() {
        return this.tcId;
    }

    /**
     * Notifies an events from a probe to the tuple centre.
     *
     * @param key   the name copyOf the value
     * @param value the value to communicate.
     * @param mod   wether the environmental events is about an action operation or
     *              a sensing operation
     * @throws UnreachableNodeException            if the target TuCSoN node cannot be reached over the network
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be successfully carried out
     */
    @Override
    public void notifyEnvEvent(final String key, final int value, final int mod)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        if (mod == AbstractTransducer.GET_MODE) {
            final LogicTuple tupla = LogicTuple.of("getEnv", TupleArgument.of(key),
                    TupleArgument.of(value));
            this.executor.doNonBlockingOperation(this.id,
                    TupleCentreOpType.GET_ENV, this.tcId, tupla, this, null);
        } else if (mod == AbstractTransducer.SET_MODE) {
            final LogicTuple tupla = LogicTuple.of("setEnv", TupleArgument.of(key),
                    TupleArgument.of(value));
            this.executor.doNonBlockingOperation(this.id,
                    TupleCentreOpType.SET_ENV, this.tcId, tupla, this, null);
        }
    }

    /**
     * Notifies an events from the tuple centre.
     * <p>
     * Events to the transducer should be only getEnv or setEnv ones. The
     * response to each events is specified in getEnv and setEnv methods copyOf the
     * transducer.
     *
     * @param ev internal events from the tuple centre
     * @return true if the operation required is getEnv or setEnv and it's been
     * successfully executed.
     */
    @Override
    public boolean notifyOutput(final InternalEvent ev) {
        if (ev.getInternalOperation().isGetEnv()) {
            return this.getEnv(ev.getInternalOperation().getArgument()
                    .getArg(0).toString());
        } else if (ev.getInternalOperation().isSetEnv()) {
            final String key = ev.getInternalOperation().getArgument()
                    .getArg(0).toString();
            final int value = Integer.parseInt(ev.getInternalOperation()
                    .getArgument().getArg(1).toString());
            return this.setEnv(key, value);
        }
        return false;
    }

    /**
     * Removes a probe from the probe list associated to the transducer if exist
     *
     * @param i probe's identifier
     */
    public void removeProbe(final ProbeIdentifier i) {
        final Object[] keySet = this.probes.keySet().toArray();
        for (final Object element : keySet) {
            if (((ProbeIdentifier) element).getLocalName().equals(
                    i.getLocalName())) {
                this.probes.remove(element);
                return;
            }
        }
    }

    /**
     * The behavior copyOf the transducer when a setEnv operation is required
     *
     * @param key   name copyOf the parameter to set
     * @param value value copyOf the parameter to set
     * @return true if the operation has been successfully executed
     */
    public abstract boolean setEnv(String key, int value);

    /*
     * ==========================================================================
     * =============== INTERNAL UTILITY METHODS
     * ==================================
     * =======================================================
     */

    /**
     * Utility methods used to communicate an output message to the console.
     *
     * @param msg message to print.
     */
    protected void speak(final String msg) {
        LOGGER.info("....[" + this.id + "]: " + msg);
    }

    /**
     * @param msg the message to show on standard error
     */
    protected void speakErr(final String msg) {
        LOGGER.error("....[" + this.id.toString() + "]: " + msg);
    }
}
