package wb;

import java.util.List;
import java.util.ArrayList;
import java.awt	.Point;
import java.util.Random;
import java.lang.Math;
import java.util.Iterator;
public class MctsTree{
	private Board seed;
	private Board sandbox;
	private Node root;
	private List<Point> walkedWithoutPush;
	private List<Crate> originalCrates;
	private Tile[][] seedTiles;
	private Point playerStart;

	private Random rand = new Random();

	public MctsTree(Board seed){
		this.seed = seed;
		sandbox = seed.clone();
//		System.out.println(sandbox);
		root = new Node(null);
		root.addOptions();
		walkedWithoutPush = new ArrayList<Point>();
		originalCrates = new ArrayList<Crate>();
		for (Crate c : seed.getCrates()){
			int index = 0;
			for (Crate added : originalCrates){
				if(added.getCoord().x > c.getCoord().x){
					break;
				}
				if (added.getCoord().x == c.getCoord().x){
					if(added.getCoord().y > c.getCoord().y){
						break;
					}
				}
				index++;
			}
			
			originalCrates.add(index,new Crate(seed, c.getCoord()));
		}

//		for (Crate c : originalCrates){
//			System.out.println(c.getCoord());
//		}
		playerStart = seed.getPlayers().get(0).getCoord();

	}

	public Board scrambleRecurse(){
		//System.out.println(seed);
		// Start of MCTS search (tree is set up)
		//int optionIndex = rand.nextInt(Integer.MAX_VALUE)%root.getChildren().size();
		
		//Point currentPoint = player.getCoord();
		//walkedWithoutPush.add(currentPoint);
		// Need to roll out here

		int numIterations = 0;
		while(numIterations < 1000){
			Player player = sandbox.getPlayers().get(0);
//			System.out.println("###################");
//			System.out.println("######   " + numIterations + "   ######");
//			System.out.println("###################");
//			//System.out.println(seed);
//			System.out.println("###################");
			mctsSearch(root, player);
			numIterations++;
			seedReset();
			
		}
		//takeAction(root.getChildren().get(1), player);
		
		return seed;
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
			if (score > 0.8){
				setGoalPositions();
				cratesToWall();

				//FileIO.saveGame(sandbox, Double.toString(score));
				//System.out.println(seed);
				//System.out.println(sandbox);
				wallsToCrate();
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
		wallsToCrate();
		//System.out.println(sandbox);
		//System.out.println(congestion);
		return Math.sqrt(congestion*terrain)/10; 
	}

	private void cratesToWall(){
		int i = 0;
		List<Crate> newCrates = sandbox.getCrates();
		for (Crate c : newCrates){
			if (c.getCoord().equals(originalCrates.get(i).getCoord())){
				sandbox.setPosition(c.getCoord(), new Wall(c.getCoord()));	
			}
			i++;
		}
	}

	private void wallsToCrate(){
		int i = 0;
		List<FloorTile> crateTiles = new ArrayList<FloorTile>();
		for (Crate c : sandbox.getCrates()){
			FloorTile tile = new FloorTile(c.getCoord());
			
			crateTiles.add(tile);	
		}

		for (FloorTile t : crateTiles){
			sandbox.setPosition(t.getCoord(), t);
			sandbox.getPosition(t.getCoord()).setContents(sandbox.getCrates().get(i));
			i++;
		}
	}

	private int getCongestionMetric(){
		int i = 0;
		int numBoxes = 0;
		int numGoals = 0;
		int numWalls = 0;
		for (Crate c : sandbox.getCrates()){
			if (sandbox.getPosition(c.getCoord()).canBeFilled()){
				//This crate still exists on the board
				Crate original = originalCrates.get(i);
				Point startPoint = original.getCoord();
				Point endPoint = c.getCoord();
				int minX = startPoint.x;
				int maxX = endPoint.x;
				int minY = startPoint.y;
				int maxY = endPoint.y;
				if (minX > endPoint.x){
					minX = endPoint.x;
					maxX = startPoint.x;
				}
				if (minY > endPoint.y){
					minY = endPoint.y;
					maxY = startPoint.y;
				}
				//System.out.println(startPoint);
				//System.out.println(endPoint);

				//System.out.println("ok?");
				for (int x = minX; x <= maxX; x++){
					for (int y = minY; y <= maxY; y++){
						//System.out.println(x + ", " + y);
						Point testing = new Point(x, y);
						if (!sandbox.getPosition(testing).canBeFilled()){
							numWalls++;
						}else if(sandbox.getPosition(testing).getContents() != null && sandbox.getPosition(testing).getContents().getType() == 1){
							numGoals++;
						}else{
							for (Crate cr : originalCrates){
								if(cr.getCoord().equals(testing)){
									numBoxes++;
								}
							}
						}
					}
				}
				//System.out.println("numBoxes = " + numBoxes);
				//System.out.println("numGoals = " + numGoals);
				//System.out.println("numWalls = " + numWalls);

			}
			i++;
		}
		return numBoxes + numGoals + numWalls;
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
		for (Crate c : sandbox.getCrates()){
			Tile goal = sandbox.getPosition(c.getCoord());
			goal.setContents(null);
			sandbox.addFinishTile((FloorTile)goal);
		}

		for (Player p : sandbox.getPlayers()){
			sandbox.getPosition(p.getCoord()).setContents(null);
			p.setCoord(playerStart);
			Tile playerTile = sandbox.getPosition(playerStart);
			playerTile.setContents(p);
		}

		for (Crate c : originalCrates){
			//System.out.println(c);
			Tile crateStart = sandbox.getPosition(c.getCoord());
			crateStart.setContents(c);
		}

		

	}

}