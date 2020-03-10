package gameGUI;
import javax.swing.JOptionPane;

public class MainClass {

	public static void main(String[] args) {
		Object[] selectionMenu = {"Play", "Ranking"};
		Object usersChoice = JOptionPane.showInputDialog(null, "Choose",
				"Main menu", JOptionPane.QUESTION_MESSAGE, null, selectionMenu, "Play");
		
		if (usersChoice == "Play") MyGameGUI.createJFrame();
		else {
			MyScoresGUI scoresGui = new MyScoresGUI();
			scoresGui.setVisible(true);
		}
	}
}