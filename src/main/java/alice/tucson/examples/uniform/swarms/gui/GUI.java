/**
 *
 */
package alice.tucson.examples.uniform.swarms.gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author ste
 *
 */
public final class GUI {

    /**
     * 
     */
    public static void init() {
        /*
         * Schedule a job for the event-dispatching thread: creating and showing
         * this application's GUI.
         */
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI.createAndShowGUI();
            }
        });
        // javax.swing.SwingUtilities.invokeLater(() -> GUI.createAndShowGUI());
    }

    private static void createAndShowGUI() {
        // Create and set up the window.
        final SwarmFrame frame = new SwarmFrame("Uniform Reading Ants");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                frame.getContentPane().add(new SwarmComponent(),
                        BorderLayout.CENTER);
                frame.revalidate();
                frame.repaint();
                frame.getContentPane().revalidate();
                frame.getContentPane().repaint();
            }
        });
    }

}
