package wb;

import java.util.List;
import java.util.ArrayList;
import java.awt	.Point;
import java.util.Random;
import java.lang.Math;
public class MctsTree{
	private Board seed;
	private Node root;
	private List<Point> walkedWithoutPush;
	private List<Crate> originalCrates;


	private Random rand = new Random();

	public MctsTree(Board seed){
		this.seed = seed;
		root = new Node(null);
		root.addOptions();
		walkedWithoutPush = new ArrayList<Point>();
		originalCrates = new ArrayList<Crate>();
		for (Crate c : seed.getCrates()){

			originalCrates.add(c.clone());
		}

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
		int optionIndex = rand.nextInt(Integer.MAX_VALUE)%root.getChildren().size();
		Player player = seed.getPlayers().get(0);
		Point currentPoint = player.getCoord();
		walkedWithoutPush.add(currentPoint);
		// Need to roll out here

		takeAction(root.getChildren().get(optionIndex), player);
		cratesToWall();
		System.out.println(seed);
		return seed;
	}

	private boolean takeAction(Node actionNode, Player player){
		if (actionNode.getAction() == MctsAction.EVALUATE){
			return true;
		}
		
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
				if (takeAction(actionNode.getChildren().get(nextActionIndex), player)){
					return true;
				}
				tryAgain++;
				tryAgain = tryAgain%actionNode.getChildren().size();
			}
		}
		return false;
		
	}

	private void rollout(Node start){

	}

	private float evaluate(Node nodeState){
		cratesToWall();
		return 0; 
	}

	private void cratesToWall(){
		int i = 0;
		List<Crate> newCrates = seed.getCrates();
		for (Crate c : newCrates){
			if (c.getCoord().equals(originalCrates.get(i).getCoord())){
				seed.setPosition(c.getCoord(), new Wall(c.getCoord()));	
			}
			i++;
		}
	}


	


}