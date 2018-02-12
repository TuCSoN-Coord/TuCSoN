/*
 * Tuple Centre media - Copyright (C) 2001-2002 aliCE team at deis.unibo.it This
 * library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with this library; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package alice.tuplecentre.core;

/**
 * This is the reset state of the TCVM
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class ResetState extends AbstractTupleCentreVMState {

    private AbstractTupleCentreVMState idleState;

    /**
     *
     * @param tcvm
     *            the tuple centre VM this state belongs to
     */
    public ResetState(final AbstractTupleCentreVMContext tcvm) {
        super(tcvm);
    }

    @Override
    public void execute() {
        if (super.vm.isStepMode()) {
            this.log();
        }
        this.vm.reset();
    }

    @Override
    public AbstractTupleCentreVMState getNextState() {
        return this.idleState;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuplecentre.core.TupleCentreVMState#isIdle()
     */
    @Override
    public boolean isIdle() {
        return false;
    }

    @Override
    public void resolveLinks() {
        this.idleState = this.vm.getState("IdleState");
    }
}
