package screensframework;

import screensframework.com.entities.Customer;
import screensframework.com.util.ConnectionConfiguration;

import java.sql.*;

/**
 * Created by clint_000 on 11/30/2015.
 */
public class HotelApp {

    public static void main(String[] args) {
        Customer customer = new Customer();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM `CUSTOMER` WHERE Cnnnn = ? and Password = ?");
            preparedStatement.setString(1, "c1015");
            preparedStatement.setString(2, "password15");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                customer.setUsername(resultSet.getString("Cnnnn"));
                customer.setEmail(resultSet.getString("Email"));
                customer.setPassword(resultSet.getString("Password"));
            }

        } catch (Exception e) {
            System.out.println("Connection FAILED");
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
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

        System.out.println("username: " + customer.getUsername());
        System.out.println("email: " + customer.getEmail());
        System.out.println("password: " + customer.getPassword());
    }
}
