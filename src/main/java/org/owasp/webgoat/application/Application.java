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
 */
public class Application {

    private static final Application INSTANCE = new Application();

    private Application() {

    }

    public static final Application getInstance() {
        return INSTANCE;
    }

    private String version = "SNAPSHOT";
    private String build = "local";
    private String name = "WebGoat";

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        if (StringUtils.isNotBlank(version)) {
            this.version = version;
        }
    }

    /**
     * @return the build
     */
    public String getBuild() {
        return build;
    }

    /**
     * @param build the build to set
     */
    public void setBuild(String build) {
        if (StringUtils.isNotBlank(build)) {
            this.build = build;
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        if (StringUtils.isNotBlank(name)) {
            this.name = name;
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("name", name).
                append("version", version).
                append("build", build).
                toString();
    }
}
