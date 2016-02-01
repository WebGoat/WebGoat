package org.owasp.webgoat.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import org.owasp.webgoat.session.WebgoatContext;

@Deprecated
/**
 * <p>WebGoatI18N class.</p>
 *
 * @version $Id: $Id
 * @author dm
 */
public class WebGoatI18N
{

	private static HashMap<Locale, ResourceBundle> labels = new HashMap<Locale, ResourceBundle>();
	private static Locale currentLocale;
	private static WebGoatResourceBundleController localeController;
	
	/**
	 * <p>Constructor for WebGoatI18N.</p>
	 *
	 * @param context a {@link org.owasp.webgoat.session.WebgoatContext} object.
	 */
	public WebGoatI18N(WebgoatContext context)
	{
		currentLocale = new Locale(context.getDefaultLanguage());
		localeController = new WebGoatResourceBundleController(currentLocale);
	}
	
	/**
	 * <p>loadLanguage.</p>
	 *
	 * @param language a {@link java.lang.String} object.
	 */
	@Deprecated
	public static void loadLanguage(String language)
	{
		// Do nothing
	}
	
	/**
	 * <p>Setter for the field <code>currentLocale</code>.</p>
	 *
	 * @param locale a {@link java.util.Locale} object.
	 */
	public static void setCurrentLocale(Locale locale)
	{
		if (!currentLocale.equals(locale))
		{
			if (!labels.containsKey(locale))
			{
				ResourceBundle resBundle = ResourceBundle.getBundle("WebGoatLabels", locale, localeController);
				labels.put(locale, resBundle);
			}
			WebGoatI18N.currentLocale = locale;
		}
	}

	/**
	 * <p>get.</p>
	 *
	 * @param strName a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String get(String strName)
	{
		return labels.get(WebGoatI18N.currentLocale).getString(strName);
	}

	private static class WebGoatResourceBundleController extends ResourceBundle.Control
	{
		private Locale fallbackLocale;

		public WebGoatResourceBundleController(Locale l)
		{
			fallbackLocale = l;
		}
	
		@Override
		public Locale getFallbackLocale(String baseName, Locale locale)
		{
			if(! fallbackLocale.equals(locale)) {
				return fallbackLocale;
			}
			return Locale.ROOT;
		}
	}
	
}
