package alice.respect.core;

import alice.tuprolog.event.SpyEvent;
import alice.tuprolog.event.SpyListener;

public class PrologSpyListener implements SpyListener {
	
	private RespectVMContext vm;
	
	public PrologSpyListener(RespectVMContext vm){
		this.vm=vm;
	}
	@Override
	public void onSpy(SpyEvent arg0) {
		vm.spy("[PROLOG]==>"+arg0);

	}

}
