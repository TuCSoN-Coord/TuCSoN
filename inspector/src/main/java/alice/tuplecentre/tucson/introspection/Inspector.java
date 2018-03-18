/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.tucson.introspection;

import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.introspection.tools.InspectorGUI;
import alice.tuplecentre.tucson.network.exceptions.DialogException;
import alice.tuplecentre.tucson.network.exceptions.DialogSendException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * @author Unknown...
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class Inspector extends Thread implements InspectorContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    /**
     *
     */
    protected final InspectorContext context;
    /**
     *
     */
    protected boolean q;

    /**
     * @param id     the agent identifier this inspector should use
     * @param tid    the identifier of the tuple centre under inspection
     * @param forGui whether the inspector is the Gui version (see Dradi MoK
     *               project 2014/2015)
     */
    public Inspector(final TucsonAgentId id, final TucsonTupleCentreId tid,
                     boolean forGui) {
        super();
        this.context = new InspectorContextStub(id, tid, forGui);
        this.context.addInspectorContextListener(this);
        this.q = false;
    }

    public Inspector(final TucsonAgentId id, final TucsonTupleCentreId tid) {
        this(id, tid, false);
    }

    /**
     * @return the inspection context used by this inspector
     */
    public InspectorContext getContext() {
        return this.context;
    }

    @Override
    public void onContextEvent(final InspectorContextEvent ev) {
        /*
         * FIXME What to do here?
         */
    }

    /**
     *
     */
    public void quit() {
        this.q = true;
        try {
            this.context.exit();
        } catch (final DialogSendException e) {
            LOGGER.error(e.getMessage());
        }
        this.interrupt();
    }

    @Override
    public synchronized void run() {
        LOGGER.info("[Inspector]: Started inspecting TuCSoN Node < "
                + this.context.getTid().getLocalName() + "@"
                + this.context.getTid().getNode() + ":"
                + this.context.getTid().getPort() + " >");
        while (!this.q) {
            try {
                this.context.acceptVMEvent();
            } catch (final DialogException e) {
                String errorMessage = "TuCSoN node "
                        + this.context.getTid().getLocalName() + "@"
                        + this.context.getTid().getNode() + ":"
                        + this.context.getTid().getPort()
                        + " disconnected unexpectedly :/";
                LOGGER.error(errorMessage);
                this.q = true;
                InspectorGUI.showErrorMessageDialog("Connection timed out", errorMessage);
            }
        }
        LOGGER.info("[Inspector]: Stopped inspecting TuCSoN Node < "
                + this.context.getTid().getLocalName() + "@"
                + this.context.getTid().getNode() + ":"
                + this.context.getTid().getPort() + " >");
    }
}
