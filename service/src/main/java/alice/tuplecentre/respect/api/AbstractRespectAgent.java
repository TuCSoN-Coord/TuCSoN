/*
 * ReSpceT Copyright (C) aliCE team at deis.unibo.it This library is free
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
package alice.tuplecentre.respect.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Base class for building ReSpecT agents.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public abstract class AbstractRespectAgent {

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
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            } catch (final IllegalArgumentException e) {
                e.printStackTrace();
            } catch (final InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static final Object[] ARGS = new Object[]{};
    @SuppressWarnings("unchecked")
    private static final Class<? extends Object>[] ARGS_CLASS = new Class[]{};
    private final AgentId id;
    private IRespectTC tc;

    /**
     * @param aid the identifier of this agent
     */
    protected AbstractRespectAgent(final AgentId aid) {
        this.id = aid;
    }

    /**
     * @param aid the identifier of this agent
     * @param rtc the ReSpecT tuple centre this agent wants to operate on
     */
    protected AbstractRespectAgent(final AgentId aid, final IRespectTC rtc) {
        this.id = aid;
        this.tc = rtc;
    }

    /**
     * @return the identifier of this agent
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
     * @param name the full name of the Java class to execute as the agent plan
     */
    protected final void execPlan(final String name) {
        Method m = null;
        try {
            m = this.getClass().getDeclaredMethod(name,
                    AbstractRespectAgent.ARGS_CLASS);
        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
        } catch (final SecurityException e) {
            e.printStackTrace();
        }
        if (m != null) {
            m.setAccessible(true);
        }
        new PlanExecutor(this, m).start();
    }

    /**
     * Body of the agent
     */
    protected abstract void mainPlan();
}
