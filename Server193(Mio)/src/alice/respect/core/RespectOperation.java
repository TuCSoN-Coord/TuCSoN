/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.respect.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import alice.respect.api.IRespectOperation;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;
import alice.tuplecentre.core.*;
import alice.tuprolog.Prolog;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.logictuple.*;

/**
 * This class represents a ReSpecT operation.
 * 
 * @author aricci
 */
public class RespectOperation extends TupleCentreOperation implements IRespectOperation {
	
	public static final int OPTYPE_TIME = 18;
	public static final int OPTYPE_GET_ENV = 19;
	public static final int OPTYPE_SET_ENV = 20;
	public static final int OPTYPE_ENV = 21;
	
	protected RespectOperation(Prolog p, int type, Tuple t, OperationCompletionListener l){
		super(p, type,t,l);
	}
	
	protected RespectOperation(Prolog p, int type, TupleTemplate t, OperationCompletionListener l){
		super(p, type,t,l);
	}
	
	protected RespectOperation(Prolog p, int type, List<Tuple> tupleList, OperationCompletionListener l){
		super(p, type, tupleList, l);
	}

	/**
	 * Gets the result of the operation,
	 * 
	 * @return
	 */
	public LogicTuple getLogicTupleResult(){
		return (LogicTuple)getResult();
	}
	
	/**
	 * Gets the results of a get operation
	 */
	public List<LogicTuple> getTupleResultList(){
		List<Tuple> tl = this.getResultList();
		List<LogicTuple> tll = new LinkedList<LogicTuple>();
		for(Tuple t :tl){
			tll.add((LogicTuple)t);
		}
		
		return tll;
	}
	
	/**
	 * Gets the argument of the operation
	 * 
	 * @return
	 */
	public LogicTuple getLogicTupleArgument(){
		if (isOut() | isOut_s()){
			return (LogicTuple)getTupleArgument();
		} else {
			return (LogicTuple)getTemplateArgument();
		} 
	}

	public static RespectOperation makeOut(Prolog p, LogicTuple t, OperationCompletionListener l){
		return new RespectOperation(p, TupleCentreOperation.OPTYPE_OUT,(Tuple)t, l);
	}		
	
	public static RespectOperation makeIn(Prolog p, LogicTuple t,OperationCompletionListener l){
		return new RespectOperation(p, TupleCentreOperation.OPTYPE_IN,(TupleTemplate)t,l);
	}

	public static RespectOperation makeRd(Prolog p, LogicTuple t,OperationCompletionListener l){
		return new RespectOperation(p, TupleCentreOperation.OPTYPE_RD,(TupleTemplate)t,l);
	}

	public static RespectOperation makeInp(Prolog p, LogicTuple t,OperationCompletionListener l){
		return new RespectOperation(p, TupleCentreOperation.OPTYPE_INP,(TupleTemplate)t,l);
	}

	public static RespectOperation makeRdp(Prolog p, LogicTuple t,OperationCompletionListener l){
		return new RespectOperation(p, TupleCentreOperation.OPTYPE_RDP,(TupleTemplate)t,l);
	}
	
	public static RespectOperation makeNo(Prolog p, LogicTuple t,OperationCompletionListener l){
		return new RespectOperation(p, TupleCentreOperation.OPTYPE_NO,(TupleTemplate)t,l);
	}
	
	public static RespectOperation makeTime(Prolog p, LogicTuple t,OperationCompletionListener l){
		return new RespectOperation(p, RespectOperation.OPTYPE_TIME,(TupleTemplate)t,l);
	}
	
	public static RespectOperation makeIn_s(Prolog p, LogicTuple t, OperationCompletionListener l){
		return new RespectOperation(p, RespectOperation.OPTYPE_IN_S, (TupleTemplate)t, l);
	}
	
	public static RespectOperation makeRd_s(Prolog p, LogicTuple t, OperationCompletionListener l){
		return new RespectOperation(p, RespectOperation.OPTYPE_RD_S, (TupleTemplate)t, l);
	}
	
	public static RespectOperation makeOut_s(Prolog p, LogicTuple t, OperationCompletionListener l){
		return new RespectOperation(p, RespectOperation.OPTYPE_OUT_S, (Tuple)t, l);
	}
	
	public static RespectOperation makeInp_s(Prolog p, LogicTuple t, OperationCompletionListener l){
		return new RespectOperation(p, RespectOperation.OPTYPE_INP_S, (TupleTemplate)t, l);
	}
	
	public static RespectOperation makeRdp_s(Prolog p, LogicTuple t, OperationCompletionListener l){
		return new RespectOperation(p, RespectOperation.OPTYPE_RDP_S, (TupleTemplate)t, l);
	}
	
	public static RespectOperation makeGetEnv(Prolog p, LogicTuple t, OperationCompletionListener l){
		RespectOperation temp=new RespectOperation(p, RespectOperation.OPTYPE_GET_ENV, (TupleTemplate)t, l);
		temp.setResult(t);
		return temp;
	}
	
	public static RespectOperation makeGet(Prolog p, LogicTuple t, OperationCompletionListener l){
		RespectOperation temp=new RespectOperation(p, RespectOperation.OPTYPE_GET, (Tuple)t, l);
		return temp;
	}
	
	public static RespectOperation makeSet(Prolog p, List<LogicTuple> logicTupleList, OperationCompletionListener l){
		List<Tuple> tupleList = new ArrayList<Tuple>(); 
		for(LogicTuple lTuple : logicTupleList)
			tupleList.add(lTuple);
		
		RespectOperation temp=new RespectOperation(p, RespectOperation.OPTYPE_SET, tupleList, l);
		return temp;
	}
	
	public static RespectOperation makeSetEnv(Prolog p, LogicTuple t, OperationCompletionListener l){
		RespectOperation temp= new RespectOperation(p, RespectOperation.OPTYPE_SET_ENV, (TupleTemplate)t, l);
		temp.setResult(t);
		return temp;
	}
	public LogicTuple toTuple(){
		LogicTuple t=null;
		Term[] tl=null;
		if (isOperationCompleted()){
			t = getLogicTupleResult();
		} else {
			t = getLogicTupleArgument();
		}
		//System.out.println("[RespectOperation] ECCOLOOOOOO ANCORA"+t);
		String opName;
		if (isOut()){
			opName = "out";
		} else if (isIn()){
			opName = "in";
		} else if (isRd()){
			opName = "rd";
		} else if (isInp()){
			opName = "inp";
		} else if (isRdp()){
			opName = "rdp";
		} else if (isGet()){
			opName = "get";
			LogicTuple[] tupleL=new LogicTuple[]{};
			//System.out.println("LUNGHEZZA1: "+this.getTupleResultList().size());
			tupleL=this.getTupleResultList().toArray(tupleL);
			//System.out.println("LUNGHEZZA2: "+tupleL.length);
			tl = new Term[tupleL.length];
			for(int i=0;i<tupleL.length;i++){
				tl[i]=(tupleL[i]).toTerm();
				//System.out.println("TERMINE: "+tl[i]);
			}
		} else if (isSet()){
			opName = "set";
			LogicTuple[] tupleL=new LogicTuple[]{};
			//System.out.println("LUNGHEZZA1: "+this.getTupleResultList().size());
			tupleL=this.getTupleListArgument().toArray(tupleL);
			//System.out.println("LUNGHEZZA2: "+tupleL.length);
			tl = new Term[tupleL.length];
			for(int i=0;i<tupleL.length;i++){
				tl[i]=(tupleL[i]).toTerm();
				//System.out.println("TERMINE: "+tl[i]);
			}
		}else if (isOut_s()){
			opName = "out_s";
		} else if (isIn_s()){
			opName = "in_s";
		} else if (isRd_s()){
			opName = "rd_s";
		} else if (isInp_s()){
			opName = "inp_s";
		} else if (isRdp_s()){
			opName = "rdp_s";
		} else if (isGetEnv()){
			return t;
		} else if (isEnv()){
			opName = "env";
		} else if (isSetEnv()){
			return t;
		} else if (isTime()){
			opName = "time";
		} else {
			opName = "unknownOp";
		}
		return new LogicTuple(opName, 
				new TupleArgument(tl!=null ? new Struct(tl) : t.toTerm()));
	}

	public String toString(){
		return toTuple().toString();
	}
	public boolean isTime() {
		return getType()==OPTYPE_TIME;
	}
	public boolean isEnv() {
		return getType()==OPTYPE_ENV;
	}
	public boolean isGetEnv() {
		return getType()==OPTYPE_GET_ENV;
	}
	public boolean isSetEnv() {
		return getType()==OPTYPE_SET_ENV;
	}
}
	
