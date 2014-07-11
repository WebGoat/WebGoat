/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.lessons.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rlawson
 */
public class LessonMenuItem {

    private String name;
    private LessonMenuItemType type;
    private List<LessonMenuItem> children = new ArrayList<LessonMenuItem>();
    private boolean complete;
    private String link;   

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
        this.name = name;
    }

    /**
     * @return the children
     */
    public List<LessonMenuItem> getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(List<LessonMenuItem> children) {
        this.children = children;
    }

    /**
     * @return the type
     */
    public LessonMenuItemType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(LessonMenuItemType type) {
        this.type = type;
    }

    public void addChild(LessonMenuItem child) {
        children.add(child);
    }

    @Override
    public String toString() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("Name: ").append(name).append(" | ");
        bldr.append("Type: ").append(type).append(" | ");
        return bldr.toString();
    }

    /**
     * @return the complete
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * @param complete the complete to set
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

}
