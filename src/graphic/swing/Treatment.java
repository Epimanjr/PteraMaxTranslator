/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic.swing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import model.Dictionary;
import model.Language;
import model.Link;
import model.Word;

/**
 *
 * @author Maxime BLAISE
 */
public class Treatment {

    /**
     * Current dictionary.
     */
    private final Dictionary czech;

    /**
     * Create a new instance in order to do some process.
     *
     * @param czech
     */
    public Treatment(Dictionary czech) {
        this.czech = czech;
    }

    /**
     * Get a word with a specific name.
     *
     * @param name Name of the word
     * @return Array
     */
    public ArrayList<Word> get(String name) {
        ArrayList<Word> res = new ArrayList<>();
        boolean add = false;
        int id1 = searchIdWord(czech.getLanguage1(), name);
        if (id1 != -1) {
            System.out.println("Find \"" + czech.getLanguage1().getName() + "\" : " + printWord(czech.getListWords1().get(id1)));
            for (Link l : czech.getListLinks()) {
                if (l.getId1() == id1) {
                    System.out.println(czech.getLanguage2().getName() + " -> " + printWord(czech.getListWords2().get(l.getId2())));
                    res.add(czech.getListWords2().get(l.getId2()));
                    add = true;
                }
            }
        }

        if (add) {
            return res;
        }

        int id2 = searchIdWord(czech.getLanguage2(), name);
        if (id2 != -1) {
            System.out.println("Find \"" + czech.getLanguage2().getName() + "\" : " + printWord(czech.getListWords2().get(id2)));
            for (Link l : czech.getListLinks()) {
                if (l.getId2() == id2) {
                    System.out.println(czech.getLanguage1().getName() + " -> " + printWord(czech.getListWords1().get(l.getId1())));
                    res.add(czech.getListWords1().get(l.getId1()));
                }
            }
        }

        return res;
    }

    /**
     * Search id of the word, to check if it's exists.
     *
     * @param language The language
     * @param name Name of the word.
     * @return id (-1 if not exist)
     */
    public int searchIdWord(Language language, String name) {
        HashMap<Integer, Word> map;
        // Loop for HashMap
        if (czech.getLanguage1().equals(language)) {
            map = czech.getListWords1();
        } else if (czech.getLanguage2().equals(language)) {
            map = czech.getListWords2();
        } else {
            System.out.println("Error in language !");
            return -1;
        }
        Set cles = map.keySet();
        Iterator it = cles.iterator();
        while (it.hasNext()) {
            Integer key = (Integer) it.next();
            Word value = map.get(key);
            if (value.getName().equalsIgnoreCase(name)) {
                return key;
            }
        }
        return -1;
    }

    private static String printWord(Word word) {
        return (word.getName() + " " + word.getGender() + " (" + word.getPhonetic() + ")");
    }

    /**
     * Add a new link.
     *
     * @param name1 Name of first word.
     * @param name2 Name of second word.
     * @param gender1 Gender of first word.
     * @param gender2 Gender of second word.
     * @param phonetic1 Phonetic of first word.
     * @param phonetic2 Phonetic of second word.
     * @return true or false
     */
    public boolean add(String name1, String name2, String gender1, String gender2, String phonetic1, String phonetic2) {
        if (name1.equals("") || name2.equals("") || gender1.equals("") || gender2.equals("") || phonetic1.equals("") || phonetic2.equals("")) {
            return false;
        }
        System.out.println("*** ADD NEW " + czech.getLanguage1().getName() + "/" + czech.getLanguage2().getName() + " WORD ***");
        int id1 = searchIdWord(czech.getLanguage1(), name1);
        if (id1 == (-1)) {
            // New word
            System.out.println("Oh no, word does not exist... ");
            id1 = addNewWord(czech.getLanguage1(), name1, gender1, phonetic1);
            System.out.println("Id of new word : " + id1);
        } else {
            System.out.println("Ok, word exists (ID " + id1 + ")");
        }

        int id2 = searchIdWord(czech.getLanguage2(), name2);
        if (id2 == (-1)) {
            // New word
            System.out.println("Oh no, word does not exist... ");
            id2 = addNewWord(czech.getLanguage2(), name2, gender2, phonetic2);
            System.out.println("Id of new word : " + id2);
        } else {
            System.out.println("Ok, word exists (ID " + id2 + ")");
        }
        // New link
        Link link = new Link(id1, id2);
        if (czech.getListLinks().contains(link)) {
            System.out.println("The dictionary already contains these words... ");
        } else {
            czech.getListLinks().add(link);
            // Save
            czech.saveAll();
            return true;
        }
        return false;
    }

    /**
     * Add a new word when don't exist.
     */
    private int addNewWord(Language language, String name, String gender, String phonetic) {
        // Create word
        Word newWord = new Word(name);
        newWord.setLanguage(language);
        newWord.setGender(gender);
        newWord.setPhonetic(phonetic);
        // Add this word
        if (czech.getLanguage1().equals(language)) {
            newWord.setId(czech.getListWords1().size());
            czech.getListWords1().put(newWord.getId(), newWord);
        } else if (czech.getLanguage2().equals(language)) {
            newWord.setId(czech.getListWords2().size());
            czech.getListWords2().put(newWord.getId(), newWord);
        } else {
            System.out.println("Error in language !");
        }
        return newWord.getId();
    }

    public String htmlForSingleLanguage(ArrayList<Word> list) {
        // Init
        String res = "<html>", colorName = "#DD0000", colorPhonetic = "#0000DD";
        // Loop
        for (Word w : list) {
            res += "<p style=\"font-size: 20px;\"><span color=\"" + colorName + "\">" + w.getName() + "</span>   <span color=\"" + colorPhonetic + "\">" + w.getPhonetic() + "</span</p>";
        }
        // Return
        return res + "</html>";
    }
    
    
}
