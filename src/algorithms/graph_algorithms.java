package algorithms;
import java.util.List;
import dataStructure.graph;
import dataStructure.node_data;

/**
 * This interface represents the regular Graph Theory algorithms including
 * clone();
 * init(String file_name);
 * save(String file_name);
 * isConnected();
 * double shortestPathDist(int src, int dest);
 * List<Node> shortestPath(int src, int dest);
 * @author boaz.benmoshe
 */

public interface graph_algorithms {

	/**
	 * Init this set of algorithms on the parameter graph.
	 * @param g
	 */
	
	public void init(graph g);

	/** 
	 * Compute a deep copy of this graph.
	 * @return
	 */

	public graph copy();

	/**
	 * Init a graph from file.
	 * @param file_name
	 */

	public void init(String file_name);

	/** Saves the graph to a file.
	 * @param file_name
	 */

	public void save(String file_name);
	
	/**
	 * Returns true if and only ifthere is a valid path from EVREY node to each other node.
	 * NOTE! Assume directional graph.
	 * A valid path (a --> b) does NOT imply a valid path (b --> a).
	 * @return
	 */
	
	public boolean isConnected();
	
	/**
	 * Returns the length of the shortest path between src to dest.
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return
	 */
	
	public double shortestPathDist(int src, int dest);
	
	/**
	 * Returns the the shortest path between src to dest as an ordered List of nodes.
	 * src --> n1 --> n2 --> ... --> dest
	 * https://en.wikipedia.org/wiki/Shortest_path_problem
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return
	 */
	
	public List<node_data> shortestPath(int src, int dest);
	
	/**
	 * Computes a relatively short path which visit each node in the targets List.
	 * NOTE! this is NOT the classical traveling salesman problem, as you can visit a node
	 * more than once, and there is no need to return to source node - just a simple path going
	 * over all nodes in the list. 
	 * @param targets
	 * @return
	 */
	
	public List<node_data> TSP(List<Integer> targets);
}
