package screensframework.com.util;

import com.sun.corba.se.spi.orbutil.fsm.Guard;
import com.sun.org.apache.bcel.internal.ExceptionConstants;
import screensframework.Global;

import javax.management.Query;
import java.security.spec.ECField;
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
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        ResultSet resultSet = null;

        String query1 = "CREATE VIEW RRESERVED AS SELECT * FROM HAS NATURAL JOIN RESERVATION";

        String query2 = "SELECT * FROM ROOM WHERE Room_Number NOT IN (SELECT ROOM_Number FROM RRESERVED r WHERE (r.Start_Date <= ? OR (r.Start_Date >= ? AND r.Start_Date < ?) ) AND (r.end_date >= ? OR (r.end_date > ? AND r.end_date <= ?) ) AND r.Hotel_Location=? AND r.IS_Cancelled = ? ) AND Hotel_Location=?";

        String query3 = "DROP VIEW RRESERVED";

//        String q = "CREATE VIEW RESERVED AS SELECT * FROM HAS NATURAL JOIN RESERVATION; SELECT * FROM ROOM AS R WHERE Room_Number NOT IN (SELECT ROOM_Number FROM RESERVED r WHERE (r.Start_Date <= '2015-12-16' OR (r.Start_Date >= '2015-12-16' AND r.Start_Date < '2015-12-18') ) AND (r.end_date >= '2015-12-18' OR (r.end_date > '2015-12-16' AND r.end_date <= '2015-12-18') ) AND r.Hotel_Location='Charlotte' AND r.IS_Cancelled =  '0' ) AND Hotel_Location='Charlotte'";
//        String query1 = "CREATE VIEW RESERVED AS SELECT * FROM HAS NATURAL JOIN RESERVATION;";
        System.out.println(query2);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement3 = connection.prepareStatement(query3);
            preparedStatement2.setString(1, start);
            preparedStatement2.setString(2, start);
            preparedStatement2.setString(3, end);
            preparedStatement2.setString(4, end);
            preparedStatement2.setString(5, start);
            preparedStatement2.setString(6, end);
            preparedStatement2.setString(7, loc);
            preparedStatement2.setString(8, "0");
            preparedStatement2.setString(9, loc);
            preparedStatement1.executeUpdate();
            resultSet = preparedStatement2.executeQuery();
            preparedStatement3.executeUpdate();
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

    public static ResultSet getCreditCards(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT Card_Number FROM PAYMENT_INFORMATION LEFT JOIN CUSTOMER ON PAYMENT_INFORMATION.Username = CUSTOMER.Cnnnn  WHERE Username = ?;";
        System.out.println(query);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement =  connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            result = preparedStatement.executeQuery();
            return result;
        } catch (Exception e) {
            System.out.println("FAILURE");
            e.printStackTrace();
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
            preparedStatement.setString(2, card_number);
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
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        ResultSet result = null;
        String query1 = "CREATE VIEW RRESERVED AS SELECT * FROM HAS NATURAL JOIN RESERVATION";

        String query2 = "SELECT * FROM  `cs4400_Group_12`.`RRESERVED` WHERE Reservation_ID = ?";

        String query3 = "DROP VIEW RRESERVED";

        System.out.println(query2);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement3 = connection.prepareStatement(query3);
            preparedStatement2.setString(1, reservation_id);
            preparedStatement1.executeUpdate();
            result = preparedStatement2.executeQuery();

            preparedStatement3.executeUpdate();
            return result;

        }catch (Exception e) {
            System.out.println("FAILURE");
            e.printStackTrace();

        }
        return null;
    }

    //gets availability of rooms (for updating)
    public static ResultSet searchAvailability(String reservation_id, String start, String end){
        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        ResultSet result = null;
        String q = "SELECT * From URESERVED NATURAL JOIN ROOM WHERE Reservation_ID = '26' AND Room_Number In ( SELECT Room_Number FROM ROOM AS R WHERE Room_Number NOT IN ( SELECT ROOM_Number FROM URESERVED r WHERE (r.Start_Date <= '2015-11-06' OR (r.Start_Date >= '2015-11-06' AND r.Start_Date < '2015-11-07') ) AND (r.end_date >= '2015-11-07' OR (r.end_date > '2015-11-06' AND r.end_date <= '2015-11-07') ) AND r.IS_Cancelled = '0' ) )";
        String query = "SELECT * From URESERVED NATURAL JOIN ROOM WHERE Reservation_ID = ? AND Room_Number In ( SELECT Room_Number FROM ROOM AS R WHERE Room_Number NOT IN ( SELECT ROOM_Number FROM URESERVED r WHERE (r.Start_Date <= ? OR (r.Start_Date >= ? AND r.Start_Date < ?) ) AND (r.end_date >= ? OR (r.end_date > ? AND r.end_date <= ?) ) AND r.IS_Cancelled = ? ) )";
        String makeViewQuery = "CREATE VIEW URESERVED AS SELECT * FROM HAS NATURAL JOIN RESERVATION;";
        String dropViewQuery = "DROP VIEW URESERVED";

        System.out.println(query);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement1 = connection.prepareStatement(query);
            preparedStatement2 = connection.prepareStatement(makeViewQuery);
            preparedStatement3 = connection.prepareStatement(dropViewQuery);

            preparedStatement1.setString(1, reservation_id);
            preparedStatement1.setString(2, start);
            preparedStatement1.setString(3, start);
            preparedStatement1.setString(4, end);
            preparedStatement1.setString(5, end);
            preparedStatement1.setString(6, start);
            preparedStatement1.setString(7, end);
            preparedStatement1.setString(8, "0");
            preparedStatement2.executeUpdate();
            result = preparedStatement1.executeQuery();
            preparedStatement3.executeUpdate();
            return result;

        }catch (Exception e) {
            System.out.println("FAILURE");
            e.printStackTrace();

        }
        return null;
    }

    //still todo

    public static int updateReservation(String username, String reservation_id, String start, String end, String total_cost){
        System.out.println("Username: " + username);

        String newCost = "";
        for (int i = 0; i < total_cost.length(); i++) {
            if (Character.isDigit(total_cost.charAt(i))) {
                newCost += total_cost.charAt(i);
            } else if (Character.toString(total_cost.charAt(i)).equals(".")) {
                break;
            }
        }
        System.out.println("Cost: " +newCost);
        System.out.println("Reservation: " +reservation_id);
        System.out.println("start date: " +start);
        System.out.println("end date: " +end);


        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String query = "UPDATE RESERVATION SET Start_Date= ?, End_Date= ?, Total_Cost= ?" +
                "WHERE Reservation_ID= ? and Username= ?";

        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, start);
            preparedStatement.setString(2, end);
            preparedStatement.setString(3, newCost);
            preparedStatement.setString(4, reservation_id);
            preparedStatement.setString(5, username);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Failed update");
            e.printStackTrace();
        } finally {
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

    //gets if rooms are available to be cancelled
    public static ResultSet searchAvailabilityToCancel(String reservation_id){
        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        ResultSet result = null;
        String query1 = "CREATE VIEW RRESERVED AS SELECT * FROM HAS NATURAL JOIN RESERVATION";

        String query2 = "SELECT * From RRESERVED NATURAL JOIN ROOM WHERE Reservation_ID = ? AND Username = ? AND Is_Cancelled=? AND Start_Date > CAST(CURRENT_TIMESTAMP AS DATE)";

        String query3 = "DROP VIEW RRESERVED";
        System.out.println(query2);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement3 = connection.prepareStatement(query3);
            preparedStatement2.setString(1, reservation_id);
            preparedStatement2.setString(2, Global.username );
            preparedStatement2.setString(3, "0");
            preparedStatement1.executeUpdate();
            result = preparedStatement2.executeQuery();
            preparedStatement3.executeUpdate();
            return result;

        }catch (Exception e) {
            System.out.println("FAILURE");
            e.printStackTrace();

        }
        return null;
    }

    public static int cancelReservation(String reservation_id, String to_update){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int resultSet = 0;
        String query = "UPDATE RESERVATION SET Is_Cancelled=?, Total_Cost=? WHERE RESERVATION_ID = ? AND Username = ?";
        System.out.println(query);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "1");
            preparedStatement.setString(2, to_update);
            preparedStatement.setString(3, reservation_id);
            preparedStatement.setString(4, Global.username);
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

    public static ResultSet getNumReviews(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT MAX(Review_Number) as Max FROM REVIEW";
        System.out.println(query);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();
            return result;

        }catch (Exception e) {
            System.out.println("FAILURE");
            e.printStackTrace();

        }
        return null;
    }

    public static ResultSet getNumReservations(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT MAX(Reservation_ID) as Max FROM RESERVATION";
        System.out.println(query);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();
            return result;

        }catch (Exception e) {
            System.out.println("FAILURE");
            e.printStackTrace();

        }
        return null;
    }

    public static int createReview(String username, String review_number, String location, String rating, String comment){
        Connection connection = null;
        System.out.println("LOCATION!! "+location);
        PreparedStatement preparedStatement = null;
        int resultSet = 0;
        String query = "INSERT INTO `cs4400_Group_12`.`REVIEW` (`Username`, `Review_Number`, `Rating`, `Comment`, `Hotel_Location`) VALUES (?, ?, ?, ?, ?);";
        System.out.println(query);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, review_number);
            preparedStatement.setString(3, rating);
            preparedStatement.setString(4, comment);
            preparedStatement.setString(5, location);
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

    public static ResultSet viewReviews(String location){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT `Rating`,`Comment` FROM  `cs4400_Group_12`.`REVIEW` WHERE Hotel_Location = ?";
        System.out.println(query);
        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, location);
            result = preparedStatement.executeQuery();
            return result;

        }catch (Exception e) {
            System.out.println("FAILURE");
            e.printStackTrace();

        }
        return null;
    }

    public static ResultSet getAugustReservationSet() {
        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        ResultSet augResultSet = null;

        String augQuery = "SELECT Hotel_Location, COUNT(Reservation_ID) FROM `RESERVED` WHERE Start_Date >= \"2015-8-1\" and Start_Date <= \"2015-8-31\" GROUP BY Hotel_Location";

        String makeViewQuery = "CREATE VIEW RESERVED AS SELECT * FROM HAS NATURAL JOIN RESERVATION";

        String dropViewQuery = "DROP VIEW RESERVED";

        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement1 = connection.prepareStatement(augQuery);
            preparedStatement2 = connection.prepareStatement(makeViewQuery);
            preparedStatement3 = connection.prepareStatement(dropViewQuery);
            preparedStatement2.executeUpdate();
            augResultSet = preparedStatement1.executeQuery();
            preparedStatement3.executeUpdate(dropViewQuery);
            return augResultSet;
        } catch (Exception e) {
            System.out.println("FAILURE");
            e.printStackTrace();
        }
        return  augResultSet;
    }

    public static ResultSet getSeptemberReservationSet() {
        Connection connection = null;

        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        ResultSet septResultSet = null;

        String septQuery = "SELECT Hotel_Location, COUNT(Reservation_ID) FROM `RESERVED` WHERE Start_Date >= \"2015-9-1\" and Start_Date <= \"2015-9-30\" GROUP BY Hotel_Location";

        String makeViewQuery = "CREATE VIEW RESERVED AS SELECT * FROM HAS NATURAL JOIN RESERVATION";

        String dropViewQuery = "DROP VIEW RESERVED";

        try {
            connection = ConnectionConfiguration.getConnection();
            preparedStatement1 = connection.prepareStatement(septQuery);
            preparedStatement2 = connection.prepareStatement(makeViewQuery);
            preparedStatement3 = connection.prepareStatement(dropViewQuery);
            preparedStatement2.executeUpdate();
            septResultSet = preparedStatement1.executeQuery();
            preparedStatement3.executeUpdate(dropViewQuery);
            return septResultSet;
        } catch (Exception e) {
            System.out.println("FAILURE");
            e.printStackTrace();
        }

        return null;
    }

    public static ResultSet getPopularCategorySet() {
        Connection connection;
        PreparedStatement preparedStatement1;
        PreparedStatement preparedStatement2;
        PreparedStatement preparedStatement3;
        PreparedStatement preparedStatement4;
        PreparedStatement preparedStatement5;
        PreparedStatement preparedStatement6;
        PreparedStatement preparedStatement7;
        ResultSet resultSet;

        String query1 = "CREATE  VIEW COST_CALCULATION AS  SELECT  * \n" +
                "FROM ROOM\n" +
                "NATURAL  JOIN HAS";

        String query2 = "CREATE  VIEW DATES_COST_CALC AS  SELECT  * \n" +
                "FROM COST_CALCULATION\n" +
                "NATURAL  JOIN RESERVATION";

        String query3 = "CREATE VIEW  TEMP_POP AS\n" +
                "(SELECT Hotel_Location, Room_Category, COUNT( Reservation_ID )  AS COUNT\n" +
                "FROM  `DATES_COST_CALC` \n" +
                "WHERE Start_Date >=  \"2015-09-1\"\n" +
                "AND Start_Date <=  \"2015-09-30\"\n" +
                "GROUP  BY Hotel_Location, Room_Category)";

        String query4 = "SELECT TEMP_POP.Hotel_Location,TEMP_POP.Room_Category, TEMP_POP.Count\n" +
                "FROM (\n" +
                "   SELECT Hotel_Location, MAX( count ) AS maxcount\n" +
                "   FROM TEMP_POP\n" +
                "   GROUP BY Hotel_Location\n" +
                ") as x inner join TEMP_POP on  TEMP_POP.Hotel_Location= x.Hotel_Location and TEMP_POP.Count = x.maxcount\n";

        String query5 = "DROP VIEW TEMP_POP";

        String query6 = "DROP VIEW DATES_COST_CALC";

        String query7 = "DROP VIEW COST_CALCULATION";

        try {
            connection = ConnectionConfiguration.getConnection();

            preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement3 = connection.prepareStatement(query3);
            preparedStatement4 = connection.prepareStatement(query4);
            preparedStatement5 = connection.prepareStatement(query5);
            preparedStatement6 = connection.prepareStatement(query6);
            preparedStatement7 = connection.prepareStatement(query7);
            preparedStatement1.executeUpdate();
            preparedStatement2.executeUpdate();
            preparedStatement3.executeUpdate();
            resultSet = preparedStatement4.executeQuery();
            preparedStatement5.executeUpdate();
            preparedStatement6.executeUpdate();
            preparedStatement7.executeUpdate();
            return resultSet;

        } catch (Exception e) {
            System.out.println("FAILURE");
            e.printStackTrace();
        }

        return null;
    }

    public static ResultSet getAugustRevenueSet() {
        Connection connection;
        PreparedStatement preparedStatement1;
        PreparedStatement preparedStatement2;
        PreparedStatement preparedStatement3;
        ResultSet resultSet;

        String query1 = "CREATE VIEW RRESERVED AS SELECT * FROM HAS NATURAL JOIN RESERVATION";

        String query2 = "SELECT Hotel_Location, SUM(Total_Cost) FROM `RRESERVED` \n" +
                "WHERE Start_Date >= \"2015-08-01\" and Start_Date <= \"2015-08-31\"\n" +
                "GROUP BY Hotel_Location";

        String query3 = "DROP VIEW RRESERVED";

        try {
            connection = ConnectionConfiguration.getConnection();

            preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement3 = connection.prepareStatement(query3);
            preparedStatement1.executeUpdate();
            resultSet = preparedStatement2.executeQuery();
            preparedStatement3.executeUpdate();
            return resultSet;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ResultSet getSeptemberRevenueSet() {
        Connection connection;
        PreparedStatement preparedStatement1;
        PreparedStatement preparedStatement2;
        PreparedStatement preparedStatement3;
        ResultSet resultSet;

        String query1 = "CREATE VIEW RRESERVED AS SELECT * FROM HAS NATURAL JOIN RESERVATION";

        String query2 = "SELECT Hotel_Location, SUM(Total_Cost) FROM `RRESERVED` \n" +
                "WHERE Start_Date >= \"2015-09-01\" and Start_Date <= \"2015-09-30\"\n" +
                "GROUP BY Hotel_Location";

        String query3 = "DROP VIEW RRESERVED";

        try {
            connection = ConnectionConfiguration.getConnection();

            preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement3 = connection.prepareStatement(query3);
            preparedStatement1.executeUpdate();
            resultSet = preparedStatement2.executeQuery();
            preparedStatement3.executeUpdate();
            return resultSet;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
