package shell;

import alice.respect.api.*;
import alice.logictuple.*;

public class RespectShellSubInterface implements IRespectShellSubInterface{
	
	private RespectFramework framework;
	private IBlockingContext context;
	private IBlockingSpecContext spec_context;
	
	@SuppressWarnings("static-access")
	public RespectShellSubInterface() throws OperationNotPossibleException, InvalidTupleCentreIdException, InstantiationNotPossibleException {
		framework=new RespectFramework();
		context=framework.getTC(new TupleCentreId("default")).getBlockingContext();
		spec_context=framework.getTC(new TupleCentreId("default")).getBlockingSpecContext();
	}
	
	@SuppressWarnings("static-access")
	public RespectShellSubInterface(String tcid) throws InvalidTupleCentreIdException{
		framework=new RespectFramework();
		context=framework.getTC(new TupleCentreId(tcid)).getBlockingContext();
		spec_context=framework.getTC(new TupleCentreId(tcid)).getBlockingSpecContext();
	}
	
	@SuppressWarnings("static-access")
	public RespectShellSubInterface(RespectFramework fw) throws InvalidTupleCentreIdException{
		framework=fw;
		context=framework.getTC(new TupleCentreId("default")).getBlockingContext();
		spec_context=framework.getTC(new TupleCentreId("default")).getBlockingSpecContext();
	}
	
	@SuppressWarnings("static-access")
	public RespectShellSubInterface(RespectFramework fw,String tcid) throws InvalidTupleCentreIdException{
		framework=fw;
		context=framework.getTC(new TupleCentreId(tcid)).getBlockingContext();
		spec_context=framework.getTC(new TupleCentreId(tcid)).getBlockingSpecContext();
	}
	
	public RespectFramework getFramework(){
		return framework;
	}
	
	
	@SuppressWarnings("static-access")
	public void focusOnTC(String tcname) throws InvalidTupleCentreIdException{
			context=RespectFramework.getTC(new TupleCentreId(tcname)).getBlockingContext();
			spec_context=framework.getTC(new TupleCentreId(tcname)).getBlockingSpecContext();
	}
	
	public String out(String tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException{
			LogicTuple lTuple=LogicTuple.parse(tuple);
			context.out(new AgentId("shell"+System.nanoTime()),lTuple);
			return "tupla aggiunta";
	}
	
	public String in(String tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException{
			LogicTuple template=LogicTuple.parse(tuple);
			LogicTuple result=context.in(new AgentId("shell"+System.nanoTime()),template);
			return "tupla ottenuta:   "+result.toString();
	}
	
	public String rd(String tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException{
			LogicTuple template=LogicTuple.parse(tuple);
			LogicTuple result=context.rd(new AgentId("shell"+System.nanoTime()),template);
			return "tupla ottenuta:   "+result.toString();
	}
	
	public String inp(String tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException{
			LogicTuple template=LogicTuple.parse(tuple);
			LogicTuple result=context.inp(new AgentId("shell"+System.nanoTime()),template);
			if (result==null) return "no tuple matched";
			else return "tupla ottenuta:   "+result.toString();
	}
	
	public String rdp(String tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException{
			LogicTuple template=LogicTuple.parse(tuple);
			LogicTuple result=context.rdp(new AgentId("shell"+System.nanoTime()),template);
			if (result==null) return "no tuple matched";
			else return "tupla ottenuta:   "+result.toString();
	}
	
	public String outS(String spec_tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException{
		LogicTuple tuple=LogicTuple.parse(spec_tuple);
		spec_context.out_s(new AgentId("shell"+System.nanoTime()),tuple);
		return "tupla di specifica aggiunta";
	}
	
	public String inS(String spec_tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException{
		LogicTuple template=LogicTuple.parse(spec_tuple);
		LogicTuple result=spec_context.in_s(new AgentId("shell"+System.nanoTime()),template);
		return "tupla di specifica ottenuta:  "+result.toString();
	}
	
	public String rdS(String spec_tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException{
		LogicTuple template=LogicTuple.parse(spec_tuple);
		LogicTuple result=spec_context.rd_s(new AgentId("shell"+System.nanoTime()),template);
		return "tupla di specifica ottenuta:  "+result.toString();
	}
	
	public String inpS(String spec_tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException{
		LogicTuple template=LogicTuple.parse(spec_tuple);
		LogicTuple result=spec_context.inp_s(new AgentId("shell"+System.nanoTime()),template);
		if (result==null) return "no tuple matched";
		else return "tupla di specifica ottenuta:  "+result.toString();
	}
	
	public String rdpS(String spec_tuple) throws InvalidLogicTupleException, OperationNotPossibleException, InvalidAgentIdException{
		LogicTuple template=LogicTuple.parse(spec_tuple);
		LogicTuple result=spec_context.rdp_s(new AgentId("shell"+System.nanoTime()),template);
		if (result==null) return "no tuple matched";
		else return "tupla di specifica ottenuta:  "+result.toString();
	}
	
	@SuppressWarnings("deprecation")
	public String setSpec(String spec) throws InvalidSpecificationException, InvalidAgentIdException{
		System.out.println(spec.toString());
		RespectSpecification rSpec=new RespectSpecification(spec);
			context.setSpec(new AgentId("shell"+System.nanoTime()),rSpec);
			return "specifica settata";
	}
	
	@SuppressWarnings("deprecation")
	public String getSpec() throws InvalidAgentIdException{
			RespectSpecification spec=context.getSpec(new AgentId("shell"+System.nanoTime()));
			return spec.toString();
	}

}
