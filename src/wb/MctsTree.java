package wb;

import java.util.List;
import java.util.ArrayList;
import java.awt	.Point;
import java.util.Random;
import java.lang.Math;
import java.util.Iterator;
public class MctsTree{

	private final int depthLimit = 1800;

	private Board seed;
	private Board sandbox;
	private double bestScore;
	private Board currentBest;
	private Node root;
	private Point playerStart;

	private int alpha;
	private int beta;
	private int gamma;
	private int tau;

	private Random rand = new Random();

	public MctsTree(Board seed, int alpha, int beta, int gamma, int tau){
		this.alpha = alpha;
		this.beta = beta;
		this.gamma = gamma;
		this.tau = tau;

		this.seed = seed;

		sandbox = seed.clone();
		root = new Node(null);
		root.addOptions();

		playerStart = seed.getPlayers().get(0).getCoord();

	}

	public double getBestScore() {
		return this.bestScore;
	}

	public Board scrambleRecurse(){
		// Start of MCTS search (tree is set up)

		// Need to roll out here
		int numIterations = 0;
		while(numIterations < depthLimit){
			Player player = sandbox.getPlayers().get(0);
			mctsSearch(root, player);
			numIterations++;
			seedReset();	
		}

		return currentBest;
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
				setGoalPositions();
				currentBest = sandbox.clone();
				bestScore = score;
			}
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
//		System.out.println(congestion);
//		System.out.println(terrain);
//		System.out.println("------");
		return Math.sqrt(congestion*terrain);
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
		int numBoxes = 0;
		int numGoals = 0;
		int numWalls = 0;

		for (int i = 0; i < sandbox.getCrates().size(); i++){
			Crate seedCrate = seed.getCrates().get(i);
			Point seedCoord = seedCrate.getCoord();
			Crate sandCrate = sandbox.getCrates().get(i);
			Point sandCoord = sandCrate.getCoord();

			Point start = new Point();
			start.x = seedCoord.x < sandCoord.x ? seedCoord.x : sandCoord.x;
			start.y = seedCoord.y < sandCoord.y ? seedCoord.y : sandCoord.y;

			Point end = new Point();
			end.x = seedCoord.x > sandCoord.x ? seedCoord.x : sandCoord.x;
			end.y = seedCoord.y > sandCoord.y ? seedCoord.y : sandCoord.y;

			for(int x = start.x; x <= end.x; x++) {
				for(int y = start.y; y <= end.y; y++) {
					Point checkThis = new Point();
					checkThis.setLocation(x, y);
					Tile seedTest = seed.getPosition(checkThis);
					Tile sandTest = sandbox.getPosition(checkThis);
					if(!sandTest.canBeFilled())
						numWalls++;
					else {
						if(seedTest.getContents() != null && seedTest.getContents().getType() == 1)
							numBoxes++;
						if(sandTest.getContents() != null && sandTest.getContents().getType() == 1)
							numGoals++;
					}
				}
			}
		}

//		if(numBoxes != 0 || numGoals != 0 || numWalls != 0) {
//			System.out.println("numBoxes = " + numBoxes);
//			System.out.println("numGoals = " + numGoals);
//			System.out.println("numWalls = " + numWalls);
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
		return tau*terrainScore;
	}


	private void seedReset(){
		sandbox = seed.clone();
	}


	private void setGoalPositions(){

		List<Crate> toRemove = new ArrayList<>();
		for(int i = 0; i < seed.getCrates().size(); i++) {
			Crate seedCrate = seed.getCrates().get(i);
			Crate sandCrate = sandbox.getCrates().get(i);
			if(!sandbox.getPosition(sandCrate.getCoord()).canBeFilled()) {
				toRemove.add(sandCrate);
				continue;
			}
			Tile goal = sandbox.getPosition(sandCrate.getCoord());
			Tile crateStart = sandbox.getPosition(seedCrate.getCoord());
			sandbox.addFinishTile((FloorTile)goal);
			sandCrate.setCoord(seedCrate.getCoord());
			crateStart.setContents(sandCrate);
		}

		for(Crate removeThis : toRemove) {
			sandbox.getCrates().remove(removeThis);
		}

		for (Player p : sandbox.getPlayers()){
			sandbox.getPosition(p.getCoord()).setContents(null);
			p.setCoord(playerStart);
			Tile playerTile = sandbox.getPosition(playerStart);
			playerTile.setContents(p);
		}
	}
}