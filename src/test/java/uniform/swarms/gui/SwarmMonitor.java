/**
 *
 */
package uniform.swarms.gui;

import java.awt.Component;
import java.util.List;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tucson.api.TucsonOperation;
import alice.tucson.api.acc.BulkSyncACC;
import alice.tucson.api.acc.NegotiationACC;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

/**
 * @author ste
 *
 */
public class SwarmMonitor {

    final SwarmComponent component;
    private final JButton[] bs;
    private final TucsonTupleCentreId[] tcids;
    private BulkSyncACC acc;

    /**
     * @param c the parent JComponent
     */
    public SwarmMonitor(final SwarmComponent c) {
        this.component = c;
        final Component[] cs = this.component.getComponents();
        this.bs = new JButton[cs.length - 3];
        this.tcids = new TucsonTupleCentreId[cs.length - 3];
        try {
            for (int i = 1; i < (cs.length - 2); i++) {
                this.bs[i - 1] = (JButton) cs[i];
                this.tcids[i - 1] = new TucsonTupleCentreId(
                        this.bs[i - 1].getName(), "localhost", "" + (20504 + i));
                SwarmMonitor.log("" + this.bs[i - 1].getName());
                SwarmMonitor.log("" + this.tcids[i - 1].getName() + ":"
                        + this.tcids[i - 1].getPort());
            }
            NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(new TucsonAgentId("tcsMonitor"));
            this.acc = negAcc.playDefaultRole();
        } catch (TucsonInvalidAgentIdException
                | TucsonInvalidTupleCentreIdException
                | TucsonOperationNotPossibleException
                | UnreachableNodeException | OperationTimeOutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void performUpdate() {
        // new Thread(() -> {
        final int[] pheromones = SwarmMonitor.this.smell();
        for (int i = 0; i < SwarmMonitor.this.bs.length; i++) {
            SwarmMonitor.this.bs[i].setText("[TC-"
                    + SwarmMonitor.this.bs[i].getName() + "] : "
                    + pheromones[i]);
        }
        // SwingUtilities.invokeLater(() -> SwarmMonitor.this.frame
        // .repaint());
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                SwarmMonitor.this.component.getParent().revalidate();
                SwarmMonitor.this.component.getParent().repaint();
                SwarmMonitor.this.component.revalidate();
                SwarmMonitor.this.component.repaint();
            }
        });
        // }).start();
    }

    private int[] smell() {
        final int[] pheromones = new int[this.tcids.length];
        try {
            TucsonOperation op;
            List<LogicTuple> tuples;
            for (int i = 0; i < this.tcids.length; i++) {
                SwarmMonitor.log("Smelling " + this.tcids[i].getName() + "...");
                op = this.acc.rdAll(this.tcids[i], LogicTuple.parse("nbr(N)"),
                        null);
                if (op.isResultSuccess()) {
                    tuples = op.getLogicTupleListResult();
                    pheromones[i] = tuples.size();
                    SwarmMonitor.log("..." + pheromones[i]
                            + " pheromones found!");
                } else {
                    pheromones[i] = -1;
                    SwarmMonitor
                            .err("Error while smelling for update: <rd_all> failure!");
                }
            }
        } catch (InvalidLogicTupleException
                | TucsonOperationNotPossibleException
                | UnreachableNodeException | OperationTimeOutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pheromones;
    }

    private static void log(final String msg) {
        System.out.println("[TcsMonitor]: " + msg);
    }

    private static void err(final String msg) {
        System.err.println(msg);
    }

}
