package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {


    public static final String LOGIN_SCREEN = "login";
    public static final String LOGIN_SCREEN_FXML = "sample.fxml";
    public static final String NEW_USER_SCREEN = "new_user";
    public static final String NEW_USER_SCREEN_FXML = "new_user.fxml";


    @Override
    public void start(Stage stage) {
        System.out.println("1111");

        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(Main.LOGIN_SCREEN,
                Main.LOGIN_SCREEN_FXML);
        mainContainer.loadScreen(Main.NEW_USER_SCREEN,
                Main.NEW_USER_SCREEN_FXML);
        System.out.println("2222");

        Group root = new Group();
        System.out.println("3333");

        root.getChildren().addAll(mainContainer);
        System.out.println("4444");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        System.out.println("555");
        stage.show();
        System.out.println("666");

//        Parent root = null;
//        try {
//            root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        }catch(Exception e){
//            System.out.println("ASDFASDF");
//        }
//
//        Scene scene = new Scene(root, 300, 275);
//        stage.setTitle("FXML Welcome");
//        stage.setScene(scene);
//        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}