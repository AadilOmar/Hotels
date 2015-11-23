package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.beans.EventHandler;
import java.util.HashMap;

/**
 * Created by aadil on 11/22/15.
 */
public class ScreensController extends StackPane {
    private HashMap<String, Node> screens = new HashMap<>();


    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    public boolean loadScreen(String name, String resource) {
        try {
            FXMLLoader myLoader = new
                    FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();
            ControlledScreen myScreenControler =
                    ((ControlledScreen) myLoader.getController());
            myScreenControler.setScreenParent(this);
            addScreen(name, loadScreen);
            return true;
        }catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean setScreen(final String name) {

        if (screens.get(name) != null) { //screen loaded
            final DoubleProperty opacity = opacityProperty();

            //Is there is more than one screen

            //no one else been displayed, then just show
            setOpacity(0.0);
            getChildren().add(screens.get(name));
            Timeline fadeIn = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(opacity, 0.0)),
                    new KeyFrame(new Duration(2500),
                            new KeyValue(opacity, 1.0)));
            fadeIn.play();

            return true;
        } else {
            System.out.println("screen hasn't been loaded!\n");
            return false;
        }

    }
}
