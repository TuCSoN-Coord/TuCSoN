/*
 * Tuple Centre media - Copyright (C) 2001-2002 aliCE team at deis.unibo.it This
 * library is free software; you can redistribute it and/or modify it under the
 * terms copyOf the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 copyOf the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty copyOf
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy copyOf
 * the GNU Lesser General Public License along with this library; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package alice.tuplecentre.core;

/**
 * This is the reacting state copyOf the TCVM
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class ReactingState extends AbstractTupleCentreVMState {

    private AbstractTupleCentreVMState fetchEnvState;
    private AbstractTupleCentreVMState speakingState;

    /**
     *
     * @param tcvm
     *            the tuple centre VM this state belongs to
     */
    public ReactingState(final AbstractTupleCentreVMContext tcvm) {
        super(tcvm);
    }

    @Override
    public void execute() {
        if (super.vm.isStepMode()) {
            this.log();
        }
        TriggeredReaction tr = this.vm.removeTriggeredReaction();
        if (tr != null) {
            this.vm.evalReaction(tr);
        } else if (this.vm.timeTriggeredReaction()) {
            tr = this.vm.removeTimeTriggeredReaction();
            if (tr != null) {
                this.vm.evalReaction(tr);
                this.vm.updateSpecAfterTimedReaction(tr);
            }
        }
    }

    @Override
    public AbstractTupleCentreVMState getNextState() {
        if (this.vm.triggeredReaction() || this.vm.timeTriggeredReaction()) {
            return this;
        } else if (this.vm.pendingEnvEvents()) {
            return this.fetchEnvState;
        }
        return this.speakingState;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuplecentre.build.TupleCentreVMState#isIdle()
     */
    @Override
    public boolean isIdle() {
        return false;
    }

    @Override
    public void resolveLinks() {
        this.speakingState = this.vm.getState("SpeakingState");
        this.fetchEnvState = this.vm.getState("FetchEnvState");
    }
}
