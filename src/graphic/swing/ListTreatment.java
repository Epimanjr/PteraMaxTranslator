package graphic.swing;

import java.util.Iterator;
import java.util.Set;
import javax.swing.JList;
import model.List;

/**
 *
 * @author Maxime BLAISE
 */
public class ListTreatment {

    /**
     * Print all list names in JList.
     *
     * @param list List instance
     * @param jlist JList (Swing object).
     */
    public static void printAllName(List list, JList<String> jlist) {
        // Get Array with names
        String[] array = new String[list.getList().size()];
        Set cles = list.getList().keySet();
        Iterator it = cles.iterator();
        int i = 0;
        while (it.hasNext()) {
            array[i] = (String) it.next();
            i++;
        }
        // Set Model
        jlist.setModel(new javax.swing.AbstractListModel() {
            String[] strings = array;

            @Override
            public int getSize() {
                return strings.length;
            }

            @Override
            public Object getElementAt(int i) {
                return strings[i];
            }
        });
    }
}
