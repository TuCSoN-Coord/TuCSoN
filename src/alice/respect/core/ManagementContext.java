package alice.respect.core;

import java.util.List;

import alice.logictuple.LogicTuple;
import alice.respect.api.AgentId;
import alice.respect.api.IManagementContext;
import alice.respect.api.IRespectOperation;
import alice.respect.api.InvalidSpecificationException;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.RespectSpecification;
import alice.tuplecentre.api.InspectableEventListener;
import alice.tuplecentre.api.ObservableEventListener;

public class ManagementContext implements IManagementContext {

	private RespectVM vm;
	private Thread vmThread; 

	// Aggiungere due metodi per ottenere e per cambiare l'ontologia dando l'id del tuple centre
	
	public ManagementContext(RespectVM vm, Thread th){
		this.vm=vm;
		vmThread=th;
	}
	
	public void setSpec(RespectSpecification spec) throws InvalidSpecificationException {
		boolean accepted = vm.setReactionSpec(spec);
		if (!accepted){
			throw new InvalidSpecificationException();
		}
	}
	
	public RespectSpecification getSpec() {
		return (RespectSpecification)vm.getReactionSpec();
	}

	public boolean abortOperation(long opId){
		return vm.abortOperation(opId);
	}

    //
    
	public void setManagementMode(boolean activate){
		vm.setManagementMode(activate);
	}
	
	public void stopCommand() throws OperationNotPossibleException {
		try {
			vm.stopCommand();
		} catch (Exception ex){
			throw new OperationNotPossibleException(); 
		}
	}

	public void goCommand()throws OperationNotPossibleException {
		try {
			vm.goCommand();
		} catch (Exception ex){
			throw new OperationNotPossibleException(); 
		}
	}

	public void nextStepCommand()throws OperationNotPossibleException {
		try {
			vm.nextStepCommand();
		} catch (Exception ex){
			throw new OperationNotPossibleException(); 
		}
	}
	
	public void setSpy(boolean what){
		vm.setSpy(what);
	}
	
	public LogicTuple[] getTSet(LogicTuple t){
		return vm.getTSet(t);
	}
	
	public LogicTuple[] getWSet(LogicTuple t){
		
		//System.out.println("getWSet");
		return vm.getWSet(t);
	}
	
	public void setWSet(List<LogicTuple> wSet){
		
		System.out.println("getWSet");
		vm.setWSet(wSet);
	}

	public LogicTuple[] getTRSet(LogicTuple t){
		return vm.getTRSet(t);
	}
	
	public void addObserver(ObservableEventListener l){
		vm.addObserver(l);
	}
	
	public void removeObserver(ObservableEventListener l){
		vm.removeObserver(l);
	}

	public boolean hasObservers(){
		return vm.hasObservers();
	}

    public void addInspector(InspectableEventListener l){
        vm.addInspector(l);
    }

    public void removeInspector(InspectableEventListener l){
        vm.removeInspector(l);
    }

    public boolean hasInspectors(){
        return vm.hasInspectors();
    }
    
    public void reset(){
        vm.reset();
    }
}
