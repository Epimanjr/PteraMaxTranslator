package shell;

import exception.LanguageException;
import java.util.ArrayList;
import java.util.Scanner;
import model.Language;
import model.Dictionary;
import model.Word;

public class Shell {

    /**
     * The list of languages.
     */
    private final ArrayList<Language> listLanguages;

    /**
     * For read line.
     */
    private final Scanner sc = new Scanner(System.in);

    Dictionary czech;

    /**
     * Create a shell with specifics languages.
     *
     * @param listLanguages List of languages
     */
    public Shell(ArrayList<Language> listLanguages) {
        this.listLanguages = listLanguages;
    }

    /**
     * Run the shell
     */
    public void run() {
        // Init dictionary
        czech = new Dictionary(listLanguages);
        czech.initialize();
        // Begin loop
        String prompt = "PteraMaxTranslator:$ ";
        String line = askForLine(prompt);
        while (!line.equals("exit")) {
            // Treatment

            // Ask new line
            line = askForLine(prompt);
        }
        System.out.println("Good bye");
    }
    
    

    private Word askForWord(Language lastLanguage) throws LanguageException {
        // Step 1 : Language
        Language language = askForLanguage();
        if(language == null || language.equals(lastLanguage)) {
            throw new LanguageException();
        }
        // Step 2 : Name of the word
        String name = askForLine("Name of the word : ");
        Word searchedWord = czech.searchWord(language, name);
        if(searchedWord != null) {
            System.out.println("Word " + name + " found !");
            return searchedWord;
        }
        System.out.println("Word " + name + " not found.");
        // Step 3 : Gender/Phonetic of the word
        String gender = askForLine("\tGender of the word : ");
        String phonetic = askForLine("\tPhonetic of the word : ");
        // Last step : Creation of the word
        Word word = new Word(language, name, gender, phonetic);
        int id = czech.getListWords(language).get(-1).getId() + 1;
        word.setId(id);
        czech.getListWords(language).put(-1, word);
        return word;
    }

    /**
     * Ask user for a language.
     *
     * @return A instance of Language
     */
    private Language askForLanguage() {
        // Build a message
        String message = "Which language ? \n";
        for (int i = 0; i < listLanguages.size(); i++) {
            message += "\t" + i + "\\ " + listLanguages.get(i).getName() + "\n";
        }
        // Ask for number and get the correct Language
        return listLanguages.get(new Integer(askForLine(message)));
    }

    /**
     * Ask user to enter something.
     *
     * @return String
     */
    private String askForLine(String message) {
        System.out.print(message);
        return sc.nextLine();
    }

    public static void main(String[] args) {
        // Init list of languages
        System.out.print("Initialize list of languages ... ");
        ArrayList<Language> listLanguages = new ArrayList<>();
        listLanguages.add(new Language("čestiná", "cs"));
        listLanguages.add(new Language("français", "fr"));
        System.out.println("OK");
        // Create shell
        Shell shell = new Shell(listLanguages);
        shell.run();
    }
}
