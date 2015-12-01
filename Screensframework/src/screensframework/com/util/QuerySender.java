package screensframework.com.util;

import javax.management.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by aadil on 11/30/15.
 */
public class QuerySender {

    private QuerySender(){

    }

    public static ResultSet findAllRooms(String start, String end, String loc){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String q = "CREATE VIEW RESERVED AS SELECT * FROM HAS NATURAL JOIN RESERVATION; SELECT Room_Number FROM ROOM AS R WHERE Room_Number NOT IN (SELECT ROOM_Number FROM RESERVED r WHERE (r.Start_Date <= '2015-12-16' OR (r.Start_Date >= '2015-12-16' AND r.Start_Date < '2015-12-18') ) AND (r.end_date >= '2015-12-18' OR (r.end_date > '2015-12-16' AND r.end_date <= '2015-12-18') ) AND r.Hotel_Location='Charlotte' AND r.IS_Cancelled =  '0' ) AND Hotel_Location='Charlotte'";
        String query = "CREATE VIEW RESERVED AS SELECT * FROM HAS NATURAL JOIN RESERVATION; SELECT Room_Number FROM ROOM AS R WHERE Room_Number NOT IN (SELECT ROOM_Number FROM RESERVED r WHERE (r.Start_Date <= ? OR (r.Start_Date >= ? AND r.Start_Date < ?) ) AND (r.end_date >= ? OR (r.end_date > ? AND r.end_date <= ?) ) AND r.Hotel_Location=? AND r.IS_Cancelled =  '0' ) AND Hotel_Location=?";
        System.out.println(query);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, start);
            preparedStatement.setString(2, start);
            preparedStatement.setString(3, end);
            preparedStatement.setString(4, end);
            preparedStatement.setString(5, start);
            preparedStatement.setString(6, end);
            preparedStatement.setString(7, loc);
            preparedStatement.setString(8, loc);
            resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet.toString());
            return resultSet;

        }catch (Exception e) {
            System.out.println("Connection FAILED");
            e.printStackTrace();

        } finally {
            System.out.println("asdfasdf");
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
        return null;
    }

    public static ResultSet makeReservationCreateReservation(String username, String reservation_id, String card_number, String start, String end, String total_cost, String isCancelled) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String q = "INSERT INTO `cs4400_Group_12`.`RESERVATION` (`Username`, `Reservation_ID`, `Card_Number`, `Start_Date`, `End_Date`, `Total_Cost`, `Is_Cancelled`) VALUES (\"c1001\", \"1\", \"1\", \"2015-12-04\", \"2015-12-05\", \"260\", \"1\");";
        String query = "INSERT INTO `cs4400_Group_12`.`RESERVATION` (`Username`, `Reservation_ID`, `Card_Number`, `Start_Date`, `End_Date`, `Total_Cost`, `Is_Cancelled`) VALUES (?, ?, ?, ?, ?, ?, ?);";
        System.out.println(query);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, reservation_id);
            preparedStatement.setString(3, card_number);
            preparedStatement.setString(4, start);
            preparedStatement.setString(5, end);
            preparedStatement.setString(6, total_cost);
            preparedStatement.setString(7, isCancelled);
            resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet.toString());
            return resultSet;

        }catch (Exception e) {
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
        return null;
    }

    public static ResultSet makeReservationCreateHas(String reservation_id, String[] room_numbers, String loc, String include_ex_bed){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String q = "INSERT INTO `cs4400_Group_12`.`HAS` (`Reservation_ID`, `Room_Number`, `Hotel_Location`, `Include_Extra_Bed`) VALUES (\"28\", \"103\", \"Orlando\", \"0\");";
        String query = "INSERT INTO `cs4400_Group_12`.`HAS` (`Reservation_ID`, `Room_Number`, `Hotel_Location`, `Include_Extra_Bed`) VALUES (?, ?, ?, ?);";
        System.out.println(query);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, reservation_id);
            preparedStatement.setString(2, room_numbers[0]);
            preparedStatement.setString(3, loc);
            preparedStatement.setString(4, include_ex_bed);
            resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet.toString());
            return resultSet;

        }catch (Exception e) {
            System.out.println("Connection FAILED");
            e.printStackTrace();

        } finally {
            System.out.println("asdfasdf");
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
        return null;
    }
}
