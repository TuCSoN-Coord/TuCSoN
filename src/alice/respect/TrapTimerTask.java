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

import java.util.*;

/**
 * @author aricci
 */
class TrapTimerTask extends TimerTask {

	private RespectVM vm;
	private TrapGenerator gen;
	private boolean cancelled;
	
	public TrapTimerTask(RespectVM vm, TrapGenerator gen){
		this.vm = vm;
		this.gen = gen;
		cancelled = false;
	}
	
	public synchronized void run(){
		if (!cancelled){
			//System.out.println("[TRAP OCCURRED!] "+gen.getId()+" "+gen.getTuple()+" VMtime "+vm.getCurrentTime()+" - ABStime "+System.currentTimeMillis());
			TrapEvent ev = new TrapEvent(vm.getCurrentTime(),gen.getTuple());
			vm.addTrapEvent(ev);
			vm.removeTrapGenerator(gen.getId());
		} else {
			//System.out.println("[TRAP SKIPPED] ");
		}
	}
	
	public synchronized boolean cancel(){
		cancelled = true;
		return super.cancel();
	}
	
	public boolean isCancelled(){
		return cancelled;
	}
	
	public long getScheduledTime(){
		return gen.getScheduledTime();
	}
}

