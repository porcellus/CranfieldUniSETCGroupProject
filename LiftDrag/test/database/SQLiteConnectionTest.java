package database;

import static org.junit.Assert.*;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import session.Session;

public class SQLiteConnectionTest
{
	SQLiteConnection database;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File file = new File("data.db");
		System.out.println(file.getAbsolutePath());
		System.out.println(file.delete());
	}
	
	@Before
    public void setUp() {

		database = new SQLiteConnection();
		database.connection();
    }
	
	@Test
	public void testConnection()
	{
		Boolean sessionExists = false;
		Boolean iterationExists = false;
		String tableName;
		DatabaseMetaData md;
		
		try
		{
			md = database.c.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);

			while (rs.next())
			{
				tableName = rs.getString(3);
				System.out.println(tableName);
				  if(sessionExists == false && tableName.equals("SESSION"))
					  sessionExists = true;
				  
				  if(iterationExists == false && tableName.equals("ITERATION"))
					  iterationExists = true;
			}
			
			assertTrue(sessionExists && iterationExists);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateSession()
	{
		String username = "qwerty";
		String password = "superpassword";
		
		try
		{
			MessageDigest md5;
			md5 = MessageDigest.getInstance("MD5");
			String encodePwd = (new HexBinaryAdapter()).marshal(md5.digest(password.getBytes()));
			database.createSession(username, password);
			String sql="SELECT COUNT(ID) AS FOUND, ID, MINANGLE, MAXANGLE, MINTHICKNESS, MAXTHICKNESS, MINCAMBER, MAXCAMBER FROM SESSION"+" WHERE NAME=\""+username+"\" AND PWD=\""+encodePwd+"\" ;";
			ResultSet rs = database.stmt.executeQuery( sql );
            rs.next();
            int numberRows = rs.getInt("FOUND");
            assertEquals(numberRows, 1);
		} catch (SQLException e)
		{
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testLoginSession()
	{
		String username = "qwerty2";
		String password = "superpassword2";
		int ID = 10;
		
		double minangle = 1;
        double maxangle = 2;
        double minthickness = 3;
        double maxthickness = 4;
        double mincamber = 5;
        double maxcamber = 6;
		
        //Session session = new Session(database, ID, username, password,
        //					minangle, maxangle, minthickness, maxthickness, mincamber, maxcamber);
        
		try
		{
			Session uploadedSession = database.createSession(username, password);
			uploadedSession.setParameters(minangle, maxangle, mincamber, maxcamber, minthickness, maxthickness);
			database.setSessionParameters(uploadedSession);
			
			Session downloadedSession = database.loginSession(username, password);
			// TODO finish
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		fail("Not yet implemented");
	}

	@Test
	public void testIsSessionExisting()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetSessionParameters()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetResult()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetResult()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetIterationNum()
	{
		fail("Not yet implemented");
	}

}
