package alice.respect24.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import alice.respect.api.IBlockingContext;
import alice.respect.api.IBlockingSpecContext;
import alice.respect.api.ILinkContext;
import alice.respect.api.IManagementContext;
import alice.respect.api.INonBlockingContext;
import alice.respect.api.INonBlockingSpecContext;
import alice.respect.api.ITCRegistry;
import alice.respect.api.InstantiationNotPossibleException;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.RespectTC;
import alice.respect.api.TupleCentreId;
import alice.tucson.service.LanguageInfo;
import alice.tucson.service.TucsonNodeLanguageManager;

/**
 * A Container for ReSpecT tuple centres
 * @author matteo casadei v 2.1.1
 *
 */

public class RespectTCContainer{

	
	public static LanguageInfo getLanguage(){
		LanguageInfo lInfo = new LanguageInfo("ReSpecT 2.4");
		lInfo.setCompatibleLanguage("ReSpecT 2.3");
		return lInfo;
	}
	
	

}
