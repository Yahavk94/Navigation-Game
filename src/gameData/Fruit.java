package gameData;
import java.util.Collection;
import org.json.JSONException;
import org.json.JSONObject;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;

/**
 * This class represents the fruit which are collected by the robots in the game.
 * @author Daniel Korotine & Yahav Karpel
 */

public class Fruit {
	private int fruitType;
	private double fruitValue;
	private Point3D fruitPos;
	private edge_data fruitEdge;
	
	/**
	 * Constructor
	 * Builds a fruit with JSON string that is retrieved from Game_Server object (from jar)
	 * and sets its edge based on the given graph.
	 * @param JSONString
	 * @param gameGraph
	 */
	
	public Fruit(String JSONString, DGraph gameGraph) {
		JSONObject getFruit;
		try {
			getFruit = new JSONObject(JSONString);
			JSONObject jsonFruit = getFruit.getJSONObject("Fruit");
			this.fruitValue = jsonFruit.getDouble("value");
			this.fruitType = jsonFruit.getInt("type");
			String[] posArray = jsonFruit.getString("pos").split(",");
			double x = Double.parseDouble(posArray[0]);
			double y = Double.parseDouble(posArray[1]);
			double z = Double.parseDouble(posArray[2]);
			this.fruitPos = new Point3D(x, y, z);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		this.fruitEdge = setEdge(gameGraph);
	}
	
	public int getType() {
		return this.fruitType;
	}

	public void setType(int fruitType) {
		this.fruitType = fruitType;
	}

	public double getValue() {
		return this.fruitValue;
	}

	public void setValue(double fruitValue) {
		this.fruitValue = fruitValue;
	}

	public Point3D getPos() {
		return this.fruitPos;
	}

	public void setPos(Point3D fruitPos) {
		this.fruitPos = fruitPos;
	}
	
	public edge_data getEdge() {
		return this.fruitEdge;
	}
	
	/**
	 * Private function
	 * This private function calculates and returns the edge our fruit is located on.
	 Using distance formula, the fruits is located on that edge if the margin between the fruit and the
	 specified edge is smaller than a very small number (epsilon).
	 * @param gameGraph
	 */
	
	private edge_data setEdge(DGraph gameGraph) {
		double epsilon = 0.000001;
		double edgeDist = 0;
		double srcToFruitDist = 0, fruitToDestDist = 0, fruitDist = 0;
		
		Collection<node_data> nodesCol = gameGraph.getV();
		for (node_data nD : nodesCol) {
			Collection<edge_data> edgesCol = gameGraph.getE(nD.getKey());
			for (edge_data eD : edgesCol) {
				double xDist = nD.getLocation().x() - gameGraph.getNode(eD.getDest()).getLocation().x();
				double yDist = nD.getLocation().y() - gameGraph.getNode(eD.getDest()).getLocation().y();
				edgeDist = Math.sqrt(xDist * xDist + yDist * yDist);
				
				double xSrcToFruit = nD.getLocation().x() - this.getPos().x();
				double ySrcToFruit = nD.getLocation().y() - this.getPos().y();
				double xFruitToDest = this.getPos().x() - gameGraph.getNode(eD.getDest()).getLocation().x();
				double yFruitToDest = this.getPos().y() - gameGraph.getNode(eD.getDest()).getLocation().y();
				srcToFruitDist = Math.sqrt(xSrcToFruit * xSrcToFruit + ySrcToFruit * ySrcToFruit);
				fruitToDestDist = Math.sqrt(xFruitToDest * xFruitToDest + yFruitToDest * yFruitToDest);
				fruitDist = srcToFruitDist + fruitToDestDist;
				
				boolean srcIsSmaller = false;
				if (Math.abs(edgeDist - fruitDist) < epsilon) {
					if (eD.getSrc() < eD.getDest()) srcIsSmaller = true;
					
					if (this.getType() == 1) { // Apple
						if (srcIsSmaller) return eD;
						return gameGraph.getEdge(eD.getDest(), eD.getSrc());
					}
					
					else if (this.getType() == -1) { // Banana
						if (srcIsSmaller) return gameGraph.getEdge(eD.getDest(), eD.getSrc());
						 return eD;
					}
				}
			}
		}
		
		return null;
	}
}
