package shell;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import model.Dictionary;
import model.Language;
import model.Link;
import model.Word;

/**
 *
 * @author Maxime BLAISE
 */
public class Main {

    public static Dictionary czech = new Dictionary(Language.Czech, Language.French);

    public static void main(String[] args) {
        czech.initialize();

        // Shell
        Scanner sc = new Scanner(System.in);
        System.out.print("PteraMaxTranslator:$ ");
        String line = sc.nextLine();
        while (!line.equals("exit")) {
            switch (line) {
                case "get":
                    get();
                    break;
                case "add":
                    add();
                    break;
            }
            System.out.print("PteraMaxTranslator:$ ");
            line = sc.nextLine();
        }
    }

    /**
     * Get a word.
     */
    public static void get() {
        System.out.println("*** GET A WORD *** ");
        // Ask the word
        Scanner sc = new Scanner(System.in);
        System.out.print("Word: ");
        String name = sc.nextLine();
        get(name);
    }

    /**
     * Get a word with a specific name.
     *
     * @param name Name of the word
     */
    private static void get(String name) {
        int id1 = searchIdWord(czech.getLanguage1(), name);
        if (id1 != -1) {
            System.out.println("Find \"" + czech.getLanguage1().getName() + "\" : " + printWord(czech.getListWords1().get(id1)));
            for (Link l : czech.getListLinks()) {
                if (l.getId1() == id1) {
                    System.out.println(czech.getLanguage2().getName() + " -> " + printWord(czech.getListWords2().get(l.getId2())));
                }
            }
        }

        int id2 = searchIdWord(czech.getLanguage2(), name);
        if (id2 != -1) {
            System.out.println("Find \"" + czech.getLanguage2().getName() + "\" : " + printWord(czech.getListWords2().get(id2)));
            for (Link l : czech.getListLinks()) {
                if (l.getId2() == id2) {
                    System.out.println(czech.getLanguage1().getName() + " -> " + printWord(czech.getListWords1().get(l.getId1())));
                }
            }
        }
    }

    /**
     * Add a new link.
     */
    public static void add() {
        System.out.println("*** ADD NEW " + czech.getLanguage1().getName() + "/" + czech.getLanguage2().getName() + " WORD ***");
        // Ask first word
        Scanner sc = new Scanner(System.in);
        System.out.print(czech.getLanguage1().getName() + " word: ");
        String name1 = sc.nextLine();
        int id1 = searchIdWord(czech.getLanguage1(), name1);
        if (id1 == (-1)) {
            // New word
            System.out.println("Oh no, word does not exist... ");
            id1 = addNewWord(czech.getLanguage1(), name1);
            System.out.println("Id of new word : " + id1);
        } else {
            System.out.println("Ok, word exists (ID " + id1 + ")");
        }
        // Ask second word
        System.out.print(czech.getLanguage2().getName() + " word: ");
        String name2 = sc.nextLine();
        int id2 = searchIdWord(czech.getLanguage2(), name2);
        if (id2 == (-1)) {
            // New word
            System.out.println("Oh no, word does not exist... ");
            id2 = addNewWord(czech.getLanguage2(), name2);
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
        }
        // Save
        czech.saveAll();
    }

    /**
     * Search id of the word, to check if it's exists.
     *
     * @param name Name of the word.
     * @return id (-1 if not exist)
     */
    private static int searchIdWord(Language language, String name) {
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

    /**
     * Add a new word when don't exist.
     */
    private static int addNewWord(Language language, String name) {
        // Init Scanner
        Scanner sc = new Scanner(System.in);
        // Create word
        Word newWord = new Word(name);
        newWord.setLanguage(language);
        // Ask gender
        System.out.print("Gender: ");
        String gender = sc.nextLine();
        newWord.setGender(gender);
        // Ask phonetic
        System.out.print("Phonetic: ");
        String phonetic = sc.nextLine();
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

    private static String printWord(Word word) {
        return (word.getName() + " " + word.getGender() + " (" + word.getPhonetic() + ")");
    }
}
