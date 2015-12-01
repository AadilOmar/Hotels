package screensframework.com.util;

import com.sun.corba.se.spi.orbutil.fsm.Guard;
import com.sun.org.apache.bcel.internal.ExceptionConstants;
import screensframework.Global;

import javax.management.Query;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        String q = "CREATE VIEW RESERVED AS SELECT * FROM HAS NATURAL JOIN RESERVATION; SELECT * FROM ROOM AS R WHERE Room_Number NOT IN (SELECT ROOM_Number FROM RESERVED r WHERE (r.Start_Date <= '2015-12-16' OR (r.Start_Date >= '2015-12-16' AND r.Start_Date < '2015-12-18') ) AND (r.end_date >= '2015-12-18' OR (r.end_date > '2015-12-16' AND r.end_date <= '2015-12-18') ) AND r.Hotel_Location='Charlotte' AND r.IS_Cancelled =  '0' ) AND Hotel_Location='Charlotte'";
        String query = "SELECT * FROM ROOM WHERE Room_Number NOT IN (SELECT ROOM_Number FROM RESERVED r WHERE (r.Start_Date <= ? OR (r.Start_Date >= ? AND r.Start_Date < ?) ) AND (r.end_date >= ? OR (r.end_date > ? AND r.end_date <= ?) ) AND r.Hotel_Location=? AND r.IS_Cancelled = ? ) AND Hotel_Location=?";
        String query1 = "CREATE VIEW RESERVED AS SELECT * FROM HAS NATURAL JOIN RESERVATION;";
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
            preparedStatement.setString(8, "0");
            preparedStatement.setString(9, loc);
            resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet);
            return resultSet;

        }catch (Exception e) {
            System.out.println("Connection FAILED");
            e.printStackTrace();

        }
        return resultSet;
    }


    public static int addCreditCard(String username, String card_number, String card_name, String ccv, String exp_date){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;
        String q = "INSERT INTO `cs4400_Group_12`.`PAYMENT_INFORMATION` (`Username`, `Card_Number`, `Name`, `CCV`, `Expiration_Date`) VALUES ('c1001', '40asdf', 'carafddx', '4dfs', '2015-11-30');";
        String query = "INSERT INTO `cs4400_Group_12`.`PAYMENT_INFORMATION` (`Username`, `Card_Number`, `Name`, `CCV`, `Expiration_Date`) VALUES (?, ?, ?, ?, ?)";

        System.out.println(query);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            System.out.println("_____"+username);
            preparedStatement.setString(2, card_number);
            System.out.println("_____"+card_number);
            preparedStatement.setString(3, card_name);
            System.out.println("_____"+card_name);
            preparedStatement.setInt(4, Integer.parseInt(ccv));
            System.out.println("_____"+ccv);
            preparedStatement.setString(5, exp_date+"-01");
            System.out.println("_____"+exp_date+"-01");
            result = preparedStatement.executeUpdate();
            System.out.println(result+" RESULT");
            return result;

        }catch (Exception e) {
            System.out.println("FAILURE");
            e.printStackTrace();

        } finally {
            System.out.println("asdfasdf");
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
        return result;
    }

    //fail
    public static ResultSet getCreditCards() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT Card_Number FROM PAYMENT_INFORMATION LEFT JOIN CUSTOMER ON PAYMENT_INFORMATION.Username = CUSTOMER.Cnnnn  WHERE Username = ?;";

        System.out.println(query);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement =  connection.prepareStatement(query);
            preparedStatement.setString(1, Global.username);
            result = preparedStatement.executeQuery();
            System.out.println(result.findColumn("Card_Number"));
            return result;
        } catch (Exception e) {
            System.out.println("FAILURE");
            e.printStackTrace();

        } finally {
            System.out.println("asdfasdf");
            if (result != null) {
                try {
                    result.close();
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
        return result;
    }

    public static int deleteCreditCard(String username, String card_number){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;
        String q = "DELETE FROM `cs4400_Group_12`.`PAYMENT_INFORMATION` WHERE (Username = 'c1001' AND Card_Number = '40')";
        String query = "DELETE FROM `cs4400_Group_12`.`PAYMENT_INFORMATION` WHERE (Username = ? AND Card_Number = ?)";

        System.out.println(query);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, Global.username);
            preparedStatement.setString(1, card_number);
            result = preparedStatement.executeUpdate();
        }catch (Exception e) {
            System.out.println("FAILURE");
            e.printStackTrace();

        } finally {
            System.out.println("asdfasdf");
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
        return result;
    }

    //fail
    public static int makeReservationReservationTable(String username, String reservation_id, String card_number, String start, String end, String total_cost, String isCancelled){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;
        String q = "INSERT INTO `cs4400_Group_12`.`RESERVATION` (`Username`, `Reservation_ID`, `Card_Number`, `Start_Date`, `End_Date`, `Total_Cost`, `Is_Cancelled`) VALUES (\"c1001\", \"1\", \"1\", \"2015-12-04\", \"2015-12-05\", \"260\", \"1\");\n";
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
            result = preparedStatement.executeUpdate();
        }catch (Exception e) {
            System.out.println("FAILURE");
            e.printStackTrace();

        } finally {
            System.out.println("asdfasdf");
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
        return result;
    }

    //fail
    public static int makeReservationHasTable(String reservation_id, String room_number, String loc, String include_ex_bed){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int resultSet = 0;
        String q = "INSERT INTO `cs4400_Group_12`.`HAS` (`Reservation_ID`, `Room_Number`, `Hotel_Location`, `Include_Extra_Bed`) VALUES (\"28\", \"103\", \"Orlando\", \"0\");";
        String query = "INSERT INTO `cs4400_Group_12`.`HAS` (`Reservation_ID`, `Room_Number`, `Hotel_Location`, `Include_Extra_Bed`) VALUES (?, ?, ?, ?);";
        System.out.println(query);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, reservation_id);
            preparedStatement.setString(2, room_number);
            preparedStatement.setString(3, loc);
            preparedStatement.setString(4, include_ex_bed);
            resultSet = preparedStatement.executeUpdate();
            return resultSet;

        }catch (Exception e) {
            System.out.println("FAILURE");
            e.printStackTrace();

        } finally {
            System.out.println("asdfasdf");
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
        return 0;
    }

    public static ResultSet getRoomsOfReservation(String reservation_id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String q = "SELECT * FROM  `cs4400_Group_12`.`RESERVED` WHERE Reservation_ID = \"26\"\n";
        String query = "SELECT * FROM  `cs4400_Group_12`.`RESERVED` WHERE Reservation_ID = ?";
        System.out.println(query);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, reservation_id);
            result = preparedStatement.executeQuery();
            return result;

        }catch (Exception e) {
            System.out.println("FAILURE");
            e.printStackTrace();

        }
        return null;
    }


    public static ResultSet searchAvailability(String reservation_id, String start, String end){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String q = "SELECT * From URESERVED NATURAL JOIN ROOM WHERE Reservation_ID = '26' AND Room_Number In ( SELECT Room_Number FROM ROOM AS R WHERE Room_Number NOT IN ( SELECT ROOM_Number FROM URESERVED r WHERE (r.Start_Date <= '2015-11-06' OR (r.Start_Date >= '2015-11-06' AND r.Start_Date < '2015-11-07') ) AND (r.end_date >= '2015-11-07' OR (r.end_date > '2015-11-06' AND r.end_date <= '2015-11-07') ) AND r.IS_Cancelled = '0' ) )";
        String query = "SELECT * From URESERVED NATURAL JOIN ROOM WHERE Reservation_ID = ? AND Room_Number In ( SELECT Room_Number FROM ROOM AS R WHERE Room_Number NOT IN ( SELECT ROOM_Number FROM URESERVED r WHERE (r.Start_Date <= ? OR (r.Start_Date >= ? AND r.Start_Date < ?) ) AND (r.end_date >= ? OR (r.end_date > ? AND r.end_date <= ?) ) AND r.IS_Cancelled = ? ) )";
        System.out.println(query);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, reservation_id);
            preparedStatement.setString(2, start);
            preparedStatement.setString(3, start);
            preparedStatement.setString(4, end);
            preparedStatement.setString(5, end);
            preparedStatement.setString(6, start);
            preparedStatement.setString(7, end);
            preparedStatement.setString(8, "0");
            result = preparedStatement.executeQuery();
            return result;

        }catch (Exception e) {
            System.out.println("FAILURE");
            e.printStackTrace();

        }
        return null;
    }
}
