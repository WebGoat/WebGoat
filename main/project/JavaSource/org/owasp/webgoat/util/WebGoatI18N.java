package org.owasp.webgoat.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import org.owasp.webgoat.session.WebgoatContext;

public class WebGoatI18N {

	private static HashMap<String,ResourceBundle> labels= new HashMap<String,ResourceBundle>();
	private static String defaultLanguage ;
	private static String currentLanguage;
	
	public WebGoatI18N(WebgoatContext context){
		Locale l = new Locale(context.getDefaultLanguage());
		WebGoatI18N.defaultLanguage=context.getDefaultLanguage();
		labels.put(context.getDefaultLanguage(),ResourceBundle.getBundle("WebGoatLabels",l));
	}
	
	public static void loadLanguage(String language){
		Locale l = new Locale(language);
		labels.put(language, ResourceBundle.getBundle("WebGoatLabels",l));
	}
	
	public static void setCurrentLanguage(String language){
		WebGoatI18N.currentLanguage=language;
	}
	
	public static String get(String strName) {
		if(labels.containsKey(WebGoatI18N.currentLanguage)){
			return labels.get(WebGoatI18N.currentLanguage).getString(strName);	
		}
		else {
			return labels.get(WebGoatI18N.defaultLanguage).getString(strName);
		}
	}

	
	
}
