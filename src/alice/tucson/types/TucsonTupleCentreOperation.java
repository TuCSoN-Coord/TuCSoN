package alice.tucson.types;

import alice.tuplecentre.api.OperationTimeOutException;


public class TucsonTupleCentreOperation implements ITucsonTupleCentreOperation{

	private Object op;
	
	public TucsonTupleCentreOperation(int type){
		
	}
	
	public Object getInternalTCOpeation(){
		return op;
	}
	
	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public boolean isGetSpec() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean isIn() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean isIn_s() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean isInp() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean isInp_s() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean isOperationCompleted() {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean isOut() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isOut_s() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean isRd() {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean isRd_s() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRdp() {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean isRdp_s() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean isSetSpec() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void waitForOperationCompletion() {
		// TODO Auto-generated method stub
		
	}

	public void waitForOperationCompletion(long ms)
			throws OperationTimeOutException {
		// TODO Auto-generated method stub
		
	}

	
	
}
