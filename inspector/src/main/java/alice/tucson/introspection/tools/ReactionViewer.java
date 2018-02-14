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

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Unknown...
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public class ReactionViewer extends javax.swing.JFrame {

    private static final long serialVersionUID = -8708893837692939114L;
    private javax.swing.JCheckBox checkLogEnable;
    private javax.swing.JTextField inputFileLog;
    private javax.swing.JScrollPane jScrollPane1;
    private final InspectorGUI mainForm;
    private javax.swing.JTextArea outputArea;
    private javax.swing.JTextField outputState;

    /**
     * Creates new form TupleForm
     *
     * @param mf
     *            the inspector GUI
     */
    public ReactionViewer(final InspectorGUI mf) {
        super();
        this.initComponents();
        this.pack();
        this.mainForm = mf;
        this.setTitle("Triggered ReSpecT reaction set of tuplecentre < "
                + this.mainForm.tid.getName() + "@"
                + this.mainForm.tid.getNode() + ":"
                + this.mainForm.tid.getPort() + " >");
        this.setSize(520, 460);
        this.inputFileLog.setText(this.mainForm.agent.logReactionFilename);
        this.outputState
                .setText("Ready for ReSpecT reactions triggering notification.");
    }

    /**
     *
     * @param st
     *            the message to append
     */
    public void appendText(final String st) {
        this.outputArea.append(st);
    }

    private void buttonBrowseActionPerformed() {
        final javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        final int returnVal = chooser.showOpenDialog(this);
        if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            final java.io.File file = chooser.getSelectedFile();
            if (file != null) {
                final String name = file.getAbsolutePath();
                this.inputFileLog.setText(name);
                this.mainForm.agent.changeLogReactionFile(name);
            }
        }
    }

    private void checkLogEnableActionPerformed() {
        if (this.checkLogEnable.isSelected()) {
            this.mainForm.agent.loggingReactions = true;
            this.outputState.setText("Please choose the output log file...");
        } else {
            this.mainForm.agent.loggingReactions = false;
        }
    }

    /** Exit the Application */
    private void exitForm() {
        this.mainForm.onReactionViewerExit();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        this.jScrollPane1 = new javax.swing.JScrollPane();
        this.outputArea = new javax.swing.JTextArea();
        final JTabbedPane jTabbedPane1 = new JTabbedPane();
        final JPanel jPanel5 = new JPanel();
        final JPanel jPanel7 = new JPanel();
        final JLabel jLabel1 = new JLabel();
        this.inputFileLog = new javax.swing.JTextField();
        final JButton buttonBrowse = new JButton();
        this.checkLogEnable = new javax.swing.JCheckBox();
        this.outputState = new javax.swing.JTextField();
        final JPanel jPanel10 = new JPanel();
        this.getContentPane().setLayout(new java.awt.GridBagLayout());
        this.setTitle("reactions executed");
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(final java.awt.event.WindowEvent evt) {
                ReactionViewer.this.exitForm();
            }
        });
        this.outputArea.setEditable(false);
        this.outputArea.setFont(new java.awt.Font("Courier New", 0, 12));
        this.jScrollPane1.setViewportView(this.outputArea);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 100.0;
        gridBagConstraints.weighty = 100.0;
        this.getContentPane().add(this.jScrollPane1, gridBagConstraints);
        jTabbedPane1.setFont(new java.awt.Font("Arial", 0, 11));
        jPanel5.setLayout(new java.awt.GridBagLayout());
        jPanel7.setLayout(new java.awt.GridBagLayout());
        jPanel7.setBorder(new javax.swing.border.TitledBorder(
                new javax.swing.border.EtchedBorder(), "store"));
        jPanel7.setFont(new java.awt.Font("Arial", 0, 11));
        jLabel1.setFont(new java.awt.Font("Arial", 0, 11));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("dump observations on file: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 10.0;
        jPanel7.add(jLabel1, gridBagConstraints);
        this.inputFileLog
                .addActionListener(new java.awt.event.ActionListener() {

            @Override
                    public void actionPerformed(
                            final java.awt.event.ActionEvent evt) {
                        ReactionViewer.this.inputFileLogActionPerformed();
                    }
                });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 100.0;
        jPanel7.add(this.inputFileLog, gridBagConstraints);
        buttonBrowse.setFont(new java.awt.Font("Arial", 0, 11));
        buttonBrowse.setText("Browse");
        buttonBrowse.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                ReactionViewer.this.buttonBrowseActionPerformed();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 5.0;
        jPanel7.add(buttonBrowse, gridBagConstraints);
        this.checkLogEnable
                .addActionListener(new java.awt.event.ActionListener() {

            @Override
                    public void actionPerformed(
                            final java.awt.event.ActionEvent evt) {
                        ReactionViewer.this.checkLogEnableActionPerformed();
                    }
                });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanel7.add(this.checkLogEnable, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 90.0;
        gridBagConstraints.weighty = 30.0;
        jPanel5.add(jPanel7, gridBagConstraints);
        jTabbedPane1.addTab("Log", null, jPanel5, "");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 100.0;
        gridBagConstraints.weighty = 10.0;
        this.getContentPane().add(jTabbedPane1, gridBagConstraints);
        jPanel10.setLayout(new java.awt.GridBagLayout());
        this.outputState.setBackground(Color.CYAN);
        this.outputState.setEditable(false);
        this.outputState.setBorder(new javax.swing.border.LineBorder(
                new java.awt.Color(0, 0, 0)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 100.0;
        gridBagConstraints.weighty = 100.0;
        jPanel10.add(this.outputState, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 100.0;
        gridBagConstraints.weighty = 5.0;
        this.getContentPane().add(jPanel10, gridBagConstraints);
    }

    private void inputFileLogActionPerformed() {
        this.mainForm.agent.changeLogReactionFile(this.inputFileLog.getText());
    }
}
