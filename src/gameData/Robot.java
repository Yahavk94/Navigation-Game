package gameData;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

/**
 * This class represents the robot, the character that collects the fruits in the game.
 * @author Daniel Korotine & Yahav Karpel
 */

public class Robot {
	private int robotId;
	private double robotValue;
	private int srcNode;
	private int destNode;
	private double robotSpeed;
	private Point3D robotPos;
	
	/**
	 * Constructor
	 * Builds a robot with JSON string that is retrieved from Game_Server object (from jar).
	 * @param JSONString
	 */
	
	public Robot(String JSONString) {
		JSONString = JSONString.substring(9, JSONString.length() - 1);
		JSONObject jsonRobot;
		String rPos;
		try {
			jsonRobot = new JSONObject(JSONString);
			this.robotId = jsonRobot.getInt("id");
			this.robotValue = jsonRobot.getDouble("value");
			this.srcNode = jsonRobot.getInt("src");
			this.destNode = jsonRobot.getInt("dest");
			this.robotSpeed = jsonRobot.getDouble("speed");
			rPos = jsonRobot.getString("pos");
			String[] posArray = rPos.split(",");
			double x = Double.parseDouble(posArray[0]);
			double y = Double.parseDouble(posArray[1]);
			double z = Double.parseDouble(posArray[2]);
			this.robotPos = new Point3D(x, y, z);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public int getId() {
		return this.robotId;
	}
	
	public void setId(int robotId) {
		this.robotId = robotId;
	}
	
	public double getValue() {
		return this.robotValue;
	}
	
	public void setValue(double robotValue) {
		this.robotValue = robotValue;
	}
	
	public int getSrcNode() {
		return this.srcNode;
	}
	
	public void setSrcNode(int srcNode) {
		this.srcNode = srcNode;
	}
	
	public int getDestNode() {
		return this.destNode;
	}
	
	public void setDestNode(int destNode) {
		this.destNode = destNode;
	}
	
	public double getSpeed() {
		return this.robotSpeed;
	}
	
	public void setSpeed(double robotSpeed) {
		this.robotSpeed = robotSpeed;
	}
	
	public Point3D getPos() {
		return this.robotPos;
	}
	
	public void setPos(Point3D robotPos) {
		this.robotPos = robotPos;
	}
}
