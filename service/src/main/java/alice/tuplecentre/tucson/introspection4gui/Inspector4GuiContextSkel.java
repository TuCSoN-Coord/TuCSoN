package alice.tuplecentre.tucson.introspection4gui;

import java.util.HashSet;
import java.util.Set;

import alice.logictuple.LogicTuple;
import alice.tuplecentre.core.InspectableEvent;
import alice.tuplecentre.core.ObservableEventExt;
import alice.tuplecentre.core.ObservableEventReactionOK;
import alice.tuplecentre.core.TriggeredReaction;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.tucson.api.exceptions.TucsonGenericException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.introspection.InspectorContextSkel;
import alice.tuplecentre.tucson.network.TucsonProtocol;
import alice.tuplecentre.tucson.network.exceptions.DialogReceiveException;
import alice.tuplecentre.tucson.network.exceptions.DialogSendException;
import alice.tuplecentre.tucson.service.ACCDescription;
import alice.tuplecentre.tucson.service.ACCProvider;
import alice.tuplecentre.tucson.service.TucsonNodeService;
import alice.tuplecentre.tucson.service.TupleCentreContainer;

public class Inspector4GuiContextSkel extends InspectorContextSkel {
	
	private Set<LogicTuple> tuples;
	private Set<LogicTuple> currentNewTuples = new HashSet<>();
	private Set<LogicTuple> currentRemovedTuples = new HashSet<>();
	
	public Inspector4GuiContextSkel(ACCProvider man, TucsonProtocol d, TucsonNodeService node, ACCDescription p)
			throws TucsonGenericException, TucsonInvalidAgentIdException, DialogReceiveException,
			TucsonInvalidTupleCentreIdException {
		super(man, d, node, p);
		this.tuples = new HashSet<>();
		System.out.println("GDRADI SKEL");
	}

	private void updateTuples(LogicTuple[] ltSet) {
		Set<LogicTuple> tuplesToKeep = new HashSet<>();
		currentNewTuples = new HashSet<>();
		currentRemovedTuples =  new HashSet<>();
		for (LogicTuple newTuple : ltSet) {
			boolean found = false;
			for (LogicTuple oldTuple : tuples) {
				// Tuple is already in tc
				if (newTuple.equals(oldTuple)) {
					tuplesToKeep.add(oldTuple);
					found = true;
					break;
				}
			}
			// Tuple is not found in tc, it is a new one
			if (!found) {
				tuplesToKeep.add(newTuple);
				currentNewTuples.add(newTuple);
			}
		}
		tuples.removeAll(tuplesToKeep);
		for (LogicTuple tupleRemoved : tuples) {
			currentRemovedTuples.add(tupleRemoved);
		}
		tuples = tuplesToKeep;
	}
	
	private void fillMsg(Inspector4GuiContextEvent msg) {
		if (currentNewTuples.size() > 0) {
        	msg.setNewTuples(currentNewTuples);
        }
        if (currentRemovedTuples.size() > 0) {
        	msg.setRemovedTuples(currentRemovedTuples);
        }
	}
	
	@Override
    public synchronized void onInspectableEvent(final InspectableEvent ev) {
		try {
            final Inspector4GuiContextEvent msg = new Inspector4GuiContextEvent();
            msg.setLocalTime(System.currentTimeMillis());
            msg.setVmTime(ev.getTime());
            if (ev.getType() == InspectableEvent.TYPE_IDLESTATE) {
                    final LogicTuple[] ltSet = (LogicTuple[]) TupleCentreContainer
                            .doManagementOperation(
                                    TupleCentreOpType.GET_TSET, this.tcId, this.protocol.getTsetFilter());

                    updateTuples(ltSet);     
                    fillMsg(msg);
                this.dialog.sendInspectorEvent(msg);
            }
            if (ev.getType() == InspectableEvent.TYPE_NEWSTATE) {
                	final LogicTuple[] ltSet = (LogicTuple[]) TupleCentreContainer
                            .doManagementOperation(
                                    TupleCentreOpType.GET_TSET, this.tcId, this.protocol.getTsetFilter());
                    updateTuples(ltSet);               
                    fillMsg(msg);
                this.dialog.sendInspectorEvent(msg);
            } else if (ev.getType() == ObservableEventExt.TYPE_REACTIONOK) {
                    final TriggeredReaction zCopy = new TriggeredReaction(null,
                            ((ObservableEventReactionOK) ev).getZ()
                                    .getReaction());
                    msg.setReactionOk(zCopy);
                    this.dialog.sendInspectorEvent(msg);
            }
        } catch (final DialogSendException e) {
            this.log("Inspector quit");
            e.printStackTrace();
        }
    }
	   
}
