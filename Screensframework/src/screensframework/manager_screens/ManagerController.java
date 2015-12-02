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
package screensframework.manager_screens;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import screensframework.ControlledScreen;
import screensframework.Global;
import screensframework.Main;
import screensframework.ScreensController;
import screensframework.com.util.QuerySender;

/**
 * FXML Controller class
 *
 * @author Aadil
 */
public class ManagerController implements Initializable, ControlledScreen {

    ScreensController myController;

    @FXML private TableView all_pop_category_table;
    @FXML private TableView all_reservations_table;
    @FXML private TableView all_revenue_table;
    @FXML private Text plok;
    @FXML private Button mand;

    ObservableList<RoomCategoryItem> pop_category_items =FXCollections.observableArrayList();
    ObservableList<ReservationReportItem> reservation_report_items =FXCollections.observableArrayList();
    ObservableList<RevenueItem> revenue_items =FXCollections.observableArrayList(
            new RevenueItem("jan", "ATL", "134200"),
            new RevenueItem("feb", "CHA", "11200"),
            new RevenueItem("mar", "MAR", "45100")
    );
    Scene current;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }



    @FXML
    protected void initialize() {

    }

    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    @FXML
    private void view_reservation_report (ActionEvent event){
        reservation_report_items = FXCollections.observableArrayList();

        myController.setScreen(Main.RESERVATION_REPORT_SCREEN);
        GridPane pane = new GridPane();
        Text header = new Text();
        header.setText("RESERVATION REPORT");
        pane.add(header, 0, 0);
        TableView table = new TableView();
        pane.add(table, 0, 2);
        Button b = new Button();
        b.setText("back");
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Global.primaryStage.setScene(current);
                myController.setScreen(Main.MANAGER_HOME_SCREEN);
            }
        });
        pane.add(b, 0, 4);

        ResultSet augResultSet = QuerySender.getAugustReservationSet();

        try {
            while (augResultSet.next()) {
                String location = augResultSet.getString("Hotel_Location");
                String numReservations = augResultSet.getString("COUNT(Reservation_ID)");
                ReservationReportItem r = new ReservationReportItem("August", location, numReservations);
                reservation_report_items.add(r);
            }
            augResultSet.close();
        } catch (Exception e) {
            System.out.println("August query failed");
            e.printStackTrace();
        }

        ResultSet septResultSet = QuerySender.getSeptemberReservationSet();

        try {
            while (septResultSet.next()) {
                String location = septResultSet.getString("Hotel_Location");
                String numReservations = septResultSet.getString("COUNT(Reservation_ID)");
                ReservationReportItem r = new ReservationReportItem("September", location, numReservations);
                reservation_report_items.add(r);
            }
            septResultSet.close();
        } catch (Exception e) {
            System.out.println("September query failed");
            e.printStackTrace();
        }

        create_reservation_report_table(table);
        add_to_table(reservation_report_items,table);
        StackPane root = new StackPane();
        root.getChildren().add(pane);
        current = Global.primaryStage.getScene();
        Global.primaryStage.setScene(new Scene(root));
        Global.primaryStage.show();
    }

    @FXML
    private void view_pop_room_category_report (ActionEvent event){
        pop_category_items = FXCollections.observableArrayList();

        GridPane pane = new GridPane();
        Text header = new Text();
        header.setText("POPULAR ROOM CATEGORIES REPORT");
        pane.add(header, 0, 0);
        TableView table = new TableView();
        pane.add(table, 0, 2);
        Button b = new Button();
        b.setText("back");
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Global.primaryStage.setScene(current);
                myController.setScreen(Main.MANAGER_HOME_SCREEN);
            }
        });
        pane.add(b, 0, 4);

        ResultSet resultSet = QuerySender.getPopularCategorySet();

        try {
            while (resultSet.next()) {
                String location = resultSet.getString("Hotel_Location");
                String roomCategory = resultSet.getString("Room_Category");
                String numReservationsForRoom = resultSet.getString("COUNT");
                RoomCategoryItem r = new RoomCategoryItem("August", location, roomCategory, numReservationsForRoom);
                pop_category_items.add(r);
            }
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        create_room_category_table(table);
        add_to_table(pop_category_items,table);
        StackPane root = new StackPane();
        root.getChildren().add(pane);
        current = Global.primaryStage.getScene();
        Global.primaryStage.setScene(new Scene(root));
        Global.primaryStage.show();
    }

    @FXML
    private void view_revenue_report (ActionEvent event){
        revenue_items = FXCollections.observableArrayList();

        myController.setScreen(Main.REVENUE_REPORT_SCREEN);
        GridPane pane = new GridPane();
        Text header = new Text();
        header.setText("REVENUE REPORT");
        pane.add(header, 0, 0);
        TableView table = new TableView();
        pane.add(table, 0, 2);
        Button b = new Button();
        b.setText("back");
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Global.primaryStage.setScene(current);
                myController.setScreen(Main.MANAGER_HOME_SCREEN);
            }
        });
        pane.add(b, 0, 4);

        ResultSet augResultSet = QuerySender.getAugustRevenueSet();

        try {
            while (augResultSet.next()) {
                String location = augResultSet.getString("Hotel_Location");
                String totalCost = augResultSet.getString("SUM(Total_Cost)");
                RevenueItem r = new RevenueItem("August", location, totalCost);
                revenue_items.add(r);
            }
            augResultSet.close();
        } catch (Exception e) {
            System.out.println("August query failed");
            e.printStackTrace();
        }

        ResultSet septResultSet = QuerySender.getSeptemberRevenueSet();

        try {
            while (septResultSet.next()) {
                String location = septResultSet.getString("Hotel_Location");
                String totalCost = septResultSet.getString("SUM(Total_Cost)");
                RevenueItem r = new RevenueItem("September", location, totalCost);
                revenue_items.add(r);
            }
            augResultSet.close();
        } catch (Exception e) {
            System.out.println("September query failed");
            e.printStackTrace();
        }

        create_revenue_report_table(table);
        add_to_table(revenue_items,table);
        StackPane root = new StackPane();
        root.getChildren().add(pane);
        current = Global.primaryStage.getScene();
        Global.primaryStage.setScene(new Scene(root));
        Global.primaryStage.show();
    }


    @FXML
    public void back (ActionEvent event){

        System.out.println(mand.getText());
        myController.setScreen(Main.MANAGER_HOME_SCREEN);
    }

    public class ReservationReportItem{
        private SimpleStringProperty month;
        private SimpleStringProperty location;
        private SimpleStringProperty totalReservations;
        public ReservationReportItem(String month, String location, String total_reservations){
            this.month = new SimpleStringProperty(month);
            this.location = new SimpleStringProperty(location);
            this.totalReservations = new SimpleStringProperty(total_reservations);
        }
        public String getMonth() {return this.month.get();}
        public String getLocation() {return this.location.get();}
        public String getTotalReservations() {return this.totalReservations.get();}

    }
    public class RoomCategoryItem{
        private SimpleStringProperty month;
        private SimpleStringProperty location;
        private SimpleStringProperty topCategory;
        private SimpleStringProperty totalReservations;
        public RoomCategoryItem(String month, String location, String top_Category, String total_reservations){
            this.month = new SimpleStringProperty(month);
            this.location = new SimpleStringProperty(location);
            this.topCategory = new SimpleStringProperty(top_Category);
            this.totalReservations = new SimpleStringProperty(total_reservations);
        }
        public String getMonth() {return this.month.get();}
        public String getLocation() {return this.location.get();}
        public String getTopCategory() {return this.topCategory.get();}
        public String getTotalReservations() {return this.totalReservations.get();}
    }
    public class RevenueItem{
        private SimpleStringProperty month;
        private SimpleStringProperty location;
        private SimpleStringProperty totalRevenue;
        private RevenueItem(String month, String location, String total_revenue){
            this.month = new SimpleStringProperty(month);
            this.location = new SimpleStringProperty(location);
            this.totalRevenue = new SimpleStringProperty(total_revenue);
        }
        public String getMonth() {return this.month.get();}
        public String getLocation() {return this.month.get();}
        public String getTotalRevenue() {return this.totalRevenue.get();}
    }


    public void create_room_category_table(TableView table) {
        System.out.println("ASDFASDF");
        TableColumn month = new TableColumn("Month");
        month.setMinWidth(100);
        TableColumn topCategory = new TableColumn("Top Room Category");
        topCategory.setMinWidth(100);
        TableColumn location = new TableColumn("Location");
        location.setMinWidth(100);
        TableColumn totalReservations = new TableColumn("Total Reservations");
        totalReservations.setMinWidth(100);

        month.setCellValueFactory(new PropertyValueFactory<RoomCategoryItem,String>("month"));
        topCategory.setCellValueFactory(new PropertyValueFactory<RoomCategoryItem,String>("topCategory"));
        location.setCellValueFactory(new PropertyValueFactory<RoomCategoryItem,String>("location"));
        totalReservations.setCellValueFactory(new PropertyValueFactory<RoomCategoryItem,String>("totalReservations"));
        table.getColumns().addAll(month, topCategory,location,totalReservations);

    }

    private void create_revenue_report_table(TableView table) {
        TableColumn month = new TableColumn("Month");
        month.setMinWidth(100);
        TableColumn location = new TableColumn("Location");
        location.setMinWidth(100);
        TableColumn totalRevenue = new TableColumn("Total Revenue");
        totalRevenue.setMinWidth(100);

        month.setCellValueFactory(new PropertyValueFactory<RevenueItem,String>("month"));
        location.setCellValueFactory(new PropertyValueFactory<RevenueItem,String>("location"));
        totalRevenue.setCellValueFactory(new PropertyValueFactory<RevenueItem,String>("totalRevenue"));
        table.getColumns().addAll(month,location,totalRevenue);
    }

    private void create_reservation_report_table(TableView table){
        TableColumn month = new TableColumn("Month");
        month.setMinWidth(100);
        TableColumn location = new TableColumn("Location");
        location.setMinWidth(100);
        TableColumn totalReservations = new TableColumn("Total Reservations");
        totalReservations.setMinWidth(100);
        month.setCellValueFactory(new PropertyValueFactory<ReservationReportItem,String>("month"));
        location.setCellValueFactory(new PropertyValueFactory<ReservationReportItem,String>("location"));
        totalReservations.setCellValueFactory(new PropertyValueFactory<ReservationReportItem,String>("totalReservations"));
        table.getColumns().addAll(month,location,totalReservations);


    }

    private void add_to_table(ObservableList items, TableView table){
        table.setItems(null);
        table.setItems(items);
    }
}
