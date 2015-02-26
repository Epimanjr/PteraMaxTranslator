/*
 * A language
 */
package model;

import java.util.Objects;

/**
 *
 * @author Maxime
 */
public class Language {
    

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
    public Language(String name, String iso) {
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.iso);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Language other = (Language) obj;
        if (!Objects.equals(this.iso, other.iso)) {
            return false;
        }
        return true;
    }

    
}
