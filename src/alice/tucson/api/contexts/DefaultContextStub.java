/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.tucson.api.contexts;

import alice.logictuple.*;
import alice.tucson.api.util.TupleSerialiser;
import alice.tucson.api.util.TucsonProtocol;
import alice.tucson.api.util.TucsonProtocolTCP;
import alice.tucson.api.contexts.DefaultContextRawProtocol;
import alice.tucson.api.*;

import java.util.*;
import java.io.*;

/**
 *  Implementation of a generic agent coordination context
 */
public class DefaultContextStub implements TucsonContext {

    private TupleCentreId defaultTid = new TupleCentreId();
    private String localNode = "localhost";

    /** user id */
    private AgentId id;

    /** current tuple centres accessed*/
    private HashMap tupleCentreConnections = new HashMap();

    private ContextDescription profile;

    public DefaultContextStub(ContextDescription p) throws InvalidContextDescriptionException {
        profile = p;

        String contextname = p.getProperty("context-name");
        if (contextname == null || (!contextname.equals("default"))) {
            throw new InvalidContextDescriptionException("default context with wrong name");
        }
        String name = p.getProperty("agent-identity");
        if (name == null) {
            throw new InvalidContextDescriptionException("invalid user identity (null)");
        }
        try {
            id = new AgentId(name);
        } catch (Exception ex){
            throw new InvalidContextDescriptionException("invalid user identity "+name);
        }
    }

    public synchronized void out(TupleCentreId tid, LogicTuple t)
            throws OperationNotAllowedException, UnreachableNodeException {
        int nTry = 1;
        do {
            TucsonProtocol info = getDialog(tid);
            DataOutputStream outStream = info.getOutputStream();
            DataInputStream inStream = info.getInputStream();
            try {
                outStream.writeInt(DefaultContextRawProtocol.TYPE_OUT);
                TupleSerialiser.writeTuple(outStream, t);
                outStream.flush();
                //
                boolean allowed = inStream.readBoolean();
                if (!allowed) {
                    throw new OperationNotAllowedException();
                }
                return;
            } catch (IOException ex) {
                resetTupleCentreInfo(tid);
            }
            nTry++;
        } while (nTry < 3);

        throw new UnreachableNodeException();
    }


    public synchronized LogicTuple in(TupleCentreId tid, LogicTuple t)
            throws OperationNotAllowedException, UnreachableNodeException {
        int nTry = 1;
        do {
            TucsonProtocol info = getDialog(tid);
            DataOutputStream outStream = info.getOutputStream();
            DataInputStream inStream = info.getInputStream();
            try {
                outStream.writeInt(DefaultContextRawProtocol.TYPE_IN);
                TupleSerialiser.writeTuple(outStream, t);
                outStream.flush();
                //
                boolean allowed = inStream.readBoolean();
                if (!allowed) {
                    throw new OperationNotAllowedException();
                }
                LogicTuple tuple = TupleSerialiser.readTuple(inStream);
                return unify(t, tuple);
            } catch (Exception ex) {
                resetTupleCentreInfo(tid);
            }
            nTry++;
        } while (nTry < 3);
        //
        throw new UnreachableNodeException();
    }

    public synchronized LogicTuple rd(TupleCentreId tid, LogicTuple t)
            throws OperationNotAllowedException, UnreachableNodeException {
        int nTry = 1;
        do {
            TucsonProtocol info = getDialog(tid);
            DataOutputStream outStream = info.getOutputStream();
            DataInputStream inStream = info.getInputStream();
            try {
                outStream.writeInt(DefaultContextRawProtocol.TYPE_RD);
                TupleSerialiser.writeTuple(outStream, t);
                outStream.flush();
                //
                boolean allowed = inStream.readBoolean();
                if (!allowed) {
                    throw new OperationNotAllowedException();
                }
                LogicTuple tuple = TupleSerialiser.readTuple(inStream);
                return unify(t, tuple);
            } catch (Exception ex) {
                resetTupleCentreInfo(tid);
            }
            nTry++;
        } while (nTry < 3);
        //
        throw new UnreachableNodeException();
    }

    public synchronized LogicTuple inp(TupleCentreId tid, LogicTuple t)
            throws OperationNotAllowedException, UnreachableNodeException {
        int nTry = 1;
        do {
            TucsonProtocol info = getDialog(tid);
            DataOutputStream outStream = info.getOutputStream();
            DataInputStream inStream = info.getInputStream();
            try {
                outStream.writeInt(DefaultContextRawProtocol.TYPE_INP);
                TupleSerialiser.writeTuple(outStream, t);
                outStream.flush();
                //
                boolean allowed = inStream.readBoolean();
                if (!allowed) {
                    throw new OperationNotAllowedException();
                }
                boolean success = inStream.readBoolean();
                if (success) {
                    LogicTuple tuple = TupleSerialiser.readTuple(inStream);
                    return unify(t, tuple);
                } else {
                    return null;
                }
            } catch (Exception ex) {
                resetTupleCentreInfo(tid);
            }
            nTry++;
        } while (nTry < 3);
        //
        throw new UnreachableNodeException();
    }

    public synchronized LogicTuple rdp(TupleCentreId tid, LogicTuple t)
            throws OperationNotAllowedException, UnreachableNodeException {
        int nTry = 1;
        do {
            TucsonProtocol info = getDialog(tid);
            DataOutputStream outStream = info.getOutputStream();
            DataInputStream inStream = info.getInputStream();
            try {
                outStream.writeInt(DefaultContextRawProtocol.TYPE_RDP);
                TupleSerialiser.writeTuple(outStream, t);
                outStream.flush();
                //
                boolean allowed = inStream.readBoolean();
                if (!allowed) {
                    throw new OperationNotAllowedException();
                }
                boolean success = inStream.readBoolean();
                if (success) {
                    LogicTuple tuple = TupleSerialiser.readTuple(inStream);
                    return unify(t, tuple);
                } else {
                    return null;
                }
            } catch (Exception ex) {
                resetTupleCentreInfo(tid);
            }

            nTry++;
        } while (nTry < 3);
        //
        throw new UnreachableNodeException();
    }

    public synchronized void setSpec(TupleCentreId tid, String spec)
            throws OperationNotAllowedException, UnreachableNodeException, InvalidSpecificationException {
        int nTry = 1;
        do {
            TucsonProtocol info = getDialog(tid);
            DataOutputStream outStream = info.getOutputStream();
            DataInputStream inStream = info.getInputStream();
            try {
                outStream.writeInt(DefaultContextRawProtocol.TYPE_SETSPEC);
                outStream.writeUTF(spec);
                outStream.flush();
                //
                boolean allowed = inStream.readBoolean();
                if (!allowed) {
                    throw new OperationNotAllowedException();
                }
                boolean success = inStream.readBoolean();
                if (!success) {
                    throw new InvalidSpecificationException();
                } else {
                    return;
                }
            } catch (IOException ex) {
                resetTupleCentreInfo(tid);
            }
            nTry++;
        } while (nTry < 3);
        //
        throw new UnreachableNodeException();
    }

    public synchronized String getSpec(TupleCentreId tid)
            throws OperationNotAllowedException, UnreachableNodeException {
        int nTry = 1;
        do {
            TucsonProtocol info = getDialog(tid);
            DataOutputStream outStream = info.getOutputStream();
            DataInputStream inStream = info.getInputStream();
            try {
                outStream.writeInt(DefaultContextRawProtocol.TYPE_GETSPEC);
                outStream.flush();
                //
                boolean allowed = inStream.readBoolean();
                if (!allowed) {
                    throw new OperationNotAllowedException();
                }
                return inStream.readUTF();
            } catch (IOException ex) {
                resetTupleCentreInfo(tid);
            }
            nTry++;
        } while (nTry < 3);
        //
        throw new UnreachableNodeException();
    }

    public void exit()
            throws OperationNotAllowedException {
        Iterator it = tupleCentreConnections.values().iterator();
        while (it.hasNext()) {
            try {
                TucsonProtocol info = (TucsonProtocol) it.next();
                DataOutputStream outStream = info.getOutputStream();
                outStream.writeInt(DefaultContextRawProtocol.TYPE_EXIT);
                outStream.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * creates or retrieve the connection to a tuple centre vm.
     * if the tuple centre is new, a new connection to target
     * daemon providing the tuple centre is created,
     * otherwise returns the already established connection
     */
    private TucsonProtocol getDialog(TupleCentreId tid)
            throws OperationNotAllowedException, UnreachableNodeException {
        String stid = tid.toString();
        Object tc = tupleCentreConnections.get(stid);
        if (tc != null) {
            return (TucsonProtocol) tc;
        } else {

            profile.setProperty("node", tid.getNode());
            profile.setProperty("tuple-centre", tid.getName());

            try {
                String node= alice.util.Tools.removeApices(tid.getNode());

                TucsonProtocol dialog = new TucsonProtocolTCP(node, 20504);
                dialog.sendEnterRequest(profile);
                dialog.receiveEnterRequestAnswer();
                if (dialog.isEnterRequestAccepted()) {
                    tupleCentreConnections.put(tid.toString(), dialog);
                    return dialog;
                }
            } catch (Exception ex){
                throw new UnreachableNodeException();
            }
        }
        throw new OperationNotAllowedException();
    }

    /**
     * Helper service, which unifies a tuple template and a tuple
     *
     * @param template the tuple template
     * @param tuple the tuple to be unified with the template
     * @return the unified tuple
     */
    private LogicTuple unify(alice.tuplecentre.TupleTemplate template, alice.tuplecentre.Tuple tuple) {
        boolean res = template.propagate(tuple);
        if (res) {
            return (LogicTuple) template;
        } else {
            return null;
        }
    }

    /**
     * reset information about an established connection
     */
    private boolean resetTupleCentreInfo(TupleCentreId tid) {
        String stid = tid.toString();
        Object tc = tupleCentreConnections.remove(stid);
        if (tc != null) {
            TucsonProtocol info = (TucsonProtocol) tc;
            try {
                info.end();
            } catch (Exception ex) {
            }
            return true;
        }
        return false;
    }

}





