package inspector;

import java.util.ArrayList;
import java.util.LinkedList;

import alice.logictuple.InvalidLogicTupleException;
import alice.logictuple.InvalidTupleOperationException;
import alice.logictuple.LogicTuple;
import alice.respect.api.AgentId;
import alice.respect.api.IBlockingContext;
import alice.respect.api.IBlockingSpecContext;
import alice.respect.api.IManagementContext;
import alice.respect.api.InvalidAgentIdException;
import alice.respect.api.InvalidSpecificationException;
import alice.respect.api.InvalidTupleCentreIdException;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.RespectFramework;
import alice.respect.api.RespectSpecification;
import alice.respect.api.TupleCentreId;


//Class which implements the methods declared in the interface IRespectShellSubInterface for the interaction between tuple centers and main interfaces

public class RespectInspectorSubInterface implements
		IRespectInspectorSubInterface {

	private RespectFramework framework;
	private IBlockingContext blk_context;
	private IBlockingSpecContext spec_context;
	private IManagementContext mng_context;
	
	@SuppressWarnings("static-access")
	public RespectInspectorSubInterface() throws InvalidTupleCentreIdException{
		framework=new RespectFramework();
		blk_context=framework.getTC(new TupleCentreId("default")).getBlockingContext();
		spec_context=framework.getTC(new TupleCentreId("default")).getBlockingSpecContext();
		mng_context=framework.getTC(new TupleCentreId("default")).getManagementContext();
		mng_context.setManagementMode(true);
	}
	
	@SuppressWarnings("static-access")
	public RespectInspectorSubInterface(RespectFramework fw) throws InvalidTupleCentreIdException{
		framework=fw;
		blk_context=framework.getTC(new TupleCentreId("default")).getBlockingContext();
		spec_context=framework.getTC(new TupleCentreId("default")).getBlockingSpecContext();
		mng_context=framework.getTC(new TupleCentreId("default")).getManagementContext();
		mng_context.setManagementMode(true);
	}
	
	@SuppressWarnings("static-access")
	public RespectInspectorSubInterface(RespectFramework fw,String tcid) throws InvalidTupleCentreIdException{
		framework=fw;
		blk_context=framework.getTC(new TupleCentreId(tcid)).getBlockingContext();
		spec_context=framework.getTC(new TupleCentreId(tcid)).getBlockingSpecContext();
		mng_context=framework.getTC(new TupleCentreId(tcid)).getManagementContext();
		mng_context.setManagementMode(true);
	}
	
	
	public RespectFramework getFramework(){
		return framework;
	}
	
	//Utility class used for fixing the representation of specification tuples and reaction in a correct format
	public String fixApices(String string){
		int comapos=string.indexOf(',');
		while (comapos!=-1){
			if (string.substring(comapos+1,comapos+4).equals("','")) return string.substring(0,comapos+1)+string.substring(comapos+4,string.lastIndexOf(')')+1);
			comapos=string.indexOf(',',comapos+1);
		}
		return string.substring(0,string.lastIndexOf(')')+1);
	}
	
	//Utility function which implements the filter functionality on a string representation of any logic tuple
	public String[] filtre(String[] list,LogicTuple template) throws InvalidLogicTupleException{
		LinkedList<String> new_list=new LinkedList<String>();
		LogicTuple tuple;
		for (int i=0;i<list.length;i++){
			tuple=LogicTuple.parse(list[i]);
			if (tuple.match(template)) new_list.add(list[i]);
		}
		Object[] array1=new_list.toArray();
		String[] array2=new String[array1.length];
		for (int j=0;j<array1.length;j++) array2[j]=(String)array1[j];
		return array2;
	}
	
	//Utility function which return list of specification tuples of a tuple center's specification, given the specification itself
	public ArrayList<String> getSpecsList(String specification){
		ArrayList<String> list=new ArrayList<String>();
		int index;
		int pre_index=0;
		String reaction;
		if (specification.equals("")) return list;
		index=specification.indexOf("reaction",1);
		while (index>0){
			reaction=specification.substring(pre_index,index);
			list.add(reaction);
			pre_index=index;
			index=specification.indexOf("reaction",index+1);
		}
		reaction=specification.substring(pre_index);
		list.add(reaction);
		return list;
	}
	
	
	//Methods declared in IRespectShellSubInterface
	
	@SuppressWarnings("static-access")
	public void focusOnTC(String tcname) throws InvalidTupleCentreIdException{
		blk_context=framework.getTC(new TupleCentreId(tcname)).getBlockingContext();
		spec_context=framework.getTC(new TupleCentreId(tcname)).getBlockingSpecContext();
		mng_context=framework.getTC(new TupleCentreId(tcname)).getManagementContext();
		mng_context.setManagementMode(true);
	}
	
	public String[] getTuples(String filter) throws InvalidLogicTupleException{
		String[] result;
		LogicTuple template=LogicTuple.parse(filter);
		LogicTuple[] tuples=mng_context.getTSet(template);
		result=new String[tuples.length];
		for (int i=0;i<tuples.length;i++){
			result[i]=tuples[i].toString();
		}
		return filtre(result,template);
	}
	
	public void addTuple(String tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException{
		LogicTuple logictuple=LogicTuple.parse(tuple);
		blk_context.out(new AgentId("inspector"+System.nanoTime()),logictuple);
	}
	
	public void removeTuple(String tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException{
		LogicTuple logictuple=LogicTuple.parse(tuple);
		blk_context.inp(new AgentId("inspector"+System.nanoTime()),logictuple);
	}
	
	public String getSpec(){
		return mng_context.getSpec().toString();
	}
	
	public void setSpec(String spec) throws InvalidSpecificationException{
		RespectSpecification specification=new RespectSpecification(spec);
		mng_context.setSpec(specification);
	}
		
	public String[] getSpecs(String filtre) throws InvalidLogicTupleException{
		LogicTuple template=LogicTuple.parse(filtre);
		String specification=getSpec();
		ArrayList<String> list=getSpecsList(specification);
		Object[] array1=list.toArray();
		String[] array2=new String[array1.length];
		for (int i=0;i<array1.length;i++) array2[i]=fixApices((String)array1[i]);
		if (array2.length!=0) return filtre(array2,template);
		else return array2;
	}
	
	public void addSpec(String reaction) throws InvalidSpecificationException, InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException{
		LogicTuple reac=LogicTuple.parse(reaction);
		spec_context.out_s(new AgentId("inspector"+System.nanoTime()),reac);
	}
	
	public void removeSpec(String reaction) throws InvalidSpecificationException, InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException{
		LogicTuple reac=LogicTuple.parse(reaction);
		spec_context.inp_s(new AgentId("inspector"+System.nanoTime()),reac);
	}
	
	public String[] getQueries(String query) throws InvalidLogicTupleException, InvalidTupleOperationException{
		String[] result;
		LogicTuple template=LogicTuple.parse(query);
		LogicTuple[] queries=mng_context.getWSet(template);
		result=new String[queries.length];
		for (int i=0;i<queries.length;i++){
			result[i]=queries[i].toString();
		}
		if (result.length!=0) return filtre(result,template);
		return result;
	}
	
	//Still not implemented!
	public void cancelQuery(String query) throws InvalidLogicTupleException, InvalidTupleOperationException{
	}
	
	public String[] getReactions(String reaction) throws InvalidLogicTupleException{
		String[] result;
		LogicTuple template=LogicTuple.parse(reaction);
		LogicTuple[] reactions=mng_context.getTRSet(template);
		result=new String[reactions.length];
		for (int i=0;i<reactions.length;i++){
			result[i]=fixApices(reactions[i].toString());
		}
		if (result.length!=0) return filtre(result,template);
		else return result;
	}
	
	public void stopTC() throws OperationNotPossibleException{
		mng_context.stopCommand();
	}
	
	public void nextTCstep() throws OperationNotPossibleException{
		mng_context.nextStepCommand();
	}
	
	public void resumeTC() throws OperationNotPossibleException{
		mng_context.goCommand();
	}
	

}
