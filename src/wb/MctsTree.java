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
		System.out.println(sandbox);
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

		for (Crate c : originalCrates){
			System.out.println(c.getCoord());
		}
		playerStart = seed.getPlayers().get(0).getCoord();

	}

	public Board scrambleBoard(){
		Node currentNode = root;
		System.out.println(seed);
		int numPushed = 0;
		List<Point> walkedWithoutPush = new ArrayList<Point>();
		Player player = seed.getPlayers().get(0);
		walkedWithoutPush.add(player.getCoord());
		while (numPushed < 3){
			while (currentNode.getAction() != MctsAction.EVALUATE){
				int childIndex = rand.nextInt(Integer.MAX_VALUE)%currentNode.getChildren().size();
				int newChildIndex = childIndex;
				Node actionNode = currentNode.getChildren().get(newChildIndex);
				boolean tryingToPush = false;
				if (actionNode.getAction() == MctsAction.MOVE){
					Point walkedTo = seed.nearbyPoint(player.getCoord(), actionNode.getMoveDirection());
					if (seed.getPosition(walkedTo) != null && seed.getPosition(walkedTo).getContents() != null){
						walkedWithoutPush = new ArrayList<Point>();
						tryingToPush = true;
					}
				}

				while(!player.doMove(currentNode.getChildren().get(newChildIndex).getMoveDirection())){

					newChildIndex += 1;
					newChildIndex = newChildIndex % currentNode.getChildren().size();
					if (newChildIndex == childIndex){
						return null;
					}
					actionNode = currentNode.getChildren().get(newChildIndex);
					if (actionNode.getAction() == MctsAction.MOVE){
						Point walkedTo = seed.nearbyPoint(player.getCoord(), actionNode.getMoveDirection());
						if (seed.getPosition(walkedTo) != null && seed.getPosition(walkedTo).getContents() != null){
							walkedWithoutPush = new ArrayList<Point>();
							tryingToPush = true;
						}else{
							tryingToPush = false;
						
						}
					}else{
						tryingToPush = false;
						break;
					}

				}
				if (tryingToPush){
					numPushed++;
					System.out.println(seed);
				}
				currentNode = currentNode.getChildren().get(childIndex);
				currentNode.addOptions();
			}
			if (numPushed < 3){
				int newChildIndex = rand.nextInt(Integer.MAX_VALUE)%currentNode.getChildren().size();
				currentNode = currentNode.getChildren().get(newChildIndex);
				currentNode.addOptions();
			}
		}
		System.out.println("##############################");
		System.out.println(seed);
		return seed;
	}

	public Board scrambleRecurse(){
		System.out.println(seed);
		// Start of MCTS search (tree is set up)
		//int optionIndex = rand.nextInt(Integer.MAX_VALUE)%root.getChildren().size();
		
		//Point currentPoint = player.getCoord();
		//walkedWithoutPush.add(currentPoint);
		// Need to roll out here

		int numIterations = 0;
		while(numIterations < 10){
			Player player = sandbox.getPlayers().get(0);
			System.out.println("###################");
			System.out.println("######   " + numIterations + "   ######");
			System.out.println("###################");
			System.out.println(seed);
			System.out.println("###################");
			mctsSearch(root, player);
			numIterations++;
			seedReset();
			
		}
		//takeAction(root.getChildren().get(1), player);
		
		return seed;
	}

	private boolean mctsSearch(Node actionNode, Player player){
		float maxScore = 0;
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
			if (maxScore < child.getScore() || bestNode == null){
				maxScore = child.getScore();
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

	/*private boolean takeAction(Node actionNode, Player player){
		actionNode.visited();
		
		if (actionNode.getAction() == MctsAction.EVALUATE){
			evaluate();
			return true;
		}
		System.out.println(actionNode.getMoveDirection());
		//Point moveTo = seed.nearbyPoint(actionNode.getMoveDirection());
		if(player.doMove(actionNode.getMoveDirection())){
			actionNode.addOptions();

			int nextActionIndex = rand.nextInt(Integer.MAX_VALUE)%actionNode.getChildren().size();
			if (takeAction(actionNode.getChildren().get(nextActionIndex), player)){
				return true;
			}
			int tryAgain = nextActionIndex + 1;
			tryAgain = tryAgain%actionNode.getChildren().size();
			while (tryAgain != nextActionIndex){
				if (takeAction(actionNode.getChildren().get(tryAgain), player)){
					return true;
				}
				tryAgain++;
				tryAgain = tryAgain%actionNode.getChildren().size();
			}
		}
		System.out.println("Damn by Kendrick l");
		return false;
		
	}*/

	private void takeAction(Node actionNode, Player player){
		 if (actionNode.getAction() == MctsAction.MOVE){
		 	player.doMove(actionNode.getMoveDirection());
		 	return;
		 }else{
		 	evaluate();
		 }
	}

	private void rollout(Node actionNode, Player player){
		int nextMove = rand.nextInt(Integer.MAX_VALUE)%10;
		actionNode.visited();
		//System.out.println(sandbox);
		if (nextMove == 9){

			float score = evaluate();
			actionNode.updateValue(score);
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

	private float evaluate(){

		cratesToWall();
		int congestion = getCongestionMetric();
		wallsToCrate();
		System.out.println(sandbox);
		System.out.println(congestion);
		return congestion; 
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
				System.out.println("numBoxes = " + numBoxes);
				System.out.println("numGoals = " + numGoals);
				System.out.println("numWalls = " + numWalls);

			}
			i++;
		}
		return numBoxes + numGoals + numWalls;
	}

	private void seedReset(){
		sandbox = seed.clone();
	}


	


}