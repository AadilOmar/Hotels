package screensframework.reviews;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import screensframework.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadFactory;

/**
 * Created by aadil on 11/27/15.
 */
public class ReviewController implements Initializable, ControlledScreen {

    Scene current;
    ObservableList<ReviewItem> all_reviews = FXCollections.observableArrayList(
            new ReviewItem("Good", "it was alright of a place"),
            new ReviewItem("Very Good", "omg I loved it"),
            new ReviewItem("Bad", "bed bugs... what more can I say?")
    );
    ScreensController myController;
    @FXML private SplitMenuButton locationMenuGiveReview;
    @FXML private SplitMenuButton locationMenuSeeReview;
    @FXML private SplitMenuButton ratingMenu;
    @FXML private Text error_giving_review;
    @FXML private Text error_seeing_review;
    @FXML private TableView<ReviewItem> reviews_table;
    @FXML private TableColumn ratingCol;
    @FXML private TableColumn commentCol;


    private boolean already_created_reviews = false;


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
    public void seeReviews(ActionEvent event){
        boolean menu_is_selected = Validator.validate_all_menus_checked(locationMenuSeeReview, null, error_seeing_review);
        if(menu_is_selected){
            if(!already_created_reviews) {

                GridPane pane = new GridPane();
                Text header = new Text();
                header.setText("VIEW REVIEWS");
                pane.add(header, 0, 0);
                TableView table = new TableView();
                pane.add(table, 0, 2);
                Button b = new Button();
                b.setText("back");
                b.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Global.primaryStage.setScene(current);
                        myController.setScreen(Main.CUSTOMER_HOME_SCREEN);
                    }
                });
                pane.add(b, 0, 4);
                create_table(table);
                add_to_table(all_reviews,table);
                StackPane root = new StackPane();
                root.getChildren().add(pane);
                current = Global.primaryStage.getScene();
                Global.primaryStage.setScene(new Scene(root));
                Global.primaryStage.show();
                
                System.out.println("FINISHED ADDING TO TABLE");
                already_created_reviews = true;
            }
        }
    }

    @FXML
    public void submitReview(ActionEvent event){
        boolean all_menus_selected = Validator.validate_all_menus_checked(locationMenuGiveReview, ratingMenu, error_giving_review);
        if(!all_menus_selected){
            return;
        }
        //maybe have confirmation screen...
        myController.setScreen(Main.CUSTOMER_HOME_SCREEN);
        //DATABASE ADD A REVIEW
    }

    @FXML
    public void back (ActionEvent event){
        myController.setScreen(Main.CUSTOMER_HOME_SCREEN);
    }

    @FXML
    public void updateGiveLocationMenu(ActionEvent event){
        MenuItem item = (MenuItem)event.getSource();
        locationMenuGiveReview.setText(item.getText().toString());
    }

    @FXML
    public void updateSeeLocationMenu(ActionEvent event){
        MenuItem item = (MenuItem)event.getSource();
        locationMenuSeeReview.setText(item.getText().toString());
    }

    @FXML
    public void updateRatingMenu(ActionEvent event){
        MenuItem item = (MenuItem)event.getSource();
        ratingMenu.setText(item.getText().toString());
    }


    private void create_table(TableView table){

        TableColumn rating = new TableColumn("Rating");
        rating.setMinWidth(100);
        TableColumn comment = new TableColumn("Comment");
        comment.setMinWidth(100);

        rating.setCellValueFactory(
                new PropertyValueFactory<ReviewItem,String>("rating")
        );
        comment.setCellValueFactory(
                new PropertyValueFactory<ReviewItem,String>("comment")
        );
        table.getColumns().addAll(rating, comment);
    }

    private void add_to_table(ObservableList<ReviewItem> reviews, TableView table){
        table.setItems(null);
        table.setItems(reviews);
    }

    public class ReviewItem{
        private String rating;
        private String comment;

        public ReviewItem(String rating, String comment){
            this.rating = new String(rating);
            this.comment = new String(comment);
        }
        public String getRating() {
            return this.rating;
        }
        public String getComment() {
            return this.comment;
        }
    }


}
