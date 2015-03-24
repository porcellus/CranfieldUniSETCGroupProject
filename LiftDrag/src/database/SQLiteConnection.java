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

import session.OptimizationResult;
import session.Session;
/**
 *
 * @author pandachan
 */
public class SQLiteConnection implements SessionControl{
    static Connection c = null;
    static Statement stmt = null;

    public static Connection connection(){ //Create the connexion to the database
        try {
            Class.forName("org.sqlite.JDBC");//use sqlite-jdbc jar in library repository
            //db and tables creation if not already existing
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS SESSION " +
                         "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                         " NAME TEXT NOT NULL, " + 
                         " PWD TEXT NOT NULL, "+
                          " MINANGLE DOUBLE, "+
                          " MAXANGLE DOUBLE, "+
                          " MINTHICKNESS DOUBLE, "+
                          " MAXTHICKNESS DOUBLE, "+
                          " MINCAMBER DOUBLE, "+
                          " MAXCAMBER DOUBLE "+
                         " )";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS ITERATION " +
                 "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                 " SESSION INT NOT NULL, " + 
                 " NUM INT NOT NULL, " + 
                 " ANGLE DOUBLE NOT NULL," + 
                 " CAMBER DOUBLE NOT NULL," + 
                 " THICKNESS DOUBLE NOT NULL," + 
                 " LIFT DOUBLE NOT NULL," + 
                 " DRAG DOUBLE NOT NULL," + 
                 "FOREIGN KEY(SESSION) REFERENCES SESSION(ID))";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        return c;
    }

    @Override
    public Session createSession(String username, String password) throws SQLException {//check if a name is not already used and create new session
        MessageDigest md5;
        try {
            //check username non existing
            stmt = c.createStatement();
            String sql="SELECT COUNT(ID) AS EXISTING FROM SESSION WHERE NAME=\""+username+"\" ;";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.getInt("EXISTING")!=0){
                rs.close();
                stmt.close();
                System.err.println("Name already used for another session");
                throw new SQLException("This session name is already used for another session.");
            }
            rs.close();
            //create a new session if it doesn't exist
            md5 = MessageDigest.getInstance("MD5");
            String encodePwd = (new HexBinaryAdapter()).marshal(md5.digest(password.getBytes()));   
            sql = "INSERT INTO SESSION (NAME,PWD) " +
                         "VALUES (\""+ username +"\", \""+ encodePwd +"\" );"; 
            c.setAutoCommit(false);
            stmt = c.createStatement();
            stmt.executeUpdate(sql);
            c.commit();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    System.out.println("Records created successfully");
                    stmt.close();
                    return new Session(username,encodePwd, this, generatedKeys.getInt(1));
                }else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        } catch (NoSuchAlgorithmException ex) {
            System.err.println( "Problem with MD5" );
            System.exit(0);
        }

        return null;
    }

    @Override
    public Session loginSession(String username, String password) throws SQLException {//return the session asked as object if it exists
        Statement stmt = null;
        MessageDigest md5;
        try {
            //password to hash
            md5 = MessageDigest.getInstance("MD5");
            String encodePwd = (new HexBinaryAdapter()).marshal(md5.digest(password.getBytes()));
            stmt = c.createStatement();
            //check if the session exists
            String sql="SELECT COUNT(ID) AS FOUND, ID, MINANGLE, MAXANGLE, MINTHICKNESS, MAXTHICKNESS, MINCAMBER, MAXCAMBER FROM SESSION"+" WHERE NAME=\""+username+"\" AND PWD=\""+encodePwd+"\" ;";
            ResultSet rs = stmt.executeQuery( sql );
            rs.next();
            int numberRows = rs.getInt("FOUND");
            if(numberRows == 0)
			{
            	rs.close();
            	stmt.close();
			    System.err.println("Session not found");
			    throw new SQLException("Invalid session name or password.");
			}
            else{//if the session exists return the session as object with all its data
                int id=rs.getInt("ID");
                double minangle = rs.getDouble("MINANGLE");
                double maxangle = rs.getDouble("MAXANGLE");
                double minthickness = rs.getDouble("MINTHICKNESS");
                double maxthickness = rs.getDouble("MAXTHICKNESS");
                double mincamber = rs.getDouble("MINCAMBER");
                double maxcamber = rs.getDouble("MAXCAMBER");
                rs.close();
                stmt.close();
                return new Session(this, id, username, encodePwd, minangle, maxangle, minthickness, maxthickness, mincamber, maxcamber);
            }
        } catch (NoSuchAlgorithmException ex) {
            System.err.println( "Problem with MD5" );
            System.exit(0);
        }
        
        return null;
    }

    @Override
    public void setSessionParameters(Session s) {//save the attributes of s into the db
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
    public void setResult(OptimizationResult result, int id) {//add the result as an iteration to the session
        Statement stmt = null;
        try {
            //find the number of iteration already existing for this session
            stmt = c.createStatement();
            String sql="SELECT COUNT(ID) AS NB FROM ITERATION WHERE SESSION="+id+" ;";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int num = rs.getInt("NB");
            num++;
            rs.close();
            //add the new result as an iteration
            c.setAutoCommit(false);
            sql = "INSERT INTO ITERATION (SESSION, ANGLE, CAMBER, THICKNESS, LIFT, DRAG, NUM) VALUES ("+ id +", "+ result.getAngle() +", "+ result.getCamber() +", "+ result.getThickness() +", "+ result.getLift() +", "+ result.getDrag() +", "+num+");";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

    @Override
    public OptimizationResult getResult(int number, int id) {//find the result for a specific iteration of a specific session
        Statement stmt = null;
        try {
            //try to find the iteration
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql="SELECT ANGLE, CAMBER, THICKNESS, LIFT, DRAG FROM ITERATION WHERE SESSION="+id+" AND NUM="+number+" ;";
            ResultSet rs = stmt.executeQuery( sql );
            while(rs.next()){//if it exists return the result
              OptimizationResult res = new OptimizationResult();
              res.set(rs.getDouble("ANGLE"),rs.getDouble("CAMBER"),rs.getDouble("THICKNESS"),rs.getDouble("LIFT"),rs.getDouble("DRAG"));
              rs.close();
              stmt.close();
              System.out.println("Iteration found");
              return res;
            }
            rs.close();
            stmt.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.err.println("Iteration not found");
        return null;
    }
    
    public int maxIteration(int id){//find the number of iteration already existing for this session
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql="SELECT COUNT(ID) AS NB FROM ITERATION WHERE SESSION="+id+" ;";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int num = rs.getInt("NB");
            rs.close();
            stmt.close();
            return num;
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return -1;
    }
}