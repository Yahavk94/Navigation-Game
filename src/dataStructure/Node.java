package dataStructure;
import java.io.Serializable;

import utils.Point3D;

public class Node implements node_data, Serializable, Comparable<Object> {
	private static final long serialVersionUID = 1L;
	private int nodeKey;
	private Point3D nodeLocation;
	private double nodeWeight;
	private String nodeInfo;
	private int nodeTag;
	
	public Node() {}
	
	public Node(int nodeKey) {
		this.nodeKey = nodeKey;
		double randomNum = Math.random();
		double x = (randomNum + 0.1) * 480;
		randomNum = Math.random();
		double y = (randomNum + 0.3) * 410;
		Point3D p3D = new Point3D(x,y);
		this.nodeLocation = p3D;
	}
	
	public Node(int nodeKey, double nodeWeight, double x, double y) {
		this.nodeKey = nodeKey;
		this.nodeWeight = nodeWeight;
		Point3D p3D = new Point3D(x,y);
		this.nodeLocation = p3D;
	}
	
	public Node(int nodeKey, Point3D nodeLocation) {
		this.nodeKey = nodeKey;
		this.nodeLocation = nodeLocation;
	}
	
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

	@Override
	public int compareTo(Object o) {
		if (this.getWeight() > ((Node)o).getWeight())
			return 1;		
		return 0;
	}
}
