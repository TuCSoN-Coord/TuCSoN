package alice.tucson.service;

import alice.tucson.types.ITCIdResolver;

public class Test
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		TucsonNodeLanguageManager.useLanguage("ciao");

		// System.out.println(TucsonTCContainer.getCurLanguage().getName());
		// TucsonTCContainer.attachTucson();

		// ITCIdResolver obj = (ITCIdResolver)
		// TucsonNodeLanguageManager.createObject("alice.tucson.resolver.Respect22Resolver",
		// new Object[0]);
		// System.out.println("a "+obj.getInfo().getName());
		// System.out.println("b "+((LanguageInfo)TucsonNodeLanguageManager.callMethod(obj,
		// "getInfo", new Object[0])).getName());
	}

}
