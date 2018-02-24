/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tucson.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import alice.logictuple.LogicTuple;
import alice.logictuple.LogicTupleOpManager;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.logictuple.exceptions.InvalidVarNameException;
import alice.respect.api.geolocation.PlatformUtils;
import alice.respect.api.geolocation.Position;
import alice.respect.api.geolocation.service.AbstractGeolocationService;
import alice.respect.api.geolocation.service.GeoServiceId;
import alice.respect.api.geolocation.service.GeolocationServiceManager;
import alice.respect.api.place.IPlace;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonOperation;
import alice.tucson.api.TucsonOperationCompletionListener;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.acc.DefaultACC;
import alice.tucson.api.acc.EnhancedACC;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.network.AbstractTucsonProtocol;
import alice.tucson.network.TucsonMsgRequest;
import alice.tucson.network.exceptions.DialogException;
import alice.tucson.service.tools.TucsonACCTool;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleCentreId;
import alice.tuplecentre.api.TupleTemplate;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuprolog.Parser;

/**
 * Active part of the Default Agent Coordination Context.
 *
 * It implements the underlying behavior needed by every TuCSoN Agent
 * {@link alice.tucson.api.AbstractTucsonAgent user} to fruitfully interact with
 * the TuCSoN Node Service {@link alice.tucson.service.TucsonNodeService TuCSoN}
 * . Essentially, it implements every method exposed in the Default ACC
 * Interface {@link DefaultACC default} offered to the agent,
 * maps each of them into TuCSoN Request Messages
 * {@link alice.tucson.network.TucsonMsgRequest req}, then waits for TuCSoN Node
 * Services replies {@link alice.tucson.network.TucsonMsgReply reply} forwarding
 * them to the agent.
 *
 * It also is in charge of establishing the first connection toward the TuCSoN
 * Node Service and the specific tuplecentre inside it as soon as needed (that
 * is, upon the first invocation of any method belonging to the ACC Interface).
 *
 * It is created from the TuCSoN Agent class. In it, an internal thread is
 * responsible to obtain the choosen ACC (the Default is the only at the moment)
 * by invoking the {@link alice.tucson.api.TucsonMetaACC#getContext getContext}
 * static method from the TuCSoN Meta ACC class
 * {@link alice.tucson.api.TucsonMetaACC metaACC}. The acquisition of such ACC
 * triggers this proxy creation and execution.
 *
 * @see alice.tucson.api.AbstractTucsonAgent TucsonAgent
 * @see alice.tucson.service.TucsonNodeService TucsonNodeService
 * @see DefaultACC DefaultACC
 * @see alice.tucson.network.TucsonMsgRequest TucsonMsgRequest
 * @see alice.tucson.network.TucsonMsgReply TucsonMsgReply
 * @see alice.tucson.api.TucsonMetaACC TucsonMetaACC
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public class ACCProxyAgentSide implements EnhancedACC {

    private static final int DEFAULT_PORT = 20504;
    /**
     * The tuple centre ID where RBAC structure is managed
     */
    protected static final String TC_ORG = "'$ORG'"; // galassi
    private volatile boolean isACCEntered; // galassi
    private ACCDescription profile; // galassi
    /**
     * TuCSoN Agent Identifier
     */
    protected TucsonAgentId aid;

    /**
     * The handlers thread pool to carry out coordination services requests
     */
    protected OperationHandler executor;
    /**
     * TuCSoN Node Service ip address
     */
    protected String node;
    /**
     * Password of Admin agents
     */
    protected String password;

    /**
     * TuCSoN Node Service listening port
     */
    protected int port;
    /**
     * Current ACC position
     */
    protected Position position;
    /**
     * Username of Admin agents
     */
    protected String username;
    /**
     * Current geolocation service
     */
    private AbstractGeolocationService myGeolocationService;

    /**
     * Default constructor: exploits the default port (20504) in the "localhost"
     * TuCSoN Node Service
     *
     * @param agId
     *            TuCSoN Agent identifier
     *
     * @throws TucsonInvalidAgentIdException
     *             if the String representation given is not valid TuCSoN agent
     *             identifier
     */
    public ACCProxyAgentSide(final Object agId)
            throws TucsonInvalidAgentIdException {
        this(agId, "localhost", ACCProxyAgentSide.DEFAULT_PORT);
    }

    /**
     * Complete constructor: starts the named TuCSoN Agent on the specific
     * TuCSoN node listening on the specified port
     *
     * @param agId
     *            TuCSoN Agent identifier
     * @param n
     *            TuCSoN node ip address
     * @param p
     *            TuCSoN node listening port
     *
     * @throws TucsonInvalidAgentIdException
     *             if the String representation given is not valid TuCSoN agent
     *             identifier
     */
    public ACCProxyAgentSide(final Object agId, final String n, final int p)
            throws TucsonInvalidAgentIdException {
        if (agId.getClass().getName().equals("alice.tucson.api.TucsonAgentId")) {
            this.aid = (TucsonAgentId) agId;
        } else {
            this.aid = new TucsonAgentId(agId.toString());
        }
        this.node = n;
        this.port = p;
        final UUID agentUUID = UUID.randomUUID();
        this.executor = new OperationHandler(agentUUID);
        this.isACCEntered = false;
        this.setPosition();
    }

    public ACCProxyAgentSide(final Object agId, final String n, final int p,
            final UUID agentUUID) throws TucsonInvalidAgentIdException {
        if (agId.getClass().getName().equals("alice.tucson.api.TucsonAgentId")) {
            this.aid = (TucsonAgentId) agId;
        } else {
            this.aid = new TucsonAgentId(agId.toString());
        }
        this.node = n;
        this.port = p;
        this.executor = new OperationHandler(agentUUID);
        this.isACCEntered = false;
    }
    
    /**
     * 
     * @param className
     *            the name of the class implementing the Geolocation Service to
     *            be used
     * @param tcId
     *            the id of the tuple centre responsible for handling
     *            Geolocation Service events
     */
    public void attachGeolocationService(final String className,
            final TucsonTupleCentreId tcId) {
        final GeolocationServiceManager geolocationManager = GeolocationServiceManager
                .getGeolocationManager();
        if (geolocationManager.getServices().size() > 0) {
            final AbstractGeolocationService geoService = geolocationManager
                    .getServiceByName(this.aid.getAgentName() + "_GeoService");
            if (geoService != null) {
                this.myGeolocationService = geoService;
                // geoService.setCompletionListener(new
                // AgentGeolocationServiceListener(this,
                // this.myGeolocationService, tcId));
                this.log("A geolocation service is already attached to this agent, using this.");
                if (!geoService.isRunning()) {
                    geoService.start();
                }
            } else {
                this.createGeolocationService(tcId, className);
            }
        } else {
            this.createGeolocationService(tcId, className);
        }
    }

    @Override
    public void enterACC() throws UnreachableNodeException,
            TucsonInvalidTupleCentreIdException {

        this.profile = new ACCDescription();
        this.profile.setProperty("agent_identity", this.aid.toString());
        this.profile.setProperty("agent_name", this.aid.getAgentName());
        this.profile.setProperty("agent-uuid",
                this.executor.agentUUID.toString());
        this.profile.setProperty("agent_role", "user");
        if (this.username != null && !this.username.equalsIgnoreCase("")
                && this.password != null && !this.password.equalsIgnoreCase("")) {
            this.profile.setProperty("credentials", "'" + this.username + ":"
                    + TucsonACCTool.encrypt(this.password) + "'");
        }
        final TucsonTupleCentreId tid = new TucsonTupleCentreId(
                ACCProxyAgentSide.TC_ORG, this.node, "" + this.port);
        this.executor.getSession(tid, this.aid);

    }

    @Override
    public synchronized void exit() {
        if (this.myGeolocationService != null) {
            GeolocationServiceManager.getGeolocationManager().destroyService(
                    this.myGeolocationService.getServiceId());
        }
        final Iterator<OperationHandler.ControllerSession> it = this.executor
                .getControllerSessions().values().iterator();
        OperationHandler.ControllerSession cs;
        AbstractTucsonProtocol info;
        OperationHandler.Controller contr;
        TucsonOperationDefault op;
        TucsonMsgRequest exit;
        while (it.hasNext()) {
            cs = it.next();
            info = cs.getSession();
            contr = cs.getController();
            contr.setStop();
            /*op = new TucsonOperationDefault(TupleCentreOpType.EXIT,
                    (TupleTemplate) null, null, this);
            this.operations.put(op.getId(), op);*/
            op = new TucsonOperationDefault(TupleCentreOpType.EXIT,
                    (TupleTemplate) null, null, this.executor /* this */);
            this.executor.addOperation(op.getId(), op);
            final InputEventMsg ev = new InputEventMsg(this.aid.toString(),
                    op.getId(), op.getType(), op.getLogicTupleArgument(), null,
                    System.currentTimeMillis(), this.getPosition());
            exit = new TucsonMsgRequest(ev);
            try {
                info.sendMsgRequest(exit);
            } catch (final DialogException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public TucsonOperation get(final TupleCentreId tid, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.GET, tid, null, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation get(final TupleCentreId tid,
                               final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.GET, tid, null, l, this.getPosition());
    }

    @Override
    public List<TucsonOpCompletionEvent> getCompletionEventsList() {
        return this.executor.events;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Map<Long, TucsonOperation> getPendingOperationsMap() {
        return this.executor.operations;
    }
    
    /**
     * 
     * @return the position of the agent behind this ACC
     */
    public Position getPosition() {
        return this.position;
    }

    @Override
    public TucsonOperation getS(final TupleCentreId tid, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        LogicTuple spec = null;
        try {
            spec = new LogicTuple("spec", new Var("S"));
        } catch (final InvalidVarNameException e) {
            // Cannot happen, the var name it's specified here
            e.printStackTrace();
        }
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.GET_S, tid, spec, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation getS(final TupleCentreId tid,
                                final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        final LogicTuple spec = new LogicTuple("spec");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.GET_S, tid, spec, l, this.getPosition());
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public UUID getUUID() {
        return this.executor.agentUUID;
    }

    @Override
    public TucsonOperation in(final TupleCentreId tid, final Tuple tuple,
                              final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.IN, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation in(final TupleCentreId tid, final Tuple tuple,
                              final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.IN, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation inAll(final TupleCentreId tid, final Tuple tuple,
                                 final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.IN_ALL, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation inAll(final TupleCentreId tid, final Tuple tuple,
                                 final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.IN_ALL, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation inp(final TupleCentreId tid, final Tuple tuple,
                               final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.INP, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation inp(final TupleCentreId tid, final Tuple tuple,
                               final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.INP, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation inpS(final TupleCentreId tid,
                                final LogicTuple event, final LogicTuple guards,
                                final LogicTuple reactionBody, final Long timeout)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException, OperationTimeOutException {
        final LogicTuple tuple = new LogicTuple(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.INP_S, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation inpS(final TupleCentreId tid,
                                final LogicTuple event, final LogicTuple guards,
                                final LogicTuple reactionBody,
                                final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        final LogicTuple tuple = new LogicTuple(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.INP_S, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation inS(final TupleCentreId tid,
                               final LogicTuple event, final LogicTuple guards,
                               final LogicTuple reactionBody, final Long timeout)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException, OperationTimeOutException {
        final LogicTuple tuple = new LogicTuple(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.IN_S, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation inS(final TupleCentreId tid,
                               final LogicTuple event, final LogicTuple guards,
                               final LogicTuple reactionBody,
                               final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        final LogicTuple tuple = new LogicTuple(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.IN_S, tid, tuple, l, this.getPosition());
    }

    @Override
    public boolean isACCEntered() {
        return this.isACCEntered;
    }

    @Override
    public TucsonOperation no(final TupleCentreId tid, final Tuple tuple,
                              final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.NO, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation no(final TupleCentreId tid, final Tuple tuple,
                              final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.NO, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation noAll(final TupleCentreId tid, final Tuple tuple,
                                 final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.NO_ALL, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation noAll(final TupleCentreId tid, final Tuple tuple,
                                 final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.NO_ALL, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation nop(final TupleCentreId tid, final Tuple tuple,
                               final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.NOP, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation nop(final TupleCentreId tid, final Tuple tuple,
                               final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.NOP, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation nopS(final TupleCentreId tid,
                                final LogicTuple event, final LogicTuple guards,
                                final LogicTuple reactionBody, final Long timeout)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException, OperationTimeOutException {
        final LogicTuple tuple = new LogicTuple(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.NOP_S, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation nopS(final TupleCentreId tid,
                                final LogicTuple event, final LogicTuple guards,
                                final LogicTuple reactionBody,
                                final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        final LogicTuple tuple = new LogicTuple(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.NOP_S, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation noS(final TupleCentreId tid,
                               final LogicTuple event, final LogicTuple guards,
                               final LogicTuple reactionBody, final Long timeout)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException, OperationTimeOutException {
        final LogicTuple tuple = new LogicTuple(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.NO_S, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation noS(final TupleCentreId tid,
                               final LogicTuple event, final LogicTuple guards,
                               final LogicTuple reactionBody,
                               final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        final LogicTuple tuple = new LogicTuple(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.NO_S, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation out(final TupleCentreId tid, final Tuple tuple,
                               final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.OUT, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation out(final TupleCentreId tid, final Tuple tuple,
                               final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.OUT, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation outAll(final TupleCentreId tid, final Tuple tuple,
                                  final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.OUT_ALL, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation outAll(final TupleCentreId tid, final Tuple tuple,
                                  final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.OUT_ALL, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation outS(final TupleCentreId tid,
                                final LogicTuple event, final LogicTuple guards,
                                final LogicTuple reactionBody, final Long timeout)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException, OperationTimeOutException {
        final LogicTuple tuple = new LogicTuple(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.OUT_S, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation outS(final TupleCentreId tid,
                                final LogicTuple event, final LogicTuple guards,
                                final LogicTuple reactionBody,
                                final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        final LogicTuple tuple = new LogicTuple(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.OUT_S, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation rd(final TupleCentreId tid, final Tuple tuple,
                              final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.RD, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation rd(final TupleCentreId tid, final Tuple tuple,
                              final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.RD, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation rdAll(final TupleCentreId tid, final Tuple tuple,
                                 final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.RD_ALL, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation rdAll(final TupleCentreId tid, final Tuple tuple,
                                 final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.RD_ALL, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation rdp(final TupleCentreId tid, final Tuple tuple,
                               final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.RDP, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation rdp(final TupleCentreId tid, final Tuple tuple,
                               final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.RDP, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation rdpS(final TupleCentreId tid,
                                final LogicTuple event, final LogicTuple guards,
                                final LogicTuple reactionBody, final Long timeout)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException, OperationTimeOutException {
        final LogicTuple tuple = new LogicTuple(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.RDP_S, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation rdpS(final TupleCentreId tid,
                                final LogicTuple event, final LogicTuple guards,
                                final LogicTuple reactionBody,
                                final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        final LogicTuple tuple = new LogicTuple(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.RDP_S, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation rdS(final TupleCentreId tid,
                               final LogicTuple event, final LogicTuple guards,
                               final LogicTuple reactionBody, final Long timeout)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException, OperationTimeOutException {
        final LogicTuple tuple = new LogicTuple(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.RD_S, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation rdS(final TupleCentreId tid,
                               final LogicTuple event, final LogicTuple guards,
                               final LogicTuple reactionBody,
                               final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        final LogicTuple tuple = new LogicTuple(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.RD_S, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation set(final TupleCentreId tid, final Tuple tuple,
                               final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.SET, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation set(final TupleCentreId tid, final Tuple tuple,
                               final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.SET, tid, tuple, l, this.getPosition());
    }
    
    /**
     * 
     */
    public final void setPosition() {
        this.position = new Position();
    }

    /**
     * 
     * @param place
     *            the position of the agent behind this ACC
     */
    public void setPosition(final IPlace place) {
        if (this.position != null) {
            this.position.setPlace(place);
        }
    }

    @Override
    public TucsonOperation setS(final TupleCentreId tid,
                                final LogicTuple spec, final Long timeout)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.SET_S, tid, spec, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation setS(final TupleCentreId tid,
                                final LogicTuple spec, final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.SET_S, tid, spec, l, this.getPosition());
    }

    @Override
    public TucsonOperation setS(final TupleCentreId tid, final String spec,
                                final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        if ("".equals(spec) || "''".equals(spec) || "'.'".equals(spec)) {
            throw new TucsonOperationNotPossibleException();
        }
        final LogicTuple specT = new LogicTuple("spec", new Value(spec));
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.SET_S, tid, specT, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation setS(final TupleCentreId tid, final String spec,
                                final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        final LogicTuple specT = new LogicTuple("spec", new Value(spec));
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.SET_S, tid, specT, l, this.getPosition());
    }

    @Override
    public TucsonOperation spawn(final TupleCentreId tid, final Tuple toSpawn,
                                 final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.SPAWN, tid, toSpawn, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation spawn(final TupleCentreId tid, final Tuple toSpawn,
                                 final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.SPAWN, tid, toSpawn, l, this.getPosition());
    }

    @Override
    public TucsonOperation uin(final TupleCentreId tid, final Tuple tuple,
                               final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.UIN, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation uin(final TupleCentreId tid, final Tuple tuple,
                               final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.UIN, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation uinp(final TupleCentreId tid, final Tuple tuple,
                                final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.UINP, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation uinp(final TupleCentreId tid, final Tuple tuple,
                                final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.UINP, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation uno(final TupleCentreId tid, final Tuple tuple,
                               final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.UNO, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation uno(final TupleCentreId tid, final Tuple tuple,
                               final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.UNO, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation unop(final TupleCentreId tid, final Tuple tuple,
                                final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.UNOP, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation unop(final TupleCentreId tid, final Tuple tuple,
                                final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.UNOP, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation urd(final TupleCentreId tid, final Tuple tuple,
                               final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.URD, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation urd(final TupleCentreId tid, final Tuple tuple,
                               final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.URD, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation urdp(final TupleCentreId tid, final Tuple tuple,
                                final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.URDP, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation urdp(final TupleCentreId tid, final Tuple tuple,
                                final TucsonOperationCompletionListener l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException {
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.URDP, tid, tuple, l, this.getPosition());
    }
    
    private void createGeolocationService(final TucsonTupleCentreId tcId,
            final String className) {
        try {
            // String normClassName = Tools.removeApices(className);;
            // Class<?> c = Class.forName( normClassName );
            // Constructor<?> ctor = c.getConstructor(new Class[] {
            // Integer.class, GeoServiceId.class, TucsonTupleCentreId.class});
            //
            final int platform = PlatformUtils.getPlatform();
            final GeoServiceId sId = new GeoServiceId(this.aid.getAgentName()
                    + "_GeoService");
            //
            // this.myGeolocationService = (GeolocationService)
            // ctor.newInstance(new Object[] {platform, sId, tcId});
            this.myGeolocationService = GeolocationServiceManager
                    .getGeolocationManager().createAgentService(platform, sId,
                            className, tcId, this);
            if (this.myGeolocationService != null) {
                // this.myGeolocationService.setCompletionListener(new
                // AgentGeolocationServiceListener(this,
                // this.myGeolocationService, tcId));
                // GeolocationServiceManager.getGeolocationManager().addService(this.myGeolocationService);
                this.myGeolocationService.start();
            } else {
                this.log("Error during service creation");
            }
        } catch (final SecurityException e) {
            this.log("Error during service creation: " + e.getMessage());
        } catch (final NoSuchMethodException e) {
            this.log("Error during service creation: " + e.getMessage());
        } catch (final IllegalArgumentException e) {
            this.log("Error during service creation: " + e.getMessage());
        } catch (final InstantiationException e) {
            this.log("Error during service creation: " + e.getMessage());
        } catch (final IllegalAccessException e) {
            this.log("Error during service creation: " + e.getMessage());
        } catch (final InvocationTargetException e) {
            this.log("Error during service creation: " + e.getMessage());
        } catch (final ClassNotFoundException e) {
            this.log("Error during service creation: " + e.getMessage());
        }
    }

    /**
     * Method internally used to log proxy activity (could be used for debug)
     *
     * @param msg
     *            String to display on the standard output
     */
    protected void log(final String msg) {
        System.out.println("[ACCProxyAgentSide]: " + msg);
    }

}
