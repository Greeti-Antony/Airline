package Airlinedemo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
		
		private static String url="jdbc:mysql://localhost:3306/airlinedemo";
	    private static String username="root";
	    private static String password="Greeti@27";
	    
		
		

		public static Connection getconnection() throws SQLException {
					return DriverManager.getConnection(url, username, password);
		}

	}
