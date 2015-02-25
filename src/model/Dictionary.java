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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maxime BLAISE
 */
public class Dictionary {

    /**
     * First language.
     */
    private Language language1;

    /**
     * Second language.
     */
    private Language language2;

    /**
     * List of the word for the first language.
     */
    private HashMap<Integer, Word> listWords1;

    /**
     * List of the word for the second language.
     */
    private HashMap<Integer, Word> listWords2;

    /**
     * List of links (two words).
     */
    private ArrayList<Link> listLinks;

    /**
     * Create dictionary with specific languages.
     *
     * @param language1 First language.
     * @param language2 Second language.
     */
    public Dictionary(Language language1, Language language2) {
        this.language1 = language1;
        this.language2 = language2;
        this.listWords1 = new HashMap<>();
        this.listWords2 = new HashMap<>();
        this.listLinks = new ArrayList<>();
    }

    /**
     * Initialize the dictionary.
     *
     * @return true if well initialize.
     */
    public boolean initialize() {
        // Init the first list of words
        try {
            System.out.print("Initialize 1st list of words ... ");
            if (!initListWord1()) {
                System.out.println("NON OK");
                return false;
            }
            System.out.println("OK (" + this.listWords1.size() + " elements.)");
        } catch (FileNotExistException ex) {
            System.out.println("NON OK");
            System.out.print("Try to create a new file for 1st list of words ... ");
            if (!saveListWord1()) {
                System.out.println("NON OK");
                return false;
            }
            System.out.println("OK");
        }
        // Init the second list of words
        try {
            System.out.print("Initialize 2nd list of words ... ");
            if (!initListWord2()) {
                System.out.println("NON OK");
                return false;
            }
            System.out.println("OK (" + this.listWords2.size() + " elements.)");
        } catch (FileNotExistException ex2) {
            System.out.println("NON OK");
            System.out.print("Try to create a new file for 2nd list of words ... ");
            if (!saveListWord2()) {
                System.out.println("NON OK");
                return false;
            }
            System.out.println("OK");
        }
        // Init the list of links
        try {
            System.out.print("Initialize list of links ... ");
            if (!initListLinks()) {
                System.out.println("NON OK");
                return false;
            }
            System.out.println("OK (" + this.listLinks.size() + " elements.)");
        } catch (FileNotExistException ex2) {
            System.out.println("NON OK");
            System.out.print("Try to create a new file for list of links ... ");
            if (!saveListLinks()) {
                System.out.println("NON OK");
                return false;
            }
            System.out.println("OK");
        }
        // END
        return true;
    }

    /**
     * Save all data in file.
     */
    public void saveAll() {
        System.out.print("Save the 1st list of words ... (" + this.listWords1.size() + " elements.)");
        System.out.println(this.saveListWord1());
        System.out.print("Save the 2st list of words ... (" + this.listWords2.size() + " elements.)");
        System.out.println(this.saveListWord2());
        System.out.print("Save the list of links ... (" + this.listLinks.size() + " elements.)");
        System.out.println(this.saveListLinks());
    }
    
    /**
     * Initialize the first list of words.
     *
     * @return boolean
     */
    private boolean initListWord1() throws FileNotExistException {
        // Init filename
        String filename = Config.folderName + "/" + Config.wordFileName + this.language1.getIso() + ".pmt";
        ObjectInputStream ois;
        try {
            // Init
            File fis = new File(filename);
            if (!fis.exists()) {
                throw new FileNotExistException();
            }
            ois = new ObjectInputStream(new FileInputStream(filename));
            // Read
            this.listWords1 = (HashMap<Integer, Word>) ois.readObject();
            // Close
            ois.close();
            return true;
        } catch (IOException ex) {
            System.out.print("-IOException-");
            return false;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Save the first list of words.
     *
     * @return boolean
     */
    private boolean saveListWord1() {
        // Init filename
        String filename = Config.folderName + "/" + Config.wordFileName + this.language1.getIso() + ".pmt";
        ObjectOutputStream oos;
        try {
            // Init
            oos = new ObjectOutputStream(new FileOutputStream(filename));
            // Write
            oos.writeObject(this.listWords1);
            // Close
            oos.close();
            return true;
        } catch (FileNotFoundException ex) {
            System.out.print("-FileNotFound-");
            return false;
        } catch (IOException ex) {
            System.out.print("-IOException");
            return false;
        }
    }

    /**
     * Initialize the second list of words.
     *
     * @return boolean
     */
    private boolean initListWord2() throws FileNotExistException {
        // Init filename
        String filename = Config.folderName + "/" + Config.wordFileName + this.language2.getIso() + ".pmt";
        ObjectInputStream ois;
        try {
            // Init
            File fis = new File(filename);
            if (!fis.exists()) {
                throw new FileNotExistException();
            }
            ois = new ObjectInputStream(new FileInputStream(filename));
            // Read
            this.listWords2 = (HashMap<Integer, Word>) ois.readObject();
            // Close
            ois.close();
            return true;
        } catch (IOException ex) {
            System.out.print("-IOException-");
            return false;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Save the second list of words.
     *
     * @return boolean
     */
    private boolean saveListWord2() {
        // Init filename
        String filename = Config.folderName + "/" + Config.wordFileName + this.language2.getIso() + ".pmt";
        ObjectOutputStream oos;
        try {
            // Init
            oos = new ObjectOutputStream(new FileOutputStream(filename));
            // Write
            oos.writeObject(this.listWords2);
            // Close
            oos.close();
            return true;
        } catch (IOException ex) {
            System.out.print("-IOException");
            return false;
        }
    }

    /**
     * Initialize the list of links.
     *
     * @return boolean
     */
    private boolean initListLinks() throws FileNotExistException {
        // Init filename
        String filename = Config.folderName + "/" + Config.linkFileName + this.language1.getIso() + this.language2.getIso() + ".pmt";
        ObjectInputStream ois;
        try {
            // Init
            File fis = new File(filename);
            if (!fis.exists()) {
                throw new FileNotExistException();
            }
            ois = new ObjectInputStream(new FileInputStream(filename));
            // Read
            this.listLinks = (ArrayList<Link>) ois.readObject();
            // Close
            ois.close();
            return true;
        } catch (IOException ex) {
            System.out.print("-IOException-");
            return false;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Save the list of links.
     *
     * @return boolean
     */
    private boolean saveListLinks() {
        // Init filename
        String filename = Config.folderName + "/" + Config.linkFileName + this.language1.getIso() + this.language2.getIso() + ".pmt";
        ObjectOutputStream oos;
        try {
            // Init
            oos = new ObjectOutputStream(new FileOutputStream(filename));
            // Write
            oos.writeObject(this.listLinks);
            // Close
            oos.close();
            return true;
        } catch (IOException ex) {
            System.out.print("-IOException");
            return false;
        }
    }

    /* GETTERS AND SETTERS */
    public Language getLanguage1() {
        return language1;
    }

    public void setLanguage1(Language language1) {
        this.language1 = language1;
    }

    public Language getLanguage2() {
        return language2;
    }

    public void setLanguage2(Language language2) {
        this.language2 = language2;
    }

    public HashMap<Integer, Word> getListWords1() {
        return listWords1;
    }

    public void setListWords1(HashMap<Integer, Word> listWords1) {
        this.listWords1 = listWords1;
    }

    public HashMap<Integer, Word> getListWords2() {
        return listWords2;
    }

    public void setListWords2(HashMap<Integer, Word> listWords2) {
        this.listWords2 = listWords2;
    }

    public ArrayList<Link> getListLinks() {
        return listLinks;
    }

    public void setListLinks(ArrayList<Link> listLinks) {
        this.listLinks = listLinks;
    }

    

}
