package gameGUI;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import gameClient.SimpleDB;
import gameClient.Triple;

/**
 * This class represents the game score menu which allows the user to choose between 3 different ranking 
 * options : Personal ranking, global ranking and our highest scores.
 * @author Yahav Karpel & Daniel Korotine
 */

public class MyScoresGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private final int YAHAV = 312437080;
	private final int DANIEL = 337938658;
	private final int X_RANGE = 800;
	private final int Y_RANGE = 600;
	
	// Create table
	private JTable scoresTable;
	Object[][] gameResults;
	
	/**
	 * Constructor
	 * this method constructs a ranking table based on the chosen ranking option.
	 */
	
	public MyScoresGUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(X_RANGE, Y_RANGE);
        this.setTitle("Game scores");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[] tableColumns = new String[] {"Num", "ID", "Level", "Score", "Moves", "Date"};
		Object[] selectionMenu = {"Personal ranking", "Global ranking", "Our high scores"};
		Object usersChoice = JOptionPane.showInputDialog(null, "Choose",
				"Main menu", JOptionPane.QUESTION_MESSAGE, null, selectionMenu, "Personal ranking");
		
		if (usersChoice == "Personal ranking") {
			gameResults = SimpleDB.printUserLog(YAHAV, DANIEL);
			scoresTable = new JTable(gameResults, tableColumns);
			this.add(new JScrollPane(scoresTable));
		}
		
		else if (usersChoice == "Global ranking") {
			Object[] stageSelection = {"0", "1", "3", "5", "9", "11", "13", "16", "19", "20", "23"};
			String initialStage = "0";
			Object selectedStage = JOptionPane.showInputDialog(null, "Choose",
					"Main menu", JOptionPane.QUESTION_MESSAGE, null, stageSelection, initialStage);
			
			ArrayList<Triple> arr = SimpleDB.getRankingForLevel(Integer.parseInt((String) selectedStage));
			JOptionPane.showMessageDialog(this, "Ranking in class for level " + selectedStage + " : " + SimpleDB.ourRanking(arr, YAHAV, DANIEL, Integer.parseInt((String)selectedStage)));
		}
		
		else if (usersChoice == "Our high scores") {
			ArrayList<Integer> arr = SimpleDB.getHighscores(YAHAV, DANIEL);
			String[] ourHighScoresColumns = new String[] {"Level", "Score"};
			Object[][] arrObj = {{0, arr.get(0)},
					             {1, arr.get(1)},
				                 {3, arr.get(2)},
				                 {5, arr.get(3)},
				                 {9, arr.get(4)},
				                 {11, arr.get(5)},
				                 {13, arr.get(6)},
				                 {16, arr.get(7)},
				                 {19, arr.get(8)},
				                 {20, arr.get(9)},
				                 {23, arr.get(10)}};
			
			scoresTable = new JTable(arrObj, ourHighScoresColumns);
			this.add(new JScrollPane(scoresTable));
		}

		this.pack();
		this.setLocationRelativeTo(null);
	}
}
