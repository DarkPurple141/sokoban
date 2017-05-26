package wb;

import java.awt.Point;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Monte-Carlo tree-sim.
 *
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
class MctsTree {
	// FIXME(jashankj): static?
	private final int depthLimit = 1800;

	private Board seed;
	private Board sandbox;
	private double bestScore;
	private Board currentBest;
	private MctsNode root;
	private Point playerStart;

	private int alpha;
	private int beta;
	private int gamma;
	private int tau;

	private Random rand = new Random();

	/**
	 * 
	 * Constructor for the intial MCTS algorithm
	 * Initialises a base node (with no action)
	 * The heuristic parameters are used in board evaluation
	 * 
	 * @param seed : The seed board that the shuffling will occur on
	 * @param alpha : The heuristic scaling of the number of boxes
	 * @param beta : The heuristic scaling of the number of goals
	 * @param gamma : The heuristic scaling of the number of walls
	 * @param tau : The heuritic scaling of the terrain metric
	 */
	public MctsTree(Board seed, int alpha, int beta, int gamma, int tau){
		this.alpha = alpha;
		this.beta = beta;
		this.gamma = gamma;
		this.tau = tau;

		this.seed = seed;

		sandbox = seed.clone();
		root = new MctsNode(null);
		root.addOptions();

		playerStart = seed.getPlayers().get(0).getCoord();

	}


	/**
	 * 
	 * @return The best score seen during the shuffling of the current seed
	 */
	public double getBestScore() {
		return this.bestScore;
	}


	/**
	 * 
	 * Begins the shuffling of the board by following a Monte Carlo Tree Search
	 * The player randomly shuffles crates around on a seed board, saving the
	 * finish positions as goals.
	 *
	 * The board with the best heuristic score after shuffling is returned from this method
	 *
	 * @return The board set up with the best heurstic score seen during shuffling
	 */
	public Board scrambleRecurse(){
		int numIterations = 0;
		while(numIterations < depthLimit){
			Player player = sandbox.getPlayers().get(0);
			mctsSearch(root, player);
			numIterations++;
			seedReset();
		}

		return currentBest;
	}


	/**
	 * 
	 * The actual running of the MCTS search. 
	 * Follows the process of 'rolling out' on unvisited nodes and then 
	 * takes the best child node and continues the process. Runs recursively
	 * 
	 * @return true when the rollout on a child node resulted in a valid configuration 
	 * @param actionNode : The current node that is being visited
	 * @param player : The player object on the sandbox board
	 */
	private boolean mctsSearch(MctsNode actionNode, Player player){
		double maxScore = 0;
		MctsNode bestNode = null;
		actionNode.visited();
		for (MctsNode child : actionNode.getChildren()){
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


	/**
	 * 
	 * Takes a particular node's action
	 * 
	 * @param actionNode : The node that is being visited and hence whose action is being taken
	 * @param player : The player object on the sandbox board
	 */
	private void takeAction(MctsNode actionNode, Player player){
		if (actionNode.getAction() == MctsAction.MOVE){
			player.doMove(actionNode.getMoveDirection());
		}else{
			evaluate();
		}
	}

	/**
	 * 
	 * Randomly moves the player around a board until the evaluation action is chosen
	 * Updates the actionNode's score
	 * 
	 * @param actionNode : The node that is being rolled out on
	 */
	private void rollout(MctsNode actionNode, Player player){
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
			while(!player.doMove(Direction.int2Dir(nextMove))){
				nextMove++;
				nextMove = nextMove%4;
				if (nextMove == firstTry){
					return;
				}
			}
			rollout(actionNode, player);
		}
	}


	/**
	 * 
	 * Runs the evaluation process on the current sandbox board
	 * 
	 * @return The heuristic score of the current sandbox board
	 */
	private double evaluate(){
		cratesToWall();
		int congestion = getCongestionMetric();
		int terrain = getTerrainMetric();
		return Math.sqrt(congestion*terrain);
	}


	/**
	 * 
	 * Turns the crates that have not been moved at all from their starting positions to walls
	 * in the sandbox board
	 * 
	 * @param parent : The new node's parent node
	 */
	private void cratesToWall(){
		int sandboxSize = sandbox.getCrates().size();
		for(int i = 0; i < sandboxSize; i++) {
			Crate original = seed.getCrates().get(i);
			Crate finish = sandbox.getCrates().get(i);
			if (original.getCoord().equals(finish.getCoord())) {
				sandbox.setPosition(original.getCoord(), new WallTile(original.getCoord()));
			}
		}
	}


	/**
	 *
	 * Returns a valuation of the current sandbox board going of it's 
	 * congestion
	 *
	 * @return The heuristic value of the board congestion
	 */
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


		return alpha*numBoxes + beta*numGoals + gamma*numWalls;
	}


	/**
	 * 
	 * The heursitic score of the sandbox board based on the terrain
	 * ie the amount of walls in free space etc.
	 * 
	 * @param parent : The new node's parent node
	 */
	private int getTerrainMetric(){
		Iterator<Tile> tileIt = sandbox.tileIterator();
		int terrainScore = 0;
		while (tileIt.hasNext()){
			Tile currentTile = tileIt.next();
			if(currentTile.canBeFilled()){
				for (Direction d : Direction.values()){
					Point nearby = sandbox.nearbyPoint(currentTile.getCoord(), d);
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


	/**
	 * 
	 * Resets the sandbox board the the seed config 
	 *
	 */
	private void seedReset(){
		sandbox = seed.clone();
	}

	/**
	 * 
	 * Turns the final positions of the crates in the sandbox board to
	 * be the goal positions in the board returned by the algorithm
	 */
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
