package alice.tucson.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

import alice.tucson.types.IResolver;
import alice.tucson.types.ITranslator;
import alice.tucson.types.TucsonOpType;

/**
 * @author lsemprini
 *
 */
public class TucsonAgentLanguageManager {
	private static LanguageInfo curLang;
	private static HashMap<String, LanguageInfo> languages;
	private static boolean firstTime = true;
		
	//Entry point of the container
	public static void useLanguage(String lang){
		//Load all the available languages
		if (firstTime){
			loadLanguages();
		}
		
		curLang = languages.get(lang);
		//If the language isn't available, so try with the default language
		if (curLang == null){
			//TuCSoN 1.9.0 Default language
			curLang = languages.get(TucsonOpType.DEFLANGUAGE);
		}
		log("Language choose: "+curLang.getName());
	}	
	
	private static void loadLanguages(){
		languages = new HashMap<String, LanguageInfo>();
				
		ArrayList<String> res = TucsonLanguageManager.getConfResolver();
	    ArrayList<String> tran = TucsonLanguageManager.getConfTranslator();
	    
		log("Loading available resolvers...");
	    //Dynamically load the resolvers and merge them with the languages of the containers
		for(int i=0; i<res.size();i++){
			try {
				IResolver resolv = (IResolver)createObject(TucsonOpType.RESOLVERPATH+res.get(i), new Object[0]);
				String langName = ((LanguageInfo)callMethod(resolv, "getInfo",  new Object[0])).getName();
				languages.put(langName.substring(0), (LanguageInfo)callMethod(resolv, "getInfo",  new Object[0]));
				log("Resolver "+langName+" loaded succesfully");
			} catch (Exception e) {
				log("Error: Unable to load "+res.get(i));
			}
		}
		
		log("Loading available translators...");
	    //Dynamically load the resolvers and merge them with the languages of the resolvers
		for(int i=0; i<tran.size();i++){
			try {
				ITranslator translator = (ITranslator)createObject(TucsonOpType.TRANSLATORPATH+tran.get(i), new Object[0]);
				String[] transInfo = (String[])callMethod(translator, "getInfo",  new Object[0]);
				String langFrom = transInfo[0];
				String langTo = transInfo[1];
				LanguageInfo tmp = languages.get(langFrom);
				if (tmp != null){
					tmp.setTransLanguage(langTo, TucsonOpType.TRANSLATORPATH+tran.get(i));
				} else {
					log("Error: Undefined translator "+tran.get(i));
				}
				log("Translator "+tran.get(i)+" loaded succesfully");
			} catch (Exception e) {
				log("Error: Unable to load "+tran.get(i));
			}
		}
		
		
        firstTime = false;
	}
	
	public static HashMap<String, LanguageInfo> getAvailableLanguages(){
		if (firstTime){
			loadLanguages();
		}
		return languages; 
	}
	
	public static LanguageInfo getCurLanguage(){
		return curLang;
		
	}
	
	private static void log(String m) {
		System.out.println("[TuCSoN AGENT LANGUAGE MANAGER] " + m);
	}
	
	public static Object createObject(String name, Object[] arglist) throws Exception{
		return TucsonLanguageManager.createObject(name, arglist);
	}
	
	public static Object callMethod(Object obj, String method, Object[] arglist) throws Exception{
		return TucsonLanguageManager.callMethod(obj, method, arglist);
	}
}
