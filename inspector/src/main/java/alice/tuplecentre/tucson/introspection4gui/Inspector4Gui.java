package alice.tuplecentre.tucson.introspection4gui;

import java.util.ArrayList;
import java.util.List;

import alice.tuple.Tuple;
import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.core.Reaction;
import alice.tuplecentre.respect.core.LogicReaction;
import alice.tuplecentre.tucson.api.TucsonAgentIdDefault;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.introspection.Inspector;
import alice.tuplecentre.tucson.introspection.InspectorContextEvent;
import alice.tuplecentre.tucson.introspection.InspectorProtocol;
import alice.tuplecentre.tucson.introspection.InspectorProtocolDefault;
import alice.tuplecentre.tucson.network.exceptions.DialogSendException;
import alice.tuprolog.Struct;

public class Inspector4Gui extends Inspector {

/*
 * **************************************************
 * 
 * 	Fields
 * 
 * **************************************************/

	private final List<Inspector4GuiObserver> observers;
	private final InspectorProtocol protocol;
	
/*
 * **************************************************
 * 
 * 	Constructors
 * 
 * **************************************************/
	
	public Inspector4Gui(final TucsonTupleCentreId arg1) throws Exception {
        super(new TucsonAgentIdDefault("inspector4gui_" + System.currentTimeMillis()), arg1, true);
		this.observers = new ArrayList<>();
        protocol = new InspectorProtocolDefault();
        protocol.setTsetObservType(InspectorProtocol.ObsType.PROACTIVE);
        protocol.setReactionsObservType(InspectorProtocol.ObsType.PROACTIVE);
		getContext().setProtocol(protocol);
	}
	
	@Override
	public synchronized void onContextEvent(final InspectorContextEvent msg) {
		if (msg instanceof Inspector4GuiContextEvent) {
			Inspector4GuiContextEvent msg4Gui = (Inspector4GuiContextEvent) msg;
			if (msg4Gui.getNewTuples() != null) {
				for (LogicTuple t : msg4Gui.getNewTuples()) {
					notifyNewTuple(t);
				}
			}
			if (msg4Gui.getRemovedTuples() != null) {
				for (LogicTuple t : msg4Gui.getRemovedTuples()) {
					notifyRemovedTuple(t);
				}
			}
		}		
		
		if (msg.getReactionOk() != null) {
			Reaction tr = msg.getReactionOk().getReaction();
			if (tr instanceof LogicReaction) {
				LogicReaction logicReaction = (LogicReaction) tr;
				Struct struct = logicReaction.getStructReaction();
				struct.resolveTerm();
				//System.out.println("START");
				//System.out.println(logicReaction.getStructReaction());
				Struct arg = struct.getArg("?");
				if (arg != null) {
					//Struct destStruct = arg.getArg("@");
					//Struct hostStruct = destStruct.getArg(":");
					// TODO seems reverse
					Struct destStruct = arg.getArg("@");
					Struct hostStruct = destStruct.getArg(":");
					// END
					String tcName = destStruct.getTerm(0).toString();
					String tcHost = hostStruct.getTerm(0).toString();
					String tcPort = alice.util.Tools.removeApices(hostStruct.getTerm(1).toString());
					if (arg.getArg(1) instanceof Struct) {
						Struct termAsStruct = (Struct) arg.getArg(1);
						System.out.println(termAsStruct);
						if (termAsStruct.getName().equals("out")) {
							String tuple = termAsStruct.getArg(0).toString();
							notifyTransfer(tcName, tcHost, tcPort, tuple, false);
						} else if (termAsStruct.getName().equals("in")) {
							String tuple = termAsStruct.getArg(0).toString();
							notifyTransfer(tcName, tcHost, tcPort, tuple, true);
						}
					}
					try {
                        TucsonTupleCentreId tcId = new TucsonTupleCentreIdDefault(tcName, tcHost, tcPort);
						notifyNewTupleCenter(tcId);
					} catch (TucsonInvalidTupleCentreIdException e) {
						e.printStackTrace();
					}
				}
				//System.out.println(struct);
			}
		} 
	}

	public void addOberver(final Inspector4GuiObserver tucsonCoreObserver) {
		this.observers.add(tucsonCoreObserver);		
	}
	
	private void notifyNewTuple(final Tuple newTuple) {
		for (Inspector4GuiObserver observer : observers) {
			observer.onNewTuple(newTuple, this.context.getTid());
		}
	}
	
	private void notifyRemovedTuple(Tuple tupleRemoved) {
		for (Inspector4GuiObserver observer : observers) {
			observer.onRemovedTuple(tupleRemoved,  this.context.getTid());
		}
	}

	private void notifyTransfer(final String tcName, final String tcHost, final String tcPort, final String tuple, final boolean reverseOrder) {
		for (Inspector4GuiObserver observer : observers) {
			if (!reverseOrder) {
                observer.onNewTrasfer(this.context.getTid().getLocalName(), tcName, tuple);
			} else {
                observer.onNewTrasfer(tcName, this.context.getTid().getLocalName(), tuple);
			}
		}
	}

	private void notifyNewTupleCenter(final TucsonTupleCentreId newTcId) {
		for (Inspector4GuiObserver observer : observers) {
			observer.onNewTupleCenter(newTcId);
		}
	}
	
	
	public void setFilter(final LogicTuple filter) {
		protocol.setTsetFilter(filter);
		try {
			getContext().setProtocol(protocol);
		} catch (DialogSendException e) {
			e.printStackTrace();
		}
	}
	
	
}
