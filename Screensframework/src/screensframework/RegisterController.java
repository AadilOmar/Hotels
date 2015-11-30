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
import java.util.ResourceBundle;
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
public class RegisterController implements Initializable , ControlledScreen {

    String[] emailArray = {"user1@gmail.com", "user2@gmail.com", "user3@gmail.com"};
    String[] passwordArray = {"pass1", "pass2", "pass3"};
    int numUsers = 3;
    @FXML private Text errorText;
    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private PasswordField confirm_password;
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
    private void register(ActionEvent event){
        boolean usernameIsUnique = true;
        boolean passwordsMatch = false;
        if(password.equals(confirm_password)){
            passwordsMatch = true ;
        }
        for(int x=0;x<numUsers;x++){
            if(email.getText().equals(emailArray[x])){
                usernameIsUnique = false;
            }
        }
        if(usernameIsUnique && passwordsMatch){
            //put new user in database
            if(Global.user_type.equals("customer")){
                myController.setScreen(Main.CUSTOMER_HOME_SCREEN);
            }
            else{
                myController.setScreen(Main.MANAGER_HOME_SCREEN);
            }
        }
        else if(!usernameIsUnique){
            errorText.setText("Error: username is already taken");
        }
        else if(!passwordsMatch){
            errorText.setText("Error: passwords don't match");

        }
    }

    @FXML
    private void to_login_screen (ActionEvent event){
        myController.setScreen(Main.LOGIN_SCREEN);
    }
}
