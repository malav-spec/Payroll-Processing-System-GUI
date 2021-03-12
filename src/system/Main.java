package system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Malav Doshi and Herik Patel
 */
public class Main extends Application {

    /**
     * Method to start the GUI window
     * @param primaryStage Stage object which is the main window for GUI
     * @throws Exception Not used
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("system.fxml"));
        primaryStage.setTitle("Payroll Processing System");
        primaryStage.setScene(new Scene(root, 750, 650));
        primaryStage.show();
    }


    /**
     * Launch the main window
     * @param args Not used
     */
    public static void main(String[] args) {
        launch(args);
    }
}