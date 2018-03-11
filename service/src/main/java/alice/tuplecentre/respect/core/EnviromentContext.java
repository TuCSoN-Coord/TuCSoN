package alice.tuplecentre.respect.core;

import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.respect.api.IEnvironmentContext;

/**
 * @author Unknown...
 */
public class EnviromentContext implements IEnvironmentContext {

    private final RespectVMContext vm;

    /**
     * @param rvm the ReSpecT VM this context refers to
     */
    public EnviromentContext(final RespectVMContext rvm) {
        this.vm = rvm;
    }

    @Override
    public long getCurrentTime() {
        return this.vm.getCurrentTime();
    }

    @Override
    public void notifyInputEnvEvent(final InputEvent ev) {
        this.vm.notifyInputEnvEvent(ev);
    }

    @Override
    public void notifyInputEvent(final InputEvent ev) {
        this.vm.notifyInputEvent(ev);
    }
}
