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

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import screensframework.reservations.ReservationController;

/**
 *
 * @author Aadil
 */
public class Main extends Application {

    private static ReservationController controller;
    public static String LOGIN_SCREEN = "login";
    public static String LOGIN_SCREEN_LAYOUT = "login.fxml";
    public static String REGISTER_SCREEN = "register";
    public static String REGISTER_SCREEN_LAYOUT = "register.fxml";
    public static String CUSTOMER_HOME_SCREEN = "customer_home";
    public static String CUSTOMER_HOME_SCREEN_LAYOUT = "customer_home.fxml";
    public static String MANAGER_HOME_SCREEN = "manager_home";
    public static String MANAGER_HOME_SCREEN_LAYOUT = "manager_home.fxml";
    public static String SEARCH_ROOMS_SCREEN = "reservations/search_rooms";
    public static String SEARCH_ROOMS_SCREEN_LAYOUT = "reservations/search_rooms.fxml";
    public static String VIEW_ALL_ROOMS_SCREEN = "reservations/view_rooms";
    public static String VIEW_ALL_ROOMS_SCREEN_LAYOUT = "reservations/view_rooms.fxml";
    public static String VIEW_CHECKED_ROOMS_SCREEN = "reservations/view_checked_rooms";
    public static String VIEW_CHECKED_ROOMS_SCREEN_LAYOUT = "reservations/view_checked_rooms.fxml";
    public static String ADD_CARD_SCREEN = "payments/card_info";
    public static String ADD_CARD_SCREEN_LAYOUT = "payments/card_info.fxml";
    public static String RESERVATION_CONFIRM_SCREEN = "reservations/confirmation";
    public static String RESERVATION_CONFIRM_SCREEN_LAYOUT = "reservations/confirmation.fxml";

    public static ReservationController getReservationController(){
        return controller;
    }

    @Override
    public void start(Stage primaryStage) {
        controller = new ReservationController();
        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(Main.LOGIN_SCREEN, Main.LOGIN_SCREEN_LAYOUT);
        mainContainer.loadScreen(Main.REGISTER_SCREEN, Main.REGISTER_SCREEN_LAYOUT);
        mainContainer.loadScreen(Main.CUSTOMER_HOME_SCREEN, Main.CUSTOMER_HOME_SCREEN_LAYOUT);
        mainContainer.loadScreen(Main.MANAGER_HOME_SCREEN, Main.MANAGER_HOME_SCREEN_LAYOUT);
        mainContainer.loadScreen(Main.SEARCH_ROOMS_SCREEN, Main.SEARCH_ROOMS_SCREEN_LAYOUT);
        mainContainer.loadScreen(Main.VIEW_ALL_ROOMS_SCREEN, Main.VIEW_ALL_ROOMS_SCREEN_LAYOUT);
        mainContainer.loadScreen(Main.VIEW_CHECKED_ROOMS_SCREEN, Main.VIEW_CHECKED_ROOMS_SCREEN_LAYOUT);
        mainContainer.loadScreen(Main.ADD_CARD_SCREEN, Main.ADD_CARD_SCREEN_LAYOUT);
        mainContainer.loadScreen(Main.RESERVATION_CONFIRM_SCREEN, Main.RESERVATION_CONFIRM_SCREEN_LAYOUT);

        mainContainer.setScreen(Main.LOGIN_SCREEN);
        
        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
