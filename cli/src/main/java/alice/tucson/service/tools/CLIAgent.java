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
package alice.tucson.service.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import alice.tucson.api.TucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.acc.EnhancedACC;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.parsing.TucsonOpParser;
import alice.tucson.service.TucsonCmd;
import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTupleOpManager;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuprolog.Parser;

/**
 * Command Line Interpreter TuCSoN agent. Waits for user input, properly parses
 * the issued command, then process it calling the corresponding method on the
 * ACC (gained through constructor). It blocks until TuCSoN reply comes.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class CLIAgent extends alice.util.Automaton {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static void busy() {
        System.out.print("[CLI]: ... ");
    }

    private static void error(final String s) {
        System.err.println("[CLI]: Unknown command <" + s + ">");
    }

    private static void input() {
        System.out.print("[CLI]: ?> ");
    }

    private static void log(final String s) {
        System.out.println("[CLI]: " + s);
    }

    private static void prompt(final String s) {
        System.out.println(" -> " + s);
    }

    private final EnhancedACC context;
    private final String node;
    private final int port;
    private final BufferedReader stdin;

    /**
     * @param ctx the ACC held by the CLI
     * @param n   the network address of the TuCSoN node to be considered
     *            default
     * @param p   the listening port of the TuCSoN node to be considered default
     */
    public CLIAgent(final EnhancedACC ctx, final String n, final int p) {
        super();
        this.context = ctx;
        this.node = n;
        this.port = p;
        this.stdin = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void boot() {
        CLIAgent.log("CLI agent listening to user...");
        this.become("goalRequest");
    }

    /**
     * Main reasoning cycle
     */
    public void goalRequest() {
        String st = "";
        while ("".equals(st)) {
            CLIAgent.input();
            try {
                st = this.stdin.readLine();
            } catch (final IOException ex) {
                System.err.println("[CLI]: " + ex);
                new CLIAgent(this.context, this.node, this.port).boot();
            }
        }
        final TucsonOpParser parser = new TucsonOpParser(st, this.node,
                this.port);
        try {
            parser.parse();
        } catch (final TucsonInvalidTupleCentreIdException e) {
            e.printStackTrace();
        }
        final TucsonCmd cmd = parser.getCmd();
        final TucsonTupleCentreId tid = parser.getTid();
        if (cmd == null || tid == null) {
            CLIAgent.error(st);
        } else if ("quit".equals(cmd.getPrimitive())
                || "exit".equals(cmd.getPrimitive())) {
            try {
                CLIAgent.log("Releasing ACC held (if any)...");
                this.context.exit();
            } catch (final TucsonOperationNotPossibleException ex) {
                System.err.println("[CLI]: " + ex);
            } finally {
                CLIAgent.log("I'm done, have a nice day :)");
                this.become("end");
            }
        } else {
            /**
             * Tokenize TuCSoN primitive & involved argument
             */
            final String methodName = cmd.getPrimitive();
            final String tuple = cmd.getArg();
            try {
                /**
                 * Admissible Ordinary primitives
                 */
                // what about timeout? it returns null too...how to discriminate
                // inp/rdp failure?
                if ("spawn".equals(methodName)) {
                    final LogicTuple t = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.spawn(tid, t,
                            Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success");
                    } else {
                        CLIAgent.prompt("failure");
                    }
                } else if ("out".equals(methodName)) {
                    final LogicTuple t = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.out(tid, t,
                            Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("in".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.in(tid, templ,
                            Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("rd".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.rd(tid, templ,
                            Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("inp".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.inp(tid, templ,
                            Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("rdp".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.rdp(tid, templ,
                            Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("no".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.no(tid, templ,
                            Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("nop".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.nop(tid, templ,
                            Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("set".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.set(tid, templ,
                            Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: "
                                + op.getLogicTupleListResult());
                    } else {
                        CLIAgent.prompt("failure: "
                                + op.getLogicTupleListResult());
                    }
                } else if ("get".equals(methodName)) {
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.get(tid,
                            Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: "
                                + op.getLogicTupleListResult());
                    } else {
                        CLIAgent.prompt("failure: "
                                + op.getLogicTupleListResult());
                    }
                } else if ("out_all".equals(methodName)) {
                    final LogicTuple t = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.outAll(tid, t,
                            Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: "
                                + op.getLogicTupleListResult());
                    } else {
                        CLIAgent.prompt("failure: "
                                + op.getLogicTupleListResult());
                    }
                } else if ("rd_all".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.rdAll(tid, templ,
                            (Long) null);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: "
                                + op.getLogicTupleListResult());
                    } else {
                        CLIAgent.prompt("failure: "
                                + op.getLogicTupleListResult());
                    }
                } else if ("no_all".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.noAll(tid, templ,
                            (Long) null);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: "
                                + op.getLogicTupleListResult());
                    } else {
                        CLIAgent.prompt("failure: "
                                + op.getLogicTupleListResult());
                    }
                } else if ("in_all".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.inAll(tid, templ,
                            (Long) null);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: "
                                + op.getLogicTupleListResult());
                    } else {
                        CLIAgent.prompt("failure: "
                                + op.getLogicTupleListResult());
                    }
                } else if ("urd".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.urd(tid, templ,
                            (Long) null);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("uno".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.uno(tid, templ,
                            (Long) null);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("urdp".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.urdp(tid, templ,
                            (Long) null);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("unop".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.unop(tid, templ,
                            (Long) null);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("uin".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.uin(tid, templ,
                            (Long) null);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("uinp".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.parse(tuple);
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.uinp(tid, templ,
                            (Long) null);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("out_s".equals(methodName)) {
                    final LogicTuple t = LogicTuples.newInstance(Parser.parseSingleTerm(
                            "reaction(" + tuple + ")",
                            new LogicTupleOpManager()));
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.outS(tid,
                            LogicTuples.newInstance(t.getArg(0)),
                            LogicTuples.newInstance(t.getArg(1)),
                            LogicTuples.newInstance(t.getArg(2)), Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("in_s".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.newInstance(
                            Parser.parseSingleTerm("reaction(" + tuple + ")",
                                    new LogicTupleOpManager()));
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.inS(tid,
                            LogicTuples.newInstance(templ.getArg(0)), LogicTuples.newInstance(
                                    templ.getArg(1)),
                            LogicTuples.newInstance(templ.getArg(2)), Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("rd_s".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.newInstance(
                            Parser.parseSingleTerm("reaction(" + tuple + ")",
                                    new LogicTupleOpManager()));
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.rdS(tid,
                            LogicTuples.newInstance(templ.getArg(0)), LogicTuples.newInstance(
                                    templ.getArg(1)),
                            LogicTuples.newInstance(templ.getArg(2)), Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("inp_s".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.newInstance(
                            Parser.parseSingleTerm("reaction(" + tuple + ")",
                                    new LogicTupleOpManager()));
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.inpS(tid,
                            LogicTuples.newInstance(templ.getArg(0)), LogicTuples.newInstance(
                                    templ.getArg(1)),
                            LogicTuples.newInstance(templ.getArg(2)), Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("rdp_s".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.newInstance(
                            Parser.parseSingleTerm("reaction(" + tuple + ")",
                                    new LogicTupleOpManager()));
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.rdpS(tid,
                            LogicTuples.newInstance(templ.getArg(0)), LogicTuples.newInstance(
                                    templ.getArg(1)),
                            LogicTuples.newInstance(templ.getArg(2)), Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("no_s".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.newInstance(
                            Parser.parseSingleTerm("reaction(" + tuple + ")",
                                    new LogicTupleOpManager()));
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.noS(tid,
                            LogicTuples.newInstance(templ.getArg(0)), LogicTuples.newInstance(
                                    templ.getArg(1)),
                            LogicTuples.newInstance(templ.getArg(2)), Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("nop_s".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.newInstance(
                            Parser.parseSingleTerm("reaction(" + tuple + ")",
                                    new LogicTupleOpManager()));
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.nopS(tid,
                            LogicTuples.newInstance(templ.getArg(0)), LogicTuples.newInstance(
                                    templ.getArg(1)),
                            LogicTuples.newInstance(templ.getArg(2)), Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: " + op.getLogicTupleResult());
                    } else {
                        CLIAgent.prompt("failure: " + op.getLogicTupleResult());
                    }
                } else if ("set_s".equals(methodName)) {
                    final LogicTuple templ = LogicTuples.newInstance(
                            Parser.parseSingleTerm(tuple,
                                    new LogicTupleOpManager()));
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.setS(tid, templ,
                            Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: "
                                + op.getLogicTupleListResult());
                    } else {
                        CLIAgent.prompt("failure: "
                                + op.getLogicTupleListResult());
                    }
                } else if ("get_s".equals(methodName)) {
                    CLIAgent.busy();
                    final TucsonOperation op = this.context.getS(tid,
                            Long.MAX_VALUE);
                    if (op.isResultSuccess()) {
                        CLIAgent.prompt("success: "
                                + op.getLogicTupleListResult());
                    } else {
                        CLIAgent.prompt("failure: "
                                + op.getLogicTupleListResult());
                    }
                } else if ("help".equals(methodName)
                        || "man".equals(methodName)
                        || "syntax".equals(methodName)) {
                    CLIAgent.log("--------------------------------------------------------------------------------");
                    CLIAgent.log("TuCSoN CLI Syntax:");
                    CLIAgent.log("");
                    CLIAgent.log("\t\ttcName@ipAddress:port ? CMD");
                    CLIAgent.log("");
                    CLIAgent.log("where CMD can be:");
                    CLIAgent.log("");
                    CLIAgent.log("\t\tout(Tuple)");
                    CLIAgent.log("\t\tin(TupleTemplate)");
                    CLIAgent.log("\t\trd(TupleTemplate)");
                    CLIAgent.log("\t\tno(TupleTemplate)");
                    CLIAgent.log("\t\tinp(TupleTemplate)");
                    CLIAgent.log("\t\trdp(TupleTemplate)");
                    CLIAgent.log("\t\tnop(TupleTemplate)");
                    CLIAgent.log("\t\tget()");
                    CLIAgent.log("\t\tset([Tuple1, ..., TupleN])");
                    CLIAgent.log("\t\tspawn(exec('Path.To.Java.Class.class')) | spawn(solve('Path/To/Prolog/Theory.pl', Goal))");
                    CLIAgent.log("\t\tin_all(TupleTemplate, TupleList)");
                    CLIAgent.log("\t\trd_all(TupleTemplate, TupleList)");
                    CLIAgent.log("\t\tno_all(TupleTemplate, TupleList)");
                    CLIAgent.log("\t\tuin(TupleTemplate)");
                    CLIAgent.log("\t\turd(TupleTemplate)");
                    CLIAgent.log("\t\tuno(TupleTemplate)");
                    CLIAgent.log("\t\tuinp(TupleTemplate)");
                    CLIAgent.log("\t\turdp(TupleTemplate)");
                    CLIAgent.log("\t\tunop(TupleTemplate)");
                    CLIAgent.log("\t\tout_s(Event,Guard,Reaction)");
                    CLIAgent.log("\t\tin_s(EventTemplate, GuardTemplate, ReactionTemplate)");
                    CLIAgent.log("\t\trd_s(EventTemplate, GuardTemplate, ReactionTemplate)");
                    CLIAgent.log("\t\tinp_s(EventTemplate, GuardTemplate ,ReactionTemplate)");
                    CLIAgent.log("\t\trdp_s(EventTemplate, GuardTemplate, ReactionTemplate)");
                    CLIAgent.log("\t\tno_s(EventTemplate, GuardTemplate, ReactionTemplate)");
                    CLIAgent.log("\t\tnop_s(EventTemplate, GuardTemplate, ReactionTemplate)");
                    CLIAgent.log("\t\tget_s()");
                    CLIAgent.log("\t\tset_s([(Event1,Guard1,Reaction1), ..., (EventN,GuardN,ReactionN)])");
                    CLIAgent.log("--------------------------------------------------------------------------------");
                } else if ("o/".equals(methodName)) {
                    CLIAgent.prompt("\\o");
                } else if ("\\o".equals(methodName)) {
                    CLIAgent.prompt("o/");
                } else if ("hi".equalsIgnoreCase(methodName)) {
                    CLIAgent.prompt("Hi there!");
                } else {
                    CLIAgent.error(methodName);
                }
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final UnreachableNodeException e) {
                e.printStackTrace();
            } catch (final OperationTimeOutException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        this.become("goalRequest");
    }
}
