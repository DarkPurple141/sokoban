package wb;

import java.util.List;
import java.util.ArrayList;
import java.awt	.Point;
import java.util.Random;
public class MctsTree{
	private Board seed;
	private Node root;

	private Random rand = new Random();

	public MctsTree(Board seed){
		this.seed = seed;
		root = new Node(null);
		root.addOptions();
	}

	public Board scrambleBoard(){
		Node currentNode = root;
		List<Point> walkedWithoutPush = new ArrayList<Point>();
		walkedWithoutPush.add(seed.getPlayers().get(0).getCoord());
		while (currentNode.getAction() != MctsAction.EVALUATE){
			int childIndex = rand.nextInt(Integer.MAX_VALUE)%currentNode.getChildren().size();
			System.out.println(currentNode.getChildren().get(childIndex));
			int newChildIndex = childIndex;
			while(!seed.getPlayers().get(0).doMove(currentNode.getChildren().get(newChildIndex).getMoveDirection())){

				newChildIndex += 1;
				newChildIndex = newChildIndex % currentNode.getChildren().size();
				System.out.println(currentNode.getChildren().get(newChildIndex));
				if (newChildIndex == childIndex){
					return null;
				}
			}
			currentNode = currentNode.getChildren().get(childIndex);
			

		}
		return seed;
	}

	private void takeAction(Node action){

	}
}