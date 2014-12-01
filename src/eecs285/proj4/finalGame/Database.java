package eecs285.proj4.finalGame;

import java.sql.*;


/*
 * Database is responsible for connecting and making queries to the
 * MySql database
 */
public class Database
{

  // JDBC database URL
  static final String DB_URL = "jdbc:mysql://sql2.freemysqlhosting.net/sql259966";

  //  Database credentials
  static final String USER = "sql259966";
  static final String PASS = "cL2!hY1%";

  static Connection conn;


  public static Connection establishConnection()
  {
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
    }
    catch( SQLException s )
    {
      System.out.println("SQLException");
      s.printStackTrace();
    }
    catch( Exception e )
    {
      //Handle errors for Class.forName
      System.out.println("Class not found exception");
      e.printStackTrace();
    }
    return conn;
  }

  public static DbEntry[] populateScores()
  {
    Statement stmt = null;
    DbEntry[] entryArray = new DbEntry[8];
    try
    {
      stmt = conn.createStatement();
      String sql = "SELECT score, user FROM scores1" +
          " ORDER BY score DESC";
      ResultSet rs = stmt.executeQuery(sql);

      int index = 0;
      //STEP 5: Extract data from result set
      while( rs.next() )
      {
        //Retrieve by column name
        int score = rs.getInt("score");
        String user = rs.getString("user");

        DbEntry e = new DbEntry(user, score);
        if( index < 8 )
        {
          entryArray[index] = e;
        }

        index += 1;
      }
      rs.close();
    }
    catch( SQLException s )
    {
      System.out.println("SQLException");
      s.printStackTrace();
      return null;
    }
    finally
    {
      //finally block used to close resources
    }
    return entryArray;
  }

  public static void insertScore(String name, int score)
  {
    Statement stmt = null;
    try
    {

      String scoreString = String.valueOf(score);
      stmt = conn.createStatement();
      String sql = "INSERT INTO scores1 (score, user)" + "VALUES (?, ?)";
      PreparedStatement preparedStatement = conn.prepareStatement(sql);
      preparedStatement.setString(1, scoreString);
      preparedStatement.setString(2, name);

      preparedStatement.executeUpdate();


    }
    catch( SQLException ex )
    {
      ex.printStackTrace();
    }
  }
}
