package screensframework.reviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import screensframework.ControlledScreen;
import screensframework.Main;
import screensframework.ScreensController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by aadil on 11/27/15.
 */
public class ReviewController implements Initializable, ControlledScreen {

    ScreensController myController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    @FXML
    public void checkReviews(ActionEvent event){

    }

    @FXML
    public void submitReview(ActionEvent event){

    }

    @FXML
    private void back (ActionEvent event){
        myController.setScreen(Main.CUSTOMER_HOME_SCREEN);
    }

}
