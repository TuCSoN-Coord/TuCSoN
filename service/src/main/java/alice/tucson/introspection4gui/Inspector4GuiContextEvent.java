package alice.tucson.introspection4gui;

import java.util.Set;

import alice.tuple.logic.LogicTuple;
import alice.tucson.introspection.InspectorContextEvent;

public class Inspector4GuiContextEvent extends InspectorContextEvent {

	private static final long serialVersionUID = 6050813675205357521L;
	
	private Set<LogicTuple> newTuples;
	private Set<LogicTuple> removedTuples;
	
	public void setRemovedTuples(Set<LogicTuple> tuples) {
		removedTuples = tuples;
	}
	
	public void setNewTuples(Set<LogicTuple> tuples) {
		newTuples = tuples;
	}
	
	public Set<LogicTuple> getNewTuples() {
		return newTuples;
	}
	
	public Set<LogicTuple> getRemovedTuples() {
		return removedTuples;
	}
    
}

