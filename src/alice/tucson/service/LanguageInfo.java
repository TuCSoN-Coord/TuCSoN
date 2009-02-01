package alice.tucson.service;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class LanguageInfo implements Serializable{

	private String languageName;
	private ArrayList<String> compatibleLanguages;
	private ArrayList<String> transLanguages;
	private ArrayList<String> transLanguagesComponent;
	private String containerComponentName;
	private String resolverComponentName;
	
	public LanguageInfo(String languageName){
		this.languageName = languageName;
		compatibleLanguages =  new ArrayList<String>();
		transLanguages = new ArrayList<String>();
		transLanguagesComponent = new ArrayList<String>();
	}

	public void setCompatibleLanguage(String language){
		compatibleLanguages.add(language);
	}
	
	public void setTransLanguage(String language, String comp){
		transLanguages.add(language);
		transLanguagesComponent.add(comp);
	}
	
	public void setContainerComponentName(String comp){
		containerComponentName = comp;
	}
	
	public void setResolverComponentName(String comp){
		resolverComponentName = comp;
	}
	
	public String getName(){
		return languageName;
	}
		
	public ArrayList<String> getCompatibleLanguages(){
		return compatibleLanguages;
	}
	
	public String getContainerComponentName(){
		return containerComponentName;
	}
	
	public String getResolverComponentName(){
		return resolverComponentName;
	}
	
	public ArrayList<String> getTranslatorLanguages(){
		return transLanguages;
	}
	
	public String getTranslatorComponentName(String transLanguage){
		int index = transLanguages.indexOf(transLanguage);
		String result = null;
		if (index != -1){
			result = transLanguagesComponent.get(index);
		} else {
			return null;
		}
		return result;
	}
	
	
}
