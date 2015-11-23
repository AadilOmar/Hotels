package screensframework;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by aadil on 11/22/15.
 */
public class Room {

    public SimpleStringProperty roomNumber;
    public SimpleStringProperty roomCategory;
    public SimpleIntegerProperty numPeopleAllowed;
    public SimpleIntegerProperty costPerDay;
    public SimpleIntegerProperty costExtraBedPerDay;
    public SimpleStringProperty selected;

    public Room(String roomNo, String category, int numPpl, int costDay, int costExBed, String selected){
        this.roomNumber = new SimpleStringProperty(roomNo);
        this.roomCategory = new SimpleStringProperty(category);
        this.numPeopleAllowed = new SimpleIntegerProperty(numPpl);
        this.costExtraBedPerDay = new SimpleIntegerProperty(costExBed);
        this.costPerDay = new SimpleIntegerProperty(costDay);
        this.selected = new SimpleStringProperty(selected);
    }

    public String getRoomNumber() {
        return this.roomNumber.get();
    }
    public String getRoomCategory() {
        return this.roomCategory.get();
    }

    public Integer getNumPeopleAllowed() {
        return this.numPeopleAllowed.get();
    }

    public Integer getCostPerDay() {
        return this.costPerDay.get();
    }
    public Integer getCostExtraBedPerDay() {
        return this.costExtraBedPerDay.get();
    }

    public String getSelected(){
        return this.selected.get();
    }

}
