/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
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
package alice.tucson.api;

import alice.logictuple.*;

public class TucsonContextSynch {

    private TucsonContext ctx;
    
    public TucsonContextSynch(TucsonContext ctx){
    		this.ctx = ctx;
    }
    
    public void out(TupleCentreId tid, LogicTuple t) throws  OperationNotAllowedException, UnreachableNodeException {
    		TucsonOpId opId = ctx.out(tid,t);
    		ctx.waitForCompletion(opId);
    }

    public LogicTuple in(TupleCentreId tid, LogicTuple t) throws  OperationNotAllowedException, UnreachableNodeException {
		TucsonOpId opId = ctx.in(tid,t);
		ContextEvent ev = ctx.waitForCompletion(opId);
		return ev.getTuple();
    }

    public LogicTuple rd(TupleCentreId tid, LogicTuple t) throws  OperationNotAllowedException, UnreachableNodeException {
		TucsonOpId opId = ctx.rd(tid,t);
		ContextEvent ev = ctx.waitForCompletion(opId);
		return ev.getTuple();
    }

    public LogicTuple inp(TupleCentreId tid, LogicTuple t) throws  OperationNotAllowedException, UnreachableNodeException {
		TucsonOpId opId = ctx.inp(tid,t);
		ContextEvent ev = ctx.waitForCompletion(opId);
		return ev.getTuple();
    }

    public LogicTuple rdp(TupleCentreId tid, LogicTuple t) throws  OperationNotAllowedException, UnreachableNodeException {
		TucsonOpId opId = ctx.rdp(tid,t);
		ContextEvent ev = ctx.waitForCompletion(opId);
		return ev.getTuple();
    }
    

    public void setSpec(TupleCentreId tid, String spec) throws  OperationNotAllowedException, UnreachableNodeException, InvalidSpecificationException {
        TucsonOpId opId = ctx.setSpec(tid,spec);
    		ContextEvent ev = ctx.waitForCompletion(opId);
    		if (!ev.operationSucceeded()){
    			throw new InvalidSpecificationException();
    		}
    }

    public String getSpec(TupleCentreId tid) throws  OperationNotAllowedException, UnreachableNodeException {
    		TucsonOpId opId = ctx.getSpec(tid);
		ContextEvent ev = ctx.waitForCompletion(opId);
		return ev.getSpec();
    }

    public void exit() throws  OperationNotAllowedException {
    		ctx.exit();
    }
}
