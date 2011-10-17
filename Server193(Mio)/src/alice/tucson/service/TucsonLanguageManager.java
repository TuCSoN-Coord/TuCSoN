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
public class TucsonLanguageManager
{
	public static final String RESOLVERPATH = "alice.tucson.resolver.";
	public static final String RESPECT22 = "ReSpecT 2.2";		
	public static final String DEFLANGUAGE = RESPECT22;
	
	public static ArrayList<String> getConfLanguage()
	{
		// Load the properties specified in the config file
		Properties prop = new Properties();

		try
		{
			prop.load(new FileInputStream("tucson_conf.properties"));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// Load the languages
		String languages = prop.getProperty("languages");	
		StringTokenizer st = new StringTokenizer(languages, ",");
		ArrayList<String> lang = new ArrayList<String>();

		while (st.hasMoreTokens())
		{
			String language = st.nextToken();
			lang.add(language);
		}
		return lang;
	}
	
	public static ArrayList<String> getConfContainer()
	{
		// Load the properties specified in the config file
		Properties prop = new Properties();

		try
		{
			prop.load(new FileInputStream("tucson_conf.properties"));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// Load the containers
		String containers = prop.getProperty("containers");	
		StringTokenizer st = new StringTokenizer(containers, ",");
		ArrayList<String> cont = new ArrayList<String>();

		while (st.hasMoreTokens())
		{
			String container = st.nextToken();
			cont.add(container);
		}
		return cont;
	}

	public static ArrayList<String> getConfResolver()
	{
		// Load the properties specified in the config file
		Properties prop = new Properties();
		try
		{
			prop.load(new FileInputStream("tucson_conf.properties"));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		String resolvers = prop.getProperty("resolvers");	
		StringTokenizer st = new StringTokenizer(resolvers, ",");
		ArrayList<String> res = new ArrayList<String>();

		while (st.hasMoreTokens())
		{
			String resolver = st.nextToken();
			res.add(resolver);
		}
		return res;
	}

	public static Object createObject(String name, Object[] arglist) throws Exception
	{
		Class cls = Class.forName(name);
		Class partypes[] = new Class[arglist.length];

		for (int i = 0; i < arglist.length; i++)
		{
			partypes[i] = arglist[i].getClass();

		}

		Constructor ct = cls.getConstructor(partypes);
		Object retobj = ct.newInstance(arglist);

		return retobj;
	}

	public static Object callMethod(Object obj, String method, Object[] arglist) throws Exception
	{
		Class cls = obj.getClass();
		System.out.println("Nome classe " + obj.getClass());
		
		Class partypes[] = new Class[arglist.length];
		
		for (int i = 0; i < arglist.length; i++)
		{
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
	public static Object callMethodObjPar(Object obj, String method, Object[] arglist) throws Exception
	{
		Class cls = obj.getClass();
		Class partypes[] = new Class[arglist.length];

		for (int i = 0; i < arglist.length; i++)
		{
			partypes[i] = new Object().getClass();
		}

		Method meth = cls.getMethod(method, partypes);
		Object retobj = meth.invoke(obj, arglist);

		return retobj;
	}

	public static Object callStaticMethod(String objName, String method, Object[] arglist) throws Exception
	{
		Class cls = Class.forName(objName);
		Class partypes[] = new Class[arglist.length];
		
		for (int i = 0; i < arglist.length; i++)
		{
			partypes[i] = arglist[i].getClass();
		}
		
		Method meth = cls.getMethod(method, partypes);
		
		Object retobj = meth.invoke(null, arglist);
		return retobj;
	}
}
