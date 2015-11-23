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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
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

    ScreensController myController;
    @FXML private TableView all_rooms_table;
    @FXML private TableView checked_rooms_table;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    //should display the rooms it finds from the database
    public void search(ActionEvent event){
        Room r1 = new Room("01", "standard", 1, 80, 15);
        Room r2 = new Room("02", "family", 4, 130, 30);
        Room r3 = new Room("03", "suite", 5, 160, 30);
        int num_rooms = 3;
        myController.setScreen(Main.VIEW_ALL_ROOMS_SCREEN);
    }

    @FXML
    //submits the reservations
    public void submit(ActionEvent event){
        Room r1 = new Room("01", "standard", 1, 80, 15);
        Room r2 = new Room("02", "family", 4, 130, 30);
        Room r3 = new Room("03", "suite", 5, 160, 30);
        int num_rooms = 3;
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
    }

    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }


}
