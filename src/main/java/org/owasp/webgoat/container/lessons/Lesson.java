/*
 * SPDX-FileCopyrightText: Copyright © 2008 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.lessons;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Lesson {

  private List<Assignment> assignments = new ArrayList<>();

  public void addAssignment(Assignment assignment) {
    this.assignments.add(assignment);
  }

  /**
   * getName.
   *
   * @return a {@link java.lang.String} object.
   */
  public LessonName getName() {
    String className = getClass().getName();
    return new LessonName(className.substring(className.lastIndexOf('.') + 1));
  }

  /**
   * Gets the category attribute of the Lesson object
   *
   * @return The category value
   */
  public Category getCategory() {
    return getDefaultCategory();
  }

  /**
   * getDefaultCategory.
   *
   * @return a {@link org.owasp.webgoat.container.lessons.Category} object.
   */
  protected abstract Category getDefaultCategory();

  /**
   * Gets the title attribute of the HelloScreen object
   *
   * @return The title value
   */
  public abstract String getTitle();

  /**
   * Returns the default "path" portion of a lesson's URL.
   *
   * <p>
   *
   * <p>Legacy webgoat lesson links are of the form "attack?Screen=Xmenu=Ystage=Z". This method
   * returns the path portion of the url, i.e., "attack" in the string above.
   *
   * <p>Newer, Spring-Controller-based classes will override this method to return "*.do"-styled
   * paths.
   *
   * @return a {@link java.lang.String} object.
   */
  protected String getPath() {
    return "#lesson/";
  }

  /**
   * Get the link that can be used to request this screen.
   *
   * <p>Rendering the link in the browser may result in Javascript sending additional requests to
   * perform necessary actions or to obtain data relevant to the lesson or the element of the lesson
   * selected by the user. Thanks to using the hash mark "#" and Javascript handling the clicks, the
   * user will experience less waiting as the pages do not have to reload entirely.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getLink() {
    return String.format("%s%s.lesson", getPath(), getId());
  }

  /**
   * Description of the Method
   *
   * @return Description of the Return Value
   */
  public String toString() {
    return getTitle();
  }

  public final String getId() {
    return this.getClass().getSimpleName();
  }

  /**
   * This is used in Thymeleaf to construct the HTML to load the lesson content from. See
   * lesson_content.html
   */
  public final String getPackage() {
    var packageName = this.getClass().getPackageName();
    // package name is the direct package name below lessons (any subpackage will be removed)
    return packageName.replaceAll("org.owasp.webgoat.lessons.", "").replaceAll("\\..*", "");
  }
}
