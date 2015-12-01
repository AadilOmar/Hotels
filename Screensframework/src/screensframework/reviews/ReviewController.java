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
import screensframework.com.util.QuerySender;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadFactory;

/**
 * Created by aadil on 11/27/15.
 */
public class ReviewController implements Initializable, ControlledScreen {

    Scene current;
    ObservableList<ReviewItem> all_reviews = FXCollections.observableArrayList(
//            new ReviewItem("Good", "it was alright of a place"),
//            new ReviewItem("Very Good", "omg I loved it"),
//            new ReviewItem("Bad", "bed bugs... what more can I say?")
    );
    ScreensController myController;
    @FXML private SplitMenuButton locationMenuGiveReview;
    @FXML private SplitMenuButton locationMenuSeeReview;
    @FXML private SplitMenuButton ratingMenu;
    @FXML private Text error_giving_review;
    @FXML private Text error_seeing_review;
    @FXML private TableView reviews_table;
    @FXML private TableColumn ratingCol;
    @FXML private TableColumn commentCol;
    @FXML private TextField comment;


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

        all_reviews = FXCollections.observableArrayList();
        boolean menu_is_selected = Validator.validate_all_menus_checked(locationMenuSeeReview, null, error_seeing_review);
        TableView table = null;
        if(menu_is_selected){
            ResultSet result;
            try {
                result = QuerySender.viewReviews(locationMenuSeeReview.getText());
                while(result.next()){
                    String comment = result.getString("Comment");
                    String rating = result.getString("Rating");
                    all_reviews.add(new ReviewItem(rating, comment));
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            GridPane pane = new GridPane();
            Text header = new Text();
            header.setText("VIEW REVIEWS");
            pane.add(header, 0, 0);
            table = new TableView();
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
            StackPane root = new StackPane();
            root.getChildren().add(pane);
            current = Global.primaryStage.getScene();
            Global.primaryStage.setScene(new Scene(root));
            Global.primaryStage.show();

            System.out.println("FINISHED ADDING TO TABLE");
            add_to_table(all_reviews,table);

        }
    }

    @FXML
    public void submitReview(ActionEvent event){
        boolean all_menus_selected = Validator.validate_all_menus_checked(locationMenuGiveReview, ratingMenu, error_giving_review);
        if(!all_menus_selected){
            return;
        }
        ResultSet set;
        int numReviews = 0;
        try {
            set = QuerySender.getNumReviews();
            while(set.next()) {
                numReviews = set.getInt("Max");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        //maybe have confirmation screen...
        myController.setScreen(Main.CUSTOMER_HOME_SCREEN);
        int result = QuerySender.createReview(Global.username, (numReviews+1)+"", locationMenuGiveReview.getText(), ratingMenu.getText(), comment.getText());
        System.out.println("REVIEW RESULT!! "+result);
    }

    @FXML
    public void back (ActionEvent event){
        all_reviews.removeAll();
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
//        table.setItems(null);
//        table.setItems(reviews);
        System.out.println("NOOOOOOOOOOOOO");
        table.setItems(reviews);
        ObservableList<TableColumn> cols = (table.getColumns());
        cols.get(0).setVisible(false);
        cols.get(0).setVisible(true);
        System.out.println("WTF-  DLETE!!"+reviews.size());
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