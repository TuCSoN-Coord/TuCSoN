/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.tucson.introspection.tools;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tucson.introspection.GetSnapshotMsg;
import alice.tucson.introspection.InspectorProtocol;

import java.awt.Color;
import java.util.ArrayList;

public class EventViewer extends javax.swing.JFrame{
	
	private static final long serialVersionUID = -9040554109345956651L;
	private Inspector mainForm;
	private alice.tucson.introspection.InspectorContext context;

	/** Creates new form TupleForm */
	public EventViewer(Inspector mainForm_){
		initComponents();
		pack();
		mainForm = mainForm_;
		context = mainForm.agent.getContext();
		setTitle("Pending TuCSoN operations set of tuplecentre < " + mainForm.tid.getName() + 
				"@" + mainForm.tid.getNode() + ":" + mainForm.tid.getPort() + " >");
		setSize(520, 460);
		radioProactive.setSelected(true);
		radioReactive.setSelected(false);
		buttonGet.setEnabled(false);
		buttonAcceptPattern.setEnabled(false);
		buttonAcceptFilterLog.setEnabled(false);
		inputFileLog.setText(mainForm.agent.logQueryFilename);
		outputState.setText("Ready for pending operations inspection.");
	}

	public void setText(String st){
		outputArea.setText(st);
	}

	public void setVMTime(long l){
		outputVmTime.setText(new Long(l).toString());
	}

	public void setLocalTime(long l){
		outputTime.setText(new Long(l).toString());
	}

	public void setNItems(long l){
		outputNoItems.setText(new Long(l).toString());
	}

	private void initComponents(){
		
		java.awt.GridBagConstraints gridBagConstraints;

		jScrollPane1 = new javax.swing.JScrollPane();
		outputArea = new javax.swing.JTextArea();
		jTabbedPane1 = new javax.swing.JTabbedPane();
		jPanel1 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		radioReactive = new javax.swing.JRadioButton();
		radioProactive = new javax.swing.JRadioButton();
		buttonGet = new javax.swing.JButton();
		jPanel3 = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		checkFilterView = new javax.swing.JCheckBox();
		buttonAcceptPattern = new javax.swing.JButton();
		inputFilterView = new javax.swing.JTextField();
		jPanel5 = new javax.swing.JPanel();
		jPanel6 = new javax.swing.JPanel();
		checkFilterLog = new javax.swing.JCheckBox();
		buttonAcceptFilterLog = new javax.swing.JButton();
		inputFilterLog = new javax.swing.JTextField();
		jPanel8 = new javax.swing.JPanel();
		jLabel3 = new javax.swing.JLabel();
		inputFileLog = new javax.swing.JTextField();
		buttonBrowse = new javax.swing.JButton();
		checkLogEnable = new javax.swing.JCheckBox();
		jPanel7 = new javax.swing.JPanel();
		jPanel11 = new javax.swing.JPanel();
		jButton1 = new javax.swing.JButton();
		jLabel5 = new javax.swing.JLabel();
		jPanel10 = new javax.swing.JPanel();
		outputState = new javax.swing.JTextField();
		jPanel9 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		outputTime = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();
		outputNoItems = new javax.swing.JTextField();
		jLabel4 = new javax.swing.JLabel();
		outputVmTime = new javax.swing.JTextField();

		getContentPane().setLayout(new java.awt.GridBagLayout());

		setTitle("W Inspector");
		addWindowListener(new java.awt.event.WindowAdapter(){
			public void windowClosing(java.awt.event.WindowEvent evt){
				exitForm(evt);
			}
		});

		outputArea.setEditable(false);
		outputArea.setFont(new java.awt.Font("Courier New", 0, 12));
		jScrollPane1.setViewportView(outputArea);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		getContentPane().add(jScrollPane1, gridBagConstraints);

		jTabbedPane1.setFont(new java.awt.Font("Arial", 0, 11));
		jPanel1.setLayout(new java.awt.GridBagLayout());

		jPanel2.setLayout(new java.awt.GridBagLayout());
		jPanel2.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "type"));
		jPanel2.setFont(new java.awt.Font("Arial", 0, 11));
		radioReactive.setFont(new java.awt.Font("Arial", 0, 11));
		radioReactive.setText("REACTIVE: update observations only upon request.");
		radioReactive.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
		radioReactive.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				radioReactiveActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 30.0;
		jPanel2.add(radioReactive, gridBagConstraints);

		radioProactive.setFont(new java.awt.Font("Arial", 0, 11));
		radioProactive.setText("PROACTIVE: update observations as soon as events happen.");
		radioProactive.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
		radioProactive.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				radioProactiveActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 100.0;
		jPanel2.add(radioProactive, gridBagConstraints);

		buttonGet.setFont(new java.awt.Font("Arial", 0, 11));
		buttonGet.setText("Observe!");
		buttonGet.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		buttonGet.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				buttonGetActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.weightx = 70.0;
		jPanel2.add(buttonGet, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		jPanel1.add(jPanel2, gridBagConstraints);

		jTabbedPane1.addTab("Observation", null, jPanel1, "");

		jPanel3.setLayout(new java.awt.GridBagLayout());

		jPanel4.setLayout(new java.awt.GridBagLayout());
		jPanel4.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "filtering"));
		checkFilterView.setFont(new java.awt.Font("Arial", 0, 11));
		checkFilterView.setText("Filter observed tuples using the following template:");
		checkFilterView.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				checkFilterViewActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 10.0;
		jPanel4.add(checkFilterView, gridBagConstraints);

		buttonAcceptPattern.setFont(new java.awt.Font("Arial", 0, 11));
		buttonAcceptPattern.setText("Match!");
		buttonAcceptPattern.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				buttonAcceptPatternActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		jPanel4.add(buttonAcceptPattern, gridBagConstraints);

		inputFilterView.setFont(new java.awt.Font("Courier New", 0, 12));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 50.0;
		jPanel4.add(inputFilterView, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		jPanel3.add(jPanel4, gridBagConstraints);

		jTabbedPane1.addTab("Filter", null, jPanel3, "");

		jPanel5.setLayout(new java.awt.GridBagLayout());

		jPanel6.setLayout(new java.awt.GridBagLayout());
		jPanel6.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "filtering"));
		jPanel6.setFont(new java.awt.Font("Arial", 0, 11));
		checkFilterLog.setFont(new java.awt.Font("Arial", 0, 11));
		checkFilterLog.setText("Filter observed tuples using the following template:");
		checkFilterLog.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				checkFilterLogActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 10.0;
		jPanel6.add(checkFilterLog, gridBagConstraints);

		buttonAcceptFilterLog.setFont(new java.awt.Font("Arial", 0, 11));
		buttonAcceptFilterLog.setText("Match!");
		buttonAcceptFilterLog.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				buttonAcceptFilterLogActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		jPanel6.add(buttonAcceptFilterLog, gridBagConstraints);

		inputFilterLog.setFont(new java.awt.Font("Courier New", 0, 12));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 100.0;
		jPanel6.add(inputFilterLog, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 70.0;
		jPanel5.add(jPanel6, gridBagConstraints);

		jPanel8.setLayout(new java.awt.GridBagLayout());
		jPanel8.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "store"));
		jPanel8.setFont(new java.awt.Font("Arial", 0, 11));
		jLabel3.setFont(new java.awt.Font("Arial", 0, 11));
		jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel3.setText("dump observations on file: ");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 10.0;
		jPanel8.add(jLabel3, gridBagConstraints);
		inputFileLog.setFont(new java.awt.Font("Courier New", 0, 12));
		inputFileLog.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				inputFileLogActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 100.0;
		jPanel8.add(inputFileLog, gridBagConstraints);
		buttonBrowse.setFont(new java.awt.Font("Arial", 0, 11));
		buttonBrowse.setText("Browse");
		buttonBrowse.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				buttonBrowseActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 5.0;
		jPanel8.add(buttonBrowse, gridBagConstraints);
		checkLogEnable.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				checkLogEnableActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		jPanel8.add(checkLogEnable, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 90.0;
		gridBagConstraints.weighty = 30.0;
		jPanel5.add(jPanel8, gridBagConstraints);

		jTabbedPane1.addTab("Log", null, jPanel5, "");

//		jPanel7.setLayout(new java.awt.GridBagLayout());
//		jPanel7.setToolTipText("enforce");
//		
//		jPanel11.setLayout(new java.awt.GridBagLayout());
//		jPanel11.setBorder(new javax.swing.border.EtchedBorder());
//		jPanel11.setPreferredSize(new java.awt.Dimension(53, 40));
//		jButton1.setText("apply");
//		jButton1.setMaximumSize(new java.awt.Dimension(80, 27));
//		jButton1.setMinimumSize(new java.awt.Dimension(80, 27));
//		jButton1.setPreferredSize(new java.awt.Dimension(80, 26));
//		jButton1.addActionListener(new java.awt.event.ActionListener(){
//			public void actionPerformed(java.awt.event.ActionEvent evt){
//				jButton1ActionPerformed(evt);
//			}
//		});
//
//		gridBagConstraints = new java.awt.GridBagConstraints();
//		gridBagConstraints.gridx = 1;
//		gridBagConstraints.gridy = 0;
//		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
//		jPanel11.add(jButton1, gridBagConstraints);
//
//		jLabel5.setText("Enforce pending query set");
//		gridBagConstraints = new java.awt.GridBagConstraints();
//		gridBagConstraints.gridx = 0;
//		gridBagConstraints.gridy = 0;
//		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
//		gridBagConstraints.weightx = 1.0;
//		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
//		jPanel11.add(jLabel5, gridBagConstraints);
//
//		gridBagConstraints = new java.awt.GridBagConstraints();
//		gridBagConstraints.gridx = 0;
//		gridBagConstraints.gridy = 1;
//		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
//		gridBagConstraints.weightx = 1.0;
//		gridBagConstraints.weighty = 1.0;
//		jPanel7.add(jPanel11, gridBagConstraints);
//
//		jTabbedPane1.addTab("Action", null, jPanel7, "");

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 10.0;
		getContentPane().add(jTabbedPane1, gridBagConstraints);

		jPanel10.setLayout(new java.awt.GridBagLayout());

		outputState.setBackground(Color.CYAN);
		outputState.setEditable(false);
		outputState.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		jPanel10.add(outputState, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 5.0;
		getContentPane().add(jPanel10, gridBagConstraints);

		jPanel9.setLayout(new java.awt.GridBagLayout());

//		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
//		jLabel1.setText("local time");
//		gridBagConstraints = new java.awt.GridBagConstraints();
//		gridBagConstraints.gridx = 3;
//		gridBagConstraints.gridy = 1;
//		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
//		gridBagConstraints.weightx = 20.0;
//		jPanel9.add(jLabel1, gridBagConstraints);
//
//		outputTime.setEditable(false);
//		outputTime.setFont(new java.awt.Font("Courier New", 0, 12));
//		outputTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
//		gridBagConstraints = new java.awt.GridBagConstraints();
//		gridBagConstraints.gridx = 4;
//		gridBagConstraints.gridy = 1;
//		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
//		gridBagConstraints.weightx = 40.0;
//		jPanel9.add(outputTime, gridBagConstraints);

		jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel2.setText("# observations: ");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 20.0;
		jPanel9.add(jLabel2, gridBagConstraints);

		outputNoItems.setEditable(false);
		outputNoItems.setFont(new java.awt.Font("Courier New", 0, 12));
		outputNoItems.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 20.0;
		jPanel9.add(outputNoItems, gridBagConstraints);

//		jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
//		jLabel4.setText("vm time");
//		gridBagConstraints = new java.awt.GridBagConstraints();
//		gridBagConstraints.gridx = 1;
//		gridBagConstraints.gridy = 1;
//		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
//		gridBagConstraints.weightx = 20.0;
//		jPanel9.add(jLabel4, gridBagConstraints);
//
//		outputVmTime.setEditable(false);
//		outputVmTime.setFont(new java.awt.Font("Courier New", 0, 12));
//		outputVmTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
//		gridBagConstraints = new java.awt.GridBagConstraints();
//		gridBagConstraints.gridx = 2;
//		gridBagConstraints.gridy = 1;
//		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
//		gridBagConstraints.weightx = 40.0;
//		jPanel9.add(outputVmTime, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 5.0;
		getContentPane().add(jPanel9, gridBagConstraints);

	}

	private String readTuple(String txt){
		String tuple = "";
		while (iCurrentPos < txt.length()){
			if ((txt.charAt(iCurrentPos) != '\n'))
				tuple += txt.charAt(iCurrentPos++);
			else{
				iCurrentPos++;
				return tuple;
			}
		}
		return null;
	}
	
	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt){
		
		try{
			
			String txt = outputArea.getText() + '\n'; 
			ArrayList<LogicTuple> list = new ArrayList<LogicTuple>();
			iCurrentPos = 0; 
			String tuple = readTuple(txt);
		    
			while (tuple!=null){
				if (!tuple.equals("")){
					LogicTuple tu = LogicTuple.parse(tuple); 
					if (tu != null){ 
						list.add(tu);
						tuple = readTuple(txt);
					}else 
						throw new InvalidLogicTupleException(); 					 
				}else 
					tuple = readTuple(txt); 
			} 
			context.setEventSet(list);
			outputState.setText("event set description accepted.");
			
		}catch (InvalidLogicTupleException e){ 
			outputState.setText("syntax error in event set description.");
			e.printStackTrace();
		} catch (Exception e) {
			outputState.setText("syntax error in event set description.");
			e.printStackTrace();
		}
		 
	}

	private void checkLogEnableActionPerformed(java.awt.event.ActionEvent evt){
		if (checkLogEnable.isSelected()){
			mainForm.agent.loggingQueries = true;
			outputState.setText("Please choose the output log file...");
		}else
			mainForm.agent.loggingQueries = false;
	}

	private void buttonAcceptFilterLogActionPerformed(java.awt.event.ActionEvent evt){
		String st = inputFilterLog.getText(); 
		LogicTuple t = null;
		try{
			t = LogicTuple.parse(st);
		}catch (InvalidLogicTupleException e){
			outputState.setText("Please input an admissible tuple template...");
		}
		if(t == null)
			outputState.setText("Please input an admissible tuple template..."); 
		else{ 
			mainForm.agent.logOpFilter = t;
			buttonGetActionPerformed(null);
		}
	}

	private void buttonBrowseActionPerformed(java.awt.event.ActionEvent evt){
		javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION){
			java.io.File file = chooser.getSelectedFile();
			if (file != null){
				String name = file.getAbsolutePath();
				inputFileLog.setText(name);
				mainForm.agent.changeLogQueryFile(name);
			}
		}
	}

	private void inputFileLogActionPerformed(java.awt.event.ActionEvent evt){
		mainForm.agent.changeLogQueryFile(inputFileLog.getText());
	}

	private void checkFilterLogActionPerformed(java.awt.event.ActionEvent evt){
		try{
			if (checkFilterLog.isSelected()){
				buttonAcceptFilterLog.setEnabled(true);
				outputState.setText("Please input an admissible tuple template...");
			}else{
				buttonAcceptFilterLog.setEnabled(false);
				mainForm.agent.logOpFilter = null;
//				inputFilterLog.setText("");
//				context.setProtocol(mainForm.protocol);
			}
		}catch (Exception e){
			outputState.setText(""+e);
		}
	}

	private void buttonAcceptPatternActionPerformed(java.awt.event.ActionEvent evt){
		try{ 
			String st = inputFilterView.getText(); 
			LogicTuple t = LogicTuple.parse(st);
			if (t == null)
				outputState.setText("Given template is not an admissible Prolog term."); 
			else{ 
				mainForm.protocol.wsetFilter = t;
				context.setProtocol(mainForm.protocol); 
			} 
			buttonGetActionPerformed(null);
		}catch (InvalidLogicTupleException e){
			outputState.setText("Given template is not an admissible Prolog term.");
		} catch (Exception e) {
			outputState.setText(""+e);
		}	 
	}

	private void checkFilterViewActionPerformed(java.awt.event.ActionEvent evt){
		try{
			if (checkFilterView.isSelected()){
				buttonAcceptPattern.setEnabled(true);
				outputState.setText("Please input an admissible tuple template...");
			}else{
				buttonAcceptPattern.setEnabled(false);
				mainForm.protocol.wsetFilter = null;
//				inputFilterView.setText("");
				context.setProtocol(mainForm.protocol);
			}
		}catch (Exception e){
			outputState.setText(""+e);
		}
	}

	private void buttonGetActionPerformed(java.awt.event.ActionEvent evt){
		try{
			context.getSnapshot(GetSnapshotMsg.WSET);
			outputState.setText("Observation done.");
		}catch (Exception e){
			outputState.setText(""+e);
		}
	}

	private void radioReactiveActionPerformed(java.awt.event.ActionEvent evt){
		try{
			mainForm.protocol.pendingQueryObservType = InspectorProtocol.REACTIVE_OBSERVATION;
			context.setProtocol(mainForm.protocol);
			buttonGet.setEnabled(true);
			radioProactive.setSelected(false);
			outputState.setText("REACTIVE observation selected, push button 'Observe!' to update.");
		}catch (Exception e){
			outputState.setText(""+e);
		}
	}

	private void radioProactiveActionPerformed(java.awt.event.ActionEvent evt){
		try{
			mainForm.protocol.pendingQueryObservType = InspectorProtocol.PROACTIVE_OBSERVATION;
			context.setProtocol(mainForm.protocol);
			buttonGet.setEnabled(false);
			radioReactive.setSelected(false);
			outputState.setText("PROACTIVE observation selected.");
		}catch (Exception e){
			outputState.setText(""+e);
		}
	}

	private void exitForm(java.awt.event.WindowEvent evt){
		mainForm.onEventViewerExit();
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanel9;
	private javax.swing.JCheckBox checkLogEnable;
	private javax.swing.JTextField outputNoItems;
	private javax.swing.JTextField inputFileLog;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JPanel jPanel7;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JRadioButton radioProactive;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JButton jButton1;
	private javax.swing.JButton buttonAcceptFilterLog;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextField outputTime;
	private javax.swing.JTextArea outputArea;
	private javax.swing.JTextField inputFilterView;
	private javax.swing.JCheckBox checkFilterView;
	private javax.swing.JButton buttonBrowse;
	private javax.swing.JPanel jPanel11;
	private javax.swing.JPanel jPanel10;
	private javax.swing.JButton buttonAcceptPattern;
	private javax.swing.JRadioButton radioReactive;
	private javax.swing.JTabbedPane jTabbedPane1;
	private javax.swing.JTextField outputState;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JTextField outputVmTime;
	private javax.swing.JCheckBox checkFilterLog;
	private javax.swing.JButton buttonGet;
	private javax.swing.JTextField inputFilterLog;
	
	private int iCurrentPos;
	// End of variables declaration//GEN-END:variables

}
