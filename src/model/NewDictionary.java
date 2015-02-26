package model;

import exception.FileNotExistException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maxime BLAISE
 */
public class NewDictionary {

    /**
     * List of languages.
     */
    private ArrayList<Language> listLanguages;

    /**
     * All list of words.
     */
    private HashMap<Language, HashMap<Integer, Word>> listWords;

    /**
     * All list of links.
     */
    private HashMap<String, ArrayList<Link>> listLinks;

    /**
     * Create a Dictionary with specifics languages.
     *
     * @param listLanguages List of languages.
     */
    public NewDictionary(ArrayList<Language> listLanguages) {
        this.listLanguages = listLanguages;
        // Init HashMap
        this.listWords = new HashMap<>();
        this.listLinks = new HashMap<>();
    }

    /**
     * Initialize all data.
     */
    public void initialize() {
        // Loop for all languages
        for (Language l : this.listLanguages) {
            System.out.print("Initialize list of \"" + l.getName() + "\" words ... ");
            if (initializeSpecificListWords(l)) {
                System.out.println("OK (" + this.listWords.get(l).size() + " elements.)");
            } else {
                System.out.println("NON OK");
            }
        }
        // Loop for all combinaisons
        for (int i = 0; i < this.listLanguages.size(); i++) {
            for (int j = 0; j < this.listLanguages.size(); j++) {
                // Only if languages if differents
                if (i != j) {
                    System.out.print("Initialize list of \"" + this.listLanguages.get(i).getName() + "-" + this.listLanguages.get(j).getName() + "\" links ...");
                    if (initializeSpecificListLinks(this.listLanguages.get(i), this.listLanguages.get(j))) {
                        System.out.println("OK (" + this.listLinks.get(this.listLanguages.get(i).getIso() + this.listLanguages.get(j).getIso()).size() + " elements.)");
                    } else {
                        System.out.println("NON OK");
                    }
                }
            }
        }
    }

    /**
     * Initialize a new list of words with a specific language.
     *
     * @param language Language
     * @return True if well initialize, false else.
     */
    public boolean initializeSpecificListWords(Language language) {
        // Init pathname
        String pathname = Config.folderName + "/" + Config.wordFileName + language.getIso() + Config.extension;
        HashMap<Integer, Word> map;
        // Test if exists
        File f = new File(pathname);
        try {
            if (!f.exists()) {
                throw new FileNotExistException();
            }

            // Read
            try ( // Init Object
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathname))) {
                // Read
                map = (HashMap<Integer, Word>) ois.readObject();
                this.listWords.put(language, map);
                return true;
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(NewDictionary.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotExistException ex) {
            try {
                try ( // Try to create new file
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathname))) {
                    map = new HashMap<>();
                    this.listWords.put(language, map);
                    oos.writeObject(map);
                    return true;
                }
            } catch (IOException ex1) {
                Logger.getLogger(NewDictionary.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return false;
    }

    /**
     * Initialize a new list of links with specifics languages.
     *
     * @param language1 First language
     * @param language2 Second language
     * @return True if well initialize, false else.
     */
    public boolean initializeSpecificListLinks(Language language1, Language language2) {
        // Init pathname
        String pathname = Config.folderName + "/" + Config.linkFileName + language1.getIso() + language2.getIso() + Config.extension;
        ArrayList<Link> list;
        // Test if exists
        File f = new File(pathname);
        try {
            if (!f.exists()) {
                throw new FileNotExistException();
            }

            // Read
            try ( // Init Object
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathname))) {
                // Read
                list = (ArrayList<Link>) ois.readObject();
                this.listLinks.put(language1.getIso() + language2.getIso(), list);
                return true;
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(NewDictionary.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotExistException ex) {
            try {
                try ( // Try to create new file
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathname))) {
                    list = new ArrayList<>();
                    this.listLinks.put(language1.getIso() + language2.getIso(), list);
                    oos.writeObject(list);
                    return true;
                }
            } catch (IOException ex1) {
                Logger.getLogger(NewDictionary.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return false;
    }

    /**
     * Get a translate from a language into a list of another languages.
     *
     * @param src First language
     * @param dest Second language
     * @param idWord The searched word (id)
     * @return A list of word
     */
    public ArrayList<ArrayList<Word>> getTranslate(Language src, ArrayList<Language> dest, int idWord) {
        // Init
        ArrayList<ArrayList<Word>> res = new ArrayList<>();
        // Loop
        for (Language language : dest) {
            res.add(getTranslate(src, language, idWord));
        }
        // Result
        return res;
    }

    /**
     * Get a translate from a language into an another language.
     *
     * @param src First language
     * @param dest Second language
     * @param idWord The searched word (id)
     * @return A list of word
     */
    public ArrayList<Word> getTranslate(Language src, Language dest, int idWord) {
        System.out.println("Try to search " + idWord + " in languages " + src.getIso() + "-" + dest.getIso());
        // Init result
        ArrayList<Word> res = new ArrayList<>();
        if (idWord != -1) {
            // Get the list of links
            String languages = src.getIso() + dest.getIso();
            ArrayList<Link> list = this.listLinks.get(languages);
            for (Link link : list) {
                if (link.getId1() == idWord) {
                    // Add new word to the result list
                    res.add(this.listWords.get(dest).get(link.getId2()));
                }
            }
        }
        // Result
        return res;
    }

    /**
     * Search Id word with specific language.
     *
     * @param language Language
     * @param name Name of the word
     * @return Id of the word
     */
    public Word searchWord(Language language, String name) {
        // Get list of words
        if (listWords.containsKey(language)) {
            HashMap<Integer, Word> map = listWords.get(language);
            // Loop to search the word
            Set set = map.keySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                int key = (Integer) it.next();
                // If the good word
                if (map.get(key).getName().equalsIgnoreCase(name)) {
                    map.get(key);
                }
            }
        }
        return null;
    }

    /**
     * Save the data after insert a new link (and words)
     *
     * @param l1 First language
     * @param l2 Second language
     */
    public void save(Language l1, Language l2) {
        // Save words
        saveWords(l1);
        saveWords(l1);
        // Save links
        saveLinks(l1, l2);
        saveLinks(l2, l1);
    }
    
    public void saveLinks(Language l1, Language l2) {
         // Init pathname
        String pathname = Config.folderName + "/" + Config.linkFileName + l1.getIso() + l2.getIso() + Config.extension;
        // Get instance
        String str = l1.getIso() + l2.getIso();
        ArrayList<Link> list = this.listLinks.get(str);
        try {
            try ( // Init stream
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathname))) {
                // Write
                oos.writeObject(list);
            }
        } catch (IOException ex) {
            Logger.getLogger(NewDictionary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Save the words of a specific language.
     *
     * @param l1 Language
     */
    public void saveWords(Language l1) {
        // Init pathname
        String pathname = Config.folderName + "/" + Config.wordFileName + l1.getIso() + Config.extension;
        // Get instance
        HashMap<Integer, Word> map = this.listWords.get(l1);
        try {
            try ( // Init stream
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathname))) {
                // Write
                oos.writeObject(map);
            }
        } catch (IOException ex) {
            Logger.getLogger(NewDictionary.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /* GETTERS AND SETTERS */
    public ArrayList<Language> getListLanguages() {
        return listLanguages;
    }

    public void setListLanguages(ArrayList<Language> listLanguages) {
        this.listLanguages = listLanguages;
    }

    public HashMap<Language, HashMap<Integer, Word>> getListWords() {
        return listWords;
    }

    public void setListWords(HashMap<Language, HashMap<Integer, Word>> listWords) {
        this.listWords = listWords;
    }

    public HashMap<String, ArrayList<Link>> getListLinks() {
        return listLinks;
    }

    public void setListLinks(HashMap<String, ArrayList<Link>> listLinks) {
        this.listLinks = listLinks;
    }

}
