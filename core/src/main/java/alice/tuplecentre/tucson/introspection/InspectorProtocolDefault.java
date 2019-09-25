/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms copyOf the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 copyOf the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty copyOf MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy copyOf the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.tucson.introspection;


import alice.tuple.logic.LogicTuple;

/**
 * TODO add documentation!
 *
 * @author Unknown...
 */
public class InspectorProtocolDefault implements InspectorProtocol {

    /**
     * defining W set observation
     */
    private ObsType pendingQueryObservType = ObsType.DISABLED;
    /**
     * defining T set observation
     */
    private ObsType reactionsObservType = ObsType.DISABLED;
    /**
     * defining stepMode observation
     */
    private ObsType stepModeObservType = ObsType.STEP_MODE_TUPLE_SPACE;
    /**
     * defining T set observation
     */
    private ObsType tsetObservType = ObsType.DISABLED;

    /**
     * desired tracing state for the vm
     */
    private boolean tracing = false;
    /**
     * filter for tuple observed
     */
    private LogicTuple tsetFilter = null;
    /**
     * filter for query observed
     */
    private LogicTuple wsetFilter = null;

    @Override
    public ObsType getPendingQueryObservType() {
        return this.pendingQueryObservType;
    }

    @Override
    public ObsType getReactionsObservType() {
        return this.reactionsObservType;
    }

    @Override
    public ObsType getStepModeObservType() {
        return this.stepModeObservType;
    }

    @Override
    public ObsType getTsetObservType() {
        return this.tsetObservType;
    }

    @Override
    public LogicTuple getTsetFilter() {
        return this.tsetFilter;
    }

    @Override
    public LogicTuple getWsetFilter() {
        return this.wsetFilter;
    }

    @Override
    public boolean isTracing() {
        return this.tracing;
    }

    @Override
    public void setPendingQueryObservType(final ObsType obsType) {
        this.pendingQueryObservType = obsType;
    }

    @Override
    public void setReactionsObservType(final ObsType obsType) {
        this.reactionsObservType = obsType;
    }

    @Override
    public void setStepModeObservType(final ObsType obsType) {
        this.stepModeObservType = obsType;
    }

    @Override
    public void setTsetObservType(final ObsType obsType) {
        this.tsetObservType = obsType;
    }

    @Override
    public void setTracing(final boolean trace) {
        this.tracing = trace;
    }

    @Override
    public void setTsetFilter(final LogicTuple filter) {
        this.tsetFilter = filter;
    }

    @Override
    public void setWsetFilter(final LogicTuple filter) {
        this.wsetFilter = filter;
    }
}
