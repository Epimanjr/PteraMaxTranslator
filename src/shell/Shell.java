package shell;

import java.util.ArrayList;
import java.util.Scanner;
import model.Language;
import model.NewDictionary;

public class Shell {

    /**
     * The list of languages.
     */
    private final ArrayList<Language> listLanguages;

    /**
     * For read line.
     */
    private final Scanner sc = new Scanner(System.in);

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
        NewDictionary czech = new NewDictionary(listLanguages);
        czech.initialize();
        // Begin loop
        String line = askForLine();
        while(!line.equals("exit")) {
            // Treatment
            
            // Ask new line
            line = askForLine();
        }
        System.out.println("Good bye");
    }

    /**
     * Ask user to enter something.
     *
     * @return String
     */
    private String askForLine() {
        System.out.print("PteraMaxTranslator:$ ");
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
