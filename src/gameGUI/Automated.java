package gameGUI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.node_data;
import gameData.Fruit;

public class Automated {
	public static ArrayList<Fruit> getGameFruits(ArrayList<Fruit> gameFruits, DGraph gameGraph, game_service myGame) {
		List<String> fruitsList = myGame.getFruits();
		for (String myFruit : fruitsList)
			gameFruits.add(new Fruit(myFruit, gameGraph));

		return gameFruits;
	}

	public static Fruit getBestFruit(ArrayList<Fruit> gameFruits, game_service myGame) {
		Fruit bestFruit = gameFruits.get(0);
		for (Fruit myFruit : gameFruits)
			if (myFruit.getValue() > bestFruit.getValue())
				bestFruit = myFruit;

		return bestFruit;
	}

	public static ArrayList<Fruit> removeBest(ArrayList<Fruit> gameFruits, game_service myGame, Fruit delFruit) {
		for (Fruit myFruit : gameFruits) {
			if (delFruit.getValue() == myFruit.getValue()) {
				gameFruits.remove(myFruit);
				return gameFruits;
			}
		}

		return gameFruits;
	}
	
	public static int getNext(ArrayList<Fruit> gameFruits, DGraph gameGraph, game_service myGame, int srcNode) {
		Graph_Algo myGraph = new Graph_Algo();
		myGraph.init(gameGraph);
		List<Integer> robotPath = new LinkedList<>();
		robotPath.add(srcNode);
		double maxProfit = 0;
		int nextNode = srcNode;

		for (Fruit myFruit : gameFruits) {
			double rToFruitSrc = myGraph.shortestPathDist(srcNode, myFruit.getEdge().getSrc());
			double rToFruitDest = myGraph.shortestPathDist(srcNode, myFruit.getEdge().getDest());
			
			if (myFruit.getType() == -1) { // Banana
				if (myGraph.shortestPathDist(srcNode, myFruit.getEdge().getDest()) == 0)
					return myFruit.getEdge().getSrc(); // Collect banana from destination to source
				else if (myFruit.getValue() / rToFruitDest > maxProfit) {
					maxProfit = myFruit.getValue() / rToFruitDest;
					nextNode = myFruit.getEdge().getDest();
				}
			}
			
			else if (myFruit.getType() == 1) { // Apple
				if (myGraph.shortestPathDist(srcNode, myFruit.getEdge().getSrc()) == 0)
					return myFruit.getEdge().getDest(); // Collect apple from source to destination
				else if (myFruit.getValue() / rToFruitSrc > maxProfit) {
					maxProfit = myFruit.getValue() / rToFruitSrc;
					nextNode = myFruit.getEdge().getSrc();
				}
			}
		}

		robotPath.add(nextNode);
		List<node_data> greedyPath = myGraph.TSP(robotPath);
		return greedyPath.get(1).getKey();
	}
}
