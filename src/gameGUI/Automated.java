package gameGUI;
import java.util.ArrayList;
import java.util.List;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.node_data;
import gameData.Fruit;

/**
 * This class represents the automatic game functions the automatic robots are going to use during the game.
 * @author yahav
 *
 */

public class Automated {
	
	/**
	 * Update gameFruits ArrayList during the automatic game
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
	 * Find and return the fruit with the greatest value
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
	 * Remove from gameFruits ArrayList the fruit with the greatest value
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
	 * Find the next node for a specified robot
	 * @param gameFruits
	 * @param gameGraph
	 * @param myGame
	 * @param srcNode
	 * @return nextNode
	 */
	
	public static int getNext(ArrayList<Fruit> gameFruits, DGraph gameGraph, int srcNode) {
		Graph_Algo myGraph = new Graph_Algo();
		myGraph.init(gameGraph);
		double maxProfit = 0;
		int toRemove = 0;
		
		for (int i = 0; i < gameFruits.size(); i++) {
			double rToFruitSrc = myGraph.shortestPathDist(srcNode, gameFruits.get(i).getEdge().getSrc());
			if (gameFruits.get(i).getValue() / rToFruitSrc > maxProfit) {
					maxProfit = gameFruits.get(i).getValue() / rToFruitSrc;
					toRemove = i;
			}
		}
		
		int getSrcNode = gameFruits.get(toRemove).getEdge().getSrc();
		node_data getDestNode = gameGraph.getNode(gameFruits.get(toRemove).getEdge().getDest());
		List<node_data> getPath = myGraph.shortestPath(srcNode, getSrcNode);
		getPath.add(getDestNode);
		gameFruits.remove(toRemove);
		return getPath.get(1).getKey();
	}
}
