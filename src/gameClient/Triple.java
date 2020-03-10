package gameClient;

/**
 * This class represents a simple data structure with 3 integers as fields : ID of student, score of student and
 * moves.
 * This class serves the single purposes of retrieving a ranking list from the data base.
 * @author Daniel Korotine and Yahav Karpel.
 */

public class Triple implements Comparable<Triple>{
	private int ID;
	private int score;
	private int moves;
	
	/**
	 * Constructor
	 * @param ID of student
	 * @param score of same student
	 * @param moves of same student to get the score
	 */
	
	public Triple(int ID, int score, int moves) {
		this.ID = ID;
		this.score = score;
		this.moves = moves;
	}

	/**
	 * Retrieves ID of student.
	 * @return
	 */
	
	public int getID() {
		return this.ID;
	}
	
	/**
	 * Set ID (shouldn't be used).
	 * @param ID of student
	 */
	
	public void setID(int iD) {
		ID = iD;
	}

	/**
	 * Retrieves score.
	 * @return score
	 */
	
	public int getScore() {
		return this.score;
	}

	/**
	 * Sets the score (shouldn't be used).
	 * @param score
	 */
	
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * For sorting purposes later on, to sort the ranking list.
	 */
	
	@Override
	public int compareTo(Triple o) {
		if (this.getScore() < o.getScore())
			return 1;
		else if (this.getScore() > o.getScore()) return -1;
		return 0;
	}
	
	/**
	 * Override of tostring function.
	 */
	
	public String toString() {
		return "ID : " + this.getID() + ", score : " + this.getScore() + ", moves : " + this.getMoves();
	}
	
	/**
	 * Retrieves amount of moves
	 * @return amount of moves
	 */
	
	public int getMoves() {
		return this.moves;
	}
}
