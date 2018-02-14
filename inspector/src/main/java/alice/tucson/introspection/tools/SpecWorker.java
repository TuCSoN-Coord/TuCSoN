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
package alice.tucson.introspection.tools;

import java.util.List;

import javax.swing.JTextArea;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tucson.api.EnhancedACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
//import alice.util.jedit.JEditTextArea;

/**
 * @author Roberto D'Elia
 */
public class SpecWorker extends Thread {

    private static String format(final LogicTuple t) {
        final StringBuffer res = new StringBuffer(21);
        try {
            res.append(t.getName()).append("(\n\t");
            res.append(t.getArg(0)).append(",\n\t");
            res.append(t.getArg(1)).append(",\n\t");
            res.append(t.getArg(2)).append("\n).\n");
        } catch (final InvalidOperationException e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    private static String predFormat(final LogicTuple t) {
        final StringBuffer res = new StringBuffer();
        try {
            if (!":-".equals(t.getName())) {
                res.append(t.toString()).append(".\n");
            } else {
                res.append(t.getArg(0)).append(" :-\n    ");
                res.append(t.getArg(1)).append(".\n");
            }
        } catch (final InvalidOperationException e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    private final EnhancedACC context;
    private final EditSpec form;
    private final JTextArea inputSpec;
    /** Kind of operation */
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
        if (this.operation == "get") {
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
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                // EditSpec.outputState.setText(e.toString());
            } catch (final UnreachableNodeException e) {
                e.printStackTrace();
                // EditSpec.outputState.setText("TuCSoN Node is unreachable.");
            } catch (final OperationTimeOutException e) {
                e.printStackTrace();
                // EditSpec.outputState.setText("TuCSoN operation timeout exceeded.");
            }
        }
        if (this.operation == "set") {
            final String spec = this.inputSpec.getText();
            try {
                if (spec.isEmpty()) {
                    this.context.setS(this.tid, LogicTuple.parse("[]"),
                            (Long) null);
                } else {
                    this.context.setS(this.tid, spec, (Long) null);
                }
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                // EditSpec.outputState.setText(e.toString());
            } catch (final UnreachableNodeException e) {
                e.printStackTrace();
                // EditSpec.outputState.setText("TuCSoN Node is unreachable.");
            } catch (final OperationTimeOutException e) {
                e.printStackTrace();
                // EditSpec.outputState.setText("TuCSoN operation timeout exceeded.");
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                // EditSpec.outputState.setText("Invalid ReSpecT specification given.");
            }
            this.form.setCompletion();
        }
    }
}
