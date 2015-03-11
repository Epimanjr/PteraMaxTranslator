package model;

import exception.FileNotExistException;
import java.io.File;
import java.io.FileInputStream;
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
public class Dictionary {

    /**
     * List of languages.
     */
    private final ArrayList<Language> listLanguages;

    /**
     * All list of words.
     */
    private final HashMap<Language, HashMap<Integer, Word>> listWords;

    /**
     * All list of links.
     */
    private final HashMap<String, ArrayList<Link>> listLinks;

    /**
     * Create a Dictionary with specifics languages.
     *
     * @param listLanguages List of languages.
     */
    public Dictionary(ArrayList<Language> listLanguages) {
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
        this.listLanguages.stream().map((l) -> {
            System.out.print("Initialize list of \"" + l.getName() + "\" words ... ");
            return l;
        }).forEach((l) -> {
            if (initializeSpecificListWords(l)) {
                System.out.println("OK (" + this.listWords.get(l).size() + " elements.)");
            } else {
                System.out.println("NON OK");
            }
        });
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
    private boolean initializeSpecificListWords(Language language) {
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
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotExistException ex) {
            try {
                try ( // Try to create new file
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathname))) {
                    map = new HashMap<>();
                    // Init map
                    map.put(-1,  new Word(language, -1, "", "", ""));
                    this.listWords.put(language, map);
                    oos.writeObject(map);
                    return true;
                }
            } catch (IOException ex1) {
                Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex1);
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
    private boolean initializeSpecificListLinks(Language language1, Language language2) {
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
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex1);
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
        dest.stream().forEach((language) -> {
            res.add(getTranslate(src, language, idWord));
        });
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
        // Init result
        ArrayList<Word> res = new ArrayList<>();
        if (idWord != -1) {
            // Get the list of links
            String languages = src.getIso() + dest.getIso();
            ArrayList<Link> list = this.listLinks.get(languages);
            list.stream().filter((link) -> (link.getId1() == idWord)).forEach((link) -> {
                // Add new word to the result list
                res.add(this.listWords.get(dest).get(link.getId2()));
            });
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
                    return map.get(key);
                }
            }
        }
        return null;
    }

    /**
     * Add and save !
     *
     * @param l1 First Language
     * @param l2 Second Language
     * @param name1 Name of the word in first language
     * @param name2 Name of the word in second language
     * @param gender1 Gender of the word in first language
     * @param gender2 Gender of the word in second language
     * @param phonetic1 Phonetic of the word in first language
     * @param phonetic2 Phonetic of the word in second language
     */
    public void addAndSave(Language l1, Language l2, String name1, String name2, String gender1, String gender2, String phonetic1, String phonetic2) {
        // Add new words
        int i1 = addNewWord(l1, name1, gender1, phonetic1);
        int i2 = addNewWord(l2, name2, gender2, phonetic2);
        // Add Links
        addNewLink(l1, l2, i1, i2);
        // Save
        save(l1, l2);
    }

    /**
     * Add new link in specifics languages
     *
     * @param l1 Language 1
     * @param l2 Language 2
     * @param i1 First Integer
     * @param i2 Second Integer
     */
    private void addNewLink(Language l1, Language l2, int i1, int i2) {
        // First Link
        String str = l1.getIso() + l2.getIso();
        Link link1 = new Link(i1, i2);
        if (!this.listLinks.get(str).contains(link1)) {
            this.listLinks.get(str).add(link1);
        }
        // Second Link
        str = l2.getIso() + l1.getIso();
        Link link2 = new Link(i2, i1);
        if (!this.listLinks.get(str).contains(link2)) {
            this.listLinks.get(str).add(link2);
        }
    }

    /**
     * Add a new word in a specific language.
     *
     * @param language Language
     * @param name Name of the word
     * @param gender Gender of the word
     * @param phonetic Phonetic of the word
     * @return New id of the word
     */
    private int addNewWord(Language language, String name, String gender, String phonetic) {
        // Create the word
        Word word = new Word(name);
        word.setGender(gender);
        word.setPhonetic(phonetic);
        // Check if exists
        Word tmp = searchWord(language, name);
        if (tmp != null) {
            // Exists
            if (tmp.getId() != (-1)) {
                return tmp.getId();
            }
        }
        // Not exists
        int id = this.listWords.get(language).size();
        word.setId(id);
        this.listWords.get(language).put(id, word);
        return id;
    }

    /**
     * Save the data after insert a new link (and words)
     *
     * @param l1 First language
     * @param l2 Second language
     */
    private void save(Language l1, Language l2) {
        // Save words
        saveWords(l1);
        saveWords(l2);
        // Save links
        saveLinks(l1, l2);
        saveLinks(l2, l1);
    }

    /**
     * Save the links of a specific language.
     *
     * @param l1 First Language
     * @param l2 Second Language
     */
    private void saveLinks(Language l1, Language l2) {
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
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Save the words of a specific language.
     *
     * @param l1 Language
     */
    private void saveWords(Language l1) {
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
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Print in console all data.
     */
    public void printData() {
        System.out.println("*** PRINT DATA *** ");
        // Print word
        Set set = listWords.keySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Language l = (Language) it.next();
            System.out.println("Print \"" + l.getName() + "\" words : ");
            HashMap<Integer, Word> map = listWords.get(l);
            Set setMap = map.keySet();
            Iterator itMap = setMap.iterator();
            while (itMap.hasNext()) {
                int i = (int) itMap.next();
                Word w = map.get(i);
                System.out.println(w.getId() + ": " + w.getName() + " -" + w.getGender() + "- " + w.getPhonetic());
            }
        }
        // Print links
        Set setLink = listLinks.keySet();
        Iterator itLink = setLink.iterator();
        while (itLink.hasNext()) {
            String str = (String) itLink.next();
            System.out.println("Print " + str + " links : ");
            ArrayList<Link> list = listLinks.get(str);
            list.stream().forEach((l) -> {
                System.out.println(l.getId1() + " - " + l.getId2());
            });
        }
        System.out.println("*** END PRINT ***");
    }

    /* GETTERS AND SETTERS */
    /**
     * Get the list of words in a specific language.
     *
     * @param language Language
     * @return The list of words
     */
    public HashMap<Integer, Word> getListWords(Language language) {
        return this.listWords.get(language);
    }

}
