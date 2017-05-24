package wb;

import java.util.List;
import java.util.ArrayList;
import java.awt	.Point;
import java.util.Random;
import java.lang.Math;
import java.util.Iterator;
public class MctsTree{

	private final int depthLimit = 3000;

	private Board seed;
	private Board sandbox;
	private double bestScore;
	private Board currentBest;
	private Node root;
	private List<Point> walkedWithoutPush;
	private Tile[][] seedTiles;
	private Point playerStart;

	private int alpha;
	private int beta;
	private int gamma;

	private Random rand = new Random();

	public MctsTree(Board seed, int alpha, int beta, int gamma){
		this.alpha = alpha;
		this.beta = beta;
		this.gamma = gamma;

		this.seed = seed;
		sandbox = seed.clone();
//		System.out.println(sandbox);
		root = new Node(null);
		root.addOptions();
		//walkedWithoutPush = new ArrayList<Point>();

//		for (Crate c : originalCrates){
//			System.out.println(c.getCoord());
//		}
		playerStart = seed.getPlayers().get(0).getCoord();

	}

	public double getBestScore() {
		return this.bestScore;
	}

	public Board scrambleRecurse(){
		//System.out.println(seed);
		// Start of MCTS search (tree is set up)
		//int optionIndex = rand.nextInt(Integer.MAX_VALUE)%root.getChildren().size();
		
		//Point currentPoint = player.getCoord();
		//walkedWithoutPush.add(currentPoint);
		// Need to roll out here
		int numIterations = 0;
		while(numIterations < depthLimit){
			Player player = sandbox.getPlayers().get(0);
			mctsSearch(root, player);
			numIterations++;
			seedReset();	
		}

		return currentBest;
		//takeAction(root.getChildren().get(1), player);
	}

	private boolean mctsSearch(Node actionNode, Player player){
		double maxScore = 0;
		Node bestNode = null;
		actionNode.visited();
		for (Node child : actionNode.getChildren()){
			if (child.timesVisited() < 1){
				rollout(child, player);
				if (child.getScore() == 0){
					return false;
				}else{
					return true;
				}
			}
			double possibleScore = child.getScore()/child.timesVisited() + 2*Math.sqrt(Math.log(actionNode.timesVisited())/child.timesVisited());
			if (maxScore < possibleScore || bestNode == null){
				maxScore = possibleScore;
				bestNode = child;
			}

		}
		bestNode.addOptions();
		takeAction(bestNode, player);
		if(mctsSearch(bestNode, player)){
			actionNode.updateValue(bestNode.getScore());
		}
		return true;
	}


	private void takeAction(Node actionNode, Player player){
		if (actionNode.getAction() == MctsAction.MOVE){
			player.doMove(actionNode.getMoveDirection());
		}else{
			evaluate();
		}
	}

	private void rollout(Node actionNode, Player player){
		int nextMove = rand.nextInt(Integer.MAX_VALUE)%20;
		actionNode.visited();
		//System.out.println(sandbox);
		if (nextMove == 19){

			double score = evaluate();
			actionNode.updateValue(score);
			if (score > bestScore){
				cratesToWall();
				setGoalPositions();

				//FileIO.saveGame(sandbox, Double.toString(score));
				currentBest = sandbox.clone();

				//System.out.println(seed);
				//System.out.println(sandbox);
				//wallsToCrate();
				bestScore = score;
			}
			//System.out.println(score);
			//actionNode.visited();
			return;
		}else{
			nextMove = nextMove%4;
			int firstTry = nextMove;
			while(!player.doMove(nextMove)){
				nextMove++;
				nextMove = nextMove%4;
				if (nextMove == firstTry){
					return;
				}
			}
			
			rollout(actionNode, player);
		}
	}

	private double evaluate(){

		cratesToWall();
		int congestion = getCongestionMetric();
		int terrain = getTerrainMetric();
		//wallsToCrate();
		//System.out.println(sandbox);
		//System.out.println(congestion);
		return Math.sqrt(congestion*terrain)/10; 
	}

	private void cratesToWall(){
		int sandboxSize = sandbox.getCrates().size();
		for(int i = 0; i < sandboxSize; i++) {
			Crate original = seed.getCrates().get(i);
			Crate finish = sandbox.getCrates().get(i);
			if (original.getCoord().equals(finish.getCoord())) {
				sandbox.setPosition(original.getCoord(), new Wall(original.getCoord()));
			}
		}
	}

//	private void wallsToCrate(){
//		int i = 0;
//		List<FloorTile> crateTiles = new ArrayList<FloorTile>();
//		for (Crate c : sandbox.getCrates()){
//			FloorTile tile = new FloorTile(c.getCoord());
//
//			crateTiles.add(tile);
//		}
//
//		for (FloorTile t : crateTiles){
//			sandbox.setPosition(t.getCoord(), t);
//			sandbox.getPosition(t.getCoord()).setContents(sandbox.getCrates().get(i));
//			i++;
//		}
//	}

	private int getCongestionMetric(){
		int i = 0;
		int numBoxes = 0;
		int numGoals = 0;
		int numWalls = 0;

		for (int i = 0; i < sandbox.getCrates().size(); i++;){
			Crate seedCrate = seed.getCrates().get(i);
			Crate sandCrate = sandbox.getCrates().get(i);

		}
//		for (Crate c : sandbox.getCrates()){//TODO adjust to use i properly
//			if (sandbox.getPosition(c.getCoord()).canBeFilled()){//TODO: Fix this
//				//This crate still exists on the board
//				Crate original = originalCrates.get(i);
//				Point startPoint = original.getCoord();
//				Point endPoint = c.getCoord();
//				int minX = startPoint.x;
//				int maxX = endPoint.x;
//				int minY = startPoint.y;
//				int maxY = endPoint.y;
//				if (minX > endPoint.x){
//					minX = endPoint.x;
//					maxX = startPoint.x;
//				}
//				if (minY > endPoint.y){
//					minY = endPoint.y;
//					maxY = startPoint.y;
//				}
//				//System.out.println(startPoint);
//				//System.out.println(endPoint);
//
//				//System.out.println("ok?");
//				for (int x = minX; x <= maxX; x++){
//					for (int y = minY; y <= maxY; y++){
//						//System.out.println(x + ", " + y);
//						Point testing = new Point(x, y);
//						if (!sandbox.getPosition(testing).canBeFilled()){
//							numWalls++;
//						}else if(sandbox.getPosition(testing).getContents() != null && sandbox.getPosition(testing).getContents().getType() == 1){
//							numGoals++;
//						}else{
//							for (Crate cr : originalCrates){
//								if(cr.getCoord().equals(testing)){
//									numBoxes++;
//								}
//							}
//						}
//					}
//				}
//				//System.out.println("numBoxes = " + numBoxes);
//				//System.out.println("numGoals = " + numGoals);
//				//System.out.println("numWalls = " + numWalls);
//
//			}
//			i++;
//		}
		return alpha*numBoxes + beta*numGoals + gamma*numWalls;
	}


	private int getTerrainMetric(){
		int[] directions = {0,1,2,3};
		Iterator<Tile> tileIt = sandbox.tileIterator();
		int terrainScore = 0;
		while (tileIt.hasNext()){
			Tile currentTile = tileIt.next();
			if(currentTile.canBeFilled()){
				for (int dir : directions){
					Point nearby = sandbox.nearbyPoint(currentTile.getCoord(), dir);
					if (nearby != null){
						Tile neighbour = sandbox.getPosition(nearby);
						if (!neighbour.canBeFilled()){
							terrainScore++;
						}
					}
				}
			}
		}
		return terrainScore;
	}


	private void seedReset(){
		sandbox = seed.clone();
	}


	private void setGoalPositions(){

		for(int i = 0; i < seed.getCrates().size(); i++) {
			Crate seedCrate = seed.getCrates().get(i);
			Crate sandCrate = sandbox.getCrates().get(i);
			if(!sandbox.getPosition(seedCrate.getCoord()).canBeFilled()) {
				sandbox.getCrates().remove(sandCrate);
				continue;
			}
			Tile goal = sandbox.getPosition(sandCrate.getCoord());
			Tile crateStart = sandbox.getPosition(seedCrate.getCoord());
			sandbox.addFinishTile((FloorTile)goal);
			sandCrate.setCoord(seedCrate.getCoord());
			crateStart.setContents(sandCrate);
		}

//		for(Crate c : seed.getCrates()) {
//			Tile correspond = sandbox.getPosition(c.getCoord());
//			if(!correspond.canBeFilled()) {
//				sandbox.getCrates().remove(correspond.getContents());
//				continue;
//			}
//
//		}
//
//		for (Crate c : sandbox.getCrates()){
//			Tile goal = sandbox.getPosition(c.getCoord());
//			goal.setContents(null);
//			sandbox.addFinishTile((FloorTile)goal);
//		}
//
//		for (Crate c : seed.getCrates()) {
//			Tile crateStart = sandbox.getPosition(c.getCoord());
//			crateStart.setContents(c);
//		}

		for (Player p : sandbox.getPlayers()){
			sandbox.getPosition(p.getCoord()).setContents(null);
			p.setCoord(playerStart);
			Tile playerTile = sandbox.getPosition(playerStart);
			playerTile.setContents(p);
		}
	}
}