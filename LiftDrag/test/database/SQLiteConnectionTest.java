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

import session.OptimizationResult;
import session.Session;

public class SQLiteConnectionTest
{
	SQLiteConnection database;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File file = new File("data.db");
		file.getAbsolutePath();
		file.delete();
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
				  if(sessionExists == false && tableName.equals("SESSION"))
					  sessionExists = true;
				  
				  if(iterationExists == false && tableName.equals("ITERATION"))
					  iterationExists = true;
			}
			
			database.close();
			
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
            database.close();
            
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
		
        
		try
		{
			Session uploadedSession = database.createSession(username, password);
			uploadedSession.setParameters(minangle, maxangle, mincamber, maxcamber, minthickness, maxthickness);
			database.setSessionParameters(uploadedSession);
			
			Session downloadedSession = database.loginSession(username, password);
			database.close();
			
			assertTrue(uploadedSession.equals(downloadedSession));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testIsSessionExisting()
	{
		String username = "qwerty3";
		String password = "superpassword3";
		
		try
		{
			Session uploadedSession = database.createSession(username, password);
			
			assertTrue(database.isSessionExisting(username));
			database.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testGetSetResult()
	{
		String username = "qwerty4";
		String password = "superpassword4";
		
		double angle = 1;
		double camber = 2;
		double thickness = 3;
		double lift = 4;
		double drag = 5;
		
		try
		{
			Session session = database.createSession(username, password);
			OptimizationResult uploadedResult = new OptimizationResult();
			uploadedResult.set(angle, camber, thickness, lift, drag);
			database.setResult(uploadedResult, session.getId());
			OptimizationResult downloadedResult = database.getResult(database.getIterationNum(session.getId()), session.getId());
			
			database.close();
			assertTrue(uploadedResult.equals(downloadedResult));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		
	}

	@Test
	public void testGetIterationNum()
	{
		String username = "qwerty5";
		String password = "superpassword5";
		
		double angle = 6;
		double camber = 7;
		double thickness = 8;
		double lift = 9;
		double drag = 10;
		
		int oldId, newId;
		
		Session session;
		try
		{
			session = database.createSession(username, password);
			oldId = database.getIterationNum(session.getId());
			OptimizationResult uploadedResult = new OptimizationResult();
			uploadedResult.set(angle, camber, thickness, lift, drag);
			database.setResult(uploadedResult, session.getId());
			newId = database.getIterationNum(session.getId());
			database.close();
			assertEquals(oldId + 1, newId);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
