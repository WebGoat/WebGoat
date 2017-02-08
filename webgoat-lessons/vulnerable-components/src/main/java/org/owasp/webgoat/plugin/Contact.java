package org.owasp.webgoat.plugin;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("contact")
public class Contact {
    @XStreamAlias("name")
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}