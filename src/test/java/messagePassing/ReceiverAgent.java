package messagePassing;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.AbstractTucsonAgent;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonAgentIdDefault;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.OrdinaryAndSpecificationSyncACC;
import alice.tuplecentre.tucson.api.acc.RootACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.service.TucsonInfo;

/**
 * Receiver thread copyOf a two-thread synchronous conversation protocol. Given a
 * target TucsonAgent and a TuCSoN Node to convey information, it performs a
 * simple synchronous conversation with its sender agent.
 *
 * @author s.mariani@unibo.it
 */
public class ReceiverAgent extends AbstractTucsonAgent<RootACC> {

    /**
     * @param args
     *            no args expected.
     */
    public static void main(final String[] args) {
        try {
            new ReceiverAgent("bob", "rob", "default@localhost:" + TucsonInfo.getDefaultPortNumber()).go();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

    private OrdinaryAndSpecificationSyncACC acc;
    private TucsonOperation op;
    private TucsonAgentId sender;

    private TucsonTupleCentreId tid;

    /**
     * @param aid
     *            the name copyOf the agent
     * @param who
     *            the name copyOf the agent to reply to
     * @param node
     *            the node used as "message transport layer"
     *
     * @throws TucsonInvalidAgentIdException
     *             if the chosen Identifier is not a valid TuCSoN agent Identifier
     */
    public ReceiverAgent(final String aid, final String who, final String node)
            throws TucsonInvalidAgentIdException {
        super(aid);
        try {
            this.sender = new TucsonAgentIdDefault(who);
            this.tid = TucsonTupleCentreId.of(node);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            this.say("Invalid tid given, killing myself...");
        }
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
    protected RootACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        return null; //not used because, NegotiationACC does not extend RootACC
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
            templ = LogicTuple.parse("msg(sender(" + this.sender.getLocalName()
                    + ")," + "content(M), receiver(" + this.getTucsonAgentId().getLocalName() + "))");
            reply = this.receive(templ);
            this.say("	< " + reply.getArg("content").getArg(0).toString());
            /*
             * ...hello...
             */
            msg = LogicTuple.parse("msg(sender(" + this.getTucsonAgentId().getLocalName() + "),"
                    + "content('Hello, " + this.sender.getLocalName() + "!'),"
                    + "receiver(" + this.sender.getLocalName() + ")" + ")");
            this.say("> Hello, " + this.sender.getLocalName() + "!");
            this.send(msg);
            /*
             * ...how are you...
             */
            templ = LogicTuple.parse("msg(sender(" + this.sender.getLocalName()
                    + ")," + "content(M), receiver(" + this.getTucsonAgentId().getLocalName() + "))");
            reply = this.receive(templ);
            this.say("	< " + reply.getArg("content").getArg(0).toString());
            /*
             * ...fine...
             */
            msg = LogicTuple.parse("msg(sender(" + this.getTucsonAgentId().getLocalName() + "),"
                    + "content('Fine thanks, and you "
                    + this.sender.getLocalName() + "?')," + "receiver("
                    + this.sender.getLocalName() + ")" + ")");
            this.say("> Fine thanks, and you " + this.sender.getLocalName()
                    + "?");
            this.send(msg);
            /*
             * ...me too...
             */
            templ = LogicTuple.parse("msg(sender(" + this.sender.getLocalName()
                    + ")," + "content(M), receiver(" + this.getTucsonAgentId().getLocalName() + "))");
            reply = this.receive(templ);
            this.say("	< " + reply.getArg("content").getArg(0).toString());
            /*
             * ...bye...
             */
            msg = LogicTuple.parse("msg(sender(" + this.getTucsonAgentId().getLocalName() + "),"
                    + "content('Nice. Well...bye!')," + "receiver("
                    + this.sender.getLocalName() + ")" + ")");
            this.say("> Nice. Well...bye!");
            this.send(msg);
            /*
             * ...bye.
             */
            templ = LogicTuple.parse("msg(sender(" + this.sender.getLocalName()
                    + ")," + "content(M), receiver(" + this.getTucsonAgentId().getLocalName() + "))");
            reply = this.receive(templ);
            this.say("	< " + reply.getArg("content").getArg(0).toString());
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
            this.say("ERROR: Given Identifier is not a valid TuCSoN agent Identifier!");
        }
    }

}
