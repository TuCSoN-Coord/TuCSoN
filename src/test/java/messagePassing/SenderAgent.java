package messagePassing;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.AbstractTucsonAgent;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonAgentIdDefault;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.OrdinaryAndSpecificationSyncACC;
import alice.tuplecentre.tucson.api.acc.RootACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.service.TucsonInfo;

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
            new SenderAgent("rob", "bob", "default@localhost:" + TucsonInfo.getDefaultPortNumber()).go();
        } catch (final TucsonInvalidAgentIdException e) {
            LOGGER.error(e.getMessage(), e);
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
     *             if the chosen Identifier is not a valid TuCSoN agent Identifier
     */
    public SenderAgent(final String aid, final String who, final String node)
            throws TucsonInvalidAgentIdException {
        super(aid);
        try {
            this.receiver = new TucsonAgentIdDefault(who);
            this.tid = new TucsonTupleCentreIdDefault(node);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            this.say("Invalid tid given, killing myself...");
        }
    }

    @Override
    protected RootACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        return null; //not used because, NegotiationACC does not extend RootACC
    }

    private LogicTuple receive(final LogicTuple templ)
            throws InvalidLogicTupleException,
            TucsonOperationNotPossibleException, UnreachableNodeException,
            OperationTimeOutException {
        this.acc.out(this.tid, LogicTuples.parse("get(msg)"), null);
        this.op = this.acc.in(this.tid, templ, null);
        this.acc.out(this.tid, LogicTuples.parse("got(msg)"), null);
        return this.op.getLogicTupleResult();
    }

    private void send(final LogicTuple msg) throws InvalidLogicTupleException,
    TucsonOperationNotPossibleException, UnreachableNodeException,
    OperationTimeOutException {
        this.acc.in(this.tid, LogicTuples.parse("get(msg)"), null);
        this.acc.out(this.tid, msg, null);
        this.acc.in(this.tid, LogicTuples.parse("got(msg)"), null);
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
            msg = LogicTuples.parse("msg(sender(" + this.getTucsonAgentId().getLocalName() + "),"
                    + "content('Hi " + this.receiver.getLocalName() + "!'),"
                    + "receiver(" + this.receiver.getLocalName() + ")" + ")");
            this.say("> Hi " + this.receiver.getLocalName() + "!");
            this.send(msg);
            /*
             * ...hello...
             */
            templ = LogicTuples.parse("msg(sender("
                    + this.receiver.getLocalName() + "),"
                    + "content(M), receiver(" + this.getTucsonAgentId().getLocalName() + "))");
            reply = this.receive(templ);
            this.say("	< " + reply.getArg("content").getArg(0).toString());
            /*
             * ...how are you...
             */
            msg = LogicTuples.parse("msg(sender(" + this.getTucsonAgentId().getLocalName() + "),"
                    + "content('How are you, " + this.receiver.getLocalName()
                    + "?')," + "receiver(" + this.receiver.getLocalName() + ")"
                    + ")");
            this.say("> How are you, " + this.receiver.getLocalName() + "?");
            this.send(msg);
            /*
             * Old template is now 'unified' with received tuple, we need a new
             * one.
             */
            templ = LogicTuples.parse("msg(sender("
                    + this.receiver.getLocalName() + "),"
                    + "content(M), receiver(" + this.getTucsonAgentId().getLocalName() + "))");
            /*
             * ...fine...
             */
            reply = this.receive(templ);
            this.say("	< " + reply.getArg("content").getArg(0).toString());
            /*
             * ...me too...
             */
            msg = LogicTuples.parse("msg(sender(" + this.getTucsonAgentId().getLocalName() + "),"
                    + "content('I am fine too, " + this.receiver.getLocalName()
                    + ", thanks!')," + "receiver("
                    + this.receiver.getLocalName() + ")" + ")");
            this.say("> I am fine too, " + this.receiver.getLocalName()
                    + ", thanks!");
            this.send(msg);
            /*
             * ...bye...
             */
            templ = LogicTuples.parse("msg(sender("
                    + this.receiver.getLocalName() + "),"
                    + "content(M), receiver(" + this.getTucsonAgentId().getLocalName() + "))");
            reply = this.receive(templ);
            this.say("	< " + reply.getArg("content").getArg(0).toString());
            /*
             * ...bye.
             */
            msg = LogicTuples.parse("msg(sender(" + this.getTucsonAgentId().getLocalName() + "),"
                    + "content('Bye!')," + "receiver("
                    + this.receiver.getLocalName() + ")" + ")");
            this.say("> Bye!");
            this.send(msg);
        } catch (final InvalidLogicTupleException e) {
            this.say("ERROR: Tuple is not an admissible Prolog term!");
            LOGGER.error(e.getMessage(), e);
        } catch (final TucsonOperationNotPossibleException e) {
            this.say("ERROR: Never seen this happen before *_*");
        } catch (final UnreachableNodeException e) {
            this.say("ERROR: Given TuCSoN Node is unreachable!");
            LOGGER.error(e.getMessage(), e);
        } catch (final OperationTimeOutException e) {
            this.say("ERROR: Endless timeout expired!");
        } catch (final TucsonInvalidAgentIdException e) {
            this.say("ERROR: Given Identifier is not a valid TuCSoN agent Identifier!");
        }
    }

}
