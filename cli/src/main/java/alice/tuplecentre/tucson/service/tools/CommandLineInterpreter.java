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
package alice.tuplecentre.tucson.service.tools;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.Objects;

import alice.tuplecentre.tucson.api.TucsonAgentIdDefault;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.acc.EnhancedACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.service.TucsonInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command Line Interpreter. Can be booted with a TuCSoN agent Identifier or using a
 * default assigned one (both passed to the CLIAgent). Gets a TuCSoN ACC from
 * TucsonMetaACC then spawns the CLIAgent who manages user input.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public final class CommandLineInterpreter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    private static final int DEF_PORT = 20504;

    /**
     * @param args the arguments to be given to the CLI
     */
    public static void main(final String[] args) {
        CommandLineInterpreter
                .log("--------------------------------------------------------------------------------");
        CommandLineInterpreter.log("Booting TuCSoN Command Line Intepreter...");
        if (alice.util.Tools.isOpt(args, "-?")
                || alice.util.Tools.isOpt(args, "-help")) {
            CommandLineInterpreter
                    .log("Arguments: {-aid <agent_identifier>} {-netid <node_address>} {-portno <portno>} {-? | -help}");
        } else {
            String aid;
            String node;
            int port;
            if (alice.util.Tools.isOpt(args, "-aid")) {
                aid = alice.util.Tools.getOpt(args, "-aid");
            } else {
                aid = "cli" + System.currentTimeMillis();
            }
            if (alice.util.Tools.isOpt(args, "-netid")) {
                node = alice.util.Tools.getOpt(args, "-netid");
            } else {
                node = "localhost";
            }
            if (alice.util.Tools.isOpt(args, "-portno")) {
                port = Integer.parseInt(alice.util.Tools
                        .getOpt(args, "-portno"));
            } else {
                port = CommandLineInterpreter.DEF_PORT;
            }
            CommandLineInterpreter.log("Version "
                    + TucsonInfo.getVersion());
            CommandLineInterpreter
                    .log("--------------------------------------------------------------------------------");
            CommandLineInterpreter.log(new Date().toString());
            CommandLineInterpreter
                    .log("Demanding for TuCSoN default ACC on port < " + port
                            + " >...");
            EnhancedACC context = null;
            try {
                context = TucsonMetaACC.getContext(new TucsonAgentIdDefault(aid),
                        node, port);
                Objects.requireNonNull(context).enterACC();
            } catch (final TucsonInvalidAgentIdException | UnreachableNodeException | TucsonInvalidTupleCentreIdException e) {
                LOGGER.error(e.getMessage(), e);
                System.exit(-1);
            }
            CommandLineInterpreter.log("Spawning CLI TuCSoN agent...");
            CommandLineInterpreter
                    .log("--------------------------------------------------------------------------------");
            new Thread(new CLIAgent(context, node, port)).start();
        }
    }

    private static void log(final String s) {
        LOGGER.info("[CommandLineInterpreter]: " + s);
    }

    private CommandLineInterpreter() {
        /*
         *
         */
    }
}
