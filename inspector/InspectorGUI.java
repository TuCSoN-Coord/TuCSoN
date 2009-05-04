package inspector;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import reaction.ISpecificationDefiner;
import reaction.ReactionDefGUI;
import shell.RespectShellSubInterface;
import shell.SimpleShellGUI;


import alice.logictuple.InvalidLogicTupleException;
import alice.logictuple.InvalidTupleOperationException;
import alice.respect.api.InvalidAgentIdException;
import alice.respect.api.InvalidSpecificationException;
import alice.respect.api.InvalidTupleCentreIdException;
import alice.respect.api.OperationNotPossibleException;


//Class which implements the inspector interface
//
//NOTE: this class doesn't use directly the ReSpecT API, but interacts with tuple centers through an IRespectInspectorSubInterface interface

@SuppressWarnings("serial")
public class InspectorGUI extends JFrame implements ActionListener,ISpecificationDefiner{

	private IRespectInspectorSubInterface sub_int;
	private JFileChooser loader;
	
	private JRadioButton local,remote;
	private JTextField tcname,tcaddr,tclog,tcstatus,tcerr;
	private JButton connect,stop,next,resume,shell;
	private JTabbedPane tabbedpane;
	
	private JList tuples;
	private JRadioButton tuple_manual_agg,tuple_automatic_agg;
	private JButton tuple_time_start,tuple_agg,tuple_load,tuple_save,tuple_add,tuple_del,tuple_edit;
	private JTextField tuple_time_agg,tuple_filtre,tuple_body;
	private Box tuples_box;
	private Updater tupleUpdater;
	
	private JList specification;
	private JRadioButton spec_manual_agg,spec_automatic_agg;
	private JButton spec_time_start,spec_agg,reaction_create,reaction_add,reaction_del,reaction_edit,spec_load,spec_save,specification_load,specification_save;
	private JTextField spec_filtre,spec_time_agg,reaction_body;
	private Box spec_box;
	private Updater specUpdater;
	
	private JList queries;
	private JRadioButton query_manual_agg,query_automatic_agg;
	private JButton query_time_start,query_agg,query_terminate;
	private JTextField query_time_agg,query_filtre;
	private Box query_box;
	private Updater queryUpdater;
	
	private JTextArea reactions;
	private JButton reaction_set_filtre,reaction_clear;
	private JTextField reaction_filtre,reaction_new_filtre;
	private Box reaction_box;
	private Updater reactionUpdater;
	private HashMap<String, Long> recent_time_reactions;
	
	
	public InspectorGUI(IRespectInspectorSubInterface sub_int){
		super ("inspector");
		this.sub_int=sub_int;
		Box box=new Box(BoxLayout.Y_AXIS);
		
		//this box contains the instruments for the connection with a determined tuple center, as well the ones for interact with its status
		Box box1=new Box(BoxLayout.X_AXIS);
		JLabel icon=new JLabel();
		icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alice/respect/images/logo.png")));
		box1.add(icon);
		Box box1_1=new Box(BoxLayout.Y_AXIS);
		Box box1_2=new Box(BoxLayout.X_AXIS);
		local=new JRadioButton("tuple centre locale");
		local.addActionListener(this);
		box1_2.add(local);
		box1_2.add(Box.createHorizontalStrut(10));
		remote=new JRadioButton("tuple centre remoto");
		remote.addActionListener(this);
		box1_2.add(remote);
		ButtonGroup group=new ButtonGroup();
		group.add(local);
		group.add(remote);
		local.setSelected(true);
		box1_1.add(box1_2);
		box1_1.add(Box.createVerticalStrut(10));
		Box box1_3=new Box(BoxLayout.X_AXIS);
		box1_3.add(new JLabel("nome del tuple centre"));
		box1_3.add(Box.createHorizontalStrut(10));
		tcname=new JTextField();
		tcname.setMaximumSize(new Dimension(1000,30));
		box1_3.add(tcname);
		box1_1.add(box1_3);
		Box box1_4=new Box(BoxLayout.X_AXIS);
		box1_4.add(new JLabel("indirizzo del tuple centre"));
		box1_4.add(Box.createHorizontalStrut(10));
		tcaddr=new JTextField();
		tcaddr.setEditable(remote.isSelected());
		tcaddr.setMaximumSize(new Dimension(1000,30));
		box1_4.add(tcaddr);
		box1_1.add(box1_4);
		connect=new JButton("connetti");
		connect.addActionListener(this);
		box1_1.add(connect);
		tclog=new JTextField();
		tclog.setText("connesso a:  default");
		tclog.setEditable(false);
		box1_1.add(tclog);
		Box box1_5=new Box(BoxLayout.X_AXIS);
		stop=new JButton("ferma il tuple centre");
		stop.addActionListener(this);
		box1_5.add(stop);
		next=new JButton("prossima operazione");
		next.addActionListener(this);
		box1_5.add(next);
		resume=new JButton("riavvia il tuple centre");
		resume.addActionListener(this);
		box1_5.add(resume);
		box1_1.add(box1_5);
		Box box1_6=new Box(BoxLayout.X_AXIS);
		tcstatus=new JTextField("tuple centre status:  working");
		tcstatus.setEditable(false);
		box1_6.add(tcstatus);
		tcerr=new JTextField();
		tcerr.setEditable(false);
		box1_6.add(tcerr);
		box1_1.add(box1_6);
		shell=new JButton("apri shell comandi");
		shell.addActionListener(this);
		box1_1.add(shell);
		box1.add(box1_1);
		box1.add(Box.createHorizontalStrut(10));
		box.add(box1);
		
		tabbedpane=new JTabbedPane();
		createTupleTab();
		createSpecTab();
		createQueryTab();
		createReactionTab();
		
		box.add(tabbedpane);
		getContentPane().add(box);
		pack();
		setVisible(true);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tupleUpdater=new Updater("tuple");
		specUpdater=new Updater("spec");
		queryUpdater=new Updater("query");
		reactionUpdater=new Updater("reaction");
		reactionUpdater.start();
		
		loader=new JFileChooser();
		recent_time_reactions=new HashMap<String,Long>();
	}
	
	//Utility procedure for the creation of the tab "tuples", in the tabbed pane, which contains the instruments for interacting with the set of tuples of the tuple center
	private void createTupleTab(){
		Box box=new Box(BoxLayout.Y_AXIS);
		
		JPanel panel=new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "set di tuple", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints=new java.awt.GridBagConstraints();
        constraints.gridheight = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        constraints.weightx = 100.0;
		tuples_box=new Box(BoxLayout.Y_AXIS);
		tuples=new JList();
		tuples.setVisibleRowCount(10);
		JScrollPane scroll=new JScrollPane(tuples);
		tuples_box.add(scroll);
		panel.add(tuples_box,constraints);
		box.add(panel);
		
		JPanel panel1=new JPanel();
		Box box1=new Box(BoxLayout.Y_AXIS);
		panel1.setLayout(new GridBagLayout());
		panel1.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "filtro su tuple", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints1=new java.awt.GridBagConstraints();
        constraints1.gridheight = 2;
        constraints1.gridx = 0;
        constraints1.gridy = 0;
        constraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        constraints1.weightx = 100.0;
		Box box2=new Box(BoxLayout.X_AXIS);
		box2.add(new JLabel("template"));
		box2.add(Box.createHorizontalStrut(10));
		tuple_filtre=new JTextField();
		box2.add(tuple_filtre);
		box1.add(box2);
		box1.add(Box.createVerticalStrut(10));
		
		Box box3=new Box(BoxLayout.X_AXIS);
		Box box3_1=new Box(BoxLayout.Y_AXIS);
		tuple_manual_agg=new JRadioButton("aggiorna manualmente");
		tuple_manual_agg.addActionListener(this);
		box3_1.add(tuple_manual_agg);
		tuple_automatic_agg=new JRadioButton("aggiorna ogni");
		tuple_automatic_agg.addActionListener(this);
		box3_1.add(tuple_automatic_agg);
		box3.add(box3_1);
		ButtonGroup group=new ButtonGroup();
		group.add(tuple_manual_agg);
		group.add(tuple_automatic_agg);
		tuple_manual_agg.setSelected(true);
		Box box3_2=new Box(BoxLayout.Y_AXIS);
		tuple_agg=new JButton("aggiorna");
		tuple_agg.addActionListener(this);
		box3_2.add(tuple_agg);
		Box box3_2_1=new Box(BoxLayout.X_AXIS);
		tuple_time_agg=new JTextField();
		box3_2_1.add(tuple_time_agg);
		tuple_time_agg.setEditable(tuple_automatic_agg.isSelected());
		box3_2_1.add(Box.createHorizontalStrut(5));
		box3_2_1.add(new JLabel("secondi"));
		box3_2_1.add(Box.createHorizontalStrut(40));
		tuple_time_start=new JButton("via");
		tuple_time_start.addActionListener(this);
		box3_2_1.add(tuple_time_start);
		tuple_time_start.setEnabled(tuple_automatic_agg.isSelected());
		box3_2.add(box3_2_1);
		box3.add(box3_2);
		box1.add(box3);
		panel1.add(box1,constraints1);
		box.add(panel1);
		
		JPanel panel2=new JPanel();
		panel2.setLayout(new GridBagLayout());
		panel2.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "operazioni", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints2=new java.awt.GridBagConstraints();
        constraints2.gridheight = 2;
        constraints2.gridx = 0;
        constraints2.gridy = 0;
        constraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        constraints2.weightx = 100.0;
		Box box4=new Box(BoxLayout.Y_AXIS);
		Box box4_3=new Box(BoxLayout.X_AXIS);
		tuple_load=new JButton("carica da file");
		tuple_load.addActionListener(this);
		box4_3.add(tuple_load);
		box4_3.add(Box.createHorizontalStrut(5));
		tuple_save=new JButton("salva su file");
		tuple_save.addActionListener(this);
		box4_3.add(tuple_save);
		box4.add(box4_3);
		box4.add(Box.createVerticalStrut(5));
		Box box4_1=new Box(BoxLayout.X_AXIS);
		tuple_add=new JButton("aggiungi");
		tuple_add.addActionListener(this);
		box4_1.add(tuple_add);
		box4_1.add(Box.createHorizontalStrut(5));
		tuple_del=new JButton("cancella");
		tuple_del.addActionListener(this);
		box4_1.add(tuple_del);
		box4_1.add(Box.createHorizontalStrut(5));
		tuple_edit=new JButton("edita");
		tuple_edit.addActionListener(this);
		box4_1.add(tuple_edit);
		box4.add(box4_1);
		Box box4_2=new Box(BoxLayout.X_AXIS);
		box4_2.add(new JLabel("tupla"));
		box4_2.add(Box.createHorizontalStrut(10));
		tuple_body=new JTextField();
		box4_2.add(tuple_body);
		box4.add(box4_2);
		panel2.add(box4,constraints2);
		box.add(panel2);
		
		tabbedpane.add("tuple",box);
	}
	
	//Utility procedure for the creation of the tab "specificaion", in the tabbed pane, which contains the instruments for interacting with the set of specification tuples of the tuple center
	private void createSpecTab(){
		Box box=new Box(BoxLayout.Y_AXIS);
		JPanel panel=new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "set di tuple di specifica", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints=new java.awt.GridBagConstraints();
        constraints.gridheight = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        constraints.weightx = 100.0;
        spec_box=new Box(BoxLayout.Y_AXIS);
		specification=new JList();
		specification.setVisibleRowCount(10);
		JScrollPane scroll=new JScrollPane(specification);
		spec_box.add(scroll);
		panel.add(spec_box,constraints);
		box.add(panel);
		
		JPanel panel1=new JPanel();
		panel1.setLayout(new GridBagLayout());
		panel1.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "filtro su tuple di specifica", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints1=new java.awt.GridBagConstraints();
        constraints1.gridheight = 2;
        constraints1.gridx = 0;
        constraints1.gridy = 0;
        constraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        constraints1.weightx = 100.0;
		Box box1=new Box(BoxLayout.Y_AXIS);
		Box box1_0=new Box(BoxLayout.X_AXIS);
		box1_0.add(new JLabel("template"));
		box1_0.add(Box.createHorizontalStrut(10));
		spec_filtre=new JTextField();
		box1_0.add(spec_filtre);
		box1.add(box1_0);
		box1.add(Box.createVerticalStrut(10));
		Box box1_1=new Box(BoxLayout.X_AXIS);
		Box box1_1_1=new Box(BoxLayout.Y_AXIS);
		spec_manual_agg=new JRadioButton("aggiorna manualmente");
		spec_manual_agg.addActionListener(this);
		box1_1_1.add(spec_manual_agg);
		spec_automatic_agg=new JRadioButton("aggiorna ogni");
		spec_automatic_agg.addActionListener(this);
		box1_1_1.add(spec_automatic_agg);
		ButtonGroup group=new ButtonGroup();
		group.add(spec_manual_agg);
		group.add(spec_automatic_agg);
		spec_manual_agg.setSelected(true);
		box1_1.add(box1_1_1);
		Box box1_1_2=new Box(BoxLayout.Y_AXIS);
		spec_agg=new JButton("aggiorna");
		spec_agg.addActionListener(this);
		box1_1_2.add(spec_agg);
		Box box1_1_2_1=new Box(BoxLayout.X_AXIS);
		spec_time_agg=new JTextField();
		box1_1_2_1.add(spec_time_agg);
		spec_time_agg.setEditable(spec_automatic_agg.isSelected());
		box1_1_2_1.add(new JLabel("secondi"));
		box1_1_2_1.add(Box.createHorizontalStrut(40));
		spec_time_start=new JButton("via");
		spec_time_start.addActionListener(this);
		box1_1_2_1.add(spec_time_start);
		spec_time_start.setEnabled(spec_automatic_agg.isSelected());
		box1_1_2.add(box1_1_2_1);
		box1_1.add(box1_1_2);
		box1.add(box1_1);
		panel1.add(box1,constraints1);
		box.add(panel1);
		
		JPanel panel2=new JPanel();
		panel2.setLayout(new GridBagLayout());
		panel2.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "operazioni", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints2=new java.awt.GridBagConstraints();
        constraints2.gridheight = 2;
        constraints2.gridx = 0;
        constraints2.gridy = 0;
        constraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        constraints2.weightx = 100.0;
        Box box2=new Box(BoxLayout.Y_AXIS);
        Box box2_3=new Box(BoxLayout.X_AXIS);
        spec_load=new JButton("carica reazioni da file");
        spec_load.addActionListener(this);
        box2_3.add(spec_load);
        spec_save=new JButton("salva reazioni su file");
        spec_save.addActionListener(this);
        box2_3.add(spec_save);
        box2_3.add(Box.createHorizontalStrut(10));
        specification_load=new JButton("setta specifica da file");
        specification_load.addActionListener(this);
        box2_3.add(specification_load);
        specification_save=new JButton("salva specifica su file");
        specification_save.addActionListener(this);
        box2_3.add(specification_save);
        box2.add(box2_3);
        box2.add(Box.createVerticalStrut(10));
        Box box2_1=new Box(BoxLayout.X_AXIS);
        reaction_create=new JButton("creazione guidata");
        reaction_create.addActionListener(this);
        box2_1.add(reaction_create);
        reaction_add=new JButton("aggiungi");
        reaction_add.addActionListener(this);
        box2_1.add(reaction_add);
        box2_1.add(Box.createHorizontalStrut(10));
        reaction_del=new JButton("cancella");
        reaction_del.addActionListener(this);
        box2_1.add(reaction_del);
        reaction_edit=new JButton("edita");
        reaction_edit.addActionListener(this);
        box2_1.add(reaction_edit);
        box2.add(box2_1);
        Box box2_2=new Box(BoxLayout.X_AXIS);
        box2_2.add(new JLabel("reazione"));
        box2_2.add(Box.createHorizontalStrut(10));
        reaction_body=new JTextField();
        box2_2.add(reaction_body);
        box2.add(box2_2);
        panel2.add(box2,constraints2);
        box.add(panel2);
		
		tabbedpane.add(box,"specification");
	}
	
	//Utility procedure for the creation of the tab "query", in the tabbed pane, which contains the instruments for interacting with the set of pending queries of the tuple center
	private void createQueryTab(){
		Box box=new Box(BoxLayout.Y_AXIS);
		JPanel panel=new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "set di query in corso", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints=new java.awt.GridBagConstraints();
        constraints.gridheight = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        constraints.weightx = 100.0;
		query_box=new Box(BoxLayout.Y_AXIS);
        queries=new JList();
        queries.setVisibleRowCount(10);
        JScrollPane scroll=new JScrollPane(queries);
        query_box.add(scroll);
        panel.add(query_box,constraints);
        box.add(panel);
		
        JPanel panel1=new JPanel();
		panel1.setLayout(new GridBagLayout());
		panel1.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "filtro sulle query in corso", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints1=new java.awt.GridBagConstraints();
        constraints1.gridheight = 2;
        constraints1.gridx = 0;
        constraints1.gridy = 0;
        constraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        constraints1.weightx = 100.0;
		Box box1=new Box(BoxLayout.Y_AXIS);
		Box box1_0=new Box(BoxLayout.X_AXIS);
		box1_0.add(new JLabel("template"));
		box1_0.add(Box.createHorizontalStrut(10));
		query_filtre=new JTextField();
		box1_0.add(query_filtre);
		box1.add(box1_0);
		box1.add(Box.createVerticalStrut(10));
        Box box1_1=new Box(BoxLayout.X_AXIS);
		Box box1_1_1=new Box(BoxLayout.Y_AXIS);
		query_manual_agg=new JRadioButton("aggiorna manualmente");
		query_manual_agg.addActionListener(this);
		box1_1_1.add(query_manual_agg);
		query_automatic_agg=new JRadioButton("aggiorna ogni");
		query_automatic_agg.addActionListener(this);
		box1_1_1.add(query_automatic_agg);
		ButtonGroup group=new ButtonGroup();
		group.add(query_manual_agg);
		group.add(query_automatic_agg);
		query_manual_agg.setSelected(true);
		box1_1.add(box1_1_1);
		Box box1_1_2=new Box(BoxLayout.Y_AXIS);
		query_agg=new JButton("aggiorna");
		query_agg.addActionListener(this);
		box1_1_2.add(query_agg);
		Box box1_1_2_1=new Box(BoxLayout.X_AXIS);
		query_time_agg=new JTextField();
		query_time_agg.addActionListener(this);
		box1_1_2_1.add(query_time_agg);
		query_time_agg.setEditable(query_automatic_agg.isSelected());
		box1_1_2_1.add(new JLabel("secondi"));
		box1_1_2_1.add(Box.createHorizontalStrut(40));
		query_time_start=new JButton("via");
		query_time_start.addActionListener(this);
		box1_1_2_1.add(query_time_start);
		query_time_start.setEnabled(query_automatic_agg.isSelected());
		box1_1_2.add(box1_1_2_1);
		box1_1.add(box1_1_2);
		box1.add(box1_1);
		panel1.add(box1,constraints1);
        box.add(panel1);
		
        JPanel panel2=new JPanel();
        panel2.setLayout(new GridBagLayout());
		panel2.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "operazioni", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints2=new java.awt.GridBagConstraints();
        constraints2.gridheight = 2;
        constraints2.gridx = 0;
        constraints2.gridy = 0;
        constraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        constraints2.weightx = 100.0;
        Box box2=new Box(BoxLayout.X_AXIS);
        query_terminate=new JButton("elimina");
        query_terminate.addActionListener(this);
        box2.add(query_terminate);
        panel2.add(box2,constraints2);
        box.add(panel2);
        
        tabbedpane.add(box,"query");
	}
	
	//Utility procedure for the creation of the tab "reaction", in the tabbed pane, which contains the instruments for the displaying of the reactions happened in the tuple center
	private void createReactionTab(){
		Box box=new Box(BoxLayout.Y_AXIS);
		JPanel panel=new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "set di reazioni in corso", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints=new java.awt.GridBagConstraints();
        constraints.gridheight = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        constraints.weightx = 100.0;
		reaction_box=new Box(BoxLayout.Y_AXIS);
		
		reactions=new JTextArea();
		reactions.setRows(15);
		reaction_box.add(reactions);
		JScrollPane scroll=new JScrollPane(reactions);
        panel.add(scroll,constraints);
        box.add(panel);
		
		JPanel panel1=new JPanel();
		panel1.setLayout(new GridBagLayout());
		panel1.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "filtro sulle reazioni in corso", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints1=new java.awt.GridBagConstraints();
        constraints1.gridheight = 2;
        constraints1.gridx = 0;
        constraints1.gridy = 0;
        constraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        constraints1.weightx = 100.0;
        Box box1=new Box(BoxLayout.Y_AXIS);
        Box box1_1=new Box(BoxLayout.X_AXIS);
        box1_1.add(new JLabel("template"));
        box1_1.add(Box.createHorizontalStrut(10));
        reaction_filtre=new JTextField();
        reaction_filtre.setEditable(false);
        box1_1.add(reaction_filtre);
        box1.add(box1_1);
        box1.add(Box.createVerticalStrut(5));
        Box box1_2=new Box(BoxLayout.X_AXIS);
        reaction_new_filtre=new JTextField();
        box1_2.add(reaction_new_filtre);
        reaction_set_filtre=new JButton("setta filtro");
        reaction_set_filtre.addActionListener(this);
        box1_2.add(reaction_set_filtre);
        box1.add(box1_2);
        box1.add(Box.createVerticalStrut(5));
        reaction_clear=new JButton("pulisci lista reazioni");
        reaction_clear.addActionListener(this);
        box1.add(reaction_clear);
        panel1.add(box1,constraints1);
        box.add(panel1);
        tabbedpane.add(box,"reazioni");
	}
	
	
	//Utility procedure for updating the set of tuples displayed in the area with the one actually in the tuple center
	public void updateTuples(){
		String template=tuple_filtre.getText();
		String[] tuples_list;
		try {
			if (template.equals("")) tuples_list = sub_int.getTuples("X");
			else tuples_list = sub_int.getTuples(template);
		} catch (InvalidLogicTupleException e) {
			e.printStackTrace();
			tuple_filtre.setText("tupla non valida");
			return;
		}
		tuples_box.removeAll();
		tuples=new JList(tuples_list);
		tuples.setVisibleRowCount(10);
		JScrollPane scroll=new JScrollPane(tuples);
		tuples_box.add(scroll);
		pack();
	}
	
	//Utility procedure for updating the set of specification tuples displayed in the area with the one actually in the tuple center
	public void updateSpec(){
		String template=spec_filtre.getText();
		String[] reactions_list;
		if (template.equals(""))
			try {
				reactions_list=sub_int.getSpecs("X");
			} catch (InvalidLogicTupleException e1) {
				e1.printStackTrace();
				reaction_filtre.setText("tupla di specifica non valida");
				return;
			}
		else
			try {
				reactions_list=sub_int.getSpecs(template);
			} catch (InvalidLogicTupleException e) {
				e.printStackTrace();
				spec_filtre.setText("tupla di specifica non valida");
				return;
			}
			spec_box.removeAll();
			specification=new JList(reactions_list);
			specification.setVisibleRowCount(10);
			JScrollPane scroll=new JScrollPane(specification);
			spec_box.add(scroll);
			pack();
	}
	
	//Utility procedure for updating the set of queries displayed in the area with the ones actually ongoing in the tuple center
	public void updateQuery(){
		String template=query_filtre.getText();
		String[] query_list;
		try {
			if (template.equals("")) query_list = sub_int.getQueries("X");
			else query_list = sub_int.getQueries(template);
		} catch (InvalidLogicTupleException e) {
			e.printStackTrace();
			query_filtre.setText("query non valida");
			return;
		} catch (InvalidTupleOperationException e) {
			e.printStackTrace();
			query_filtre.setText("operazione non eseguibile");
			return;
		}
		query_box.removeAll();
		queries=new JList(query_list);
		queries.setVisibleRowCount(10);
		JScrollPane scroll=new JScrollPane(queries);
		query_box.add(scroll);
		pack();
	}
	
	//Utility procedure for adding to the set of reaction displayed in the area with the new ones happened in the tuple center
	//This method uses an HashMap for keeping trace of recent reactions and the time they are triggered, so reaction already memorized aren't displayed
	//Reaction are removed from the HashMap, after a certain time.
	public void updateReactions(){
		String template=reaction_filtre.getText();
		String[] reaction_list;
		try {
			if (template.equals("")) reaction_list = sub_int.getReactions("X");
			else reaction_list = sub_int.getReactions(template);
		} catch (InvalidLogicTupleException e) {
			e.printStackTrace();
			reaction_filtre.setText("");
			reaction_new_filtre.setText("template reazione non valido");
			return;
		}
		long curr_time=System.nanoTime();
		for (int i=0;i<reaction_list.length;i++){
			String reaction=reaction_list[i];
			Long time=(Long)recent_time_reactions.get(reaction);
			if ((time==null)||(curr_time-time.longValue()>500000000)){
				reactions.append(reaction+"   at time: "+curr_time+"\n");
				recent_time_reactions.put(reaction,new Long(curr_time));
			}
		}
	}
	
	
	//Method declared in ISpecificationDefiner
	//it impose as new specification tuple the one passed as parameter
	public void ObtainSpecification(String reaction) {
		reaction_body.setText(reaction);
	}
	
	
	//Utility procedure for reading the content of a specified file
	private String getByFile(File file) throws IOException{
		String res="";
		BufferedReader br;
		br=new BufferedReader(new FileReader(file));
		String line=br.readLine();
		while(line!=null){
			res+=line+" \n";
			line = br.readLine();
		}
		br.close();
		return res;
	}
	
	//Utility procedure for writing a string on a specified file
	private void writeOnFile(File file,String info) throws IOException{
		BufferedWriter bw;
		bw=new BufferedWriter(new FileWriter(file,true));
		bw.write(info);
		bw.close();
	}
	
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e){
		//connect to the specified tuple center, visualizing the sets of relative informations
		if (e.getSource()==connect){
			String tuplecentre;
			if (local.isSelected()){
				if (tcname.getText().equals("")) tuplecentre="default";
				else tuplecentre=tcname.getText();
			}
			else tuplecentre=tcname.getText()+"@"+tcaddr.getText();
			tclog.setText("connesso a:  "+tuplecentre);
			try {
				sub_int.focusOnTC(tuplecentre);
			} catch (InvalidTupleCentreIdException e1) {
				e1.printStackTrace();
				tcname.setText("errore di connessione");
				tcaddr.setText("");
			}
			updateTuples();
			updateSpec();
			updateQuery();
			updateReactions();
		}
		//stop the execution of the current tuple center
		if (e.getSource()==stop){
			String status=tcstatus.getText();
			try {
				tcstatus.setText("tuple centre status:  stopped");
				sub_int.stopTC();
			} catch (OperationNotPossibleException e1) {
				e1.printStackTrace();
				tcstatus.setText(status);
				tcerr.setText("operazione fallita");
			}
		}
		//execute the next operation scheduled inside the tule center
		if (e.getSource()==next){
			String status=tcstatus.getText();
			try {
				tcstatus.setText("");
				sub_int.nextTCstep();
			} catch (OperationNotPossibleException e1) {
				e1.printStackTrace();
				tcstatus.setText(status);
				tcerr.setText("operazione fallita");
			}
		}
		//restart the tuple center execution
		if (e.getSource()==resume){
			String status=tcstatus.getText();
			try {
				tcstatus.setText("");
				sub_int.resumeTC();
			} catch (OperationNotPossibleException e1) {
				e1.printStackTrace();
				tcstatus.setText(status);
				tcerr.setText("operazione fallita");
			}
		}
		//open a shell (of the simple type)
		if (e.getSource()==shell){
			new ShellRunner().start();
		}
		
		//actions relative to the buttons for the updating options
		
		if (e.getSource()==tuple_manual_agg){
			tupleUpdater.stop();
			tuple_agg.setEnabled(tuple_manual_agg.isSelected());
			tuple_time_agg.setEditable(tuple_automatic_agg.isSelected());
			tuple_time_start.setEnabled(tuple_automatic_agg.isSelected());
		}
		if (e.getSource()==tuple_agg){
			updateTuples();
		}
		if (e.getSource()==tuple_automatic_agg){
			tuple_agg.setEnabled(tuple_manual_agg.isSelected());
			tuple_time_agg.setEditable(tuple_automatic_agg.isSelected());
			tuple_time_start.setEnabled(tuple_automatic_agg.isSelected());
		}
		if (e.getSource()==tuple_time_start){
			tupleUpdater=new Updater("tuple");
			tupleUpdater.start();
		}
		
		if (e.getSource()==spec_manual_agg){
			specUpdater.stop();
			spec_agg.setEnabled(spec_manual_agg.isSelected());
			spec_time_agg.setEditable(spec_automatic_agg.isSelected());
			spec_time_start.setEnabled(spec_automatic_agg.isSelected());
		}
		if (e.getSource()==spec_agg){
			updateSpec();
		}
		if (e.getSource()==spec_automatic_agg){
			spec_agg.setEnabled(spec_manual_agg.isSelected());
			spec_time_agg.setEditable(spec_automatic_agg.isSelected());
			spec_time_start.setEnabled(spec_automatic_agg.isSelected());
		}
		if (e.getSource()==spec_time_start){
			specUpdater=new Updater("spec");
			specUpdater.start();
		}
		
		if (e.getSource()==query_manual_agg){
			queryUpdater.stop();
			query_agg.setEnabled(query_manual_agg.isSelected());
			query_time_agg.setEditable(query_automatic_agg.isSelected());
			query_time_start.setEnabled(query_automatic_agg.isSelected());
		}
		if (e.getSource()==query_agg){
			updateQuery();
		}
		if (e.getSource()==query_automatic_agg){
			query_agg.setEnabled(query_manual_agg.isSelected());
			query_time_agg.setEditable(query_automatic_agg.isSelected());
			query_time_start.setEnabled(query_automatic_agg.isSelected());
		}
		if (e.getSource()==query_time_start){
			queryUpdater=new Updater("query");
			queryUpdater.start();
		}
		
		//actions relative to the instruments of the tab "tuples"
		
		//add the specified tuple to the tuples set
		if (e.getSource()==tuple_add){
			String tuple=tuple_body.getText();
			try {
				sub_int.addTuple(tuple);
			} catch (InvalidLogicTupleException e1) {
				e1.printStackTrace();
				tuple_body.setText("tupla non valida");
			} catch (OperationNotPossibleException e1) {
				e1.printStackTrace();
				tuple_body.setText("operazione fallita");
			} catch (InvalidAgentIdException e1) {
				e1.printStackTrace();
				tuple_body.setText("accesso negato");
			}
			updateTuples();
		}
		//remove the selected tuples from the set of tuples
		if (e.getSource()==tuple_del){
			Object[] ob_list=tuples.getSelectedValues();
			String[] tuple_list=new String[ob_list.length];
			for (int k=0;k<ob_list.length;k++) tuple_list[k]=(String)ob_list[k];
			for (int i=0;i<tuple_list.length;i++)
				try {
					sub_int.removeTuple(tuple_list[i]);
				} catch (InvalidLogicTupleException e1) {
					e1.printStackTrace();
					tuple_body.setText("tupla non valida");
					break;
				} catch (OperationNotPossibleException e1) {
					e1.printStackTrace();
					tuple_body.setText("operazione fallita");
					break;
				} catch (InvalidAgentIdException e1) {
					e1.printStackTrace();
					tuple_body.setText("accesso negato");
					break;
				}
				updateTuples();
		}
		//remove a single selected tuple in the set and place it in the input filed for editing it
		if (e.getSource()==tuple_edit){
			String tuple=(String)tuples.getSelectedValue();
			tuple_body.setText(tuple);
			try {
				sub_int.removeTuple(tuple);
			} catch (InvalidLogicTupleException e1) {
				e1.printStackTrace();
				tuple_body.setText("tupla non valida");
			} catch (OperationNotPossibleException e1) {
				e1.printStackTrace();
				tuple_body.setText("operazione fallita");
			} catch (InvalidAgentIdException e1) {
				e1.printStackTrace();
				tuple_body.setText("accesso negato");
			}
			updateTuples();
		}
		//load tuples from a file and add them too the tuples set
		if (e.getSource()==tuple_load){
			int load_res=loader.showOpenDialog(this);
			if (load_res==JFileChooser.APPROVE_OPTION){
				File file=loader.getSelectedFile();
				BufferedReader br;
				try {
					br=new BufferedReader(new FileReader(file));
					String line=br.readLine();
					while(line!=null){
						try {
							sub_int.addTuple(line);
						} catch (InvalidLogicTupleException e1) {
							e1.printStackTrace();
							tuple_body.setText("tupla di specifica non valida");
						} catch (OperationNotPossibleException e1) {
							e1.printStackTrace();
							tuple_body.setText("operazione fallita");
						} catch (InvalidAgentIdException e1) {
							e1.printStackTrace();
							tuple_body.setText("accesso negato");
						}
						line = br.readLine();
					}
					br.close();
					updateTuples();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					tuple_body.setText("file non trovato");
				} catch (IOException e1) {
					e1.printStackTrace();
					tuple_body.setText("errore di lettura da file");
				}
			}
		}
		//save the selected tuples in a file
		if (e.getSource()==tuple_save){
			int load_res=loader.showOpenDialog(this);
			if (load_res==JFileChooser.APPROVE_OPTION){
				File file=loader.getSelectedFile();
				Object[] items=tuples.getSelectedValues();
				for (int i=0;i<items.length;i++){
					try {
						writeOnFile(file,(String)items[i]+"\n");
					} catch (IOException e1) {
						e1.printStackTrace();
						tuple_body.setText("errore nella scrittura del file");
					}
				}
			}
		}
		
		//actions relative to the instruments of the tab "specification"
		
		//open the window for the driven specification tuple's definition
		if (e.getSource()==reaction_create){
			new ReactionDefGUI(this);
		}
		if (e.getSource()==reaction_add){
			String reaction=reaction_body.getText();
			try {
				sub_int.addSpec(reaction);
			} catch (InvalidSpecificationException e1) {
				e1.printStackTrace();
				reaction_body.setText("sintassi reazione non valida");
			} catch (InvalidLogicTupleException e1) {
				e1.printStackTrace();
				reaction_body.setText("sintassi reazione non valida");
			} catch (OperationNotPossibleException e1) {
				e1.printStackTrace();
				reaction_body.setText("operazione fallita");
			} catch (InvalidAgentIdException e1) {
				e1.printStackTrace();
				reaction_body.setText("accesso negato");
			}
			updateSpec();
		}
		//remove the selected specification tuples from the specification tuples set
		if (e.getSource()==reaction_del){
			Object[] ob_list=specification.getSelectedValues();
			String[] reaction_list=new String[ob_list.length];
			for (int k=0;k<ob_list.length;k++) reaction_list[k]=(String)ob_list[k];
			for (int i=0;i<reaction_list.length;i++)
				try {
					sub_int.removeSpec(reaction_list[i]);
				} catch (InvalidSpecificationException e1) {
					e1.printStackTrace();
					reaction_body.setText("sintassi reazione non valida");
				} catch (InvalidLogicTupleException e1) {
					e1.printStackTrace();
					reaction_body.setText("sintassi reazione non valida");
				} catch (OperationNotPossibleException e1) {
					e1.printStackTrace();
					reaction_body.setText("operazione fallita");
				} catch (InvalidAgentIdException e1) {
					e1.printStackTrace();
					reaction_body.setText("accesso negato");
				}
			updateSpec();
		}
		//remove a single selected specification tuple from the set and place it in the input field for the editing
		if (e.getSource()==reaction_edit){
			String reaction=(String)specification.getSelectedValue();
			try {
				sub_int.removeSpec(reaction);
				reaction_body.setText(reaction);
			} catch (InvalidSpecificationException e1) {
				e1.printStackTrace();
				reaction_body.setText("sintassi reazione non valida");
			} catch (InvalidLogicTupleException e1) {
				e1.printStackTrace();
				reaction_body.setText("sintassi reazione non valida");
			} catch (OperationNotPossibleException e1) {
				e1.printStackTrace();
				reaction_body.setText("operazione fallita");
			} catch (InvalidAgentIdException e1) {
				e1.printStackTrace();
				reaction_body.setText("accesso negato");
			}
			updateSpec();
		}
		//load one or more specification tuples from a file and add them to the set
		if (e.getSource()==spec_load){
			int load_res=loader.showOpenDialog(this);
			if (load_res==JFileChooser.APPROVE_OPTION){
				File file=loader.getSelectedFile();
				BufferedReader br;
				try {
					br=new BufferedReader(new FileReader(file));
					String line=br.readLine();
					while(line!=null){
						try {
							sub_int.addSpec(line);
						} catch (InvalidSpecificationException e1) {
							e1.printStackTrace();
							reaction_body.setText("tupla di specifica non valida");
						} catch (InvalidLogicTupleException e1) {
							e1.printStackTrace();
							reaction_body.setText("tupla di specifica non valida");
						} catch (OperationNotPossibleException e1) {
							e1.printStackTrace();
							reaction_body.setText("operazione fallita");
						} catch (InvalidAgentIdException e1) {
							e1.printStackTrace();
							reaction_body.setText("accesso negato");
						}
						line = br.readLine();
					}
					br.close();
					updateSpec();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					reaction_body.setText("file non trovato");
				} catch (IOException e1) {
					e1.printStackTrace();
					reaction_body.setText("errore di lettura da file");
				}
			}
		}
		//save the selected specification tuples on a file
		if (e.getSource()==spec_save){
			int load_res=loader.showOpenDialog(this);
			if (load_res==JFileChooser.APPROVE_OPTION){
				File file=loader.getSelectedFile();
				Object[] items=specification.getSelectedValues();
				for (int i=0;i<items.length;i++){
					try {
						writeOnFile(file,(String)items[i]+"\n");
					} catch (IOException e1) {
						e1.printStackTrace();
						reaction_body.setText("errore nella scrittura del file");
					}
				}
			}
		}
		//load the whole tuple centre specification from a file and set it as the current one
		if (e.getSource()==specification_load){
			int load_res=loader.showOpenDialog(this);
			if (load_res==JFileChooser.APPROVE_OPTION){
				File file=loader.getSelectedFile();
				String spec;
				try {
					spec = getByFile(file);
					sub_int.setSpec(spec);
				} catch (IOException e1) {
					e1.printStackTrace();
					reaction_body.setText("errore nella lettura da file");
				} catch (InvalidSpecificationException e1) {
					e1.printStackTrace();
					reaction_body.setText("specifica non valida");
				}
				updateSpec();
			}
		}
		//save the whole tuple center's specification on a file
		if (e.getSource()==specification_save){
			int load_res=loader.showOpenDialog(this);
			if (load_res==JFileChooser.APPROVE_OPTION){
				File file=loader.getSelectedFile();
				String spec=sub_int.getSpec();
				try {
					writeOnFile(file,spec);
				} catch (IOException e1) {
					e1.printStackTrace();
					reaction_body.setText("errore nella scrittura su file");
				}
			}
		}
		
		//actions relative to the instruments of the tab "query"
		
		//remove the selected queries from the ongoing queries set
		if (e.getSource()==query_terminate){
			Object[] ob_list=queries.getSelectedValues();
			String[] query_list=new String[ob_list.length];
			for (int i=0;i<query_list.length;i++) query_list[i]=(String)ob_list[i];
			for (int j=0;j<query_list.length;j++)
				try {
					sub_int.cancelQuery(query_list[j]);
				} catch (InvalidLogicTupleException e1) {
					e1.printStackTrace();
					
				} catch (InvalidTupleOperationException e1) {
					e1.printStackTrace();
					
				}
			//updateQuery();
		}
		
		//actions relative to the instruments of the tab "reactions"
		
		//set the specified value in the field of the reactions' filter
		if (e.getSource()==reaction_set_filtre){
			reaction_filtre.setText(reaction_new_filtre.getText());
		}
		//clear the list of the happened reactions
		if (e.getSource()==reaction_clear){
			reactions.setText("");
		}
		
	}
	
	
	//Inner class for updating the visualization of the set of tuples, specification tuple, queries or reactions.
	//The type of set updated depends by the parameter passed to the constructors
	
	private class Updater extends Thread{
		
		private String type;
		private float waittime;
		
		private Updater(String type){
			super();
			this.type=type;
		}
		
		public void run(){
			if (type.equals("tuple")){
				while (tuple_automatic_agg.isSelected()){
					waittime=Float.valueOf(tuple_time_agg.getText());
					updateTuples();
					try {
						sleep((long) waittime*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						tuple_time_agg.setText("error");
					}
				}
			}
			if (type.equals("spec")){
				while (spec_automatic_agg.isSelected()){
					waittime=Float.valueOf(spec_time_agg.getText());
					updateSpec();
					try {
						sleep((long) waittime*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						spec_time_agg.setText("error");
					}
				}
			}
			if (type.equals("query")){
				while (query_automatic_agg.isSelected()){
					waittime=Float.valueOf(query_time_agg.getText());
					updateQuery();
					try {
						sleep((long) waittime*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						query_time_agg.setText("error");
					}
				}
			}
			if (type.equals("reaction")){
				while (true){
					updateReactions();
					
				}
			}
		}
		
	}
	
	
	//Inner class which allows the opening of a shell (of simple type) on a different thread
	
	private class ShellRunner extends Thread{
		
		private ShellRunner(){
			super();
		}
		
		public void run(){
			try {
				new SimpleShellGUI(new RespectShellSubInterface(((RespectInspectorSubInterface) sub_int).getFramework())/*,new AgentId("shell")*/);
			} catch (InvalidTupleCentreIdException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	
	
	public static void main(String[] args) {
		try {
			new InspectorGUI(new RespectInspectorSubInterface());
		} catch (InvalidTupleCentreIdException e) {
			e.printStackTrace();
			
		}
	}

	

}
