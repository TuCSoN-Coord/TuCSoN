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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.logictuple.exceptions.InvalidVarNameException;
import alice.logictuple.exceptions.LogicTupleException;
import alice.respect.core.RespectOperationDefault;
import alice.tucson.api.TucsonAgentIdDefault;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonGenericException;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.introspection.InspectorContextSkel;
import alice.tucson.introspection4gui.Inspector4GuiContextSkel;
import alice.tucson.network.AbstractTucsonProtocol;
import alice.tucson.network.exceptions.DialogReceiveException;
import alice.tucson.network.exceptions.DialogSendException;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.util.Tools;

/**
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public class ACCProvider {

    private static final int WAITING_TIME = 10;

    private static void log(final String st) {
        System.out.println("..[ACCProvider]: " + st);
    }

    private TucsonAgentIdDefault aid;
    private final TucsonTupleCentreId config;
    private final ExecutorService exec;
    private final TucsonNodeService node;

    /**
     *
     * @param n
     *            the TuCSoN node whose ACC should reference
     * @param tid
     *            the identifier of the tuple centre used for internal
     *            configuration purpose
     */
    public ACCProvider(final TucsonNodeService n, final TucsonTupleCentreId tid) {
        try {
            this.aid = new TucsonAgentIdDefault("context_manager");
        } catch (final TucsonInvalidAgentIdException e) {
            // Cannot happen because it's specified here
            e.printStackTrace();
        }
        this.node = n;
        this.config = tid;
        this.exec = Executors.newCachedThreadPool();
        ACCProvider.log("Listening to incoming ACC requests...");
    }

    /**
     *
     * @param profile
     *            the Object decribing a request for an ACC
     * @param dialog
     *            the network protocol used to dialog with the (possibly) given
     *            ACC
     * @return wether the request has been accepted (therefore the ACC given) or
     *         not
     * @throws DialogReceiveException
     *             if there is something wrong in the reception stream
     * @throws TucsonInvalidTupleCentreIdException
     *             if the TupleCentreIdentifier, contained into AbstractTucsonProtocol's
     *             message, does not represent a valid TuCSoN identifier
     *
     * @throws TucsonInvalidAgentIdException
     *             if the ACCDescription's "agent-identity" property does not
     *             represent a valid TuCSoN identifier
     */
    // exception handling is a mess, need to review it...
    public synchronized boolean processContextRequest(
            final ACCDescription profile, final AbstractTucsonProtocol dialog)
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
            final LogicTuple req = new LogicTuple("context_request", new Value(
                    Tools.removeApices(agentName)), new Var("CtxId"),
                    new Value(agentClass), new Value(agentUUID));
            /*
             * final LogicTuple req = new LogicTuple("context_request", new
             * Value( agentName), new Var("CtxId"));
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
                    e.printStackTrace();
                }
                return false;
            }
            final TupleArgument res = result.getArg(1);
            if ("failed".equals(res.getName())) {
                profile.setProperty("failure", res.getArg(0).getName());
                try {
                    dialog.sendEnterRequestRefused();
                } catch (DialogSendException e) {
                    e.printStackTrace();
                }
                return false;
            }
            final TupleArgument ctxId = res.getArg(0);
            profile.setProperty("context-id", ctxId.toString());
            ACCProvider.log("ACC request accepted, ACC id is < "
                    + ctxId.toString() + " >");
            try {
                dialog.sendEnterRequestAccepted();
            } catch (DialogSendException e) {
                e.printStackTrace();
            }
            final String agentRole = profile.getProperty("agent-role");
            if ("$inspector".equals(agentRole)) {
                final AbstractACCProxyNodeSide skel = new InspectorContextSkel(
                        this, dialog, this.node, profile);
                this.node.addNodeAgent(skel);
                skel.start();
            } else if ("$inspector4gui".equals(agentRole)) {
                final AbstractACCProxyNodeSide skel = new Inspector4GuiContextSkel(
                        this, dialog, this.node, profile);
                this.node.addNodeAgent(skel);
                this.node.addInspectorAgent((InspectorContextSkel) skel);
                skel.start();
            } else {
                // should I pass here the TuCSoN node port?
                final AbstractACCProxyNodeSide skel = new ACCProxyNodeSide(
                        this, dialog, this.node, profile);
                this.node.addNodeAgent(skel);
                this.exec.execute(skel);
            }
            return true;
        } catch (final LogicTupleException e) {
            profile.setProperty("failure", "generic");
            e.printStackTrace();
            return false;
        } catch (final TucsonGenericException e) {
            profile.setProperty("failure", "generic");
            e.printStackTrace();
            return false;
        } catch (final TucsonInvalidLogicTupleException e) {
            profile.setProperty("failure", "generic");
            e.printStackTrace();
            return false;
        } catch (final TucsonOperationNotPossibleException e) {
            profile.setProperty("failure", "generic");
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @throws InterruptedException
     *             if this provider is interrupted during termination
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
     *
     * @param ctxId
     *            the numeric, progressive identifier of the ACC given
     * @param id
     *            the identifier of the agent requiring shutdown
     * @return wether shutdown can be carried out or not
     */
    // exception handling is a mess, need to review it...
    public synchronized boolean shutdownContext(final int ctxId,
            final TucsonAgentIdDefault id) {
        LogicTuple req = null;
        try {
            req = new LogicTuple("context_shutdown", new Value(ctxId),
                    new Value(id.toString()), new Var("CtxId"));
        } catch (InvalidVarNameException e1) {
            e1.printStackTrace();
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
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
            return false;
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
            return false;
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
            return false;
        }
        // try {
        if ("ok".equals(result.getArg(2).getName())) {
            return true;
        }
        return false;
        // } catch (final InvalidLogicTupleOperationException e) {
        // e.printStackTrace();
        // return false;
        // }
    }
}
