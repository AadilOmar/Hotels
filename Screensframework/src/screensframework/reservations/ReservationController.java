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
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

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

/**
 * FXML Controller class
 *
 * @author Aadil
 */
public class ReservationController implements Initializable, ControlledScreen {
    ObservableList<Room> all_rooms =FXCollections.observableArrayList(
            new Room("01", "standard", 1, 80, 15, "x", "x"),
            new Room("02", "family", 4, 130, 30, "x", "x"),
            new Room("03", "suite", 5, 160, 30, "x", "x")
    );
    ObservableList<Room> selected_rooms = FXCollections.observableArrayList(
    );
    ScreensController myController;
    //for all
    @FXML private TableView all_rooms_table;
    @FXML private TableView all_to_cancel_rooms_table;
    @FXML private TableView all_reserved_rooms_table; //table for updating reservations
    @FXML private TableView checked_rooms_table;
    @FXML private TableColumn column;
    @FXML private SplitMenuButton card;
    @FXML private SplitMenuButton location;
    @FXML private Text confirmation_reservation_id;

    private boolean already_created_all_rooms = false;
    private boolean already_created_checked_rooms = false;
    private boolean already_created_reserved_rooms = false;

    //error texts for each of the views that need one
    @FXML private Text error_search_all;
    @FXML private Text error_picked_rooms;
    @FXML private Text error_invalid_reservaion_id;
    @FXML private Text error_search_reservation;
    @FXML private Text error_rooms_not_available_for_update;


    //for searching all rooms
    @FXML private TextField start_date;
    @FXML private TextField end_date;

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

    }

    //finds if there is a reservation by the ID given. updates the dates of original reservation
    @FXML
    public void searchReservation(ActionEvent event){

        String reservationId = reservation_id.getText().toString();

        //check if reservations exist by that reservation id
        boolean idIsValid = Validator.validate_reservation_id(reservationId, error_invalid_reservaion_id);
        if (idIsValid){
            String old_start = "01/12/2016";
            String old_end = "01/14/2016";
            current_start_date.setText(old_start);
            current_end_date.setText(old_end);
        }

    }

    //searches if the rooms have availability in the dates specified. Returns no rooms if they dont
    @FXML
    public void searchAvailability(ActionEvent event){
        boolean datesAreValid = Validator.validate_reservation_date(new_start_date.getText(), new_end_date.getText(), error_search_reservation);
        if(datesAreValid){
            boolean allRoomsAvailable = true;
            //QUERY WITH DATES TO SEE IF ALL ARE AVAILABLE ROOMS  (all_rooms should be the rooms found)
            if(allRoomsAvailable){
                error_rooms_not_available_for_update.setText("");
                if(!already_created_reserved_rooms){
                    create_table(all_reserved_rooms_table);
                    createTableListener();
                    already_created_reserved_rooms = true;
                }
                add_to_table(all_rooms, all_reserved_rooms_table);
                updateTotalCost(all_rooms, updated_cost);
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
        String start = start_date.getText().toString();
        String end = end_date.getText().toString();
//        String start = "09/29/2016";
//        String end =  "09/30/2016";
        boolean datesAreValid = Validator.validate_reservation_date(start, end, error_search_all);
        if(!datesAreValid){ return; }
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
        String new_id = create_reservation_id();
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
        start_date_picked.setText(Global.newReservationStart);
        updateTotalCost(selected_rooms, total_cost);
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

    private void updateTotalCost(ObservableList<Room> selectedRooms, Text toUpdate){
        int total = 0;
        for(int x=0;x<selectedRooms.size();x++){
            Room r = selectedRooms.get(x);
            total+=r.costPerDay.get();
            if(r.getSelectedBed().equals("yes")){
                total+=r.getCostExtraBedPerDay();
            }
        }
        toUpdate.setText("$"+total+".00");
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
                    updateTotalCost(selected_rooms, total_cost);
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
        TableColumn selected;
        if(table == checked_rooms_table){
            selected = new TableColumn("Extra Bed");
            selected.setMinWidth(100);
        }
        else if(table == all_reserved_rooms_table){
            selected = new TableColumn("Extra Bed");
            selected.setMinWidth(100);
        }
        else{
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
                new PropertyValueFactory<Room,Integer>("costExtraBedPerDay")
        );
        if(table==(all_reserved_rooms_table)){
            selected.setCellValueFactory(
                    new PropertyValueFactory<Room,Boolean>("selectedBed")
            );
        }
        if(table==(checked_rooms_table)){
            selected.setCellValueFactory(
                    new PropertyValueFactory<Room,Boolean>("selectedBed")
            );
        }
        else{
            selected.setCellValueFactory(
                    new PropertyValueFactory<Room,Boolean>("selected")
            );
        }

        table.getColumns().addAll(roomNumber, roomType, maxPeople,costPerDay, costExtraBed, selected);

    }

    private void add_to_table(ObservableList<Room> rooms, TableView table){
        table.setItems(null);
        table.setItems(rooms);

    }
}

