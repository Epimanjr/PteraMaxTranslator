/*
 * Manage a word.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author Maxime BLAISE
 */
public class Word implements Serializable {

    /**
     * Language of the word.
     */
    private Language language;

    /**
     * Id of the word.
     */
    private int id;

    /**
     * Name of the word.
     */
    private String name;

    /**
     * Gender of the word.
     */
    private String gender;

    /**
     * Phonetic of the word.
     */
    private String phonetic;

    /**
     * Create a word with a specific name.
     *
     * @param name Name of the word.
     */
    public Word(String name) {
        this.name = name;
    }

    /**
     * Create a word with all parameters.
     *
     * @param language Language of the word.
     * @param id Id of the word.
     * @param name Name of the word.
     * @param gender Gender of the word.
     * @param phonetic Phonetic of the word.
     */
    public Word(Language language, int id, String name, String gender, String phonetic) {
        this.language = language;
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.phonetic = phonetic;
    }

    /* GETTERS AND SETTERS */
    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

}
