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
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import screensframework.ControlledScreen;
import screensframework.Main;
import screensframework.Room;
import screensframework.ScreensController;

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

    private boolean already_created_all_rooms = false;
    private boolean already_created_checked_rooms = false;
    private boolean already_created_reserved_rooms = false;

    //for searching all rooms
    @FXML private TextField start_date;
    @FXML private TextField end_date;

    //for updating reservation
    @FXML private TextField reservation_id;
    @FXML private TextField current_start_date;
    @FXML private TextField current_end_date;
    @FXML private TextField new_start_date;
    @FXML private TextField new_end_date;
    @FXML private Text updated_cost;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        create_table(all_reserved_rooms_table);
//        create_table(all_rooms_table);
//        create_table(checked_rooms_table);

        // TODO
    }

    //removes the reservation from the database
    @FXML
    public void cancelReservation(ActionEvent event){

    }

    //finds if there is a reservation by the ID given and displays the rooms in the table as well as the original dates
    @FXML
    public void searchReservation(ActionEvent event){
        String reservationId = reservation_id.getText().toString();
        //find reservations by that id
        if(!already_created_reserved_rooms){
            create_table(all_reserved_rooms_table);
            createTableListener();
            already_created_reserved_rooms = true;
        }
        add_to_table(all_rooms, all_reserved_rooms_table);
    }

    //searches if the rooms have availability in the dates specified. Returns no rooms if they dont
    @FXML
    public void searchAvailability(ActionEvent event){

    }

    @FXML
    private void back (ActionEvent event){
        myController.setScreen(Main.CUSTOMER_HOME_SCREEN);
    }


    //updates the reservation in the databse based on what dates were selected
    @FXML
    public void update_reservation(ActionEvent event){

    }

    @FXML
    //should display the rooms it finds from the database
    public void search_all_rooms(ActionEvent event){
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

