/**
 *
 */
package uniform.swarms.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author ste
 *
 */
public class SwarmListener implements ActionListener {

    private final SwarmComponent component;
    private SwarmMonitor monitor;

    /**
     * @param c the parent JComponent
     */
    public SwarmListener(final SwarmComponent c) {
        this.component = c;
        this.monitor = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (this.monitor == null) {
            this.monitor = new SwarmMonitor(this.component);
        }
        this.monitor.performUpdate();
    }

}
