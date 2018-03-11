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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import alice.tuple.Tuple;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.exceptions.OperationNotAllowedException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.network.TucsonProtocol;
import alice.tuplecentre.tucson.network.TucsonProtocolTCP;
import alice.tuplecentre.tucson.network.exceptions.DialogException;
import alice.tuplecentre.tucson.network.exceptions.DialogSendException;
import alice.tuplecentre.tucson.service.ACCDescription;

/**
 *
 * @author Unknown...
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public class InspectorContextStub implements InspectorContext {

    /** listeners registrated for virtual machine output events */
    private final List<InspectorContextListener> contextListeners = new ArrayList<InspectorContextListener>();
    private TucsonProtocol dialog;
    private boolean exitFlag;
    /** user id */
    private final TucsonAgentId id;
    private final ACCDescription profile;
    /** current observation protocol */
    private InspectorProtocol protocol;
    /** id of the tuple centre to be observed */
    protected TucsonTupleCentreId tid;

    /**
     * @param i
     *            the agent identifier to be used by this inspector
     * @param tc
     *            the identifier of the tuple centre to inspect
     * @param forGui
     *            whether the inspector is the Gui version (see Dradi MoK
     *            project 2014/2015)
     */
    public InspectorContextStub(final TucsonAgentId i,
                                final TucsonTupleCentreId tc, final boolean forGui) {
        this.profile = new ACCDescription();
        this.profile.setProperty("agent-identity", i.toString());
        String agentRole = forGui ? "$inspector4gui" : "$inspector";
        this.profile.setProperty("agent-role", agentRole);
        this.profile.setProperty("tuple-centre", tc.getLocalName());
        this.profile.setProperty("agent-uuid", UUID.randomUUID().toString());
        this.id = i;
        this.tid = tc;
        try {
            this.getTupleCentreInfo(tc);
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final OperationNotAllowedException e) {
            e.printStackTrace();
        }
        this.exitFlag = false;
    }

    public InspectorContextStub(final TucsonAgentId i,
            final TucsonTupleCentreId tc) {
        this(i, tc, false);
    }

    @Override
    public void acceptVMEvent() throws DialogException {
        try {
            final InspectorContextEvent msg = this.dialog
                    .receiveInspectorEvent();
            for (int i = 0; i < this.contextListeners.size(); i++) {
                this.contextListeners.get(i).onContextEvent(msg);
            }
        } catch (final DialogException e) {
            if (!this.exitFlag) {
                throw new DialogException("node-disconnected");
            }
        }
    }

    @Override
    public void addInspectorContextListener(final InspectorContextListener l) {
        this.contextListeners.add(l);
    }

    @Override
    public void exit() throws DialogSendException {
        this.exitFlag = true;
        this.dialog.sendNodeMsg(new ShutdownMsg(this.id));
    }

    @Override
    public void getSnapshot(final byte snapshotMsg) throws DialogSendException {
        this.dialog.sendNodeMsg(new GetSnapshotMsg(this.id, snapshotMsg));
    }

    @Override
    public TucsonTupleCentreId getTid() {
        return this.tid;
    }

    @Override
    public void isStepMode() {
        try {
            this.dialog.sendNodeMsg(new IsActiveStepModeMsg(this.id));
        } catch (final DialogException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nextStep() throws DialogSendException {
        this.dialog.sendNodeMsg(new NextStepMsg(this.id));
    }

    @Override
    public void removeInspectorContextListener(final InspectorContextListener l) {
        this.contextListeners.remove(l);
    }

    @Override
    public void reset() throws DialogSendException {
        this.dialog.sendNodeMsg(new ResetMsg(this.id));
    }

    @Override
    public void setEventSet(final List<Tuple> wset) throws DialogSendException {
        this.dialog.sendNodeMsg(new SetEventSetMsg(this.id, wset));
    }

    @Override
    public void setProtocol(final InspectorProtocol p)
            throws DialogSendException {
        final InspectorProtocol newp = new InspectorProtocolDefault();
        newp.setTsetObservType(p.getTsetObservType());
        newp.setTsetFilter(p.getTsetFilter());
        newp.setWsetFilter(p.getWsetFilter());
        newp.setTracing(p.isTracing());
        newp.setPendingQueryObservType(p.getPendingQueryObservType());
        newp.setReactionsObservType(p.getReactionsObservType());
        newp.setStepModeObservType(p.getStepModeObservType());
        this.dialog.sendNodeMsg(new SetProtocolMsg(this.id, newp));
        this.protocol = p;
    }

    @Override
    public void setTupleSet(final List<Tuple> tset) throws DialogSendException {
        this.dialog.sendNodeMsg(new SetTupleSetMsg(this.id, tset));
    }

    @Override
    public void vmStepMode() throws DialogSendException {
        this.dialog.sendNodeMsg(new StepModeMsg(this.id));
    }

    /**
     * if request to a new tuple centre -> create new connection to target
     * daemon providing the tuple centre otherwise return the already
     * established connection
     */
    private TucsonProtocol getTupleCentreInfo(
            final TucsonTupleCentreId tc) throws UnreachableNodeException,
            OperationNotAllowedException {
        try {
            final String node = alice.util.Tools.removeApices(tc.getNode());
            final int port = tc.getPort();
            this.dialog = new TucsonProtocolTCP(node, port);
            this.dialog.sendEnterRequest(this.profile);
            this.dialog.receiveEnterRequestAnswer();
            if (this.dialog.isEnterRequestAccepted()) {
                this.protocol = new InspectorProtocolDefault();
                final NewInspectorMsg msg = new NewInspectorMsg(this.id,
                        tc.toString(), this.protocol);
                this.dialog.sendInspectorMsg(msg);
                return this.dialog;
            }
        } catch (final DialogException e) {
            e.printStackTrace();
        }
        throw new OperationNotAllowedException();
    }

    /**
     * resolve information about a tuple centre
     *
     * @param titcd
     *            the identifier of the tuple centre to be resolved
     */
    protected void resolveTupleCentreInfo(final TucsonTupleCentreId titcd) {
        try {
            this.getTupleCentreInfo(titcd);
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final OperationNotAllowedException e) {
            e.printStackTrace();
        }
    }
}
