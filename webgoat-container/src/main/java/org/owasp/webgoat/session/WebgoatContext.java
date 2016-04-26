package org.owasp.webgoat.session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * <p>WebgoatContext class.</p>
 *
 * @version $Id: $Id
 * @author dm
 */
@Configuration
public class WebgoatContext {

    @Value("${webgoat.database.connection.string}")
    private String databaseConnectionString;

    private String realConnectionString = null;

    @Value("${webgoat.database.driver}")
    private String databaseDriver;

    private String databaseUser;

    private String databasePassword;

    private boolean showCookies = false;

    private boolean showParams = false;

    private boolean showRequest = false;

    private boolean showSource = false;

    private boolean showSolution = false;

    private boolean enterprise = false;

    private boolean codingExercises = false;

    @Value("${webgoat.feedback.address}")
    private String feedbackAddress;

    @Value("${webgoat.feedback.address.html}")
    private String feedbackAddressHTML = "";

    private boolean isDebug = false;

    @Value("${webgoat.default.language}")
    private String defaultLanguage;

    /**
     * returns the connection string with the real path to the database
     * directory inserted at the word PATH
     *
     * @return The databaseConnectionString value
     */
    public String getDatabaseConnectionString() {
        return this.databaseConnectionString;
    }

    /**
     * Gets the databaseDriver attribute of the WebSession object
     *
     * @return The databaseDriver value
     */
    public String getDatabaseDriver() {
        return (databaseDriver);
    }

    /**
     * Gets the databaseUser attribute of the WebSession object
     *
     * @return The databaseUser value
     */
    public String getDatabaseUser() {
        return (databaseUser);
    }

    /**
     * Gets the databasePassword attribute of the WebSession object
     *
     * @return The databasePassword value
     */
    public String getDatabasePassword() {
        return (databasePassword);
    }

    /**
     * <p>isEnterprise.</p>
     *
     * @return a boolean.
     */
    public boolean isEnterprise() {
        return enterprise;
    }

    /**
     * <p>isCodingExercises.</p>
     *
     * @return a boolean.
     */
    public boolean isCodingExercises() {
        return codingExercises;
    }

    /**
     * <p>Getter for the field <code>feedbackAddress</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFeedbackAddress() {
        return feedbackAddress;
    }

    /**
     * <p>Getter for the field <code>feedbackAddressHTML</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFeedbackAddressHTML() {
        return feedbackAddressHTML;
    }

    /**
     * <p>isDebug.</p>
     *
     * @return a boolean.
     */
    public boolean isDebug() {
        return isDebug;
    }

    /**
     * <p>isShowCookies.</p>
     *
     * @return a boolean.
     */
    public boolean isShowCookies() {
        return showCookies;
    }

    /**
     * <p>isShowParams.</p>
     *
     * @return a boolean.
     */
    public boolean isShowParams() {
        return showParams;
    }

    /**
     * <p>isShowRequest.</p>
     *
     * @return a boolean.
     */
    public boolean isShowRequest() {
        return showRequest;
    }

    /**
     * <p>isShowSource.</p>
     *
     * @return a boolean.
     */
    public boolean isShowSource() {
        return showSource;
    }

    /**
     * <p>isShowSolution.</p>
     *
     * @return a boolean.
     */
    public boolean isShowSolution() {
        return showSolution;
    }

    /**
     * <p>Getter for the field <code>defaultLanguage</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDefaultLanguage() {
        return defaultLanguage;
    }
}
