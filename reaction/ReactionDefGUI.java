package reaction;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

import java.awt.event.*;


//This class realizes an interface which grant an accurately driven method for defining a specification tuple

@SuppressWarnings("serial")
public class ReactionDefGUI extends JFrame implements ActionListener{
	
	private ISpecificationDefiner caller;
	
	private JComboBox event_op,guard_body,goal_op,goal_ob;
	private JTextField event_arg,goal_arg,event_time,guard_time,tcname,tcaddr,comput;
	private JButton add_guard,add_goal,done;
	private JTextArea guard_list,goals_list;
	private JLabel label1,label2,label3,label4,label5,label6,label7,label8;
	private JRadioButton tc,observ,comp,local,remote;
	private JCheckBox ext;
	
	
	public ReactionDefGUI(ISpecificationDefiner caller){
		super("reazione");
		this.caller=caller;
		Box box=new Box(BoxLayout.Y_AXIS);
		box.add(Box.createVerticalStrut(10));
		
		event_op=new JComboBox();
		event_op.addItem("out");
		event_op.addItem("in");
		event_op.addItem("rd");
		event_op.addItem("inp");
		event_op.addItem("rdp");
		event_op.addItem("no");
		event_op.addItem("set");
		event_op.addItem("get");
		event_op.addItem("out_s");
		event_op.addItem("in_s");
		event_op.addItem("rd_s");
		event_op.addItem("inp_s");
		event_op.addItem("rdp_s");
		event_op.addItem("no_s");
		event_op.addItem("set_s");
		event_op.addItem("get_s");
		event_op.addItem("time");
		event_op.setEditable(true);
		event_op.addActionListener(this);
		label1=new JLabel("tupla o template");
		label1.setEnabled(!event_op.getSelectedItem().equals("time"));
		event_arg=new JTextField();
		event_arg.setSize(200,20);
		event_arg.setEnabled(!event_op.getSelectedItem().equals("time"));
		label2=new JLabel("secondi");
		label2.setEnabled(event_op.getSelectedItem().equals("time"));
		event_time=new JTextField();
		event_time.setEnabled(event_op.getSelectedItem().equals("time"));
		guard_body=new JComboBox();
		guard_body.addItem("true");
		guard_body.addItem("request");
		guard_body.addItem("response");
		guard_body.addItem("success");
		guard_body.addItem("failure");
		guard_body.addItem("exdo");
		guard_body.addItem("endo");
		guard_body.addItem("intra");
		guard_body.addItem("inter");
		guard_body.addItem("from_agent");
		guard_body.addItem("to_agent");
		guard_body.addItem("from_tc");
		guard_body.addItem("to_tc");
		guard_body.addItem("before");
		guard_body.addItem("after");
		guard_body.setEditable(true);
		guard_body.addActionListener(this);
		label3=new JLabel("secondi");
		label3.setEnabled((guard_body.getSelectedItem().equals("before"))||(guard_body.getSelectedItem().equals("after")));
		guard_time=new JTextField();
		guard_time.setEnabled((guard_body.getSelectedItem().equals("before"))||(guard_body.getSelectedItem().equals("after")));
		add_guard=new JButton("aggiungi predicato");
		add_guard.addActionListener(this);
		guard_list=new JTextArea();
		guard_list.setRows(3);
		tc=new JRadioButton("su tuple centre");
		tc.addActionListener(this);
		observ=new JRadioButton("osservazionale");
		observ.addActionListener(this);
		comp=new JRadioButton("computazionale");
		comp.addActionListener(this);
		ButtonGroup group=new ButtonGroup();
		group.add(tc);
		group.add(observ);
		group.add(comp);
		tc.setSelected(true);
		ext=new JCheckBox("operazione su tuple centre esterno");
		ext.addActionListener(this);
		ext.setSelected(false);
		ext.setEnabled(tc.isSelected());
		local=new JRadioButton("locale");
		local.addActionListener(this);
		local.setEnabled(ext.isSelected());
		remote=new JRadioButton("remoto");
		remote.addActionListener(this);
		remote.setEnabled(ext.isSelected());
		ButtonGroup group2=new ButtonGroup();
		group2.add(local);
		group2.add(remote);
		local.setSelected(true);
		label7=new JLabel("nome tuple centre");
		label7.setEnabled(ext.isSelected());
		label8=new JLabel("indirizzo tuple centre");
		label8.setEnabled(ext.isSelected());
		tcname=new JTextField();
		tcname.setEnabled(ext.isSelected());
		tcaddr=new JTextField();
		tcaddr.setEnabled(remote.isSelected());
		label4=new JLabel("operazione su tuple centre");
		label4.setEnabled(tc.isSelected());
		goal_op=new JComboBox();
		goal_op.addItem("out");
		goal_op.addItem("in");
		goal_op.addItem("rd");
		goal_op.addItem("inp");
		goal_op.addItem("rdp");
		goal_op.addItem("no");
		goal_op.addItem("get");
		goal_op.addItem("set");
		goal_op.addItem("out_s");
		goal_op.addItem("in_s");
		goal_op.addItem("rd_s");
		goal_op.addItem("inp_s");
		goal_op.addItem("rdp_s");
		goal_op.addItem("no_s");
		goal_op.addItem("get_s");
		goal_op.addItem("set_s");
		goal_op.setEditable(true);
		goal_op.setEnabled(tc.isSelected());
		label5=new JLabel("template");
		label5.setEnabled(tc.isSelected());
		goal_arg=new JTextField();
		goal_arg.setEditable((tc.isSelected())||(observ.isSelected()));
		goal_ob=new JComboBox();
		goal_ob.addItem("current_predicate");
		goal_ob.addItem("current_tuple");
		goal_ob.addItem("current_source");
		goal_ob.addItem("current_target");
		goal_ob.addItem("current_time");
		goal_ob.addItem("event_predicate");
		goal_ob.addItem("event_tuple");
		goal_ob.addItem("event_source");
		goal_ob.addItem("event_target");
		goal_ob.addItem("event_time");
		goal_ob.addItem("start_predicate");
		goal_ob.addItem("start_tuple");
		goal_ob.addItem("start_source");
		goal_ob.addItem("start_target");
		goal_ob.addItem("start_time");
		goal_ob.setEditable(true);
		goal_ob.setEnabled(observ.isSelected());
		label6=new JLabel("computazione");
		label6.setEnabled(comp.isSelected());
		comput=new JTextField();
		comput.setEnabled(comp.isSelected());
		add_goal=new JButton("aggiungi obbiettivo");
		add_goal.addActionListener(this);
		add_goal.setActionCommand("aggiungi");
		goals_list=new JTextArea();
		goals_list.setRows(8);
		done=new JButton("fatto");
		done.addActionListener(this);
		done.setActionCommand("fatto");
		
		//this panel contains the instruments for defining the event of the specification tuple
		JPanel panel1=new JPanel(new GridBagLayout());
		panel1.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "event", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints=new java.awt.GridBagConstraints();
        constraints.gridheight = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        constraints.weightx = 100.0;
		Box box1=new Box(BoxLayout.Y_AXIS);
		Box box1_1=new Box(BoxLayout.X_AXIS);
		box1_1.add(new JLabel("operazione evento"));
		box1_1.add(Box.createHorizontalStrut(20));
		box1_1.add(event_op);
		box1.add(box1_1);
		Box box1_2=new Box(BoxLayout.X_AXIS);
		box1_2.add(label1);
		box1_2.add(Box.createHorizontalStrut(20));
		box1_2.add(event_arg);
		box1.add(box1_2);
		Box box1_3=new Box(BoxLayout.X_AXIS);
		box1_3.add(label2);
		box1_3.add(Box.createHorizontalStrut(50));
		box1_3.add(event_time);
		box1.add(box1_3);
		panel1.add(box1,constraints);
		box.add(panel1);
		
		//this panel contains the instrument for defining the guard of the specification tuple
		JPanel panel2=new JPanel(new GridBagLayout());
		panel2.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "guard", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints2=new java.awt.GridBagConstraints();
		constraints2.gridheight = 2;
        constraints2.gridx = 0;
        constraints2.gridy = 0;
        constraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        constraints2.weightx = 100.0;
        Box box3=new Box(BoxLayout.Y_AXIS);
        box3.add(guard_body);
        Box box3_1=new Box(BoxLayout.X_AXIS);
        box3_1.add(label3);
        box3_1.add(Box.createHorizontalStrut(50));
        box3_1.add(guard_time);
        box3.add(box3_1);
        box3.add(Box.createVerticalStrut(5));
        box3.add(add_guard);
        box3.add(Box.createVerticalStrut(5));
        box3.add(new JLabel("lista predicati di guardia"));
        JScrollPane scroll1=new JScrollPane();
        scroll1.setViewportView(guard_list);
        box3.add(scroll1);
		panel2.add(box3,constraints2);
		box.add(panel2);
		
		//this panel contains the instruments for defining the reaction body (the set of reaction goals) of the specification tuple
		JPanel panel3=new JPanel(new GridBagLayout());
		panel3.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "goals", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints3=new java.awt.GridBagConstraints();
		constraints3.gridheight = 2;
        constraints3.gridx = 0;
        constraints3.gridy = 0;
        constraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        constraints3.weightx = 100.0;
        Box box2=new Box(BoxLayout.Y_AXIS);
        Box box2_3=new Box(BoxLayout.X_AXIS);
        box2_3.add(tc);
        box2_3.add(observ);
        box2_3.add(comp);
        box2.add(box2_3);
        box2.add(ext);
        Box box2_5=new Box(BoxLayout.X_AXIS);
        box2_5.add(local);
        box2_5.add(remote);
        box2.add(box2_5);
        Box box2_6=new Box(BoxLayout.X_AXIS);
        box2_6.add(label7);
        box2_6.add(Box.createHorizontalStrut(15));
        box2_6.add(tcname);
        box2.add(box2_6);
        Box box2_7=new Box(BoxLayout.X_AXIS);
        box2_7.add(label8);
        box2_6.add(Box.createHorizontalStrut(5));
        box2_7.add(tcaddr);
        box2.add(box2_7);
        Box box2_1=new Box(BoxLayout.X_AXIS);
        box2_1.add(label4);
        box2_1.add(Box.createHorizontalStrut(10));
        box2_1.add(goal_op);
        box2.add(box2_1);
        box2.add(goal_ob);
        Box box2_2=new Box(BoxLayout.X_AXIS);
        box2_2.add(label5);
        box2_2.add(Box.createHorizontalStrut(10));
        box2_2.add(goal_arg);
        box2.add(box2_2);
        Box box2_4=new Box(BoxLayout.X_AXIS);
        box2_4.add(label6);
        box2_4.add(Box.createHorizontalStrut(10));
        box2_4.add(comput);
        box2.add(box2_4);
        box2.add(Box.createVerticalStrut(10));
        box2.add(add_goal);
        box2.add(Box.createVerticalStrut(5));
        box2.add(new JLabel("lista obbiettivi reazione"));
        JScrollPane scroll=new JScrollPane();
        scroll.setViewportView(goals_list);
        box2.add(scroll);
        panel3.add(box2,constraints3);
        box.add(panel3);
		
		box.add(done);
		box.add(Box.createVerticalStrut(10));
        getContentPane().add(box);
		pack();
		setVisible(true);
	}
	
	
	//Utility function for obtain the event of the specification tuple, specified in "event" panel
	private String composeEvent() {
		if (event_op.getSelectedItem().equals("time")){
			try {
				double time=Double.parseDouble(event_time.getText());
				return "time("+time+")";
			} catch(NumberFormatException e) {
				e.printStackTrace();
				return "";
			}
		}
		else return event_op.getSelectedItem()+"("+event_arg.getText()+")";
	}
	
	//Utility function for obtaining the guard body of the specification tuple, specified in "guard" panel
	private String composeGuard(){
		if (guard_list.getText().equals("")) {
			if ((!guard_body.getSelectedItem().equals("before"))&&(!guard_body.getSelectedItem().equals("after"))) return (String)guard_body.getSelectedItem();
			else return (String)guard_body.getSelectedItem()+"("+guard_time.getText()+")";
		} else return "("+guard_list.getText()+")";
	}
	
	//Utility function for obtaining the whole specification tuple
	private String composeReaction(){
		String event=composeEvent();
		if (event.equals("")) return "sintassi errata";
		String guard=composeGuard();
		return "reaction("+event+","+guard+",("+goals_list.getText()+"))";
	}
		
	
	public void actionPerformed(ActionEvent e){
		//add the specified guard predicate into the area of guard body
		if (e.getSource()==add_guard){
			String guard;
			if ((!guard_body.getSelectedItem().equals("before"))&&(!guard_body.getSelectedItem().equals("after"))) guard=(String)guard_body.getSelectedItem();
			else guard=(String)guard_body.getSelectedItem()+"("+guard_time.getText()+")";
			if (guard_list.getText().equals("")) guard_list.append(guard);
			else guard_list.append(",\n"+guard);
		}
		//add the specified goal to the area of the reaction body
		if (e.getSource()==add_goal){
			String goal;
			if (tc.isSelected()){
				String act=(String)goal_op.getSelectedItem()+"("+goal_arg.getText()+")";
				if (ext.isSelected()){
					String tuplecentre;
					if (local.isSelected()) tuplecentre=tcname.getText();
					else tuplecentre=tcname.getText()+"@"+tcaddr.getText();
					goal=tuplecentre+"?"+act;
				} else goal=act;
			} else if (observ.isSelected()) goal=(String)goal_ob.getSelectedItem();
			else goal=comp.getText();
			if (goals_list.getText().equals("")) goals_list.append(goal);
			else goals_list.append(",\n"+goal);
		}
		//compose the specification tuple, return it to the caller interface and close the current window
		if (e.getSource()==done){
			caller.ObtainSpecification(composeReaction());
			System.out.print("fatto");
			dispose();
		}
		
		//the following commands specify what graphic instrument have to be displayed, in consideration of the selection buttons selected
		
		if (e.getSource()==event_op){
			event_arg.setEnabled(!event_op.getSelectedItem().equals("time"));
			event_time.setEnabled(event_op.getSelectedItem().equals("time"));
			label1.setEnabled(!event_op.getSelectedItem().equals("time"));
			label2.setEnabled(event_op.getSelectedItem().equals("time"));
		}
		if (e.getSource()==guard_body){
			guard_time.setEnabled((guard_body.getSelectedItem().equals("before"))||(guard_body.getSelectedItem().equals("after")));
			label3.setEnabled((guard_body.getSelectedItem().equals("before"))||(guard_body.getSelectedItem().equals("after")));
		}
		if ((e.getSource()==tc)||(e.getSource()==observ)||(e.getSource()==comp)){
			ext.setEnabled(tc.isSelected());
			label4.setEnabled(tc.isSelected());
			goal_op.setEnabled(tc.isSelected());
			label5.setEnabled((tc.isSelected())||(observ.isSelected()));
			goal_arg.setEnabled((tc.isSelected())||(observ.isSelected()));
			goal_ob.setEnabled(observ.isSelected());
			label6.setEnabled(comp.isSelected());
			comput.setEnabled(comp.isSelected());
		}
		if (e.getSource()==ext){
			local.setEnabled(ext.isSelected());
			remote.setEnabled(ext.isSelected());
			label7.setEnabled(ext.isSelected());
			label8.setEnabled(ext.isSelected());
			tcname.setEnabled(ext.isSelected());
		}
		if ((e.getSource()==local)||(e.getSource()==remote)){
			tcaddr.setEnabled(remote.isSelected());
		}
	}
	
	
	public static void main(String[] args){
		new ReactionDefGUI(new shell.AdvancedShellGUI());
	}

}
