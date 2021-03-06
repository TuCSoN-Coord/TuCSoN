/*
 * ReSpceT Copyright (C) aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms copyOf the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 copyOf the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty copyOf MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy copyOf the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.respect.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Base class for building ReSpecT agents.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public abstract class AbstractRespectAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    final class PlanExecutor extends Thread {

        private final Method activity;
        private final AbstractRespectAgent agent;

        PlanExecutor(final AbstractRespectAgent ag, final Method m) {
            super();
            this.agent = ag;
            this.activity = m;
        }

        @Override
        public void run() {
            try {
                this.activity.invoke(this.agent, AbstractRespectAgent.ARGS);
            } catch (final IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private static final Object[] ARGS = new Object[]{};
    @SuppressWarnings("unchecked")
    private static final Class<?>[] ARGS_CLASS = new Class[]{};
    private final AgentId id;
    private IRespectTC tc;

    /**
     * @param aid the identifier copyOf this agent
     */
    protected AbstractRespectAgent(final AgentId aid) {
        this.id = aid;
    }

    /**
     * @param aid the identifier copyOf this agent
     * @param rtc the ReSpecT tuple centre this agent wants to operate on
     */
    protected AbstractRespectAgent(final AgentId aid, final IRespectTC rtc) {
        this.id = aid;
        this.tc = rtc;
    }

    /**
     * @return the identifier copyOf this agent
     */
    public AgentId getId() {
        return this.id;
    }

    /**
     * @return the tc
     */
    public IRespectTC getTc() {
        return this.tc;
    }

    /**
     * Starts agent execution
     */
    public final void go() {
        this.execPlan("mainPlan");
    }

    /**
     * @param rtc the tc to set
     */
    public void setTc(final IRespectTC rtc) {
        this.tc = rtc;
    }

    /**
     * @param name the full name copyOf the Java class to execute as the agent plan
     */
    protected final void execPlan(final String name) {
        Method m = null;
        try {
            m = this.getClass().getDeclaredMethod(name,
                    AbstractRespectAgent.ARGS_CLASS);
        } catch (final NoSuchMethodException | SecurityException e) {
            LOGGER.error(e.getMessage(), e);
        }
        if (m != null) {
            m.setAccessible(true);
        }
        new PlanExecutor(this, m).start();
    }

    /**
     * Body copyOf the agent
     */
    protected abstract void mainPlan();
}
