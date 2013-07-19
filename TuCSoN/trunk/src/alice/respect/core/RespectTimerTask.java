package alice.respect.core;

import java.util.TimerTask;

import alice.tuplecentre.core.InputEvent;

/**
 * 
 * @author ste (mailto: s.mariani@unibo.it) on 02/lug/2013
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
        this.vm.notifyInputEvent(new InputEvent(this.vm.getId(), this.op,
                this.vm.getId(), this.vm.getCurrentTime()));
    }

}
