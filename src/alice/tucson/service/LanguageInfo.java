package alice.tucson.service;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class LanguageInfo implements Serializable{

	private String languageName;
	private ArrayList<String> compatibleLanguages;
	private ArrayList<String> transLanguages;
	private ArrayList<String> transLanguagesComponent;
	private String componentName;
	
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
	
	public void setComponentName(String comp){
		componentName = comp;
	}
	
	public String getName(){
		return languageName;
	}
		
	public ArrayList<String> getCompatibleLanguages(){
		return compatibleLanguages;
	}
	
	public String getComponentName(){
		return componentName;
	}
	
	public ArrayList<String> getTransLanguages(){
		return transLanguages;
	}
	
	public String getTransLanguageComponent(String transLanguage){
		return transLanguagesComponent.get(transLanguages.indexOf(transLanguage));
	}
	
	
}
