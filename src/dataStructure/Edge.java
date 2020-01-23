package dataStructure;
import java.io.Serializable;

/**
 * This class represents an edge of a directed graph by it's source node, destination node, weight, info and tag.
 * @author Yahav Karpel and Daniel Korotine.
 */

public class Edge implements edge_data, Serializable {
	private static final long serialVersionUID = 1L;
	private int srcNode;
	private int destNode;
	private double edgeWeight;
	private String edgeInfo;
	private int edgeTag;
	
	/**
	 * Constructor
	 * This method constructs an edge by a given source node, destination node and weight.
	 */
	
	public Edge(int srcNode, int destNode, double edgeWeight) {
		this.srcNode = srcNode;
		this.destNode = destNode;
		this.edgeWeight = edgeWeight;
	}

	@Override
	public int getSrc() {
		return this.srcNode;
	}

	@Override
	public int getDest() {
		return this.destNode;
	}

	@Override
	public double getWeight() {
		return this.edgeWeight;
	}

	@Override
	public String getInfo() {
		return this.edgeInfo;
	}

	@Override
	public void setInfo(String s) {
		this.edgeInfo = s;
	}

	@Override
	public int getTag() {
		return this.edgeTag;
	}

	@Override
	public void setTag(int t) {
		this.edgeTag = t;
	}
}
