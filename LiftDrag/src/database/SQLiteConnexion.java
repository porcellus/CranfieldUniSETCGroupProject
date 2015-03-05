/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import session.Session;
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
                   "(ID INT PRIMARY KEY NOT NULL AUTOINCREMENT," +
                   " NAME TEXT NOT NULL, " + 
                   " PWD TEXT NOT NULL, "+
                    " MINANGLE FLOAT, "+
                    " MAXANGLE FLOAT, "+
                    " MINTHICKNESS FLOAT, "+
                    " MAXTHICKNESS FLOAT, "+
                    " MINCAMBER FLOAT, "+
                    " MAXCAMBER FLOAT, "+
                   " )";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE IF NOT EXISTS ITERATION " +
           "(ID INT PRIMARY KEY NOT NULL AUTOINCREMENT," +
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
        Statement stmt = null;
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            String encodePwd = (new HexBinaryAdapter()).marshal(md5.digest(password.getBytes()));
            try {
              c.setAutoCommit(false);
              stmt = c.createStatement();
              String sql = "INSERT INTO SESSION (NAME,PWD) " +
                           "VALUES ("+ username +", "+ encodePwd +" );"; 
              stmt.executeUpdate(sql);
              stmt.close();
              c.commit();
              System.out.println("Records created successfully");
              return new Session(username,password, this);
            } catch ( Exception e ) {
              System.err.println( e.getClass().getName() + ": " + e.getMessage() );
              System.exit(0);
            }            
        } catch (NoSuchAlgorithmException ex) {
          System.err.println( "Problem with MD5" );
          System.exit(0);
        }
      return null;
    }

    @Override
    public Session loginSession(String username, String password) {
        Statement stmt = null;
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            String encodePwd = (new HexBinaryAdapter()).marshal(md5.digest(password.getBytes()));
            try {
              c.setAutoCommit(false);
              stmt = c.createStatement(); 
              ResultSet rs = stmt.executeQuery( "SELECT COUNT(ID) AS FOUND, ID, MINANGLE, MAXANGLE, MINTHICKNESS, MAXTHICKNESS, MINCAMBER, MAXCAMBER FROM SESSION"+
                                                " WHERE NAME="+username+
                                                " AND PWD="+encodePwd+" ;" );
              rs.next();
              int numberRows = rs.getInt("FOUND");
              if(numberRows != 1) System.err.println("Session not found");
              else{
                  int id=rs.getInt("ID");
                  float minangle = rs.getFloat("MINANGLE");
                  float maxangle = rs.getFloat("MAXANGLE");
                    float minthickness = rs.getFloat("MINTHICKNESS");
                    float maxthickness = rs.getFloat("MAXTHICKNESS");
                    float mincamber = rs.getFloat("MINCAMBER");
                    float maxcamber = rs.getFloat("MAXCAMBER");
                    rs.close();
                    stmt.close();
                    System.out.println("Session found");
                    return new Session(this, id, username, minangle, maxangle, minthickness, maxthickness, mincamber, maxcamber);
              }
            } catch ( Exception e ) {
              System.err.println( e.getClass().getName() + ": " + e.getMessage() );
              System.exit(0);
            }
        } catch (NoSuchAlgorithmException ex) {
          System.err.println( "Problem with MD5" );
          System.exit(0);
        }
        return null;
    }

    @Override
    public void setSessionParameters(Session s) {
        Statement stmt = null;
        try {
          c.setAutoCommit(false);
          stmt = c.createStatement();
          String sql = "UPDATE SESSION set MINANGLE="+s.getMinangle()+", MAXANGLE="+s.getMaxangle()+", MINTHICKNESS="+s.getMinthickness()+", MAXTHICKNESS="+s.getMaxthickness()+", MINCAMBER="+s.getMincamber()+", MAXCAMBER="+s.getMaxcamber()+" where ID="+s.getId()+";";
          stmt.executeUpdate(sql);
          c.commit();
          stmt.close();
        } catch ( Exception e ) {
          System.err.println( e.getClass().getName() + ": " + e.getMessage() );
          System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

    @Override
    public void setResult(float result, int id) {
        Statement stmt = null;
        try {
          c.setAutoCommit(false);
          stmt = c.createStatement();
          //num of the iteration
          ResultSet rs = stmt.executeQuery( "SELECT MAX(NUM) AS MAXNUM FROM ITERATION"+
                                            " WHERE SESSION="+id+" ;" );
          rs.next();
          int num = rs.getInt("MAXNUM");
          //insert new iteration
          String sql = "INSERT INTO ITERATION (SESSION,RESULT,NUM) " +
                       "VALUES ("+ id +", "+ result +", "+ num +" );"; 
          stmt.executeUpdate(sql);
          stmt.close();
          c.commit();
        } catch ( Exception e ) {
          System.err.println( e.getClass().getName() + ": " + e.getMessage() );
          System.exit(0);
        }
        System.out.println("Records created successfully");
    }

    @Override
    public float getResult(int number, int id) {
        Statement stmt = null;
        try {
          c.setAutoCommit(false);
          stmt = c.createStatement();
          ResultSet rs = stmt.executeQuery( "SELECT RESULT FROM ITERATION"+
                                            " WHERE SESSION="+id+
                                            " AND NUM="+number+" ;" );
          while(rs.next()){
            float res = rs.getFloat("RESULT");
            rs.close();
            stmt.close();
            System.err.println("Iteration found");
            return res;
          }
          rs.close();
          stmt.close();
        } catch ( Exception e ) {
          System.err.println( e.getClass().getName() + ": " + e.getMessage() );
          System.exit(0);
        }
        System.err.println("Iteration not found");
        return -1;
    }
}
    
