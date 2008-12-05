/**
 *Classe di test per vedere che cosa fa il Context
 */
package alice.tuprolog.test.theory;
 
 import alice.tuprolog.*;
 import java.util.*;
 import javax.swing.*;
 import java.awt.*;
 
 
 public class Tester2{
 
 
 	public static void main(String[] args) throws Exception{
 		
 		
 			 Prolog engine = new Prolog();
 			 maskera2 mask = new maskera2();
 			 mask.showGUI();
 			 
 			 Theory th1 = new Theory("doppio(X,Y):- Y is 2*X.","Doppio");
 			 Theory th2 = new Theory("fatt(0,1).\n"+
 								"fatt(N,Y) :- N>0, N1 is N-1, fatt(N1,Y1), Y is N*Y1.","Fattoriale");
 								
			 Theory thFile = new Theory(new java.io.FileInputStream("bin/alice/tuprolog/test/theory/dafile.pl"),"daFile");
			 
			 
			 engine.addTheory(th1);
			 engine.addTheory(th2);
			 engine.addTheory(thFile);
			 
			 mask.println("Teorie attive prima dell'azione");
			 for (int k=0; k<engine.getTheoryNames().length; k++){
			 	
			 	mask.println(engine.getTheoryNames()[k]);
			 
			 }//fine for
 			 
 			 
 			 engine.solve("modify.");
 			 mask.println("\nTeorie attive dopo l'azione");
			 for (int k=0; k<engine.getTheoryNames().length; k++){
			 	
			 	mask.println(engine.getTheoryNames()[k]);
			 
			 }//fine for
 			 
 			 mask.println("\nRisultato dell'invocazione 'triplo(4,K).'");
 			 SolveInfo info = engine.solve("triplo(4,K).");
 			 mask.println(info.toString());
 			 
 			 
 	}//fine main
 
}//fine classe


//****************************************************************************************



	/**
	 *Classe di supporto per visualizzazione grafica
	 */
	class maskera2 extends JPanel{
		
		//area di testo
		protected JTextArea area;
		
		
		/**
		 *Costruttore
		 */
		public maskera2(){
			super();
				
			area = new JTextArea(20,25);
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