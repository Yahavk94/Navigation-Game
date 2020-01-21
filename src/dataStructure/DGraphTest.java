package dataStructure;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;
import java.util.Iterator;
import org.junit.jupiter.api.Test;

class DGraphTest {
	
	@Test
	void testDGraph() {
		DGraph dG = new DGraph();
		assertEquals(0, dG.nodeSize()); // dG contains no nodes
		assertEquals(0, dG.edgeSize()); // dG contains no edges
	}

	@Test
	void testGetNode() {
		DGraph dG = new DGraph();
		node_data newNode = new Node(1);
		dG.addNode(newNode);
		
		assertEquals(newNode, dG.getNode(1));
		assertEquals(1, dG.nodeSize()); // dG contains 1 node
	}

	@Test
	void testGetEdge() {
		DGraph dG = new DGraph();
		dG.addNode(new Node(1));
		dG.addNode(new Node(2));
		dG.addNode(new Node(3));
		dG.addNode(new Node(4));
		dG.connect(1,2,3);
		dG.connect(3,4,5);
		dG.connect(2,4,10);
		
		assertEquals(3, dG.getEdge(1,2).getWeight());
		assertEquals(5, dG.getEdge(3,4).getWeight());
		assertEquals(10, dG.getEdge(2,4).getWeight());
		assertEquals(3, dG.edgeSize()); // dG contains 3 edges
		
		assertEquals(null, dG.getEdge(1,4));
	}
	
	@Test
	void testAddNode() {
		DGraph dG = new DGraph();
		node_data newNode1 = new Node(1);
		node_data newNode2 = new Node(2);
		dG.addNode(newNode1);
		dG.addNode(newNode2);

		assertEquals(2, dG.nodeSize()); // dG contains 2 nodes
	}

	@Test
	void testConnect() {
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
		
		for (int i = 1; i < 4; i++)
			for (int j = i + 1; j < 4; j++)
				assertEquals(i, dG.getEdge(i,j).getWeight());
		
		assertEquals(null, dG.getEdge(2,1));
	}

	@Test
	void testGetV() {
		DGraph dG = new DGraph();
		dG.addNode(new Node(1));
		dG.addNode(new Node(2));
		dG.addNode(new Node(3));
		dG.addNode(new Node(4));
		
		Collection<node_data> nodesCol = dG.getV();
		Iterator<node_data> nodesIter = nodesCol.iterator();
		int nodesCounter = 0;
		
		while (nodesIter.hasNext()) {
			node_data nD = nodesIter.next();
			if (dG.getNode(nD.getKey()) != null) nodesCounter++;
		}
		
		assertEquals(nodesCounter, dG.nodeSize());
	}

	@Test
	void testGetE() {
		DGraph dG = new DGraph();
		dG.addNode(new Node(1));
		dG.addNode(new Node(2));
		dG.addNode(new Node(3));
		dG.addNode(new Node(4));
		dG.addNode(new Node(5));
		dG.addNode(new Node(6));
		dG.connect(1,2,7);
		dG.connect(1,4,11);
		dG.connect(1,6,14);
		dG.connect(2,3,10);
		dG.connect(3,1,9);
		dG.connect(3,4,11);
		dG.connect(4,2,15);
		dG.connect(5,4,5);
		dG.connect(5,6,9);
		dG.connect(6,5,9);
		dG.connect(6,4,3);
		
		Collection<node_data> nodesCol = dG.getV();
		Iterator<node_data> nodesIter = nodesCol.iterator();
		int edgesCounter = 0;
		
		while (nodesIter.hasNext()) {
			node_data nD = nodesIter.next();
			Collection<edge_data> edgesCol = dG.getE(nD.getKey());
			Iterator<edge_data> edgesIter = edgesCol.iterator();
			while (edgesIter.hasNext()) {
				edge_data eD = edgesIter.next();
				if (dG.getEdge(nD.getKey(), eD.getDest()) != null) edgesCounter++;
			}
		}
		
		assertEquals(edgesCounter, dG.edgeSize());
	}

	@Test
	void testRemoveNode() {
		DGraph dG = new DGraph();
		dG.addNode(new Node(1));
		dG.addNode(new Node(2));
		dG.addNode(new Node(3));
		dG.addNode(new Node(4));
		dG.addNode(new Node(5));
		dG.connect(1,3,1);
		dG.connect(2,3,2);
		dG.connect(3,4,4);
		dG.connect(3,5,5);
		
		assertEquals(5, dG.nodeSize());
		assertEquals(4, dG.edgeSize());
		
		dG.removeNode(3);
		assertEquals(4, dG.nodeSize());
		assertEquals(0, dG.edgeSize());
	}

	@Test
	void testRemoveEdge() {
		DGraph dG = new DGraph();
		dG.addNode(new Node(1));
		dG.addNode(new Node(2));
		dG.addNode(new Node(3));
		dG.addNode(new Node(4));
		dG.addNode(new Node(5));
		dG.connect(1,3,12);
		dG.connect(3,1,34);
		dG.connect(2,3,2);
		dG.connect(3,2,12);
		dG.connect(3,4,4);
		dG.connect(4,3,41);
		dG.connect(3,5,5);
		dG.connect(5,3,15);
		
		dG.removeEdge(1,3);
		assertEquals(7, dG.edgeSize());
		dG.removeEdge(2,3);
		assertEquals(6, dG.edgeSize());
		dG.removeEdge(4,3);
		assertEquals(5, dG.edgeSize());
		dG.removeEdge(5,3);
		assertEquals(4, dG.edgeSize());
		dG.removeEdge(3,1);
		assertEquals(3, dG.edgeSize());
		dG.removeEdge(3,2);
		assertEquals(2, dG.edgeSize());
		dG.removeEdge(3,4);
		assertEquals(1, dG.edgeSize());
		dG.removeEdge(3,5);
		assertEquals(0, dG.edgeSize());
	}

	@Test
	void testNodeSize() {
		DGraph dG = new DGraph();
		dG.addNode(new Node(1));
		dG.addNode(new Node(2));
		dG.addNode(new Node(3));
		dG.addNode(new Node(4));
		dG.addNode(new Node(5));
		
		assertEquals(5, dG.nodeSize());
		dG.addNode(new Node(1)); // Existed node won't change nodes size
		assertEquals(5, dG.nodeSize());
	}

	@Test
	void testEdgeSize() {
		DGraph dG = new DGraph();
		dG.addNode(new Node(1));
		dG.addNode(new Node(2));
		dG.addNode(new Node(3));
		dG.addNode(new Node(4));
		dG.addNode(new Node(5));
		dG.connect(1,3,12);
		dG.connect(3,1,34);
		dG.connect(2,3,2);
		dG.connect(3,2,12);
		dG.connect(3,4,4);
		dG.connect(4,3,41);
		dG.connect(3,5,5);
		dG.connect(5,3,15);
		
		assertEquals(8, dG.edgeSize());
		dG.connect(1,3,7); // Existed edge (given any weight) won't change edges size
		assertEquals(8, dG.edgeSize());
	}

	@Test
	void testGetMC() {
		DGraph dG = new DGraph();
		dG.addNode(new Node(1));
		dG.addNode(new Node(2));
		dG.addNode(new Node(3));
		dG.addNode(new Node(4));
		dG.addNode(new Node(5));
		dG.connect(1,3,12);
		dG.connect(3,1,34);
		dG.connect(2,3,2);
		dG.connect(3,2,12);
		dG.connect(3,4,4);
		dG.connect(4,3,41);
		dG.connect(3,5,5);
		dG.connect(5,3,15);
		
		assertEquals(13, dG.getMC());
		dG.removeNode(3); // 9 operations have been made
		assertEquals(22, dG.getMC());
	}
}