package alice.respect.core;

import alice.tuprolog.SpyEvent;
import alice.tuprolog.SpyListener;

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
