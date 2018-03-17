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
package alice.tuplecentre.tucson.api;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import alice.tuplecentre.tucson.api.acc.RootACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FSA-like TuCSoN agent.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public abstract class AbstractAutomaton extends AbstractTucsonAgent<RootACC> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    /**
     *
     */
    protected static Class<?>[] argType;
    /**
     *
     */
    protected Object[] arguments = null;
    /**
     *
     */
    protected String state = "boot";

    /**
     * @param aid name of the agent (must be a valid Prolog term)
     * @throws TucsonInvalidAgentIdException if the given String is not a valid representation of a
     *                                       ReSpecT agent identifier
     * @see alice.tuprolog.Term Term
     */
    public AbstractAutomaton(final String aid)
            throws TucsonInvalidAgentIdException {
        super(aid);
    }

    /**
     * To change state.
     *
     */
    protected void become() {
        if (!"end".equals(this.state)) {
            this.state = "end";
            this.arguments = null;
        }
    }

    /**
     * To change state
     *
     * @param s    the string representing the state to become
     * @param args arguments to be used in the target state
     */
    protected void become(final String s, final Object[] args) {
        if (!"end".equals(this.state)) {
            this.state = s;
            this.arguments = args.clone();
        }
    }

    /**
     * Init state.
     */
    protected abstract void boot();

    /**
     * End state.
     *
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     */
    protected void end() {
        this.getACC().exit();
    }

    /**
     * Error state.
     */
    protected void error() {
        this.become();
    }

    /**
     * Main FSA cycle.
     */
    @Override
    protected final void main() {
        // FIXME I don't think the string is correct...
        try {
            AbstractAutomaton.argType = new Class[]{Class
                    .forName("java.lang.Object")};
        } catch (final ClassNotFoundException e) {
            LOGGER.error("[Automaton]: " + e);
            LOGGER.error(e.getMessage(), e);
            this.error();
        }
        while (true) {
            if (!this.state.equals("end")) {
                if (this.arguments == null) {
                    Method m;
                    try {
                        m = this.getClass().getDeclaredMethod(this.state,
                                (Class<?>[]) null);
                        m.setAccessible(true);
                        m.invoke(this, (Object[]) null);
                    } catch (final SecurityException | InvocationTargetException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException e) {
                        LOGGER.error(e.getMessage(), e);
                        this.error();
                    }
                } else {
                    Method m;
                    try {
                        m = this.getClass().getDeclaredMethod(this.state,
                                AbstractAutomaton.argType);
                        m.setAccessible(true);
                        m.invoke(this, this.arguments);
                    } catch (final SecurityException | InvocationTargetException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException e) {
                        LOGGER.error(e.getMessage(), e);
                        this.error();
                    }
                }
            } else {
                this.end();
                break;
            }
        }
    }
}
