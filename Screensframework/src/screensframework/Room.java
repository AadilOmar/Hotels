package screensframework;

/**
 * Created by aadil on 11/22/15.
 */
public class Room {

    public String roomNumber;
    public String roomCategory;
    public int numPeopleAllowed;
    public int costPerDay;
    public int costExtraBedPerDay;

    public Room(String roomNo, String category, int numPpl, int costDay, int costExBed){
        this.roomNumber = roomNumber;
        this.roomCategory = category;
        this.numPeopleAllowed = numPpl;
        this.costExtraBedPerDay = costExBed;
        this.costPerDay = costDay;
    }




}
