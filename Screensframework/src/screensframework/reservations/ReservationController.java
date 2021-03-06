/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License"). You
 * may not use this file except in compliance with the License. You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package screensframework.reservations;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.sun.org.apache.bcel.internal.ExceptionConstants;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import screensframework.*;
import screensframework.com.util.ConnectionConfiguration;
import screensframework.com.util.QuerySender;

/**
 * FXML Controller class
 *
 * @author Aadil
 */
public class ReservationController implements Initializable, ControlledScreen {
    boolean dontAddBedInfo = false;

    ObservableList<Room> all_rooms =FXCollections.observableArrayList(

    );
    ObservableList<Room> selected_rooms = FXCollections.observableArrayList(
    );
    ScreensController myController;
    //for all
    @FXML private TableView all_rooms_table;
    @FXML private TableView all_to_cancel_rooms_table;
    @FXML private TableView all_reserved_rooms_table; //table for updating reservations
    @FXML private TableView checked_rooms_table;
    @FXML private TableView all_cancelled_rooms_table;

    @FXML private TableColumn column;
    @FXML private SplitMenuButton card;
    @FXML private Text confirmation_reservation_id;

    long length_of_stay = 1;

    private boolean already_created_all_rooms = false;
    private boolean already_created_checked_rooms = false;
    private boolean already_created_reserved_rooms = false;
    private boolean already_created_cancelled_rooms = false;

    //error texts for each of the views that need one
    @FXML private Text error_search_all;
    @FXML private Text error_picked_rooms;
    @FXML private Text error_invalid_reservaion_id;
    @FXML private Text error_search_reservation;
    @FXML private Text error_rooms_not_available_for_update;
    @FXML private Text error_invalid_reservaion_id_cancel;
    @FXML private Text error_invalid_dates_cancel;


    //for searching all rooms
    @FXML private TextField start_date;
    @FXML private TextField end_date;
    @FXML private SplitMenuButton location;


    //for looking at selected rooms
    @FXML private Text start_date_picked;
    @FXML private Text end_date_picked;
    @FXML private Text total_cost;

    //for updating reservation
    @FXML private TextField reservation_id;
    @FXML private Text current_start_date;
    @FXML private Text current_end_date;
    @FXML private TextField new_start_date;
    @FXML private TextField new_end_date;
    @FXML private Text updated_cost;
    int total_rooms = 0;
    int availablerooms = 0;

    //for canceling reservation

    @FXML private Text total_cost_reserved;
    @FXML private Text cancellation_date;
    @FXML private Text amount_refunded;
    @FXML private Text start_date_cancelled;
    @FXML private Text end_date_cancelled;



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    //removes the reservation from the database
    @FXML
    public void cancelReservation(ActionEvent event){
        //have confirmation screen maybe?...if time
        myController.setScreen(Main.CUSTOMER_HOME_SCREEN);
    }

    //finds if there is a reservation by the ID given. updates the dates of original reservation
    @FXML
    public void searchReservationCancel(ActionEvent event){

        String reservationId = reservation_id.getText().toString();

        //check if reservations exist by that reservation id


        boolean idIsValid = Validator.validate_reservation_id(reservationId, error_invalid_reservaion_id_cancel);

        if(!already_created_cancelled_rooms){

            create_table(all_cancelled_rooms_table);
            createTableListener();
            already_created_cancelled_rooms = true;
        }
        setCancelledDate();
        add_to_table(all_rooms, all_cancelled_rooms_table);
        updateTotalCost(all_rooms, total_cost_reserved, 0);
        setAmountToBeRefunded(total_cost_reserved.getText());



    }

    //finds if there is a reservation by the ID given. updates the dates of original reservation
    @FXML
    public void searchReservation(ActionEvent event){

        String reservationId = reservation_id.getText().toString();

        //check if reservations exist by that reservation id
        System.out.println("11111");
        boolean validId = false;
        String start = "";
        String end = "";
        ResultSet roomsInReservation = QuerySender.getRoomsOfReservation(reservation_id.getText());
        try{
            System.out.println("2222");
            while(roomsInReservation.next()){
                System.out.println("-------");
                String isCancelled = roomsInReservation.getString("is_Cancelled");
                start = roomsInReservation.getString("Start_Date");
                end = roomsInReservation.getString("End_Date");
                if(isCancelled.equals("0")){
                    validId = true;
                    total_rooms++;
                }
            }
            roomsInReservation.close();
        }catch(Exception e){
            System.out.println("lkasflkasjf");
        }
        if(validId){
            System.out.println("5555");
            current_start_date.setText(start);
            current_end_date.setText(end);
            error_invalid_reservaion_id_cancel.setText("");
        }
        else{
            error_invalid_reservaion_id_cancel.setText("A reservation with that ID could not be found");
        }


    }


    //searches if the rooms have availability in the dates specified. Returns no rooms if they dont
    @FXML
    public void searchAvailability(ActionEvent event){

        boolean datesAreValid = Validator.validate_reservation_date(new_start_date.getText(), new_end_date.getText(), error_search_reservation);
        if(datesAreValid){
            boolean allRoomsAvailable = true;

            ResultSet result = QuerySender.searchAvailability(reservation_id.getText(), new_start_date.getText(), new_end_date.getText());
            System.out.println("RESULT!!! "+result);
            try {
                while (result.next()) {
                    String roomNumber = result.getString("Room_Number");
                    String roomCategory = result.getString("Room_Category");
                    String location = result.getString("Hotel_Location");
                    int costPerDay = Integer.parseInt(result.getString("Cost"));
                    int costExBedPerDay = Integer.parseInt(result.getString("Cost_Extra_Bed"));
                    int numPeopleAllowed = Integer.parseInt(result.getString("Number_People"));
                    System.out.println(costExBedPerDay +" cc");
                    availablerooms++;
                    Room newRoom = new Room(roomNumber, roomCategory, numPeopleAllowed, costPerDay, costExBedPerDay, "x", "x");
                    all_rooms.add(newRoom);
                }

            }catch(Exception e){
                e.printStackTrace();
                System.out.println("fuck me");
            }

            if(total_rooms == availablerooms){
                error_rooms_not_available_for_update.setText("");
                if(!already_created_reserved_rooms){
                    dontAddBedInfo = true;
                    create_table(all_reserved_rooms_table);
                    createTableListener();
                    dontAddBedInfo = false;
                    already_created_reserved_rooms = true;
                }
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = null;
                Date endDate= null;
                try {
                    startDate = df.parse(new_start_date.getText());
                    endDate = df.parse(new_end_date.getText());
                }catch(Exception e){

                }
                length_of_stay = endDate.getTime() - startDate.getTime();
                length_of_stay = TimeUnit.DAYS.convert(length_of_stay, TimeUnit.MILLISECONDS);
                add_to_table(all_rooms, all_reserved_rooms_table);
                updateTotalCost(all_rooms, updated_cost, (int)length_of_stay);
            }
            else{
                error_rooms_not_available_for_update.setText("The rooms are not available for this date range");
            }
        }
    }

    @FXML
    public void back (ActionEvent event){
        myController.setScreen(Main.CUSTOMER_HOME_SCREEN);
    }


    //updates the reservation in the database based on what dates were selected
    @FXML
    public void update_reservation(ActionEvent event){
        //QUERY TO UPDATE RESERVATION
        confirmation_reservation_id.setText(reservation_id.getText());
        myController.setScreen(Main.RESERVATION_CONFIRM_SCREEN);
    }

    @FXML
    //should display the rooms it finds from the database
    public void search_all_rooms(ActionEvent event){
        String start = start_date.getText();
        String end = end_date.getText();
        String loc = location.getText();

//        boolean datesAreValid = Validator.validate_reservation_date(start, end, error_search_all);
//        if(!datesAreValid){ return; }


        ResultSet result = QuerySender.findAllRooms(start, end, loc);
        try{
            while(result.next()){
                String roomNumber = result.getString("Room_Number");
                String roomCategory = result.getString("Room_Category");
                String location = result.getString("Hotel_Location");
                int costPerDay = Integer.parseInt(result.getString("Cost"));
                int costExBedPerDay = Integer.parseInt(result.getString("Cost_Extra_Bed"));
                int numPeopleAllowed = Integer.parseInt(result.getString("Number_People"));
                Room newRoom = new Room(roomNumber, roomCategory, numPeopleAllowed, costPerDay, costExBedPerDay, "x", "x");
                all_rooms.add(newRoom);
            }
            result.close();
        }catch(Exception e){
            System.out.println("lkasflkasjf");
        }

        Global.newReservationStart = start;
        Global.newReservationEnd = end;
        myController.setScreen(Main.VIEW_ALL_ROOMS_SCREEN);
        if(!already_created_all_rooms) {
            create_table(all_rooms_table);
            already_created_all_rooms=true;
            createTableListener();
        }
        add_to_table(all_rooms, all_rooms_table);
    }

    @FXML
    //submits the reservations
    public void submit(ActionEvent event){
        boolean card_is_valid = Validator.validate_selected_card(card, error_picked_rooms);
        if (!card_is_valid){ return; }
        int num_rooms = selected_rooms.size();
        String card_name = "";
        String card_num = "";
        String start = start_date_picked.getText();
        String end = end_date_picked.getText();
        String total = total_cost.getText();
        String new_id = ""; //create new id
        QuerySender.makeReservationReservationTable(Global.username, new_id, card_num, start, end, total, "0");
        for(int x=0;x<num_rooms;x++){
            QuerySender.makeReservationHasTable(new_id, selected_rooms.get(x).getRoomNumber(), location.getText(), selected_rooms.get(x).getSelectedBed());
        }
        confirmation_reservation_id.setText(new_id);
        myController.setScreen(Main.RESERVATION_CONFIRM_SCREEN);
    }

    @FXML
    //goes to add_card view.
    public void add_card(ActionEvent event){
        myController.setScreen(Main.ADD_CARD_SCREEN);
    }

    @FXML
    //should display the rooms that were clicked as well as the start date, end date, and total cost
    public void checkDetails(ActionEvent event){
        //sets the start/end date text to whatever was put in when creating reservation
        ArrayList<MenuItem> list = new ArrayList<MenuItem>();
        ArrayList<String> stringList = new ArrayList<String>();
        ResultSet result = QuerySender.getCreditCards();
        try {
            while (result.next()) {
                System.out.println("_________________");
                String card_number = result.getString("Card_Number");
                card_number = card_number.substring(card_number.length() - 5);
                MenuItem item = new MenuItem("cw");
                stringList.add(card_number);
                list.add(item);
                System.out.println(list+" l");
            }
        }catch(Exception e){
//            e.printStackTrace();
            System.out.println("NOOO");
        }
        Global.cards = stringList;
        card.getItems().addAll(list);
//        card.getItems().addAll(new MenuItem("Logout"), new MenuItem("Sleep"));
        System.out.println("+++"+list);
//        card.getItems().addAll(list);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate= null;
        try {
            startDate = df.parse(start_date.getText());
            endDate = df.parse(end_date.getText());
        }catch(Exception e){

        }
        length_of_stay = endDate.getTime() - startDate.getTime();
        length_of_stay = TimeUnit.DAYS.convert(length_of_stay, TimeUnit.MILLISECONDS);
        System.out.println("LENGTH"+length_of_stay);
        start_date_picked.setText(Global.newReservationStart);
        updateTotalCost(selected_rooms, total_cost, (int)length_of_stay);
        end_date_picked.setText(Global.newReservationEnd);
        myController.setScreen(Main.VIEW_CHECKED_ROOMS_SCREEN);
        if(!already_created_checked_rooms){
            create_table(checked_rooms_table);
            already_created_checked_rooms=true;
            createTableListener();
        }
        add_to_table(selected_rooms,checked_rooms_table);
    }

    @FXML
    public void updateCardMenu(ActionEvent event){
        MenuItem item = (MenuItem)event.getSource();
        card.setText(item.getText().toString());
    }
    @FXML
    public void updateLocationMenu(ActionEvent event){
        MenuItem item = (MenuItem)event.getSource();
        location.setText(item.getText().toString());
    }

    private String create_reservation_id(){
        //return id
        int total_reservations = 100;
        int id = total_reservations+1;
        Random rand = new Random();
        return rand.nextInt(100000)+"";
    }

    private void updateTotalCost(ObservableList<Room> selectedRooms, Text toUpdate, int stay_length){
        int total = 0;
        for(int x=0;x<selectedRooms.size();x++){
            Room r = selectedRooms.get(x);
            total+=r.costPerDay.get();
            if(r.getSelectedBed().equals("yes")){
                total+=r.getCostExtraBedPerDay();
            }
        }
        toUpdate.setText("$"+total*stay_length+".00");
    }

    private void setCancelledDate(){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        cancellation_date.setText(dateFormat.format(date));

    }
    private void setAmountToBeRefunded(String totalCost){
        NumberFormat format = NumberFormat.getCurrencyInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date currentDate = new Date();
        Date startDate = new Date();
        int total = 0;
        try{
            total = Integer.parseInt(format.parse(totalCost).toString());
            startDate = sdf.parse(start_date_cancelled.getText());
            currentDate = sdf.parse(sdf.format(currentDate));
        }catch(ParseException e){

        }
        long diff = startDate.getTime() - currentDate.getTime();
        long daysAway = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        int refund = 0;
        if(daysAway > 3){
            refund = total;
        }
        else if(daysAway > 1 && daysAway <= 3){
            refund = (int)(total*.8);
        }
        else if(daysAway <= 1){
            refund = 0;
        }
        amount_refunded.setText("$"+refund+".00");
    }

    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    private void createTableListener(){
        all_rooms_table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown()) {
                    System.out.println(all_rooms_table.getSelectionModel().getSelectedItem());
                    Room roomSelected = (Room)all_rooms_table.getSelectionModel().getSelectedItem();
                    if(roomSelected.selected.get().equals("x")){
                        roomSelected.selected = new SimpleStringProperty("yes");
                        selected_rooms.add(roomSelected);
                    }
                    else{
                        roomSelected.selected = new SimpleStringProperty("x");
                        selected_rooms.remove(roomSelected);
                    }
                    TableColumn c = (TableColumn)all_rooms_table.getColumns().get(0);
                    c.setVisible(false);
                    c.setVisible(true);
                }
            }
        });
        checked_rooms_table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown()) {
                    System.out.println(checked_rooms_table.getSelectionModel().getSelectedItem());
                    Room roomSelected = (Room)checked_rooms_table.getSelectionModel().getSelectedItem();
                    if(roomSelected.selectedBed.get().equals("x")){
                        roomSelected.selectedBed = new SimpleStringProperty("yes");
                    }
                    else{
                        roomSelected.selectedBed = new SimpleStringProperty("x");
                    }
                    updateTotalCost(selected_rooms, total_cost, (int)length_of_stay);
                    TableColumn c = (TableColumn)checked_rooms_table.getColumns().get(0);
                    c.setVisible(false);
                    c.setVisible(true);
                }
            }
        });
    }

    private void create_table(TableView table){

        TableColumn roomNumber = new TableColumn("Room Number");
        roomNumber.setMinWidth(100);
        TableColumn roomType = new TableColumn("Room Type");
        roomType.setMinWidth(100);
        TableColumn maxPeople = new TableColumn("Max People");
        maxPeople.setMinWidth(100);
        TableColumn costPerDay = new TableColumn("Cost per Day");
        costPerDay.setMinWidth(100);
        TableColumn costExtraBed = new TableColumn("Cost Extra Bed");
        costExtraBed.setMinWidth(100);
        TableColumn selected = null;
        if (table == checked_rooms_table) {
            selected = new TableColumn("Extra Bed");
            selected.setMinWidth(100);
        } else if (table == all_reserved_rooms_table) {
            selected = new TableColumn("Extra Bed");
            selected.setMinWidth(100);
        } else {
            selected = new TableColumn("Select");
            selected.setMinWidth(100);
        }



        roomNumber.setCellValueFactory(
                new PropertyValueFactory<Room,String>("roomNumber")
        );
        roomType.setCellValueFactory(
                new PropertyValueFactory<Room,String>("roomCategory")
        );
        maxPeople.setCellValueFactory(
                new PropertyValueFactory<Room,Integer>("numPeopleAllowed")
        );
        costPerDay.setCellValueFactory(
                new PropertyValueFactory<Room,Integer>("costPerDay")
        );
        costExtraBed.setCellValueFactory(
                new PropertyValueFactory<Room, Integer>("costExtraBedPerDay")
        );
        if (table == (all_reserved_rooms_table)) {
            selected.setCellValueFactory(
                    new PropertyValueFactory<Room, Boolean>("selectedBed")
            );
        }
        if (table == (checked_rooms_table)) {
            selected.setCellValueFactory(
                    new PropertyValueFactory<Room, Boolean>("selectedBed")
            );
        } else {
            selected.setCellValueFactory(
                    new PropertyValueFactory<Room, Boolean>("selected")
            );
        }

        table.getColumns().addAll(roomNumber, roomType, maxPeople, costPerDay, costExtraBed, selected);
    }

    private void add_to_table(ObservableList<Room> rooms, TableView table){
        table.setItems(null);
        table.setItems(rooms);

    }
}

