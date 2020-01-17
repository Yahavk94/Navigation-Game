package gui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileFilter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

public class Graph_GUI extends JFrame implements ActionListener, MouseListener {
	private Graph_Algo myGraph = new Graph_Algo();

	public Graph_GUI(Graph_Algo myGraph) {
		graph dGraph = new DGraph();
		dGraph = myGraph.copy();
		this.myGraph.init(dGraph);
		initGUI();
	}

	private void initGUI() {
		this.setSize(600,600); // Size has been adjusted to my graph
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));

		MenuBar menuBar = new MenuBar();
		Menu menuOption1 = new Menu("File");
		Menu menuOption2 = new Menu("Functions");
		Menu menuOption3 = new Menu("More");

		menuBar.add(menuOption1);
		menuBar.add(menuOption2);
		menuBar.add(menuOption3);
		this.setMenuBar(menuBar);

		// File menu
		MenuItem item1 = new MenuItem("Init");
		item1.addActionListener(this);
		MenuItem item2 = new MenuItem("Save");
		item2.addActionListener(this);

		// Functions menu
		MenuItem item3 = new MenuItem("isConnected");
		item3.addActionListener(this);
		MenuItem item4 = new MenuItem("shortestPathDist");
		item4.addActionListener(this);
		MenuItem item5 = new MenuItem("shortestPath");
		item5.addActionListener(this);
		MenuItem item6 = new MenuItem("TSP");
		item6.addActionListener(this);

		// More menu
		MenuItem item7 = new MenuItem("Add edge");
		item7.addActionListener(this);
		MenuItem item8 = new MenuItem("Remove edge");
		item8.addActionListener(this);
		MenuItem item9 = new MenuItem("Nodes num");
		item9.addActionListener(this);
		MenuItem item10 = new MenuItem("Edges num");
		item10.addActionListener(this);

		menuOption1.add(item1);
		menuOption1.add(item2);
		menuOption2.add(item3);
		menuOption2.add(item4);
		menuOption2.add(item5);
		menuOption2.add(item6);
		menuOption3.add(item7);
		menuOption3.add(item8);
		menuOption3.add(item9);
		menuOption3.add(item10);
		this.addMouseListener(this);
	}

	public void paint(Graphics graphDrawer) {
		super.paint(graphDrawer);
		graph dgraph = this.myGraph.copy();

		Collection<node_data> nodesCol = dgraph.getV();
		for (node_data nD : nodesCol) {
			Point3D pSrc = nD.getLocation();

			// Start black node drawl
			graphDrawer.setColor(Color.BLACK);
			graphDrawer.fillOval(pSrc.ix(), pSrc.iy(), 10, 10);
			String nodeNumber = "";
			nodeNumber += nD.getKey();
			graphDrawer.drawString(nodeNumber, pSrc.ix(), pSrc.iy() - 5);
			// End black node drawl

			Collection<edge_data> edgesCol = dgraph.getE(nD.getKey());
			if (edgesCol != null) {
				for (edge_data eD : edgesCol) {
					node_data destNode = dgraph.getNode(eD.getDest());
					Point3D pDest = destNode.getLocation();

					// Start red edge drawl
					graphDrawer.setColor(Color.RED);
					graphDrawer.drawLine(pSrc.ix() + 5, pSrc.iy() + 5, pDest.ix() + 5, pDest.iy() + 5);
					// End red edge drawl

					// Start dark Gray edge weight drawl
					graphDrawer.setColor(Color.DARK_GRAY);
					String edgeWeight = "";
					edgeWeight += eD.getWeight();
					double xDrawer = (pSrc.x() + pDest.x()) / 2 + 7;
					double yDrawer = (pSrc.y() + pDest.y()) / 2 + 7;
					graphDrawer.drawString(edgeWeight, (int)xDrawer, (int)yDrawer);
					// End dark Gray edge weight drawl

					// Start yellow edge direction drawl
					graphDrawer.setColor(Color.YELLOW);
					double directionX = ((pSrc.x() + 2) * 8 + (pDest.x()) + 2) / 9;
					double directionY = ((pSrc.y() + 2) * 8 + (pDest.y()) + 2) / 9;
					graphDrawer.fillOval((int)directionX, (int)directionY, 10, 10);
					// End yellow edge direction drawl
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String strCommand = e.getActionCommand();
		
		if (strCommand.equals("Init")) { // Load graph by text
			String graphFileName = JOptionPane.showInputDialog("Enter file name");
			try {
				myGraph.init(graphFileName);
				repaint();
			} catch (Exception E) {
				JOptionPane.showMessageDialog(this, "File not found", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		if (strCommand.equals("Save")) { // Save graph by text
			String graphFileName = JOptionPane.showInputDialog("Enter file name");
			try {
				myGraph.save(graphFileName);
				JOptionPane.showMessageDialog(this, "Saved");
			} catch (Exception E) {
				JOptionPane.showMessageDialog(this, "Unable to save", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		if (strCommand.equals("isConnected")) {
			if (myGraph.isConnected())
				JOptionPane.showMessageDialog(this, "The graph is strongly connected");
			else
				JOptionPane.showMessageDialog(this, "The graph is not strongly connected");
		}

		if (strCommand.equals("shortestPathDist")) {
			graph dGraph = new DGraph();
			dGraph = myGraph.copy();
			String srcId = JOptionPane.showInputDialog(this, "Enter source node");
			
			boolean sFlag = true; // Start source node validity check
			try {
				if (dGraph.getNode(Integer.parseInt(srcId)) == null)
					throw new RuntimeException();
			} catch (Exception E) {
				JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
				sFlag = false;
			} // End source node validity check

			if (sFlag) {
				String destId = JOptionPane.showInputDialog(this, "Enter destination node");
				try {
					node_data srcNode = dGraph.getNode(Integer.parseInt(srcId));
					node_data destNode = dGraph.getNode(Integer.parseInt(destId));
					int srcKey = srcNode.getKey();
					int destKey = destNode.getKey();
					double sD = myGraph.shortestPathDist(srcKey, destKey);
					String pathMessage = "The shortest distance between the specified nodes is " +sD;
					JOptionPane.showMessageDialog(this, pathMessage);
				} catch (Exception E) {
					JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		if (strCommand.equals("shortestPath")) {
			graph dGraph = new DGraph();
			dGraph = myGraph.copy();
			String srcId = JOptionPane.showInputDialog(this, "Enter source node");

			boolean sFlag = true; // Start source node validity check
			try {
				if (dGraph.getNode(Integer.parseInt(srcId)) == null)
					throw new RuntimeException();
			} catch (Exception E) {
				JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
				sFlag = false;
			} // End source node validity check

			if (sFlag) {
				String destId = JOptionPane.showInputDialog(this, "Enter destination node");
				try {
					node_data srcNode = dGraph.getNode(Integer.parseInt(srcId));
					node_data destNode = dGraph.getNode(Integer.parseInt(destId));
					int srcKey = srcNode.getKey();
					int destKey = destNode.getKey();
					
					List<node_data> nodesPath = (List<node_data>)myGraph.shortestPath(srcKey, destKey);
					if (nodesPath == null)
						JOptionPane.showMessageDialog(this, "No path between the specified nodes");

					else {
						String pathMessage = "";
						for (int i = 0; i < nodesPath.size(); i++)
							pathMessage += nodesPath.get(i).getKey() + " ";
						JOptionPane.showMessageDialog(this, pathMessage);
					}
				} catch (Exception E) {
					JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		if (strCommand.equals("TSP")) {
			graph dGraph = new DGraph();
			dGraph = myGraph.copy();
			List<Integer> pathList = new LinkedList<>();
			String numOfNodes = JOptionPane.showInputDialog(this, "Enter path length");
			try {
				int numNodes = Integer.parseInt(numOfNodes);
				if (numNodes == 0)
					JOptionPane.showMessageDialog(this, "Empty list hence there is no path");

				else {
					for (int i = 0; i < numNodes; i++) {
						String nodeId = JOptionPane.showInputDialog(this, "Enter node number");
						if (dGraph.getNode(Integer.parseInt(nodeId)) == null) // Invalid input
							throw new RuntimeException();
						pathList.add(Integer.parseInt(nodeId));
					}

					List<node_data> tspList = (List<node_data>)myGraph.TSP(pathList);
					if (tspList == null)
						JOptionPane.showMessageDialog(this, "No path between the specified nodes");

					else {
						String pathMessage = "";
						for (int i = 0; i < tspList.size(); i++)
							pathMessage += tspList.get(i).getKey() + " ";
						JOptionPane.showMessageDialog(this, pathMessage);
					}
				}
			} catch (Exception E) {
				JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		if (strCommand.equals("Add edge")) {
			graph dGraph = new DGraph();
			dGraph = myGraph.copy();
			String srcId = JOptionPane.showInputDialog(this, "Enter source node");
			boolean sFlag = true;
			
			try { // Start source node validity check
				if (dGraph.getNode(Integer.parseInt(srcId)) == null)
					throw new RuntimeException();
			} catch (Exception E) {
				JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
				sFlag = false;
			} // End source node validity check

			if (sFlag) {
				String destId = JOptionPane.showInputDialog(this, "Enter destination node");
				boolean vFlag = true; // Input validity flag
				boolean eFlag = false; // Edge existence flag
				try {
					node_data srcNode = dGraph.getNode(Integer.parseInt(srcId));
					node_data destNode = dGraph.getNode(Integer.parseInt(destId));
					int srcKey = srcNode.getKey();
					int destKey = destNode.getKey();
					if (dGraph.getEdge(srcKey, destKey) != null) {
						eFlag = true; // Edge exists
						JOptionPane.showMessageDialog(this, "There is already an edge between the specified node");
					}
				} catch (Exception E) {
					vFlag = false; // Invalid input
					JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
				}

				if (vFlag && !eFlag) {
					String edgeWeight = JOptionPane.showInputDialog(this, "Enter node's weight");
					try {
						node_data srcNode = dGraph.getNode(Integer.parseInt(srcId));
						node_data destNode = dGraph.getNode(Integer.parseInt(destId));
						int srcKey = srcNode.getKey();
						int destKey = destNode.getKey();
						double wEdge = Double.parseDouble(edgeWeight);

						if (wEdge <= 0) // Invalid weight
							throw new RuntimeException();
						
						dGraph.connect(srcKey, destKey, wEdge);
						myGraph.init(dGraph);
						repaint();
					} catch (Exception E) {
						JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		
		if (strCommand.equals("Remove edge")) {
			graph dGraph = new DGraph();
			dGraph = myGraph.copy();
			String srcId = JOptionPane.showInputDialog(this, "Enter source node");
			boolean sFlag = true;
			
			try { // Start source node validity check
				if (dGraph.getNode(Integer.parseInt(srcId)) == null)
					throw new RuntimeException();
			} catch (Exception E) {
				JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
				sFlag = false;
			} // End source node validity check

			if (sFlag) {
				String destId = JOptionPane.showInputDialog(this, "Enter destination node");
				try {
					node_data srcNode = dGraph.getNode(Integer.parseInt(srcId));
					node_data destNode = dGraph.getNode(Integer.parseInt(destId));
					int srcKey = srcNode.getKey();
					int destKey = destNode.getKey();
					if (dGraph.getEdge(srcKey, destKey) != null) {
						dGraph.removeEdge(srcKey, destKey);
						myGraph.init(dGraph);
						repaint();
					}
				} catch (Exception E) {
					JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		if (strCommand.equals("Nodes num")) {
			graph dGraph = new DGraph();
			dGraph = myGraph.copy();
			JOptionPane.showMessageDialog(this, dGraph.nodeSize());
		}

		if (strCommand.equals("Edges num")) {
			graph dGraph = new DGraph();
			dGraph = myGraph.copy();
			JOptionPane.showMessageDialog(this, dGraph.edgeSize());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("mouseClicked");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		Point3D p3D = new Point3D(x,y);
		graph dGraph = myGraph.copy();
		Node newNode = new Node(dGraph.nodeSize() + 1, p3D);
		dGraph.addNode(newNode);
		myGraph.init(dGraph);
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Nothing
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Nothing
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Nothing
	}
}
