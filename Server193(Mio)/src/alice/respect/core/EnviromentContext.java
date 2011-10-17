package alice.respect.core;

import alice.respect.api.IEnvironmentContext;
import alice.tuplecentre.core.InputEvent;

public class EnviromentContext implements IEnvironmentContext{

	private RespectVMContext vm;
	
	public EnviromentContext(RespectVMContext vm){
		this.vm=vm;
	}
	
	public void notifyInputEnvEvent(InputEvent ev) {
		vm.notifyInputEnvEvent(ev);
	}

	public long getCurrentTime() {
		return vm.getCurrentTime();
	}

	public void notifyInputEvent(InputEvent ev) {
		vm.notifyInputEvent(ev);
	}

}
