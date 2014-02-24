package alice.respect.core;

import java.util.TimerTask;

import alice.tuplecentre.core.InputEvent;

/**
 * 
 * @author Matteo Casadei
 * 
 */
public class RespectTimerTask extends TimerTask {

    private final RespectOperation op;
    private final RespectVMContext vm;

    /**
     * 
     * @param rvm
     *            the ReSpecT VM which should schedule this timed task
     * @param rop
     *            the scheduled operation
     */
    public RespectTimerTask(final RespectVMContext rvm,
            final RespectOperation rop) {
        super();
        this.vm = rvm;
        this.op = rop;
    }

    @Override
    public void run() {
        // System.out.println("[TIME]: event = " + this.op + " at " +
        // this.vm.getCurrentTime());
        this.vm.notifyInputEvent(new InputEvent(this.vm.getId(), this.op,
                this.vm.getId(), this.vm.getCurrentTime()));
    }

}
