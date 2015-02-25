
package model;

import java.util.HashMap;

/**
 *
 * @author Maxime BLAISE
 */
public class List extends HashMap<Word, Word> {


    
    /**
     * The current dictionary
     */
    private final Dictionary czech;

    /**
     * Constructor.
     *
     * @param czech Current dictionary.
     */
    public List(Dictionary czech) {
        this.czech = czech;
    }

}
