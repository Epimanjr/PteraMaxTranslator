package model;

import exception.FileNotExistException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maxime BLAISE
 */
public class List {

    /**
     * Data structure.
     */
    private HashMap<String, HashMap<Word, Word>> list;

    /**
     * The current dictionary.
     */
    private final Dictionary czech;

    /**
     * Constructor.
     *
     * @param czech Current dictionary.
     */
    public List(Dictionary czech) {
        this.czech = czech;
        list = new HashMap<>();
    }

    /**
     * Initialize the list.
     * @return boolean
     */
    public boolean initialize() {
        try {
            System.out.print("Read the list in the file ... ");
            if(read()) {
                System.out.println("OK (" + this.list.size() + " elements.)");
                return true;
            }
        } catch (FileNotExistException ex) {
            System.out.println("NON OK");
            System.out.print("Try to generate a new list file ... ");
            if(save()) {
                System.out.println("OK");
                return true;
            }
        }
        return false;
    }
    
    /**
     * Read the list in the file.
     *
     * @return boolean
     * @throws FileNotExistException if file not exists
     */
    public boolean read() throws FileNotExistException {
        // Init pathname
        String pathname = Config.folderName + "/" + Config.listFileName + czech.getLanguage1().getIso() + czech.getLanguage2().getIso() + ".pmt";
        // Check exist
        File f = new File(pathname);
        if (!f.exists()) {
            throw new FileNotExistException();
        }
        ObjectInputStream ois;
        try {
            // Init
            ois = new ObjectInputStream(new FileInputStream(pathname));
            // Read
            this.list = (HashMap<String, HashMap<Word, Word>>) ois.readObject();
            // Close
            ois.close();
            return true;
        } catch (IOException ex) {
            return false;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(List.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Save the list in a file.
     *
     * @return boolean
     */
    public boolean save() {
        // Init pathname
        String pathname = Config.folderName + "/" + Config.listFileName + czech.getLanguage1().getIso() + czech.getLanguage2().getIso() + ".pmt";
        ObjectOutputStream oos;
        try {
            // Init
            oos = new ObjectOutputStream(new FileOutputStream(pathname));
            // Write
            oos.writeObject(this.list);
            // Close
            oos.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /* GETTERS AND SETTERS */
    public HashMap<String, HashMap<Word, Word>> getList() {
        return list;
    }

    public Dictionary getCzech() {
        return czech;
    }
    
    public static void main(String[] args) {
        Dictionary czech = new Dictionary(Language.Czech, Language.French);
        czech.initialize();
        List l = new List(czech);
        l.initialize();
    }

}
