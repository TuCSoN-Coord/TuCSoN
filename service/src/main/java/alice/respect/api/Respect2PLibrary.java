/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.respect.api;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import alice.tuple.logic.LogicMatchingEngine;
import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTupleDefault;
import alice.tuple.logic.LogicTupleOpManager;
import alice.tuple.logic.TupleArgument;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuple.logic.exceptions.InvalidTupleArgumentException;
import alice.respect.api.exceptions.InvalidTupleCentreIdException;
import alice.respect.api.geolocation.GeoUtils;
import alice.respect.api.geolocation.PlatformUtils;
import alice.respect.api.geolocation.Position;
import alice.respect.api.geolocation.service.AbstractGeolocationService;
import alice.respect.api.geolocation.service.GeolocationServiceManager;
import alice.respect.core.InternalEvent;
import alice.respect.core.InternalOperation;
import alice.respect.core.RespectOperationDefault;
import alice.respect.core.RespectVMContext;
import alice.respect.core.TransducersManager;
import alice.respect.situatedness.TransducerId;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.network.NetworkUtils;
import alice.tuplecentre.api.IId;
import alice.tuple.Tuple;
import alice.tuplecentre.api.TupleCentreOperation;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractEvent;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.OutputEvent;
import alice.tuprolog.InvalidTermException;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Var;
import alice.util.Tools;

/**
 * TuProlog library defining the behaviour of ReSpecT primitives, used inside
 * ReSpecT VM.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 */
public class Respect2PLibrary extends alice.tuprolog.Library {

    private static final long serialVersionUID = 7865604500315298959L;

    /**
     * @param source
     */
    private static boolean checkIP(final IId source) {
        if (source instanceof TupleCentreId) {
            final TupleCentreId tcid = (TupleCentreId) source;
            if (alice.util.Tools.removeApices(tcid.getNode()).equals(
                    Respect2PLibrary.getFirstActiveIP())) {
                return true;
            }
        }
        if (source instanceof TucsonTupleCentreId) {
            final TucsonTupleCentreId ttcid = (TucsonTupleCentreId) source;
            if (alice.util.Tools.removeApices(ttcid.getNode()).equals(
                    Respect2PLibrary.getFirstActiveIP())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return
     */
    // TODO Should return a list with all running interfaces
    private static String getFirstActiveIP() {
        Enumeration<NetworkInterface> interfaces;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                final NetworkInterface current = interfaces.nextElement();
                if (!current.isUp() || current.isLoopback()
                        || current.isVirtual()) {
                    continue;
                }
                final Enumeration<InetAddress> addresses = current
                        .getInetAddresses();
                while (addresses.hasMoreElements()) {
                    final InetAddress currentAddress = addresses.nextElement();
                    if (currentAddress.isLoopbackAddress()) {
                        continue;
                    }
                    if (currentAddress instanceof Inet4Address) {
                        return currentAddress.getHostAddress();
                    }
                }
            }
        } catch (final SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private static Term list2tuple(final List<Tuple> list) {
        final Term[] termArray = new Term[list.size()];
        final Iterator<Tuple> it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            termArray[i] = ((LogicTuple) it.next()).toTerm();
            i++;
        }
        return new Struct(termArray);
    }

    private static void log(final String s) {
        System.out.println("......[Respect2PLibrary]: " + s);
    }

    private RespectVMContext vm;
    private final static LogicTupleOpManager opMan = new LogicTupleOpManager();

    /**
     *
     * @param time
     *            the time that should have passed
     * @return <code>true</code> if the given time passed
     */
    public boolean after_1(final Term time) {
        return !this.before_1(time);
    }
    
    /**
     * 
     * @param space
     *            type of node position. It can be specified as either its
     *            absolute physical position (S=ph), its IP number (S=ip), its
     *            domain name (S=dns), its geographical location (S=map), or its
     *            organisational position (S=org).
     * @param position
     *            the expected position.
     * @return <code>true</code> if the tuple centre is currently executing at
     *         the given position, specified according to the given space term.
     */
    public boolean at_2(final Term space, final Term position) {
        return this.near_3(space, position, this.vm.getDistanceTollerance());
    }

    /**
     * @param time
     *            the time to compare.
     *
     * @return true if the ReSpecT reaction has been triggered before the given
     *         time.
     */
    public boolean before_1(final Term time) {
        final AbstractEvent ev = this.vm.getCurrentReactionEvent();
        final long evtTime = ev.getTime();
        long compareTime;
        compareTime = ((alice.tuprolog.Number) time).longValue();
        if (evtTime <= compareTime) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param time1
     *            the time that should have passed
     * @param time2
     *            the time that should not have passed
     * @return <code>true</code> if the time is between given values
     */
    public boolean between_2(final Term time1, final Term time2) {
        return this.after_1(time1) && this.before_1(time2);
    }

    /**
     *
     * @return <code>true</code> if ReSpecT VM is in the completion phase
     */
    public boolean compl_0() {
        return this.response_0();
    }

    /**
     *
     * @return <code>true</code> if ReSpecT VM is in the completion phase
     */
    public boolean completion_0() {
    	return this.response_0();
    }
    
    /**
     * 
     * @param space
     *            type of node position. It can be specified as either its
     *            absolute physical position (S=ph), its IP number (S=ip), its
     *            domain name (S=dns), its geographical location (S=map), or its
     *            organisational position (S=org).
     * @param position
     *            the expected position.
     * @return <code>true</code> if the given position, specified according to
     *         the given space term, unifies with the position of the node which
     *         the tuple centre belongs to.
     */
    public boolean current_place_2(final Term space, final Term position) {
        final Position vmPosition = this.vm.getPosition();
        final Term vmPosTerm = vmPosition.getPlace(space).toTerm();
        if (!(position instanceof Var)
                && this.unify(space, Term.createTerm(Position.PH))) {
            return this
                    .near_3(space, position, this.vm.getDistanceTollerance()); // necessary
            // because
            // of
            // latitude/longitude
            // high
            // variability
        }
        return this.unify(position, vmPosTerm); // if S=map, S=org, S=ip or
                                                // S=dns the position must
                                                // match exactly
    }

    /**
     * @param predicate
     *            the expected ReSpecT predicate currently under solving
     *            process.
     *
     * @return true if the actual ReSpecT predicate currently under solving
     *         process is the expected ReSpecT predicate currently under solving
     *         process.
     */
    public boolean current_predicate_1(final Term predicate) {
        return this.unify(predicate, new Struct("current_predicate("
                + predicate + ")"));
    }

    /**
     * @param source
     *            the expected tuplecentre which is responsible for the
     *            generation of the currently processing ReSpecT reaction.
     *
     * @return true if the actual tuplecentre is the expected tuplecentre which
     *         is responsible for the generation of the currently processing
     *         ReSpecT reaction.
     */
    public boolean current_source_1(final Term source) {
        // final Term t = ((TupleCentreId) this.vm.getId()).toTerm();
        final Term t = Term.createTerm(this.vm.getId().toString(),
                Respect2PLibrary.opMan);
        return this.unify(source, t);
    }

    /**
     * @param target
     *            the expected target of the current operation
     *
     * @return <code>true</code> if the given term unifies with the current
     *         target
     */
    public boolean current_target_1(final Term target) {
        // final Term t = ((TupleCentreId) this.vm.getId()).toTerm();
        final Term t = Term.createTerm(this.vm.getId().toString(),
                Respect2PLibrary.opMan);
        // Respect2PLibrary.log("target = " + target + " | " + t + " = t");
        return this.unify(target, t);
    }

    /**
     * @param time
     *            the expected time we're at.
     *
     * @return <code>true</code> if the given term unifies with the current time
     */
    public boolean current_time_1(final Term time) {
        final long vmTime = this.vm.getCurrentTime();
        return this.unify(time, new alice.tuprolog.Long(vmTime));
    }

    /**
     * @param tuple
     *            the expected logic tuple which directly started the current
     *            ReSpecT computation.
     *
     * @return true if the actual logic tuple which directly started the current
     *         ReSpecT computation is the expected logic tuple which directly
     *         started the current ReSpecT computation.
     */
    public boolean current_tuple_1(final Term tuple) {
        return this.unify(tuple, new Var());
    }

    /**
     *
     * @return <code>true</code> if the current ReSpecT event is coming from
     *         this tuple centre
     */
    public boolean endo_0() {
        return !this.exo_0();
    }

    /**
     *
     * @param key
     *            the term representing the environmental property to be tested
     * @param value
     *            the term representing the value of the environmental property
     * @return <code>true</code> if the environmental property given has the
     *         given value
     */
    public boolean env_2(final Term key, final Term value) {
        if (value instanceof alice.tuprolog.Var) {
            final String res = this.vm.getCurrentReactionEvent().getEventProp(
                    key.getTerm().toString());
            if (res == null) {
                return false;
            }
            return this.unify(value, new Struct(res));
        }
        return false;
    }
    
    /**
     * 
     * @param space
     *            type of node position. It can be specified as either its
     *            absolute physical position (S=ph), its IP number (S=ip), its
     *            domain name (S=dns), its geographical location (S=map), or its
     *            organisational position (S=org).
     * @param position
     *            the expected position.
     * @return <code>true</code> if the given position, specified according to
     *         the given space term, unifies with the position of the node where
     *         the triggering event was originated.
     */
    public boolean event_place_2(final Term space, final Term position) {
        /**
         * event fa riferimento alla causa diretta, dunque se l'evento passa la
         * guardia operation, ci� che serve � la posizione dell'agente, mentre
         * se non passa la guardia operation, (e.g. � un link_in) serve la
         * posizione del tc (dunque del nodo).
         */
        final InputEvent ev = this.vm.getCurrentEvent();
        Term ePlaceTerm = null;
        if (ev.isLinking()) {
            ePlaceTerm = this.vm.getPosition().getPlace(space).toTerm();
        } else {
            ePlaceTerm = ev.getPosition().getPlace(space).toTerm();
        }
        return this.unify(position, ePlaceTerm);
    }

    /**
     *
     * @param predicate
     *            the predicate direct cause of current ReSpecT event
     * @return <code>true</code> if the current ReSpecT event has the given
     *         direct cause
     */
    public boolean event_predicate_1(final Term predicate) {
        return this
                .unify(predicate, this.vm.getCurrentReactionTerm().getTerm());
    }

    /**
     * @param source
     *            the identifier of the direct cause source of the event
     *
     * @return <code>true</code> if current ReSpecT event direct cause has the
     *         given source
     */
    public boolean event_source_1(final Term source) {
        final IId id = this.vm.getCurrentReactionEvent().getSource();
        if (id.isAgent()) {
            // final Term aid = ((AgentId) id).toTerm();
            final Term aid = Term.createTerm(id.toString(),
                    new LogicTupleOpManager());
            return this.unify(source, aid);
        } else if (id.isTC()) {
            // final Term tcid = ((TupleCentreId) id).toTerm();
            final Term tcid = Term.createTerm(id.toString(),
                    new LogicTupleOpManager());
            return this.unify(source, tcid);
        } else {
            return false;
        }
    }

    /**
     *
     * @param target
     *            the identifier of the direct cause target
     * @return <code>true</code> if current ReSpecT event direct cause has the
     *         given target
     */
    public boolean event_target_1(final Term target) {
        final IId id = this.vm.getCurrentReactionEvent().getTarget();
        if (id.isAgent()) {
            // final Term aid = ((AgentId) id).toTerm();
            final Term aid = Term.createTerm(id.toString(),
                    new LogicTupleOpManager());
            return this.unify(target, aid);
        } else if (id.isTC()) {
            // final Term tcid = ((TupleCentreId) id).toTerm();
            final Term tcid = Term.createTerm(id.toString(),
                    new LogicTupleOpManager());
            return this.unify(target, tcid);
        } else {
            return false;
        }
    }

    /**
     * @param time
     *            the expected time at which the current ReSpecT computation has
     *            been triggered.
     *
     * @return true if the actual time at which the current ReSpecT computation
     *         has been triggered is the expected time at which the current
     *         ReSpecT computation has been triggered.
     */
    public boolean event_time_1(final Term time) {
        final long reTime = this.vm.getCurrentReactionEvent().getTime();
        return this.unify(time, new alice.tuprolog.Long(reTime));
    }

    /**
     * @param tuple
     *            the tuple argument of the direct cause predicate
     *
     * @return <code>true</code> if the direct cause tuple argument has the
     *         given tuple
     */
    public boolean event_tuple_1(final Term tuple) {
        final Term t = this.vm.getCurrentReactionTerm().getArg(0);
        return this.unify(tuple, t);
    }

    /**
     * @return true if the initial requestor of the ReSpecT operation IS NOT the
     *         currently reacting tuplecentre.
     */
    public boolean exo_0() {
        final AbstractEvent ev = this.vm.getCurrentReactionEvent();
        final IId source = ev.getSource();
        if (!source.isTC()) {
            return true;
        }
        final IId currentTc = this.vm.getId();
        if (currentTc.toString().equals(source.toString())) {
            return false;
        }
        if (Respect2PLibrary.checkIP(source)) {
            return false;
        }
        return true;
    }

    /**
     * @return true if the ReSpecT operation failed.
     */
    public boolean failure_0() {
        final AbstractEvent ev = this.vm.getCurrentReactionEvent();
        final TupleCentreOperation op = ev.getSimpleTCEvent();
        return op.isResultFailure();
    }

    /**
     * @return true if the initial requestor of the ReSpecT operation is an
     *         agent (either Java or tuProlog or whatever).
     */
    public boolean from_agent_0() {
        final AbstractEvent ev = this.vm.getCurrentReactionEvent();
        final IId source = ev.getSource();
        if (source.isAgent()) {
            return true;
        }
        return false;
    }

    /********************************************************************
     * Situated ReSpecT extension: still to test.
     ********************************************************************/
    /**
     *
     * @return <code>true</code> if the triggering event has been generated by
     *         the environment
     */
    public boolean from_env_0() {
    	return this.vm.getCurrentReactionEvent().getSource().isEnv();
    }

    /**
     * @return true if the initial requestor of the ReSpecT operation is a
     *         ReSpecT tuplecentre.
     */
    public boolean from_tc_0() {
        final AbstractEvent ev = this.vm.getCurrentReactionEvent();
        final IId source = ev.getSource();
        if (source.isTC()) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param arg0
     *            the Prolog variable to unify the result with
     * @param arg1
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg1 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @return <code>true</code> if the operation is successfull
     */
    public boolean get_2(final Term arg0, final Term arg1)
            throws InvalidTupleCentreIdException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg1);
        tcName = tid.getName();
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local get triggered...");
            final List<Tuple> list = this.vm.getAllTuples();
            final TupleArgument[] array = new TupleArgument[list.size()];
            int i = 0;
            while (!list.isEmpty()) {
                array[i++] = new TupleArgument(
                        ((LogicTuple) list.remove(0)).toTerm());
            }
            final Tuple tuple = new LogicTupleDefault("get",
                    array);
            if (((LogicTuple) tuple).getArg(0) != null) {
                final Term term = ((LogicTuple) tuple).toTerm();
                this.unify(arg0, term.copyGoal(v, 0));
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent ev = new InternalEvent(ce,
                        InternalOperation.makeGetR(new LogicTupleDefault(arg0
                                .copyGoal(v, 0))));
                ev.setSource(ce.getReactingTC());
                ev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(ev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote get triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeGet(new LogicTupleDefault(arg0.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param arg0
     *            the Prolog variable to unify the result with
     * @param arg1
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg1 (tuple centre's id) is not a well-formed ground logic
     *             term
     *
     * @return <code>true</code> if the operation is successfull
     */
    public boolean get_s_2(final Term arg0, final Term arg1)
            throws InvalidTupleCentreIdException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg1);
        tcName = tid.getName();
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local get_s triggered...");
            final Iterator<LogicTuple> it = this.vm.getSpecTupleSetIterator();
            final List<Tuple> list = new LinkedList<Tuple>();
            while (it.hasNext()) {
                list.add(it.next());
            }
            final TupleArgument[] array = new TupleArgument[list.size()];
            int i = 0;
            while (!list.isEmpty()) {
                array[i++] = new TupleArgument(
                        ((LogicTuple) list.remove(0)).toTerm());
            }
            final Tuple tuple = new LogicTupleDefault("get_s",
                    array);
            if (((LogicTuple) tuple).getArg(0) != null) {
                final Term term = ((LogicTuple) tuple).toTerm();
                this.unify(arg0, term.copyGoal(v, 0));
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent ev = new InternalEvent(ce,
                        InternalOperation.makeGetSR(new LogicTupleDefault(arg0
                                .copyGoal(v, 0))));
                ev.setSource(ce.getReactingTC());
                ev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(ev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote get_s triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeGetS(new LogicTupleDefault(arg0.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param env
     *            the identifier of the target environmental resource
     * @param key
     *            the environmental property to sense
     * @param val
     *            the value sensed
     * @throws OperationTimeOutException
     *             if the notification operation expires timeout
     * @throws UnreachableNodeException
     *             if the TuCSoN tuple centre target of the notification cannot
     *             be reached over the network
     * @throws TucsonOperationNotPossibleException
     *             if the requested operation cannot be performed for some
     *             reason
     * @return <code>true</code> if the operation is successfull
     */
    public boolean getEnv_3(final Term env, final Term key, final Term val)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        // Get engine's copy of key and val
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        final AbstractMap<Var, Var> v1 = new LinkedHashMap<Var, Var>();
        final LogicTuple lt = new LogicTupleDefault("getEnv", new TupleArgument(
                key.copyGoal(v, 0)), new TupleArgument(val.copyGoal(v1, 0)));
        // Building internal event
        final InputEvent ev = this.vm.getCurrentEvent();
        // log("### DEBUG >>> ev = " + ev);
        final InternalEvent internalEv = new InternalEvent(ev,
                InternalOperation.makeGetEnv(lt));
        // log("### DEBUG >>> iev = " + internalEv);
        final String normEnv = env.toString().substring(
                env.toString().indexOf("(") + 1, env.toString().indexOf(","));
        final EnvId envId = new EnvId(normEnv);
        internalEv.setTarget(envId); // Set target resource
        // log("### DEBUG >>> target = " + envId);
        internalEv.setSource(this.vm.getId()); // Set the source of the event
        final TransducersManager tm = TransducersManager.INSTANCE;
        // Getting the transducer from the transducer manager
        final TransducerId tId = tm.getTransducerId(envId);
        if (tm.getTransducer(tId.getAgentName()).notifyOutput(internalEv)) {
            this.vm.fetchTriggeredReactions(internalEv);
            return true;
        }
        return false;
    }

    /**
     * ReSpecT theory to interface with Prolog.
     *
     * @return the String representation of the Prolog theory usable by tuProlog
     *         agents
     */
    @Override
    public String getTheory() {
        return ":- op(551, xfx, '?'). \n" + ":- op(550, xfx, '@'). \n"
                + ":- op(549, xfx, ':'). \n" + ":- op(548, xfx, '.'). \n"
                + "TC ? Op :- not(TC = Name @ Host), TC@localhost ? Op, !. \n"
                + "TC ? out(T) :- out(T,TC). \n" + "TC ? in(T) :- in(T,TC). \n"
                + "TC ? rd(T) :- rd(T,TC). \n" + "TC ? inp(T) :- inp(T,TC). \n"
                + "TC ? rdp(T) :- rdp(T,TC). \n" + "TC ? no(T) :- no(T,TC). \n"
                + "TC ? nop(T) :- nop(T,TC). \n"
                + "TC ? set(T) :- set(T,TC). \n"
                + "TC ? get(T) :- get(T,TC). \n"
                + "TC ? spawn(T) :- spawn(T,TC). \n"
                + "TC ? out_s(E,G,R) :- out_s(E,G,R,TC). \n"
                + "TC ? in_s(E,G,R) :- in_s(E,G,R,TC). \n"
                + "TC ? rd_s(E,G,R) :- rd_s(E,G,R,TC). \n"
                + "TC ? inp_s(E,G,R) :- inp_s(E,G,R,TC). \n"
                + "TC ? rdp_s(E,G,R) :- rdp_s(E,G,R,TC). \n"
                + "TC ? no_s(E,G,R) :- no_s(E,G,R,TC). \n"
                + "TC ? nop_s(E,G,R) :- nop_s(E,G,R,TC). \n"
                + "TC ? set_s(E,G,R) :- set_s(E,G,R,TC). \n"
                + "TC ? get_s(T) :- get_s(T,TC). \n"
                + "out(T):-out(T,this@localhost). \n"
                + "in(T):-in(T,this@localhost). \n"
                + "rd(T):-rd(T,this@localhost). \n"
                + "inp(T):-inp(T,this@localhost). \n"
                + "rdp(T):-rdp(T,this@localhost). \n"
                + "no(T):-no(T,this@localhost). \n"
                + "nop(T):-nop(T,this@localhost). \n"
                + "set(T):-set(T,this@localhost). \n"
                + "get(T):-get(T,this@localhost). \n"
                + "spawn(T):-spawn(T,this@localhost). \n"
                + "out_s(E,G,R):-out_s(E,G,R,this@localhost). \n"
                + "in_s(E,G,R):-in_s(E,G,R,this@localhost). \n"
                + "rd_s(E,G,R):-rd_s(E,G,R,this@localhost). \n"
                + "inp_s(E,G,R):-inp_s(E,G,R,this@localhost). \n"
                + "rdp_s(E,G,R):-rdp_s(E,G,R,this@localhost). \n"
                + "no_s(E,G,R):-no_s(E,G,R,this@localhost). \n"
                + "nop_s(E,G,R):-nop_s(E,G,R,this@localhost). \n"
                + "set_s(E,G,R):-set_s(E,G,R,this@localhost). \n"
                + "get_s(T):-get_s(T,this@localhost). \n"
                + "TC ? uin(T) :- uin(T,TC). \n"
                + "TC ? uinp(T) :- uinp(T,TC). \n"
                + "TC ? urd(T) :- urd(T,TC). \n"
                + "TC ? urdp(T) :- urdp(T,TC). \n"
                + "TC ? uno(T) :- uno(T,TC). \n"
                + "TC ? unop(T) :- unop(T,TC). \n"
                + "TC ? out_all(L) :- out_all(L,TC). \n"
                + "TC ? in_all(T,L) :- in_all(T,L,TC). \n"
                + "TC ? rd_all(T,L) :- rd_all(T,L,TC). \n"
                + "TC ? no_all(T,L) :- no_all(T,L,TC). \n"
                + "urd(T):-urd(T,this@localhost). \n"
                + "urdp(T):-urdp(T,this@localhost). \n"
                + "uin(T):-uin(T,this@localhost). \n"
                + "uinp(T):-uinp(T,this@localhost). \n"
                + "uno(T):-uno(T,this@localhost). \n"
                + "unop(T):-unop(T,this@localhost). \n"
                + "out_all(L):-out_all(L,this@localhost). \n"
                + "in_all(T,L):-in_all(T,L,this@localhost). \n"
                + "rd_all(T,L):-rd_all(T,L,this@localhost). \n"
                + "no_all(T,L):-no_all(T,L,this@localhost). \n"
                + "Env ? getEnv(Key,Value) :- getEnv(Env,Key,Value). \n"
                + "Env ? setEnv(Key,Value) :- setEnv(Env,Key,Value). \n"
                + "completion :- response. \n" + "compl :- response. \n"
                + "resp :- response. \n" + "post :- response. \n"
                + "invocation :- request. \n" + "inv :- request. \n"
                + "req :- request. \n" + "pre :- request. \n"
                + "operation :- from_agent, to_tc. \n"
                + "internal :- from_tc, to_tc, endo, intra. \n"
                + "link_in :- from_tc, to_tc, exo, intra. \n"
                + "link_out :- from_tc, to_tc, endo, inter. \n"
                + "between(T1,T2) :- after(T1), before(T2). \n";
    }

    /**
     *
     * @param arg0
     *            the Prolog variable to unify the result with
     * @param arg1
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg1 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @return <code>true</code> if the operation is successfull
     */
    public boolean in_2(final Term arg0, final Term arg1)
            throws InvalidTupleCentreIdException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg1);
        tcName = tid.getName();
        final LogicTuple tuArg = new LogicTupleDefault(arg0);
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local in triggered...");
            final Tuple tuple = this.vm
                    .removeMatchingTuple(tuArg, true);
            if (tuple != null) {
                final Term term = ((LogicTuple) tuple).toTerm();
                this.unify(arg0, term.copyGoal(v, 0));
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent ev = new InternalEvent(ce,
                        InternalOperation.makeInR(new LogicTupleDefault(arg0.copyGoal(
                                v, 0))));
                ev.setSource(ce.getReactingTC());
                ev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(ev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote in triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeIn(new LogicTupleDefault(arg0.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param arg0
     *            the tuple template to be used
     *
     * @param arg1
     *            the Prolog variable to unify the result with
     * @param arg2
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg2 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @throws InvalidLogicTupleException
     *             if the text does not represent a valid logic tuple
     * @return <code>true</code> if the operation is successfull
     */
    public boolean in_all_3(final Term arg0, final Term arg1, final Term arg2)
            throws InvalidTupleCentreIdException, InvalidLogicTupleException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg2);
        tcName = tid.getName();
        final LogicTuple tuArg = new LogicTupleDefault(arg0);
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local in_all triggered...");
            final List<Tuple> tuples = this.vm
                    .inAllTuples(tuArg);
            if (tuples != null) {
                final Term term = Respect2PLibrary.list2tuple(tuples);
                this.unify(arg1, term.copyGoal(v, 0));
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote in_all triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final String tuple = arg0.getTerm().toString() + "," + arg1;
        LogicTuple resultArg = null;
        resultArg = LogicTupleDefault.parse(tuple);
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeInAll(resultArg, null), tid,
                this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param ev
     *            the triggering event of a ReSpecT specification
     * @param g
     *            the guard of a ReSpecT specification
     * @param r
     *            the body of a ReSpecT specification
     * @param tc
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if tc (tuple centre's id) is not a well-formed ground logic
     *             term
     * @throws InvalidTupleArgumentException
     *             if is not possible to create a valid Prolog Term with the
     *             specified values for ev, g and r
     * @return <code>true</code> if the operation is successfull
     *
     */
    public boolean in_s_4(final Term ev, final Term g, final Term r,
            final Term tc) throws InvalidTupleCentreIdException,
            InvalidTupleArgumentException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(tc);
        tcName = tid.getName();
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        Term goal;
        try {
            goal = Term.createTerm(
                    "reaction(" + ev.getTerm() + "," + g.getTerm() + ","
                            + r.getTerm() + ")", new LogicTupleOpManager());
        } catch (final InvalidTermException e) {
            throw new InvalidTupleArgumentException(
                    "Cannot create a valid Prolog Term with ev: "
                            + ev.getTerm() + "" + ", g: " + g.getTerm()
                            + ", r: " + r.getTerm(), e);
        }
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local in_s triggered...");
            final Term newArg = goal.copyGoal(v, 0);
            final LogicTuple tuArg = new LogicTupleDefault(newArg);
            final Tuple tuple = this.vm
                    .removeMatchingSpecTuple(tuArg);
            if (tuple != null) {
                final Term term = ((LogicTuple) tuple).toTerm();
                this.unify(goal, term.copyGoal(v, 0));
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent iev = new InternalEvent(ce,
                        InternalOperation.makeInSR(new LogicTupleDefault(goal
                                .copyGoal(v, 0))));
                iev.setSource(ce.getReactingTC());
                iev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(iev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote in_s triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeInS(new LogicTupleDefault(goal.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /********************************************************************
     * ReSpecT guard predicates.
     ********************************************************************/
    /**
     *
     * @param m
     *            the ReSpecT VM this tuProlog library interfaces to
     */
    public void init(final RespectVMContext m) {
        this.vm = m;
    }

    /**
     *
     * @param arg0
     *            the Prolog variable to unify the result with
     * @param arg1
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg1 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @return <code>true</code> if the operation is successfull
     */
    public boolean inp_2(final Term arg0, final Term arg1)
            throws InvalidTupleCentreIdException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg1);
        tcName = tid.getName();
        final LogicTuple tuArg = new LogicTupleDefault(arg0);
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local inp triggered...");
            final Tuple tuple = this.vm
                    .removeMatchingTuple(tuArg, true);
            if (tuple != null) {
                final Term term = ((LogicTuple) tuple).toTerm();
                this.unify(arg0, term.copyGoal(v, 0));
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent ev = new InternalEvent(ce,
                        InternalOperation.makeInR(new LogicTupleDefault(arg0.copyGoal(
                                v, 0))));
                ev.setSource(ce.getReactingTC());
                ev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(ev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote inp triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeInp(new LogicTupleDefault(arg0.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param ev
     *            the triggering event of a ReSpecT specification
     * @param g
     *            the guard of a ReSpecT specification
     * @param r
     *            the body of a ReSpecT specification
     * @param tc
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if tc (tuple centre's id) is not a well-formed ground logic
     *             term
     * @throws InvalidTupleArgumentException
     *             if an invalid tuple argument is used
     * @return <code>true</code> if the operation is successfull
     *
     */
    public boolean inp_s_4(final Term ev, final Term g, final Term r,
            final Term tc) throws InvalidTupleCentreIdException,
            InvalidTupleArgumentException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(tc);
        tcName = tid.getName();
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        Term goal;
        try {
            goal = Term.createTerm(
                    "reaction(" + ev.getTerm() + "," + g.getTerm() + ","
                            + r.getTerm() + ")", new LogicTupleOpManager());
        } catch (final InvalidTermException e) {
            throw new InvalidTupleArgumentException(
                    "Cannot create a valid Prolog Term with ev: "
                            + ev.getTerm() + "" + ", g: " + g.getTerm()
                            + ", r: " + r.getTerm(), e);
        }
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local inp_s triggered...");
            final Term newArg = goal.copyGoal(v, 0);
            final LogicTuple tuArg = new LogicTupleDefault(newArg);
            final Tuple tuple = this.vm
                    .removeMatchingSpecTuple(tuArg);
            if (tuple != null) {
                final Term term = ((LogicTuple) tuple).toTerm();
                this.unify(goal, term.copyGoal(v, 0));
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent iev = new InternalEvent(ce,
                        InternalOperation.makeInSR(new LogicTupleDefault(goal
                                .copyGoal(v, 0))));
                iev.setSource(ce.getReactingTC());
                iev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(iev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote inp_s triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeInpS(new LogicTupleDefault(goal.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @return <code>true</code> if the triggering event has not this tuple
     *         centre as target
     */
    public boolean inter_0() {
        return !this.intra_0();
    }

    /**
     *
     * @return <code>true</code> if the triggering event has being generated by
     *         this tc and target to this same tc
     */
    public boolean internal_0() {
    	return this.from_tc_0() && this.to_tc_0() && this.endo_0()
                && this.intra_0();
    }

    /**
     * @return true if the final target of the ReSpecT operation is the
     *         currently reacting tuplecentre.
     */
    public boolean intra_0() {
        final AbstractEvent ev = this.vm.getCurrentReactionEvent();
        final IId target = ev.getTarget();
        if (!target.isTC()) {
            return false;
        }
        final IId currentTc = this.vm.getId();
        if (currentTc.toString().equals(target.toString())) {
            return true;
        }
        if (Respect2PLibrary.checkIP(target)) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return <code>true</code> if the ReSpecT VM is currently in the
     *         invocation phase
     */
    public boolean inv_0() {
        return this.request_0();
    }

    /**
     *
     * @return <code>true</code> if the ReSpecT VM is currently in the
     *         invocation phase
     */
    public boolean invocation_0() {
        return this.request_0();
    }

    /**
     *
     * @return <code>true</code> if the triggering event is coming from a
     *         different tuple centre
     */
    public boolean link_in_0() {
        return this.from_tc_0() && this.to_tc_0() && this.exo_0()
                && this.intra_0();
    }

    /**
     *
     * @return <code>true</code> if the triggering event is originating from
     *         this tuple centre toward a different one
     */
    public boolean link_out_0() {
        return this.from_tc_0() && this.to_tc_0() && this.endo_0()
                && this.inter_0();
    }
    
    /**
     * 
     * @param space
     *            type of node position. It can be specified as either its
     *            absolute physical position (S=ph), its IP number (S=ip), its
     *            domain name (S=dns), its geographical location (S=map), or its
     *            organisational position (S=org).
     * @param center
     *            the center point of the spatial region.
     * @param radius
     *            the radius of the spatial ragion (in meters).
     * @return <code>true</code> if the tuple centre is currently executing at
     *         the position included in the spatial region with the given center
     *         and radius, specified according to the given space term.
     */
    public boolean near_3(final Term space, final Term center, final Term radius) {
        final Position vmPosition = this.vm.getPosition();
        final Term vmPosTerm = vmPosition.getPlace(space).toTerm();
        if (this.unify(space, Term.createTerm(Position.PH))) {
            final Struct vmPosStruct = (Struct) vmPosTerm;
            final Struct centerStruct = (Struct) center;
            if (!"coords".equals(centerStruct.getName())) {
                return false;
            }
            final float vmX = ((alice.tuprolog.Number) vmPosStruct.getArg(0))
                    .floatValue();
            final float vmY = ((alice.tuprolog.Number) vmPosStruct.getArg(1))
                    .floatValue();
            final float cX = ((alice.tuprolog.Number) centerStruct.getArg(0))
                    .floatValue();
            final float cY = ((alice.tuprolog.Number) centerStruct.getArg(1))
                    .floatValue();
            final float radiusN = GeoUtils
                    .toDegrees(((alice.tuprolog.Number) radius).floatValue());
            return Math.pow(vmX - cX, 2) + Math.pow(vmY - cY, 2) <= Math.pow(
                    radiusN, 2); // check if the current node position is
                                 // inside a circle having given center and
                                 // radius
        } else if (this.unify(space, Term.createTerm(Position.MAP))) {
            final GeolocationServiceManager geolocationManager = GeolocationServiceManager
                    .getGeolocationManager();
            if (geolocationManager.getServices().size() > 0) {
                final int platform = PlatformUtils.getPlatform();
                final AbstractGeolocationService geoService = GeolocationServiceManager
                        .getGeolocationManager().getAppositeService(platform);
                if (geoService != null) {
                    final Term centerCoords = geoService.geocode(Tools
                            .removeApices(center.toString()));
                    return this.near_3(Term.createTerm("ph"), centerCoords,
                            radius);
                }
            }
        } else if (this.unify(space, Term.createTerm(Position.ORG))) {
            final String orgCenterS = Tools.removeApices(center.toString());
            final String[] orgCenterParts = orgCenterS.split("at ");
            return this.near_3(Term.createTerm("map"),
                    Term.createTerm("'" + orgCenterParts[1] + "'"), radius);
        } else if (this.unify(space, Term.createTerm(Position.IP))) {
            final float radiusN = ((alice.tuprolog.Number) radius).floatValue();
            final String vmIpS = Tools.removeApices(vmPosTerm.toString());
            final String vmIp = NetworkUtils.getIp(vmIpS);
            // String vmIp = NetworkUtils.getIp("192.168.0.5/26");
            final int vmMask = NetworkUtils.getNetmask(vmIpS);
            NetworkUtils.getDecimalNetmask(vmIpS);
            final String centerIpS = Tools.removeApices(center.toString());
            final String centerIp = NetworkUtils.getIp(centerIpS);
            final int mask = NetworkUtils.getNetmask(centerIpS);
            final String decMask = NetworkUtils.getDecimalNetmask(centerIpS);
            if (vmMask <= mask + radiusN && vmMask >= mask - radiusN) {
                return NetworkUtils.sameNetwork(vmIp, centerIp, decMask);
            }
            return false;
        } else if (this.unify(space, Term.createTerm(Position.DNS))) {
            final float radiusN = ((alice.tuprolog.Number) radius).floatValue();
            final String vmDnsS = Tools.removeApices(vmPosTerm.toString());
            // String vmDnsS = "prova2.ciccio.fai.unibo.it";
            final String centerDnsS = Tools.removeApices(center.toString());
            if (radiusN == 0) {
                return vmDnsS.equals(centerDnsS);
            }
            final String[] vmDnsParts = vmDnsS.split("\\.");
            final String[] centerDnsParts = centerDnsS.split("\\.");
            StringBuffer toCheck = new StringBuffer(centerDnsParts.length);
            for (int i = centerDnsParts.length - 1; i >= 0; i--) {
                for (int j = i; j < centerDnsParts.length; j++) {
                    toCheck.append(centerDnsParts[j]
                            + (j < centerDnsParts.length - 1 ? "." : ""));
                }
                if (vmDnsS.contains(toCheck)) {
                    for (int k = (int) radiusN; k > 0; k--) {
                        if (vmDnsParts[k].equals(centerDnsParts[i])) {
                            return true;
                        }
                    }
                }
                toCheck = new StringBuffer();
            }
        }
        return false;
    }

    /**
     *
     * @param arg0
     *            the Prolog variable to unify the result with
     * @param arg1
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg1 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @return <code>true</code> if the operation is successfull
     */
    public boolean no_2(final Term arg0, final Term arg1)
            throws InvalidTupleCentreIdException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg1);
        tcName = tid.getName();
        final LogicTuple tuArg = new LogicTupleDefault(arg0);
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local no triggered...");
            final Tuple tuple = this.vm
                    .readMatchingTuple(tuArg);
            if (tuple == null) {
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent ev = new InternalEvent(ce,
                        InternalOperation.makeNoR(new LogicTupleDefault(arg0.copyGoal(
                                v, 0))));
                ev.setSource(ce.getReactingTC());
                ev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(ev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote no triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeNo(new LogicTupleDefault(arg0.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param arg0
     *            the tuple template to be used
     *
     * @param arg1
     *            the Prolog variable to unify the result with
     * @param arg2
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg2 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @throws InvalidLogicTupleException
     *             if the text does not represent a valid logic tuple
     * @return <code>true</code> if the operation is successfull
     */
    public boolean no_all_3(final Term arg0, final Term arg1, final Term arg2)
            throws InvalidTupleCentreIdException, InvalidLogicTupleException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg2);
        tcName = tid.getName();
        final LogicTuple tuArg = new LogicTupleDefault(arg0);
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local no_all triggered...");
            final List<Tuple> tuples = this.vm
                    .readAllTuples(tuArg);
            if (tuples == null) {
                final Term term = Respect2PLibrary.list2tuple(tuples);
                this.unify(arg1, term.copyGoal(v, 0));
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote no_all triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final String tuple = arg0.getTerm().toString() + "," + arg1;
        LogicTuple resultArg = null;
        resultArg = LogicTupleDefault.parse(tuple);
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeNoAll(resultArg, null), tid,
                this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param ev
     *            the triggering event of a ReSpecT specification
     * @param g
     *            the guard of a ReSpecT specification
     * @param r
     *            the body of a ReSpecT specification
     * @param tc
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if tc (tuple centre's id) is not a well-formed ground logic
     *             term
     * @throws InvalidTupleArgumentException
     *             if is not possible to create a valid Prolog Term with the
     *             specified values for ev, g and r
     * @return <code>true</code> if the operation is successfull
     *
     */
    public boolean no_s_4(final Term ev, final Term g, final Term r,
            final Term tc) throws InvalidTupleCentreIdException,
            InvalidTupleArgumentException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(tc);
        tcName = tid.getName();
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        Term goal;
        try {
            goal = Term.createTerm(
                    "reaction(" + ev.getTerm() + "," + g.getTerm() + ","
                            + r.getTerm() + ")", new LogicTupleOpManager());
        } catch (final InvalidTermException e) {
            throw new InvalidTupleArgumentException(
                    "Cannot create a valid Prolog Term with ev: "
                            + ev.getTerm() + "" + ", g: " + g.getTerm()
                            + ", r: " + r.getTerm(), e);
        }
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local no_s triggered...");
            final Term newArg = goal.copyGoal(v, 0);
            final LogicTuple tuArg = new LogicTupleDefault(newArg);
            final Tuple tuple = this.vm
                    .readMatchingSpecTuple(tuArg);
            if (tuple == null) {
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent iev = new InternalEvent(ce,
                        InternalOperation.makeNoSR(new LogicTupleDefault(goal
                                .copyGoal(v, 0))));
                iev.setSource(ce.getReactingTC());
                iev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(iev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote no_s triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeNoS(new LogicTupleDefault(goal.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param arg0
     *            the Prolog variable to unify the result with
     * @param arg1
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg1 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @return <code>true</code> if the operation is successfull
     */
    public boolean nop_2(final Term arg0, final Term arg1)
            throws InvalidTupleCentreIdException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg1);
        tcName = tid.getName();
        final LogicTuple tuArg = new LogicTupleDefault(arg0);
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local nop triggered...");
            final Tuple tuple = this.vm
                    .readMatchingTuple(tuArg);
            if (tuple == null) {
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent ev = new InternalEvent(ce,
                        InternalOperation.makeNoR(new LogicTupleDefault(arg0.copyGoal(
                                v, 0))));
                ev.setSource(ce.getReactingTC());
                ev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(ev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote nop triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeNop(new LogicTupleDefault(arg0.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param ev
     *            the triggering event of a ReSpecT specification
     * @param g
     *            the guard of a ReSpecT specification
     * @param r
     *            the body of a ReSpecT specification
     * @param tc
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if tc (tuple centre's id) is not a well-formed ground logic
     *             term
     * @throws InvalidTupleArgumentException
     *             if is not possible to create a valid Prolog Term with the
     *             specified values for ev, g and r
     * @return <code>true</code> if the operation is successfull
     *
     */
    public boolean nop_s_4(final Term ev, final Term g, final Term r,
            final Term tc) throws InvalidTupleCentreIdException,
            InvalidTupleArgumentException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(tc);
        tcName = tid.getName();
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        Term goal;
        try {
            goal = Term.createTerm(
                    "reaction(" + ev.getTerm() + "," + g.getTerm() + ","
                            + r.getTerm() + ")", new LogicTupleOpManager());
        } catch (final InvalidTermException e) {
            throw new InvalidTupleArgumentException(
                    "Cannot create a valid Prolog Term with ev: "
                            + ev.getTerm() + "" + ", g: " + g.getTerm()
                            + ", r: " + r.getTerm(), e);
        }
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local nop_s triggered...");
            final Term newArg = goal.copyGoal(v, 0);
            final LogicTuple tuArg = new LogicTupleDefault(newArg);
            final Tuple tuple = this.vm
                    .readMatchingSpecTuple(tuArg);
            if (tuple == null) {
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent iev = new InternalEvent(ce,
                        InternalOperation.makeNoSR(new LogicTupleDefault(goal
                                .copyGoal(v, 0))));
                iev.setSource(ce.getReactingTC());
                iev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(iev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote nop_s triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeNopS(new LogicTupleDefault(goal.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /********************************************************************
     * ReSpecT composite guard predicates.
     ********************************************************************/
    /**
     *
     * @return <code>true</code> if the triggering event comes from an agent and
     *         is directed toward a tuple centre
     */
    public boolean operation_0() {
        return this.from_agent_0() && this.to_tc_0();
    }

    /********************************************************************
     * ReSpecT primitives provided as Java methods.
     ********************************************************************/
    /**
     *
     * @param arg0
     *            the tuple to inject in the tuple centre
     * @param arg1
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg1 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @return <code>true</code> if the operation is successfull
     */
    public boolean out_2(final Term arg0, final Term arg1)
            throws InvalidTupleCentreIdException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg1);
        tcName = tid.getName();
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local out triggered...");
            final Term newArg = arg0.copyGoal(v, 0);
            final LogicTuple tuArg = new LogicTupleDefault(newArg);
            this.vm.addTuple(tuArg, true);
            final InputEvent ce = this.vm.getCurrentEvent();
            final InternalEvent ev = new InternalEvent(ce,
                    InternalOperation.makeOutR(new LogicTupleDefault(arg0.copyGoal(v,
                            0))));
            ev.setSource(ce.getReactingTC());
            ev.setTarget(ce.getReactingTC());
            this.vm.fetchTriggeredReactions(ev);
            return true;
        }
        Respect2PLibrary.log("Remote out triggered...");
        // final InputEvent ce = this.vm.getCurrentEvent();
        // TupleCentreId newTid = null;
        // try {
        // newTid =
        // new TupleCentreId(Term.createTerm(alice.util.Tools
        // .removeApices(((Struct) arg1.getTerm()).getArg(0)
        // .getTerm().toString()), new MyOpManager()));
        // } catch (final InvalidTupleCentreIdException e) {
        // e.printStackTrace();
        // return false;
        // }
        // final InputEvent outEv =
        // new InputEvent(ce.getReactingTC(), RespectOperationDefault.makeOut(
        // this.getProlog(), new LogicTuple(arg0.copyGoal(v, 0)),
        // null), newTid, this.vm.getCurrentTime());
        // outEv.setIsLinking(true);
        // outEv.setTarget(newTid);
        // this.vm.addTemporaryOutputEvent(outEv);
        // return true;
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeOut(new LogicTupleDefault(arg0.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param arg0
     *            the list of tuples to injectin the tuple centre
     * @param arg1
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg1 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @return <code>true</code> if the operation is successfull
     */
    public boolean out_all_2(final Term arg0, final Term arg1)
            throws InvalidTupleCentreIdException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg1);
        tcName = tid.getName();
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local out_all triggered...");
            final Term newArg = arg0.copyGoal(v, 0);
            final LogicTuple tuArg = new LogicTupleDefault(newArg);
            this.vm.addListTuple(tuArg);
            final InputEvent ce = this.vm.getCurrentEvent();
            final InternalEvent ev = new InternalEvent(ce,
                    InternalOperation.makeOutAllR(new LogicTupleDefault(arg0.copyGoal(
                            v, 0))));
            ev.setSource(ce.getReactingTC());
            ev.setTarget(ce.getReactingTC());
            this.vm.fetchTriggeredReactions(ev);
            return true;
        }
        Respect2PLibrary.log("Remote out_all triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeOutAll(
                        new LogicTupleDefault(arg0.copyGoal(v, 0)), null), tid,
                this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param ev
     *            the triggering event of a ReSpecT specification
     * @param g
     *            the guard of a ReSpecT specification
     * @param r
     *            the body of a ReSpecT specification
     * @param tc
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if tc (tuple centre's id) is not a well-formed ground logic
     *             term
     * @throws InvalidTupleArgumentException
     *             if is not possible to create a valid Prolog Term with the
     *             specified values for ev, g and r
     * @return <code>true</code> if the operation is successfull
     *
     */
    public boolean out_s_4(final Term ev, final Term g, final Term r,
            final Term tc) throws InvalidTupleCentreIdException,
            InvalidTupleArgumentException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(tc);
        tcName = tid.getName();
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        Term goal;
        try {
            goal = Term.createTerm(
                    "reaction(" + ev.getTerm() + "," + g.getTerm() + ","
                            + r.getTerm() + ")", new LogicTupleOpManager());
        } catch (final InvalidTermException e) {
            throw new InvalidTupleArgumentException(
                    "Cannot create a valid Prolog Term with ev: "
                            + ev.getTerm() + "" + ", g: " + g.getTerm()
                            + ", r: " + r.getTerm(), e);
        }
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local out_s triggered...");
            final Term newArg = goal.copyGoal(v, 0);
            final LogicTuple tuArg = new LogicTupleDefault(newArg);
            this.vm.addSpecTuple(tuArg);
            final InputEvent ce = this.vm.getCurrentEvent();
            final InternalEvent iev = new InternalEvent(ce,
                    InternalOperation.makeOutSR(new LogicTupleDefault(goal.copyGoal(v,
                            0))));
            iev.setSource(ce.getReactingTC());
            iev.setTarget(ce.getReactingTC());
            this.vm.fetchTriggeredReactions(iev);
            return true;
        }
        Respect2PLibrary.log("Remote out_s triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeOutS(new LogicTupleDefault(goal.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @return <code>true</code> if the ReSpecT VM is in the completion phase
     */
    public boolean post_0() {
        return this.response_0();
    }

    /**
     *
     * @return <code>true</code> if the ReSpecT VM is in the invocation phase
     */
    public boolean pre_0() {
        return this.request_0();
    }

    /**
     *
     * @param arg0
     *            the Prolog variable to unify the result with
     * @param arg1
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg1 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @return <code>true</code> if the operation is successfull
     */
    public boolean rd_2(final Term arg0, final Term arg1)
            throws InvalidTupleCentreIdException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg1);
        tcName = tid.getName();
        final LogicTuple tuArg = new LogicTupleDefault(arg0);
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local rd triggered...");
            final Tuple tuple = this.vm
                    .readMatchingTuple(tuArg);
            if (tuple != null) {
                final Term term = ((LogicTuple) tuple).toTerm();
                this.unify(arg0, term.copyGoal(v, 0));
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent ev = new InternalEvent(ce,
                        InternalOperation.makeRdR(new LogicTupleDefault(arg0.copyGoal(
                                v, 0))));
                ev.setSource(ce.getReactingTC());
                ev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(ev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote rd triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeRd(new LogicTupleDefault(arg0.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param arg0
     *            the tuple template to be used
     *
     * @param arg1
     *            the Prolog variable to unify the result with
     * @param arg2
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg2 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @throws InvalidLogicTupleException
     *             if the text does not represent a valid logic tuple
     * @return <code>true</code> if the operation is successfull
     */
    public boolean rd_all_3(final Term arg0, final Term arg1, final Term arg2)
            throws InvalidTupleCentreIdException, InvalidLogicTupleException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg2);
        tcName = tid.getName();
        final LogicTuple tuArg = new LogicTupleDefault(arg0);
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local rd_all triggered...");
            final List<Tuple> tuples = this.vm
                    .readAllTuples(tuArg);
            if (tuples != null) {
                final Term term = Respect2PLibrary.list2tuple(tuples);
                this.unify(arg1, term.copyGoal(v, 0));
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote rd_all triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final String tuple = arg0.getTerm().toString() + "," + arg1;
        LogicTuple resultArg = null;
        resultArg = LogicTupleDefault.parse(tuple);
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeRdAll(resultArg, null), tid,
                this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param ev
     *            the triggering event of a ReSpecT specification
     * @param g
     *            the guard of a ReSpecT specification
     * @param r
     *            the body of a ReSpecT specification
     * @param tc
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if tc (tuple centre's id) is not a well-formed ground logic
     *             term
     * @throws InvalidTupleArgumentException
     *             if is not possible to create a valid Prolog Term with the
     *             specified values for ev, g and r
     * @return <code>true</code> if the operation is successfull
     *
     */
    public boolean rd_s_4(final Term ev, final Term g, final Term r,
            final Term tc) throws InvalidTupleCentreIdException,
            InvalidTupleArgumentException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(tc);
        tcName = tid.getName();
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        Term goal;
        try {
            goal = Term.createTerm(
                    "reaction(" + ev.getTerm() + "," + g.getTerm() + ","
                            + r.getTerm() + ")", new LogicTupleOpManager());
        } catch (final InvalidTermException e) {
            throw new InvalidTupleArgumentException(
                    "Cannot create a valid Prolog Term with ev: "
                            + ev.getTerm() + "" + ", g: " + g.getTerm()
                            + ", r: " + r.getTerm(), e);
        }
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local rd_s triggered...");
            final Term newArg = goal.copyGoal(v, 0);
            final LogicTuple tuArg = new LogicTupleDefault(newArg);
            final Tuple tuple = this.vm
                    .readMatchingSpecTuple(tuArg);
            if (tuple != null) {
                final Term term = ((LogicTuple) tuple).toTerm();
                this.unify(goal, term.copyGoal(v, 0));
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent iev = new InternalEvent(ce,
                        InternalOperation.makeRdSR(new LogicTupleDefault(goal
                                .copyGoal(v, 0))));
                iev.setSource(ce.getReactingTC());
                iev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(iev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote rd_s triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeRdS(new LogicTupleDefault(goal.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param arg0
     *            the Prolog variable to unify the result with
     * @param arg1
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg1 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @return <code>true</code> if the operation is successfull
     */
    public boolean rdp_2(final Term arg0, final Term arg1)
            throws InvalidTupleCentreIdException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg1);
        tcName = tid.getName();
        final LogicTuple tuArg = new LogicTupleDefault(arg0);
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local rdp triggered...");
            final Tuple tuple = this.vm
                    .readMatchingTuple(tuArg);
            if (tuple != null) {
                final Term term = ((LogicTuple) tuple).toTerm();
                this.unify(arg0, term.copyGoal(v, 0));
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent ev = new InternalEvent(ce,
                        InternalOperation.makeRdR(new LogicTupleDefault(arg0.copyGoal(
                                v, 0))));
                ev.setSource(ce.getReactingTC());
                ev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(ev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote rdp triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeRdp(new LogicTupleDefault(arg0.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /********************************************************************
     * ReSpecT reaction observation predicates.
     ********************************************************************/
    /**
     *
     * @param ev
     *            the triggering event of a ReSpecT specification
     * @param g
     *            the guard of a ReSpecT specification
     * @param r
     *            the body of a ReSpecT specification
     * @param tc
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if tc (tuple centre's id) is not a well-formed ground logic
     *             term
     * @throws InvalidTupleArgumentException
     *             if is not possible to create a valid Prolog Term with the
     *             specified values for ev, g and r
     * @return <code>true</code> if the operation is successfull
     *
     */
    public boolean rdp_s_4(final Term ev, final Term g, final Term r,
            final Term tc) throws InvalidTupleCentreIdException,
            InvalidTupleArgumentException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(tc);
        tcName = tid.getName();
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        Term goal;
        try {
            goal = Term.createTerm(
                    "reaction(" + ev.getTerm() + "," + g.getTerm() + ","
                            + r.getTerm() + ")", new LogicTupleOpManager());
        } catch (final InvalidTermException e) {
            throw new InvalidTupleArgumentException(
                    "Cannot create a valid Prolog Term with ev: "
                            + ev.getTerm() + "" + ", g: " + g.getTerm()
                            + ", r: " + r.getTerm(), e);
        }
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local rdp_s triggered...");
            final Term newArg = goal.copyGoal(v, 0);
            final LogicTuple tuArg = new LogicTupleDefault(newArg);
            final Tuple tuple = this.vm
                    .readMatchingSpecTuple(tuArg);
            if (tuple != null) {
                final Term term = ((LogicTuple) tuple).toTerm();
                this.unify(goal, term.copyGoal(v, 0));
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent iev = new InternalEvent(ce,
                        InternalOperation.makeRdSR(new LogicTupleDefault(goal
                                .copyGoal(v, 0))));
                iev.setSource(ce.getReactingTC());
                iev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(iev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote rdp_s triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeRdpS(new LogicTupleDefault(goal.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @return <code>true</code> if the ReSpecT VM is in the invocation phase
     */
    public boolean req_0() {
        return this.request_0();
    }

    /**
     * @return true if the ReSpecT VM is in the 'invocation phase'.
     */
    public boolean request_0() {
        final AbstractEvent ev = this.vm.getCurrentReactionEvent();
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        return !op.isResultDefined();
    }

    /**
     *
     * @return <code>true</code> if the ReSpecT VM is in the completion phase
     */
    public boolean resp_0() {
        return this.response_0();
    }

    /**
     * @return true if the ReSpecT VM is in the 'completion phase'.
     */
    public boolean response_0() {
        final AbstractEvent ev = this.vm.getCurrentReactionEvent();
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        return op.isResultDefined()
                && !"ListeningState".equals(this.vm.getCurrentState());
    }

    /**
     *
     * @param env
     *            the identifier of the target environmental resource
     * @param key
     *            the environmental property to modify
     * @param val
     *            the value modified
     * @throws OperationTimeOutException
     *             if the notification operation expires timeout
     * @throws UnreachableNodeException
     *             if the TuCSoN tuple centre target of the notification cannot
     *             be reached over the network
     * @throws TucsonOperationNotPossibleException
     *             if the requested operation cannot be performed for some
     *             reason
     * @return <code>true</code> if the operation is successfull
     */
    public boolean setEnv_3(final Term env, final Term key, final Term val)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        // Get engine's copy of key and val
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        final AbstractMap<Var, Var> v1 = new LinkedHashMap<Var, Var>();
        final LogicTuple lt = new LogicTupleDefault("setEnv", new TupleArgument(
                key.copyGoal(v, 0)), new TupleArgument(val.copyGoal(v1, 0)));
        // Building internal event
        final InputEvent ev = this.vm.getCurrentEvent();
        // log("### DEBUG >>> ev = " + ev);
        final InternalEvent internalEv = new InternalEvent(ev,
                InternalOperation.makeSetEnv(lt));
        // log("### DEBUG >>> iev = " + internalEv);
        final String normEnv = env.toString().substring(
                env.toString().indexOf("(") + 1, env.toString().indexOf(","));
        final EnvId envId = new EnvId(normEnv);
        internalEv.setTarget(envId);
        // log("### DEBUG >>> target = " + envId);
        internalEv.setSource(this.vm.getId());
        final TransducersManager tm = TransducersManager.INSTANCE;
        // Getting the transducer from the transducer manager
        final TransducerId tId = tm.getTransducerId(envId);
        if (tm.getTransducer(tId.getAgentName()).notifyOutput(internalEv)) {
            this.vm.fetchTriggeredReactions(internalEv);
            return true;
        }
        return false;
    }

    /**
     *
     * @param arg0
     *            the Java class full name or tuProlog theory file path to spawn
     * @param arg1
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg1 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @return <code>true</code> if the operation is successfull
     */
    public boolean spawn_2(final Term arg0, final Term arg1)
            throws InvalidTupleCentreIdException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg1);
        tcName = tid.getName();
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local spawn triggered...");
            final Term newArg = arg0.copyGoal(v, 0);
            final LogicTuple tuArg = new LogicTupleDefault(newArg);
            final InputEvent ce = this.vm.getCurrentEvent();
            this.vm.spawnActivity(tuArg, ce.getReactingTC(), ce.getReactingTC());
            final InternalEvent ev = new InternalEvent(ce,
                    InternalOperation.makeSpawnR(new LogicTupleDefault(arg0.copyGoal(
                            v, 0))));
            ev.setSource(ce.getReactingTC());
            ev.setTarget(ce.getReactingTC());
            this.vm.fetchTriggeredReactions(ev);
            return true;
        }
        Respect2PLibrary.log("Remote spawn triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeSpawn(new LogicTupleDefault(arg0.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }
    
    /**
     * 
     * @param space
     *            type of node position. It can be specified as either its
     *            absolute physical position (S=ph), its IP number (S=ip), its
     *            domain name (S=dns), its geographical location (S=map), or its
     *            organisational position (S=org).
     * @param place
     *            the expected position.
     * @return <code>true</code> if the given position, specified according to
     *         the given space term, unifies with the position of the node where
     *         the event chain that led to the triggering event was originated.
     */
    public boolean start_place_2(final Term space, final Term place) {
        /**
         * start fa riferimento alla causa prima, dunque servir� "sempre" (pu�
         * non essere vero, ma per ora fingiamo di si) la posizione dell'agente.
         */
        /*
         * FindBugs states e can only be an InputEvent (and it seems so by
         * navigating the type hierarchy)
         */
//         final AbstractEvent e = this.vm.getCurrentEvent();
//         Term startEvPosTerm = null;
//         if (e.isInternal()) {
//         final InternalEvent ie = (InternalEvent) e;
//         startEvPosTerm =
//         ie.getInputEvent().getPosition().getPlace(space).toTerm();
//         }
//         if (e.isOutput()) {
//         final OutputEvent oe = (OutputEvent) e;
//         startEvPosTerm =
//         oe.getInputEvent().getPosition().getPlace(space).toTerm();
//         }
    	
        final Term startEvPosTerm = this.vm.getCurrentEvent().getPosition()
                .getPlace(space).toTerm();
        return this.unify(place, startEvPosTerm);
    }

    /**
     *
     * @param predicate
     *            the predicate prime cause of current ReSpecT event
     * @return <code>true</code> if the current ReSpecT event has the given
     *         prime cause
     */
    public boolean start_predicate_1(final Term predicate) {
        final AbstractEvent e = this.vm.getCurrentReactionEvent();
        if (e.isInternal()) {
            final InternalEvent ie = (InternalEvent) e;
            return LogicMatchingEngine.propagate(new LogicTupleDefault(predicate),
                    (LogicTuple) ie.getInputEvent().getSimpleTCEvent()
                            .getPredicate());
        }
        if (e.isOutput()) {
            final OutputEvent oe = (OutputEvent) e;
            return LogicMatchingEngine.propagate(new LogicTupleDefault(predicate),
                    (LogicTuple) oe.getInputEvent().getSimpleTCEvent()
                            .getPredicate());
        }
        return this
                .unify(predicate, this.vm.getCurrentReactionTerm().getTerm());
    }

    /**
     *
     * @param source
     *            the identifier of the prime cause source of the event
     *
     * @return <code>true</code> if current ReSpecT event prime cause has the
     *         given source
     */
    public boolean start_source_1(final Term source) {
        final AbstractEvent e = this.vm.getCurrentReactionEvent();
        if (e.isInternal()) {
            final InternalEvent ie = (InternalEvent) e;
            final IId id = ie.getInputEvent().getSource();
            if (id.isAgent()) {
                // final Term aid = ((AgentId) id).toTerm();
                final Term aid = Term.createTerm(id.toString(),
                        new LogicTupleOpManager());
                return this.unify(source, aid);
            } else if (id.isTC()) {
                // final Term tcid = ((TupleCentreId) id).toTerm();
                final Term tcid = Term.createTerm(id.toString(),
                        new LogicTupleOpManager());
                return this.unify(source, tcid);
            } else {
                return false;
            }
        } else if (e.isOutput()) {
            final OutputEvent oe = (OutputEvent) e;
            final IId id = oe.getInputEvent().getSource();
            if (id.isAgent()) {
                // final Term aid = ((AgentId) id).toTerm();
                final Term aid = Term.createTerm(id.toString(),
                        new LogicTupleOpManager());
                return this.unify(source, aid);
            } else if (id.isTC()) {
                // final Term tcid = ((TupleCentreId) id).toTerm();
                final Term tcid = Term.createTerm(id.toString(),
                        new LogicTupleOpManager());
                return this.unify(source, tcid);
            } else {
                return false;
            }
        } else {
            final IId id = e.getSource();
            if (id.isAgent()) {
                // final Term aid = ((AgentId) id).toTerm();
                final Term aid = Term.createTerm(id.toString(),
                        new LogicTupleOpManager());
                return this.unify(source, aid);
            } else if (id.isTC()) {
                // final Term tcid = ((TupleCentreId) id).toTerm();
                final Term tcid = Term.createTerm(id.toString(),
                        new LogicTupleOpManager());
                return this.unify(source, tcid);
            } else {
                return false;
            }
        }
    }

    /**
     *
     * @param target
     *            the identifier of the prime cause target
     * @return <code>true</code> if current ReSpecT event prime cause has the
     *         given target
     */
    public boolean start_target_1(final Term target) {
        final AbstractEvent e = this.vm.getCurrentReactionEvent();
        if (e.isInternal()) {
            final InternalEvent ie = (InternalEvent) e;
            final IId id = ie.getInputEvent().getTarget();
            if (id.isAgent()) {
                // final Term aid = ((AgentId) id).toTerm();
                final Term aid = Term.createTerm(id.toString(),
                        new LogicTupleOpManager());
                return this.unify(target, aid);
            } else if (id.isTC()) {
                // final Term tcid = ((TupleCentreId) id).toTerm();
                final Term tcid = Term.createTerm(id.toString(),
                        new LogicTupleOpManager());
                return this.unify(target, tcid);
            } else {
                return false;
            }
        } else if (e.isOutput()) {
            final OutputEvent oe = (OutputEvent) e;
            final IId id = oe.getInputEvent().getTarget();
            if (id.isAgent()) {
                // final Term aid = ((AgentId) id).toTerm();
                final Term aid = Term.createTerm(id.toString(),
                        new LogicTupleOpManager());
                return this.unify(target, aid);
            } else if (id.isTC()) {
                // final Term tcid = ((TupleCentreId) id).toTerm();
                final Term tcid = Term.createTerm(id.toString(),
                        new LogicTupleOpManager());
                return this.unify(target, tcid);
            } else {
                return false;
            }
        } else {
            final IId id = e.getTarget();
            if (id.isAgent()) {
                // final Term aid = ((AgentId) id).toTerm();
                final Term aid = Term.createTerm(id.toString(),
                        new LogicTupleOpManager());
                return this.unify(target, aid);
            } else if (id.isTC()) {
                // final Term tcid = ((TupleCentreId) id).toTerm();
                final Term tcid = Term.createTerm(id.toString(),
                        new LogicTupleOpManager());
                return this.unify(target, tcid);
            } else {
                return false;
            }
        }
    }

    /**
     *
     * @param time
     *            the expected time at which the current ReSpecT computation has
     *            been triggered.
     *
     * @return true if the actual time at which the current ReSpecT computation
     *         has been triggered is the expected time at which the current
     *         ReSpecT computation has been triggered.
     */
    public boolean start_time_1(final Term time) {
        final AbstractEvent e = this.vm.getCurrentReactionEvent();
        if (e.isInternal()) {
            final InternalEvent ie = (InternalEvent) e;
            return this.unify(time, new alice.tuprolog.Long(ie.getInputEvent()
                    .getTime()));
        }
        if (e.isOutput()) {
            final OutputEvent oe = (OutputEvent) e;
            return this.unify(time, new alice.tuprolog.Long(oe.getInputEvent()
                    .getTime()));
        }
        return this.unify(time, new alice.tuprolog.Long(e.getTime()));
    }

    /**
     *
     * @param tuple
     *            the tuple argument of the direct cause predicate
     *
     * @return <code>true</code> if the direct cause tuple argument has the
     *         given tuple
     */
    public boolean start_tuple_1(final Term tuple) {
        final AbstractEvent e = this.vm.getCurrentReactionEvent();
        if (e.isInternal()) {
            final InternalEvent ie = (InternalEvent) e;
            return this.unify(tuple,
                    Term.createTerm(ie.getInputEvent().getTuple().toString()));
        }
        if (e.isOutput()) {
            final OutputEvent oe = (OutputEvent) e;
            return this.unify(tuple,
                    Term.createTerm(oe.getInputEvent().getTuple().toString()));
        }
        return this.unify(tuple, Term.createTerm(e.getTuple().toString()));
    }

    /**
     * @return true if the ReSpecT operation completed successfully.
     */
    public boolean success_0() {
        final AbstractEvent ev = this.vm.getCurrentReactionEvent();
        final TupleCentreOperation op = ev.getSimpleTCEvent();
        return op.isResultSuccess()
                && !"ListeningState".equals(this.vm.getCurrentState());
    }

    /**
     * @return true if the final target of the ReSpecT operation is an agent
     *         (either Java or tuProlog or whatever).
     */
    public boolean to_agent_0() {
        final AbstractEvent ev = this.vm.getCurrentReactionEvent();
        final IId target = ev.getTarget();
        if (target.isAgent()) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return <code>true</code> if the triggering event was directed toward the
     *         environment
     */
    public boolean to_env_0() {
        return this.vm.getCurrentReactionEvent().getTarget().isEnv();
    }

    /**
     * @return true if the final target of the ReSpecT operation is a ReSpecT
     *         tuplecentre.
     */
    public boolean to_tc_0() {
        final AbstractEvent ev = this.vm.getCurrentReactionEvent();
        final IId target = ev.getTarget();
        if (target.isTC()) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param arg0
     *            the Prolog variable to unify the result with
     * @param arg1
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg1 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @return <code>true</code> if the operation is successfull
     */
    public boolean uin_2(final Term arg0, final Term arg1)
            throws InvalidTupleCentreIdException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg1);
        tcName = tid.getName();
        final LogicTuple tuArg = new LogicTupleDefault(arg0);
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local uin triggered...");
            final Tuple tuple = this.vm
                    .removeUniformTuple(tuArg);
            if (tuple != null) {
                final Term term = ((LogicTuple) tuple).toTerm();
                this.unify(arg0, term.copyGoal(v, 0));
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent ev = new InternalEvent(ce,
                        InternalOperation.makeUinR(new LogicTupleDefault(arg0
                                .copyGoal(v, 0))));
                ev.setSource(ce.getReactingTC());
                ev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(ev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote uin triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeUin(new LogicTupleDefault(arg0.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param arg0
     *            the Prolog variable to unify the result with
     * @param arg1
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg1 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @return <code>true</code> if the operation is successfull
     */
    public boolean uinp_2(final Term arg0, final Term arg1)
            throws InvalidTupleCentreIdException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg1);
        tcName = tid.getName();
        final LogicTuple tuArg = new LogicTupleDefault(arg0);
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local uinp triggered...");
            final Tuple tuple = this.vm
                    .removeUniformTuple(tuArg);
            if (tuple != null) {
                final Term term = ((LogicTuple) tuple).toTerm();
                this.unify(arg0, term.copyGoal(v, 0));
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent ev = new InternalEvent(ce,
                        InternalOperation.makeUinR(new LogicTupleDefault(arg0
                                .copyGoal(v, 0))));
                ev.setSource(ce.getReactingTC());
                ev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(ev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote uinp triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeUinp(new LogicTupleDefault(arg0.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param arg0
     *            the Prolog variable to unify the result with
     * @param arg1
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg1 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @return <code>true</code> if the operation is successfull
     */
    public boolean uno_2(final Term arg0, final Term arg1)
            throws InvalidTupleCentreIdException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg1);
        tcName = tid.getName();
        final LogicTuple tuArg = new LogicTupleDefault(arg0);
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local uno triggered...");
            final Tuple tuple = this.vm
                    .readUniformTuple(tuArg);
            if (tuple == null) {
                Respect2PLibrary.log("uno success");
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent ev = new InternalEvent(ce,
                        InternalOperation.makeUnoR(new LogicTupleDefault(arg0
                                .copyGoal(v, 0))));
                ev.setSource(ce.getReactingTC());
                ev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(ev);
                return true;
            }
            Respect2PLibrary.log("uno failure");
            return false;
        }
        Respect2PLibrary.log("Remote uno triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeUno(new LogicTupleDefault(arg0.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param arg0
     *            the Prolog variable to unify the result with
     * @param arg1
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg1 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @return <code>true</code> if the operation is successfull
     */
    public boolean unop_2(final Term arg0, final Term arg1)
            throws InvalidTupleCentreIdException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg1);
        tcName = tid.getName();
        final LogicTuple tuArg = new LogicTupleDefault(arg0);
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local unop triggered...");
            final Tuple tuple = this.vm
                    .readUniformTuple(tuArg);
            if (tuple == null) {
                Respect2PLibrary.log("unop success");
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent ev = new InternalEvent(ce,
                        InternalOperation.makeUnoR(new LogicTupleDefault(arg0
                                .copyGoal(v, 0))));
                ev.setSource(ce.getReactingTC());
                ev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(ev);
                return true;
            }
            Respect2PLibrary.log("unop failure");
            return false;
        }
        Respect2PLibrary.log("Remote unop triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeUnop(new LogicTupleDefault(arg0.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param arg0
     *            the Prolog variable to unify the result with
     * @param arg1
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg1 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @return <code>true</code> if the operation is successfull
     */
    public boolean urd_2(final Term arg0, final Term arg1)
            throws InvalidTupleCentreIdException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg1);
        tcName = tid.getName();
        final LogicTuple tuArg = new LogicTupleDefault(arg0);
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local urd triggered...");
            final Tuple tuple = this.vm
                    .readUniformTuple(tuArg);
            if (tuple != null) {
                final Term term = ((LogicTuple) tuple).toTerm();
                this.unify(arg0, term.copyGoal(v, 0));
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent ev = new InternalEvent(ce,
                        InternalOperation.makeUrdR(new LogicTupleDefault(arg0
                                .copyGoal(v, 0))));
                ev.setSource(ce.getReactingTC());
                ev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(ev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote urd triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeUrd(new LogicTupleDefault(arg0.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }

    /**
     *
     * @param arg0
     *            the Prolog variable to unify the result with
     * @param arg1
     *            the identifier of the target tuple centre
     * @throws InvalidTupleCentreIdException
     *             if arg1 (tuple centre's id) is not a well-formed ground logic
     *             term
     * @return <code>true</code> if the operation is successfull
     */
    public boolean urdp_2(final Term arg0, final Term arg1)
            throws InvalidTupleCentreIdException {
        String tcName = null;
        TupleCentreId tid = null;
        tid = new TupleCentreId(arg1);
        tcName = tid.getName();
        final LogicTuple tuArg = new LogicTupleDefault(arg0);
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        if ("this".equals(tcName)) {
            Respect2PLibrary.log("Local urdp triggered...");
            final Tuple tuple = this.vm
                    .readUniformTuple(tuArg);
            if (tuple != null) {
                final Term term = ((LogicTuple) tuple).toTerm();
                this.unify(arg0, term.copyGoal(v, 0));
                final InputEvent ce = this.vm.getCurrentEvent();
                final InternalEvent ev = new InternalEvent(ce,
                        InternalOperation.makeUrdR(new LogicTupleDefault(arg0
                                .copyGoal(v, 0))));
                ev.setSource(ce.getReactingTC());
                ev.setTarget(ce.getReactingTC());
                this.vm.fetchTriggeredReactions(ev);
                return true;
            }
            return false;
        }
        Respect2PLibrary.log("Remote urdp triggered...");
        final InputEvent ce = this.vm.getCurrentEvent();
        final InputEvent outEv = new InputEvent(ce.getReactingTC(),
                RespectOperationDefault.makeUrdp(new LogicTupleDefault(arg0.copyGoal(v, 0)),
                        null), tid, this.vm.getCurrentTime(), ce.getPosition());
        outEv.setIsLinking(true);
        outEv.setTarget(tid);
        this.vm.addTemporaryOutputEvent(outEv);
        return true;
    }
}
