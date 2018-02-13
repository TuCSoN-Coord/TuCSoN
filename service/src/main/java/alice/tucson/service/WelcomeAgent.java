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
package alice.tucson.service;

import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.network.AbstractTucsonProtocol;
import alice.tucson.network.TPFactory;
import alice.tucson.network.exceptions.DialogAcceptException;
import alice.tucson.network.exceptions.DialogCloseException;
import alice.tucson.network.exceptions.DialogInitializationException;
import alice.tucson.network.exceptions.DialogReceiveException;
import alice.tucson.network.exceptions.DialogSendException;
import alice.tucson.network.exceptions.InvalidProtocolTypeException;

/**
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Saverio Cicora
 *
 */
public class WelcomeAgent extends Thread {

    private static void log(final String st) {
        System.out.println("..[WelcomeAgent]: " + st);
    }

    private final ACCProvider contextManager;
    private AbstractTucsonProtocol mainDialog;
    private final TucsonNodeService node;
    private boolean shut;

    /**
     *
     * @param n
     *            the TuCSoN node this internal agent refers to
     * @param cm
     *            the ACC provider this internal agent should delegate ACC
     *            requests to
     */
    public WelcomeAgent(final TucsonNodeService n, final ACCProvider cm) {
        super();
        this.contextManager = cm;
        this.node = n;
        this.shut = false;
        this.start();
    }

    /**
     *
     */
    @Override
    public void run() {
        try {
            this.mainDialog = TPFactory.getDialogNodeSide(
                    TPFactory.DIALOG_TYPE_TCP, this.node.getTCPPort());
        } catch (final DialogInitializationException e) {
            // TODO CICORA: what is the correct behavior when a port is already
            // used?
            e.printStackTrace();
            this.shut = true;
        } catch (final InvalidProtocolTypeException e) {
            // Cannot happen
            e.printStackTrace();
        }
        AbstractTucsonProtocol dialog = null;
        try {
            while (!this.isShutdown()) {
                WelcomeAgent.log("Listening to incoming connections...");
                WelcomeAgent
                        .log("--------------------------------------------------------------------------------");
                try {
                    dialog = this.mainDialog.acceptNewDialog();
                } catch (final DialogAcceptException e) {
                    // TODO CICORA: what is the correct behavior?
                    if (this.isShutdown()) {
                        WelcomeAgent
                                .log("Shutdown request received, shutting down...");
                    } else {
                        e.printStackTrace();
                    }
                    this.shut = true;
                    break;
                }
                dialog.receiveFirstRequest();
                if (dialog.isEnterRequest()) {
                    dialog.receiveEnterRequest();
                    final ACCDescription desc = dialog.getContextDescription();
                    WelcomeAgent
                            .log("Delegating ACCProvider received enter request...");
                    this.contextManager.processContextRequest(desc, dialog);
                } else if (dialog.isNodeActiveQuery()) {
                    dialog.sendNodeActiveReply();
                }
            }
        } catch (final DialogReceiveException e) {
            e.printStackTrace();
        } catch (final DialogSendException e) {
            e.printStackTrace();
        } catch (TucsonInvalidAgentIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TucsonInvalidTupleCentreIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.node.removeNodeAgent(this);
    }

    /**
     *
     */
    public synchronized void shutdown() {
        /*
         * TODO CICORA: it's not the WelcomAgent's thread to call this, but it's
         * an acceptable compromise to avoid adding an additional thread
         */
        this.shut = true;
        try {
            if (this.mainDialog != null) {
                this.mainDialog.end();
            }
        } catch (final DialogCloseException e) {
            // TODO CICORA: what is the correct behavior?
            e.printStackTrace();
        }
    }

    private synchronized boolean isShutdown() {
        return this.shut;
    }
}
