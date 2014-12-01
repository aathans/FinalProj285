package eecs285.proj4.finalGame;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Database {

    // JDBC database URL
    static final String DB_URL = "jdbc:mysql://sql2.freemysqlhosting.net/sql259966";

    //  Database credentials
    static final String USER = "sql259966";
    static final String PASS = "cL2!hY1%";


    public static Connection establishConnection(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        }
        catch (SQLException s) {
            System.out.println("SQLException");
            s.printStackTrace();
        }
        catch(Exception e){
            //Handle errors for Class.forName
            System.out.println("Class not found exception");
            e.printStackTrace();
        }
        return conn;
    }

    public static void populateScores(Connection conn){
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "SELECT score, user FROM scores1" +
                    " ORDER BY score DESC";
            ResultSet rs = stmt.executeQuery(sql);

            DbEntry[] entryArray = new DbEntry[8];
            int index = 0;
            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                int score  = rs.getInt("score");
                String user = rs.getString("user");

                DbEntry e = new DbEntry(user, score);
                if (index < 8) {
                    entryArray[index] = e;
                    System.out.print("Score: " + entryArray[index].getScore());
                    System.out.println(", User: " + entryArray[index].getUser());
                }

                index += 1;
            }
            rs.close();
        }
        catch (SQLException s) {
            System.out.println("SQLException");
            s.printStackTrace();
        }
        finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    conn.close();
            }catch(SQLException se){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
}
