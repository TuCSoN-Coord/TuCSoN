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

import alice.respect.api.IRespectOperation;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;
import alice.tuplecentre.core.*;
import alice.logictuple.*;

/**
 * This class represents a ReSpecT operation.
 * 
 * @author aricci
 */
public class RespectOperation extends TupleCentreOperation implements IRespectOperation {
	
	public static final int OPTYPE_TIME = 10;
	public static final int OPTYPE_GET_ENV = 16;
	public static final int OPTYPE_SET_ENV = 17;
	public static final int OPTYPE_ENV = 18;
	
	protected RespectOperation(int type, Tuple t, OperationCompletionListener l){
		super(type,t,l);
	}
	
	protected RespectOperation(int type, TupleTemplate t, OperationCompletionListener l){
		super(type,t,l);
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

	public static RespectOperation makeOut(LogicTuple t, OperationCompletionListener l){
		return new RespectOperation(TupleCentreOperation.OPTYPE_OUT,(Tuple)t, l);
	}		
	
	public static RespectOperation makeIn(LogicTuple t,OperationCompletionListener l){
		return new RespectOperation(TupleCentreOperation.OPTYPE_IN,(TupleTemplate)t,l);
	}

	public static RespectOperation makeRd(LogicTuple t,OperationCompletionListener l){
		return new RespectOperation(TupleCentreOperation.OPTYPE_RD,(TupleTemplate)t,l);
	}

	public static RespectOperation makeInp(LogicTuple t,OperationCompletionListener l){
		return new RespectOperation(TupleCentreOperation.OPTYPE_INP,(TupleTemplate)t,l);
	}

	public static RespectOperation makeRdp(LogicTuple t,OperationCompletionListener l){
		return new RespectOperation(TupleCentreOperation.OPTYPE_RDP,(TupleTemplate)t,l);
	}
	
	public static RespectOperation makeNo(LogicTuple t,OperationCompletionListener l){
		return new RespectOperation(TupleCentreOperation.OPTYPE_NO,(TupleTemplate)t,l);
	}
	
	public static RespectOperation makeTime(LogicTuple t,OperationCompletionListener l){
		return new RespectOperation(RespectOperation.OPTYPE_TIME,(TupleTemplate)t,l);
	}
	
	public static RespectOperation makeIn_s(LogicTuple t, OperationCompletionListener l){
		return new RespectOperation(RespectOperation.OPTYPE_IN_S, (TupleTemplate)t, l);
	}
	
	public static RespectOperation makeRd_s(LogicTuple t, OperationCompletionListener l){
		return new RespectOperation(RespectOperation.OPTYPE_RD_S, (TupleTemplate)t, l);
	}
	
	public static RespectOperation makeOut_s(LogicTuple t, OperationCompletionListener l){
		return new RespectOperation(RespectOperation.OPTYPE_OUT_S, (Tuple)t, l);
	}
	
	public static RespectOperation makeInp_s(LogicTuple t, OperationCompletionListener l){
		return new RespectOperation(RespectOperation.OPTYPE_INP_S, (TupleTemplate)t, l);
	}
	
	public static RespectOperation makeRdp_s(LogicTuple t, OperationCompletionListener l){
		return new RespectOperation(RespectOperation.OPTYPE_RDP_S, (TupleTemplate)t, l);
	}
	public static RespectOperation makeGetEnv(LogicTuple t, OperationCompletionListener l){
		RespectOperation temp=new RespectOperation(RespectOperation.OPTYPE_GET_ENV, (TupleTemplate)t, l);
		temp.setResult(t);
		return temp;
	}
	public static RespectOperation makeSetEnv(LogicTuple t, OperationCompletionListener l){
		RespectOperation temp= new RespectOperation(RespectOperation.OPTYPE_SET_ENV, (TupleTemplate)t, l);
		temp.setResult(t);
		return temp;
	}
	public LogicTuple toTuple(){
		LogicTuple t;
		if (isOperationCompleted()){
			t = getLogicTupleResult();
		} else {
			t = getLogicTupleArgument();
		}
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
		} else if (isOut_s()){
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
				new TupleArgument(t.toTerm()));
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
	
