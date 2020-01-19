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
	
	/**
	 * Update gameFruits ArrayList during the game
	 * @param gameFruits
	 * @param gameGraph
	 * @param myGame
	 * @return gameFruits
	 */
	
	public static ArrayList<Fruit> getGameFruits(ArrayList<Fruit> gameFruits, DGraph gameGraph, game_service myGame) {
		List<String> fruitsList = myGame.getFruits();
		for (String myFruit : fruitsList)
			gameFruits.add(new Fruit(myFruit, gameGraph));

		return gameFruits;
	}
	
	/**
	 * Find and return the greatest value fruit
	 * @param gameFruits
	 * @param myGame
	 * @return bestFruit
	 */

	public static Fruit getBestFruit(ArrayList<Fruit> gameFruits) {
		Fruit bestFruit = gameFruits.get(0);
		for (Fruit myFruit : gameFruits)
			if (myFruit.getValue() > bestFruit.getValue())
				bestFruit = myFruit;

		return bestFruit;
	}
	
	/**
	 * Remove from gameFruits ArrayList the greatest value fruit
	 * @param gameFruits
	 * @param myGame
	 * @param delFruit
	 * @return gameFruits
	 */

	public static ArrayList<Fruit> removeBestFruit(ArrayList<Fruit> gameFruits, Fruit delFruit) {
		for (Fruit myFruit : gameFruits) {
			if (delFruit.getValue() == myFruit.getValue()) {
				gameFruits.remove(myFruit);
				return gameFruits;
			}
		}

		return gameFruits;
	}
	
	/**
	 * Find the next node for a specified robot using greedy method
	 * @param gameFruits
	 * @param gameGraph
	 * @param myGame
	 * @param srcNode
	 * @return nextNode
	 */
	
	public static int getNext(ArrayList<Fruit> gameFruits, DGraph gameGraph, int srcNode) {
		Graph_Algo myGraph = new Graph_Algo();
		myGraph.init(gameGraph);
		List<Integer> robotPath = new LinkedList<>();
		robotPath.add(srcNode);
		double maxProfit = 0;
		int nextNode = srcNode;
		int toRemove = 0;
		
		for (int i = 0; i < gameFruits.size(); i++) {
			double rToFruitSrc = myGraph.shortestPathDist(srcNode, gameFruits.get(i).getEdge().getSrc());
			double rToFruitDest = myGraph.shortestPathDist(srcNode, gameFruits.get(i).getEdge().getDest());
			
			if (gameFruits.get(i).getType() == -1) { // Banana
				if (rToFruitDest == 0) {
					if (gameFruits.get(i).getValue() / rToFruitSrc > maxProfit)
						return gameFruits.get(i).getEdge().getSrc(); // Collect banana from destination to source
				}
				
				else if (gameFruits.get(i).getValue() / rToFruitDest > maxProfit) {
					maxProfit = gameFruits.get(i).getValue() / rToFruitDest;
					nextNode = gameFruits.get(i).getEdge().getDest();
					toRemove = i;
				}
			}
			
			else if (gameFruits.get(i).getType() == 1) { // Apple
				if (rToFruitSrc == 0) {
					if (gameFruits.get(i).getValue() / rToFruitDest > maxProfit)
						return gameFruits.get(i).getEdge().getDest(); // Collect apple from source to destination
				}
				
				else if (gameFruits.get(i).getValue() / rToFruitSrc > maxProfit) {
					maxProfit = gameFruits.get(i).getValue() / rToFruitSrc;
					nextNode = gameFruits.get(i).getEdge().getSrc();
					toRemove = i;
				}
			}
		}

		robotPath.add(nextNode);
		//gameFruits.remove(toRemove);
		List<node_data> greedyPath = myGraph.TSP(robotPath);
		return greedyPath.get(1).getKey();
	}
}
