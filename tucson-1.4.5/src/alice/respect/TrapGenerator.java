/*
 * ReSpecT Framework - aliCE team at deis.unibo.it
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
package alice.respect;

import alice.logictuple.*;

class TrapGenerator {

	private long time;
	private LogicTuple tuple;
	private long id; 
	private TrapTimerTask task;
	private RespectVM vm;
	
	public TrapGenerator(long id, long time, LogicTuple tuple, RespectVM vm){
		this.id=id;
		this.time=time;
		this.tuple=tuple;
		this.vm=vm;
	}
	
	public void install(){
		task = new TrapTimerTask(vm,this);
		vm.installTrapTimerTask(task);
	}
	
	public void kill(){
		if (task!=null){
			task.cancel();
		}
	}
	
	public long getScheduledTime(){
		return time;
	}
	
	public long getId(){
		return id;
	}
	
	public LogicTuple getTuple(){
		return tuple;
	}
}
