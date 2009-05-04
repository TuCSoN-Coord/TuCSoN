/*
 * Created on Feb 19, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package alice.tuplecentre.api;


/**
 * @author aricci
 */
public interface ITupleCentreManagement {

	void setManagementMode(boolean activate);
	
	void stopCommand() throws OperationNotPossibleException;

	void goCommand() throws OperationNotPossibleException;

	void nextStepCommand() throws OperationNotPossibleException;

	//

	boolean	setReactionSpec(BehaviourSpecification spec);

	BehaviourSpecification getReactionSpec();

}
