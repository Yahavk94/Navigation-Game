package gameGUI;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import gameClient.SimpleDB;
import gameClient.Triple;

public class MyScoresGUI extends JFrame {
	private boolean myGameFlag = true;
	private final int YAHAV = 312437080;
	private final int DANIEL = 337938658;
	private final int X_RANGE = 800;
	private final int Y_RANGE = 600;
	
	Object[][] results;
	
	public MyScoresGUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(X_RANGE, Y_RANGE);
		
        //headers for the table
        String[] columns = new String[] {
            "Num", "Id", "Level", "Score", "Moves", "Date"
        };
        
        //create table with data
        JTable table;
         
        //add the table to the frame
        //this.add(new JScrollPane(table));
         
        this.setTitle("Table Example");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        this.pack();
        this.setVisible(true);
		
		Object[] selectionMenu = {"My game stats", "Global ranking"};
		String initialChoice = "My game stats";
		Object selectedChoice = JOptionPane.showInputDialog(null, "Choose",
				"Main menu", JOptionPane.QUESTION_MESSAGE, null, selectionMenu, initialChoice);
		
		if (selectedChoice != "My game stats") myGameFlag = false;
		
		if (myGameFlag) {
			results = SimpleDB.printUserLog(YAHAV, DANIEL, false);
			table = new JTable(results, columns);
			this.add(new JScrollPane(table));
		}
		
		else {
			Object[] stageSelection = {"0", "1", "3", "5", "9", "11", "13", "16", "19", "20", "23"};
			String initialStage = "0";
			Object selectedStage = JOptionPane.showInputDialog(null, "Choose",
					"Main menu", JOptionPane.QUESTION_MESSAGE, null, stageSelection, initialStage);
			
			ArrayList<Triple> arr = SimpleDB.getRankingForLevel(Integer.parseInt((String) selectedStage));
			JOptionPane.showMessageDialog(this, "Ranking in class for level " + selectedStage + " : " +SimpleDB.ourRanking(arr, YAHAV, DANIEL));
		}

		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
