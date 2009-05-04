package alice.respect.api;

import alice.tuplecentre.core.InputEvent;

public interface IEnvironmentContext {

	void notifyInputEnvEvent(InputEvent ev);
	void notifyInputEvent(InputEvent ev);
	long getCurrentTime();
}
