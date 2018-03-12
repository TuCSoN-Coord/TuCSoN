package alice.tuplecentre.tucson.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import alice.tuple.Tuple;
import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTupleOpManager;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.TupleArguments;
import alice.tuple.logic.exceptions.InvalidVarNameException;
import alice.tuplecentre.api.OperationIdentifier;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonOperationCompletionListener;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.rbac.Permission;
import alice.tuplecentre.tucson.rbac.Role;
import alice.tuprolog.Parser;

/**
 * Class implementing the RBAC ACC.
 *
 * @author Emanuele Buccelli
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public class RBACACCProxyAgentSide extends ACCProxyAgentSide {

    private List<String> permissions;
    private Role role;

    /**
     * Builds an RBAC ACC given the associated agent Identifier or name, its initial
     * role and the assigned UUID.
     *
     * @param aid       the associated agent Identifier or name (String)
     * @param r         the initial role to play
     * @param agentUUID the UUID assigned
     * @throws TucsonInvalidAgentIdException if the given agent Identifier is NOT valid
     */
    public RBACACCProxyAgentSide(final Object aid, final Role r,
                                 final UUID agentUUID) throws TucsonInvalidAgentIdException {
        this(aid, "localhost", TucsonInfo.getDefaultPortNumber(), r, agentUUID);
    }

    /**
     * Builds an RBAC ACC given the associated agent Identifier or name, the IP address
     * of the TuCSoN node it is willing to interact with, its TCP port also, as
     * well as the associated agent initial role and the assigned UUID.
     *
     * @param aid       the associated agent Identifier or name (String)
     * @param n         the IP address
     * @param p         the TCP port number
     * @param r         the initial role to play
     * @param agentUUID the UUID assigned
     * @throws TucsonInvalidAgentIdException if the given agent Identifier is NOT valid
     */
    public RBACACCProxyAgentSide(final Object aid, final String n, final int p,
                                 final Role r, final UUID agentUUID)
            throws TucsonInvalidAgentIdException {
        super(aid, n, p, agentUUID);
        this.setRole(r);
    }

    @Override
    public TucsonOperation get(final TupleCentreIdentifier tid, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("get");
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.GET, tid, null, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation get(final TupleCentreIdentifier tid,
                               final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("get");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.GET, tid, null, l, this.getPosition());
    }

    @Override
    public List<TucsonOpCompletionEvent> getCompletionEventsList() {
        return this.executor.events;
    }

    @Override
    public Map<OperationIdentifier, TucsonOperation> getPendingOperationsMap() {
        return this.executor.operations;
    }

    @Override
    public TucsonOperation getS(final TupleCentreIdentifier tid, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("getS");
        LogicTuple spec = null;
        try {
            spec = LogicTuples.newInstance("spec", TupleArguments.newVarArgument("S"));
        } catch (final InvalidVarNameException e) {
            /*
             * Cannot happen
             */
            e.printStackTrace();
        }
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.GET_S, tid, spec, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation getS(final TupleCentreIdentifier tid,
                                final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("getS");
        final LogicTuple spec = LogicTuples.newInstance("spec");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.GET_S, tid, spec, l, this.getPosition());
    }

    @Override
    public UUID getUUID() {
        return this.executor.agentUUID;
    }

    @Override
    public TucsonOperation in(final TupleCentreIdentifier tid, final Tuple tuple,
                              final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("in");
        return super.in(tid, tuple, timeout);
    }

    @Override
    public TucsonOperation in(final TupleCentreIdentifier tid, final Tuple tuple,
                              final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("in");
        return super.in(tid, tuple, l);
    }

    @Override
    public TucsonOperation inAll(final TupleCentreIdentifier tid, final Tuple tuple,
                                 final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("inAll");
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.IN_ALL, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation inAll(final TupleCentreIdentifier tid, final Tuple tuple,
                                 final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("inAll");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.IN_ALL, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation inp(final TupleCentreIdentifier tid, final Tuple tuple,
                               final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("inp");
        return super.inp(tid, tuple, timeout);
    }

    @Override
    public TucsonOperation inp(final TupleCentreIdentifier tid, final Tuple tuple,
                               final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("inp");
        return super.inp(tid, tuple, l);
    }

    @Override
    public TucsonOperation inpS(final TupleCentreIdentifier tid,
                                final LogicTuple event, final LogicTuple guards,
                                final LogicTuple reactionBody, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("inpS");
        final LogicTuple tuple = LogicTuples.newInstance(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.INP_S, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation inpS(final TupleCentreIdentifier tid,
                                final LogicTuple event, final LogicTuple guards,
                                final LogicTuple reactionBody,
                                final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("inpS");
        final LogicTuple tuple = LogicTuples.newInstance(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.INP_S, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation inS(final TupleCentreIdentifier tid,
                               final LogicTuple event, final LogicTuple guards,
                               final LogicTuple reactionBody, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("inS");
        final LogicTuple tuple = LogicTuples.newInstance(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.IN_S, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation inS(final TupleCentreIdentifier tid,
                               final LogicTuple event, final LogicTuple guards,
                               final LogicTuple reactionBody,
                               final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("inS");
        final LogicTuple tuple = LogicTuples.newInstance(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.IN_S, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation no(final TupleCentreIdentifier tid, final Tuple tuple,
                              final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("no");
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.NO, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation no(final TupleCentreIdentifier tid, final Tuple tuple,
                              final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("no");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.NO, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation noAll(final TupleCentreIdentifier tid, final Tuple tuple,
                                 final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("noAll");
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.NO_ALL, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation noAll(final TupleCentreIdentifier tid, final Tuple tuple,
                                 final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("noAll");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.NO_ALL, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation nop(final TupleCentreIdentifier tid, final Tuple tuple,
                               final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("nop");
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.NOP, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation nop(final TupleCentreIdentifier tid, final Tuple tuple,
                               final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("nop");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.NOP, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation nopS(final TupleCentreIdentifier tid,
                                final LogicTuple event, final LogicTuple guards,
                                final LogicTuple reactionBody, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("nopS");
        final LogicTuple tuple = LogicTuples.newInstance(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.NOP_S, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation nopS(final TupleCentreIdentifier tid,
                                final LogicTuple event, final LogicTuple guards,
                                final LogicTuple reactionBody,
                                final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("nopS");
        final LogicTuple tuple = LogicTuples.newInstance(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.NOP_S, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation noS(final TupleCentreIdentifier tid,
                               final LogicTuple event, final LogicTuple guards,
                               final LogicTuple reactionBody, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("noS");
        final LogicTuple tuple = LogicTuples.newInstance(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.NO_S, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation noS(final TupleCentreIdentifier tid,
                               final LogicTuple event, final LogicTuple guards,
                               final LogicTuple reactionBody,
                               final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("noS");
        final LogicTuple tuple = LogicTuples.newInstance(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.NO_S, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation out(final TupleCentreIdentifier tid, final Tuple tuple,
                               final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("out");
        return super.out(tid, tuple, timeout);
    }

    @Override
    public TucsonOperation out(final TupleCentreIdentifier tid, final Tuple tuple,
                               final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("out");
        return super.out(tid, tuple, l);
    }

    @Override
    public TucsonOperation outAll(final TupleCentreIdentifier tid, final Tuple tuple,
                                  final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("outAll");
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.OUT_ALL, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation outAll(final TupleCentreIdentifier tid, final Tuple tuple,
                                  final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("outAll");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.OUT_ALL, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation outS(final TupleCentreIdentifier tid,
                                final LogicTuple event, final LogicTuple guards,
                                final LogicTuple reactionBody, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("outS");
        final LogicTuple tuple = LogicTuples.newInstance(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.OUT_S, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation outS(final TupleCentreIdentifier tid,
                                final LogicTuple event, final LogicTuple guards,
                                final LogicTuple reactionBody,
                                final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("outS");
        final LogicTuple tuple = LogicTuples.newInstance(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.OUT_S, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation rd(final TupleCentreIdentifier tid, final Tuple tuple,
                              final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("rd");
        return super.rd(tid, tuple, timeout);
    }

    @Override
    public TucsonOperation rd(final TupleCentreIdentifier tid, final Tuple tuple,
                              final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("rd");
        return super.rd(tid, tuple, l);
    }

    @Override
    public TucsonOperation rdAll(final TupleCentreIdentifier tid, final Tuple tuple,
                                 final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("rdAll");
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.RD_ALL, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation rdAll(final TupleCentreIdentifier tid, final Tuple tuple,
                                 final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("rdAll");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.RD_ALL, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation rdp(final TupleCentreIdentifier tid, final Tuple tuple,
                               final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("rdp");
        return super.rdp(tid, tuple, timeout);
    }

    @Override
    public TucsonOperation rdp(final TupleCentreIdentifier tid, final Tuple tuple,
                               final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("rdp");
        return super.rdp(tid, tuple, l);
    }

    @Override
    public TucsonOperation rdpS(final TupleCentreIdentifier tid,
                                final LogicTuple event, final LogicTuple guards,
                                final LogicTuple reactionBody, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("rdpS");
        final LogicTuple tuple = LogicTuples.newInstance(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.RDP_S, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation rdpS(final TupleCentreIdentifier tid,
                                final LogicTuple event, final LogicTuple guards,
                                final LogicTuple reactionBody,
                                final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("rdpS");
        final LogicTuple tuple = LogicTuples.newInstance(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.RDP_S, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation rdS(final TupleCentreIdentifier tid,
                               final LogicTuple event, final LogicTuple guards,
                               final LogicTuple reactionBody, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("rdS");
        final LogicTuple tuple = LogicTuples.newInstance(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.RD_S, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation rdS(final TupleCentreIdentifier tid,
                               final LogicTuple event, final LogicTuple guards,
                               final LogicTuple reactionBody,
                               final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("rdS");
        final LogicTuple tuple = LogicTuples.newInstance(Parser.parseSingleTerm(
                "reaction(" + event + "," + guards + "," + reactionBody + ")",
                new LogicTupleOpManager()));
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.RD_S, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation set(final TupleCentreIdentifier tid, final Tuple tuple,
                               final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("set");
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.SET, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation set(final TupleCentreIdentifier tid, final Tuple tuple,
                               final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("set");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.SET, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation setS(final TupleCentreIdentifier tid,
                                final LogicTuple spec, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("setS");
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.SET_S, tid, spec, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation setS(final TupleCentreIdentifier tid,
                                final LogicTuple spec, final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("setS");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.SET_S, tid, spec, l, this.getPosition());
    }

    @Override
    public TucsonOperation setS(final TupleCentreIdentifier tid, final String spec,
                                final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("setS");
        if ("".equals(spec) || "''".equals(spec) || "'.'".equals(spec)) {
            throw new TucsonOperationNotPossibleException();
        }
        final LogicTuple specT = LogicTuples.newInstance("spec", TupleArguments.newValueArgument(spec));
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.SET_S, tid, specT, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation setS(final TupleCentreIdentifier tid, final String spec,
                                final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("setS");
        final LogicTuple specT = LogicTuples.newInstance("spec", TupleArguments.newValueArgument(spec));
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.SET_S, tid, specT, l, this.getPosition());
    }

    @Override
    public TucsonOperation spawn(final TupleCentreIdentifier tid, final Tuple toSpawn,
                                 final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("spawn");
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.SPAWN, tid, toSpawn, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation spawn(final TupleCentreIdentifier tid, final Tuple toSpawn,
                                 final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("spawn");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.SPAWN, tid, toSpawn, l, this.getPosition());
    }

    @Override
    public TucsonOperation uin(final TupleCentreIdentifier tid, final Tuple tuple,
                               final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("uin");
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.UIN, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation uin(final TupleCentreIdentifier tid, final Tuple tuple,
                               final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("uin");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.UIN, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation uinp(final TupleCentreIdentifier tid, final Tuple tuple,
                                final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("uinp");
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.UINP, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation uinp(final TupleCentreIdentifier tid, final Tuple tuple,
                                final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("uinp");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.UINP, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation uno(final TupleCentreIdentifier tid, final Tuple tuple,
                               final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("uno");
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.UNO, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation uno(final TupleCentreIdentifier tid, final Tuple tuple,
                               final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("uno");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.UNO, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation unop(final TupleCentreIdentifier tid, final Tuple tuple,
                                final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("unop");
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.UNOP, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation unop(final TupleCentreIdentifier tid, final Tuple tuple,
                                final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("unop");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.UNOP, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation urd(final TupleCentreIdentifier tid, final Tuple tuple,
                               final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("urd");
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.URD, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation urd(final TupleCentreIdentifier tid, final Tuple tuple,
                               final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("urd");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.URD, tid, tuple, l, this.getPosition());
    }

    @Override
    public TucsonOperation urdp(final TupleCentreIdentifier tid, final Tuple tuple,
                                final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.checkPermission("urdp");
        return this.executor.doBlockingOperation(this.aid,
                TupleCentreOpType.URDP, tid, tuple, timeout, this.getPosition());
    }

    @Override
    public TucsonOperation urdp(final TupleCentreIdentifier tid, final Tuple tuple,
                                final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.checkPermission("urdp");
        return this.executor.doNonBlockingOperation(this.aid,
                TupleCentreOpType.URDP, tid, tuple, l, this.getPosition());
    }

    private void checkPermission(final String perm)
            throws TucsonOperationNotPossibleException {
        if (this.permissions.isEmpty() || !this.permissions.contains(perm)) {
            throw new TucsonOperationNotPossibleException();
        }
    }

    private void setPermissions() {
        this.permissions = new ArrayList<String>();
        final List<Permission> perms = this.role.getPolicy().getPermissions();
        for (final Permission perm : perms) {
            this.permissions.add(perm.getPermissionName());
        }
    }

    private void setRole(final Role r) {
        this.role = r;
        this.setPermissions();
    }
}
