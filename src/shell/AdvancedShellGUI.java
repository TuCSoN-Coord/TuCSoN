package shell;

import reaction.ISpecificationDefiner;
import reaction.ReactionDefGUI;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

import alice.logictuple.InvalidLogicTupleException;
import alice.respect.api.InstantiationNotPossibleException;
import alice.respect.api.InvalidAgentIdException;
import alice.respect.api.InvalidSpecificationException;
import alice.respect.api.InvalidTupleCentreIdException;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.RespectFramework;

//Class which implements the interface of the advance type shell
//this kind of shell interface allows a driven method for defining operation on a tuple center, towards a set of graphic instruments
//
//NOTA: NOTE: this class doesn't use directly the ReSpecT API, but interacts with tuple centers through an IRespectShellSubInterface interface

@SuppressWarnings("serial")
public class AdvancedShellGUI extends JFrame implements ISpecificationDefiner, ActionListener
{
	private IRespectShellSubInterface subint;
	private JButton simple_shell, new_shell, exec_op, create, load, load_spec, get, exec_op2;
	private JRadioButton local, remote;
	private JTextField tc_name, tc_addr, tuple_arg, spec_arg;
	private JComboBox tuple_op, spec_op;
	private JTextArea output;
	private JFileChooser loader;

	public AdvancedShellGUI()
	{
		super("shell comandi");
		setVisible(true);
	}

	public AdvancedShellGUI(IRespectShellSubInterface subint)
	{
		super("shell comandi");
		this.subint = subint;
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(Box.createVerticalStrut(10));
		Box box1 = new Box(BoxLayout.X_AXIS);
		JLabel icon = new JLabel();
		icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alice/respect/images/logo.png")));
		box1.add(icon);
		box1.add(Box.createHorizontalStrut(10));
		Box box2 = new Box(BoxLayout.Y_AXIS);

		// this panel contains the instruments for the selection of the tuple
		// center
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout(FlowLayout.RIGHT));
		simple_shell = new JButton("shell semplice");
		simple_shell.addActionListener(this);
		panel1.add(simple_shell);
		box2.add(panel1);
		box2.add(Box.createVerticalStrut(5));
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.RIGHT));
		new_shell = new JButton("nuova shell");
		new_shell.addActionListener(this);
		panel2.add(new_shell);
		box2.add(panel2);
		box2.add(Box.createVerticalStrut(20));
		local = new JRadioButton("tuple centre locale");
		local.addActionListener(this);
		remote = new JRadioButton("tuple centre remoto");
		remote.addActionListener(this);
		ButtonGroup group = new ButtonGroup();
		group.add(local);
		group.add(remote);
		local.setSelected(true);
		Box box2_0 = new Box(BoxLayout.X_AXIS);
		box2_0.add(local);
		box2_0.add(remote);
		box2.add(box2_0);
		Box box2_1 = new Box(BoxLayout.X_AXIS);
		box2_1.add(new JLabel("nome del tuple centre"));
		box2_1.add(Box.createHorizontalStrut(20));
		tc_name = new JTextField();
		box2_1.add(tc_name);
		box2.add(box2_1);
		Box box2_2 = new Box(BoxLayout.X_AXIS);
		box2_2.add(new JLabel("indirizzo del tuple centre"));
		box2_2.add(Box.createHorizontalStrut(20));
		tc_addr = new JTextField();
		tc_addr.setEditable(remote.isSelected());
		box2_2.add(tc_addr);
		box2.add(box2_2);
		box1.add(box2);
		box1.add(Box.createHorizontalStrut(10));
		box.add(box1);
		box.add(Box.createVerticalStrut(10));

		// this panel contains the instruments for operating with the tuples set
		// of the tuple center
		JPanel panel3 = new JPanel();
		panel3.setLayout(new GridBagLayout());
		panel3.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "operazioni su tuple",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints = new java.awt.GridBagConstraints();
		constraints.gridheight = 2;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		constraints.weightx = 100.0;
		Box box3 = new Box(BoxLayout.Y_AXIS);
		Box box3_1 = new Box(BoxLayout.X_AXIS);
		Box box3_1_1 = new Box(BoxLayout.Y_AXIS);
		box3_1_1.add(new JLabel("nome operazione"));
		box3_1_1.add(new JLabel("template o tupla argomento"));
		box3_1.add(box3_1_1);
		box3_1.add(Box.createHorizontalStrut(10));
		Box box3_1_2 = new Box(BoxLayout.Y_AXIS);
		tuple_op = new JComboBox();
		tuple_op.addItem("out");
		tuple_op.addItem("in");
		tuple_op.addItem("rd");
		tuple_op.addItem("inp");
		tuple_op.addItem("rdp");
		tuple_op.setEditable(true);
		box3_1_2.add(tuple_op);
		tuple_arg = new JTextField();
		box3_1_2.add(tuple_arg);
		box3_1.add(box3_1_2);
		box3.add(box3_1);
		exec_op = new JButton("esegui");
		exec_op.addActionListener(this);
		box3.add(exec_op);
		panel3.add(box3, constraints);
		box.add(panel3);
		box.add(Box.createVerticalStrut(10));

		// this panel contains the instruments for operating with the tuple
		// centre specification
		JPanel panel4 = new JPanel();
		panel4.setLayout(new GridBagLayout());
		panel4.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(),
				"operazioni su tuple di specifica", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints2 = new java.awt.GridBagConstraints();
		constraints2.gridheight = 2;
		constraints2.gridx = 0;
		constraints2.gridy = 0;
		constraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		constraints2.weightx = 100.0;
		Box box4 = new Box(BoxLayout.Y_AXIS);
		Box box4_1 = new Box(BoxLayout.X_AXIS);
		Box box4_1_1 = new Box(BoxLayout.Y_AXIS);
		box4_1_1.add(new JLabel("operazione"));
		box4_1_1.add(new JLabel("tupla o template di specifica"));
		box4_1.add(box4_1_1);
		box4_1.add(Box.createHorizontalStrut(10));
		Box box4_1_2 = new Box(BoxLayout.Y_AXIS);
		spec_op = new JComboBox();
		spec_op.addItem("out_s");
		spec_op.addItem("in_s");
		spec_op.addItem("rd_s");
		spec_op.addItem("inp_s");
		spec_op.addItem("rdp_s");
		spec_op.setEditable(true);
		box4_1_2.add(spec_op);
		spec_arg = new JTextField();
		box4_1_2.add(spec_arg);
		box4_1.add(box4_1_2);
		box4.add(box4_1);
		Box box4_2 = new Box(BoxLayout.X_AXIS);
		create = new JButton("definizione guidata");
		create.addActionListener(this);
		box4_2.add(create);
		box4_2.add(Box.createHorizontalStrut(10));
		load = new JButton("carica da file");
		load.addActionListener(this);
		box4_2.add(load);
		box4_2.add(Box.createHorizontalStrut(10));
		exec_op2 = new JButton("esegui");
		exec_op2.addActionListener(this);
		box4_2.add(exec_op2);
		box4_2.add(Box.createHorizontalStrut(50));
		box4.add(box4_2);
		box4.add(Box.createVerticalStrut(10));
		Box box4_3 = new Box(BoxLayout.X_AXIS);
		load_spec = new JButton("setta specifica da file");
		load_spec.addActionListener(this);
		box4.add(load_spec);
		box4_3.add(Box.createHorizontalStrut(10));
		get = new JButton("ottieni specifica d'implementazione");
		get.addActionListener(this);
		box4.add(get);
		panel4.add(box4, constraints2);
		box.add(panel4);

		// this panel contain the output area
		JPanel panel5 = new JPanel();
		panel5.setLayout(new GridBagLayout());
		panel5.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "output",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints3 = new java.awt.GridBagConstraints();
		constraints3.gridheight = 2;
		constraints3.gridx = 0;
		constraints3.gridy = 0;
		constraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
		constraints3.weightx = 100.0;
		output = new JTextArea();
		output.setRows(10);
		output.setEditable(false);
		JScrollPane scroll2 = new JScrollPane();
		scroll2.setViewportView(output);
		panel5.add(scroll2, constraints3);
		box.add(panel5);
		box.add(Box.createVerticalStrut(10));

		Box framebox = new Box(BoxLayout.X_AXIS);
		framebox.add(Box.createHorizontalStrut(10));
		framebox.add(box);
		framebox.add(Box.createHorizontalStrut(10));
		getContentPane().add(framebox);
		pack();
		setVisible(true);

		loader = new JFileChooser();
	}

	// Utility function which allows the reading of the content of a file
	private String getByFile(File file) throws IOException
	{
		String res = "";
		BufferedReader br;
		br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		while (line != null)
		{
			res += line + " \n";
			line = br.readLine();
		}
		br.close();
		return res;
	}

	// Procedure specified in the ISpecificatinDefiner interface:
	// it impose as new specification tuple the one passed as parameter
	public void ObtainSpecification(String reaction)
	{
		spec_arg.setText(reaction);
	}

	public void actionPerformed(ActionEvent e)
	{
		// change the shell type (open the advanced one)
		if (e.getSource() == simple_shell)
		{
			RespectShellSubInterface sb = (RespectShellSubInterface) subint;
			RespectFramework fw = sb.getFramework();
			try
			{
				new SimpleShellGUI(new RespectShellSubInterface(fw)/* ,id */).setDefaultCloseOperation(EXIT_ON_CLOSE);
			}
			catch (InvalidTupleCentreIdException e1)
			{
				e1.printStackTrace();

			}
			dispose();
		}
		// open a new shell window (of the advanced type shell)
		if (e.getSource() == new_shell)
		{
			RespectShellSubInterface sb = (RespectShellSubInterface) subint;
			RespectFramework fw = sb.getFramework();
			try
			{
				new AdvancedShellGUI(new RespectShellSubInterface(fw)/* ,id */);
			}
			catch (InvalidTupleCentreIdException e1)
			{
				e1.printStackTrace();
			}
		}
		// change the tuple center type selection into local/remote
		if ((e.getSource() == local) || (e.getSource() == remote))
		{
			tc_addr.setEditable(remote.isSelected());
		}
		// execute the specified operation on the tuples set of the tuple center
		if (e.getSource() == exec_op)
		{
			new respectPrimitiveExecutioner().start();
		}
		// execute the specified operation on the specification of the tuple
		// center
		if (e.getSource() == exec_op2)
		{
			new respectSpecPrimitiveExecutioner().start();
		}
		// open a window for the driven definition of a new specification tuple
		if (e.getSource() == create)
		{
			new ReactionDefGUI(this);
		}
		// get the current specification of the tuple centre
		if (e.getSource() == get)
		{
			new respectGetSpecificationExecutioner().start();
		}
		// get a single specification tuple's definition from a file
		if (e.getSource() == load)
		{
			int load_res = loader.showOpenDialog(this);
			if (load_res == JFileChooser.APPROVE_OPTION)
			{
				File file = loader.getSelectedFile();
				String reac;
				try
				{
					reac = getByFile(file);
					spec_arg.setText(reac);
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
					output.append("\berrore nella lettura del file\n");
				}
			}
		}
		// get the whole tuple centre's specification from a file and set it
		if (e.getSource() == load_spec)
		{
			int load_res = loader.showOpenDialog(this);
			if (load_res == JFileChooser.APPROVE_OPTION)
			{
				File file = loader.getSelectedFile();
				try
				{
					String spec = getByFile(file);
					new respectSetSpecificationExecutioner(spec).start();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
					output.append("\berrore nella lettura del file\n");
				}
			}
		}
	}

	// inner class for the execution of the operation on tuples set, specified
	// in the "tuple" panel

	private class respectPrimitiveExecutioner extends Thread
	{

		private String tuplecentre;

		private respectPrimitiveExecutioner()
		{
			super();
			if (local.isSelected())
			{
				tuplecentre = tc_name.getText();
				if (tuplecentre.equals(""))
					tuplecentre = "default";
			}
			else
				tuplecentre = tc_addr.getText() + "@" + tc_name.getText();
		}

		public void run()
		{
			exec_op.setEnabled(false);
			exec_op2.setEnabled(false);
			simple_shell.setEnabled(false);

			try
			{
				subint.focusOnTC(tuplecentre);
			}
			catch (InvalidTupleCentreIdException e)
			{
				e.printStackTrace();
				output.append("\btuple centre non valido\n");
				exec_op.setEnabled(true);
				exec_op2.setEnabled(true);
				simple_shell.setEnabled(true);
				return;
			}
			String result;
			result = "operazione non riconosciuta";
			String op = (String) tuple_op.getSelectedItem();
			String tuple = tuple_arg.getText();

			try
			{
				if (op.equals("out"))
					result = subint.out(tuple);
				if (op.equals("in"))
					result = subint.in(tuple);
				if (op.equals("rd"))
					result = subint.rd(tuple);
				if (op.equals("inp"))
					result = subint.inp(tuple);
				if (op.equals("rdp"))
					result = subint.rdp(tuple);
			}
			catch (InvalidLogicTupleException e)
			{
				e.printStackTrace();
				output.append("\btupla non valida\n");
				exec_op.setEnabled(true);
				exec_op2.setEnabled(true);
				simple_shell.setEnabled(true);
				return;
			}
			catch (OperationNotPossibleException e)
			{
				e.printStackTrace();
				output.append("\boperazione non riuscita\n");
				exec_op.setEnabled(true);
				exec_op2.setEnabled(true);
				simple_shell.setEnabled(true);
				return;
			}
			catch (InvalidAgentIdException e)
			{
				e.printStackTrace();
				output.append("\baccesso negato\n");
				exec_op.setEnabled(true);
				exec_op2.setEnabled(true);
				simple_shell.setEnabled(true);
				return;
			}

			output.append("\btuple centre: " + tuplecentre + ":   " + result + "\n");
			exec_op.setEnabled(true);
			exec_op2.setEnabled(true);
			simple_shell.setEnabled(true);
		}

	}

	// inner class for the execution of the operation on the tuple centre's
	// specification, defined in the "specificatin" panel

	private class respectSpecPrimitiveExecutioner extends Thread
	{

		private String tuplecentre;

		private respectSpecPrimitiveExecutioner()
		{
			super();
			if (local.isSelected())
			{
				tuplecentre = tc_name.getText();
				if (tuplecentre.equals(""))
					tuplecentre = "default";
			}
			else
				tuplecentre = tc_addr.getText() + "@" + tc_name.getText();
		}

		public void run()
		{
			try
			{
				subint.focusOnTC(tuplecentre);
			}
			catch (InvalidTupleCentreIdException e)
			{
				e.printStackTrace();
				output.append("\btuple centre non valido\n");
				return;
			}
			String result;
			result = "operazione non riconosciuta";
			String op = (String) spec_op.getSelectedItem();
			String tuple = spec_arg.getText();

			try
			{
				if (op.equals("out_s"))
					result = subint.outS(tuple);
				if (op.equals("in_s"))
					result = subint.inS(tuple);
				if (op.equals("rd_s"))
					result = subint.rdS(tuple);
				if (op.equals("inp_s"))
					result = subint.inpS(tuple);
				if (op.equals("rdp_s"))
					result = subint.rdpS(tuple);
			}
			catch (InvalidLogicTupleException e)
			{
				e.printStackTrace();
				output.append("\btupla di specifica non valida\n");
				return;
			}
			catch (OperationNotPossibleException e)
			{
				e.printStackTrace();
				output.append("\boperazione non riuscita\n");
				return;
			}
			catch (InvalidAgentIdException e)
			{
				e.printStackTrace();
				output.append("\baccesso negato\n");
				return;
			}

			output.append("\btuple centre: " + tuplecentre + ":   " + result + "\n");
		}

	}

	// inner class for setting the tuple center's specification with the one
	// specified at constructors level

	private class respectSetSpecificationExecutioner extends Thread
	{

		private String tuplecentre;
		private String spec;

		private respectSetSpecificationExecutioner(String specification)
		{
			super();
			if (local.isSelected())
			{
				tuplecentre = tc_name.getText();
				if (tuplecentre.equals(""))
					tuplecentre = "default";
			}
			else
				tuplecentre = tc_addr.getText() + "@" + tc_name.getText();
			spec = specification;
		}

		public void run()
		{
			try
			{
				subint.focusOnTC(tuplecentre);
			}
			catch (InvalidTupleCentreIdException e)
			{
				e.printStackTrace();
				output.append("\btuple centre non valido");
				return;
			}
			String result;
			try
			{
				result = subint.setSpec(spec);
			}
			catch (InvalidSpecificationException e)
			{
				e.printStackTrace();
				output.append("\bspecifica invalida\n");
				return;
			}
			catch (InvalidAgentIdException e)
			{
				e.printStackTrace();
				output.append("\baccesso negato\n");
				return;
			}
			catch (OperationNotPossibleException e)
			{
				e.printStackTrace();
				output.append("\boperazione negata\n");
				return;
			}
			
			output.append("\btuple centre: " + tuplecentre + ":   " + result + "\n");
		}

	}

	// inner class for getting the specification of the the tuple center

	private class respectGetSpecificationExecutioner extends Thread
	{

		private String tuplecentre;

		private respectGetSpecificationExecutioner()
		{
			super();
			if (local.isSelected())
			{
				tuplecentre = tc_name.getText();
				if (tuplecentre.equals(""))
					tuplecentre = "default";
			}
			else
				tuplecentre = tc_addr.getText() + "@" + tc_name.getText();
		}

		public void run()
		{
			try
			{
				subint.focusOnTC(tuplecentre);
			}
			catch (InvalidTupleCentreIdException e)
			{
				e.printStackTrace();
				output.append("\btuple centre non valido\n");
				return;
			}
			String result;
			try
			{
				result = subint.getSpec();
			}
			catch (InvalidAgentIdException e)
			{
				e.printStackTrace();
				output.append("\baccesso negato\n");
				return;
			}
			catch (OperationNotPossibleException e)
			{
				e.printStackTrace();
				output.append("\baccesso negato\n");
				return;
			}
			output.append("\btuple centre: " + tuplecentre + ":   " + result + "\n");
		}

	}

	public static void main(String[] args) throws OperationNotPossibleException, InvalidTupleCentreIdException,
			InstantiationNotPossibleException, InvalidAgentIdException
	{
		new AdvancedShellGUI(new RespectShellSubInterface()).setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}
