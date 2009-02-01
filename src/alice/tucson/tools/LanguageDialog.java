package alice.tucson.tools;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import alice.tucson.service.LanguageInfo;
import alice.tucson.service.TucsonNodeLanguageManager;
import alice.tucson.service.TucsonTCContainer;

public class LanguageDialog extends JDialog implements ActionListener, ListSelectionListener{
	
	private JButton bLog1;
	private Object sync;
	private String lang;
	private JList list;
	private String port;
	private JTextField portField;
	
	public LanguageDialog(Object sync, String lang, String port){
		super();
		this.sync = sync;
		this.lang = lang;
		this.port = port;
		setTitle("Language chooser");
		setSize(400,200);
		setLocationRelativeTo(null);
		Container c=getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
		JPanel p1=new JPanel();
		p1.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		p1.setLayout(new BoxLayout(p1, BoxLayout.LINE_AXIS));
		
		JPanel p2=new JPanel();
		p2.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
		p2.setLayout(new BoxLayout(p2, BoxLayout.LINE_AXIS));
		
		JPanel p3=new JPanel();
		p2.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
		p2.setLayout(new BoxLayout(p2, BoxLayout.LINE_AXIS));
		
		Collection<LanguageInfo> ar1 = (Collection<LanguageInfo>) TucsonNodeLanguageManager.getAvailableLanguages().values();
		String[] ar2= new String[ar1.size()];
		Iterator<LanguageInfo> it = ar1.iterator();
		for (int i=0; i< ar1.size(); i++){
			ar2[i] = it.next().getName();
		}		
		
		list=new JList(ar2);
		JScrollPane pScroll=new JScrollPane(list);
		bLog1=new JButton("Run");
		JLabel lab = new JLabel("Choose the language and press Run for launch the node");
		bLog1.setEnabled(false);
		JLabel lab2 = new JLabel("Port number: ");
		portField=new JTextField(15);
		portField.setText("20504");
		p1.add(pScroll);
		p1.add(Box.createHorizontalStrut(20));
		p2.add(lab);
		//p1.add(Box.createVerticalStrut(25));
		p1.add(bLog1);
		//p1.add(Box.createVerticalStrut(100));
		p3.add(lab2);
		p3.add(portField);		
		bLog1.addActionListener(this);
		list.addListSelectionListener(this);
		
		c.add(p2);
		c.add(p1);
		c.add(p3);
		
		
	}
	
	public String getPort(){
		return port;
	}
	
	public String getLanguage(){
		return lang;
	}

	
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource()==bLog1){
			this.lang = (String)list.getSelectedValue();
			this.port = portField.getText();
			synchronized (sync) {
				sync.notify();
			}
			dispose();
		}
		
	}


	public void valueChanged(ListSelectionEvent arg0) {
		bLog1.setEnabled(true);			
	}
}
