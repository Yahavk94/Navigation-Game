package dataStructure;
import java.io.Serializable;
import utils.Point3D;

/**
 * This class represents a node of a directed graph by it's key, location, weight, info and tag.
 * @author Yahav Karpel and Daniel Korotine.
 */

public class Node implements node_data, Serializable, Comparable<Object> {
	private static final long serialVersionUID = 1L;
	private int nodeKey;
	private Point3D nodeLocation;
	private double nodeWeight;
	private String nodeInfo;
	private int nodeTag;
	
	/**
	 * Constructor
	 * This method constructs a node by a given key.
	 * @param nodeKey
	 */
	
	public Node(int nodeKey) {
		this.nodeKey = nodeKey;
	}
	
	/**
	 * Constructor
	 * This method constructs a node by a given key and x and y parameters to set it's location.
	 * @param nodeKey
	 * @param nodeWeight
	 * @param x
	 * @param y
	 */
	
	public Node(int nodeKey, double nodeWeight, double x, double y) {
		this.nodeKey = nodeKey;
		this.nodeWeight = nodeWeight;
		Point3D p3D = new Point3D(x,y);
		this.nodeLocation = p3D;
	}
	
	/**
	 * Constructor
	 * This method constructs a node by a given key and a Point3D object to set it's location.
	 * @param nodeKey
	 * @param nodeLocation
	 */
	
	public Node(int nodeKey, Point3D nodeLocation) {
		this.nodeKey = nodeKey;
		this.nodeLocation = nodeLocation;
	}
	
	/**
	 * Constructor
	 * This method constructs a node by a given key, weight and a Point3D object to set it's location.
	 * @param nodeKey
	 * @param nodeWeight
	 * @param nodeLocation
	 */
	
	public Node(int nodeKey, double nodeWeight, Point3D nodeLocation) {
		this.nodeKey = nodeKey;
		this.nodeWeight = nodeWeight;
		this.nodeLocation = nodeLocation;
	}

	@Override
	public int getKey() {
		return this.nodeKey;
	}

	@Override
	public Point3D getLocation() {
		return this.nodeLocation;
	}

	@Override
	public void setLocation(Point3D p) {
		this.nodeLocation = new Point3D(p);
	}

	@Override
	public double getWeight() {
		return this.nodeWeight;
	}

	@Override
	public void setWeight(double w) {
		this.nodeWeight = w;
	}

	@Override
	public String getInfo() {
		return this.nodeInfo;
	}

	@Override
	public void setInfo(String s) {
		this.nodeInfo = s;
	}

	@Override
	public int getTag() {
		return this.nodeTag;
	}

	@Override
	public void setTag(int t) {
		this.nodeTag = t;
	}
	
	/**
	 * (Later on) Using priority queue in Dijkstra's algorithm (shortestPathDist) to compare between the 
	 * elements in it.
	 */

	@Override
	public int compareTo(Object o) {
		if (this.getWeight() > ((Node)o).getWeight())
			return 1;		
		return 0;
	}
}
