package com.tms.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionHelper {
		
		private String url;
		private static ConnectionHelper instance;
		String path = "jdbc:mysql://localhost:3306/TMS?user=root&password=br549dab";
		//String path = "jdbc:mysql://localhost:3306/TMS?user=root&password=sonali28";
		  String dbName = "TMS";
		  String driver = "com.mysql.jdbc.Driver";	
		  String userName = "root"; 
		  String password = "br549dab";
		  

		private ConnectionHelper()
		{
				
				try {
				Class.forName("com.mysql.jdbc.Driver");
				url=path+dbName + ","+userName+"," +password;
	            /*ResourceBundle bundle = ResourceBundle.getBundle("data");
	            driver = bundle.getString("jdbc.driver");
	            Class.forName(driver);
	            url=bundle.getString("jdbc.url");*/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public static Connection getConnection() throws SQLException {
			if (instance == null) {
				instance = new ConnectionHelper();
			}
			try {
				return DriverManager.getConnection(instance.path);
			} catch (SQLException e) {
				throw e;
			}
		}
		
		public static void close(Connection connection)
		{
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}


