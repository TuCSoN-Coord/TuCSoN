/**
 *Classe di test per vedere che cosa fa il Context
 */
 
 package alice.tuprolog.test.theory;
 
 import alice.tuprolog.*;
 import java.util.*;
 import javax.swing.*;
 import java.awt.*;
 
 
 public class Tester{
 
 
 	public static void main(String[] args) throws Exception{
 	
 	
 		Prolog engine = new Prolog();
 		maskera mask = new maskera();
 		
 		mask.showGUI();
 			
 		 		
 		Theory th = new Theory("fatt(0,1).\n"+
 								"fatt(N,Y) :- N>0, N1 is N-1, fatt(N1,Y1), Y is N*Y1.","pippo");
 	 	
 	 	Theory th1 = new Theory(new java.io.FileInputStream("bin/alice/tuprolog/test/theory/fatty.pl"), "pappo");
 	 	Theory th2 = new Theory(new java.io.FileInputStream("bin/alice/tuprolog/test/theory/prova.pl"), "test");
 	 	Theory th3 = new Theory("uni(X):- X is 2.","pallino");
 		
 		
 		mask.println("Situazione iniziale teorie: "+engine.getTheory().toString()); 

 		
 		engine.addTheory(th);
 		engine.addTheory(th1); 
 		engine.addTheory(th2); 		

		engine.addTheory(th3);
		 	
 		
 		mask.println("Situazione attuale TOTALE teorie: "+engine.getTheory().toString()); 

 		mask.println("Nomi delle teorie caricate attualmente: ");
 		String[] tmp = engine.getTheoryNames();
 		 		
 		for(int k=0; k<tmp.length; k++){
 			
 			
 			mask.println("Teoria n "+k+": "+tmp[k]);
 		}
 		
 		
 		
 		Theory theo = engine.getTheory("pippo");
 		mask.println("Teoria trovate dal nome "+theo.getName());

 		mask.println("\n\nTentativo di estrazione teoria singola.");
 		
	
 		 		
 		SolveInfo info = engine.solve("fatt(4,K).");
 		if (info.isSuccess()){
 			mask.println("********* Soluzione: "+info.toString()+"\n");
 		} else 
			mask.println("mi dispiace ma non va bene: "+info.toString());
	
 		

 		
 		
 		
 		

 		 			
// 		mask.println("\nTeoria estratta da vettore: "+ prova.getName() + engine.getTheoryByName("pippo").toString()); 
 		
 		
 		
 		mask.println("Nomi delle teorie caricate attualmente: ");
 		tmp = engine.getTheoryNames();
 		 		
 		for(int k=0; k<tmp.length; k++){
 			
 			
 			mask.println("Teoria n "+k+": "+tmp[k]);
 		}
 		
 		
 	//	engine.removeTheory("pippo");
 				
 		
 		
 		mask.println("Situazione attuale teorie: "+engine.getTheory().toString());
 		
 		mask.println("Nomi delle teorie caricate attualmente: ");
 		tmp = engine.getTheoryNames();
 		 		
 		for(int k=0; k<tmp.length; k++){
 			
 			
 			mask.println("Teoria n "+k+": "+tmp[k]);
 		}
 		
 		
 		
 		
 		info = engine.solve("prova.");
 		if (info.isSuccess()){
 			mask.println("********* Soluzione: "+info.toString()+"\n");
 		} else 
			mask.println("mi dispiace ma non va bene: "+info.toString());
 
 		 		
 		
 		///DA QUI SOTTO
 		
 		//engine.removeTheory("default");
 		
 		//engine.addTheory(th3);
 		
  		info = engine.solve("provarem.");
 		if (info.isSuccess()){
 			mask.println("********* Soluzione: "+info.toString()+"\n");
 		} else 
			mask.println("mi dispiace ma non va bene: "+info.toString()); 		
 		
 		
 		
 		
 		mask.println("\n\nSituazione attuale teorie: "+engine.getTheory().toString());
 		
 		mask.println("Nomi delle teorie caricate attualmente: ");
 		tmp = engine.getTheoryNames();
 		 		
 		for(int k=0; k<tmp.length; k++){
 			
 			
 			mask.println("Teoria n "+k+": "+tmp[k]);
 		}
 		
 		engine.removeTheory("pallino");
 		mask.println("\nRimozione corretta....");
 		
 		mask.println("Nomi delle teorie caricate attualmente: ");
 		tmp = engine.getTheoryNames();
 		 		
 		for(int k=0; k<tmp.length; k++){
 			
 			
 			mask.println("Teoria n "+k+": "+tmp[k]);
 		}
 		
 		
// 		engine.addTheory(th2);
// 		mask.println("\nSituazione database dopo riINSERIMENTO: "+engine.getTheory().toString()); 
 		
		
//		mask.println("\nProva creazione nome interno: "+engine.getTheName(th)); 
// 		engine.setTheory(th2);
// 		mask.println("\nSituazione database dopo settaggio: "+engine.getTheory().toString()); 
 		
 		//setTheory non aggiorna il vettore delle teorie caricate (per ora)
// 		mask.println("\nTeoria estratta da vettore: "+ prova.getName() + engine.getTheoryByName("dafile2").toString()); 
 		
	
	 }//fine main
 
}//fine classe


//****************************************************************************************



	/**
	 *Classe di supporto per visualizzazione grafica
	 */
	class maskera extends JPanel{
		
		//area di testo
		protected JTextArea area;
		
		
		/**
		 *Costruttore
		 */
		public maskera(){
			super();
				
			area = new JTextArea(20,50);
			area.setEditable(false);
			JScrollPane jsp = new JScrollPane(area);
			
			add(jsp);
					
		}//fine costruttore
		
		/**
		 *metodo per scrivere MSG sull'area testo
		 */
		public void println(String msg){
			area.append(msg+"\n");
		}
		//fine println
		
		
		/**
		 *metodo per mostrare il pannello grafico
		 */
		public void showGUI(){
			
			JFrame frame =new JFrame("Default output");
			frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

			Container c= frame.getContentPane();
			//maskera mask = new maskera();
			
			
			c.add(this);
						
					
			frame.pack();
			frame.setVisible(true);
			
			
		}//fine showGUI	
			
	
	}//fine classe maskera