/*
 * A language
 */
package model;

/**
 *
 * @author Maxime
 */
public enum Language {
    Czech("čestiná", "cs"),
    French("français", "fr");

    /**
     * Original name of the language.
     */
    private final String name;

    /**
     * Code ISO.
     */
    private final String iso;

    /**
     * Create a language with his name and ISO code.
     *
     * @param name Original name of the language.
     * @param iso Iso code.
     */
    private Language(String name, String iso) {
        this.name = name;
        this.iso = iso;
    }

    /**
     * Return the original name of the language.
     * 
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Return the Iso code of the language.
     * @return String
     */
    public String getIso() {
        return iso;
    }

}
