package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by aadil on 11/22/15.
 */
public class ScreenController implements Initializable, ControlledScreen {

    ScreensController myController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void goToMain(ActionEvent event){
        myController.setScreen("login_screen");
    }

    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }


    //any required method here
}