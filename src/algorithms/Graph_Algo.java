package algorithms;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;

public class Graph_Algo implements graph_algorithms, Serializable {
	private DGraph graphAlgo;

	public Graph_Algo() {
		this.graphAlgo = new DGraph();
	}

	@Override
	public void init(graph g) {
		this.graphAlgo = (DGraph)g;
	}

	@Override
	public void init(String file_name) {
		try {
			FileInputStream streamIn = new FileInputStream(file_name);
			ObjectInputStream objectInputStream = new ObjectInputStream(streamIn);
			this.graphAlgo = (DGraph)objectInputStream.readObject();
			objectInputStream.close();
		} catch (Exception e) {
			throw new RuntimeException("File not found");
		}
	}

	@Override
	public void save(String file_name) {
		try {    
			FileOutputStream file = new FileOutputStream(file_name);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(this.graphAlgo);
			out.close();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isConnected() {
		if (this.graphAlgo.nodeSize() == 0) return true; // Base case
		if (this.graphAlgo.nodeSize() == 1) return true; // Base case
		
		int actualSize = this.graphAlgo.nodeSize();
		boolean[] dNodes = new boolean[actualSize + 1]; // 0 might be a node
		
		for (int i = 0; i < dNodes.length; i++)
			dNodes[i] = false; // Initialize as not visited

		iterativeDFS(this.graphAlgo, dNodes);
		for (int i = 0; i < actualSize; i++)
			if (!dNodes[i]) return false; // Check connectivity

		DGraph tGraph = getTranspose();
		for (int i = 0; i < dNodes.length; i++)
			dNodes[i] = false; // Initialize as not visited

		iterativeDFS(tGraph, dNodes);
		for (int i = 0; i < actualSize; i++)
			if (!dNodes[i]) return false; // Check (transpose) connectivity

		return true;
	}

	@Override
	public double shortestPathDist(int src, int dest) { // Dijkstra
		node_data startNode = this.graphAlgo.getNode(src);
		startNode.setWeight(0);
		startNode.setInfo("Not visited");
		Collection<node_data> nodesCol = this.graphAlgo.getV();
		PriorityQueue<node_data> gN = new PriorityQueue<>();

		for (node_data graphNode : nodesCol) { // Initialize parameters
			if (graphNode.getKey() != src) {
				graphNode.setWeight(Double.POSITIVE_INFINITY);
				graphNode.setInfo("Not visited");
				graphNode.setTag(-1);
			}
		}

		gN.add(startNode); // gN (Priority queue) contains startNode
		
		while (!gN.isEmpty()) {
			node_data anyNode = gN.remove();
			anyNode.setInfo("Visited");
			Collection<edge_data> edgesCol = this.graphAlgo.getE(anyNode.getKey()); // Adjacency nodes
			if (edgesCol != null) {
				for (edge_data graphEdge : edgesCol) {
					node_data nNode = this.graphAlgo.getNode(graphEdge.getDest()); // Destination node
					if (anyNode.getWeight() + graphEdge.getWeight() < nNode.getWeight()) {
						nNode.setWeight(anyNode.getWeight() + graphEdge.getWeight()); // Relaxation
						nNode.setTag(anyNode.getKey());
						gN.add(nNode);
					}
				}
			}
		}

		return this.graphAlgo.getNode(dest).getWeight();
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		if (this.shortestPathDist(src, dest) == Double.POSITIVE_INFINITY) // Check path nonexistence
			return null;

		List<node_data> pathNodes = new LinkedList<>();
		node_data currentNode = this.graphAlgo.getNode(dest);

		while (currentNode.getKey() != src) { // Build a destination -> source path using currentNode's predecessor
			pathNodes.add(currentNode);
			currentNode = this.graphAlgo.getNode(currentNode.getTag());
		}

		pathNodes.add(currentNode);
		List<node_data> newPathNodes = new LinkedList<>();
		int i = 1;

		while (i <= pathNodes.size()) { // Change path to source -> destination
			newPathNodes.add(pathNodes.get(pathNodes.size() - i));
			i++;
		}

		return newPathNodes;
	}

	@Override
	public List<node_data> TSP(List<Integer> targets) {
		if (targets.isEmpty()) return null; // Target list is empty
		
		List<node_data> nodesPath = new LinkedList<>();
		int i = 0;
		int srcNode = targets.get(i++);

		if (targets.size() == 1) { // Target list contains 1 element
			nodesPath.add(this.graphAlgo.getNode(srcNode));
			return nodesPath;
		}

		while (i < targets.size()) { // Target list contains more than 1 element
			int destNode = targets.get(i++);
			if (shortestPath(srcNode, destNode) == null) return null; // No path
			
			List<node_data> newPath = (List<node_data>)shortestPath(srcNode, destNode);
			if (i != 2) // Remove newPath's srcNode to avoid duplications
				newPath.remove(newPath.get(0));
			nodesPath.addAll(newPath); // Add newPath
			srcNode = destNode;
		}

		return nodesPath;
	}

	@Override
	public graph copy() {
		DGraph dGraph = new DGraph();
		Collection<node_data> nodesCol = this.graphAlgo.getV();
		for (node_data nD : nodesCol) { // Initialize nodes
			dGraph.addNode(nD);
			if (this.graphAlgo.getE(nD.getKey()) != null) { // Specified node must contain at least one edge
				Collection<edge_data> edgesCol = this.graphAlgo.getE(nD.getKey());
				for (edge_data eD : edgesCol) { // Connect (edge) source -> destination
					dGraph.addNode(this.graphAlgo.getNode(eD.getDest()));
					dGraph.connect(eD.getSrc(), eD.getDest(), eD.getWeight());
				}
			}
		}

		return dGraph;
	}

	/* -------------------- Private functions -------------------- */

	private void iterativeDFS(DGraph dGraph, boolean[] dNodes) {
		Stack<Integer> nodeStack = new Stack<>();
		nodeStack.push(0); // Push the source node into the stack

		while (!nodeStack.isEmpty()) {
			int startNode = nodeStack.pop(); // Pop a vertex from stack

			if (dNodes[startNode]) continue; // Ignore if already discovered
			dNodes[startNode] = true; // startNode has just discovered

			Collection<edge_data> edgesCol = dGraph.getE(startNode);
			if (edgesCol != null) {
				for (edge_data eD : edgesCol) {
					int otherNode = eD.getDest();
					if (!dNodes[otherNode])
						nodeStack.push(otherNode);
				}
			}
		}
	}

	private DGraph getTranspose() {
		DGraph tGraph = new DGraph();
		Collection<node_data> nodesCol = this.graphAlgo.getV();
		for (node_data nD : nodesCol) { // Initialize nodes
			tGraph.addNode(nD);
			if (this.graphAlgo.getE(nD.getKey()) != null) { // Specified node must contain at least one edge
				Collection<edge_data> edgesCol = this.graphAlgo.getE(nD.getKey());
				for (edge_data eD : edgesCol) { // Connect (edge) source -> destination
					tGraph.addNode(this.graphAlgo.getNode(eD.getDest()));
					tGraph.connect(eD.getDest(), eD.getSrc(), eD.getWeight());
				}
			}
		}

		return tGraph;
	}
}
