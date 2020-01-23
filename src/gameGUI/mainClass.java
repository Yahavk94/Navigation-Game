package gameGUI;

import javax.swing.JOptionPane;

public class MainClass {

	public static void main(String[] args) {
		Object[] selectionMenu = {"Play", "Ranking"};
		String initialChoice = "Play";
		Object selectedChoice = JOptionPane.showInputDialog(null, "Choose",
				"Main menu", JOptionPane.QUESTION_MESSAGE, null, selectionMenu, initialChoice);
		
		if (selectedChoice == "Play") MyGameGUI.createJFrame();
		else {
			MyScoresGUI scoresGui = new MyScoresGUI();
			scoresGui.setVisible(true);
		}
	}
}
