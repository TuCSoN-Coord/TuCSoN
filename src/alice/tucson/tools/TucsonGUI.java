package alice.tucson.tools;

import inspector.InspectorGUI;
import inspector.RespectInspectorSubInterface;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


import alice.respect.api.InvalidTupleCentreIdException;
import alice.tucson.api.exception.TucsonException;
import alice.tucson.service.Node;
import alice.tucson.service.TucsonNodeLanguageManager;
import alice.tucson.service.TucsonTCUsers;


@SuppressWarnings("serial")
public class TucsonGUI extends JFrame implements ActionListener{
	private static final int DEFAULT_TCP_PORT = 20504;
	private Node node;
	private JPanel p1, p2, p3, pTop, jPanelTc, jPanelAct;
	private Container c;
	private String lastTCPainted;
	private JButton insp, cmd1a, cmd1b, cmd2a, cmd2b, cmd3;
	private JButton[] butList;
	private String selectTc;
	
	
	public TucsonGUI(){
		super("TuCSoN 1.9");
		//setVisible(true);
		setSize(500,500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c=getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
		
		pTop = new JPanel();
		pTop.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
		pTop.setLayout(new BoxLayout(pTop, BoxLayout.LINE_AXIS));
		c.add(pTop);
		JLabel jLabel3 = new JLabel();
		//jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alice/tucson/images/logo.gif")));
        jLabel3.setPreferredSize(new java.awt.Dimension(115, 130));
        pTop.add(jLabel3);
        pTop.add(Box.createHorizontalStrut(130));
		insp = new JButton("Run Local Inspector");
		insp.addActionListener(this);
		pTop.add(insp);
		
		
		p1=new JPanel();
		p1.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
		p1.setLayout(new BoxLayout(p1, BoxLayout.LINE_AXIS));
		c.add(p1);
		
		
		p2=new JPanel();
		p2.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
		p2.setLayout(new BoxLayout(p2, BoxLayout.PAGE_AXIS));
		p1.add(p2);
		Box boxTcs = new Box(BoxLayout.X_AXIS);
		
		JLabel tcs = new JLabel("Available Tuple Centres: ");
		jPanelTc = new JPanel();
		jPanelTc.setLayout(new BoxLayout(jPanelTc, BoxLayout.PAGE_AXIS));
		jPanelTc.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "tuple centres", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		JPanel pan = new JPanel();
		jPanelTc.add(pan);
		boxTcs.add(tcs);
		p2.add(boxTcs);
		p2.add(jPanelTc);
		
		
		
		p3=new JPanel();
		p3.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
		p3.setLayout(new BoxLayout(p3, BoxLayout.PAGE_AXIS));
		p1.add(p3);
		Box boxTc = new Box(BoxLayout.X_AXIS);
		JLabel selTc = new JLabel("Selected Tuple Centre: ");
		boxTc.add(selTc);
		p3.add(boxTc);
		jPanelAct = new JPanel();
		jPanelAct.setLayout(new BoxLayout(jPanelAct, BoxLayout.PAGE_AXIS));
		jPanelAct.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "action", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		JPanel pCmd1 = new JPanel();
		JPanel pCmd2 = new JPanel();
		JPanel pCmd3 = new JPanel();
		pCmd1.add(new JLabel("Persistence  "));
		cmd1a = new JButton("Enable");
		cmd1a.addActionListener(this);
		pCmd1.add(cmd1a);
		cmd1b = new JButton("Disable");
		cmd1b.addActionListener(this);
		pCmd1.add(cmd1b);
		pCmd2.add(new JLabel("Observability  "));
		cmd2a = new JButton("Activate");
		cmd2a.addActionListener(this);
		pCmd2.add(cmd2a);
		cmd2b = new JButton("Deactivate");
		cmd2b.addActionListener(this);
		pCmd2.add(cmd2b);
		cmd3 = new JButton("Destroy");
		cmd3.addActionListener(this);
		pCmd3.add(cmd3);
		jPanelAct.add(pCmd1);
		jPanelAct.add(pCmd2);
		jPanelAct.add(pCmd3);
		p3.add(jPanelAct);
		p1.add(p3);
		pack();
		setVisible(true);
		
		String lang = "";
		Object sync = new Object();
		String port = "";
		LanguageDialog langDialog=new LanguageDialog(sync, lang, port); 
		langDialog.setVisible(true);
		synchronized (sync) {
			try {
				sync.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		TucsonNodeLanguageManager.useLanguage(langDialog.getLanguage());
		setTitle("TuCSoN 1.9           Language "+TucsonNodeLanguageManager.getCurLanguage().getName());
		int portNum = Integer.parseInt(langDialog.getPort());
		try {
			if (portNum<0 || portNum>64000){
				portNum = DEFAULT_TCP_PORT;
			}
			node = new Node(null,portNum,null);
		} catch (TucsonException e) {
			e.printStackTrace();
		}
		new NodeThread(node).start();
		
		new TucsonGUIUpdater(this).start();
		
		
		
	}
	
	public void repaintCores(){
		Collection<TucsonTCUsers> cores = node.getCores().values();
		Iterator<TucsonTCUsers> it = cores.iterator();
		butList = new JButton[cores.size()];
		p2.remove(jPanelTc);
		
		jPanelTc = new JPanel();
		jPanelTc.setLayout(new BoxLayout(jPanelTc, BoxLayout.PAGE_AXIS));
		jPanelTc.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "tuple centres", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
		JPanel intPane = new JPanel();
		intPane.setLayout(new BoxLayout(intPane, BoxLayout.PAGE_AXIS));
		JScrollPane scroll = new JScrollPane(intPane);	
		//scroll.setMaximumSize(new Dimension(100,200));
		for(int i=0;i<cores.size();i++){
			JPanel butPan = new JPanel();
			butPan.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
			butList[i] = new JButton(it.next().getTucsonTupleCentreId().getName());
			butList[i].setMaximumSize(new Dimension(120,20));
			butPan.add(butList[i]);
			intPane.add(butPan);
			butList[i].addActionListener(this);
		}
		jPanelTc.add(scroll);
		p2.add(jPanelTc);				
		//setVisible(true);
		pack();
		
	}
	
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == insp){
			new InspectorThread().start();
		}else if (ev.getSource() == cmd1a){
			if (selectTc == null){
				new ExceptionDialog("Select a tuple centre").setVisible(true);
			}else {
				node.enablePersistence(selectTc);
			}
		}else if (ev.getSource() == cmd1b){
			if (selectTc == null){
				new ExceptionDialog("Select a tuple centre").setVisible(true);
			}else {
				node.disablePersistence(selectTc);
			}
		}else if (ev.getSource() == cmd2a){
			if (selectTc == null){
				new ExceptionDialog("Select a tuple centre").setVisible(true);
			}else {
				node.activateObservability();
			}
		}else if (ev.getSource() == cmd2b){
			if (selectTc == null){
				new ExceptionDialog("Select a tuple centre").setVisible(true);
			}else {
				node.deactivateObservability();
			}
		}else if (ev.getSource() == cmd3){
			if (selectTc == null){
				new ExceptionDialog("Select a tuple centre").setVisible(true);
			}else {
				node.destroyCore(selectTc);
				cmd1a.setEnabled(false);
				cmd1b.setEnabled(false);
				cmd2a.setEnabled(false);
				cmd2b.setEnabled(false);
				cmd3.setEnabled(false);
				repaintCores();
			}
		}else {
			selectTc = ((JButton)ev.getSource()).getLabel();
			p1.remove(p3);
			p3=new JPanel();
			p3.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
			p3.setLayout(new BoxLayout(p3, BoxLayout.PAGE_AXIS));
			p1.add(p3);
			Box boxTc = new Box(BoxLayout.X_AXIS);
			JLabel selTc = new JLabel("Selected Tuple Centre: "+selectTc);
			boxTc.add(selTc);
			p3.add(boxTc);
			jPanelAct = new JPanel();
			jPanelAct.setLayout(new BoxLayout(jPanelAct, BoxLayout.PAGE_AXIS));
			jPanelAct.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "action", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11)));
			JPanel pCmd1 = new JPanel();
			JPanel pCmd2 = new JPanel();
			JPanel pCmd3 = new JPanel();
			pCmd1.add(new JLabel("Persistence  "));
			cmd1a = new JButton("Enable");
			cmd1a.addActionListener(this);
			pCmd1.add(cmd1a);
			cmd1b = new JButton("Disable");
			cmd1b.addActionListener(this);
			pCmd1.add(cmd1b);
			pCmd2.add(new JLabel("Observability  "));
			cmd2a = new JButton("Activate");
			cmd2a.addActionListener(this);
			pCmd2.add(cmd2a);
			cmd2b = new JButton("Deactivate");
			cmd2b.addActionListener(this);
			pCmd2.add(cmd2b);
			cmd3 = new JButton("Destroy");
			cmd3.addActionListener(this);
			pCmd3.add(cmd3);
			jPanelAct.add(pCmd1);
			jPanelAct.add(pCmd2);
			jPanelAct.add(pCmd3);
			p3.add(jPanelAct);
			p1.add(p3);
			setVisible(true);
		}
	}
		
		
		
	
	
	public static void main(String[] args){
		new TucsonGUI();
	}
	
	public class TucsonGUIUpdater extends Thread{
		private TucsonGUI gui;
		public TucsonGUIUpdater(TucsonGUI gui){
			this.gui = gui;
		}
		
		public void run(){
			while(true){
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}			
				gui.repaintCores();
				if (lastTCPainted!=null){
					//gui.paintTC(lastTCPainted);
				}
			}
		}
	}
	public class InspectorThread extends Thread{
		
		public void run(){
			try {
				new InspectorGUI(new RespectInspectorSubInterface()).setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			} catch (InvalidTupleCentreIdException e) {
				e.printStackTrace();
			}
		}
	}
}


