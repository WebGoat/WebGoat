/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.application;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Singleton which is created on context startup
 *
 * @author rlawson
 * @version $Id: $Id
 */
public class Application {

    private static final Application INSTANCE = new Application();

    private Application() {

    }

    /**
     * <p>getInstance.</p>
     *
     * @return a {@link org.owasp.webgoat.application.Application} object.
     */
    public static final Application getInstance() {
        return INSTANCE;
    }

    private String version = "SNAPSHOT";
    private String build = "local";
    private String name = "WebGoat";

    /**
     * <p>Getter for the field <code>version</code>.</p>
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * <p>Setter for the field <code>version</code>.</p>
     *
     * @param version the version to set
     */
    public void setVersion(String version) {
        if (StringUtils.isNotBlank(version)) {
            this.version = version;
        }
    }

    /**
     * <p>Getter for the field <code>build</code>.</p>
     *
     * @return the build
     */
    public String getBuild() {
        return build;
    }

    /**
     * <p>Setter for the field <code>build</code>.</p>
     *
     * @param build the build to set
     */
    public void setBuild(String build) {
        if (StringUtils.isNotBlank(build)) {
            this.build = build;
        }
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name the name to set
     */
    public void setName(String name) {
        if (StringUtils.isNotBlank(name)) {
            this.name = name;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("name", name).
                append("version", version).
                append("build", build).
                toString();
    }
}
