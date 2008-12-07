package shell;

import alice.respect.api.Agent;
import alice.respect.api.AgentId;
import alice.respect.api.InstantiationNotPossibleException;
import alice.respect.api.InvalidAgentIdException;
import alice.respect.api.InvalidTupleCentreIdException;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.RespectException;

public class ShellAgent extends Agent {
	
	public ShellAgent(String name) throws InvalidAgentIdException, RespectException{
		super(new AgentId(name));
	}

	protected void mainPlan() {
		try {
			new SimpleShellGUI(new RespectShellSubInterface()/*,this.getId()*/);
		} catch (OperationNotPossibleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTupleCentreIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationNotPossibleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InvalidAgentIdException, RespectException{
		new ShellAgent("shell").mainPlan();
	}

}
