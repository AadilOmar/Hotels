package screensframework.com.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by clint_000 on 11/30/2015.
 */
public class ConnectionConfiguration {

    public static Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_12", "cs4400_Group_12", "HfxkoFoX");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}
