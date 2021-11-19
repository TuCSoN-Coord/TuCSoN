/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms copyOf the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 copyOf the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty copyOf MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy copyOf the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.tucson.service;

import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.TupleArgument;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuple.logic.exceptions.InvalidVarNameException;
import alice.tuple.logic.exceptions.LogicTupleException;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.respect.core.RespectOperationDefault;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonAgentIdDefault;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.exceptions.TucsonGenericException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.introspection.InspectorContextSkel;
import alice.tuplecentre.tucson.introspection4gui.Inspector4GuiContextSkel;
import alice.tuplecentre.tucson.network.TucsonProtocol;
import alice.tuplecentre.tucson.network.exceptions.DialogReceiveException;
import alice.tuplecentre.tucson.network.exceptions.DialogSendException;
import alice.util.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class ACCProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int WAITING_TIME = 10;

    private static void log(final String st) {
        LOGGER.info("..[ACCProvider]: " + st);
    }

    private TucsonAgentId aid;
    private final TucsonTupleCentreId config;
    private final ExecutorService exec;
    private final TucsonNodeService node;

    /**
     * @param n   the TuCSoN node whose ACC should reference
     * @param tid the identifier copyOf the tuple centre used for internal
     *            configuration purpose
     */
    public ACCProvider(final TucsonNodeService n, final TucsonTupleCentreId tid) {
        try {
            this.aid = new TucsonAgentIdDefault("context_manager");
        } catch (final TucsonInvalidAgentIdException e) {
            // Cannot happen because it's specified here
            LOGGER.error(e.getMessage(), e);
        }
        this.node = n;
        this.config = tid;
        this.exec = Executors.newCachedThreadPool();
        ACCProvider.log("Listening to incoming ACC requests...");
    }

    /**
     * @param profile the Object decribing a request for an ACC
     * @param dialog  the network protocol used to dialog with the (possibly) given
     *                ACC
     * @throws DialogReceiveException              if there is something wrong in the reception stream
     * @throws TucsonInvalidTupleCentreIdException if the TupleCentreIdentifier, contained into AbstractTucsonProtocol's
     *                                             message, does not represent a valid TuCSoN identifier
     * @throws TucsonInvalidAgentIdException       if the ACCDescription's "agent-identity" property does not
     *                                             represent a valid TuCSoN identifier
     */
    // exception handling is a mess, need to review it...
    public synchronized void processContextRequest(
            final ACCDescription profile, final TucsonProtocol dialog)
            throws DialogReceiveException, TucsonInvalidAgentIdException,
            TucsonInvalidTupleCentreIdException {
        ACCProvider.log("Processing ACC request...");
        try {
            String agentName = profile.getProperty("agent-identity");
            if (agentName == null) {
                agentName = profile.getProperty("tc-identity");
            }

            final String agentUUID = profile.getProperty("agent-uuid");
            String agentClass = profile.getProperty("agent-class");
            if (agentClass == null) {
                agentClass = "basic";
            }
            final LogicTuple req = LogicTuple.of("context_request", TupleArgument.of(
                    Tools.removeApices(agentName)), TupleArgument.var("CtxId"),
                    TupleArgument.of(agentClass), TupleArgument.of(agentUUID));
            /*
             * final LogicTuple req = LogicTuples.fromTerm("context_request", new
             * Value( agentName), TupleArguments.var("CtxId"));
             */
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.INP, req, null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.aid, opRequested,
                    this.config, System.currentTimeMillis(), null);
            final LogicTuple result = (LogicTuple) TupleCentreContainer
                    .doBlockingOperation(ev);
            // final LogicTuple result =
            // (LogicTuple) TupleCentreContainer.doBlockingOperation(
            // TupleCentreOpType.INP, this.aid, this.config,
            // req);
            if (result == null) {
                profile.setProperty("failure", "context not available");
                try {
                    dialog.sendEnterRequestRefused();
                } catch (DialogSendException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                return;
            }
            final TupleArgument res = result.getArg(1);
            if ("failed".equals(res.getName())) {
                profile.setProperty("failure", res.getArg(0).getName());
                try {
                    dialog.sendEnterRequestRefused();
                } catch (DialogSendException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                return;
            }
            final TupleArgument ctxId = res.getArg(0);
            profile.setProperty("context-id", ctxId.toString());
            ACCProvider.log("ACC request accepted, ACC id is < "
                    + ctxId.toString() + " >");
            try {
                dialog.sendEnterRequestAccepted();
            } catch (DialogSendException e) {
                LOGGER.error(e.getMessage(), e);
            }
            final String agentRole = profile.getProperty("agent-role");
            switch (agentRole) {
                case "$inspector": {
                    final AbstractACCProxyNodeSide skel = new InspectorContextSkel(
                            this, dialog, this.node, profile);
                    this.node.addNodeAgent(skel);
                    skel.start();
                    break;
                }
                case "$inspector4gui": {
                    final AbstractACCProxyNodeSide skel = new Inspector4GuiContextSkel(
                            this, dialog, this.node, profile);
                    this.node.addNodeAgent(skel);
                    this.node.addInspectorAgent((InspectorContextSkel) skel);
                    skel.start();
                    break;
                }
                default: {
                    // should I pass here the TuCSoN node port?
                    final AbstractACCProxyNodeSide skel = new ACCProxyNodeSide(
                            this, dialog, this.node, profile);
                    this.node.addNodeAgent(skel);
                    this.exec.execute(skel);
                    break;
                }
            }
        } catch (final LogicTupleException | TucsonOperationNotPossibleException | TucsonInvalidLogicTupleException | TucsonGenericException e) {
            profile.setProperty("failure", "generic");
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * @throws InterruptedException if this provider is interrupted during termination
     */
    public void shutdown() throws InterruptedException {
        ACCProvider.log("Shutdown interrupt received, shutting down...");
        this.exec.shutdownNow();
        if (this.exec.awaitTermination(ACCProvider.WAITING_TIME,
                TimeUnit.SECONDS)) {
            ACCProvider.log("Executors correctly stopped");
        } else {
            ACCProvider.log("Executors may be still running");
        }
    }

    /**
     * @param ctxId the numeric, progressive identifier copyOf the ACC given
     * @param id    the identifier copyOf the agent requiring shutdown
     */
    // exception handling is a mess, need to review it...
    public synchronized void shutdownContext(final int ctxId,
                                             final TucsonAgentId id) {
        LogicTuple req = null;
        try {
            req = LogicTuple.of("context_shutdown", TupleArgument.of(ctxId),
                    TupleArgument.of(id.toString()), TupleArgument.var("CtxId"));
        } catch (InvalidVarNameException e1) {
            LOGGER.error(e1.getMessage(), e1);
        }
        LogicTuple result;
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.INP, req, null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.aid, opRequested,
                    this.config, System.currentTimeMillis(), null);
            result = (LogicTuple) TupleCentreContainer.doBlockingOperation(ev);
            // result =
            // (LogicTuple) TupleCentreContainer.doBlockingOperation(
            // TupleCentreOpType.INP, this.aid, this.config,
            // req);
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
            return;
        }
        // try {
        "ok".equals(Objects.requireNonNull(result).getArg(2).getName());
        // } catch (final InvalidLogicTupleOperationException e) {
        // LOGGER.error(e.getMessage(), e);
        // return false;
        // }
    }
}
