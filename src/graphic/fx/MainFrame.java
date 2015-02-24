
package graphic.fx;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Maxime BLAISE
 */
public class MainFrame extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new MainGroup(), Config.width, Config.height);
        stage.setScene(scene);
        stage.show();
    }
    
    class MainGroup extends Group {

        // Begin declare
        
        // End declare
        
        public MainGroup() {
            
        }
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
