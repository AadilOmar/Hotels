package screensframework;

import javafx.scene.control.SplitMenuButton;
import javafx.scene.text.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aadil on 11/29/15.
 */
public class Validator {

    private Validator(){

    }

    //returns String error if there is issues with the date. Returns null if no errors
    public static boolean validate_reservation_date(String start, String end, Text errorText){

        String validDate = "([0-9]{2})/([0-9]{2})/[0-9]{4}";
        if(!(start.matches(validDate) && end.matches(validDate))){
            errorText.setText("Date not in the format mm/dd/yyy");
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date currentDate = new Date();
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            currentDate = sdf.parse(sdf.format(currentDate));
            startDate = sdf.parse(start);
            endDate = sdf.parse(end);
        }catch(ParseException e){
            errorText.setText("error reading date values");
            return false;
        }

        if(startDate.compareTo(endDate) >= 0 ){
            errorText.setText("End Date must be after start Date");
            return false;
        }
        if(currentDate.compareTo(startDate) > 0 || currentDate.compareTo(endDate) > 0){
            errorText.setText("Dates of Stay must be after current date");
            return false;
        }

        return true;
    }

    public static boolean validate_selected_card(SplitMenuButton card, Text errorText){
        if(card.getText().equals("card")){
            errorText.setText("Choose a card to proceed with the reservation");
            return false;
        }
        if(card.getItems().size()==0){
            errorText.setText("Add a card to proceed with the reservation");
            return false;
        }
        return true;
    }
}
