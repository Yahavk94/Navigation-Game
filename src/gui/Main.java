package gui;
import algorithms.Graph_Algo;
import dataStructure.DGraph;

public class Main {
	public static void main(String[] args) {
		DGraph dG = new DGraph();
		
		Graph_Algo newGraph = new Graph_Algo();
		newGraph.init(dG);  // Initialize newGraph using dG

		
		Graph_GUI window = new Graph_GUI(newGraph);
		window.setVisible(true);
	}
}
