package gameGUI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import gameData.Fruit;

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
		List<Integer> robotPath = new LinkedList<>();
		robotPath.add(srcNode);
		double maxProfit = 0;
		int nextNode = srcNode;
		int toRemove = 0;
		
		for (int i = 0; i < gameFruits.size(); i++) {
			double rToFruitSrc = myGraph.shortestPathDist(srcNode, gameFruits.get(i).getEdge().getSrc());
			double rToFruitDest = myGraph.shortestPathDist(srcNode, gameFruits.get(i).getEdge().getDest());
			
			double reduction = 1;
			
			if (gameFruits.get(i).getType() == -1) { // Banana
				if (rToFruitDest == 0) {
					if (gameFruits.get(i).getValue()*reduction / rToFruitSrc > maxProfit)
						return gameFruits.get(i).getEdge().getSrc(); // Collect banana from destination to source
				}
				
				else if (gameFruits.get(i).getValue()*reduction  / rToFruitDest > maxProfit) {
					maxProfit = gameFruits.get(i).getValue() *reduction / rToFruitDest;
					nextNode = gameFruits.get(i).getEdge().getDest();
					toRemove = i;
				}
			}
			
			else if (gameFruits.get(i).getType() == 1) { // Apple
				if (rToFruitSrc == 0) {
					if (gameFruits.get(i).getValue()*reduction  / rToFruitDest > maxProfit)
						return gameFruits.get(i).getEdge().getDest(); // Collect apple from source to destination
				}
				
				else if (gameFruits.get(i).getValue()*reduction  / rToFruitSrc > maxProfit) {
					maxProfit = gameFruits.get(i).getValue()*reduction  / rToFruitSrc;
					nextNode = gameFruits.get(i).getEdge().getSrc();
					toRemove = i;
				}
			}
		}

		robotPath.add(nextNode);
		gameFruits.remove(toRemove);
		List<node_data> greedyPath = myGraph.TSP(robotPath);
		return greedyPath.get(1).getKey();
	}
	
	public static int getNextFruit(ArrayList<Fruit> gameFruits, DGraph gameGraph, int srcNode, boolean dest) {
		Graph_Algo myGraph = new Graph_Algo();
		myGraph.init(gameGraph);
		List<node_data> minPath = new ArrayList<>();
		Fruit toDelete = null;
		
		double min = Double.MAX_VALUE;
		for (Fruit fruit : gameFruits) {
			if (fruit.getEdge().getSrc() == 32 && fruit.getEdge().getDest() == 33) {
				continue;
			}
			
			List<Integer> list = new ArrayList<>();
			list.add(srcNode);
			list.add(fruit.getEdge().getSrc());
			list.add(fruit.getEdge().getDest());
			
			List<node_data> tspList = myGraph.TSP(list);
			double sum = 0;
			
			Iterator<node_data> iter = tspList.iterator();
			int source = iter.next().getKey();
			
			while (iter.hasNext()) {
				int destination = iter.next().getKey();
				edge_data edge = gameGraph.getEdge(source, destination);
				sum += edge.getWeight();
				source = destination;
			}
			
			if (sum < min) {
				min = sum;
				minPath = tspList;
				toDelete = fruit;
			}
		}
		
		gameFruits.remove(toDelete);
		if(!dest)
			return minPath.get(1).getKey();
		else
			return minPath.get(0).getKey();
	}
}
