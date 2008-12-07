package alice.tucson.service;

import java.util.ArrayList;

import alice.respect.api.AgentId;
import alice.respect.api.TupleCentreId;
import alice.tucson.api.exception.LanguageNotFoundException;

	public class TucsonResolver {

		private ArrayList<LanguageInfo> languages;

		public TucsonResolver(ArrayList<LanguageInfo> languages){
			this.languages = languages;
		}
		

		/*
		public Object translateTCIdSyntax(Object tid, int type, String languageFrom, LanguageInfo languageTo) throws LanguageNotFoundException{
			boolean search = isEquals(languageFrom,languageTo);
			if (search == true){
				//No translator needed
				return tid;
			} else {
				//Search a translator
				String translator = searchLanguageTrans(languageTo);
				//TODO chiama il translator dinamicamente con reflection
				return tid;
			}
		}
		
		public Object translateTupleSyntax(Object t, int type, String languageFrom, LanguageInfo languageTo) throws LanguageNotFoundException{
			boolean search = isEquals(languageFrom,languageTo);
			if (search == true){
				//No translator needed
				return t;
			} else {
				//Search a translator
				String translator = searchLanguageTrans(languageTo);
				//TODO chiama il translator dinamicamente con reflection
				return t;
			}
		}*/
		
		private boolean isEquals(String languageFrom, LanguageInfo languageTo){
			//TODO 2 while, appena becco il languageFrom allora lo confronto con il languageTo e con i suoi l. comp
			
			/*
			String compName = null;
			LanguageInfo curLang = null;
			String curLangString = null;
			Iterator<LanguageInfo> it = languages.iterator();
			while (it.hasNext()){
				curLang = it.next();
				if (curLang.getName().equals(languageTo.getName())){
					compName = curLang.getComponentName();
					break;					
				}
				Iterator<String> it2 = curLang.getCompatibleLanguages().iterator();
				while(it2.hasNext()){
					curLangString = it2.next();
					if (curLangString.equals(languageTo.getName())){
						compName = curLang.getComponentName();
						break;					
					}
				}
				if (compName != null)
					break;
			}
			if (compName != null){
				
			}*/
			return true;
		}
		
		private String searchLanguageTrans(LanguageInfo language){
			//TODO cerco uno alla volta i languageTo tra i langTranslator
			return null;
		}
		
		
	}
