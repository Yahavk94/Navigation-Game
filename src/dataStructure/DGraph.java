package dataStructure;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gameData.Fruit;
import utils.Point3D;

public class DGraph implements graph, Serializable {
	private HashMap<Integer, node_data> gNode;
	private HashMap<Integer, HashMap<Integer, edge_data>> gEdge;

	private int numNodes = 0;
	private int numEdges = 0;
	private int MC = 0;

	public DGraph() {
		this.gNode = new HashMap<Integer, node_data>();
		this.gEdge = new HashMap<Integer, HashMap<Integer, edge_data>>();
	}

	@Override
	public node_data getNode(int key) {
		return this.gNode.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		if (this.gEdge.containsKey(src)) return this.gEdge.get(src).get(dest);
		return null;
	}

	@Override
	public void addNode(node_data n) {
		if (!this.gNode.containsKey(n.getKey())) {
			this.gNode.put(n.getKey(), n);
			this.numNodes++;
			this.MC++;
		}
	}

	@Override
	public void connect(int src, int dest, double w) {
		if (this.gNode.containsKey(src) && this.gNode.containsKey(dest)) { // Check nodes existence
			if (getEdge(src, dest) == null) {
				if (!this.gEdge.containsKey(src)) { // Source node contains no edges
					this.gEdge.put(src, new HashMap<Integer, edge_data>());
					this.gEdge.get(src).put(dest, new Edge(src, dest, w));
				}

				else // Connect (edge) source -> destination
					this.gEdge.get(src).put(dest, new Edge(src, dest, w));
				
				this.numEdges++;
				this.MC++;
			}
		}

		else
			throw new RuntimeException("ERR! Unable to connect since nodes are nonexist");
	}

	@Override
	public Collection<node_data> getV() {
		return this.gNode.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		if (this.gEdge.containsKey(node_id)) return this.gEdge.get(node_id).values();
		return null;
	}

	@Override
	public node_data removeNode(int key) {
		if (this.gNode.containsKey(key)) { // Check specified node existence
			Map<Integer, node_data> map = gNode;
			for (int newKey : map.keySet()) { // Check each node.
	            node_data newNode = map.get(newKey);
	            if (this.getEdge(key, newNode.getKey()) != null)
	            	removeEdge(key, newNode.getKey()); // Remove (edge) specified node -> destination
	            
	            if (this.getEdge(newNode.getKey(), key) != null)
	            	removeEdge(newNode.getKey(), key); // Remove (edge) source -> specified node
	        }
			
			this.gNode.remove(key); // Remove specified node
			this.numNodes--;
			this.MC++;
		}
		
		return null; // Specified node doesn't exist
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		if (this.gEdge.containsKey(src)) {
			if (this.gEdge.get(src).containsKey(dest)) { // Check specified edge existence
				this.numEdges--;
				this.MC++;
				return this.gEdge.get(src).remove(dest);
			}
		}
		
		return null;
	}

	@Override
	public int nodeSize() {
		return this.numNodes;
	}

	@Override
	public int edgeSize() {
		return this.numEdges;
	}

	@Override
	public int getMC() {
		return this.MC;
	}
	
	/**
	 * Add fruit to the edge if contains any fruit
	 * @param myFruit
	 */
	
	/**
	 * Init DGraph from JSON string
	 * @param JSONString
	 */
	
	public void init(String JSONString) {
		JSONObject jsonDGraph;
		String gPos;
		try {
			jsonDGraph = new JSONObject(JSONString);
			JSONArray jsonEdges = jsonDGraph.getJSONArray("Edges");
			JSONArray jsonNodes = jsonDGraph.getJSONArray("Nodes");
			
			for (int i = 0; i < jsonNodes.length(); i++) {
				JSONObject jsonNode = ((JSONObject)jsonNodes.get(i));
				gPos = jsonNode.getString("pos");
				String[] posArray = gPos.split(",");
				double x = Double.parseDouble(posArray[0]);
				double y = Double.parseDouble(posArray[1]);
				double z = Double.parseDouble(posArray[2]);
				Point3D nodeLocation = new Point3D(x, y, z);
				node_data nD = new Node(jsonNode.getInt("id"), nodeLocation);
				this.addNode(nD);
			}
			
			for (int i = 0; i < jsonEdges.length(); i++) {
				int srcNode = ((JSONObject)jsonEdges.get(i)).getInt("src");
				int destNode = ((JSONObject)jsonEdges.get(i)).getInt("dest");
				double edgeWeight = ((JSONObject)jsonEdges.get(i)).getDouble("w");
				this.connect(srcNode, destNode, edgeWeight);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
