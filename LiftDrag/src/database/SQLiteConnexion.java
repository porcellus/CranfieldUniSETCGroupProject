/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;
import java.sql.*;
import session.Session;
//fake change
/**
 *
 * @author pandachan
 */
public class SQLiteConnexion implements SessionControl
{
  static Connection c = null;
  public static Connection connexion()
  {
    
    Statement stmt = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:test.db");
      //if new db
      stmt = c.createStatement();
      String sql = "CREATE TABLE IF NOT EXISTS SESSION " +
                   "(ID INT PRIMARY KEY NOT NULL," +
                   " NAME TEXT NOT NULL, " + 
                   " PWD TEXT NOT NULL)"; //secure pwd
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE IF NOT EXISTS ITERATION " +
           "(ID INT PRIMARY KEY NOT NULL," +
           " SESSION INT NOT NULL, " + 
           " NUM INT NOT NULL, " + 
           " RESULT FLOAT NOT NULL," + 
           "FOREIGN KEY(SESSION) REFERENCES SESSION(ID))";
      stmt.executeUpdate(sql);
      stmt.close();
      c.close();
      
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Opened database successfully");
    return c;
  }

    @Override
    public Session createSession(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Session loginSession(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
    
