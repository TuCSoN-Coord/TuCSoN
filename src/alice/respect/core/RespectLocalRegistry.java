package alice.respect.core;

import java.util.HashMap;
import alice.respect.api.IRespectTC;
import alice.respect.api.ITCRegistry;
import alice.respect.api.InstantiationNotPossibleException;
import alice.respect.api.RespectTC;
import alice.respect.api.TupleCentreId;

public class RespectLocalRegistry implements ITCRegistry {

	/**
	 * internal representation of the registry, keys are
	 * tuple centre id (as String)
	 */
	private HashMap reg;
	/**
	 * default queue size for tuple centre implicitly created
	 * via getBlockingContext
	 */
	private int QUEUE_SIZE = 10;

	public RespectLocalRegistry(){
		this.reg = new HashMap();
	}

	/**
	 * Add a tuple centre to the registry. 
	 * WARNING: If a tuple centre with the same name yet exists 
	 * the specified tuple centre is not added to the registry.
	 * 
	 * @param tc the tuple centre to be added to the registry
	 */
	public void addTC(IRespectTC tc) {
		//System.out.println("********ID:********** "+tc.getId());
		/*if (!reg.containsKey(tc.getId()))
                reg.put(tc.getId().toString(), tc);
            System.out.println("---------------------Added "+tc.getId().toString());*/
		if (!reg.containsKey(tc.getId().getName()))
			reg.put(tc.getId().getName(), tc);
		//System.out.println("---------------------Added "+tc.getId().toString());
		//System.out.println("********SIZE:********** "+this.getSize());	
	}

	public IRespectTC getTC(TupleCentreId id)throws InstantiationNotPossibleException{
		//System.out.println("********ID:********** "+id+" "+reg.containsKey(id));
		/*
		 * if the tc "does not exist" yet is 
		 * created first and then then it is retrieved
		 * as getTC always succeed
		 */
		/*if (!reg.containsKey(id.toString())) {
			this.addTC(new RespectTC(id, QUEUE_SIZE));
		}
		return (IRespectTC)reg.get(id.toString());*/
		if (!reg.containsKey(id.getName())) {
			//this.addTC(new RespectTC(id, QUEUE_SIZE));
			throw new InstantiationNotPossibleException();
		}
		return (IRespectTC)reg.get(id.getName());
	}

	public int getSize(){
		return reg.size();
	}

}
