package gameGUI;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import gameClient.SimpleDB;
import gameClient.Triple;

public class MyScoresGUI extends JFrame {
	private final int YAHAV = 312437080;
	private final int DANIEL = 337938658;
	private final int X_RANGE = 800;
	private final int Y_RANGE = 600;
	
	// Create table with data
	private JTable table;
	
	Object[][] results;
	
	public MyScoresGUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(X_RANGE, Y_RANGE);
		
        // Headers for the table
        String[] columns = new String[] {
            "Num", "Id", "Level", "Score", "Moves", "Date"
        };     
         
        this.setTitle("Table Example");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
		
		Object[] selectionMenu = {"Personal ranking", "Global ranking", "Our high scores"};
		String initialChoice = "Personal ranking";
		Object selectedChoice = JOptionPane.showInputDialog(null, "Choose",
				"Main menu", JOptionPane.QUESTION_MESSAGE, null, selectionMenu, initialChoice);
		
		if (selectedChoice == "Personal ranking") {
			results = SimpleDB.printUserLog(YAHAV, DANIEL);
			table = new JTable(results, columns);
			this.add(new JScrollPane(table));
		}
		
		else if (selectedChoice == "Our high scores") {
			ArrayList<Integer> arr = SimpleDB.getHighscores(YAHAV, DANIEL);
			String[] columns2 = new String[] {
		            "Level", "Score"
		        };     
			
			Object[][] arrObj = {
				{0, arr.get(0)},
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
			
			
			
			table = new JTable(arrObj, columns2);
			this.add(new JScrollPane(table));
		}
		
		else if (selectedChoice == "Global ranking") {
			Object[] stageSelection = {"0", "1", "3", "5", "9", "11", "13", "16", "19", "20", "23"};
			String initialStage = "0";
			Object selectedStage = JOptionPane.showInputDialog(null, "Choose",
					"Main menu", JOptionPane.QUESTION_MESSAGE, null, stageSelection, initialStage);
			
			
			ArrayList<Triple> arr = SimpleDB.getRankingForLevel(Integer.parseInt((String) selectedStage));
			JOptionPane.showMessageDialog(this, "Ranking in class for level " + selectedStage + " : " +SimpleDB.ourRanking(arr, YAHAV, DANIEL, Integer.parseInt((String) selectedStage)));
			
		}

		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
