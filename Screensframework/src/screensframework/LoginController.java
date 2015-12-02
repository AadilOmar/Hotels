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

package screensframework;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import screensframework.com.entities.Customer;
import screensframework.com.util.ConnectionConfiguration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Aadil
 */
public class LoginController implements Initializable, ControlledScreen {

    String[] usernameArray = {"user1", "user2", "user3"};
    String[] passwordArray = {"pass1", "pass2", "pass3"};
    int numUsers = 3;
    @FXML private Text errorText;
    @FXML private TextField username;
    @FXML private PasswordField password;
    boolean match = false;
    ScreensController myController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }


    @FXML
    protected void to_register_screen(ActionEvent event) {
        myController.setScreen(Main.REGISTER_SCREEN);
    }

    @FXML
    protected void login(ActionEvent event) {
        Customer c1 = new Customer();
        Customer c2 = new Customer();

        Connection connection = null;

        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        ResultSet resultSet1 = null;
        ResultSet resultSet2 = null;

        try {
            connection = ConnectionConfiguration.getConnection();

            //query to check customer table
            preparedStatement1 = connection.prepareStatement("SELECT * FROM CUSTOMER WHERE Cnnnn = ? and Password = ?");
            preparedStatement1.setString(1, username.getText());
            preparedStatement1.setString(2, password.getText());
            resultSet1 = preparedStatement1.executeQuery();

            while (resultSet1.next()) {
                c1.setUsername(resultSet1.getString("Cnnnn"));
                c1.setEmail(resultSet1.getString("Email"));
                c1.setPassword(resultSet1.getString("Password"));
            }


            //query to check management table
            preparedStatement2 = connection.prepareStatement("SELECT * FROM MANAGEMENT WHERE Mnnnn = ? and Password = ?");
            preparedStatement2.setString(1, username.getText());
            preparedStatement2.setString(2, password.getText());
            resultSet2 = preparedStatement2.executeQuery();

            while (resultSet2.next()) {
                c2.setUsername(resultSet2.getString("Mnnnn"));
                c2.setPassword(resultSet2.getString("Password"));

            }

        } catch (Exception e) {
            System.out.println("Connection FAILED");
            e.printStackTrace();

        } finally {
            if (resultSet1 != null) {
                try {
                    resultSet1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (resultSet2 != null) {
                try {
                    resultSet2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (preparedStatement1 != null) {
                try {
                    preparedStatement1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (preparedStatement2 != null) {
                try {
                    preparedStatement2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        //the login credentials were found in the database
        if (c1.getUsername() != null && c1.getEmail() != null && c1.getPassword() != null) {
            //go to next Customer screen.
            errorText.setText("good");
            Global.username = c1.getUsername();
            myController.setScreen(Main.CUSTOMER_HOME_SCREEN);
        } else if (c2.getUsername() != null && c2.getPassword() != null) {
            //go to next Manager screen
            Global.username = c2.getUsername();
            myController.setScreen(Main.MANAGER_HOME_SCREEN);
        }
        else {
            //show error message
            errorText.setText("Error: username or password incorrect");
        }
    }
}

