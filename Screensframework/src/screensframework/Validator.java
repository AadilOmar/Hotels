package screensframework;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aadil on 11/29/15.
 */
public class Validator {


    private Validator() {

    }

    //returns String error if there is issues with the date. Returns null if no errors
    public static boolean validate_reservation_date(String start, String end, Text errorText) {

        String validDate = "([0-9]{4})-([0-9]{2})-[0-9]{2}";
        if (!(start.matches(validDate) && end.matches(validDate))) {
            errorText.setText("Date not in the format yyyy-MM-dd");
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            currentDate = sdf.parse(sdf.format(currentDate));
            startDate = sdf.parse(start);
            endDate = sdf.parse(end);
        } catch (ParseException e) {
            errorText.setText("error reading date values");
            return false;
        }

        if (startDate.compareTo(endDate) >= 0) {
            errorText.setText("End Date must be after start Date");
            return false;
        }
        if (currentDate.compareTo(startDate) > 0 || currentDate.compareTo(endDate) > 0) {
            errorText.setText("Dates of Stay must be after current date");
            return false;
        }
        errorText.setText("");
        return true;
    }

    public static boolean validate_selected_card(SplitMenuButton card, Text errorText) {
        if (card.getText().equals("card")) {
            errorText.setText("Choose a card to proceed with the reservation");
            return false;
        }
        if (card.getItems().size() == 0) {
            errorText.setText("Add a card to proceed with the reservation");
            return false;
        }
        errorText.setText("");
        return true;
    }

    public static boolean validate_adding_card(TextField card_name, TextField card_number, TextField exp_date, TextField cvv, Text errorText) {
        String validExp = "([0-9]{4})-[0-9]{2}";
        String validCvv = "([0-9]{3})||([0-9]{4})||([0-9]{2})";
        String validCardNumber = "([0-9]{16})";

        if(card_name.getText().equals("") || card_number.getText().equals("") || exp_date.getText().equals("") || cvv.getText().equals("")){
            errorText.setText("All fields must be filled");
            return false;
        }
        if(card_name.getText().length()<3){
            errorText.setText("name on card must be longer");
            return false;
        }
        if(!card_number.getText().matches(validCardNumber)){
            errorText.setText("card number must have 16 digits");
            return false;
        }
        if(!exp_date.getText().matches(validExp)){
            errorText.setText("expiration is not in the form yyyy-mm");
            return false;
        }
        if(!cvv.getText().matches(validCvv)){
            errorText.setText("ccv must have between 2 and 4 digits");
            return false;
        }
        errorText.setText("");
        return true;
    }

    public static boolean validate_reservation_id(String id, Text errorText) {
        boolean validId = true;
        if (!validId){
            errorText.setText("A future reservation with that ID could not be found");
            return false;
        }
        errorText.setText("");
        return true;
    }

    public static boolean validate_all_menus_checked(SplitMenuButton location, SplitMenuButton rating, Text errorText){
        if(rating==null){
            if(location.getText().equals("Hotel Location")){
                errorText.setText("Location must be selected see reviews");
                return false;
            }
        }
        else{
            if(location.getText().equals("Hotel Location") || rating.getText().equals("Rating")){
                errorText.setText("Location and Rating must be selected to submit a review");
                return false;
            }
        }

        return true;
    }


}

