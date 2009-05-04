package shell;

import javax.swing.*;

import alice.logictuple.InvalidLogicTupleException;
import alice.respect.api.InstantiationNotPossibleException;
import alice.respect.api.InvalidAgentIdException;
import alice.respect.api.InvalidSpecificationException;
import alice.respect.api.InvalidTupleCentreIdException;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.RespectFramework;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

//Class which implements the interface of the simple type shell
//this kind of shell interface allows to define operations on a tuple center by a simple command line
//
//NOTE: this class doesn't use directly the ReSpecT API, but interacts with tuple centers through an IRespectShellSubInterface interface

@SuppressWarnings("serial")
public class SimpleShellGUI extends JFrame implements ActionListener
{
	private IRespectShellSubInterface subint;
	private JTextField input, tcname, tcaddr;
	private JTextArea out;
	private JButton adv_shell, new_shell, load;
	private JCheckBox fix;
	private JRadioButton local, remote;
	private JLabel label1, label2;
	private JFileChooser loader;

	public SimpleShellGUI(IRespectShellSubInterface subint)
	{
		super("shell comandi");
		this.subint = subint;
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(Box.createVerticalStrut(10));

		Box box1 = new Box(BoxLayout.X_AXIS);
		JLabel icon = new JLabel();
		icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alice/respect/images/logo.png")));
		box1.add(icon);
		Box box2 = new Box(BoxLayout.Y_AXIS);
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout(FlowLayout.RIGHT));
		adv_shell = new JButton("shell avanzata");
		adv_shell.addActionListener(this);
		panel1.add(adv_shell);
		box2.add(panel1);
		box2.add(Box.createVerticalStrut(5));
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.RIGHT));
		new_shell = new JButton("nuova shell");
		new_shell.addActionListener(this);
		panel2.add(new_shell);
		box2.add(panel2);
		box2.add(Box.createVerticalStrut(30));

		fix = new JCheckBox("tuple centre fissato");
		fix.addActionListener(this);
		fix.setSelected(false);
		box2.add(fix);
		fix.setSelected(false);
		box2.add(Box.createVerticalStrut(10));
		Box box2_1 = new Box(BoxLayout.X_AXIS);
		local = new JRadioButton("tuple centre locale");
		local.addActionListener(this);
		box2_1.add(local);
		remote = new JRadioButton("tuple centre remoto");
		remote.addActionListener(this);
		box2_1.add(remote);
		ButtonGroup group = new ButtonGroup();
		group.add(local);
		group.add(remote);
		local.setSelected(true);
		box2.add(box2_1);

		Box box2_2 = new Box(BoxLayout.X_AXIS);
		box2_2.add(Box.createHorizontalStrut(10));
		Box box2_2_1 = new Box(BoxLayout.Y_AXIS);
		label1 = new JLabel("nome del tuple centre");
		box2_2_1.add(label1);
		label2 = new JLabel("indirizzo del tuple centre");
		box2_2_1.add(label2);
		box2_2.add(box2_2_1);
		Box box2_2_2 = new Box(BoxLayout.Y_AXIS);
		tcname = new JTextField();
		box2_2_2.add(tcname);
		tcaddr = new JTextField();
		box2_2_2.add(tcaddr);
		box2_2.add(box2_2_2);
		box2.add(box2_2);

		local.setEnabled(fix.isSelected());
		remote.setEnabled(fix.isSelected());
		label1.setEnabled(fix.isSelected());
		label2.setEnabled(fix.isSelected());
		tcname.setEnabled(fix.isSelected());
		tcaddr.setEnabled((fix.isSelected()) && (remote.isSelected()));

		box2.add(Box.createVerticalStrut(10));
		box1.add(box2);
		box.add(box1);

		box.add(Box.createVerticalStrut(5));
		load = new JButton("setta specifica da file");
		load.addActionListener(this);
		box.add(load);
		box.add(Box.createVerticalStrut(5));

		input = new JTextField();
		input.addActionListener(this);
		JPanel panel3 = new JPanel();
		panel3.setLayout(new GridBagLayout());
		panel3.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "input",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints = new java.awt.GridBagConstraints();
		constraints.gridheight = 2;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		constraints.weightx = 100.0;
		panel3.add(input, constraints);
		box.add(panel3);

		out = new JTextArea();
		out.setRows(10);
		out.setEditable(false);
		JPanel panel4 = new JPanel();
		panel4.setLayout(new GridBagLayout());
		panel4.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "output",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Verdana", 0, 11)));
		GridBagConstraints constraints2 = new java.awt.GridBagConstraints();
		constraints2.gridheight = 2;
		constraints2.gridx = 0;
		constraints2.gridy = 0;
		constraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		constraints2.weightx = 100.0;
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(out);
		panel4.add(scroll, constraints2);
		box.add(panel4);

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

	public void actionPerformed(ActionEvent e)
	{
		// change the shell type (open the advanced shell type)
		if (e.getSource() == adv_shell)
		{
			RespectShellSubInterface sb = (RespectShellSubInterface) subint;
			RespectFramework fw = sb.getFramework();
			try
			{
				new AdvancedShellGUI(new RespectShellSubInterface(fw)).setDefaultCloseOperation(EXIT_ON_CLOSE);
			}
			catch (InvalidTupleCentreIdException e1)
			{
				e1.printStackTrace();

			}
			dispose();
		}
		// open a new shell window (of the simple type shell)
		else if (e.getSource() == new_shell)
		{
			RespectShellSubInterface sb = (RespectShellSubInterface) subint;
			RespectFramework fw = sb.getFramework();
			try
			{
				new SimpleShellGUI(new RespectShellSubInterface(fw));
			}
			catch (InvalidTupleCentreIdException e1)
			{
				e1.printStackTrace();
			}
		}
		// enable/disable the manual selection of the tuple center
		else if (e.getSource() == fix)
		{
			local.setEnabled(fix.isSelected());
			remote.setEnabled(fix.isSelected());
			label1.setEnabled(fix.isSelected());
			label2.setEnabled(fix.isSelected());
			tcname.setEnabled(fix.isSelected());
			tcaddr.setEnabled((fix.isSelected()) && (remote.isSelected()));
		}
		// change the tuple center type selection into local/remote
		else if ((e.getSource() == local) || (e.getSource() == remote))
		{
			tcaddr.setEnabled(remote.isSelected());
		}
		// load the tuple center specification from a file
		else if (e.getSource() == load)
		{
			int load_res = loader.showOpenDialog(this);
			if (load_res == JFileChooser.APPROVE_OPTION)
			{
				File file = loader.getSelectedFile();
				String spec;
				try
				{
					spec = getByFile(file);
					subint.setSpec(spec);
					out.append("\bspecifica settata\n");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
					out.append("\berrore nella lettura del file\n");
				}
				catch (InvalidSpecificationException e1)
				{
					e1.printStackTrace();
					out.append("specifica invalida");
				}
				catch (InvalidAgentIdException e1)
				{
					e1.printStackTrace();
					out.append("accesso negato");
				}
				catch (OperationNotPossibleException e1)
				{
					e1.printStackTrace();
					out.append("operazione negata");
				}				
			}
		}
		// if the action event's source wasn't anyone from these, it was the
		// command line:
		// execute the command from command line
		else
		{
			new respectCommandExecutioner().start();
		}
	}

	// inner class for the execution of commands from command line

	private class respectCommandExecutioner extends Thread
	{

		private String command;

		private respectCommandExecutioner()
		{
			super();
			command = input.getText();
		}

		public void run()
		{
			// simulates the blocking nature of certain operation, by disabling
			// the input field
			input.setEnabled(false);
			adv_shell.setEnabled(false);

			String tuplecentre, operation, tuple, op_result;
			if (command.indexOf('?') > command.indexOf('('))
			{
				out.append("\bcomando non valido" + "\n");
				input.setEnabled(true);
				adv_shell.setEnabled(true);
				return;
			}
			// identify the tuple center which connect to
			if (command.indexOf('?') == -1)
				tuplecentre = "default";
			else
				tuplecentre = command.substring(0, command.indexOf('?'));
			if (fix.isSelected())
			{
				if (local.isSelected())
					tuplecentre = tcname.getText();
				else
					tuplecentre = tcname.getText() + "@" + tcaddr.getText();
			}
			try
			{
				subint.focusOnTC(tuplecentre);
			}
			catch (InvalidTupleCentreIdException e)
			{
				e.printStackTrace();
				out.append("tuple centre non valido");
				input.setEnabled(true);
				adv_shell.setEnabled(true);
				return;
			}
			// identify the command to be executed
			if ((command.indexOf('(') == -1) || (command.charAt(command.length() - 1) != ')'))
			{
				out.append("\bcomando non valido" + "\n");
				input.setEnabled(true);
				adv_shell.setEnabled(true);
				return;
			}
			operation = command.substring(command.indexOf('?') + 1, command.indexOf('('));
			tuple = command.substring(command.indexOf('(') + 1, command.length() - 1);

			op_result = "operazione non riconosciuta";
			// try to execute the specified operation
			try
			{
				if (operation.equals("out"))
					op_result = subint.out(tuple);
				if (operation.equals("in"))
					op_result = subint.in(tuple);
				if (operation.equals("rd"))
					op_result = subint.rd(tuple);
				if (operation.equals("inp"))
					op_result = subint.inp(tuple);
				if (operation.equals("rdp"))
					op_result = subint.rdp(tuple);
				if (operation.equals("out_s"))
					op_result = subint.outS(tuple);
				if (operation.equals("in_s"))
					op_result = subint.inS(tuple);
				if (operation.equals("rd_s"))
					op_result = subint.rdS(tuple);
				if (operation.equals("inp_s"))
					op_result = subint.inpS(tuple);
				if (operation.equals("rdp_s"))
					op_result = subint.rdpS(tuple);
				if (operation.equals("set_spec"))
					op_result = subint.setSpec(tuple);
				if (operation.equals("get_spec"))
					op_result = subint.getSpec();
			}
			catch (InvalidLogicTupleException e)
			{
				e.printStackTrace();
				out.append("\btupla non valida\n");
				input.setEnabled(true);
				adv_shell.setEnabled(true);
				return;
			}
			catch (OperationNotPossibleException e)
			{
				e.printStackTrace();
				out.append("\boperazione non riuscita\n");
				input.setEnabled(true);
				adv_shell.setEnabled(true);
				return;
			}
			catch (InvalidAgentIdException e)
			{
				e.printStackTrace();
				out.append("\baccesso negato\n");
				input.setEnabled(true);
				adv_shell.setEnabled(true);
				return;
			}
			catch (InvalidSpecificationException e)
			{
				e.printStackTrace();
				out.append("\bspecifica non valida\n");
				input.setEnabled(true);
				adv_shell.setEnabled(true);
				return;
			}
			// print the result in output and re-enable the input field
			out.append("\btuple centre " + tuplecentre + ":    " + op_result + "\n");
			input.setEnabled(true);
			adv_shell.setEnabled(true);
		}

	}

	public static void main(String[] args) throws InvalidAgentIdException
	{
		try
		{
			new SimpleShellGUI(new RespectShellSubInterface()).setDefaultCloseOperation(EXIT_ON_CLOSE);

		}
		catch (OperationNotPossibleException e)
		{
			e.printStackTrace();
		}
		catch (InvalidTupleCentreIdException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationNotPossibleException e)
		{
			e.printStackTrace();
		}
	}

}
