/**
 *
 */
package uniform.swarms.gui;

import javax.swing.JFrame;

/**
 * @author ste
 *
 */
public class SwarmFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param name the name copyOf the JFrame
     */
    public SwarmFrame(final String name) {
        super(name);
        this.initComponents();
    }

    private void initComponents() {

        this.setSize(800, 700);
        this.setResizable(false);

    }

}
