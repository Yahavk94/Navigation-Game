package gameClient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/**
 * This class represents a simple example of using MySQL Data-Base.
 * Use this example for writing solution. 
 * @author boaz.benmoshe
 *
 */
public class SimpleDB {
	public static final String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser="student";
	public static final String jdbcUserPassword="OOP2020student";

	/**
	 * Simple main for demonstrating the use of the Data-base
	 * @param args
	 */
	public static void main(String[] args) {
		int id1 = 999;  // "dummy existing ID  
		int level = 0;
		allUsers();
		printLog();
		String kml = getKML(id1,level);
		System.out.println("***** KML file example: ******");
		System.out.println(kml);
	}
	/** simply prints all the games as played by the users (in the database).
	 * 
	 */
	public static void printLog() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);

			while(resultSet.next())
			{
				System.out.println("Id: " + resultSet.getInt("UserID")+","+resultSet.getInt("levelID")+","+resultSet.getInt("moves")+","+resultSet.getDate("time"));
			}
			resultSet.close();
			statement.close();		
			connection.close();
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * this function returns the KML string as stored in the database (userID, level);
	 * @param id
	 * @param level
	 * @return
	 */
	public static String getKML(int id, int level) {
		String ans = null;
		String allCustomersQuery = "SELECT * FROM Users where userID="+id+";";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			if(resultSet!=null && resultSet.next()) {
				ans = resultSet.getString("kml_"+level);
			}
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}
	public static int allUsers() {
		int ans = 0;
		String allCustomersQuery = "SELECT * FROM Users;";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()) {
				System.out.println("Id: " + resultSet.getInt("UserID"));
				ans++;
			}
			resultSet.close();
			statement.close();		
			connection.close();
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}
	
	public static Object[][] printUserLog(int ID_1, int ID_2) {
		
		ArrayList<String> gameData = new ArrayList<String>();
		
		int n = getNumOfGames(ID_1, ID_2);
		
		Object[][] gData = new Object[n][6];
		
		String [] innerArray = new String[6];

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			
			
			int i =0;
			while(resultSet.next())
			{
				int ID=resultSet.getInt("UserID");
				
				if(ID==ID_1||ID==ID_2) {
					//String data = "Id: " + ID +","+resultSet.getInt("levelID")+", score: "+resultSet.getInt("score")+","+resultSet.getInt("moves")+","+resultSet.getDate("time");
					//gameData.add(data);
					
					gData[i][0] = "" +(i+1);
					gData[i][1] = ""+ID;
					gData[i][2] = ""+resultSet.getInt("levelID");
					gData[i][3] = ""+resultSet.getInt("score");
					gData[i][4] = ""+resultSet.getInt("moves");
					gData[i][5] = ""+resultSet.getDate("time");
					i++;
				}
			}
			
			resultSet.close();
			statement.close();		
			connection.close();	
			
		}
		
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return gData;

	}
	
	public static ArrayList<Triple> getRankingForLevel(int level) {
		
		ArrayList<Triple> levelRanking = new ArrayList<Triple>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			
			
			
			while(resultSet.next())
			{
				int levelId=resultSet.getInt("levelID");
				
				if(levelId==level) {
					levelRanking.add(new Triple(resultSet.getInt("UserID"),resultSet.getInt("score"),resultSet.getInt("moves")));
				}
			}
			levelRanking.sort(null);
			
			resultSet.close();
			statement.close();		
			connection.close();		
		}
		
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return rmDuplicates(levelRanking);
	}
	
	public static ArrayList<Triple> rmDuplicates(ArrayList<Triple> ranking){
		ArrayList<Triple> noDuplList = new ArrayList<Triple>();
		ArrayList<Integer> idList = new ArrayList<Integer>();
		
		for(Triple trip : ranking) {
			int id = trip.getID();
			if(!idList.contains(id)) {
				idList.add(id);
				noDuplList.add(trip);
			}
		}
		return noDuplList;
	}
	
	public static int ourRanking(ArrayList<Triple> ranking, int id1, int id2) {
		for (Triple triple : ranking)
			if (triple.getID() == id1 || triple.getID() == id2)
				return ranking.indexOf(triple) + 1;
		
		return -1;
	}
	
	public static int getNumOfGames(int ID_1, int ID_2, boolean flag) {
		
		int numOfGames = 0;
		int total = 0;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			
			
			
			while(resultSet.next())
			{
				int ID=resultSet.getInt("UserID");
				
				if(ID==ID_1||ID==ID_2) {
					numOfGames ++;
					total++;
				}
				
				else total++;
			}
			
			resultSet.close();
			statement.close();		
			connection.close();		
		}
		
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if (!flag) return numOfGames;
		else return total;
	}
}

