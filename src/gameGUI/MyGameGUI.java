package gameGUI;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.json.JSONException;
import org.json.JSONObject;
import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import gameClient.KML_Logger;
import gameData.Fruit;
import gameData.Robot;
import utils.Point3D;

public class MyGameGUI extends JPanel {
	private game_service myGame;
	private DGraph gameGraph = new DGraph();
	private Graph_Algo autoGameGraph = new Graph_Algo();
	
	// Frame
	private final int X_RANGE = 1200;
	private final int Y_RANGE = 600;
	private final int OFFSET = 100;
	private JPanel gamePanel;

	// Manual game fields
	private Thread manualMoveMario;
	private Thread manualChooseLocation;
	private JButton moveButton;

	// Automatic game fields
	private Thread autoChooseLocation;
	private Thread autoMoveMario;
	private ArrayList<Fruit> gameFruits = new ArrayList<>();

	private BufferedImage appleImage;
	private BufferedImage bananaImage;
	private BufferedImage marioImage;
	private Image backgroundImage;

	private static int robotsNum = 0;
	private static int robotsCounter = 0;
	private int totalGameScore = 0;
	
	// Game mode flags
	private boolean autoMode = false;
	private boolean modeFlag = false;
	
	// KML fields
	private KML_Logger kml;
	private boolean nodeJustOnce = false;
	
	public static void createJFrame() {
		JFrame mainFrame = new JFrame("Th3 M4z3 0F W4z3");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String lNumber = JOptionPane.showInputDialog("Enter level");
		try {
			int levelNumber = Integer.parseInt(lNumber);
			if (!(levelNumber >= 0 && levelNumber < 24)) throw new RuntimeException();
			mainFrame.add(new MyGameGUI(levelNumber));
		} catch (Exception Ex) {
			JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		mainFrame.pack();
		mainFrame.setVisible(true);
		mainFrame.setLocationRelativeTo(null);
	}
	
	public MyGameGUI(int gameNumber) {
		gamePanel = new JPanel();
		gamePanel.setPreferredSize(new Dimension(X_RANGE, Y_RANGE));
		moveButton = new JButton("Move");
		gamePanel.add(moveButton);
		gamePanel.add(Box.createHorizontalGlue());
		this.add(gamePanel, BorderLayout.SOUTH);
		backgroundImage = Toolkit.getDefaultToolkit().createImage("Background.jpg");
		kml = new KML_Logger(gameNumber);
		
		// Manual game threads
		manualChooseLocation = new Thread() {
			public void run() {
				while (robotsCounter < robotsNum) {
					String srcNode = JOptionPane.showInputDialog("Enter source node");
					try {
						int sourceNode = Integer.parseInt(srcNode);
						if (gameGraph.getNode(sourceNode) == null) throw new RuntimeException();
						myGame.addRobot(sourceNode);
						repaint();
						robotsCounter++;
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}

				myGame.startGame();
				manualMoveMario.start();
			}
		};

		manualMoveMario = new Thread() {
			public void run() {
				JSONObject getManualGameScore;
				long start = System.currentTimeMillis();
				while (myGame.isRunning()) {
					if (System.currentTimeMillis() - start > 30) {
						try {
							getManualGameScore = new JSONObject(myGame.toString());
							JSONObject manualGameScore = getManualGameScore.getJSONObject("GameServer");
							totalGameScore = manualGameScore.getInt("grade");
							myGame.move();
						} catch (Exception e) {
							System.out.println("Exception");
						}

						start = System.currentTimeMillis();
						repaint();
					}
				}
			}
		};

		// Automatic game threads
		autoChooseLocation = new Thread() {
			public void run() {
				gameFruits = Automated.getGameFruits(gameFruits, gameGraph, myGame);
				while (robotsCounter < robotsNum) {
					Fruit bestFruit = Automated.getBestFruit(gameFruits);

					// Choose the best location based on the greatest fruits values
					int autoSrcNode;
					if (bestFruit.getType() == 1)
						autoSrcNode = bestFruit.getEdge().getSrc(); // Apple
					else
						autoSrcNode = bestFruit.getEdge().getDest(); // Banana
					myGame.addRobot(autoSrcNode);

					gameFruits = Automated.removeBestFruit(gameFruits, bestFruit);
					repaint();
					robotsCounter++;
				}

				myGame.startGame();
				autoMoveMario.start();
			}
		};

		autoMoveMario = new Thread() {
			public void run() {
				JSONObject getAutoGameScore;
				long start = System.currentTimeMillis();
				while (myGame.isRunning()) {
					if (System.currentTimeMillis() - start > 30) {
						try {
							gameFruits.clear();
							gameFruits = Automated.getGameFruits(gameFruits, gameGraph, myGame);
							getAutoGameScore = new JSONObject(myGame.toString());
							JSONObject autoGameScore = getAutoGameScore.getJSONObject("GameServer");
							totalGameScore = autoGameScore.getInt("grade");
							
							for (String autoRobot : myGame.getRobots()) {
								JSONObject autoGameString = new JSONObject(autoRobot);
								JSONObject autoGameRobot = autoGameString.getJSONObject("Robot");
								int robotSN = autoGameRobot.getInt("id");
								int autoRobotSrc = autoGameRobot.getInt("src");
								int autoRobotDest = autoGameRobot.getInt("dest");
								
								if (autoRobotDest == -1) {
									autoRobotDest = Automated.getNext(gameFruits, gameGraph, autoRobotSrc);
									myGame.chooseNextEdge(robotSN, autoRobotDest);
								}
							}

							myGame.move();
						} catch (Exception e) {
							System.out.println("Exception");
						}

						start = System.currentTimeMillis();
						repaint();
					}
				}
				
				int n = JOptionPane.showConfirmDialog(null, "Export to KML?" ,"Export" , JOptionPane.YES_NO_OPTION);
	            if (n == JOptionPane.YES_OPTION){
	                kml.closeDocument();
	                System.exit(0);
	            }
	            
	            else {
	                System.exit(0);
	            }
			}
		};

		// Initialize gameGraph
		myGame = Game_Server.getServer(gameNumber);
		gameGraph.init(myGame.getGraph());

		// Extract robots number from JSON
		JSONObject getGame;
		try {
			getGame = new JSONObject(myGame.toString());
			JSONObject gameServer = getGame.getJSONObject("GameServer");
			robotsNum = gameServer.getInt("robots");
		} catch(JSONException e) {
			e.printStackTrace();
		}

		// Read fruits and robot images
		try {
			File appleFile = new File("Red.png");
			appleImage = ImageIO.read(appleFile);
			File bananaFile = new File("Yellow.png");
			bananaImage = ImageIO.read(bananaFile);
			File marioFile = new File("Mario.png");
			marioImage = ImageIO.read(marioFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// moveRobot Action Listener
		moveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean vFlag = true;
				String rNumber = JOptionPane.showInputDialog("Enter robot serial number");
				try {
					int robotNumber = Integer.parseInt(rNumber);
					if (robotNumber < 0) throw new RuntimeException();
					if (robotNumber >= robotsNum) throw new RuntimeException();
				} catch (Exception Ex) {
					JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
					vFlag = false;
				}

				if (vFlag) {
					String destNode = JOptionPane.showInputDialog("Enter destination node");
					try {
						int destinationNode = Integer.parseInt(destNode);
						if (gameGraph.getNode(destinationNode) == null) throw new RuntimeException();
						myGame.chooseNextEdge(Integer.parseInt(rNumber), destinationNode);
					} catch (Exception Ex) {
						JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g.drawImage(backgroundImage, 5, 50, 1200, 500, null);
		
		// For scaling
		Collection<node_data> nodesCol = gameGraph.getV();
		double minX = getMinX(nodesCol);
		double maxX = getMaxX(nodesCol);
		double minY = getMinY(nodesCol);
		double maxY = getMaxY(nodesCol);
		
		double invertedSrc, invertedDest;
		
		// Black edges drawl
		for (node_data node : nodesCol) {
			Collection<edge_data> edgesCol = gameGraph.getE(node.getKey());
			for (edge_data edge : edgesCol) {
				Point3D srcLocation = node.getLocation();
				node_data dest = gameGraph.getNode(edge.getDest());
				Point3D destLocation = dest.getLocation();
				g.setColor(Color.BLACK);
				
				double scaledX = scale(srcLocation.x(), minX, maxX, OFFSET, (double)X_RANGE - OFFSET);
				double scaledY = scale(srcLocation.y(), minY, maxY, OFFSET, (double)Y_RANGE - OFFSET);
				
				double scaledXDest = scale(destLocation.x(), minX, maxX, OFFSET, (double)X_RANGE - OFFSET);
				double scaledYDest = scale(destLocation.y(), minY, maxY, OFFSET, (double)Y_RANGE - OFFSET);
				
				invertedSrc = Y_RANGE - scaledY;
				invertedDest = Y_RANGE - scaledYDest;
				
				g2.setStroke(new BasicStroke(2));
				g2.drawLine((int)scaledX + 5, (int)invertedSrc + 5, (int)scaledXDest + 5, (int)invertedDest + 5);
			}
		}

		// Red nodes drawl on top of edges
		for (node_data node : nodesCol) {
			Point3D nodeLocation = node.getLocation();
			g.setColor(Color.RED);
			double scaledX = scale(nodeLocation.x(), minX, maxX, OFFSET, (double)X_RANGE - OFFSET);
			double scaledY = scale(nodeLocation.y() ,minY, maxY, OFFSET, (double)Y_RANGE - OFFSET);
			invertedSrc = Y_RANGE - scaledY;
			g.fillOval((int)scaledX, (int)invertedSrc, 10, 10);
			g.drawString("" +node.getKey(), (int)scaledX, (int)invertedSrc - 5);
			
			if (!nodeJustOnce)
				kml.addNodePlaceMark(node.getLocation().toString());
		}
		
		nodeJustOnce = true;

		// Fruits drawl
		List<String> fruitsList = myGame.getFruits();
		for (String myFruit : fruitsList) {
			Fruit newFruit = new Fruit(myFruit, gameGraph);
			Point3D fruitLocation = newFruit.getPos();
			double fruitX = fruitLocation.x();
			double fruitY = fruitLocation.y();
			fruitX = scale(fruitX, minX, maxX, OFFSET, X_RANGE - OFFSET);
			fruitY = scale(fruitY, minY, maxY, OFFSET, Y_RANGE - OFFSET);
			
			invertedDest = Y_RANGE - fruitY;
			
			if (newFruit.getType() == 1)
				g.drawImage(appleImage, (int)fruitX - 5, (int)invertedDest - 5, 20, 20, this);
			else
				g.drawImage(bananaImage, (int)fruitX - 5, (int)invertedDest - 5, 20, 20, this);
			
			kml.addFruitPlaceMark(newFruit.getType() , newFruit.getPos().toString());
		}

		// Robots drawl
		List<String> robotsList = myGame.getRobots();
		for (String myRobot : robotsList) {
			Robot newRobot = new Robot(myRobot);
			Point3D robotLocation = newRobot.getPos();
			double robotX = robotLocation.x();
			double robotY = robotLocation.y();
			
			robotX = scale(robotX, minX, maxX, OFFSET, X_RANGE - OFFSET);
			robotY = scale(robotY, minY, maxY, OFFSET, Y_RANGE - OFFSET);
			
			invertedDest = Y_RANGE - robotY;
			g.drawImage(marioImage, (int)robotX - 20, (int)invertedDest - 20, 40, 60, this);
			
			kml.addRobotPlaceMark(newRobot.getPos().toString());
		}

		// Remaining time drawl
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		g.setColor(Color.BLACK);
		long timeLeft = myGame.timeToEnd() / 1000;
		if (timeLeft < 10) {
			g.setColor(Color.RED);
			if (timeLeft > 0.05) g.drawString("HURRY UP", X_RANGE - 85, 40);
		}
		
		if (timeLeft < 0.05) g.drawString("GAME OVER", X_RANGE - 100, 20);
		else g.drawString("Remaining time : " +timeLeft, X_RANGE - 150, 20);
		
		// Total score drawl
		g.setColor(Color.BLACK);
		g.drawString("Total game score : " +totalGameScore, X_RANGE - 1190, 20);

		if (!modeFlag) {
			int dialogButton = JOptionPane.showConfirmDialog(null, "Manual?", "Manual or Auto", JOptionPane.YES_NO_OPTION);
			if (dialogButton == JOptionPane.YES_OPTION)
				autoMode = false;

			else {
				autoMode = true;
				autoGameGraph.init(gameGraph);
			}

			modeFlag = true;
		}

		if (!autoMode) {
			if (!manualChooseLocation.isAlive() && !manualMoveMario.isAlive())
				manualChooseLocation.start();
		}

		else {
			if (!autoChooseLocation.isAlive() && !autoMoveMario.isAlive())
				autoChooseLocation.start();
		}
	}

	/**
	 * 
	 * @param data denote some data to be scaled
	 * @param r_min the minimum of the range of your data
	 * @param r_max the maximum of the range of your data
	 * @param t_min the minimum of the range of your desired target scaling
	 * @param t_max the maximum of the range of your desired target scaling
	 * @return
	 */

	private double scale(double data, double r_min, double r_max, double t_min, double t_max) {
		double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
		return res;
	}

	private double getMinX(Collection<node_data> nodes) {
		double min = Double.MAX_VALUE;
		for (node_data node : nodes) {
			double temp = node.getLocation().x();
			if (temp < min)
				min = temp;
		}

		return min;
	}

	private double getMinY(Collection<node_data> nodes) {
		double min = Double.MAX_VALUE;
		for (node_data node : nodes) {
			double temp = node.getLocation().y();
			if (temp < min)
				min = temp;
		}

		return min;
	}

	private double getMaxX(Collection<node_data> nodes) {
		double max = Double.MIN_VALUE;
		for (node_data node : nodes) {
			double temp = node.getLocation().x();
			if (temp > max)
				max = temp;
		}

		return max;
	}

	private double getMaxY(Collection<node_data> nodes) {
		double max = Double.MIN_VALUE;
		for (node_data node : nodes) {
			double temp = node.getLocation().y();
			if (temp > max)
				max = temp;
		}

		return max;
	}
}
