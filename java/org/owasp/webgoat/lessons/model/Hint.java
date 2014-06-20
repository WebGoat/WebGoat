/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.lessons.model;

/**
 *
 * @author rlawson
 */
public class Hint {

    private String hint;
    private String lesson;
    private int number;

    /**
     * @return the hint
     */
    public String getHint() {
        return hint;
    }

    /**
     * @param hint the hint to set
     */
    public void setHint(String hint) {
        this.hint = hint;
    }

    /**
     * @return the lesson
     */
    public String getLesson() {
        return lesson;
    }

    /**
     * @param lesson the lesson to set
     */
    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

}
