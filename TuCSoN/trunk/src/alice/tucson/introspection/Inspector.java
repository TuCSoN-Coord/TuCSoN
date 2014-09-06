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
package alice.tucson.introspection;

import java.io.IOException;

import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.network.exceptions.DialogSendException;

/**
 * 
 * @author Unknown...
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * 
 */
public class Inspector extends Thread implements InspectorContextListener {
    /**
     * 
     */
    protected InspectorContext context;
    /**
     * 
     */
    protected boolean q;

    /**
     * 
     * @param id
     *            the agent identifier this inspector should use
     * @param tid
     *            the identifier of the tuple centre under inspection
     */
    public Inspector(final TucsonAgentId id, final TucsonTupleCentreId tid) {
        super();
        this.context = new InspectorContextStub(id, tid);
        this.context.addInspectorContextListener(this);
        this.q = false;
    }

    /**
     * 
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
        } catch (DialogSendException e){
        	e.printStackTrace();
        }
        this.interrupt();
    }

    @Override
    public synchronized void run() {
        System.out.println("[Inspector]: Started inspecting TuCSoN Node < "
                + this.context.getTid().getName() + "@"
                + this.context.getTid().getNode() + ":"
                + this.context.getTid().getPort() + " >");
        while (!this.q) {
            this.context.acceptVMEvent();
        }
        System.out.println("[Inspector]: Stopped inspecting TuCSoN Node < "
                + this.context.getTid().getName() + "@"
                + this.context.getTid().getNode() + ":"
                + this.context.getTid().getPort() + " >");
    }
}
