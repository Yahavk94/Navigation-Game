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
	
	/**
	 * returns the game data for student1 and student2: level, score, time and date of the game, number of moves.
	 * return type is 2d object array for JTable
	 * @param ID_1	id of student1
	 * @param ID_2 	id of student2
	 * @return	2d object array of game data of specified students
	 */
	
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
				int ID = resultSet.getInt("UserID");
				
				if (ID==ID_1||ID==ID_2) {
					gData[i][0] = "" +(i+1);
					gData[i][1] = "" +ID;
					gData[i][2] = "" +resultSet.getInt("levelID");
					gData[i][3] = "" +resultSet.getInt("score");
					gData[i][4] = "" +resultSet.getInt("moves");
					gData[i][5] = "" +resultSet.getDate("time");
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
	
	/**
	 * This function build a ranking list for specified level, and sorts it by decreasing score
	 * @param level	level for which ranking should be retrieved
	 * @return sorted ranking, sorted by score, but with duplicate IDs 
	 */
	
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
	
	/**
	 * removes duplicates from ranking list, keeps only the highest score per ID
	 * @param ranking sorted Ranking list with duplicates from highest to lowest score
	 * @return Ranking list without duplicates with only highest score
	 */
	
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
	
	/**
	 * this function counts the amount of times 2 specific students have played on the server
	 * @param ID_1	ID of student 1
	 * @param ID_2	ID of student 2
	 * @return	number of games played
	 */
	
	public static int getNumOfGames(int ID_1, int ID_2) {
		
		int numOfGames = 0;

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
		
		return numOfGames;
	}
	
	/**
	 * returns the rank of student1 and student2 in the class for a specific level, only considering valid entries,
	 * i.e. entries with more than allowed moves are ignored, dummy IDs: 999 and 0 are ignored
	 * @param ranking rank list
	 * @param id1 ID of student 1
	 * @param id2 ID of student 2
	 * @param level level for which ranking is requested
	 * @return
	 */
	public static int ourRanking(ArrayList<Triple> ranking, int id1, int id2, int level) {
		int rank = 1;
		//ArrayList<Integer> users = getUserIDs();
		int maxMoves = getMaxMoves(level);
		for (Triple triple : ranking) {
			if (triple.getID() == id1 || triple.getID() == id2)
				return rank;
			if(triple.getMoves()<=maxMoves&&/*users.contains(triple.getID())&&*/triple.getID()!=999) //check if valid entry
				rank++;
		}

		return -1;
	}

	/**
	 * get the maximum number of moves that are allowed for given level
	 * returns integer max_value if level has no specified move limit
	 * @param level	level ID 
	 * @return	max allowed number of moves
	 */
	private static int getMaxMoves(int level){
		int maxMoves;

		switch(level){
		case 0: maxMoves= 290;
		break;
		case 1: 
		case 3: maxMoves = 580;
		break;
		case 5: maxMoves = 500;
		break;
		case 9: 
		case 11:
		case 13: maxMoves = 580;
		break;
		case 16: maxMoves = 290;
		break;
		case 19: maxMoves = 580;
		break;
		case 20: maxMoves = 290;
		break;
		case 23: maxMoves = 1140;
		break;
		default: maxMoves = Integer.MAX_VALUE;
		}
		return maxMoves;
	}
	
	
	/**
	 * get IDs of all users registered in the data base
	 * @return arraylist of user IDs
	 */
	public static ArrayList<Integer> getUserIDs() {
		ArrayList<Integer> userIDs = new ArrayList<Integer>();
		String allCustomersQuery = "SELECT * FROM Users;";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()) {
				userIDs.add(resultSet.getInt("UserID"));
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
		return userIDs;
	}
	
	/**
	 * find the row with the highest score for given level
	 * @param gameData	array of game data
	 * @param level
	 * @return	row with highest score
	 */
	public static int toHighlightLevel(Object[][] gameData, int level){

		int maxScore = Integer.MIN_VALUE;
		int maxIndex = -1;
		
		int idx = 0;

		for(Object [] obj : gameData) {
			int currentLevel = Integer.parseInt((String)obj[2]);
			if(level==currentLevel) {
				int score = Integer.parseInt((String)obj[3]);
				if(score>maxScore) {
					maxScore=score;	
					maxIndex=idx;
				}
			}
			++idx;
		}

		return maxIndex;
	}
	
	/**
	 * get array of highscores of student1 or student2 for each level
	 * @param ranking rank list
	 * @param id1	id of student1
	 * @param id2	id of student2
	 * @return
	 */
	
	public static ArrayList<Integer> getHighscores(int id1, int id2) {
		ArrayList<Integer> highestScores = new ArrayList<Integer>();
		ArrayList<Integer> levels = new ArrayList<Integer>();
		levels.add(0);
		levels.add(1);
		levels.add(3);
		levels.add(5);
		levels.add(9);
		levels.add(11);
		levels.add(13);
		levels.add(16);
		levels.add(19);
		levels.add(20);
		levels.add(23);
		
		for(int level : levels) {
			ArrayList<Triple> ranking = getRankingForLevel(level);
			highestScores.add(ourHighestScore(ranking, id1, id2));
		}
		
		return highestScores;
		
	}
	/**
	 * find highest score for specific level from student1 or student2
	 * @param ranking rank list
	 * @param id1	id of student1
	 * @param id2	id of student2
	 * @param level
	 * @return
	 */
	public static int ourHighestScore(ArrayList<Triple> ranking, int id1, int id2) {

		for (Triple triple : ranking) {
			if (triple.getID() == id1 || triple.getID() == id2)
				return triple.getScore();
			}

		return 0;
	}
}

