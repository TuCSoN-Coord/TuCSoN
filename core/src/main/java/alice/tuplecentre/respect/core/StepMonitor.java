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
package alice.tuplecentre.respect.core;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Roberto D'Elia
 */
public class StepMonitor {

    private final Condition canCompute;
    private final ReentrantLock lock;

    public StepMonitor() {
        this.lock = new ReentrantLock();
        this.canCompute = this.lock.newCondition();
    }

    /**
     * @throws InterruptedException if the synchronisation wait gets interrupted
     */
    public void awaitEvent() throws InterruptedException {
        this.lock.lock();
        try {
            this.canCompute.await();
        } finally {
            this.lock.unlock();
        }
    }

    public void signalEvent() {
        this.lock.lock();
        try {
            this.canCompute.signal();
        } finally {
            this.lock.unlock();
        }
    }
}
