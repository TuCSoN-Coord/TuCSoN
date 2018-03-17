/*
 * User.java Copyright 2000-2001-2002 aliCE team at deis.unibo.it This software
 * is the proprietary information of deis.unibo.it Use is subject to license
 * terms.
 */
package alice.tuplecentre.tucson.service;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.TupleArgument;
import alice.tuple.logic.TupleArguments;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuple.logic.exceptions.InvalidVarNameException;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.respect.core.RespectOperationDefault;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonAgentIdDefault;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuprolog.InvalidTermException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 * michele.bombardi@studio.unibo.it)
 */
public class NodeManagementAgent extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    private static void log(final String s) {
        LOGGER.info("[NodeManagementAgent]: " + s);
    }

    private final TucsonTupleCentreId config;
    private final TucsonNodeService node;
    private TucsonAgentId nodeManAid;

    /**
     * @param conf the identifier of the tuple centre to be used for
     *             configuration
     * @param n    the TuCSoN node this management agent belongs to
     */
    public NodeManagementAgent(final TucsonTupleCentreId conf,
                               final TucsonNodeService n) {
        super();
        try {
            this.nodeManAid = new TucsonAgentIdDefault("node_management_agent");
        } catch (final TucsonInvalidAgentIdException e) {
            // Cannot happen, the agend id it's specified here
            LOGGER.error(e.getMessage(), e);
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
            //noinspection InfiniteLoopStatement,InfiniteLoopStatement,InfiniteLoopStatement
            while (true) {
                LogicTuple cmd;
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.IN, LogicTuples.newInstance("cmd",
                                TupleArguments.newVarArgument("X")), null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.nodeManAid,
                        opRequested, this.config, System.currentTimeMillis(),
                        null);
                cmd = (LogicTuple) TupleCentreContainer.doBlockingOperation(ev);
                // cmd =
                // (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.IN, this.nodeManAid,
                // this.config,
                // LogicTuples.newInstance("cmd", TupleArguments.newVarArgument("X")));
                if (cmd != null) {
                    this.execCmd(cmd.getArg(0));
                } else {
                    throw new InterruptedException();
                }
            }
        } catch (final InvalidTermException | InvalidVarNameException | InvalidLogicTupleException | TucsonOperationNotPossibleException | TucsonInvalidLogicTupleException e) {
            LOGGER.error(e.getMessage(), e);
            this.node.removeNodeAgent(this);
        } catch (final InterruptedException e) {
            NodeManagementAgent
                    .log("Shutdown interrupt received, shutting down...");
            this.node.removeNodeAgent(this);
        }
    }

    private void execCmd(final TupleArgument cmd)
            throws
            TucsonInvalidLogicTupleException,
            TucsonOperationNotPossibleException {
        final String name = cmd.getName();
        NodeManagementAgent.log("Executing command " + name);
        switch (name) {
            case "destroy":
                final String tcName = cmd.getArg(0).getName();
                final boolean result = this.node.destroyCore(tcName);
                if (result) {
                    try {
                        // Operation Make
                        final RespectOperationDefault opRequested = RespectOperationDefault.make(
                                TupleCentreOpType.OUT, LogicTuples.newInstance(
                                        "cmd_result", TupleArguments.newValueArgument("destroy"),
                                        TupleArguments.newValueArgument("ok")), null);
                        // InputEvent Creation
                        final InputEvent ev = new InputEvent(this.nodeManAid,
                                opRequested, this.config,
                                System.currentTimeMillis(), null);
                        TupleCentreContainer.doBlockingOperation(ev);
                    } catch (final InvalidLogicTupleException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                    // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                    // .outCode(), this.nodeManAid, this.config,
                    // LogicTuples.newInstance("cmd_result", TupleArguments.newValueArgument("destroy"),
                    // TupleArguments.newValueArgument("ok")));
                } else {
                    try {
                        // Operation Make
                        final RespectOperationDefault opRequested = RespectOperationDefault.make(
                                TupleCentreOpType.OUT, LogicTuples.newInstance(
                                        "cmd_result", TupleArguments.newValueArgument("destroy"),
                                        TupleArguments.newValueArgument("failed")), null);
                        // InputEvent Creation
                        final InputEvent ev = new InputEvent(this.nodeManAid,
                                opRequested, this.config,
                                System.currentTimeMillis(), null);
                        TupleCentreContainer.doBlockingOperation(ev);
                    } catch (final InvalidLogicTupleException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                    // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                    // .outCode(), this.nodeManAid, this.config,
                    // LogicTuples.newInstance("cmd_result", TupleArguments.newValueArgument("destroy"),
                    // TupleArguments.newValueArgument("failed")));
                }
                break;
            case "enable_persistency":
                try {
                    NodeManagementAgent.log("Enabling persistency...");
                    this.node.enablePersistency(LogicTuples.newInstance(cmd.getArg(0)));
                    // Operation Make
                    final RespectOperationDefault opRequested = RespectOperationDefault.make(
                            TupleCentreOpType.OUT, LogicTuples.newInstance(
                                    "cmd_result", cmd, TupleArguments.newValueArgument("ok")), null);
                    // InputEvent Creation
                    final InputEvent ev = new InputEvent(this.nodeManAid,
                            opRequested, this.config, System.currentTimeMillis(),
                            null);
                    TupleCentreContainer.doBlockingOperation(ev);
                    NodeManagementAgent.log("...persistency enabled.");
                } catch (final InvalidLogicTupleException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
                // this.nodeManAid, this.config, LogicTuples.newInstance("cmd_result",
                // cmd, TupleArguments.newValueArgument("ok")));
                break;
            case "disable_persistency":
                try {
                    NodeManagementAgent.log("Disabling persistency...");
                    this.node.disablePersistency(LogicTuples.newInstance(cmd.getArg(0)));
                    // Operation Make
                    final RespectOperationDefault opRequested = RespectOperationDefault.make(
                            TupleCentreOpType.OUT, LogicTuples.newInstance(
                                    "cmd_result", TupleArguments.newValueArgument("disable_persistency"),
                                    TupleArguments.newValueArgument("ok")), null);
                    // InputEvent Creation
                    final InputEvent ev = new InputEvent(this.nodeManAid,
                            opRequested, this.config, System.currentTimeMillis(),
                            null);
                    TupleCentreContainer.doBlockingOperation(ev);
                    NodeManagementAgent.log("...persistency disabled.");
                } catch (final InvalidLogicTupleException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
                // this.nodeManAid, this.config, LogicTuples.newInstance("cmd_result",
                // TupleArguments.newValueArgument("disable_persistency"), TupleArguments.newValueArgument("ok")));
                break;
            case "enable_observability":
                this.node.activateObservability();
                break;
            case "disable_observability":
                this.node.deactivateObservability();
                break;
        }
    }
}
