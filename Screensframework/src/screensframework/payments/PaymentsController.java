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
package screensframework.payments;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import screensframework.*;
import screensframework.com.util.QuerySender;

/**
 * FXML Controller class
 *
 * @author Aadil
 */
public class PaymentsController implements Initializable, ControlledScreen {

    ScreensController myController;
    @FXML private Text error_card_detail;
    @FXML private TextField exp_date;
    @FXML private TextField card_number;
    @FXML private TextField card_name;
    @FXML private TextField cvv;
    @FXML private SplitMenuButton all_cards;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void update_cards(ActionEvent event) {
        System.out.println("UPADTING");
        if(all_cards.getItems()!=null) {
            all_cards.getItems().removeAll();
        }

        ArrayList<MenuItem> list = new ArrayList<MenuItem>();
        for(int x=0;x<Global.cards.size();x++){
            System.out.println("A CARD");
            MenuItem i = new MenuItem(Global.cards.get(x));
            i.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    all_cards.setText(((MenuItem)actionEvent.getSource()).getText());
                }
            });
            list.add(i);
        }
        all_cards.getItems().removeAll();
        all_cards.getItems().setAll(list);
        all_cards.setVisible(false);
        all_cards.setVisible(true);
    }


    @FXML
    //saves the card info
    public void save(ActionEvent event) {
        boolean card_fields_are_valid = Validator.validate_adding_card(card_name, card_number, exp_date, cvv, error_card_detail);
        if(!card_fields_are_valid){
            return;
        }

        int result = QuerySender.addCreditCard(Global.username, card_number.getText(), card_name.getText(), cvv.getText(), exp_date.getText());
        if(result == 1) {
            Global.cardItems.add(new MenuItem(card_number.getText()));
            Global.cards.add(card_number.getText());
        }

        System.out.println("ASDFASDFADSF "+result);
        myController.setScreen(Main.VIEW_CHECKED_ROOMS_SCREEN);
    }

    @FXML
    //deletes the card
    public void delete(ActionEvent event) {

        int result = QuerySender.deleteCreditCard(Global.username, all_cards.getText());
        if(result == 1){
            int index_to_delete = 0;
            for(int x=0;x<Global.cards.size();x++){
                if(Global.cards.get(x).equals("all_cards.getText()")){
                    index_to_delete = x;
                }
            }
            Global.cardItems.remove(index_to_delete);
            Global.cards.remove(index_to_delete);
        }
        myController.setScreen(Main.CUSTOMER_HOME_SCREEN);
    }

    @FXML
    //saves the card info
    public void back(ActionEvent event) {
        myController.setScreen(Main.VIEW_CHECKED_ROOMS_SCREEN);
    }

    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }


}
