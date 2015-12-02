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
import java.sql.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
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
    boolean allRoomsAvailable;

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
    @FXML private Text error_no_rooms_selected;
    @FXML private Text error_no_permission;

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
    @FXML private TextField reservation_id_cancel;



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

        ArrayList<Room> rooms= new ArrayList<Room>();
        String id = reservation_id_cancel.getText();

        ResultSet roomsInReservation = QuerySender.getRoomsOfReservation(id);
        String userThatReserved = null;
        int total_cost_of_reservation = 0;
        String start_date_of_reservation = null;
        try {
            while (roomsInReservation.next()) {
                userThatReserved = roomsInReservation.getString("Username");
                total_cost_of_reservation =  roomsInReservation.getInt("Total_Cost");
                start_date_of_reservation =  roomsInReservation.getString("Start_Date");
            }
        }catch(Exception e){e.printStackTrace();}
        if(!Global.username.equals(userThatReserved)){
            error_no_permission.setText("You do not have permission to cancel this reservation");
            return;
        }
        else{
            error_no_permission.setText("");
        }

//        int refund = Integer.parseInt(amount_refunded.getText().replace("$", "").replace(".00","0"));
        int refund = setAmountToBeRefunded(total_cost_of_reservation+"", start_date_of_reservation);
        int to_save = total_cost_of_reservation-refund;
        int result = QuerySender.cancelReservation(id, to_save+"");
        total_cost_reserved.setText(total_cost_of_reservation+"");
        amount_refunded.setText(refund+"");
        System.out.println("DLETED RESERVATION: "+result);


        myController.setScreen(Main.CUSTOMER_HOME_SCREEN);
    }

    //finds if there is a reservation by the ID given. updates the dates of original reservation
    @FXML
    public void searchReservationCancel(ActionEvent event){
        all_rooms.removeAll();
        all_rooms = FXCollections.observableArrayList();
        all_cancelled_rooms_table.getItems().removeAll();
        if(all_cancelled_rooms_table.getColumns().size() != 0){
            System.out.println("Asdfasdf");
            ObservableList<TableColumn> cols = (all_cancelled_rooms_table.getColumns());
            cols.get(0).setVisible(false);
            cols.get(0).setVisible(true);
            all_rooms.removeAll();
        }

        String reservationId = reservation_id_cancel.getText().toString();

        //check if reservations exist by that reservation id
        System.out.println("11111");
        boolean validId = false;
        String start = "";
        String end = "";
        String total = "";
        ResultSet roomsInReservation = QuerySender.getRoomsOfReservation(reservationId);
        try{
            System.out.println("2222");
            while(roomsInReservation.next()){
                System.out.println("-------");
                String isCancelled = roomsInReservation.getString("is_Cancelled");
                start = roomsInReservation.getString("Start_Date");
                end = roomsInReservation.getString("End_Date");
                total = roomsInReservation.getString("Total_Cost");
                System.out.println(start);
                System.out.println(end);
                if(isCancelled.equals("0")){
                    validId = true;
                }
            }
            roomsInReservation.close();
        }catch(Exception e){
            System.out.println("lkasflkasjf");
        }
        if(validId){
            System.out.println("5555");
            start_date_cancelled.setText(start);
            end_date_cancelled.setText(end);
            error_invalid_reservaion_id_cancel.setText("");
        }
        else{
            error_invalid_reservaion_id_cancel.setText("You don't have an existing reservation with the ID you specified");
            return;
        }
        ResultSet result = QuerySender.searchAvailabilityToCancel(reservationId);
        System.out.println("RESULT!!! "+result);
        try {
            while (result.next()) {
                System.out.println("IN LOOP!!");
                String roomNumber = result.getString("Room_Number");
                String roomCategory = result.getString("Room_Category");
                String location = result.getString("Hotel_Location");
                int costPerDay = Integer.parseInt(result.getString("Cost"));
                int costExBedPerDay = Integer.parseInt(result.getString("Cost_Extra_Bed"));
                int numPeopleAllowed = Integer.parseInt(result.getString("Number_People"));
                String selectedBed = result.getString("Include_Extra_Bed");
                Room newRoom = new Room(roomNumber, roomCategory, numPeopleAllowed, costPerDay, costExBedPerDay, "x", selectedBed);
                System.out.println("NEW ROOM"+newRoom.toString());
                all_rooms.add(newRoom);
            }

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("fuck me");
        }

        if(!already_created_cancelled_rooms){

            create_table(all_cancelled_rooms_table);
            createTableListener();
            already_created_cancelled_rooms = true;
            all_cancelled_rooms_table.setItems(all_rooms);
        }
        add_to_table(all_rooms, all_cancelled_rooms_table);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate= null;
        try {
            startDate = df.parse(start_date_cancelled.getText());
            endDate = df.parse(end_date_cancelled.getText());
        }catch(Exception e){
            System.out.println("DAMN!!");
            e.printStackTrace();
        }
        length_of_stay = endDate.getTime() - startDate.getTime();
        length_of_stay = TimeUnit.DAYS.convert(length_of_stay, TimeUnit.MILLISECONDS);
//        updateTotalCost(all_rooms, total_cost_reserved, (int)length_of_stay);
        setCancelledDate();
        total_cost_reserved.setText(total);
        setAmountToBeRefunded(total, start_date_cancelled.getText());
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
            error_invalid_reservaion_id.setText("");
        }
        else{
            allRoomsAvailable = false;
            error_invalid_reservaion_id.setText("A reservation with that ID could not be found");
        }


    }


    //searches if the rooms have availability in the dates specified. Returns no rooms if they dont
    @FXML
    public void searchAvailability(ActionEvent event){

        boolean datesAreValid = Validator.validate_reservation_date(new_start_date.getText(), new_end_date.getText(), error_search_reservation);
        if(datesAreValid){
            allRoomsAvailable = true;
            availablerooms = 0;
            all_rooms = FXCollections.observableArrayList();
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
        if (allRoomsAvailable) {
            QuerySender.updateReservation(reservation_id.getText(), new_start_date.getText(), new_end_date.getText(), Integer.parseInt(total_cost.getText()));
            confirmation_reservation_id.setText(reservation_id.getText());
            System.out.println("update");
            myController.setScreen(Main.RESERVATION_CONFIRM_SCREEN);
            } else {
            error_rooms_not_available_for_update.setText("Unable to update reservation since rooms are unavailable");
            }
        confirmation_reservation_id.setText(reservation_id.getText());
        myController.setScreen(Main.RESERVATION_CONFIRM_SCREEN);
    }

    @FXML
    //should display the rooms it finds from the database
    public void search_all_rooms(ActionEvent event){
        String start = start_date.getText();
        String end = end_date.getText();
        String loc = location.getText();

        boolean datesAreValid = Validator.validate_reservation_date(start, end, error_search_all);
        if(!datesAreValid){ return; }

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
        String card_num = card.getText();
        String start = start_date_picked.getText();
        String end = end_date_picked.getText();
        String total = total_cost.getText();
        total = total.replace("$","");
        total = total.replace(".00","");
        String new_id = "";
        ResultSet numReservations = QuerySender.getNumReservations();
        try {
            while (numReservations.next()) {
                new_id = (numReservations.getInt("Max")+1)+"";
            }
        }catch(Exception e){e.printStackTrace();}
        int madeReservation = QuerySender.makeReservationReservationTable(Global.username, new_id, card_num, start, end, total, "0");
        System.out.println("MADE RESERVATION: "+madeReservation);
        for(int x=0;x<num_rooms;x++){
            int selected_bed = 0;
            if(selected_rooms.get(x).getSelectedBed().equals("yes")){
                selected_bed = 1;
            }
            int madeHas = QuerySender.makeReservationHasTable(new_id, selected_rooms.get(x).getRoomNumber(), location.getText(), selected_bed+"");
            System.out.println("MADE HAS: "+madeHas);
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
        if(selected_rooms.size()==0){
            error_no_rooms_selected.setText("No rooms were selected");
            return;
        }
        else{
            error_no_rooms_selected.setText("");
        }
        card.getItems().removeAll();

        //sets the start/end date text to whatever was put in when creating reservation
        ArrayList<MenuItem> list = new ArrayList<MenuItem>();
        card.getItems().addAll(list);
        ArrayList<String> stringList = new ArrayList<String>();
        try {
            ResultSet result = QuerySender.getCreditCards(Global.username);
            while (result.next()) {
                String card_number = result.getString("Card_Number");
                System.out.println("_________________" + card_number);
                MenuItem item = new MenuItem(card_number);
                item.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        MenuItem i = (MenuItem) actionEvent.getSource();
                        card.setText(i.getText());
                    }
                });
                stringList.add(card_number);
                list.add(item);
                System.out.println(list + " l");
            }
        } catch (Exception e) {
            //            e.printStackTrace();
            System.out.println("NOOO");
        }
        Global.cards = stringList;
        Global.cardItems = list;
        System.out.println("ASDFASDF"+stringList);

        card.setVisible(false);
        card.setVisible(true);
        System.out.println("+++"+list);
        card.getItems().addAll(list);
//        updateCards(stringList);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate= null;
        try {
            startDate = df.parse(Global.newReservationStart);
            endDate = df.parse(Global.newReservationEnd);
        }catch(Exception e){
            e.printStackTrace();
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

    private void updateCards(ArrayList<String> list){
        //item was added
        System.out.println("WAS ADDED");
        if(card.getItems().size() < list.size()){
            card.getItems().add(new MenuItem(list.get(list.size()-1)));
        }
        //item was removed
        else if (card.getItems().size() > list.size()){
            System.out.println("WAS REMOVED");
            int toRemove = 0;
            for(int x=0;x<card.getItems().size();x++){
                //list doesnt contain item
                if(!list.contains(card.getItems().get(x))){
                    toRemove = x;
                }
            }
            card.getItems().remove(toRemove);
        }
        for(int x=0;x<card.getItems().size();x++){

        }
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
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        cancellation_date.setText(dateFormat.format(date));

    }
    private int setAmountToBeRefunded(String totalCost, String start){
        NumberFormat format = NumberFormat.getCurrencyInstance();
        System.out.println("nigga yes");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        Date startDate = new Date();
        String total = null;
        try{
            total = format.parse(totalCost).toString();
            System.out.println(total);
            startDate = sdf.parse(start);
            currentDate = sdf.parse(sdf.format(currentDate));
        }catch(ParseException e){
            e.printStackTrace();
        }
        long diff = startDate.getTime() - currentDate.getTime();
        long daysAway = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        System.out.println(startDate);
        System.out.println(currentDate);
        System.out.println(daysAway);
        int refund = 0;
        if(daysAway > 3){
            refund = Integer.parseInt(total);
        }
        else if(daysAway > 1 && daysAway <= 3){
            refund = (int)(Integer.parseInt(total)*.8);
        }
        else if(daysAway <= 1){
            refund = 0;
        }
        amount_refunded.setText("$"+refund+".00");
        return refund;
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
        System.out.println("NOOOOOOOOOOOOO");
        table.setItems(rooms);
        ObservableList<TableColumn> cols = (table.getColumns());
        cols.get(0).setVisible(false);
        cols.get(0).setVisible(true);
        System.out.println("WTF-  DLETE!!"+rooms.size());


    }
}

