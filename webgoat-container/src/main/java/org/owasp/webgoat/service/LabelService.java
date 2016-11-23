package org.owasp.webgoat.service;

import org.owasp.webgoat.i18n.LabelProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * <p>LabelService class.</p>
 *
 * @author zupzup
 */

@Controller
public class LabelService {

    private static final String URL_LABELS_MVC = "/service/labels.mvc";

    private static final Logger logger = LoggerFactory.getLogger(LabelService.class);

    @Autowired
    private LabelProvider labelProvider;

    /**
     * Fetches labels for given language
     * If no language is provided, the language is determined from the request headers
     * Otherwise, fall back to default language
     *
     * @param lang the language to fetch labels for (optional)
     * @return a map of labels
     * @throws Exception
     */
    @RequestMapping(path = URL_LABELS_MVC, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<Map<String, String>> fetchLabels(@RequestParam(value = "lang", required = false) String lang, HttpServletRequest request) throws Exception {
        Locale locale;
        if (StringUtils.isEmpty(lang)) {
            logger.debug("No language provided, determining from request headers");
            locale = request.getLocale();
            if (locale != null) {
                logger.debug("Locale set to {}", locale);
            }
        } else {
            locale = Locale.forLanguageTag(lang);
            logger.debug("Language provided: {} leads to Locale: {}", lang, locale);
        }
        return new ResponseEntity<>(labelProvider.getLabels(locale), HttpStatus.OK);
    }
}
