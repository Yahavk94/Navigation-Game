package gameGUI;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import gameData.Fruit;

class AutomatedTest {

	@Test
	void testGetGameFruits() {
		DGraph gameGraph = new DGraph();
		game_service myGame = Game_Server.getServer(0);
		gameGraph.init(myGame.getGraph());
		List<String> fruitsList = myGame.getFruits();
		assertEquals(fruitsList.size(), 1);
		Fruit newFruit = new Fruit(fruitsList.get(0), gameGraph);
		assertEquals(newFruit.getType(), -1); // Banana
	}
	
	void testGetBestFruit() {
		DGraph gameGraph = new DGraph();
		game_service myGame = Game_Server.getServer(0);
		gameGraph.init(myGame.getGraph());
		ArrayList<Fruit> emptyList = new ArrayList<>();
		ArrayList<Fruit> gameFruits = Automated.getGameFruits(emptyList, gameGraph, myGame);
		assertEquals(gameFruits.get(0), Automated.getBestFruit(gameFruits));
	}
	
	void removeBestFruit() {
		DGraph gameGraph = new DGraph();
		game_service myGame = Game_Server.getServer(0);
		gameGraph.init(myGame.getGraph());
		ArrayList<Fruit> emptyList = new ArrayList<>();
		ArrayList<Fruit> gameFruits = Automated.getGameFruits(emptyList, gameGraph, myGame);
		gameFruits = Automated.removeBestFruit(gameFruits, gameFruits.get(0));
		assertEquals(gameFruits.size(), 0);
	}
}
