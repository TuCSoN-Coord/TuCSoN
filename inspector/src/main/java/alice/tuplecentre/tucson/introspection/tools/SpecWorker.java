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
package alice.tuplecentre.tucson.introspection.tools;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextArea;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.acc.EnhancedACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import alice.util.jedit.JEditTextArea;

/**
 * @author Roberto D'Elia
 */
public class SpecWorker extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static String format(final LogicTuple t) {
        final StringBuilder res = new StringBuilder(21);
        try {
            res.append(t.getName()).append("(\n\t");
            res.append(t.getArg(0)).append(",\n\t");
            res.append(t.getArg(1)).append(",\n\t");
            res.append(t.getArg(2)).append("\n).\n");
        } catch (final InvalidOperationException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return res.toString();
    }

    private static String predFormat(final LogicTuple t) {
        final StringBuilder res = new StringBuilder();
        try {
            if (!":-".equals(t.getName())) {
                res.append(t.toString()).append(".\n");
            } else {
                res.append(t.getArg(0)).append(" :-\n    ");
                res.append(t.getArg(1)).append(".\n");
            }
        } catch (final InvalidOperationException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return res.toString();
    }

    private final EnhancedACC context;
    private final EditSpec form;
    private final JTextArea inputSpec;
    /**
     * Kind copyOf operation
     */
    private final String operation;
    private final TucsonTupleCentreId tid;

    public SpecWorker(final String op, final EnhancedACC c,
                      final TucsonTupleCentreId ttcid, final EditSpec editSpec,
                      final JTextArea input) {
        this.operation = op;
        this.context = c;
        this.tid = ttcid;
        this.form = editSpec;
        this.inputSpec = input;
    }

    @Override
    public void run() {
        if (Objects.equals(this.operation, "get")) {
            try {
                final StringBuffer spec = new StringBuffer();
                final List<LogicTuple> list = this.context.getS(this.tid,
                        (Long) null).getLogicTupleListResult();
                for (final LogicTuple t : list) {
                    if ("reaction".equals(t.getName())) {
                        spec.append(SpecWorker.format(t));
                    } else {
                        spec.append(SpecWorker.predFormat(t));
                    }
                }
                this.form.getCompletion(spec);
            } catch (final TucsonOperationNotPossibleException | OperationTimeOutException | UnreachableNodeException e) {
                LOGGER.error(e.getMessage(), e);
                // EditSpec.outputState.setText(e.toString());
            }
        }
        if (Objects.equals(this.operation, "set")) {
            final String spec = this.inputSpec.getText();
            try {
                if (spec.isEmpty()) {
                    this.context.setS(this.tid, LogicTuple.parse("[]"),
                            (Long) null);
                } else {
                    this.context.setS(this.tid, spec, (Long) null);
                }
            } catch (final TucsonOperationNotPossibleException | InvalidLogicTupleException | OperationTimeOutException | UnreachableNodeException e) {
                LOGGER.error(e.getMessage(), e);
                // EditSpec.outputState.setText(e.toString());
            }
            this.form.setCompletion();
        }
    }
}
