package algorithms;
import static org.junit.jupiter.api.Assertions.*;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.node_data;

class Graph_AlgoTest {

	@BeforeClass
	void testGraph_Algo() {
		Graph_Algo newGraph = new Graph_Algo();
		DGraph dGraph = (DGraph)newGraph.copy();
		assertEquals(0, dGraph.nodeSize());
		assertEquals(0, dGraph.edgeSize());
	}
	
	@Before
	void testGraph_AlgoDGraph() {
		DGraph dG = new DGraph();
		dG.addNode(new Node(1));
		dG.addNode(new Node(2));
		dG.addNode(new Node(3));
		dG.addNode(new Node(4));
		dG.connect(1,2,1);
		dG.connect(1,3,1);
		dG.connect(1,4,1);
		dG.connect(2,3,2);
		dG.connect(3,2,3);
		dG.connect(2,4,2);
		dG.connect(3,4,3);
		
		Graph_Algo newGraph = new Graph_Algo();
		newGraph.init(dG);  // Initialize newGraph using dG
		DGraph dGraph = (DGraph)newGraph.copy();
		assertEquals(4, dGraph.nodeSize());
		assertEquals(7, dGraph.edgeSize());
	}
	
	@Test
	void testInitGraph() {
		DGraph dG = new DGraph();
		dG.addNode(new Node(1));
		dG.addNode(new Node(2));
		dG.addNode(new Node(3));
		dG.addNode(new Node(4));
		dG.connect(1,2,1);
		dG.connect(1,3,1);
		dG.connect(1,4,1);
		dG.connect(2,3,2);
		dG.connect(3,2,3);
		dG.connect(2,4,2);
		dG.connect(3,4,3);
		
		Graph_Algo newGraph = new Graph_Algo();
		newGraph.init(dG); // Initialize newGraph using dG
		DGraph dGraph = (DGraph)newGraph.copy();
		assertEquals(4, dGraph.nodeSize());
		assertEquals(7, dGraph.edgeSize());
	}

	/*@Test
	void testInitString() {
		fail("Not yet implemented");
	}
	@Test
	void testSave() {
		fail("Not yet implemented");
	}*/

	@Test
	void testIsConnected() {
		DGraph dG = new DGraph();
		Graph_Algo newGraph = new Graph_Algo();
		dG.addNode(new Node(0));
		dG.addNode(new Node(1));
		dG.addNode(new Node(2));
		dG.addNode(new Node(3));
		dG.addNode(new Node(4));
		dG.connect(1,2,2);
		dG.connect(2,3,3);
		dG.connect(3,4,4);
		dG.connect(4,1,1);
		dG.connect(2,4,4);
		dG.connect(4,2,7);
		
		newGraph.init(dG); // Initialize newGraph using dG
		
		// New strongly connected graph has been created
		assertTrue(newGraph.isConnected()); // Check connectivity
		
		dG.removeEdge(2,3); // newGraph is not strongly connected anymore
		assertFalse(newGraph.isConnected()); // Check connectivity
	}

	@Test
	void testShortestPathDist() {
		DGraph dG = new DGraph();
		Graph_Algo newGraph = new Graph_Algo();
		dG.addNode(new Node(1));
		dG.addNode(new Node(2));
		dG.addNode(new Node(3));
		dG.addNode(new Node(4));
		dG.addNode(new Node(5));
		dG.connect(1,2,12);
		dG.connect(1,3,11);
		dG.connect(1,4,3);
		dG.connect(2,5,1);
		dG.connect(3,2,3);
		dG.connect(3,5,2);
		dG.connect(4,1,2);
		dG.connect(4,2,5);
		dG.connect(4,3,5);
		dG.connect(4,5,4);
		dG.connect(5,2,2);
		dG.connect(5,3,1);
		
		newGraph.init(dG); // Initialize newGraph using dG
		assertEquals(8, newGraph.shortestPathDist(1,3));
		assertEquals(Double.POSITIVE_INFINITY, newGraph.shortestPathDist(5,4)); // No path
		
		dG.connect(2,1,1);
		assertEquals(6, newGraph.shortestPathDist(5,4));
	}

	@Test
	void testShortestPath() {
		DGraph dG = new DGraph();
		Graph_Algo newGraph = new Graph_Algo();
		dG.addNode(new Node(1));
		dG.addNode(new Node(2));
		dG.addNode(new Node(3));
		dG.addNode(new Node(4));
		dG.addNode(new Node(5));
		dG.connect(1,2,12);
		dG.connect(1,3,11);
		dG.connect(1,4,3);
		dG.connect(2,1,1);
		dG.connect(2,5,1);
		dG.connect(3,2,3);
		dG.connect(3,5,2);
		dG.connect(4,1,2);
		dG.connect(4,2,5);
		dG.connect(4,3,5);
		dG.connect(4,5,4);
		dG.connect(5,2,2);
		dG.connect(5,3,1);
		
		newGraph.init(dG); // Initialize newGraph using dG
		LinkedList<node_data> pathNodes = (LinkedList<node_data>)newGraph.shortestPath(5,4);
		assertEquals(5, pathNodes.get(0).getKey());
		assertEquals(2, pathNodes.get(1).getKey());
		assertEquals(1, pathNodes.get(2).getKey());
		assertEquals(4, pathNodes.get(3).getKey());
		
		dG.removeEdge(2,1); // No path
		pathNodes = (LinkedList<node_data>)newGraph.shortestPath(5,4);
		assertEquals(null, pathNodes);
	}

	@Test
	void testTSP() {
		DGraph dG = new DGraph();
		Graph_Algo newGraph = new Graph_Algo();
		dG.addNode(new Node(0));
		dG.addNode(new Node(1));
		dG.addNode(new Node(2));
		dG.addNode(new Node(3));
		dG.addNode(new Node(4));
		dG.connect(0,1,1);
		dG.connect(1,2,2);
		dG.connect(2,3,3);
		dG.connect(3,0,0);
		dG.connect(2,4,4);
		dG.connect(4,2,7);
		
		newGraph.init(dG); // Initialize newGraph using dG
		assertTrue(newGraph.isConnected());
		LinkedList<Integer> targetList = new LinkedList<Integer>();
		targetList.add(0);
		targetList.add(4);
		targetList.add(3); // 1 -> 2 -> 3 -> 5 -> 3 -> 4
		
		LinkedList<node_data> tspList = (LinkedList<node_data>)newGraph.TSP(targetList);
		assertEquals(0, tspList.get(0).getKey());
		assertEquals(1, tspList.get(1).getKey());
		assertEquals(2, tspList.get(2).getKey());
		assertEquals(4, tspList.get(3).getKey());
		assertEquals(2, tspList.get(4).getKey());
		assertEquals(3, tspList.get(5).getKey());
	}

	@Test
	void testCopy() {
		DGraph dG = new DGraph();
		Graph_Algo newGraph = new Graph_Algo();
		dG.addNode(new Node(1));
		dG.addNode(new Node(2));
		dG.addNode(new Node(3));
		dG.addNode(new Node(4));
		dG.addNode(new Node(5));
		dG.connect(1,2,12);
		dG.connect(1,3,11);
		dG.connect(1,4,3);
		dG.connect(2,5,1);
		dG.connect(3,2,3);
		dG.connect(3,5,2);
		dG.connect(4,1,2);
		dG.connect(4,2,5);
		dG.connect(4,3,5);
		dG.connect(4,5,4);
		dG.connect(5,2,2);
		dG.connect(5,3,1);
		
		newGraph.init(dG); // Initialize newGraph using dG
		DGraph dGraph = (DGraph)newGraph.copy();
		assertEquals(12, dGraph.edgeSize());
		dG.connect(1,5,6);
		assertNotEquals(13, dGraph.edgeSize()); // Deep copy has been made
	}
}