/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.lessons;

import java.util.ArrayList;
import java.util.List;

public class LessonMenuItem {

  private String name;
  private LessonMenuItemType type;
  private List<LessonMenuItem> children = new ArrayList<>();
  private boolean complete;
  private String link;
  private int ranking;

  /**
   * Getter for the field <code>name</code>.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Setter for the field <code>name</code>.
   *
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getter for the field <code>children</code>.
   *
   * @return the children
   */
  public List<LessonMenuItem> getChildren() {
    return children;
  }

  /**
   * Setter for the field <code>children</code>.
   *
   * @param children the children to set
   */
  public void setChildren(List<LessonMenuItem> children) {
    this.children = children;
  }

  /**
   * Getter for the field <code>type</code>.
   *
   * @return the type
   */
  public LessonMenuItemType getType() {
    return type;
  }

  /**
   * Setter for the field <code>type</code>.
   *
   * @param type the type to set
   */
  public void setType(LessonMenuItemType type) {
    this.type = type;
  }

  /**
   * addChild.
   *
   * @param child a {@link LessonMenuItem} object.
   */
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
   * isComplete.
   *
   * @return the complete
   */
  public boolean isComplete() {
    return complete;
  }

  /**
   * Setter for the field <code>complete</code>.
   *
   * @param complete the complete to set
   */
  public void setComplete(boolean complete) {
    this.complete = complete;
  }

  /**
   * Getter for the field <code>link</code>.
   *
   * @return the link
   */
  public String getLink() {
    return link;
  }

  /**
   * Setter for the field <code>link</code>.
   *
   * @param link the link to set
   */
  public void setLink(String link) {
    this.link = link;
  }

  public void setRanking(int ranking) {
    this.ranking = ranking;
  }

  public int getRanking() {
    return this.ranking;
  }
}
