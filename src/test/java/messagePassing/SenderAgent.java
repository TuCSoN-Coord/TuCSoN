package messagePassing;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.tucson.api.*;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.OrdinaryAndSpecificationSyncACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * Sender thread of a two-thread synchronous conversation protocol. Given a
 * target TucsonAgent and a TuCSoN Node to convey information, it performs a
 * simple synchronous conversation with its target agent.
 *
 * @author s.mariani@unibo.it
 */
public class SenderAgent extends AbstractTucsonAgent {

    /**
     * @param args
     *            no args are expected.
     */
    public static void main(final String[] args) {
        try {
            new SenderAgent("rob", "bob", "default@localhost:20504").go();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

    private OrdinaryAndSpecificationSyncACC acc;
    private TucsonOperation op;
    private TucsonAgentId receiver;

    private TucsonTupleCentreId tid;

    /**
     * @param aid
     *            the name of the agent
     * @param who
     *            the name of the agent to talk with
     * @param node
     *            the node used as "message transport layer"
     *
     * @throws TucsonInvalidAgentIdException
     *             if the chosen ID is not a valid TuCSoN agent ID
     */
    public SenderAgent(final String aid, final String who, final String node)
            throws TucsonInvalidAgentIdException {
        super(aid);
        try {
            this.receiver = new TucsonAgentId(who);
            this.tid = new TucsonTupleCentreId(node);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            this.say("Invalid tid given, killing myself...");
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuplecentre.tucson.api.AbstractTucsonAgent#operationCompleted(alice.tuplecentre
     * .core.AbstractTupleCentreOperation)
     */
    @Override
    public void operationCompleted(final AbstractTupleCentreOperation arg0) {
        /*
         * not used atm
         */
    }

    @Override
    public void operationCompleted(final TucsonOperation arg0) {
        /*
         * not used atm
         */
    }

    private LogicTuple receive(final LogicTuple templ)
            throws InvalidLogicTupleException,
            TucsonOperationNotPossibleException, UnreachableNodeException,
            OperationTimeOutException {
        this.acc.out(this.tid, LogicTuple.parse("get(msg)"), null);
        this.op = this.acc.in(this.tid, templ, null);
        this.acc.out(this.tid, LogicTuple.parse("got(msg)"), null);
        return this.op.getLogicTupleResult();
    }

    private void send(final LogicTuple msg) throws InvalidLogicTupleException,
    TucsonOperationNotPossibleException, UnreachableNodeException,
    OperationTimeOutException {
        this.acc.in(this.tid, LogicTuple.parse("get(msg)"), null);
        this.acc.out(this.tid, msg, null);
        this.acc.in(this.tid, LogicTuple.parse("got(msg)"), null);
    }

    @Override
    protected void main() {
        this.say("I'm started.");
        try {
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(this.getTucsonAgentId());
            this.acc = negAcc.playDefaultRole();
            LogicTuple msg;
            LogicTuple templ;
            LogicTuple reply;
            /*
             * hi...
             */
            msg = LogicTuple.parse("msg(sender(" + this.myName() + "),"
                    + "content('Hi " + this.receiver.getAgentName() + "!'),"
                    + "receiver(" + this.receiver.getAgentName() + ")" + ")");
            this.say("> Hi " + this.receiver.getAgentName() + "!");
            this.send(msg);
            /*
             * ...hello...
             */
            templ = LogicTuple.parse("msg(sender("
                    + this.receiver.getAgentName() + "),"
                    + "content(M), receiver(" + this.myName() + "))");
            reply = this.receive(templ);
            this.say("	< " + reply.getArg("content").getArg(0).toString());
            /*
             * ...how are you...
             */
            msg = LogicTuple.parse("msg(sender(" + this.myName() + "),"
                    + "content('How are you, " + this.receiver.getAgentName()
                    + "?')," + "receiver(" + this.receiver.getAgentName() + ")"
                    + ")");
            this.say("> How are you, " + this.receiver.getAgentName() + "?");
            this.send(msg);
            /*
             * Old template is now 'unified' with received tuple, we need a new
             * one.
             */
            templ = LogicTuple.parse("msg(sender("
                    + this.receiver.getAgentName() + "),"
                    + "content(M), receiver(" + this.myName() + "))");
            /*
             * ...fine...
             */
            reply = this.receive(templ);
            this.say("	< " + reply.getArg("content").getArg(0).toString());
            /*
             * ...me too...
             */
            msg = LogicTuple.parse("msg(sender(" + this.myName() + "),"
                    + "content('I am fine too, " + this.receiver.getAgentName()
                    + ", thanks!')," + "receiver("
                    + this.receiver.getAgentName() + ")" + ")");
            this.say("> I am fine too, " + this.receiver.getAgentName()
                    + ", thanks!");
            this.send(msg);
            /*
             * ...bye...
             */
            templ = LogicTuple.parse("msg(sender("
                    + this.receiver.getAgentName() + "),"
                    + "content(M), receiver(" + this.myName() + "))");
            reply = this.receive(templ);
            this.say("	< " + reply.getArg("content").getArg(0).toString());
            /*
             * ...bye.
             */
            msg = LogicTuple.parse("msg(sender(" + this.myName() + "),"
                    + "content('Bye!')," + "receiver("
                    + this.receiver.getAgentName() + ")" + ")");
            this.say("> Bye!");
            this.send(msg);
        } catch (final InvalidLogicTupleException e) {
            this.say("ERROR: Tuple is not an admissible Prolog term!");
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            this.say("ERROR: Never seen this happen before *_*");
        } catch (final UnreachableNodeException e) {
            this.say("ERROR: Given TuCSoN Node is unreachable!");
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            this.say("ERROR: Endless timeout expired!");
        } catch (final TucsonInvalidAgentIdException e) {
            this.say("ERROR: Given ID is not a valid TuCSoN agent ID!");
        }
    }

}
