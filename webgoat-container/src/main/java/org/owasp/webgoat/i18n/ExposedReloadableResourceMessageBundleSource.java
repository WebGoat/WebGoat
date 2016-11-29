package org.owasp.webgoat.i18n;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;
import java.util.Properties;

/**
 * <p>ExposedReloadableResourceMessageBundleSource class.</p>
 * Extends the reloadable message source with a way to get all messages
 *
 * @author zupzup
 */

public class ExposedReloadableResourceMessageBundleSource extends ReloadableResourceBundleMessageSource {
    /**
     * Gets all messages for presented Locale.
     * @param locale user request's locale
     * @return all messages
     */
    public Properties getMessages(Locale locale) {
        return getMergedProperties(locale).getProperties();
    }
}
