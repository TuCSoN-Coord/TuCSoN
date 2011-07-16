package alice.tucson.service;

import java.util.ArrayList;
import java.util.HashMap;

public class TucsonAgentLanguageManager
{
	private static LanguageInfo curLang;
	private static HashMap<String, LanguageInfo> languages;
	private static boolean firstTime = true;

	// Entry point of the container
	public static void useLanguage(String lang)
	{
		// Load all the available languages
		if (firstTime)
		{
			loadLanguages();
		}

		curLang = languages.get(lang);
		// If the language isn't available, so try with the default language
		if (curLang == null)
		{
			// TuCSoN 1.9.1 Default language
			curLang = languages.get(TucsonLanguageManager.DEFLANGUAGE);
		}
		
		log("Language choose: " + curLang.getName());		
	}

	private static void loadLanguages()
	{
		languages = new HashMap<String, LanguageInfo>();

		ArrayList<String> lang = TucsonLanguageManager.getConfLanguage();
		log("Loading available languages ...");
		ArrayList<String> res = TucsonLanguageManager.getConfResolver();
		log("Loading available resolvers ...");
		
		LanguageInfo language = null;
		
		for (int i = 0; i < lang.size(); i++)
		{			
			try 
			{
				language = new LanguageInfo(lang.get(i));
				log("Language " + language.getName() + " loaded succesfully");
				language.setResolverComponentName(TucsonLanguageManager.RESOLVERPATH + res.get(i));
				log("Resolver " + language.getName() + " loaded succesfully");

				languages.put(lang.get(i), language);																				
			} 
			catch (Exception e) 
			{
				log("Error: Unable to load " + language.getName());
			}		
		}		
		firstTime = false;
	}

	public static HashMap<String, LanguageInfo> getAvailableLanguages()
	{
		if (firstTime)
		{
			loadLanguages();
		}
		return languages;
	}

	public static LanguageInfo getCurLanguage()
	{
		return curLang;
	}

	private static void log(String m)
	{
		System.out.println("[TuCSoN AGENT LANGUAGE MANAGER] " + m);
	}

	public static Object createObject(String name, Object[] arglist) throws Exception
	{
		return TucsonLanguageManager.createObject(name, arglist);
	}

	public static Object callMethod(Object obj, String method, Object[] arglist) throws Exception
	{
		return TucsonLanguageManager.callMethod(obj, method, arglist);
	}
	
	public static Object callStaticMethod(String objName, String method, Object[] arglist) throws Exception
	{
		return TucsonLanguageManager.callStaticMethod(objName, method, arglist);
	}
}
