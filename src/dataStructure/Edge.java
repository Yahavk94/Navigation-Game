package dataStructure;
import java.io.Serializable;

import gameData.Fruit;

public class Edge implements edge_data, Serializable {
	private int srcNode;
	private int destNode;
	private double edgeWeight;
	private String edgeInfo;
	private int edgeTag;
	private Fruit fruit;
	private boolean hasFruit;
	
	public Edge() {}
	
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
	
	public boolean getHasFruit() {
		return this.hasFruit;
	}
	
	public void setHasFruit(boolean hasFruit) {
		this.hasFruit = hasFruit;
	}
	
	public Fruit getFruit() {
		return this.fruit;
	}
	
	public void setFruit(Fruit fruit) {
		this.fruit = fruit;
	}
}
