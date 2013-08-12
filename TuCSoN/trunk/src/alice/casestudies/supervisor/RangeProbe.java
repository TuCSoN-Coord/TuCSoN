package alice.casestudies.supervisor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import alice.respect.core.TransducerManager;
import alice.respect.situated.AbstractProbeId;
import alice.respect.situated.ISimpleProbe;
import alice.respect.situated.TransducerId;
import alice.respect.situated.TransducerStandardInterface;

public class RangeProbe implements ActionListener, ISimpleProbe {

    private final SupervisorGUI gui;
    private final AbstractProbeId id;
    private TransducerId tId;
    private TransducerStandardInterface transducer;

    public RangeProbe(final AbstractProbeId i) {
        this.id = i;
        this.gui = SupervisorGUI.getLightGUI();
        this.gui.addRangeButtonActionListener(this);
    }

    public void actionPerformed(final ActionEvent arg0) {
        try {
            if ("btnMin".equals(((javax.swing.JButton) arg0.getSource())
                    .getName())) {
                if (this.transducer == null) {
                    TransducerManager.getTransducerManager();
                    this.transducer =
                            TransducerManager.getTransducer(this.tId
                                    .getAgentName());
                }
                this.transducer.notifyEnvEvent("min",
                        Integer.parseInt(this.gui.getMinValue()));
            } else if ("btnMax".equals(((javax.swing.JButton) arg0.getSource())
                    .getName())) {
                if (this.transducer == null) {
                    TransducerManager.getTransducerManager();
                    this.transducer =
                            TransducerManager.getTransducer(this.tId
                                    .getAgentName());
                }
                this.transducer.notifyEnvEvent("max",
                        Integer.parseInt(this.gui.getMaxValue()));
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public AbstractProbeId getIdentifier() {
        return this.id;
    }

    public TransducerId getTransducer() {
        return this.tId;
    }

    public boolean readValue(final String key) {
        try {
            if ("min".equals(key)) {
                if (this.transducer == null) {
                    TransducerManager.getTransducerManager();
                    this.transducer =
                            TransducerManager.getTransducer(this.tId
                                    .getAgentName());
                }
                this.transducer.notifyEnvEvent(key,
                        Integer.parseInt(this.gui.getMinValue()));
                return true;
            } else if ("max".equals(key)) {
                if (this.transducer == null) {
                    TransducerManager.getTransducerManager();
                    this.transducer =
                            TransducerManager.getTransducer(this.tId
                                    .getAgentName());
                }
                this.transducer.notifyEnvEvent(key,
                        Integer.parseInt(this.gui.getMaxValue()));
                return true;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setTransducer(final TransducerId t) {
        this.tId = t;
    }

    public boolean writeValue(final String key, final int value) {
        return false;
    }

}
