package alice.tucson.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author lsemprini
 *
 */
public class TucsonLanguageManager {

	public static ArrayList<String> getConfContainer(){
		//Load the properties specified in the config file
		Properties prop = new Properties();
        try {
			prop.load(new FileInputStream("tucson_conf.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Load the containers
		String containers = prop.getProperty("containers");
		StringTokenizer st = new StringTokenizer(containers);
		ArrayList<String> cont = new ArrayList<String>();
	    while (st.hasMoreTokens()) {
	    	String temp = st.nextToken().substring(0);
	    	cont.add(temp.substring(0, temp.length()-1));
	    }
	    return cont;
	}
	
	public static ArrayList<String> getConfResolver(){
		//Load the properties specified in the config file
		Properties prop = new Properties();
        try {
			prop.load(new FileInputStream("tucson_conf.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Load the resolvers(component for Agent Module interaction like AgentId, TupleCentreId, LogicTuple, ecc.)
		String resolvers = prop.getProperty("resolvers");
		StringTokenizer st = new StringTokenizer(resolvers);
		ArrayList<String> res = new ArrayList<String>();
	    while (st.hasMoreTokens()) {
	    	String temp = st.nextToken().substring(0);
	    	res.add(temp.substring(0, temp.length()-1));	    	
	    }
	    return res;
	}
	
	public static ArrayList<String> getConfTranslator(){
		//Load the properties specified in the config file
		Properties prop = new Properties();
        try {
			prop.load(new FileInputStream("tucson_conf.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Load the translators
		String translators = prop.getProperty("translators");
		StringTokenizer st = new StringTokenizer(translators);
		ArrayList<String> tran = new ArrayList<String>();
	    while (st.hasMoreTokens()) {
	    	String temp = st.nextToken().substring(0);
	    	tran.add(temp.substring(0, temp.length()-1));	    	
	    }
	    return tran;
	}
	
	public static Object createObject(String name, Object[] arglist) throws Exception{
		Class cls = Class.forName(name);
		Class partypes[] = new Class[arglist.length];
		for (int i=0;i<arglist.length;i++){
			partypes[i] = arglist[i].getClass();
		}
		Constructor ct = cls.getConstructor(partypes);
		Object retobj = ct.newInstance(arglist);
		return retobj;
	}

	public static Object callMethod(Object obj, String method, Object[] arglist) throws Exception{
		Class cls = Class.forName(obj.getClass().getName());
		Class partypes[] = new Class[arglist.length];
		for (int i=0;i<arglist.length;i++){
			partypes[i] = arglist[i].getClass();
		}
		Method meth = cls.getMethod(method, partypes);
		Object retobj = meth.invoke(obj, arglist);
		return retobj;
	}
	
		
	/**
	 * call a method with a forced list of Object parameters
	 *
	 */
	public static Object callMethodObjPar(Object obj, String method, Object[] arglist) throws Exception{
		Class cls = Class.forName(obj.getClass().getName());
		Class partypes[] = new Class[arglist.length];
		for (int i=0;i<arglist.length;i++){
			partypes[i] = new Object().getClass();
		}
		Method meth = cls.getMethod(method, partypes);
		Object retobj = meth.invoke(obj, arglist);
		return retobj;
	}
	
	public static Object callStaticMethod(String objName, String method, Object[] arglist) throws Exception{
		Class cls = Class.forName(objName);
		Class partypes[] = new Class[arglist.length];
		for (int i=0;i<arglist.length;i++){
			partypes[i] = arglist[i].getClass();
		}
		Method meth = cls.getMethod(method, partypes);
		Object retobj = meth.invoke(null, arglist);
		return retobj;
		
	}
}
