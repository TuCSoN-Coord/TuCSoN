/*
 * User.java Copyright 2000-2001-2002 aliCE team at deis.unibo.it This software
 * is the proprietary information of deis.unibo.it Use is subject to license
 * terms.
 */
package alice.tucson.service;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTupleDefault;
import alice.tuple.logic.TupleArgument;
import alice.tuple.logic.ValueArgument;
import alice.tuple.logic.VarArgument;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuple.logic.exceptions.InvalidLogicTupleOperationException;
import alice.tuple.logic.exceptions.InvalidVarNameException;
import alice.respect.core.RespectOperationDefault;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuprolog.InvalidTermException;

/**
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 */
public class NodeManagementAgent extends Thread {

    private static void log(final String s) {
        System.out.println("[NodeManagementAgent]: " + s);
    }

    private final TucsonTupleCentreId config;
    private final TucsonNodeService node;
    private TucsonAgentId nodeManAid;

    /**
     *
     * @param conf
     *            the identifier of the tuple centre to be used for
     *            configuration
     * @param n
     *            the TuCSoN node this management agent belongs to
     */
    public NodeManagementAgent(final TucsonTupleCentreId conf,
            final TucsonNodeService n) {
        super();
        try {
            this.nodeManAid = new TucsonAgentId("node_management_agent");
        } catch (final TucsonInvalidAgentIdException e) {
            // Cannot happen, the agend id it's specified here
            e.printStackTrace();
        }
        this.node = n;
        this.config = conf;
        this.start();
    }

    /**
     * 
     */
    @Override
    public void run() {
        try {
            while (true) {
                LogicTuple cmd;
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.IN, new LogicTupleDefault("cmd",
                                new VarArgument("X")), null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.nodeManAid,
                        opRequested, this.config, System.currentTimeMillis(),
                        null);
                cmd = (LogicTuple) TupleCentreContainer.doBlockingOperation(ev);
                // cmd =
                // (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.IN, this.nodeManAid,
                // this.config,
                // new LogicTuple("cmd", new VarArgument("X")));
                if (cmd != null) {
                    this.execCmd(cmd.getArg(0));
                } else {
                    throw new InterruptedException();
                }
            }
        } catch (final InvalidTermException e) {
            e.printStackTrace();
            this.node.removeNodeAgent(this);
        } catch (final InterruptedException e) {
            NodeManagementAgent
                    .log("Shutdown interrupt received, shutting down...");
            this.node.removeNodeAgent(this);
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
            this.node.removeNodeAgent(this);
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
            this.node.removeNodeAgent(this);
        } catch (final InvalidLogicTupleOperationException e) {
            e.printStackTrace();
            this.node.removeNodeAgent(this);
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
            this.node.removeNodeAgent(this);
        } catch (InvalidVarNameException e) {
			e.printStackTrace();
			this.node.removeNodeAgent(this);
		}
    }
    
    private void execCmd(final TupleArgument cmd)
            throws InvalidLogicTupleOperationException,
            TucsonInvalidLogicTupleException,
            TucsonOperationNotPossibleException {
        final String name = cmd.getName();
        NodeManagementAgent.log("Executing command " + name);
        if ("destroy".equals(name)) {
            final String tcName = cmd.getArg(0).getName();
            final boolean result = this.node.destroyCore(tcName);
            if (result) {
                try {
                    // Operation Make
                    final RespectOperationDefault opRequested = RespectOperationDefault.make(
                            TupleCentreOpType.OUT, new LogicTupleDefault(
                                    "cmd_result", new ValueArgument("destroy"),
                                    new ValueArgument("ok")), null);
                    // InputEvent Creation
                    final InputEvent ev = new InputEvent(this.nodeManAid,
                            opRequested, this.config,
                            System.currentTimeMillis(), null);
                    TupleCentreContainer.doBlockingOperation(ev);
                } catch (final InvalidLogicTupleException e) {
                    e.printStackTrace();
                }
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.nodeManAid, this.config,
                // new LogicTuple("cmd_result", new Value("destroy"),
                // new Value("ok")));
            } else {
                try {
                    // Operation Make
                    final RespectOperationDefault opRequested = RespectOperationDefault.make(
                            TupleCentreOpType.OUT, new LogicTupleDefault(
                                    "cmd_result", new ValueArgument("destroy"),
                                    new ValueArgument("failed")), null);
                    // InputEvent Creation
                    final InputEvent ev = new InputEvent(this.nodeManAid,
                            opRequested, this.config,
                            System.currentTimeMillis(), null);
                    TupleCentreContainer.doBlockingOperation(ev);
                } catch (final InvalidLogicTupleException e) {
                    e.printStackTrace();
                }
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.nodeManAid, this.config,
                // new LogicTuple("cmd_result", new Value("destroy"),
                // new Value("failed")));
            }
        } else if ("enable_persistency".equals(name)) {
            try {
            	NodeManagementAgent.log("Enabling persistency...");
            	this.node.enablePersistency(new LogicTupleDefault(cmd.getArg(0)));
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.OUT, new LogicTupleDefault(
                                "cmd_result", cmd, new ValueArgument("ok")), null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.nodeManAid,
                        opRequested, this.config, System.currentTimeMillis(),
                        null);
                TupleCentreContainer.doBlockingOperation(ev);
                NodeManagementAgent.log("...persistency enabled.");
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.nodeManAid, this.config, new LogicTuple("cmd_result",
            // cmd, new Value("ok")));
        } else if ("disable_persistency".equals(name)) {
            try {
            	NodeManagementAgent.log("Disabling persistency...");
                this.node.disablePersistency(new LogicTupleDefault(cmd.getArg(0)));
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.OUT, new LogicTupleDefault(
                                "cmd_result", new ValueArgument("disable_persistency"),
                                new ValueArgument("ok")), null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.nodeManAid,
                        opRequested, this.config, System.currentTimeMillis(),
                        null);
                TupleCentreContainer.doBlockingOperation(ev);
                NodeManagementAgent.log("...persistency disabled.");
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.nodeManAid, this.config, new LogicTuple("cmd_result",
            // new Value("disable_persistency"), new Value("ok")));
        } else if ("enable_observability".equals(name)) {
            this.node.activateObservability();
        } else if ("disable_observability".equals(name)) {
            this.node.deactivateObservability();
        }
    }
}
