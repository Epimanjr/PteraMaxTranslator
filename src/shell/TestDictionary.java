/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shell;

import java.util.ArrayList;
import model.Language;
import model.NewDictionary;

/**
 *
 * @author Maxime
 */
public class TestDictionary {
 
    public static void main(String[] args) {
        // Init list of languages
        ArrayList<Language> list = new ArrayList<>();
        list.add(Language.French);
        list.add(Language.Czech);
        // Create dictionary
        NewDictionary czech = new NewDictionary(list);
        czech.initialize();
        System.out.println("Search id word for ahoj : " + czech.searchWordId(Language.Czech, "ahoj"));
    }
}
