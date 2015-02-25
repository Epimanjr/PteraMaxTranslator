/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic.swing;

import exception.FileNotExistException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Config;
import model.Language;
import model.Word;

/**
 *
 * @author Maxime BLAISE
 */
public class ReadTreatment {

    /**
     * Save a list of words.
     *
     * @param dest Language
     * @param map The list
     * @return boolean
     */
    public static boolean saveListWords(Language dest, HashMap<Integer, Word> map) {
        // Init filename
        String filename = Config.folderName + "/" + Config.wordFileName + dest.getIso() + ".pmt";
        ObjectOutputStream oos;
        try {
            // Init
            oos = new ObjectOutputStream(new FileOutputStream(filename));
            // Write
            oos.writeObject(map);
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
     * Read a list of words.
     *
     * @param src Language
     * @return The list
     */
    public static HashMap<Integer, Word> readListWords(Language src) {
        // Init res
        HashMap<Integer, Word> res;
        // Init filename
        String filename = Config.folderName + "/" + Config.wordFileName + src.getIso() + ".pmt";
        ObjectInputStream ois;
        try {
            // Init
            File fis = new File(filename);
            if (!fis.exists()) {
                throw new FileNotExistException();
            }
            ois = new ObjectInputStream(new FileInputStream(filename));
            // Read
            res = (HashMap<Integer, Word>) ois.readObject();
            // Close
            ois.close();
            return res;
        } catch (IOException ex) {
            System.out.print("-IOException-");
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReadTreatment.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (FileNotExistException ex) {
            res = new HashMap<>();
            saveListWords(src, res);
            return res;
        }
    }
}
