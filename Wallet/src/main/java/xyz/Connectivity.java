package xyz;
import java.sql.*;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Connectivity {
    public static final String URLTOCONNECT = "jdbc:mysql://localhost:3306/wallet";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "Sjv@tavant2021";
    Connection dbCon;
    Statement theStatement;
    PreparedStatement thePreparedStatement;
    String qry;
    Connectivity(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbCon = DriverManager.getConnection(URLTOCONNECT, USERNAME, PASSWORD);
            theStatement = dbCon.createStatement();
            System.out.println("Database connection established");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Can't load the driver: " + e.getMessage());
        }
        catch (SQLException e) {
            System.out.println("Can't connect to the database: " + e.getMessage());
        }
    }
    //Account Creation
    boolean createAccount(String name,String userId,String password,Integer balance){

        String qry = "insert into account_users(aname, userid, pass, balance) values(?, ?, ?, ?)";
        try {
//        	Get the PreparedStatement object
            thePreparedStatement = dbCon.prepareStatement(qry);

//			Put the values for ?
            thePreparedStatement.setString(1, name);
            thePreparedStatement.setString(2, userId);
            thePreparedStatement.setString(3, password);
            thePreparedStatement.setInt(4, balance);

//			Execute the query
            if(thePreparedStatement.executeUpdate() > 0)
                System.out.println("User added successfully...");
            return true;
        } catch (SQLException e) {
            System.out.println("User Not Created: " + e.getMessage());
            return false;
        }
    }
    //Credit
    void addAmountToUser(Integer amount, String userId) {
        qry = "update account_users set balance = balance + ? where userid = ?";
        try {
            thePreparedStatement = dbCon.prepareStatement(qry);
//			Map the values to ?
            thePreparedStatement.setInt(1, amount);
            thePreparedStatement.setString(2, userId);

            if (thePreparedStatement.executeUpdate() > 0) {
                System.out.println("Amount Credited successfully to "+userId+"!!!");
                addlog("credit", userId, userId, amount);
                return;
            }
        } catch (SQLException e) {
            System.out.println("Amount Not Credited, Try again after some time "); // + e.getMessage());
        }
    }
    //Debit
    void removeAmountFromUser(Integer amount, String userId, String password) {
        qry = "update account_users set balance = balance - ? where userid = ? and pass=?";
        try {
            thePreparedStatement = dbCon.prepareStatement(qry);
//			Map the values to ?
            thePreparedStatement.setInt(1, amount);
            thePreparedStatement.setString(2, userId);
            thePreparedStatement.setString(3, password);
            if (thePreparedStatement.executeUpdate() > 0) {
                System.out.println("Amount Debited successfully from "+userId+"!!!");
                addlog("debit", userId, userId, amount);
                return;
            }
        } catch (SQLException e) {
            System.out.println("Insufficent Funds");
//            System.out.println("Insufficent Funds " + e.getMessage());
        }
    }
    //Transfering amount
    void transferAmount(String d_userId,String c_userId, String d_password, Integer amount){
        //Debit first
        qry = "update account_users set balance = balance - ? where userid = ? and pass=?";
        try {
            thePreparedStatement = dbCon.prepareStatement(qry);
//			Map the values to ?
            thePreparedStatement.setInt(1, amount);
            thePreparedStatement.setString(2, d_userId);
            thePreparedStatement.setString(3, d_password);
            if (thePreparedStatement.executeUpdate() > 0) {
                System.out.println("Amount Debited successfully from "+d_userId+"!!!");
            }
        } catch (SQLException e) {
            //System.out.println("Insufficent Funds " + e.getMessage());
            System.out.println("Insufficent Funds " + e.getMessage());
            return;
        }
        //Credit second
        qry = "update account_users set balance = balance + ? where userid = ?";
        try {
            thePreparedStatement = dbCon.prepareStatement(qry);
//			Map the values to ?
            thePreparedStatement.setInt(1, amount);
            thePreparedStatement.setString(2, c_userId);

            if (thePreparedStatement.executeUpdate() > 0) {
                System.out.println("Amount Credited successfully to "+c_userId+"!!!");
            }
        } catch (SQLException e) {
            System.out.println("Amount Not Credited, Try again after some time "); // + e.getMessage());
        }
        addlog("transfer", c_userId, d_userId, amount);
    }
    //For Checking validity of user in login time
    boolean verifyUser(String userId, String password){
        qry="select count(*) as count_size from account_users where userid = ? and pass = ?";
        int count=0;
        try {
            thePreparedStatement = dbCon.prepareStatement(qry);
            thePreparedStatement.setString(1, userId);
            thePreparedStatement.setString(2, password);
            ResultSet theResultSet = thePreparedStatement.executeQuery();
            while(theResultSet.next()){
                if(theResultSet.getInt("count_size")==1){
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Query cannot be executed: " + e.getMessage());
            System.out.println("Query cannot be executed: " + e);
            return false;
        }
    }
    //For Checking validity of user in transfer time
    boolean userExistsOrNot(String userId){
        qry="select count(*) as count_size from account_users where userid = ?";
        int count=0;
        try {
            thePreparedStatement = dbCon.prepareStatement(qry);
            thePreparedStatement.setString(1, userId);

            ResultSet theResultSet = thePreparedStatement.executeQuery();
            while(theResultSet.next()){
                if(theResultSet.getInt("count_size")==1){
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Query cannot be executed: " + e.getMessage());
            return false;
        }
    }
    // Checking account Balance
    void showAccountBalance(String userId, String password){
        qry = "select balance from account_users where userid = ? and pass=?";
        try {
            thePreparedStatement = dbCon.prepareStatement(qry);
            thePreparedStatement.setString(1, userId);
            thePreparedStatement.setString(2, password);
            ResultSet theResultSet = thePreparedStatement.executeQuery();
            //
            while(theResultSet.next()){
                Integer bal_val= theResultSet.getInt("balance");
                System.out.println("The remaining balance in the account is: "+bal_val);
                if(bal_val<=500)
                    System.out.println("Balance is low");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Unable to show amount, Try again after some time " + e.getMessage());
        }
    }
    //Add Transaction files
    void addlog(String type_of_transaction, String c_userId, String d_userId, Integer amount){
//        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//        String formattedDate = LocalDateTime.now().format(myFormatObj);
        String qry = "insert into transactions(type_of_transaction, c_userid, d_userid, amount, time_of_transaction) values(?, ?,?, ?, ?)";
        try {
//        	Get the PreparedStatement object
            thePreparedStatement = dbCon.prepareStatement(qry);

//			Put the values for ?
            thePreparedStatement.setString(1, type_of_transaction);
            thePreparedStatement.setString(2, c_userId);
            thePreparedStatement.setString(3, d_userId);

            thePreparedStatement.setInt(4, amount);
            thePreparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

//			Execute the query
            if (thePreparedStatement.executeUpdate() > 0)
                System.out.println("Timestamp added sucessfully");
        } catch (SQLException e) {
            System.out.println("Timestamp not added: " + e.getMessage());

        }

    }
    //Checking existance of userid in userid creation step
    boolean checkuserId(String userId){
        qry="select count(*) as count_size from account_users where userid = ? ";
        int count=0;
        try {
            thePreparedStatement = dbCon.prepareStatement(qry);
            thePreparedStatement.setString(1, userId);
            ResultSet theResultSet = thePreparedStatement.executeQuery();
            while(theResultSet.next()){
                if(theResultSet.getInt("count_size")==0){
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Query cannot be executed: " + e.getMessage());

            return false;
        }
    }
    //To Chcek Transaction History
    void showTransactionHistory(String userId){
        qry="select *  from transactions where c_userid = ? or d_userid = ?";
        int count=0;
        try {
            thePreparedStatement = dbCon.prepareStatement(qry);
            thePreparedStatement.setString(1, userId);
            thePreparedStatement.setString(2, userId);
            ResultSet theResultSet = thePreparedStatement.executeQuery();
            while(theResultSet.next()) {
                System.out.println("The sender Id: " + theResultSet.getString("d_userId") +
                        ", The receiver Id: " + theResultSet.getString("c_userId") +
                        ", Amount: " + theResultSet.getInt("amount") +
                                ", Type of transaction: " + theResultSet.getString("type_of_transaction")+
                        ", Time Stamp: "+theResultSet.getTimestamp("time_of_transaction"));
            }
        } catch (SQLException e) {
            System.out.println("Try Again: " + e.getMessage());
            System.out.println("Try Again: " + e);
        }
    }
}

