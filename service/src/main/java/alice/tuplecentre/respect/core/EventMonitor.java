package alice.tuplecentre.respect.core;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Saverio Cicora
 *
 */
public class EventMonitor {

    private final Condition canCompute;
    private boolean event;
    private final ReentrantLock lock;

    /**
     *
     */
    public EventMonitor() {
        this.event = false;
        this.lock = new ReentrantLock();
        this.canCompute = this.lock.newCondition();
    }

    /**
     *
     * @throws InterruptedException
     *             if the synchronisation wait gets interrupted
     */
    public void awaitEvent() throws InterruptedException {
        this.lock.lock();
        try {
            while (!this.event) {
                this.canCompute.await();
            }
            this.event = false;
            return;
        } finally {
            this.lock.unlock();
        }
    }

    /**
     *
     * @return wether at least an event has been signaled
     */
    public boolean hasEvent() {
        this.lock.lock();
        try {
            return this.event;
        } finally {
            this.lock.unlock();
        }
    }

    /**
     *
     */
    public void signalEvent() {
        this.lock.lock();
        try {
            this.event = true;
            this.canCompute.signal();
        } finally {
            this.lock.unlock();
        }
    }
}
