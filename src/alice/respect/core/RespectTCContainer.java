package alice.respect.core;

import java.net.InetAddress;
import java.net.UnknownHostException;

import alice.respect.api.IBlockingContext;
import alice.respect.api.IBlockingSpecContext;
import alice.respect.api.ILinkContext;
import alice.respect.api.IManagementContext;
import alice.respect.api.INonBlockingContext;
import alice.respect.api.INonBlockingSpecContext;
import alice.respect.api.IRespectTC;
import alice.respect.api.ITCRegistry;
import alice.respect.api.InstantiationNotPossibleException;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.RespectTC;
import alice.respect.api.TupleCentreId;
import alice.tucson.service.InterTupleCentreContext;
import alice.tucson.service.InterTupleCentreContextStub;
import alice.tucson.service.LanguageInfo;
import alice.tucson.service.Node;
import alice.tucson.service.TucsonTCContainer;

/**
 * A Container for ReSpecT tuple centres
 * @author matteo casadei v 2.1.1
 *
 */

public class RespectTCContainer {

	private static ITCRegistry registry = new RespectLocalRegistry();
	private static boolean isTucsonPresent = false;
	
	public static LanguageInfo getLanguage(){
		LanguageInfo lInfo = new LanguageInfo("ReSpecT 2.2");
		lInfo.setCompatibleLanguage("ReSpecT 2.1");
		lInfo.setCompatibleLanguage("ReSpecT 2.0");
		return lInfo;
	}
	
	public static boolean createRespectTC(TupleCentreId id, int q) throws 
                InstantiationNotPossibleException{
		registry.addTC(new RespectTC(id,q));
		return true;
	}
	
	public static IBlockingContext getBlockingContext(TupleCentreId id) throws
                OperationNotPossibleException{
		return ((RespectTC)registry.getTC(id)).getBlockingContext();		
	}
	
	public static INonBlockingContext getNonBlockingContext(TupleCentreId id) 
                throws OperationNotPossibleException{	
		return ((RespectTC)registry.getTC(id)).getNonBlockingContext();
	}
	
	/**
	 * Allow to set the Tucson presence
	 *
	 * @param id the identifier of the agent whose events must be removed
	 */
	public static void setTucson(){
		isTucsonPresent = true;
	}
	
	public static ILinkContext getLinkContext(TupleCentreId id) 
                throws OperationNotPossibleException{
		if (!isTucsonPresent){
			//TuCSoN not set
			return ((RespectTC)registry.getTC(id)).getLinkContext();
		} else {
			try {
				//Local call (check if is created a not existent tuple centre)
				if (id.getNode().equals(InetAddress.getLocalHost())){
					return ((RespectTC)registry.getTC(id)).getLinkContext();
				}					
			} catch (UnknownHostException e) {
			}
			//Remote call with TuCSoN set
			return new RespectInterTupleCentreContextStub(id, TucsonTCContainer.getCurLanguage().getName());			
		}
	}
	
	public static INonBlockingSpecContext getNonBlockingSpecContext(TupleCentreId id)
		throws OperationNotPossibleException{
		return ((RespectTC)registry.getTC(id)).getNonBlockingSpecContext();
	}

	public static IBlockingSpecContext getBlockingSpecContext(TupleCentreId id) 
		throws OperationNotPossibleException{
		return ((RespectTC)registry.getTC(id)).getBlockingSpecContext();
	}
	
	public static IManagementContext getManagementContext(TupleCentreId id)
		throws OperationNotPossibleException{
		return ((RespectTC)registry.getTC(id)).getManagementContext();
	}
	
	
	//LS for use Interface
	public static RespectTC getVM(TupleCentreId id){
		return (RespectTC)registry.getTC(id);
	}

}
